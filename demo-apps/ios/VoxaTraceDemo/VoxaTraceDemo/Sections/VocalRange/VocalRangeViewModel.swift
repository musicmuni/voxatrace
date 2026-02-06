import Foundation
import Combine
import VoxaTrace

/// ViewModel for vocal range detection using VocalRangeSession.
///
/// ## VoxaTrace Integration (~20 lines)
/// ```swift
/// // 1. Create pitch detector
/// let detectorConfig = PitchDetectorConfig.Builder().algorithm(.yin).enableProcessing().build()
/// let detector = CalibraPitch.createDetector(config: detectorConfig)
///
/// // 2. Create session
/// let config = VocalRangeSessionConfig.custom(countdownSeconds: 3)
/// let session = VocalRangeSession.create(config: config, detector: detector)
///
/// // 3. Observe state via AsyncSequence
/// for await state in session.state { /* update UI */ }
///
/// // 4. Control session
/// session.start() / session.cancel() / session.confirmNote()
///
/// // 5. Feed audio
/// session.addAudio(samples: samples16k)
/// ```
@MainActor
final class VocalRangeViewModel: ObservableObject {

    // MARK: - Published State

    @Published private(set) var phase: VocalRangePhase = .idle
    @Published private(set) var countdownSeconds: Int32 = 0
    @Published private(set) var phaseMessage: String = "Ready to detect your vocal range"
    @Published private(set) var currentPitch: VocalPitch? = nil
    @Published private(set) var currentAmplitude: Float = 0
    @Published private(set) var stabilityProgress: Float = 0
    @Published private(set) var bestLowNote: DetectedNote? = nil
    @Published private(set) var bestHighNote: DetectedNote? = nil
    @Published private(set) var lowNote: DetectedNote? = nil
    @Published private(set) var highNote: DetectedNote? = nil
    @Published private(set) var result: VocalRangeResult? = nil
    @Published private(set) var error: String? = nil

    // MARK: - Computed Properties

    var currentPitchLabel: String { currentPitch?.noteLabel ?? "-" }
    var currentPitchHz: Float { currentPitch?.frequencyHz ?? 0 }

    var isDetecting: Bool {
        phase == .detectingLow || phase == .detectingHigh
    }

    var hasError: Bool { error != nil }

    var bestNoteForPhase: DetectedNote? {
        switch phase {
        case .detectingLow: return bestLowNote
        case .detectingHigh: return bestHighNote
        default: return nil
        }
    }

    var canLockNote: Bool { bestNoteForPhase != nil }

    var isFlowActive: Bool {
        phase != .cancelled && phase != .idle
    }

    // MARK: - Private

    private var session: VocalRangeSession?
    private var recorder: SonixRecorder?
    private var audioTask: Task<Void, Never>?
    private var observerTask: Task<Void, Never>?

    // MARK: - Lifecycle

    func onDisappear() {
        stop()
        cleanup()
    }

    // MARK: - Actions

    func start() {
        // Create detector
        let detectorConfig = PitchDetectorConfig.Builder()
            .algorithm(.yin)
            .enableProcessing()
            .build()
        let detector = CalibraPitch.createDetector(config: detectorConfig)

        // Create session
        let config = VocalRangeSessionConfig.custom(countdownSeconds: 3)
        let newSession = VocalRangeSession.create(config: config, detector: detector)
        session = newSession

        // Create recorder
        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("range_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, config: .voice, audioSession: .recording)

        guard let recorder = recorder else { return }

        // Observe session state
        observerTask = Task {
            for await state in newSession.state {
                await MainActor.run {
                    self.phase = state.phase
                    self.countdownSeconds = state.countdownSeconds
                    self.phaseMessage = state.phaseMessage
                    self.currentPitch = state.currentPitch
                    self.currentAmplitude = state.currentAmplitude
                    self.stabilityProgress = state.stabilityProgress
                    self.bestLowNote = state.bestLowNote
                    self.bestHighNote = state.bestHighNote
                    self.lowNote = state.lowNote
                    self.highNote = state.highNote
                    self.result = state.result
                    self.error = state.error
                }
            }
        }

        // Start session
        newSession.start()

        // Start recording
        recorder.start()

        // Feed audio to session
        // ADR-017: Pass sampleRate directly; VocalRangeSession handles resampling internally
        audioTask = Task {
            let hwRate = AudioSessionManager.hardwareSampleRate
            for await buffer in recorder.audioBuffers {
                newSession.addAudio(samples: buffer.samples, sampleRate: hwRate)
            }
        }
    }

    func stop() {
        audioTask?.cancel()
        audioTask = nil
        observerTask?.cancel()
        observerTask = nil
        recorder?.stop()
        session?.cancel()

        countdownSeconds = 0
        phase = .cancelled
    }

    func reset() {
        stop()
        phase = .idle
        countdownSeconds = 0
        phaseMessage = "Ready to detect your vocal range"
        currentPitch = nil
        currentAmplitude = 0
        stabilityProgress = 0
        bestLowNote = nil
        bestHighNote = nil
        lowNote = nil
        highNote = nil
        result = nil
        error = nil
    }

    func confirmNote() {
        _ = session?.confirmNote()
    }

    // MARK: - Private Methods

    private func cleanup() {
        stop()
        recorder?.release()
        recorder = nil
        session?.release()
        session = nil
    }
}
