import Foundation
import Combine
import VoxaTrace

private let SILENCE_GRACE_MS: Int64 = 500
private let PREFS_KEY_BEST_SCORE = "breath_monitor_best_score"

/// Breath monitor state.
enum BreathMonitorState {
    case idle
    case waitingForVoice
    case singing
    case complete
}

/// ViewModel for breath monitoring using CalibraVAD.
///
/// ## VoxaTrace Integration (~20 lines)
/// ```swift
/// // 1. Create VAD
/// vad = CalibraVAD.create(.singingRealtime { ModelLoader.loadSingingRealtimeVAD() })
///
/// // 2. Process audio buffers
/// let ratio = vad.getVADRatio(samples: samples16k)
/// let hasVoice = ratio > 0.5
///
/// // 3. Offline analysis with CalibraBreath
/// let extractor = CalibraPitch.createContourExtractor()
/// let contour = extractor.extract(audio: samples16k, sampleRate: 16000)
/// let capacity = CalibraBreath.computeCapacity(times: contour.times, pitchesHz: contour.pitchesHz)
/// ```
@MainActor
final class BreathMonitorViewModel: ObservableObject {

    // MARK: - Published State

    @Published private(set) var monitoringState: BreathMonitorState = .idle
    @Published private(set) var elapsedSeconds: Float = 0.0
    @Published private(set) var bestScore: Float = UserDefaults.standard.float(forKey: PREFS_KEY_BEST_SCORE)
    @Published private(set) var isVoiceDetected = false
    @Published private(set) var recordingLevel: Float = 0.0
    @Published private(set) var status = "Hold a note as long as you can!"

    // Offline analysis state
    @Published private(set) var offlineBreathCapacity: Float = 0.0
    @Published private(set) var offlineVoicedTime: Float = 0.0
    @Published private(set) var offlineHasEnoughData = false
    @Published private(set) var isAnalyzingOffline = false

    // MARK: - Private

    private var recorder: SonixRecorder?
    private var vad: CalibraVAD?
    private var startTimeMs: Int64 = 0
    private var lastVoiceTimeMs: Int64 = 0

    // MARK: - Lifecycle

    func onDisappear() {
        stopMonitoring()
        cleanupAudio()
    }

    // MARK: - Actions

    func startMonitoring() {
        setupAudioIfNeeded()
        guard let recorder = recorder, let vad = vad else { return }

        // Reset state
        elapsedSeconds = 0
        isVoiceDetected = false
        recordingLevel = 0
        monitoringState = .waitingForVoice
        status = "Start singing when ready..."
        vad.reset()

        // Observe level from Sonix
        _ = recorder.observeLevel { [weak self] level in
            self?.recordingLevel = level
        }

        // Collect audio buffers for VAD
        Task {
            let hwRate = AudioSessionManager.hardwareSampleRate

            for await buffer in recorder.audioBuffers {
                // ADR-017: Pass sampleRate directly; CalibraVAD handles resampling internally
                let ratio = vad.getVADRatio(samples: buffer.samples, sampleRate: hwRate)
                if ratio < 0 { continue }

                let currentTimeMs = Int64(Date().timeIntervalSince1970 * 1000)
                let hasVoice = ratio > 0.5

                await MainActor.run {
                    self.processVADResult(hasVoice: hasVoice, currentTimeMs: currentTimeMs)
                }
            }
        }

        recorder.start()
    }

    func stopMonitoring() {
        recorder?.stop()
        vad?.reset()
        monitoringState = .idle
        status = "Hold a note as long as you can!"
    }

    func resetBestScore() {
        bestScore = 0
        UserDefaults.standard.set(0, forKey: PREFS_KEY_BEST_SCORE)
    }

    func analyzeOffline() {
        isAnalyzingOffline = true
        offlineBreathCapacity = 0
        offlineVoicedTime = 0
        offlineHasEnoughData = false

        Task {
            guard let audioURL = Bundle.main.url(forResource: "Alankaar 01_voice", withExtension: "m4a"),
                  let audioData = SonixDecoder.decode(path: audioURL.path) else {
                await MainActor.run { isAnalyzingOffline = false }
                return
            }

            // ADR-017: Pass original samples; ContourExtractor handles resampling internally
            let extractor = CalibraPitch.createContourExtractor()
            let contour = extractor.extract(audio: audioData.samples, sampleRate: audioData.sampleRate)
            extractor.release()

            let hasEnough = CalibraBreath.hasEnoughData(times: contour.times, pitchesHz: contour.pitchesHz)
            let capacity = hasEnough ? CalibraBreath.computeCapacity(times: contour.times, pitchesHz: contour.pitchesHz) : 0
            let voicedTime = CalibraBreath.getCumulativeVoicedTime(times: contour.times, pitchesHz: contour.pitchesHz)

            await MainActor.run {
                offlineHasEnoughData = hasEnough
                offlineBreathCapacity = capacity
                offlineVoicedTime = voicedTime
                isAnalyzingOffline = false
            }
        }
    }

    // MARK: - Formatting

    func formatTime(_ seconds: Float) -> String {
        let mins = Int(seconds / 60)
        let secs = seconds.truncatingRemainder(dividingBy: 60)
        if mins > 0 {
            return String(format: "%d:%05.2f", mins, secs)
        } else {
            return String(format: "%.2f", secs)
        }
    }

    // MARK: - Private Methods

    private func setupAudioIfNeeded() {
        guard vad == nil else { return }

        vad = CalibraVAD.create(.singingRealtime { ModelLoader.loadSingingRealtimeVAD() })

        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("breath_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, config: .voice, audioSession: .recording)
    }

    private func cleanupAudio() {
        recorder?.stop()
        recorder?.release()
        recorder = nil
        vad?.release()
        vad = nil
    }

    private func processVADResult(hasVoice: Bool, currentTimeMs: Int64) {
        switch monitoringState {
        case .waitingForVoice:
            if hasVoice {
                monitoringState = .singing
                startTimeMs = currentTimeMs
                lastVoiceTimeMs = currentTimeMs
                isVoiceDetected = true
                status = "Keep going!"
            }

        case .singing:
            if hasVoice {
                lastVoiceTimeMs = currentTimeMs
                isVoiceDetected = true
            } else {
                isVoiceDetected = false
                let silenceDuration = currentTimeMs - lastVoiceTimeMs

                if silenceDuration > SILENCE_GRACE_MS {
                    monitoringState = .complete
                    elapsedSeconds = Float(lastVoiceTimeMs - startTimeMs) / 1000.0

                    if elapsedSeconds > bestScore {
                        bestScore = elapsedSeconds
                        UserDefaults.standard.set(bestScore, forKey: PREFS_KEY_BEST_SCORE)
                        status = "New record! \(formatTime(elapsedSeconds))"
                    } else {
                        status = "Good try! \(formatTime(elapsedSeconds))"
                    }

                    recorder?.stop()
                }
            }

            if monitoringState == .singing {
                elapsedSeconds = Float(currentTimeMs - startTimeMs) / 1000.0
            }

        default:
            break
        }
    }
}
