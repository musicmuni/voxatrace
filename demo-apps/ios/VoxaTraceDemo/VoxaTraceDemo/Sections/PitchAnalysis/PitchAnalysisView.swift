import SwiftUI

/// Pitch Analysis view demonstrating PitchAnalysis histogram and tonal segment labeling.
struct PitchAnalysisView: View {
    @StateObject private var viewModel = PitchAnalysisViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Pitch Analysis")
                .font(.headline)

            Text("Folded pitch histogram (one octave, 240 bins) and tonal segments")
                .font(.subheadline)
                .foregroundColor(.secondary)

            Button("Analyze Alankaar Voice") {
                viewModel.analyzeOffline()
            }
            .buttonStyle(.bordered)
            .disabled(viewModel.isAnalyzing)

            if viewModel.isAnalyzing {
                ProgressView()
                    .frame(maxWidth: .infinity)
            }

            if !viewModel.histogramBins.isEmpty {
                histogramChart
            }

            if !viewModel.tonalSegments.isEmpty {
                tonalSegmentsCard
            }

            apiInfoCard
        }
    }

    private var histogramChart: some View {
        let noteNames = ["S", "r", "R", "g", "G", "M", "m", "P", "d", "D", "n", "N"]
        let maxVal = viewModel.histogramValues.max() ?? 1

        return VStack(alignment: .leading, spacing: 8) {
            Text("Pitch Histogram (folded, 1 octave)")
                .font(.subheadline)
                .fontWeight(.semibold)

            GeometryReader { geometry in
                let chartWidth = geometry.size.width - 28
                let chartHeight = geometry.size.height - 30
                let barCount = CGFloat(viewModel.histogramValues.count)
                let barWidth = chartWidth / barCount

                ZStack(alignment: .bottomLeading) {
                    // Bars
                    HStack(spacing: 0) {
                        ForEach(0..<viewModel.histogramValues.count, id: \.self) { i in
                            let value = viewModel.histogramValues[i]
                            let height = maxVal > 0 ? CGFloat(value / maxVal) * chartHeight : 0

                            Rectangle()
                                .fill(Color.blue)
                                .frame(width: max(barWidth, 0.5), height: max(height, 0))
                                .frame(maxHeight: .infinity, alignment: .bottom)
                        }
                    }
                    .padding(.leading, 20)
                    .padding(.bottom, 20)

                    // Note labels
                    ForEach(0..<noteNames.count, id: \.self) { noteIdx in
                        let x = 20 + CGFloat(noteIdx) * chartWidth / 12.0
                        Text(noteNames[noteIdx])
                            .font(.system(size: 9))
                            .foregroundColor(.secondary)
                            .position(x: x, y: geometry.size.height - 6)
                    }
                }
            }
            .frame(height: 200)
        }
        .padding(12)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(8)
    }

    private var tonalSegmentsCard: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("Sung Notes (\(viewModel.tonalSegments.count))")
                .font(.subheadline)
                .fontWeight(.semibold)

            HStack {
                Text("Note").font(.caption).fontWeight(.medium).frame(maxWidth: .infinity, alignment: .leading)
                Text("Start").font(.caption).fontWeight(.medium).frame(maxWidth: .infinity, alignment: .leading)
                Text("Duration").font(.caption).fontWeight(.medium).frame(maxWidth: .infinity, alignment: .leading)
            }

            ForEach(viewModel.tonalSegments) { segment in
                HStack {
                    Text(segment.label).font(.caption2).fontWeight(.medium).frame(maxWidth: .infinity, alignment: .leading)
                    Text(String(format: "%.2f s", segment.startTime)).font(.caption2).frame(maxWidth: .infinity, alignment: .leading)
                    Text(String(format: "%.2f s", segment.endTime - segment.startTime)).font(.caption2).frame(maxWidth: .infinity, alignment: .leading)
                }
            }

            Text("Also see: PitchAnalysis.fitLinearSegments() for pitch slope analysis")
                .font(.caption2)
                .foregroundColor(.secondary)
                .italic()
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
            Text("PitchAnalysis.computeHistogram(config: FOLDED, numBins: 240)")
                .font(.caption2)
                .foregroundColor(.secondary)
            Text("PitchAnalysis.labelByMeanPitch() - note labeling (merged)")
                .font(.caption2)
                .foregroundColor(.secondary)
            Text("PitchDetection.createContourExtractor() - pitch extraction")
                .font(.caption2)
                .foregroundColor(.secondary)
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }
}

typealias PitchAnalysisSection = PitchAnalysisView

#Preview {
    PitchAnalysisView()
        .padding()
}
