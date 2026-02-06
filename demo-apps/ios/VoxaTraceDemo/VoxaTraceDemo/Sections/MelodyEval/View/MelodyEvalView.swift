import SwiftUI
import VoxaTrace

/// Melody evaluation view with singalong mode.
/// Students hear the reference melody (first 4 segments of Alankaar 01) while recording.
struct MelodyEvalView: View {
    @StateObject private var viewModel = MelodyEvalViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Melody Singalong")
                .font(.headline)

            Text(viewModel.status)
                .font(.caption)
                .foregroundColor(.secondary)

            // Step 1: Load reference
            if !viewModel.referenceLoaded {
                loadReferenceSection
            } else {
                // Pattern display showing what to sing
                patternDisplaySection

                // Step 2: Singalong (play + record)
                singalongSection

                // Results (shown after evaluation)
                if let result = viewModel.result {
                    resultsSection(result)
                }
            }
        }
        .onDisappear {
            viewModel.onDisappear()
        }
    }

    private var loadReferenceSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Step 1: Load Reference")
                .font(.subheadline)
                .fontWeight(.medium)

            Text("First 4 phrases of \(viewModel.referenceName):")
                .font(.caption)
                .foregroundColor(.secondary)

            // Preview what will be loaded
            HStack(spacing: 8) {
                ForEach(viewModel.segmentNames, id: \.self) { name in
                    Text(name)
                        .font(.caption)
                        .fontWeight(.medium)
                        .padding(.horizontal, 10)
                        .padding(.vertical, 6)
                        .background(Color(.secondarySystemBackground))
                        .cornerRadius(6)
                }
            }

            Button("Load \(viewModel.referenceName)") {
                viewModel.loadReference()
            }
            .buttonStyle(.borderedProminent)
        }
    }

    private var patternDisplaySection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Phrases to Sing:")
                .font(.caption)
                .foregroundColor(.secondary)

            ScrollView(.horizontal, showsIndicators: false) {
                HStack(spacing: 8) {
                    ForEach(0..<viewModel.segmentNames.count, id: \.self) { index in
                        segmentChip(index: index)
                    }
                }
            }
        }
        .padding(12)
        .background(Color.blue.opacity(0.1))
        .cornerRadius(8)
    }

    private func segmentChip(index: Int) -> some View {
        let isActive = viewModel.currentSegmentIndex == index
        let score = viewModel.segmentScore(at: index)

        let backgroundColor: Color = {
            if let score = score {
                if score < 0 {
                    return .gray // Special case
                } else if score >= 0.8 {
                    return .green
                } else if score >= 0.6 {
                    return .orange
                } else {
                    return .red
                }
            } else if isActive {
                return .blue
            } else {
                return Color(.secondarySystemBackground)
            }
        }()

        let foregroundColor: Color = (score != nil || isActive) ? .white : .primary

        return VStack(spacing: 2) {
            Text(viewModel.segmentNames[index])
                .font(.caption)
                .fontWeight(.medium)
            if let score = score {
                Text(score < -1.5 ? "No voice" : score < 0 ? "N/A" : "\(Int(score * 100))%")
                    .font(.system(size: 10))
            }
        }
        .padding(.horizontal, 12)
        .padding(.vertical, 8)
        .background(backgroundColor)
        .foregroundColor(foregroundColor)
        .cornerRadius(8)
        .animation(.easeInOut(duration: 0.2), value: isActive)
    }

    private var singalongSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Step 2: Singalong")
                .font(.subheadline)
                .fontWeight(.medium)

            Text("Listen to the reference and sing along")
                .font(.caption)
                .foregroundColor(.secondary)

            HStack {
                if viewModel.isSingalongActive {
                    Button {
                        viewModel.stopSingalong()
                    } label: {
                        HStack(spacing: 6) {
                            Image(systemName: "stop.fill")
                            Text("Stop")
                        }
                    }
                    .buttonStyle(.borderedProminent)
                    .tint(.red)

                    Spacer()

                    HStack(spacing: 4) {
                        Circle()
                            .fill(Color.red)
                            .frame(width: 10, height: 10)
                        Text(MusicUtils.formatTime(viewModel.recordingDuration))
                            .font(.caption)
                            .monospacedDigit()
                    }
                } else if viewModel.isPreparing {
                    Button {
                        // Disabled while preparing
                    } label: {
                        HStack(spacing: 6) {
                            ProgressView()
                                .scaleEffect(0.8)
                            Text("Preparing...")
                        }
                    }
                    .buttonStyle(.borderedProminent)
                    .disabled(true)
                } else {
                    Button {
                        viewModel.startSingalong()
                    } label: {
                        HStack(spacing: 6) {
                            Image(systemName: "music.mic")
                            Text(viewModel.hasRecording ? "Try Again" : "Start Singalong")
                        }
                    }
                    .buttonStyle(.borderedProminent)
                    .disabled(!viewModel.isReady || viewModel.isEvaluating)

                    if viewModel.hasRecording && !viewModel.isEvaluating {
                        Spacer()
                        Image(systemName: "checkmark.circle.fill")
                            .foregroundColor(.green)
                    }
                }
            }

            if viewModel.isSingalongActive {
                HStack {
                    Text("Level:")
                        .font(.caption)
                    ProgressView(value: Double(min(max(viewModel.recordingLevel, 0), 1)))
                }
            }

            if viewModel.isEvaluating {
                HStack {
                    ProgressView()
                        .scaleEffect(0.8)
                    Text("Evaluating...")
                        .font(.caption)
                        .foregroundColor(.secondary)
                }
            }
        }
    }

    private func resultsSection(_ result: SingingResult) -> some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Results")
                .font(.subheadline)
                .fontWeight(.medium)

            // Overall score
            overallScoreCard(result)

            // Per-segment breakdown
            if !result.segmentResults.isEmpty {
                Text("Per-Phrase Scores")
                    .font(.caption)
                    .foregroundColor(.secondary)

                ForEach(result.sortedSegmentResults, id: \.index) { entry in
                    if let segmentResult = entry.attempts.first {
                        segmentRow(index: entry.index, result: segmentResult)
                    }
                }
            }
        }
    }

    private func overallScoreCard(_ result: SingingResult) -> some View {
        let backgroundColor: Color = {
            if result.overallScore < 0 {
                return .gray // Special case
            } else if result.overallScore >= 0.8 {
                return .green
            } else if result.overallScore >= 0.6 {
                return .orange
            } else {
                return .red
            }
        }()

        let feedbackMessage: String = {
            if result.overallScore >= 0.9 {
                return "Excellent! Perfect performance."
            } else if result.overallScore >= 0.8 {
                return "Great job! Almost perfect."
            } else if result.overallScore >= 0.7 {
                return "Good job! Keep practicing."
            } else if result.overallScore >= 0.5 {
                return "Not bad. Focus on pitch accuracy."
            } else {
                return "Keep practicing! Match each phrase carefully."
            }
        }()

        return VStack(spacing: 4) {
            Text("Overall Score")
                .font(.caption)
                .foregroundColor(.white.opacity(0.8))
            Text(result.overallScore >= 0 ? "\(Int(result.overallScore * 100))%" : "N/A")
                .font(.largeTitle)
                .fontWeight(.bold)
                .foregroundColor(.white)
            Text(feedbackMessage)
                .font(.caption)
                .foregroundColor(.white.opacity(0.9))
        }
        .frame(maxWidth: .infinity)
        .padding(16)
        .background(backgroundColor)
        .cornerRadius(12)
    }

    private func segmentRow(index: Int, result: SegmentResult) -> some View {
        let backgroundColor: Color = {
            if result.score < 0 {
                return .gray.opacity(0.2) // Special case
            } else if result.score >= 0.8 {
                return .green.opacity(0.2)
            } else if result.score >= 0.6 {
                return .orange.opacity(0.2)
            } else {
                return .red.opacity(0.2)
            }
        }()

        let phraseName = index < viewModel.segmentNames.count ? viewModel.segmentNames[index] : "Phrase \(index + 1)"

        return HStack {
            Text(phraseName)
                .font(.caption)
                .fontWeight(.medium)
            Text("(\(result.segment.lyrics.trimmingCharacters(in: .whitespaces)))")
                .font(.caption)
                .foregroundColor(.secondary)
                .lineLimit(1)
            Spacer()
            Text(result.score < -1.5 ? "No voice" : result.score < 0 ? "N/A" : "\(Int(result.score * 100))%")
                .font(.caption)
                .fontWeight(.medium)
        }
        .padding(8)
        .background(backgroundColor)
        .cornerRadius(6)
    }
}

/// Backward compatibility alias.
typealias MelodyEvalSection = MelodyEvalView

#Preview {
    MelodyEvalView()
        .padding()
}
