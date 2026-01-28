import SwiftUI
import VoxaTrace

// MARK: - Backend Comparison Demo

/// Side-by-side comparison of two VAD backends.
///
/// Showcases:
/// - Concurrent processing with two different backends
/// - Latency and accuracy differences
/// - Use case guidance for each backend
struct BackendComparisonDemo: View {
    // Backend selection (default left to SINGING_REALTIME for comparison)
    @State private var leftBackendIndex: Int = 2  // SINGING_REALTIME
    @State private var rightBackendIndex: Int = 0 // SPEECH

    // VAD instances
    @State private var vadLeft: CalibraVAD?
    @State private var vadRight: CalibraVAD?

    // VAD state - left
    @State private var vadRatioLeft: Float = 0.0
    @State private var activityLevelLeft: VoiceActivityLevel = .none
    @State private var waveformSamplesLeft: [WaveformSample] = []
    @State private var latencyMsLeft: Int = 0

    // VAD state - right
    @State private var vadRatioRight: Float = 0.0
    @State private var activityLevelRight: VoiceActivityLevel = .none
    @State private var waveformSamplesRight: [WaveformSample] = []
    @State private var latencyMsRight: Int = 0

    // Recording state
    @State private var recorder: SonixRecorder?
    @State private var isRecording = false

    private let backends = VADBackendInfo.all
    private let maxWaveformSamples = 100

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Compare two backends processing the same audio")
                .font(.caption)
                .foregroundColor(.secondary)

            // Backend pickers
            HStack(spacing: 16) {
                CompactBackendPicker(
                    label: "Left:",
                    selectedIndex: $leftBackendIndex,
                    backends: backends
                )
                .onChange(of: leftBackendIndex) { _ in
                    recreateVADs()
                }

                CompactBackendPicker(
                    label: "Right:",
                    selectedIndex: $rightBackendIndex,
                    backends: backends
                )
                .onChange(of: rightBackendIndex) { _ in
                    recreateVADs()
                }
            }

            Divider()

            // Side-by-side comparison
            HStack(spacing: 12) {
                // Left backend
                comparisonColumn(
                    info: backends[leftBackendIndex],
                    vadRatio: vadRatioLeft,
                    activityLevel: activityLevelLeft,
                    waveformSamples: waveformSamplesLeft,
                    latencyMs: latencyMsLeft
                )

                // Divider
                Rectangle()
                    .fill(Color.gray.opacity(0.3))
                    .frame(width: 1)

                // Right backend
                comparisonColumn(
                    info: backends[rightBackendIndex],
                    vadRatio: vadRatioRight,
                    activityLevel: activityLevelRight,
                    waveformSamples: waveformSamplesRight,
                    latencyMs: latencyMsRight
                )
            }

            Spacer()

            // Control button
            Button(action: toggleRecording) {
                HStack {
                    Image(systemName: isRecording ? "stop.fill" : "mic.fill")
                    Text(isRecording ? "Stop Comparison" : "Start Comparison")
                }
                .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)
            .tint(isRecording ? .red : .blue)

