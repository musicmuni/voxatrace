import SwiftUI
import VoxaTrace

// MARK: - Data Structures

/// Sample data for waveform visualization
struct WaveformSample {
    let amplitude: Float
    let isVoice: Bool
}

/// Backend information for display
struct VADBackendInfo {
    let backend: VADBackend
    let name: String
    let description: String
    let guidance: String
    let latency: String

    static let all: [VADBackendInfo] = [
        VADBackendInfo(
            backend: .speech,
            name: "Speech",
            description: "Silero neural network",
            guidance: "Best for: Conversations, podcasts, voice commands",
            latency: "~32ms"
        ),
        VADBackendInfo(
            backend: .general,
            name: "General",
            description: "RMS-based detection",
            guidance: "Best for: Simple detection, no ML dependency needed",
            latency: "~1ms"
        ),
        VADBackendInfo(
            backend: .singingRealtime,
            name: "Singing RT",
            description: "SwiftF0 pitch-based",
            guidance: "Best for: Karaoke, singing games, low-latency apps",
            latency: "~32ms"
        ),
        VADBackendInfo(
            backend: .singing,
            name: "Singing",
            description: "Essentia YAMNet classifier",
            guidance: "Best for: Music apps, vocal extraction, highest accuracy",
            latency: "~960ms"
        )
    ]

    static func info(for backend: VADBackend) -> VADBackendInfo {
        all.first { $0.backend == backend } ?? all[0]
    }
}

// MARK: - Waveform View

/// Real-time waveform visualization with VAD overlay
struct VADWaveformView: View {
    let samples: [WaveformSample]
    var height: CGFloat = 80

    var body: some View {
        Canvas { context, size in
            guard !samples.isEmpty else { return }

            let barWidth = size.width / CGFloat(samples.count)
            let midY = size.height / 2

            for (index, sample) in samples.enumerated() {
                let normalizedAmp = min(max(CGFloat(sample.amplitude), 0), 1)
                let barHeight = normalizedAmp * size.height * 0.9
                let color = sample.isVoice ? Color.green : Color.gray.opacity(0.4)

                let rect = CGRect(
                    x: CGFloat(index) * barWidth,
                    y: midY - barHeight / 2,
                    width: max(barWidth - 1, 1),
                    height: max(barHeight, 2)
                )
                context.fill(Path(roundedRect: rect, cornerRadius: 1), with: .color(color))
            }
        }
        .frame(height: height)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(8)
    }
}

// MARK: - Statistics Card

/// Displays a labeled statistic value
struct VADStatCard: View {
    let title: String
    let value: String
    var highlight: Bool = false

    var body: some View {
        VStack(spacing: 2) {
            Text(title)
                .font(.caption2)
                .foregroundColor(.secondary)
            Text(value)
                .font(.caption)
                .fontWeight(.semibold)
                .foregroundColor(highlight ? .green : .primary)
        }
        .frame(maxWidth: .infinity)
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }
}

// MARK: - Backend Info Card

/// Displays backend information and use case guidance
struct BackendInfoCard: View {
    let info: VADBackendInfo

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            HStack {
                Text(info.name)
                    .font(.caption)
                    .fontWeight(.semibold)
                Spacer()
                Text(info.latency)
                    .font(.caption2)
                    .foregroundColor(.secondary)
                    .padding(.horizontal, 6)
                    .padding(.vertical, 2)
                    .background(Color(.tertiarySystemBackground))
                    .cornerRadius(4)
            }

            Text(info.description)
                .font(.caption2)
                .foregroundColor(.secondary)

            Text(info.guidance)
                .font(.caption2)
                .foregroundColor(.blue)
        }
        .padding(10)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(8)
    }
}

// MARK: - Sensitivity Slider

/// Slider for adjusting VAD threshold
struct SensitivitySlider: View {
    @Binding var threshold: Float
    var range: ClosedRange<Float> = 0.2...0.9

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            HStack {
                Text("Sensitivity:")
                    .font(.caption)
                Spacer()
                Text(String(format: "%.2f", threshold))
                    .font(.caption)
                    .fontWeight(.medium)
                    .foregroundColor(.secondary)
            }

