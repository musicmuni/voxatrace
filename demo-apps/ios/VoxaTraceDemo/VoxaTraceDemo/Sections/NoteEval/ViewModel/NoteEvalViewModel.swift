import Foundation
import Combine
import VoxaTrace

/// ViewModel for note/exercise evaluation using CalibraNoteEval with singalong mode.
///
/// ## VoxaTrace Integration
/// ```swift
/// // 1. Synthesize reference notes
/// let synth = SonixMidiSynthesizer.Builder().soundFontPath(path: sfPath).build()
/// synth.synthesizeFromNotes(notes: midiNotes, outputPath: outputPath)
///
/// // 2. Play reference + record student simultaneously (singalong)
/// player = SonixPlayer.create(source: referencePath, audioSession: .playAndRecord)
/// recorder = SonixRecorder.create(outputPath: path, config: .voice, audioSession: .playAndRecord)
///
/// // 3. Extract pitch and evaluate
/// let extractor = CalibraPitch.createContourExtractor()
/// let contour = extractor.extract(audio: studentAudio, sampleRate: 16000)
/// let result = CalibraNoteEval.evaluate(pattern:, student:, referenceKeyHz:, ...)
/// ```
@MainActor
final class NoteEvalViewModel: ObservableObject {

    // MARK: - Published State

    @Published private(set) var selectedExercise = 0
    @Published private(set) var isSingalongActive = false
    @Published private(set) var hasRecording = false
    @Published private(set) var recordingDuration: Float = 0.0
    @Published private(set) var recordingLevel: Float = 0.0
    @Published private(set) var isEvaluating = false
    @Published private(set) var result: ExerciseResult?
    @Published private(set) var status = "Select an exercise and tap Singalong"

    // Synthesis state
    @Published private(set) var isPreparing = false
    @Published private(set) var isReady = false

    // Evaluation settings
    @Published var selectedPreset: NoteEvalPreset = .balanced
    @Published var noteDurationMs: Int32 = 1000

    /// Available note durations (0.5s to 2s)
    let availableDurations: [Int32] = [500, 1000, 1500, 2000]

    // Exercise definitions: (name, MIDI notes, key MIDI note)
    let exercises: [(String, [Int32], Int32)] = [
        ("C Major Scale (ascending)", [48, 50, 52, 53, 55, 57, 59, 60], 48),
        ("C Major Scale (descending)", [60, 59, 57, 55, 53, 52, 50, 48], 48),
        ("C Minor Scale", [48, 50, 51, 53, 55, 56, 58, 60], 48),
        ("C Major Arpeggio", [48, 52, 55, 60], 48),
        ("G Major Scale", [55, 57, 59, 60, 62, 64, 66, 67], 55),
        ("Sa Re Ga (C)", [48, 50, 52], 48),
        ("Sa Re Ga Ma Pa (G)", [55, 57, 59, 60, 62], 55)
    ]

    // MARK: - Computed Properties

    var currentExerciseName: String {
        exercises[selectedExercise].0
    }

    var currentMidiNotes: [Int32] {
        exercises[selectedExercise].1
    }

    var currentKeyMidi: Int32 {
        exercises[selectedExercise].2
    }

    // MARK: - Private

    private var recorder: SonixRecorder?
    private var collectedAudio: [Float] = []

    // Singalong
    private var referencePlayer: SonixPlayer?
    private var playerObserverTask: Task<Void, Never>?
    private var synthesizedOutputPath: URL?
    private var lastSynthesizedExercise: Int = -1
    private var lastSynthesizedDuration: Int32 = -1

    // MARK: - Lifecycle

    func onDisappear() {
        cleanup()
    }

    // MARK: - Actions

    func selectExercise(_ index: Int) {
        selectedExercise = index
        result = nil
        hasRecording = false
        // Mark reference as needing re-synthesis if exercise changed
        if index != lastSynthesizedExercise {
            isReady = false
        }
        status = "Selected: \(exercises[index].0)"
    }

    /// Update note duration and invalidate reference if needed.
    func setNoteDuration(_ ms: Int32) {
        noteDurationMs = ms
        // Force re-synthesis with new duration
        if ms != lastSynthesizedDuration {
            isReady = false
        }
    }

    // MARK: - Singalong Actions

    /// Prepare the singalong session by synthesizing reference audio.
    func prepare() {
        guard !isPreparing else { return }

        isPreparing = true
        status = "Preparing..."

        Task {
            await synthesizeReferenceIfNeeded()
            await loadPlayerForSingalong()

            await MainActor.run {
                isPreparing = false
                if isReady {
                    status = "Ready! Tap Singalong to start."
                }
            }
        }
    }

