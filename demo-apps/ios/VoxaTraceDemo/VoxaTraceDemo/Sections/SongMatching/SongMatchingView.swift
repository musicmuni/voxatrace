import SwiftUI
import VoxaTrace

/// Song matching view demonstrating TesseraRange search vector and match APIs.
struct SongMatchingView: View {
    @StateObject private var viewModel = SongMatchingViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Song Matching")
                .font(.headline)

            Text("Match a singer's voice to a song using search vectors")
                .font(.subheadline)
                .foregroundColor(.secondary)

            Button("Match Singer to Song") {
                viewModel.matchSingerToSong()
            }
            .buttonStyle(.borderedProminent)
            .disabled(viewModel.isAnalyzing)

            if viewModel.isAnalyzing {
                ProgressView()
                    .frame(maxWidth: .infinity)
            }

            if let error = viewModel.errorMessage {
                Text(error)
                    .font(.subheadline)
                    .foregroundColor(.red)
            }

            if viewModel.hasResult {
                matchResultCard
            }

            apiInfoCard
        }
    }

    // MARK: - Match Result Card

    private var matchResultCard: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Match Results")
                .font(.subheadline)
                .fontWeight(.semibold)

            HStack {
                VStack(alignment: .leading) {
                    Text("Similarity")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    if let similarity = viewModel.similarity {
                        Text(String(format: "%.2f", similarity))
                            .font(.title)
                            .fontWeight(.bold)
                            .foregroundColor(similarityColor(similarity))
                    }
                }

                Spacer()

                VStack(alignment: .trailing) {
                    Text("Difficulty")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    if let difficulty = viewModel.difficulty {
                        Text("\(difficulty) / 5")
                            .font(.title)
                            .fontWeight(.bold)
                    }
                }
            }

            // Similarity bar
            if let similarity = viewModel.similarity {
                ProgressView(value: Double(min(max(similarity, 0), 1)))
                    .tint(similarityColor(similarity))
            }

            Divider()

            // Vector dimensions
            HStack {
                Text("Singer vector: \(viewModel.singerVectorDims)-dim")
                    .font(.caption)
                    .foregroundColor(.secondary)
                Spacer()
                Text("Song vector: \(viewModel.songVectorDims)-dim")
                    .font(.caption)
                    .foregroundColor(.secondary)
            }

            // Difficulty stars
            if let difficulty = viewModel.difficulty {
                let filled = Int(difficulty)
                let empty = 5 - filled
                Text("Difficulty: " + String(repeating: "\u{2605}", count: filled) + String(repeating: "\u{2606}", count: empty))
                    .font(.subheadline)
            }
        }
        .padding(16)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(12)
    }

    // MARK: - API Info Card

    private var apiInfoCard: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("APIs Demonstrated:")
                .font(.caption)
                .fontWeight(.medium)

            Text("TesseraRange.computeSearchVector() - 13-dim search vector from contour")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("TesseraRange.computeMatch() - singer vs song similarity + difficulty")
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

    private func similarityColor(_ similarity: Float) -> Color {
        if similarity >= 0.7 { return .green }
        if similarity >= 0.4 { return .orange }
        return .red
    }
}

/// Backward compatibility alias.
typealias SongMatchingSection = SongMatchingView

#Preview {
    SongMatchingView()
        .padding()
}
