import SwiftUI
import calibra
import sonix

private let SILENCE_GRACE_MS: Int64 = 500
private let PREFS_KEY_BEST_SCORE = "breath_monitor_best_score"

enum BreathMonitorState {
    case idle
    case waitingForVoice
    case singing
    case complete
}

/// Breath Monitor using Calibra public API.
///
/// Demonstrates:
/// - `CalibraVAD` (Silero) for voice activity detection
/// - `SonixRecorder` for audio capture
struct BreathMonitorSection: View {
    @State private var monitoringState: BreathMonitorState = .idle
    @State private var elapsedSeconds: Float = 0.0
    @State private var bestScore: Float = UserDefaults.standard.float(forKey: PREFS_KEY_BEST_SCORE)
    @State private var isVoiceDetected = false
    @State private var recordingLevel: Float = 0.0
    @State private var status = "Hold a note as long as you can!"

    @State private var recorder: SonixRecorder?

    // Calibra public API - using VAD for voice detection
    @State private var vad: CalibraVAD?

    @State private var startTimeMs: Int64 = 0
    @State private var lastVoiceTimeMs: Int64 = 0

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Breath Monitor")
                .font(.headline)

            Text(status)
                .font(.subheadline)
                .foregroundColor(.secondary)

            // Main timer display
            timerCard

            // Level meter (volume from Sonix)
            if monitoringState != .idle {
                HStack {
                    Text("Level:")
                        .font(.caption)
                    ProgressView(value: Double(min(max(recordingLevel, 0), 1)))
                        .tint(isVoiceDetected ? .green : .blue)
                }
            }

            // Best score display
            HStack {
                HStack {
                    Text("Best:")
                        .font(.subheadline)
                    Text(formatTime(bestScore))
                        .font(.headline)
                        .foregroundColor(.blue)
                }
                Spacer()
                if bestScore > 0 {
                    Button("Reset") {
                        resetBestScore()
                    }
                    .font(.subheadline)
                }
            }

            // Control buttons
            controlButtons
        }
        .onDisappear {
            stopMonitoring()
            cleanupAudio()
        }
    }

    private var timerCard: some View {
        let backgroundColor: Color = {
            switch monitoringState {
            case .singing:
                return isVoiceDetected ? Color.green : Color.orange
            case .complete:
                return Color.blue.opacity(0.2)
            default:
                return Color(.secondarySystemBackground)
            }
        }()

        let textColor: Color = monitoringState == .singing ? .white : .primary

        return VStack(spacing: 8) {
            Text(formatTime(elapsedSeconds))
                .font(.system(size: 48, weight: .bold, design: .monospaced))
                .foregroundColor(textColor)

            if monitoringState == .singing {
                HStack(spacing: 8) {
                    Circle()
                        .fill(isVoiceDetected ? Color.white : Color.white.opacity(0.5))
                        .frame(width: 12, height: 12)
                    Text(isVoiceDetected ? "Voice detected" : "Silence...")
                        .foregroundColor(.white.opacity(0.8))
                }
            }
        }
        .frame(maxWidth: .infinity)
        .padding(24)
        .background(backgroundColor)
        .cornerRadius(12)
    }

    private var controlButtons: some View {
        HStack {
            switch monitoringState {
            case .idle:
                Button("Start") {
                    startMonitoring()
                }
                .buttonStyle(.borderedProminent)
                .frame(maxWidth: .infinity)

            case .complete:
                Button("Try Again") {
                    startMonitoring()
                }
                .buttonStyle(.borderedProminent)
                .frame(maxWidth: .infinity)

            default:
                Button("Stop") {
                    stopMonitoring()
                }
                .buttonStyle(.borderedProminent)
                .tint(.red)
                .frame(maxWidth: .infinity)
            }
        }
    }

    private func setupAudioIfNeeded() {
        guard vad == nil else { return }

        // Create VAD using Calibra public API with SINGING preset
        vad = CalibraVAD.create(preset: .singing)

        // Create recorder using Sonix
        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("breath_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, format: "m4a", quality: "voice")
    }

    private func cleanupAudio() {
        recorder?.stop()
        recorder?.release()
        recorder = nil

        vad?.release()
        vad = nil
    }

    private func startMonitoring() {
        setupAudioIfNeeded()
        guard let recorder = recorder, let vad = vad else { return }

        // Reset state
        elapsedSeconds = 0
        isVoiceDetected = false
        recordingLevel = 0
        monitoringState = .waitingForVoice
        status = "Start singing when ready..."
        vad.reset()

        // Observe level from Sonix for the meter
        _ = recorder.observeLevel { level in
            recordingLevel = level
        }

        // Collect audio buffers from Sonix for VAD
        Task {
            let hwRate = Int(Sonix.hardwareSampleRate)

            for await buffer in recorder.audioBuffers {
                // Resample to 16kHz for Calibra (expects 16kHz input)
                let samples16k = SonixResampler.resample(
                    samples: buffer.floatSamples,
                    fromRate: hwRate,
                    toRate: 16000
                )

                // Get VAD ratio using Calibra public API
                let ratio = vad.getVADRatio(samples: samples16k)

                // Skip if no complete chunk processed
                if ratio < 0 {
                    continue
                }

                let currentTimeMs = Int64(Date().timeIntervalSince1970 * 1000)
                let hasVoice = ratio > 0.5

                await MainActor.run {
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

                                recorder.stop()
                            }
                        }

                        // Update elapsed time
                        if monitoringState == .singing {
                            elapsedSeconds = Float(currentTimeMs - startTimeMs) / 1000.0
                        }

                    default:
                        break
                    }
                }
            }
        }

        recorder.start()
    }

    private func stopMonitoring() {
        recorder?.stop()
        vad?.reset()
        monitoringState = .idle
        status = "Hold a note as long as you can!"
    }

    private func resetBestScore() {
        bestScore = 0
        UserDefaults.standard.set(0, forKey: PREFS_KEY_BEST_SCORE)
    }

    private func formatTime(_ seconds: Float) -> String {
        let mins = Int(seconds / 60)
        let secs = seconds.truncatingRemainder(dividingBy: 60)
        if mins > 0 {
            return String(format: "%d:%05.2f", mins, secs)
        } else {
            return String(format: "%.2f", secs)
        }
    }
}