            // Comparison insights
            if isRecording {
                comparisonInsights
            }
        }
        .onDisappear {
            stopRecording()
            cleanup()
        }
    }

    // MARK: - Comparison Column

    @ViewBuilder
    private func comparisonColumn(
        info: VADBackendInfo,
        vadRatio: Float,
        activityLevel: VoiceActivityLevel,
        waveformSamples: [WaveformSample],
        latencyMs: Int
    ) -> some View {
        VStack(spacing: 8) {
            // Backend name and latency
            HStack {
                Text(info.name)
                    .font(.caption)
                    .fontWeight(.semibold)
                Spacer()
                Text("\(latencyMs)ms")
                    .font(.caption2)
                    .foregroundColor(.secondary)
            }

            // Waveform
            VADWaveformView(samples: waveformSamples, height: 60)

            // VAD percentage
            Text(String(format: "%.0f%%", vadRatio * 100))
                .font(.system(size: 28, weight: .bold))
                .foregroundColor(colorForLevel(activityLevel))

            // Activity level
            Text(textForLevel(activityLevel))
                .font(.caption2)
                .foregroundColor(.secondary)

            // Use case
            Text(info.guidance)
                .font(.caption2)
                .foregroundColor(.blue)
                .multilineTextAlignment(.center)
                .lineLimit(2)
        }
        .frame(maxWidth: .infinity)
    }

    // MARK: - Comparison Insights

    private var comparisonInsights: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("Insights")
                .font(.caption)
                .fontWeight(.medium)

            HStack {
                let leftInfo = backends[leftBackendIndex]
                let rightInfo = backends[rightBackendIndex]

                if latencyMsLeft < latencyMsRight {
                    Text("\(leftInfo.name) is faster (\(latencyMsRight - latencyMsLeft)ms)")
                        .font(.caption2)
                        .foregroundColor(.green)
                } else if latencyMsRight < latencyMsLeft {
                    Text("\(rightInfo.name) is faster (\(latencyMsLeft - latencyMsRight)ms)")
                        .font(.caption2)
                        .foregroundColor(.green)
                } else {
                    Text("Similar latency")
                        .font(.caption2)
                        .foregroundColor(.secondary)
                }
            }

            let diff = abs(vadRatioLeft - vadRatioRight)
            if diff > 0.2 {
                Text("Significant detection difference (\(Int(diff * 100))%)")
                    .font(.caption2)
                    .foregroundColor(.orange)
            }
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
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

    private func recreateVADs() {
        let wasRecording = isRecording
        if wasRecording {
            stopRecording()
        }

        vadLeft?.release()
        vadRight?.release()

        vadLeft = createVAD(for: backends[leftBackendIndex].backend)
        vadRight = createVAD(for: backends[rightBackendIndex].backend)

        resetStatistics()

        if wasRecording {
            startRecording()
        }
    }

    private func resetStatistics() {
        vadRatioLeft = 0.0
        vadRatioRight = 0.0
        activityLevelLeft = .none
        activityLevelRight = .none
        waveformSamplesLeft.removeAll()
        waveformSamplesRight.removeAll()
        latencyMsLeft = 0
        latencyMsRight = 0
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
        // Configure audio session for recording
        AudioSessionManager.configure(.recording)

        // Create VADs if needed
        if vadLeft == nil {
            vadLeft = createVAD(for: backends[leftBackendIndex].backend)
        }
        if vadRight == nil {
            vadRight = createVAD(for: backends[rightBackendIndex].backend)
        }

        // Create recorder
        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("vad_compare_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, config: .voice)

        guard let recorder = recorder,
              let vadLeft = vadLeft,
              let vadRight = vadRight else { return }

        isRecording = true
        resetStatistics()

        // Process audio buffers through both VADs
        Task {
            let hwRate = AudioSessionManager.hardwareSampleRate

            for await buffer in recorder.audioBuffers {
                // Resample to 16kHz
                let samples16k = SonixResampler.resample(
                    samples: buffer.samples,
                    fromRate: hwRate,
                    toRate: 16000
                )

                // Process left VAD
                let startLeft = CFAbsoluteTimeGetCurrent()
                let ratioLeft = vadLeft.getVADRatio(samples: samples16k)
                let endLeft = CFAbsoluteTimeGetCurrent()
                let latencyLeft = Int((endLeft - startLeft) * 1000)

                // Process right VAD
                let startRight = CFAbsoluteTimeGetCurrent()
                let ratioRight = vadRight.getVADRatio(samples: samples16k)
                let endRight = CFAbsoluteTimeGetCurrent()
                let latencyRight = Int((endRight - startRight) * 1000)

                // Skip if no complete chunk processed
                if ratioLeft < 0 && ratioRight < 0 {
                    continue
                }

                // Calculate amplitude for waveform
                let amplitude = calculateRMS(samples: samples16k)

                await MainActor.run {
                    // Update left
                    if ratioLeft >= 0 {
                        self.vadRatioLeft = ratioLeft
                        self.activityLevelLeft = classifyLevel(ratio: ratioLeft)
                        self.latencyMsLeft = latencyLeft

                        let sampleLeft = WaveformSample(amplitude: amplitude, isVoice: ratioLeft > 0.5)
                        self.waveformSamplesLeft.append(sampleLeft)
                        if self.waveformSamplesLeft.count > self.maxWaveformSamples {
                            self.waveformSamplesLeft.removeFirst()
                        }
                    }

                    // Update right
                    if ratioRight >= 0 {
                        self.vadRatioRight = ratioRight
                        self.activityLevelRight = classifyLevel(ratio: ratioRight)
                        self.latencyMsRight = latencyRight

                        let sampleRight = WaveformSample(amplitude: amplitude, isVoice: ratioRight > 0.5)
                        self.waveformSamplesRight.append(sampleRight)
                        if self.waveformSamplesRight.count > self.maxWaveformSamples {
                            self.waveformSamplesRight.removeFirst()
                        }
                    }
                }
            }
        }

        recorder.start()
    }

    private func stopRecording() {
        recorder?.stop()
        isRecording = false
        vadLeft?.reset()
        vadRight?.reset()
    }

    private func cleanup() {
        recorder?.stop()
        recorder?.release()
        recorder = nil

        vadLeft?.release()
        vadLeft = nil

        vadRight?.release()
        vadRight = nil
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

    private func colorForLevel(_ level: VoiceActivityLevel) -> Color {
        switch level {
        case .none: return .gray
        case .partial: return .orange
        case .full: return .green
        @unknown default: return .gray
        }
    }

    private func textForLevel(_ level: VoiceActivityLevel) -> String {
        switch level {
        case .none: return "None"
        case .partial: return "Partial"
        case .full: return "Full"
        @unknown default: return "Unknown"
        }
    }
}
