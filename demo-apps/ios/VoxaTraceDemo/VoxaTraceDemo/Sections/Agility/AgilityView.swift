import SwiftUI

/// Vocal agility analysis view demonstrating TesseraAgility API.
struct AgilityView: View {
    @StateObject private var viewModel = AgilityViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Vocal Agility")
                .font(.headline)

            Text("Analyze vocal agility from an audio file using TesseraAgility")
                .font(.subheadline)
                .foregroundColor(.secondary)

            Button("Analyze Alankaar Voice") {
                viewModel.analyzeOffline()
            }
            .buttonStyle(.borderedProminent)
            .disabled(viewModel.isAnalyzing)

            if viewModel.isAnalyzing {
                ProgressView()
                    .frame(maxWidth: .infinity)
            }

            if viewModel.hasResult {
                resultCard
            }

            apiInfoCard
        }
    }

    // MARK: - Result Card

    private var resultCard: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Agility Result")
                .font(.subheadline)
                .fontWeight(.semibold)

            HStack {
                VStack(alignment: .leading) {
                    Text("Agility Score")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    Text(String(format: "%.0f%%", viewModel.agilityScore * 100))
                        .font(.title2)
                        .fontWeight(.bold)
                        .foregroundColor(.blue)
                }

                Spacer()
            }

            Divider()

            Text("Contour Summary")
                .font(.caption)
                .fontWeight(.medium)

            HStack {
                VStack(alignment: .leading) {
                    Text("RMS Peak")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    Text(String(format: "%.4f", viewModel.rmsPeak))
                        .font(.title3)
                        .fontWeight(.bold)
                }

                Spacer()

                VStack(alignment: .trailing) {
                    Text("Oscillation Peak")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    Text(String(format: "%.4f", viewModel.oscPeak))
                        .font(.title3)
                        .fontWeight(.bold)
                }
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

            Text("TesseraAgility.computeScore() - vocal agility scoring")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("TesseraAgility.computeContour() - agility contour intermediate")
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
}

/// Backward compatibility alias.
typealias AgilitySection = AgilityView

#Preview {
    AgilityView()
        .padding()
}
