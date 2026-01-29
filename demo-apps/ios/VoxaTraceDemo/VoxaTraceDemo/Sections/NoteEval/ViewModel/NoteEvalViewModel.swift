import Foundation
import Combine
import VoxaTrace

/// ViewModel for note/exercise evaluation using CalibraNoteEval.
///
/// ## VoxaTrace Integration (~20 lines)
/// ```swift
/// // 1. Create exercise pattern
/// let pattern = ExercisePattern.fromMidiNotes(midiNotes: midiNotes, noteDurationMs: 500)
///
/// // 2. Record student performance
/// recorder = SonixRecorder.create(outputPath: path, config: .voice, audioSession: .recording)
///
/// // 3. Extract pitch and evaluate
/// let extractor = CalibraPitch.createContourExtractor()
/// let contour = extractor.extract(audio: studentAudio, sampleRate: 16000)
/// let result = CalibraNoteEval.evaluate(pattern:, student:, referenceKeyHz:, studentKeyHz:, scoreType:, leewaySamples:)
/// ```
@MainActor
final class NoteEvalViewModel: ObservableObject {

    // MARK: - Published State

    @Published private(set) var selectedExercise = 0
    @Published private(set) var isRecording = false
    @Published private(set) var hasRecording = false
    @Published private(set) var recordingDuration: Float = 0.0
    @Published private(set) var recordingLevel: Float = 0.0
    @Published private(set) var isEvaluating = false
    @Published private(set) var result: ExerciseResult?
    @Published private(set) var status = "Select an exercise and record"

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

    // MARK: - Lifecycle

    func onDisappear() {
        cleanup()
    }

    // MARK: - Actions

    func selectExercise(_ index: Int) {
        selectedExercise = index
        result = nil
        hasRecording = false
        status = "Selected: \(exercises[index].0)"
    }

    func startRecording() {
        collectedAudio = []
        hasRecording = false
        result = nil
        recordingDuration = 0.0
        status = "Recording..."

        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("note_eval_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, config: .voice, audioSession: .recording)

        isRecording = true

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

        recorder?.start()
    }

    func stopRecording() {
        recorder?.stop()
        isRecording = false

        if !collectedAudio.isEmpty {
            hasRecording = true
            status = "Recording complete. Ready to evaluate."
        } else {
            status = "No audio recorded. Try again."
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

            let pattern = ExercisePattern.fromMidiNotes(
                midiNotes: midiNotes,
                noteDurationMs: 500
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
                studentKeyHz: 0,
                scoreType: 0,
                leewaySamples: 0
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
    }
}
