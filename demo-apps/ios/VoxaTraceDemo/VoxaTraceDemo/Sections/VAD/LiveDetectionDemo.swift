import SwiftUI
import VoxaTrace

// MARK: - Live Detection Demo

/// Real-time voice activity detection with all 4 backends.
///
/// Showcases:
/// - SPEECH (Silero neural network)
/// - GENERAL (RMS-based, no model)
/// - SINGING_REALTIME (SwiftF0 pitch-based)
/// - SINGING (Essentia YAMNet classifier)
struct LiveDetectionDemo: View {
    // Backend selection (default to SINGING_REALTIME - index 2)
    @State private var selectedBackendIndex: Int = 2

    // VAD state
    @State private var vad: CalibraVAD?
    @State private var vadRatio: Float = 0.0
    @State private var activityLevel: VoiceActivityLevel = .none

    // Recording state
    @State private var recorder: SonixRecorder?
    @State private var isRecording = false

    // Configuration
    @State private var threshold: Float = 0.5

    // Statistics
    @State private var voiceTimeSeconds: Float = 0.0
    @State private var silenceTimeSeconds: Float = 0.0
    @State private var lastProcessingLatencyMs: Int = 0

    // Waveform buffer (rolling window of ~5 seconds at 32ms per frame)
    @State private var waveformSamples: [WaveformSample] = []
    private let maxWaveformSamples = 160

    private let backends = VADBackendInfo.all

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            // Backend picker
            BackendPicker(
                selectedIndex: $selectedBackendIndex,
                backends: backends
            )
            .onChange(of: selectedBackendIndex) { _ in
                recreateVAD()
            }

            // Backend info card
            BackendInfoCard(info: backends[selectedBackendIndex])

            Divider()

            // Waveform visualization
            VADWaveformView(samples: waveformSamples)

            // VAD display
            VADDisplayCard(vadRatio: vadRatio, activityLevel: activityLevel)

            // Voice indicator
            if isRecording {
                VoiceIndicator(isVoiceDetected: vadRatio > threshold)
            }

            // Statistics
            VADStatsRow(
                voiceTime: voiceTimeSeconds,
                silenceTime: silenceTimeSeconds,
                latencyMs: lastProcessingLatencyMs
            )

            // Sensitivity slider
            SensitivitySlider(threshold: $threshold)

            Spacer()

            // Control button
            Button(action: toggleRecording) {
                HStack {
                    Image(systemName: isRecording ? "stop.fill" : "mic.fill")
                    Text(isRecording ? "Stop" : "Start Detection")
                }
                .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)
            .tint(isRecording ? .red : .blue)

