import SwiftUI
import VoxaTrace

/// Side-by-side comparison view of two VAD backends.
///
/// Showcases:
/// - Concurrent processing with two different backends
/// - Latency and accuracy differences
/// - Use case guidance for each backend
struct BackendComparisonView: View {
    @StateObject private var viewModel = BackendComparisonViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Compare two backends processing the same audio")
                .font(.caption)
                .foregroundColor(.secondary)

            // Side-by-side comparison with pickers above each column
            HStack(alignment: .top, spacing: 12) {
                // Left backend
                VStack(spacing: 8) {
                    CompactBackendPicker(
                        label: "Left:",
                        selectedIndex: $viewModel.leftBackendIndex,
                        backends: viewModel.backends
                    )

                    comparisonColumn(
                        info: viewModel.backends[viewModel.leftBackendIndex],
                        vadRatio: viewModel.vadRatioLeft,
                        activityLevel: viewModel.activityLevelLeft,
                        waveformSamples: viewModel.waveformSamplesLeft,
                        latencyMs: viewModel.latencyMsLeft
                    )
                }

                // Divider
                Rectangle()
                    .fill(Color.gray.opacity(0.3))
                    .frame(width: 1)

                // Right backend
                VStack(spacing: 8) {
                    CompactBackendPicker(
                        label: "Right:",
                        selectedIndex: $viewModel.rightBackendIndex,
                        backends: viewModel.backends
                    )

                    comparisonColumn(
                        info: viewModel.backends[viewModel.rightBackendIndex],
                        vadRatio: viewModel.vadRatioRight,
                        activityLevel: viewModel.activityLevelRight,
                        waveformSamples: viewModel.waveformSamplesRight,
                        latencyMs: viewModel.latencyMsRight
                    )
                }
            }

            Spacer()

            // Control button
            Button(action: { viewModel.toggleRecording() }) {
                HStack {
                    Image(systemName: viewModel.isRecording ? "stop.fill" : "mic.fill")
                    Text(viewModel.isRecording ? "Stop Comparison" : "Start Comparison")
                }
                .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)
            .tint(viewModel.isRecording ? .red : .blue)

            // Comparison insights
            if viewModel.isRecording {
                comparisonInsights
            }
        }
        .onDisappear {
            viewModel.onDisappear()
        }
    }

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
                .foregroundColor(viewModel.colorForLevel(activityLevel))

            // Activity level
            Text(viewModel.textForLevel(activityLevel))
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

    private var comparisonInsights: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("Insights")
                .font(.caption)
                .fontWeight(.medium)

            HStack {
                let leftInfo = viewModel.backends[viewModel.leftBackendIndex]
                let rightInfo = viewModel.backends[viewModel.rightBackendIndex]

                if viewModel.latencyMsLeft < viewModel.latencyMsRight {
                    Text("\(leftInfo.name) is faster (\(viewModel.latencyMsRight - viewModel.latencyMsLeft)ms)")
                        .font(.caption2)
                        .foregroundColor(.green)
                } else if viewModel.latencyMsRight < viewModel.latencyMsLeft {
                    Text("\(rightInfo.name) is faster (\(viewModel.latencyMsLeft - viewModel.latencyMsRight)ms)")
                        .font(.caption2)
                        .foregroundColor(.green)
                } else {
                    Text("Similar latency")
                        .font(.caption2)
                        .foregroundColor(.secondary)
                }
            }

            let diff = abs(viewModel.vadRatioLeft - viewModel.vadRatioRight)
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
}
