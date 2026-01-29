import Foundation
import Combine
import VoxaTrace

/// ViewModel for real-time pitch detection.
///
/// ## VoxaTrace Integration (~15 lines)
/// ```swift
/// // 1. Create detector with configuration
/// let config = PitchDetectorConfig.Builder()
///     .algorithm(.swiftF0)
///     .voiceType(.auto)
///     .build()
/// detector = CalibraPitch.createDetector(config: config)
///
/// // 2. Process audio buffers
/// let result = detector.detect(samples: buffer.samples, sampleRate: hwRate)
/// let amplitude = detector.getAmplitude(samples: buffer.samples, sampleRate: hwRate)
/// ```
@MainActor
final class RealtimePitchViewModel: ObservableObject {

    // MARK: - Configuration

    @Published var selectedAlgorithm: Int = 0 {
        didSet { recreateDetectorIfRecording() }
    }
    @Published var selectedPreset: Int = 1 {
        didSet { recreateDetectorIfRecording() }
    }
    @Published var selectedVoiceType: Int = 0 {
        didSet { recreateDetectorIfRecording() }
    }
    @Published var selectedQuietHandling: Int = 1 {
        didSet { recreateDetectorIfRecording() }
    }
    @Published var selectedStrictness: Int = 1 {
        didSet { recreateDetectorIfRecording() }
    }

    // MARK: - Published State

    @Published private(set) var isRecording = false
    @Published private(set) var currentPitch: PitchPoint?
    @Published private(set) var amplitude: Float = 0.0
    @Published private(set) var recordedPitches: [Float] = []
    @Published private(set) var pitchHistory: [Float] = []
    @Published var showGraph = false

    // MARK: - Configuration Data

    let algorithms = PitchAlgorithmInfo.all
    let presets = PitchPresetInfo.all
    let voiceTypes = VoiceTypeInfo.all
    let quietHandlings = QuietHandlingInfo.all
    let strictnesses = StrictnessInfo.all

    let maxHistoryPoints = 200

    // MARK: - Private

    private var detector: CalibraPitch.Detector?
    private var recorder: SonixRecorder?
    private var recordingTask: Task<Void, Never>?

    // MARK: - Lifecycle

    func onDisappear() {
        stopRecording()
        cleanup()
    }

    // MARK: - Actions

    func toggleRecording() {
        if isRecording {
            stopRecording()
            showGraph = true
        } else {
            startRecording()
            showGraph = false
        }
    }

    func clearRecording() {
        recordedPitches = []
        showGraph = false
    }

    // MARK: - Private Methods

    private func recreateDetector() {
        detector?.release()

        let algorithm = algorithms[selectedAlgorithm].algorithm
        let detectorConfig = PitchDetectorConfig.Builder()
            .preset(presets[selectedPreset].config)
            .algorithm(algorithm)
            .voiceType(voiceTypes[selectedVoiceType].type)
            .quietHandling(quietHandlings[selectedQuietHandling].handling)
            .strictness(strictnesses[selectedStrictness].strictness)
            .build()

        detector = CalibraPitch.createDetector(config: detectorConfig)
    }

    private func recreateDetectorIfRecording() {
        if isRecording {
            stopRecording()
        }
        recreateDetector()
    }

    private func setupAudioIfNeeded() {
        if detector == nil {
            recreateDetector()
        }

        if recorder == nil {
            let tempPath = FileManager.default.temporaryDirectory
                .appendingPathComponent("pitch_realtime_temp.m4a").path
            recorder = SonixRecorder.create(outputPath: tempPath, config: .voice, audioSession: .recording)
        }
    }

    private func cleanup() {
        // Cancel the recording task first
        recordingTask?.cancel()
        recordingTask = nil

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
        recordedPitches = []
        pitchHistory = []
        isRecording = true

        recordingTask = Task {
            let hwRate = AudioSessionManager.hardwareSampleRate

            for await buffer in recorder.audioBuffers {
                // Check for cancellation before processing
                if Task.isCancelled { break }

                let result = detector.detect(samples: buffer.samples, sampleRate: hwRate)
                let amp = detector.getAmplitude(samples: buffer.samples, sampleRate: hwRate)

                await MainActor.run {
                    self.currentPitch = result
                    self.amplitude = amp
                    self.recordedPitches.append(result.pitch)

                    self.pitchHistory.append(result.pitch)
                    if self.pitchHistory.count > self.maxHistoryPoints {
                        self.pitchHistory.removeFirst()
                    }
                }
            }
        }

        recorder.start()
    }

    private func stopRecording() {
        // Cancel the recording task first to ensure it stops using the detector
        recordingTask?.cancel()
        recordingTask = nil

        recorder?.stop()
        isRecording = false

        detector?.reset()
        currentPitch = nil
        amplitude = 0.0
    }
}
