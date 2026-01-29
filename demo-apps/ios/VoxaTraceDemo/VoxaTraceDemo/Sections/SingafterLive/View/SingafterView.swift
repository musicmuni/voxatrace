import SwiftUI
import VoxaTrace

/// Singafter practice view demonstrating CalibraLiveEval with singafter mode.
struct SingafterView: View {
    @StateObject private var viewModel = SingafterViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Singafter Practice (Chalan)")
                .font(.headline)

            Text(viewModel.status)
                .font(.caption)
                .foregroundColor(.secondary)

            if !viewModel.lessonLoaded {
                Button("Load Lesson") {
                    viewModel.loadLesson()
                }
                .buttonStyle(.borderedProminent)
            } else {
                // Phrase pair selector
                phraseSelector

                // Current phrase lyrics
                if !viewModel.phrasePairs.isEmpty {
                    Text(viewModel.currentLyrics)
                        .font(.body)
                        .padding(12)
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .background(Color.blue.opacity(0.1))
                        .cornerRadius(8)
                }

                // Phase indicator
                phaseIndicators

                // Practice controls
                practiceControls

                // Score display after evaluation
                if viewModel.practicePhase == .evaluated {
                    scoreCard
                }
            }
        }
        .onDisappear {
            viewModel.onDisappear()
        }
    }

    private var phraseSelector: some View {
        HStack {
            Text("Phrase \(viewModel.currentPairIndex + 1) of \(viewModel.phrasePairs.count)")
                .font(.subheadline)

            Spacer()

            HStack(spacing: 8) {
                Button("<") {
                    viewModel.navigatePrevious()
                }
                .disabled(!viewModel.canNavigatePrevious)

                Button(">") {
                    viewModel.navigateNext()
                }
                .disabled(!viewModel.canNavigateNext)
            }
            .buttonStyle(.bordered)
        }
    }

    private var phaseIndicators: some View {
        HStack {
            Spacer()
            PhaseIndicatorView(
                label: "Listen",
                isActive: viewModel.practicePhase == .listening,
                isComplete: viewModel.practicePhase == .singing || viewModel.practicePhase == .evaluated
            )
            Text("\u{2192}")
            PhaseIndicatorView(
                label: "Sing",
                isActive: viewModel.practicePhase == .singing,
                isComplete: viewModel.practicePhase == .evaluated
            )
            Text("\u{2192}")
            PhaseIndicatorView(
                label: "Score",
                isActive: viewModel.practicePhase == .evaluated,
                isComplete: false
            )
            Spacer()
        }
    }

    private var practiceControls: some View {
        HStack {
            switch viewModel.practicePhase {
            case .idle, .evaluated:
                Button("Start Practice") {
                    viewModel.startPractice()
                }
                .buttonStyle(.borderedProminent)
                .frame(maxWidth: .infinity)

            case .listening, .singing:
                Button("Stop") {
                    viewModel.forceStop()
                }
                .buttonStyle(.borderedProminent)
                .tint(.red)
                .frame(maxWidth: .infinity)
            default:
                EmptyView()
            }
        }
    }

    private var scoreCard: some View {
        let backgroundColor: Color = {
            if viewModel.segmentScore >= 0.8 {
                return .green
            } else if viewModel.segmentScore >= 0.6 {
                return .orange
            } else {
                return .red
            }
        }()

        return VStack(spacing: 8) {
            Text("\(Int(viewModel.segmentScore * 100))%")
                .font(.largeTitle)
                .fontWeight(.bold)
                .foregroundColor(.white)
            Text(viewModel.feedbackMessage)
                .foregroundColor(.white)
        }
        .frame(maxWidth: .infinity)
        .padding(16)
        .background(backgroundColor)
        .cornerRadius(12)
    }
}

// MARK: - Phase Indicator

private struct PhaseIndicatorView: View {
    let label: String
    let isActive: Bool
    let isComplete: Bool

    var body: some View {
        VStack(spacing: 4) {
            ZStack {
                Circle()
                    .fill(backgroundColor)
                    .frame(width: 32, height: 32)
                if isComplete {
                    Text("\u{2713}")
                        .font(.caption)
                        .fontWeight(.bold)
                        .foregroundColor(.white)
                }
            }
            Text(label)
                .font(.caption)
                .foregroundColor(isActive ? .blue : .secondary)
        }
    }

    private var backgroundColor: Color {
        if isActive {
            return .blue
        } else if isComplete {
            return .green
        } else {
            return Color(.secondarySystemBackground)
        }
    }
}

/// Backward compatibility alias.
typealias SingafterLiveEvalSection = SingafterView

#Preview {
    SingafterView()
        .padding()
}