            HStack(spacing: 8) {
                Text("High")
                    .font(.caption2)
                    .foregroundColor(.secondary)
                Slider(value: $threshold, in: range)
                Text("Low")
                    .font(.caption2)
                    .foregroundColor(.secondary)
            }

            Text("Lower threshold = more sensitive to quiet sounds")
                .font(.caption2)
                .foregroundColor(.secondary)
        }
    }
}

// MARK: - VAD Display Card

/// Large display showing VAD percentage and activity level
struct VADDisplayCard: View {
    let vadRatio: Float
    let activityLevel: VoiceActivityLevel

    var body: some View {
        VStack(spacing: 8) {
            // VAD ratio as percentage
            Text(String(format: "%.0f%%", vadRatio * 100))
                .font(.system(size: 48, weight: .bold))
                .foregroundColor(activityColor)

            // Activity level
            Text(activityLevelText)
                .font(.title3)
                .foregroundColor(.secondary)

            // Progress bar
            ProgressView(value: Double(vadRatio))
                .progressViewStyle(.linear)
                .tint(activityColor)
        }
        .frame(maxWidth: .infinity)
        .padding(20)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(12)
    }

    private var activityColor: Color {
        switch activityLevel {
        case .none: return .gray
        case .partial: return .orange
        case .full: return .green
        @unknown default: return .gray
        }
    }

    private var activityLevelText: String {
        switch activityLevel {
        case .none: return "None"
        case .partial: return "Partial"
        case .full: return "Full"
        @unknown default: return "Unknown"
        }
    }
}

// MARK: - Voice Indicator

/// Animated indicator showing voice detection state
struct VoiceIndicator: View {
    let isVoiceDetected: Bool
    @State private var isPulsing = false

    var body: some View {
        HStack(spacing: 8) {
            Circle()
                .fill(isVoiceDetected ? Color.green : Color.gray)
                .frame(width: 12, height: 12)
                .scaleEffect(isVoiceDetected && isPulsing ? 1.2 : 1.0)
                .animation(
                    isVoiceDetected
                        ? .easeInOut(duration: 0.5).repeatForever(autoreverses: true)
                        : .default,
                    value: isPulsing
                )
                .onChange(of: isVoiceDetected) { detected in
                    isPulsing = detected
                }

            Text(isVoiceDetected ? "Voice Detected" : "Silence")
                .font(.subheadline)
                .foregroundColor(isVoiceDetected ? .green : .secondary)
        }
    }
}

// MARK: - Backend Picker

/// Segmented picker for selecting VAD backend
struct BackendPicker: View {
    @Binding var selectedIndex: Int
    let backends: [VADBackendInfo]

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("Backend:")
                .font(.caption)
                .foregroundColor(.secondary)

            Picker("Backend", selection: $selectedIndex) {
                ForEach(0..<backends.count, id: \.self) { index in
                    Text(backends[index].name).tag(index)
                }
            }
            .pickerStyle(.segmented)
        }
    }
}

// MARK: - Compact Backend Picker (for comparison view)

/// Menu-style picker for compact backend selection
struct CompactBackendPicker: View {
    let label: String
    @Binding var selectedIndex: Int
    let backends: [VADBackendInfo]

    var body: some View {
        HStack {
            Text(label)
                .font(.caption)
                .foregroundColor(.secondary)

            Picker(label, selection: $selectedIndex) {
                ForEach(0..<backends.count, id: \.self) { index in
                    Text(backends[index].name).tag(index)
                }
            }
            .pickerStyle(.menu)
        }
    }
}

// MARK: - Statistics Row

/// Horizontal row of statistics
struct VADStatsRow: View {
    let voiceTime: Float
    let silenceTime: Float
    let latencyMs: Int

    var body: some View {
        HStack(spacing: 8) {
            VADStatCard(
                title: "Voice",
                value: String(format: "%.1fs", voiceTime),
                highlight: true
            )
            VADStatCard(
                title: "Silence",
                value: String(format: "%.1fs", silenceTime)
            )
            VADStatCard(
                title: "Latency",
                value: "\(latencyMs)ms"
            )
        }
    }
}