    /// Start singalong: play reference and record simultaneously.
    func startSingalong() {
        guard isReady else {
            // Auto-prepare and start
            Task {
                prepare()
                while isPreparing {
                    try? await Task.sleep(nanoseconds: 100_000_000)
                }
                if isReady {
                    startSingalong()
                }
            }
            return
        }

        collectedAudio = []
        hasRecording = false
        result = nil
        recordingDuration = 0.0
        status = "Sing along with the notes..."

        // Create recorder for simultaneous playback+recording with echo cancellation
        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("note_eval_temp.m4a").path
        let recorderConfig = SonixRecorderConfig.Builder()
            .preset(.voice)
            .echoCancellation(true)
            .build()
        recorder = SonixRecorder.create(outputPath: tempPath, config: recorderConfig, audioSession: .playAndRecord)

        isSingalongActive = true

        // Start collecting audio
        Task {
            let hwRate = AudioSessionManager.hardwareSampleRate
            var sampleCount = 0

            guard let recorder = recorder else { return }

            for await buffer in recorder.audioBuffers {
                let samples16k = SonixResampler.resample(
                    samples: buffer.samples,
                    fromRate: hwRate,
                    toRate: 16000
                )

                collectedAudio.append(contentsOf: samples16k)
                sampleCount += samples16k.count

                let sum = samples16k.reduce(0) { $0 + $1 * $1 }
                let rms = sqrt(sum / Float(samples16k.count))

                await MainActor.run {
                    recordingLevel = min(rms * 5, 1.0)
                    recordingDuration = Float(sampleCount) / 16000.0
                }
            }
        }

        // Start recorder and player together
        recorder?.start()
        referencePlayer?.play()
    }

    /// Stop singalong session and auto-evaluate.
    func stopSingalong() {
        recorder?.stop()
        referencePlayer?.stop()
        isSingalongActive = false

        if !collectedAudio.isEmpty {
            hasRecording = true
            status = "Recording complete. Evaluating..."
            evaluate()
        } else {
            status = "No audio recorded. Try again."
        }
    }

    // MARK: - Private Synthesis Methods

    private func synthesizeReferenceIfNeeded() async {
        // Skip if already synthesized for this exercise and duration
        let needsResynthesize = selectedExercise != lastSynthesizedExercise ||
                                noteDurationMs != lastSynthesizedDuration
        guard needsResynthesize else { return }

        guard let sfPath = copyAssetToFile(name: "harmonium", ext: "sf2") else {
            await MainActor.run {
                status = "Soundfont not found"
            }
            return
        }

        let midiNotes = currentMidiNotes
        let duration = noteDurationMs

        // Create MidiNote objects with proper timing (times in milliseconds as Float)
        var notes: [MidiNote] = []
        for (index, midi) in midiNotes.enumerated() {
            let startTime = Float(index) * Float(duration)
            let endTime = startTime + Float(duration) - 50 // Small gap between notes
            notes.append(MidiNote(note: midi, startTime: startTime, endTime: endTime))
        }

        let tempDir = FileManager.default.temporaryDirectory
        let outFile = tempDir.appendingPathComponent("note_eval_reference.wav")

        let synth = SonixMidiSynthesizer.Builder()
            .soundFontPath(path: sfPath)
            .sampleRate(rate: 44100)
            .build()

        let success = synth.synthesizeFromNotes(notes: notes, outputPath: outFile.path)

        if success {
            synthesizedOutputPath = outFile
            lastSynthesizedExercise = selectedExercise
            lastSynthesizedDuration = duration
        } else {
            await MainActor.run {
                status = "Synthesis failed"
            }
        }
    }

    private func loadPlayerForSingalong() async {
        guard let outputPath = synthesizedOutputPath else { return }

        playerObserverTask?.cancel()
        referencePlayer?.release()

        do {
            // Use playAndRecord mode for simultaneous playback+recording
            let player = try await SonixPlayer.create(source: outputPath.path, audioSession: .playAndRecord)
            referencePlayer = player

            // Auto-stop when playback finishes
            playerObserverTask = player.observeIsPlaying { [weak self] playing in
                guard let self = self else { return }
                if !playing && self.isSingalongActive {
                    self.stopSingalong()
                }
            }

            await MainActor.run {
                isReady = true
            }
        } catch {
            await MainActor.run {
                status = "Player error: \(error.localizedDescription)"
            }
        }
    }

    func evaluate() {
        guard !collectedAudio.isEmpty else {
            status = "No recording to evaluate"
            return
        }

        isEvaluating = true
        status = "Evaluating..."

        Task {
            let midiNotes = currentMidiNotes
            let keyMidi = currentKeyMidi
            let duration = noteDurationMs
            let preset = selectedPreset

            let pattern = ExercisePattern.fromMidiNotes(
                midiNotes: midiNotes,
                noteDurationMs: duration
            )

            let studentAudio = collectedAudio

            let extractor = CalibraPitch.createContourExtractor()
            let studentContour = extractor.extract(audio: studentAudio, sampleRate: 16000)
            extractor.release()

            let keyHz = MusicUtils.midiToHz(Int(keyMidi))

            let evalResult = CalibraNoteEval.evaluate(
                pattern: pattern,
                student: studentContour,
                referenceKeyHz: keyHz,
                preset: preset
            )

            await MainActor.run {
                result = evalResult
                isEvaluating = false
                status = "Evaluation complete: \(evalResult.scorePercent)%"
            }
        }
    }

    func noteResult(at index: Int) -> NoteResult? {
        result?.noteResults.first { $0.noteIndex == Int32(index) }
    }

    // MARK: - Private Methods

    private func cleanup() {
        recorder?.stop()
        recorder?.release()
        recorder = nil

        playerObserverTask?.cancel()
        referencePlayer?.release()
        referencePlayer = nil
    }
}
