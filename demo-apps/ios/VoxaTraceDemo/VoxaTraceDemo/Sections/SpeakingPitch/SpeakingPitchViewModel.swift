import Foundation
import Combine
import VoxaTrace

private let RMS_THRESHOLD: Float = 0.02
private let COUNTDOWN_SECONDS = 4
private let FEMALE_THRESHOLD_HZ: Float = 174.61 // F3

/// Detection state for speaking pitch.
enum SpeakingPitchDetectionState {
    case idle
    case listening
    case countdown
    case processing
    case complete
}

/// Inferred gender from pitch.
enum Gender {
    case male
    case female
}

/// ViewModel for speaking pitch detection using CalibraSpeakingPitch.
///
/// ## VoxaTrace Integration (~15 lines)
/// ```swift
/// // 1. Create pitch detector
/// let config = PitchDetectorConfig.Builder().algorithm(.yin).build()
/// pitch = CalibraPitch.createDetector(config: config)
///
/// // 2. Get amplitude for voice detection
/// let rms = pitch.getAmplitude(samples: samples16k, sampleRate: 16000)
///
/// // 3. Detect speaking pitch from collected audio
/// let pitchHz = CalibraSpeakingPitch.detectFromAudio(audioMono: samples)
/// ```
@MainActor
final class SpeakingPitchViewModel: ObservableObject {

    // MARK: - Published State

    @Published private(set) var detectionState: SpeakingPitchDetectionState = .idle
    @Published private(set) var countdownSeconds: Int = COUNTDOWN_SECONDS
    @Published private(set) var currentLevel: Float = 0.0
    @Published private(set) var detectedPitchHz: Float = 0.0
    @Published private(set) var detectedPitchNote = ""
    @Published private(set) var detectedGender: Gender?
    @Published private(set) var status = "Speak naturally to detect your speaking pitch"

    // Offline analysis state
    @Published private(set) var offlinePitchHz: Float = 0.0
    @Published private(set) var offlinePitchNote = ""
    @Published private(set) var offlineGender: Gender?
    @Published private(set) var isAnalyzingOffline = false

    // MARK: - Private

    private var recorder: SonixRecorder?
    private var pitch: CalibraPitch.Detector?
    private var collectedChunks: [Float] = []
    private var collectedSampleRate: Int = 16000

    // MARK: - Lifecycle

    func onDisappear() {
        stopDetection()
        cleanupAudio()
    }

    // MARK: - Actions

    func startDetection() {
        setupAudioIfNeeded()
        guard let recorder = recorder else { return }

        // Reset state
        detectionState = .listening
        countdownSeconds = COUNTDOWN_SECONDS
        detectedPitchHz = 0
        detectedPitchNote = ""
        detectedGender = nil
        collectedChunks = []
        status = "Say something..."

        // Collect audio buffers
        // ADR-017: Pass sampleRate directly; CalibraPitch handles resampling internally
        Task {
            let hwRate = AudioSessionManager.hardwareSampleRate

            for await buffer in recorder.audioBuffers {
                let calculatedRms = pitch?.getAmplitude(samples: buffer.samples, sampleRate: hwRate) ?? 0.0

                await MainActor.run {
                    currentLevel = calculatedRms

                    switch detectionState {
                    case .listening:
                        if calculatedRms > RMS_THRESHOLD {
                            detectionState = .countdown
                            status = "Keep speaking..."
                            collectedSampleRate = hwRate
                            startCountdown()
                        }

                    case .countdown:
                        collectedChunks.append(contentsOf: buffer.samples)

                    default:
                        break
                    }
                }
            }
        }

        recorder.start()
    }

    func stopDetection() {
        recorder?.stop()
        detectionState = .idle
        status = "Speak naturally to detect your speaking pitch"
    }

    func analyzeOffline() {
        isAnalyzingOffline = true
        offlinePitchHz = 0
        offlinePitchNote = ""
        offlineGender = nil

        Task {
            guard let audioURL = Bundle.main.url(forResource: "Alankaar 01_voice", withExtension: "m4a"),
                  let audioData = SonixDecoder.decode(path: audioURL.path) else {
                await MainActor.run { isAnalyzingOffline = false }
                return
            }

            // ADR-017: Pass original samples; CalibraSpeakingPitch handles resampling internally
            let pitchHz = CalibraSpeakingPitch.detectFromAudio(audioMono: audioData.samples, sampleRate: audioData.sampleRate)

            await MainActor.run {
                if pitchHz > 0 {
                    offlinePitchHz = pitchHz
                    offlinePitchNote = MusicUtils.getMidiNoteName(pitchHz)
                    offlineGender = pitchHz >= FEMALE_THRESHOLD_HZ ? .female : .male
                }
                isAnalyzingOffline = false
            }
        }
    }

    // MARK: - Private Methods

    private func setupAudioIfNeeded() {
        guard pitch == nil else { return }

        let config = PitchDetectorConfig.Builder()
            .algorithm(.yin)
            .build()
        pitch = CalibraPitch.createDetector(config: config)

        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("speaking_pitch_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, config: .voice, audioSession: .recording)
    }

    private func cleanupAudio() {
        recorder?.stop()
        recorder?.release()
        recorder = nil

        pitch?.release()
        pitch = nil
    }

    private func startCountdown() {
        Task {
            for i in stride(from: COUNTDOWN_SECONDS, through: 1, by: -1) {
                await MainActor.run { countdownSeconds = i }
                try? await Task.sleep(nanoseconds: 1_000_000_000)

                if await MainActor.run(body: { detectionState != .countdown }) {
                    return
                }
            }

            await MainActor.run {
                detectionState = .processing
                status = "Processing..."
                recorder?.stop()
                processAudio()
            }
        }
    }

    private func processAudio() {
        guard !collectedChunks.isEmpty else {
            status = "No audio collected. Try again."
            detectionState = .complete
            return
        }

        let allSamples = collectedChunks
        // ADR-017: Pass sampleRate; CalibraSpeakingPitch handles resampling internally
        let pitchHz = CalibraSpeakingPitch.detectFromAudio(audioMono: allSamples, sampleRate: collectedSampleRate)

        if pitchHz > 0 {
            detectedPitchHz = pitchHz
            detectedPitchNote = MusicUtils.getMidiNoteName(pitchHz)
            detectedGender = pitchHz >= FEMALE_THRESHOLD_HZ ? .female : .male
            status = "Detection complete!"
        } else {
            status = "Could not detect speaking pitch. Try again."
        }

        detectionState = .complete
    }
}
