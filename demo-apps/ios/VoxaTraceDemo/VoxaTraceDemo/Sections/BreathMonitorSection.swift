import SwiftUI
import VoxaTrace

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

    // Offline analysis state
    @State private var offlineBreathCapacity: Float = 0.0
    @State private var offlineVoicedTime: Float = 0.0
    @State private var offlineHasEnoughData = false
    @State private var isAnalyzingOffline = false

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

            Divider()
                .padding(.vertical, 8)

            // Offline Analysis Section
            offlineAnalysisSection
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

        // Configure audio session for recording
        AudioSessionManager.configure(.recording)

        // Create VAD using Calibra public API with SINGING_REALTIME backend (low latency)
        vad = CalibraVAD.create(.singingRealtime { ModelLoader.loadSingingRealtimeVAD() })

        // Create recorder using Sonix
        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("breath_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, config: .voice)
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
            let hwRate = AudioSessionManager.hardwareSampleRate

            for await buffer in recorder.audioBuffers {
                // Resample to 16kHz for Calibra (expects 16kHz input)
                let samples16k = SonixResampler.resample(
                    samples: buffer.samples,
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

    // MARK: - Offline Analysis

    private var offlineAnalysisSection: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Offline Breath Analysis")
                .font(.headline)

            Text("Analyze breath capacity from audio file")
                .font(.caption)
                .foregroundColor(.secondary)

            Button("Analyze Alankaar Voice") {
                analyzeOffline()
            }
            .buttonStyle(.bordered)
            .disabled(isAnalyzingOffline)

            if isAnalyzingOffline {
                ProgressView()
                    .frame(maxWidth: .infinity)
            }

            if offlineVoicedTime > 0 {
                VStack(alignment: .leading, spacing: 8) {
                    Text("Offline Result")
                        .font(.subheadline)
                        .fontWeight(.semibold)

                    HStack {
                        VStack(alignment: .leading) {
                            Text("Breath Capacity")
                                .font(.caption)
                                .foregroundColor(.secondary)
                            Text(formatTime(offlineBreathCapacity))
                                .font(.title2)
                                .fontWeight(.bold)
                        }

                        Spacer()

                        VStack(alignment: .center) {
                            Text("Voiced Time")
                                .font(.caption)
                                .foregroundColor(.secondary)
                            Text(formatTime(offlineVoicedTime))
                                .font(.title2)
                        }

                        Spacer()

                        VStack(alignment: .trailing) {
                            Text("Enough Data")
                                .font(.caption)
                                .foregroundColor(.secondary)
                            Text(offlineHasEnoughData ? "Yes" : "No")
                                .font(.title2)
                                .foregroundColor(offlineHasEnoughData ? .green : .red)
                        }
                    }
                }
                .padding(12)
                .background(Color(.secondarySystemBackground))
                .cornerRadius(8)
            }

            // API Info
            VStack(alignment: .leading, spacing: 4) {
                Text("APIs Demonstrated:")
                    .font(.caption)
                    .fontWeight(.medium)

                Text("• SonixDecoder.decode() - Load audio from file")
                    .font(.caption2)
                    .foregroundColor(.secondary)

                Text("• CalibraPitch.createContourExtractor() - Extract pitch contour")
                    .font(.caption2)
                    .foregroundColor(.secondary)

                Text("• CalibraBreath.hasEnoughData() - Check data sufficiency")
                    .font(.caption2)
                    .foregroundColor(.secondary)

                Text("• CalibraBreath.computeCapacity() - Compute breath capacity")
                    .font(.caption2)
                    .foregroundColor(.secondary)

                Text("• CalibraBreath.getCumulativeVoicedTime() - Get voiced time")
                    .font(.caption2)
                    .foregroundColor(.secondary)
            }
            .padding(8)
            .background(Color(.tertiarySystemBackground))
            .cornerRadius(6)
        }
    }

    private func analyzeOffline() {
        isAnalyzingOffline = true
        offlineBreathCapacity = 0
        offlineVoicedTime = 0
        offlineHasEnoughData = false

        Task {
            // Load audio from bundle
            guard let audioURL = Bundle.main.url(forResource: "Alankaar 01_voice", withExtension: "m4a"),
                  let audioData = SonixDecoder.decode(path: audioURL.path) else {
                await MainActor.run {
                    isAnalyzingOffline = false
                }
                return
            }

            // Resample to 16kHz for Calibra APIs
            let samples16k = SonixResampler.resample(
                samples: audioData.samples,
                fromRate: Int(audioData.sampleRate),
                toRate: 16000
            )

            // Extract pitch contour using ContourExtractor
            // This handles chunking internally - no manual array manipulation needed
            let extractor = CalibraPitch.createContourExtractor()
            let contour = extractor.extract(audio: samples16k)
            extractor.release()

            // Analyze breath using CalibraBreath
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
}
