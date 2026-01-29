import SwiftUI
import VoxaTrace

/// Session summary screen showing overall results and phrase pair scores.
struct SingafterSummaryView: View {
    let phrasePairs: [PhrasePair]
    let completedResults: [Int: [SegmentResult]]
    let onPracticeAgain: () -> Void

    @State private var selectedAggregation: ResultAggregation = .latest

    var body: some View {
        ScrollView {
            VStack(spacing: 20) {
                Text("Session Complete")
                    .font(.title2)
                    .fontWeight(.bold)

                // Overall score
                overallScoreView

                // Aggregation picker
                VStack(alignment: .leading, spacing: 8) {
                    Text("Score Mode")
                        .font(.caption)
                        .foregroundColor(.secondary)

                    Picker("Score Mode", selection: $selectedAggregation) {
                        Text("Latest").tag(ResultAggregation.latest)
                        Text("Best").tag(ResultAggregation.best)
                        Text("Average").tag(ResultAggregation.average)
                    }
                    .pickerStyle(.segmented)
                }

                Divider()

                // Phrase pair results grid
                VStack(alignment: .leading, spacing: 8) {
                    Text("Phrase Results")
                        .font(.subheadline)
                        .fontWeight(.semibold)

                    LazyVGrid(columns: [GridItem(.flexible()), GridItem(.flexible())], spacing: 12) {
                        ForEach(0..<phrasePairs.count, id: \.self) { index in
                            if let results = completedResults[index],
                               let result = getResult(from: results) {
                                PhraseResultCard(
                                    phraseIndex: index,
                                    lyrics: phrasePairs[index].lyrics,
                                    result: result
                                )
                            } else {
                                notPracticedCard(index: index)
                            }
                        }
                    }
                }

                Divider()

                // Statistics
                statisticsView

                // Practice again
                Button("Practice Again", action: onPracticeAgain)
                    .buttonStyle(.borderedProminent)
                    .frame(maxWidth: .infinity)
            }
            .padding()
        }
    }

    private var overallScoreView: some View {
        let score = computeOverallScore()
        let level = PerformanceLevel.fromScore(score)

        return VStack(spacing: 8) {
            Text("\(Int(score * 100))%")
                .font(.system(size: 64, weight: .bold))
                .foregroundColor(scoreColor(score))

            Text(level.displayName)
                .font(.title3)
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity)
        .padding(24)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(16)
    }

    private var statisticsView: some View {
        let scores = completedResults.values.compactMap { getResult(from: $0)?.score }

        return VStack(alignment: .leading, spacing: 8) {
            Text("Statistics")
                .font(.subheadline)
                .fontWeight(.semibold)

            HStack {
                StatItem(label: "Practiced", value: "\(completedResults.count)/\(phrasePairs.count)")
                Spacer()
                StatItem(label: "Best", value: scores.isEmpty ? "-" : "\(Int((scores.max() ?? 0) * 100))%")
                Spacer()
                StatItem(label: "Average", value: scores.isEmpty ? "-" : "\(Int((scores.reduce(0, +) / Float(scores.count)) * 100))%")
            }
            .padding(12)
            .background(Color(.secondarySystemBackground))
            .cornerRadius(8)
        }
    }

    private func notPracticedCard(index: Int) -> some View {
        VStack(spacing: 4) {
            Text("Phrase \(index + 1)")
                .font(.caption2)
                .foregroundColor(.secondary)
            Text("Not practiced")
                .font(.caption)
                .foregroundColor(.secondary)
                .frame(height: 60)
        }
        .padding(8)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(8)
    }

    private func getResult(from results: [SegmentResult]) -> SegmentResult? {
        guard !results.isEmpty else { return nil }
        switch selectedAggregation {
        case .latest: return results.last
        case .best: return results.max(by: { $0.score < $1.score })
        case .average: return results.last
        default: return results.last
        }
    }

    private func computeOverallScore() -> Float {
        let scores = completedResults.values.compactMap { getResult(from: $0)?.score }
        guard !scores.isEmpty else { return 0 }

        switch selectedAggregation {
        case .latest: return scores.last ?? 0
        case .best: return scores.max() ?? 0
        case .average: return scores.reduce(0, +) / Float(scores.count)
        default: return scores.last ?? 0
        }
    }

    private func scoreColor(_ score: Float) -> Color {
        if score >= 0.8 { return .green }
        if score >= 0.6 { return .orange }
        return .red
    }
}

// MARK: - Phrase Result Card

private struct PhraseResultCard: View {
    let phraseIndex: Int
    let lyrics: String
    let result: SegmentResult

    var body: some View {
        VStack(spacing: 4) {
            Text("Phrase \(phraseIndex + 1)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("\(Int(result.score * 100))%")
                .font(.title2)
                .fontWeight(.bold)
                .foregroundColor(scoreColor)

            Text(lyrics)
                .font(.caption2)
                .foregroundColor(.secondary)
                .lineLimit(1)

            Text(PerformanceLevel.fromScore(result.score).displayName)
                .font(.caption)
                .foregroundColor(.secondary)
        }
        .padding(8)
        .frame(maxWidth: .infinity)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(8)
    }

    private var scoreColor: Color {
        if result.score >= 0.8 { return .green }
        if result.score >= 0.6 { return .orange }
        return .red
    }
}

// MARK: - Stat Item

private struct StatItem: View {
    let label: String
    let value: String

    var body: some View {
        VStack(spacing: 2) {
            Text(label)
                .font(.caption2)
                .foregroundColor(.secondary)
            Text(value)
                .font(.subheadline)
                .fontWeight(.semibold)
        }
    }
}
