import Foundation
import Combine
import VoxaTrace

/// ViewModel for exploring PitchPoint properties in real-time.
///
/// ## VoxaTrace Integration (~10 lines)
/// ```swift
/// // 1. Create detector
/// let detector = CalibraPitch.createDetector(config: config)
///
/// // 2. Detect pitch
/// let result = detector.detect(samples: buffer.samples, sampleRate: hwRate)
/// // Access: result.pitch, result.isSinging, result.note, result.midiNote,
/// //         result.centsOff, result.tuning, result.reliability, result.confidence
/// ```
@MainActor
final class PitchPointExplorerViewModel: ObservableObject {

    // MARK: - Configuration

    @Published var selectedAlgorithm: Int = 0 {
        didSet {
            if isRecording {
                stopRecording()
            }
            detector?.release()
            detector = nil
        }
    }

    // MARK: - Published State

    @Published private(set) var isRecording = false
    @Published private(set) var currentPitch: PitchPoint?
    @Published private(set) var amplitude: Float = 0.0

    // MARK: - Configuration Data

    let algorithms = PitchAlgorithmInfo.all

    // MARK: - Private

    private var detector: CalibraPitch.Detector?
    private var recorder: SonixRecorder?

    // MARK: - Lifecycle

    func onDisappear() {
        stopRecording()
        cleanup()
    }

    // MARK: - Actions

    func toggleRecording() {
        if isRecording {
            stopRecording()
        } else {
            startRecording()
        }
    }

    // MARK: - Helpers

    func tuningString(_ tuning: PitchPoint.Tuning) -> String {
        switch tuning {
        case .silent: return "SILENT"
        case .flat: return "FLAT"
        case .inTune: return "IN_TUNE"
        case .sharp: return "SHARP"
        default: return "UNKNOWN"
        }
    }

    // MARK: - Private Methods

    private func setupAudioIfNeeded() {
        guard detector == nil else { return }

        let algorithm = algorithms[selectedAlgorithm].algorithm
        let detectorConfig = PitchDetectorConfig.Builder()
            .algorithm(algorithm)
            .build()

        detector = CalibraPitch.createDetector(config: detectorConfig)

        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("pitch_explorer_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, config: .voice, audioSession: .recording)
    }

    private func cleanup() {
        recorder?.stop()
        recorder?.release()
        recorder = nil

        detector?.release()
        detector = nil
    }

    private func startRecording() {
        setupAudioIfNeeded()
        guard let recorder = recorder, let detector = detector else { return }

        detector.reset()
        isRecording = true

        Task {
            let hwRate = AudioSessionManager.hardwareSampleRate

            for await buffer in recorder.audioBuffers {
                let result = detector.detect(samples: buffer.samples, sampleRate: hwRate)
                let amp = detector.getAmplitude(samples: buffer.samples, sampleRate: hwRate)

                await MainActor.run {
                    self.currentPitch = result
                    self.amplitude = amp
                }
            }
        }

        recorder.start()
    }

    private func stopRecording() {
        recorder?.stop()
        isRecording = false

        detector?.reset()
        currentPitch = nil
        amplitude = 0.0
    }
}
