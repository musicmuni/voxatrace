import SwiftUI

/// Voice Profile view demonstrating Tessera.analyze() for multi-metric batch analysis.
struct VoiceProfileView: View {
    @StateObject private var viewModel = VoiceProfileViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Voice Profile")
                .font(.headline)

            Text("Multi-metric analysis \u{2014} breath, agility, and range in one call")
                .font(.subheadline)
                .foregroundColor(.secondary)

            Button("Analyze Voice") {
                viewModel.analyzeOffline()
            }
            .buttonStyle(.borderedProminent)
            .disabled(viewModel.isAnalyzing)

            if viewModel.isAnalyzing {
                ProgressView()
                    .frame(maxWidth: .infinity)
            }

            if !viewModel.vocalRangeLow.isEmpty {
                dashboardCard
            }

            apiInfoCard
        }
    }

    // MARK: - Dashboard Card

    private var dashboardCard: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Voice Profile Result")
                .font(.subheadline)
                .fontWeight(.semibold)

            HStack(alignment: .top) {
                // Breath section
                VStack(spacing: 4) {
                    Text("Breath")
                        .font(.caption)
                        .fontWeight(.medium)
                    Text(String(format: "%.1f s", viewModel.breathCapacity))
                        .font(.title2)
                        .fontWeight(.bold)
                    Text("capacity")
                        .font(.caption2)
                        .foregroundColor(.secondary)
                    Text(String(format: "%.0f%%", viewModel.breathControl * 100))
                        .font(.headline)
                        .foregroundColor(controlColor(viewModel.breathControl))
                    Text("control")
                        .font(.caption2)
                        .foregroundColor(.secondary)
                }
                .frame(maxWidth: .infinity)

                // Agility section
                VStack(spacing: 4) {
                    Text("Agility")
                        .font(.caption)
                        .fontWeight(.medium)
                    Text(String(format: "%.0f%%", viewModel.agilityScore * 100))
                        .font(.title2)
                        .fontWeight(.bold)
                        .foregroundColor(scoreColor(viewModel.agilityScore))
                    Text("score")
                        .font(.caption2)
                        .foregroundColor(.secondary)
                }
                .frame(maxWidth: .infinity)

                // Range section
                VStack(spacing: 4) {
                    Text("Range")
                        .font(.caption)
                        .fontWeight(.medium)
                    Text("\(viewModel.vocalRangeLow) \u{2014} \(viewModel.vocalRangeHigh)")
                        .font(.headline)
                        .fontWeight(.bold)
                    Text(String(format: "%.1f octaves", viewModel.vocalRangeOctaves))
                        .font(.caption2)
                        .foregroundColor(.secondary)
                }
                .frame(maxWidth: .infinity)
            }
        }
        .padding(12)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(8)
    }

    // MARK: - API Info Card

    private var apiInfoCard: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("APIs Demonstrated:")
                .font(.caption)
                .fontWeight(.medium)

            Text("Tessera.analyze() - multi-metric batch analysis")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("TesseraMetric.ALL - breath, agility, vocal range")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("PitchDetection.createContourExtractor() - pitch extraction")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("SonixDecoder.decode() - audio file loading")
                .font(.caption2)
                .foregroundColor(.secondary)
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }

    // MARK: - Helpers

    private func controlColor(_ value: Float) -> Color {
        if value >= 0.7 { return .green }
        if value >= 0.4 { return .orange }
        return .red
    }

    private func scoreColor(_ value: Float) -> Color {
        if value >= 0.7 { return .green }
        if value >= 0.4 { return .orange }
        return .red
    }
}

/// Backward compatibility alias.
typealias VoiceProfileSection = VoiceProfileView

#Preview {
    VoiceProfileView()
        .padding()
}
