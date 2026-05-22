import SwiftUI
import VoxaTrace

/// Intonation analysis view demonstrating Accura for pitch accuracy measurement.
struct IntonationView: View {
    @StateObject private var viewModel = IntonationViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Intonation Analysis")
                .font(.headline)

            Text("Analyze pitch accuracy against Equal Temperament and Just Intonation")
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

            if let eqResult = viewModel.eqResult {
                resultSection(
                    title: "Equal Temperament",
                    result: eqResult,
                    score: viewModel.eqScore
                )
            }

            if let jiResult = viewModel.jiResult {
                resultSection(
                    title: "Just Intonation",
                    result: jiResult,
                    score: viewModel.jiScore
                )
            }

            apiInfoCard
        }
    }

    // MARK: - Result Section

    private func resultSection(
        title: String,
        result: IntonationAnalysisResult,
        score: PitchingScore?
    ) -> some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(title)
                .font(.subheadline)
                .fontWeight(.semibold)

            HStack {
                VStack(alignment: .leading) {
                    Text("Score")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    if let score = score {
                        Text(String(format: "%.0f", score.score))
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundColor(scoreColor(score.score))
                    } else {
                        Text("--")
                            .font(.title2)
                    }
                }

                Spacer()

                VStack(alignment: .trailing) {
                    Text("Notes")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    Text("\(result.notes.count)")
                        .font(.title2)
                        .fontWeight(.bold)
                }
            }

            // Per-note rows
            ForEach(Array(result.notes.enumerated()), id: \.offset) { _, note in
                noteRow(note)
            }
        }
        .padding(12)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(8)
    }

    private func noteRow(_ note: NoteAnalysis) -> some View {
        HStack {
            Text(note.label)
                .font(.subheadline)
                .fontWeight(.medium)
                .frame(width: 40, alignment: .leading)

            Text(String(format: "%+.1f cents", note.deviationCents))
                .font(.caption)
                .foregroundColor(.secondary)

            Spacer()

            Text(String(format: "%.0f", note.score))
                .font(.caption)
                .foregroundColor(scoreColor(note.score))
        }
    }

    // MARK: - API Info Card

    private var apiInfoCard: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("APIs Demonstrated:")
                .font(.caption)
                .fontWeight(.medium)

            Text("Accura.analyzePitching() - intonation analysis (EQ/JI)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("Accura.calculateScore() - overall pitch accuracy score")
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

    private func scoreColor(_ score: Float) -> Color {
        if score >= 70 { return .green }
        if score >= 40 { return .orange }
        return .red
    }
}

/// Backward compatibility alias.
typealias IntonationSection = IntonationView

#Preview {
    IntonationView()
        .padding()
}
