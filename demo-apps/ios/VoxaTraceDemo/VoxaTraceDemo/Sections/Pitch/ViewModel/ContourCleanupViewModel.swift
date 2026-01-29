import Foundation
import Combine
import VoxaTrace

/// ViewModel for contour cleanup comparison.
///
/// ## VoxaTrace Integration (~15 lines)
/// ```swift
/// // 1. Extract raw contour
/// let extractor = CalibraPitch.createContourExtractor(config: config)
/// let raw = extractor.extract(audio: samples, sampleRate: 16000)
///
/// // 2. Apply cleanup presets
/// let scoring = CalibraPitch.PostProcess.cleanup(raw, options: .scoring)
/// let display = CalibraPitch.PostProcess.cleanup(raw, options: .display)
/// ```
@MainActor
final class ContourCleanupViewModel: ObservableObject {

    // MARK: - Configuration

    @Published var selectedAlgorithm: Int = 0

    // MARK: - Published State

    @Published private(set) var isRecording = false
    @Published private(set) var isProcessing = false
    @Published private(set) var rawContour: PitchContour?
    @Published private(set) var scoringContour: PitchContour?
    @Published private(set) var displayContour: PitchContour?
    @Published var showRaw = true
    @Published var showScoring = true
    @Published var showDisplay = false

    // MARK: - Configuration Data

    let algorithms = PitchAlgorithmInfo.all

    // MARK: - Private

    private var recorder: SonixRecorder?
    private var collectedSamples: [Float] = []

    // MARK: - Lifecycle

    func onDisappear() {
        stopRecording()
        cleanup()
    }

    // MARK: - Actions

    func toggleRecording() {
        if isRecording {
            stopRecording()
            processRecording()
        } else {
            startRecording()
        }
    }

    func clearResults() {
        rawContour = nil
        scoringContour = nil
        displayContour = nil
        collectedSamples = []
    }

    // MARK: - Statistics

    struct ContourStats {
        let voicedCount: Int
        let octaveErrors: Int
        let blips: Int
    }

    func contourStats(_ contour: PitchContour?) -> ContourStats {
        guard let contour = contour else {
            return ContourStats(voicedCount: 0, octaveErrors: 0, blips: 0)
        }
        let pitches = contour.pitchesHz
        let voiced = pitches.filter { $0 > 0 }.count
        let octaveErrors = countOctaveErrors(pitches)
        let blips = countBlips(pitches, minFrames: 8)
        return ContourStats(voicedCount: voiced, octaveErrors: octaveErrors, blips: blips)
    }

    private func countOctaveErrors(_ pitches: [Float]) -> Int {
        var count = 0
        for i in 1..<pitches.count {
            if pitches[i] > 0 && pitches[i-1] > 0 {
                let ratio = pitches[i] / pitches[i-1]
                if ratio > 1.8 || ratio < 0.55 {
                    count += 1
                }
            }
        }
        return count
    }

    private func countBlips(_ pitches: [Float], minFrames: Int) -> Int {
        var blipCount = 0
        var runLength = 0
        for pitch in pitches {
            if pitch > 0 {
                runLength += 1
            } else {
                if runLength > 0 && runLength < minFrames {
                    blipCount += 1
                }
                runLength = 0
            }
        }
        if runLength > 0 && runLength < minFrames {
            blipCount += 1
        }
        return blipCount
    }

    // MARK: - Private Methods

    private func setupRecorderIfNeeded() {
        guard recorder == nil else { return }
        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("pitch_cleanup_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, config: .voice, audioSession: .recording)
    }

    private func cleanup() {
        recorder?.stop()
        recorder?.release()
        recorder = nil
    }

    private func startRecording() {
        setupRecorderIfNeeded()
        guard let recorder = recorder else { return }

        collectedSamples = []
        isRecording = true

        Task {
            let hwRate = AudioSessionManager.hardwareSampleRate

            for await buffer in recorder.audioBuffers {
                let samples16k = SonixResampler.resample(
                    samples: buffer.samples,
                    fromRate: hwRate,
                    toRate: 16000
                )

                await MainActor.run {
                    collectedSamples.append(contentsOf: samples16k)
                }
            }
        }

        recorder.start()
    }

    private func stopRecording() {
        recorder?.stop()
        isRecording = false
    }

    private func processRecording() {
        guard !collectedSamples.isEmpty else { return }
        isProcessing = true

        Task {
            let algorithm = algorithms[selectedAlgorithm].algorithm
            let extractorConfig = ContourExtractorConfig.Builder()
                .algorithm(algorithm)
                .pitchPreset(.balanced)
                .cleanup(.raw)
                .hopMs(10)
                .build()

            let extractor = CalibraPitch.createContourExtractor(config: extractorConfig)
            let raw = extractor.extract(audio: collectedSamples, sampleRate: 16000)
            extractor.release()

            let scoring = CalibraPitch.PostProcess.cleanup(raw, options: .scoring)
            let display = CalibraPitch.PostProcess.cleanup(raw, options: .display)

            await MainActor.run {
                rawContour = raw
                scoringContour = scoring
                displayContour = display
                isProcessing = false
            }
        }
    }
}
