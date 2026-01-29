import Foundation
import Combine
import VoxaTrace

/// ViewModel for offline melody evaluation using CalibraMelodyEval.
///
/// ## VoxaTrace Integration (~20 lines)
/// ```swift
/// // 1. Load reference
/// let transData = Parser.parseTransString(content: transContent)
/// let audioData = SonixDecoder.decode(path: audioURL.path)
/// reference = LessonMaterial.fromAudio(samples:, sampleRate:, segments:, keyHz:)
///
/// // 2. Record student
/// recorder = SonixRecorder.create(outputPath: path, config: .voice, audioSession: .recording)
///
/// // 3. Evaluate
/// let extractor = CalibraPitch.createContourExtractor()
/// let result = CalibraMelodyEval.evaluate(reference: reference, student: studentMaterial, contourExtractor: extractor)
/// ```
@MainActor
final class MelodyEvalViewModel: ObservableObject {

    // MARK: - Published State

    @Published private(set) var referenceLoaded = false
    @Published private(set) var segments: [Segment] = []
    @Published private(set) var isRecording = false
    @Published private(set) var hasRecording = false
    @Published private(set) var recordingDuration: Float = 0.0
    @Published private(set) var recordingLevel: Float = 0.0
    @Published private(set) var isEvaluating = false
    @Published private(set) var result: SingingResult?
    @Published private(set) var status = "Ready"

    let referenceName = "Alankaar 01"

    // MARK: - Private

    private var reference: LessonMaterial?
    private var recorder: SonixRecorder?
    private var collectedAudio: [Float] = []

    // MARK: - Lifecycle

    func onDisappear() {
        cleanup()
    }

    // MARK: - Actions

    func loadReference() {
        status = "Loading reference..."

        guard let transURL = Bundle.main.url(forResource: referenceName, withExtension: "trans"),
              let transContent = try? String(contentsOf: transURL),
              let transData = Parser.parseTransString(content: transContent) else {
            status = "Failed to load trans file"
            return
        }

        // Use only first segment for easier testing
        let firstSeg = transData.segments[0]
        segments = [
            .create(
                index: 0,
                startSeconds: firstSeg.startTime,
                endSeconds: firstSeg.endTime,
                lyrics: firstSeg.lyrics
            )
        ]

        guard let audioURL = Bundle.main.url(forResource: referenceName, withExtension: "m4a") else {
            status = "Failed to load audio file"
            return
        }

        guard let audioData = SonixDecoder.decode(path: audioURL.path) else {
            status = "Failed to decode audio"
            return
        }

        reference = LessonMaterial.fromAudio(
            samples: audioData.samples,
            sampleRate: Int32(audioData.sampleRate),
            segments: segments,
            keyHz: 196.0
        )

        referenceLoaded = true
        status = "Reference loaded. Record your performance."
    }

    func startRecording() {
        collectedAudio = []
        hasRecording = false
        result = nil
        recordingDuration = 0.0
        status = "Recording..."

        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("melody_eval_temp.m4a").path
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
        guard let reference = reference, !collectedAudio.isEmpty else {
            status = "Missing reference or recording"
            return
        }

        isEvaluating = true
        status = "Evaluating..."

        Task {
            let studentAudio = collectedAudio

            let studentMaterial = LessonMaterial.fromAudio(
                samples: studentAudio,
                sampleRate: 16000,
                segments: [],
                keyHz: reference.keyHz
            )

            let extractor = CalibraPitch.createContourExtractor()

            let evalResult = CalibraMelodyEval.evaluate(
                reference: reference,
                student: studentMaterial,
                contourExtractor: extractor
            )

            extractor.release()

            await MainActor.run {
                result = evalResult
                isEvaluating = false
                status = "Evaluation complete: \(Int(evalResult.overallScore * 100))%"
            }
        }
    }

    // MARK: - Private Methods

    private func cleanup() {
        recorder?.stop()
        recorder?.release()
        recorder = nil
    }
}