            // API usage info
            apiInfoSection
        }
        .onDisappear {
            stopRecording()
            cleanup()
        }
    }

    // MARK: - API Info

    private var apiInfoSection: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("API Usage:")
                .font(.caption)
                .fontWeight(.medium)

            Text(apiCodeExample)
                .font(.caption2)
                .foregroundColor(.secondary)
                .padding(8)
                .background(Color(.tertiarySystemBackground))
                .cornerRadius(6)
        }
    }

    private var apiCodeExample: String {
        let backend = backends[selectedBackendIndex].backend
        switch backend {
        case .general:
            return """
            let vad = CalibraVAD.create(.general)
            let ratio = vad.getVADRatio(samples: samples16k)
            """
        case .speech:
            return """
            let vad = CalibraVAD.create(.speech {
                ModelLoader.loadSileroVAD()
            })
            let ratio = vad.getVADRatio(samples: samples16k)
            """
        case .singingRealtime:
            return """
            let vad = CalibraVAD.create(.singingRealtime {
                ModelLoader.loadSingingRealtimeVAD()
            })
            let ratio = vad.getVADRatio(samples: samples16k)
            """
        case .singing:
            return """
            let vad = CalibraVAD.create(.singing {
                ModelLoader.loadSingingVAD()
            })
            let ratio = vad.getVADRatio(samples: samples16k)
            """
        default:
            return "// Unknown backend"
        }
    }

    // MARK: - VAD Creation

    private func createVAD(for backend: VADBackend) -> CalibraVAD {
        switch backend {
        case .general:
            return CalibraVAD.create(.general)
        case .speech:
            return CalibraVAD.create(.speech { ModelLoader.loadSpeechVAD() })
        case .singingRealtime:
            return CalibraVAD.create(.singingRealtime { ModelLoader.loadSingingRealtimeVAD() })
        case .singing:
            return CalibraVAD.create(.singing { ModelLoader.loadSingingVAD() })
        default:
            return CalibraVAD.create(.general)
        }
    }

    private func recreateVAD() {
        let wasRecording = isRecording
        if wasRecording {
            stopRecording()
        }

        vad?.release()
        let backend = backends[selectedBackendIndex].backend
        vad = createVAD(for: backend)

        // Reset statistics
        resetStatistics()

        if wasRecording {
            startRecording()
        }
    }

    private func resetStatistics() {
        vadRatio = 0.0
        activityLevel = .none
        voiceTimeSeconds = 0.0
        silenceTimeSeconds = 0.0
        lastProcessingLatencyMs = 0
        waveformSamples.removeAll()
    }

    // MARK: - Recording Control

    private func toggleRecording() {
        if isRecording {
            stopRecording()
        } else {
            startRecording()
        }
    }

    private func startRecording() {
        // Create VAD if needed
        if vad == nil {
            let backend = backends[selectedBackendIndex].backend
            vad = createVAD(for: backend)
        }

        // Create recorder
        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("vad_live_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, format: "m4a", quality: "voice")

        guard let recorder = recorder, let vad = vad else { return }

        isRecording = true
        resetStatistics()

        // Process audio buffers
        Task {
            let hwRate = Int(Sonix.hardwareSampleRate)

            for await buffer in recorder.audioBuffers {
                let startTime = CFAbsoluteTimeGetCurrent()

                // Resample to 16kHz for Calibra
                let samples16k = SonixResampler.resample(
                    samples: buffer.samples,
                    fromRate: hwRate,
                    toRate: 16000
                )

                // Get VAD ratio
                let ratio = vad.getVADRatio(samples: samples16k)

                let endTime = CFAbsoluteTimeGetCurrent()
                let latencyMs = Int((endTime - startTime) * 1000)

                // Skip if no complete chunk processed
                if ratio < 0 {
                    continue
                }

                // Calculate amplitude for waveform (RMS)
                let amplitude = calculateRMS(samples: samples16k)

                // Determine voice state
                let isVoice = ratio > threshold

                // Update statistics
                let frameDuration: Float = 0.032 // ~32ms per frame

                await MainActor.run {
                    // Update VAD ratio and level
                    self.vadRatio = ratio
                    self.activityLevel = classifyLevel(ratio: ratio)
                    self.lastProcessingLatencyMs = latencyMs

                    // Update time accumulators
                    if isVoice {
                        self.voiceTimeSeconds += frameDuration
                    } else {
                        self.silenceTimeSeconds += frameDuration
                    }

                    // Update waveform buffer
                    let sample = WaveformSample(amplitude: amplitude, isVoice: isVoice)
                    self.waveformSamples.append(sample)
                    if self.waveformSamples.count > self.maxWaveformSamples {
                        self.waveformSamples.removeFirst()
                    }
                }
            }
        }

        recorder.start()
    }

    private func stopRecording() {
        recorder?.stop()
        isRecording = false
        vad?.reset()
    }

    private func cleanup() {
        recorder?.stop()
        recorder?.release()
        recorder = nil

        vad?.release()
        vad = nil
    }

    // MARK: - Helpers

    private func calculateRMS(samples: [Float]) -> Float {
        guard !samples.isEmpty else { return 0 }
        let sumSquares = samples.reduce(0) { $0 + $1 * $1 }
        return sqrt(sumSquares / Float(samples.count))
    }

    private func classifyLevel(ratio: Float) -> VoiceActivityLevel {
        if ratio < VADConstants.noSingingThreshold {
            return .none
        } else if ratio < VADConstants.partialSingingThreshold {
            return .partial
        } else {
            return .full
        }
    }
}
