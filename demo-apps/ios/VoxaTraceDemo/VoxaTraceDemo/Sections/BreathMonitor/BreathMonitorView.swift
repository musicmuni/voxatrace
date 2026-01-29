import SwiftUI

/// Breath monitor view demonstrating CalibraVAD for voice activity detection.
struct BreathMonitorView: View {
    @StateObject private var viewModel = BreathMonitorViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Breath Monitor")
                .font(.headline)

            Text(viewModel.status)
                .font(.subheadline)
                .foregroundColor(.secondary)

            // Main timer display
            timerCard

            // Level meter
            if viewModel.monitoringState != .idle {
                HStack {
                    Text("Level:")
                        .font(.caption)
                    ProgressView(value: Double(min(max(viewModel.recordingLevel, 0), 1)))
                        .tint(viewModel.isVoiceDetected ? .green : .blue)
                }
            }

            // Best score display
            bestScoreSection

            // Control buttons
            controlButtons

            Divider()
                .padding(.vertical, 8)

            // Offline Analysis Section
            offlineAnalysisSection
        }
        .onDisappear {
            viewModel.onDisappear()
        }
    }

    private var timerCard: some View {
        let backgroundColor: Color = {
            switch viewModel.monitoringState {
            case .singing:
                return viewModel.isVoiceDetected ? Color.green : Color.orange
            case .complete:
                return Color.blue.opacity(0.2)
            default:
                return Color(.secondarySystemBackground)
            }
        }()

        let textColor: Color = viewModel.monitoringState == .singing ? .white : .primary

        return VStack(spacing: 8) {
            Text(viewModel.formatTime(viewModel.elapsedSeconds))
                .font(.system(size: 48, weight: .bold, design: .monospaced))
                .foregroundColor(textColor)

            if viewModel.monitoringState == .singing {
                HStack(spacing: 8) {
                    Circle()
                        .fill(viewModel.isVoiceDetected ? Color.white : Color.white.opacity(0.5))
                        .frame(width: 12, height: 12)
                    Text(viewModel.isVoiceDetected ? "Voice detected" : "Silence...")
                        .foregroundColor(.white.opacity(0.8))
                }
            }
        }
        .frame(maxWidth: .infinity)
        .padding(24)
        .background(backgroundColor)
        .cornerRadius(12)
    }

    private var bestScoreSection: some View {
        HStack {
            HStack {
                Text("Best:")
                    .font(.subheadline)
                Text(viewModel.formatTime(viewModel.bestScore))
                    .font(.headline)
                    .foregroundColor(.blue)
            }
            Spacer()
            if viewModel.bestScore > 0 {
                Button("Reset") {
                    viewModel.resetBestScore()
                }
                .font(.subheadline)
            }
        }
    }

    private var controlButtons: some View {
        HStack {
            switch viewModel.monitoringState {
            case .idle:
                Button("Start") {
                    viewModel.startMonitoring()
                }
                .buttonStyle(.borderedProminent)
                .frame(maxWidth: .infinity)

            case .complete:
                Button("Try Again") {
                    viewModel.startMonitoring()
                }
                .buttonStyle(.borderedProminent)
                .frame(maxWidth: .infinity)

            default:
                Button("Stop") {
                    viewModel.stopMonitoring()
                }
                .buttonStyle(.borderedProminent)
                .tint(.red)
                .frame(maxWidth: .infinity)
            }
        }
    }

    private var offlineAnalysisSection: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Offline Breath Analysis")
                .font(.headline)

            Text("Analyze breath capacity from audio file")
                .font(.caption)
                .foregroundColor(.secondary)

            Button("Analyze Alankaar Voice") {
                viewModel.analyzeOffline()
            }
            .buttonStyle(.bordered)
            .disabled(viewModel.isAnalyzingOffline)

            if viewModel.isAnalyzingOffline {
                ProgressView()
                    .frame(maxWidth: .infinity)
            }

            if viewModel.offlineVoicedTime > 0 {
                offlineResultCard
            }

            apiInfoCard
        }
    }

    private var offlineResultCard: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Offline Result")
                .font(.subheadline)
                .fontWeight(.semibold)

            HStack {
                VStack(alignment: .leading) {
                    Text("Breath Capacity")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    Text(viewModel.formatTime(viewModel.offlineBreathCapacity))
                        .font(.title2)
                        .fontWeight(.bold)
                }

                Spacer()

                VStack(alignment: .center) {
                    Text("Voiced Time")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    Text(viewModel.formatTime(viewModel.offlineVoicedTime))
                        .font(.title2)
                }

                Spacer()

                VStack(alignment: .trailing) {
                    Text("Enough Data")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    Text(viewModel.offlineHasEnoughData ? "Yes" : "No")
                        .font(.title2)
                        .foregroundColor(viewModel.offlineHasEnoughData ? .green : .red)
                }
            }
        }
        .padding(12)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(8)
    }

    private var apiInfoCard: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("APIs Demonstrated:")
                .font(.caption)
                .fontWeight(.medium)

            Text("- SonixDecoder.decode() - Load audio from file")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("- CalibraPitch.createContourExtractor() - Extract pitch contour")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("- CalibraBreath.hasEnoughData() - Check data sufficiency")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("- CalibraBreath.computeCapacity() - Compute breath capacity")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("- CalibraBreath.getCumulativeVoicedTime() - Get voiced time")
                .font(.caption2)
                .foregroundColor(.secondary)
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }
}

/// Backward compatibility alias.
typealias BreathMonitorSection = BreathMonitorView

#Preview {
    BreathMonitorView()
        .padding()
}
