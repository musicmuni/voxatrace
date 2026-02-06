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
    private var recordingSampleRate: Int = 16000

    // Singalong
    private var referencePlayer: SonixPlayer?
    private var playerObserverTask: Task<Void, Never>?
    private var audioCollectionTask: Task<Void, Never>?
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

        // Start recorder first to get actual sample rate
        recorder?.start()

        // Capture actual sample rate (SDK handles resampling internally per ADR-017)
        recordingSampleRate = Int(recorder?.actualSampleRate ?? 16000)
        print("[NoteEval] 🎤 Recorder started, sampleRate=\(recordingSampleRate)")

        // Start collecting audio (store task to properly cancel on stop)
        audioCollectionTask = Task {
            var sampleCount = 0
            var bufferCount = 0

            guard let recorder = recorder else {
                print("[NoteEval] ❌ Recorder is nil in collection task")
                return
            }

            for await buffer in recorder.audioBuffers {
                let samples = buffer.samples
                bufferCount += 1

                collectedAudio.append(contentsOf: samples)
                sampleCount += samples.count

                let sum = samples.reduce(0) { $0 + $1 * $1 }
                let rms = sqrt(sum / Float(samples.count))
                let minSample = samples.min() ?? 0
                let maxSample = samples.max() ?? 0

                // Log every 10th buffer to avoid spam
                if bufferCount % 10 == 0 {
                    print("[NoteEval] 📦 Buffer #\(bufferCount): samples=\(samples.count), total=\(sampleCount), rms=\(String(format: "%.4f", rms)), range=[\(String(format: "%.4f", minSample)), \(String(format: "%.4f", maxSample))]")
                }

                await MainActor.run {
                    recordingLevel = min(rms * 5, 1.0)
                    recordingDuration = Float(sampleCount) / Float(recordingSampleRate)
                }
            }
            print("[NoteEval] 🏁 Audio collection ended: \(bufferCount) buffers, \(sampleCount) total samples")
        }

        // Start player
        referencePlayer?.play()
    }

    /// Stop singalong session and auto-evaluate.
    func stopSingalong() {
        print("[NoteEval] 🛑 stopSingalong called, collectedAudio.count=\(collectedAudio.count)")

        // Cancel audio collection first (matches Android pattern)
        audioCollectionTask?.cancel()
        audioCollectionTask = nil

        recorder?.stop()
        referencePlayer?.stop()
        isSingalongActive = false

        // Log audio stats
        if !collectedAudio.isEmpty {
            let minSample = collectedAudio.min() ?? 0
            let maxSample = collectedAudio.max() ?? 0
            let sum = collectedAudio.reduce(0) { $0 + $1 * $1 }
            let rms = sqrt(sum / Float(collectedAudio.count))
            let durationSec = Float(collectedAudio.count) / Float(recordingSampleRate)
            print("[NoteEval] 📊 Audio stats: samples=\(collectedAudio.count), duration=\(String(format: "%.2f", durationSec))s, rms=\(String(format: "%.4f", rms)), range=[\(String(format: "%.4f", minSample)), \(String(format: "%.4f", maxSample))]")

            hasRecording = true
            status = "Recording complete. Evaluating..."
            evaluate()
        } else {
            print("[NoteEval] ⚠️ No audio collected!")
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
            print("[NoteEval] ❌ evaluate() called but collectedAudio is empty")
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

            print("[NoteEval] 🎼 Pattern: midiNotes=\(midiNotes), keyMidi=\(keyMidi), duration=\(duration)ms, preset=\(preset)")

            let pattern = ExercisePattern.fromMidiNotes(
                midiNotes: midiNotes,
                noteDurationMs: duration
            )
            print("[NoteEval] 🎼 Pattern created: noteCount=\(pattern.noteFrequencies.count), totalDuration=\(pattern.totalDurationMs)ms")
            for (i, freq) in pattern.noteFrequencies.enumerated() {
                let dur = pattern.noteDurations[i]
                print("[NoteEval]    Note[\(i)]: freq=\(String(format: "%.1f", freq))Hz, duration=\(dur)ms")
            }

            let studentAudio = collectedAudio
            let sampleRate = recordingSampleRate
            print("[NoteEval] 🔊 Audio to extract: \(studentAudio.count) samples @ \(sampleRate)Hz = \(String(format: "%.2f", Float(studentAudio.count) / Float(sampleRate)))s")

            // SDK handles resampling internally (ADR-017)
            let extractor = CalibraPitch.createContourExtractor()
            let studentContour = extractor.extract(audio: studentAudio, sampleRate: sampleRate)
            extractor.release()

            // Log pitch extraction results
            let pitchSamples = studentContour.samples
            let voicedSamples = pitchSamples.filter { $0.isVoiced }
            print("[NoteEval] 🎵 Pitch extraction: totalSamples=\(pitchSamples.count), voicedSamples=\(voicedSamples.count)")

            if !voicedSamples.isEmpty {
                let pitches = voicedSamples.map { $0.pitch }
                let minPitch = pitches.min() ?? 0
                let maxPitch = pitches.max() ?? 0
                let avgPitch = pitches.reduce(0, +) / Float(pitches.count)
                print("[NoteEval] 🎵 Voiced pitch stats: min=\(String(format: "%.1f", minPitch))Hz, max=\(String(format: "%.1f", maxPitch))Hz, avg=\(String(format: "%.1f", avgPitch))Hz")

                // Show first few and last few samples
                let firstFew = Array(voicedSamples.prefix(5))
                let lastFew = Array(voicedSamples.suffix(5))
                print("[NoteEval] 🎵 First voiced samples:")
                for p in firstFew {
                    print("[NoteEval]    t=\(String(format: "%.3f", p.timeSeconds))s, pitch=\(String(format: "%.1f", p.pitch))Hz")
                }
                print("[NoteEval] 🎵 Last voiced samples:")
                for p in lastFew {
                    print("[NoteEval]    t=\(String(format: "%.3f", p.timeSeconds))s, pitch=\(String(format: "%.1f", p.pitch))Hz")
                }
            } else {
                print("[NoteEval] ⚠️ NO VOICED SAMPLES DETECTED!")
            }

            let keyHz = CalibraMusic.midiToHz(Float(keyMidi))
            print("[NoteEval] 🎹 Reference key: midi=\(keyMidi), hz=\(String(format: "%.2f", keyHz))Hz")

            let evalResult = CalibraNoteEval.evaluate(
                pattern: pattern,
                student: studentContour,
                referenceKeyHz: keyHz,
                preset: preset
            )

            // Log evaluation results
            print("[NoteEval] 📈 EVALUATION RESULT:")
            print("[NoteEval]    Overall score: \(evalResult.scorePercent)%")
            print("[NoteEval]    Note results count: \(evalResult.noteResults.count)")
            for noteResult in evalResult.noteResults {
                print("[NoteEval]    Note[\(noteResult.noteIndex)]: score=\(noteResult.scorePercent)%, expectedFreq=\(String(format: "%.1f", noteResult.expectedFrequencyHz))Hz, level=\(noteResult.level), passing=\(noteResult.isPassing)")
            }

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
        audioCollectionTask?.cancel()
        audioCollectionTask = nil

        recorder?.stop()
        recorder?.release()
        recorder = nil

        playerObserverTask?.cancel()
        referencePlayer?.release()
        referencePlayer = nil
    }
}
