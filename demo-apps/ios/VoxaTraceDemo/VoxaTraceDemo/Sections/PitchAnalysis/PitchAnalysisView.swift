import SwiftUI

/// Pitch Analysis view demonstrating PitchAnalysis histogram and tonal segment labeling.
struct PitchAnalysisView: View {
    @StateObject private var viewModel = PitchAnalysisViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Pitch Analysis")
                .font(.headline)

            Text("Pitch histogram and tonal segment labeling")
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

            if !viewModel.histogramValues.isEmpty {
                histogramCard
            }

            if !viewModel.tonalSegments.isEmpty {
                tonalSegmentsCard
            }

            apiInfoCard
        }
    }

    // MARK: - Histogram Card

    private var histogramCard: some View {
        let maxValue = viewModel.histogramValues.max() ?? 1

        return VStack(alignment: .leading, spacing: 8) {
            Text("Pitch Histogram")
                .font(.subheadline)
                .fontWeight(.semibold)

            ForEach(Array(viewModel.histogramValues.enumerated()), id: \.offset) { index, value in
                if value > 0 && index < viewModel.histogramBinCenters.count {
                    HStack(spacing: 8) {
                        Text(String(format: "%.0f", viewModel.histogramBinCenters[index]))
                            .font(.caption)
                            .frame(width: 40, alignment: .trailing)

                        GeometryReader { geometry in
                            Rectangle()
                                .fill(Color.blue)
                                .frame(width: geometry.size.width * CGFloat(value / maxValue))
                                .cornerRadius(2)
                        }
                        .frame(height: 12)
                    }
                }
            }
        }
        .padding(12)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(8)
    }

    // MARK: - Tonal Segments Card

    private var tonalSegmentsCard: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Tonal Segments")
                .font(.subheadline)
                .fontWeight(.semibold)

            // Header row
            HStack {
                Text("Label")
                    .font(.caption)
                    .fontWeight(.medium)
                    .frame(maxWidth: .infinity, alignment: .leading)
                Text("Start")
                    .font(.caption)
                    .fontWeight(.medium)
                    .frame(maxWidth: .infinity, alignment: .leading)
                Text("Duration")
                    .font(.caption)
                    .fontWeight(.medium)
                    .frame(maxWidth: .infinity, alignment: .leading)
            }

            ForEach(viewModel.tonalSegments) { segment in
                HStack {
                    Text(segment.label)
                        .font(.caption)
                        .fontWeight(.medium)
                        .frame(maxWidth: .infinity, alignment: .leading)
                    Text(String(format: "%.2f s", segment.startTime))
                        .font(.caption)
                        .frame(maxWidth: .infinity, alignment: .leading)
                    Text(String(format: "%.2f s", segment.duration))
                        .font(.caption)
                        .frame(maxWidth: .infinity, alignment: .leading)
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

            Text("PitchAnalysis.computeHistogram() - pitch distribution")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("PitchAnalysis.labelByMeanPitch() - tonal segment labeling")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("PitchAnalysis.estimateTuningOffset() - tuning estimation")
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
typealias PitchAnalysisSection = PitchAnalysisView

#Preview {
    PitchAnalysisView()
        .padding()
}
