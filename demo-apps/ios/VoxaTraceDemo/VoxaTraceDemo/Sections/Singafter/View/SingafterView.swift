import SwiftUI
import VoxaTrace

/// Main view for Singafter Practice demo.
///
/// This view is purely declarative UI - all business logic lives in SingafterViewModel.
/// Demonstrates clean MVVM separation where:
/// - View: Renders UI based on ViewModel state
/// - ViewModel: Manages CalibraLiveEval session and transforms state
/// - Repository: Handles data loading
struct SingafterView: View {
    @StateObject private var viewModel = SingafterViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            header
            content
        }
        .onAppear {
            Task {
                await viewModel.onAppear()
            }
        }
        .onDisappear {
            viewModel.onDisappear()
        }
    }

    // MARK: - Header

    private var header: some View {
        HStack {
            Text("Singafter Practice")
                .font(.headline)
            Spacer()
            PresetPicker(selectedPreset: viewModel.selectedPreset) { preset in
                Task { await viewModel.changePreset(preset) }
            }
        }
    }

    // MARK: - Content

    @ViewBuilder
    private var content: some View {
        switch viewModel.uiState {
        case .idle, .loading:
            loadingView

        case .error(let message):
            errorView(message)

        case .ready, .practicing:
            practiceView

        case .summary:
            summaryView
        }
    }

    // MARK: - Loading

    private var loadingView: some View {
        HStack {
            ProgressView().scaleEffect(0.8)
            Text("Loading lesson...")
                .font(.caption)
                .foregroundColor(.secondary)
        }
    }

    // MARK: - Error

    private func errorView(_ message: String) -> some View {
        HStack {
            Image(systemName: "exclamationmark.triangle.fill")
                .foregroundColor(.orange)
            Text(message)
                .font(.caption)
                .foregroundColor(.secondary)
        }
    }

    // MARK: - Practice

    private var practiceView: some View {
        VStack(alignment: .leading, spacing: 12) {
            // Progress bar
            SegmentProgressBar(
                segmentCount: viewModel.phrasePairs.count,
                currentIndex: viewModel.currentPairIndex,
                completedIndices: viewModel.completedPairIndices,
                isDisabled: false,
                onSegmentTap: viewModel.goToPair
            )

            // Status message
            Text(viewModel.statusMessage)
                .font(.caption)
                .foregroundColor(.secondary)

            Divider()

            // Lyrics
            if !viewModel.currentLyrics.isEmpty {
                LyricsCard(lyrics: viewModel.currentLyrics)
            }

            // Phase indicators
            phaseIndicators

            // Live pitch feedback (only during singing phase)
            if viewModel.practicePhase == .singing {
                PitchFeedbackCard(
                    pitch: viewModel.currentPitch,
                    progress: viewModel.segmentProgress,
                    noteName: MusicUtils.getMidiNoteName(viewModel.currentPitch)
                )
            }

            // Last result
            if let result = viewModel.lastResult {
                SegmentResultCard(result: result)
            }

            // Practice button
            PracticeButton(
                isPracticing: viewModel.isPracticing,
                onStart: viewModel.play,
                onStop: viewModel.pause
            )

            Divider()

            // Navigation
            NavigationControls(
                canGoPrevious: viewModel.canGoPrevious,
                canGoNext: viewModel.canGoNext,
                canRetry: viewModel.canRetry,
                onPrevious: viewModel.previousPair,
                onRetry: viewModel.retry,
                onNext: viewModel.nextPair
            )

            // Session controls
            SessionControls(
                onReset: viewModel.reset,
                onFinish: viewModel.finish
            )
        }
    }

    // MARK: - Phase Indicators

    private var phaseIndicators: some View {
        HStack {
            Spacer()
            PhaseIndicatorView(
                label: "Listen",
                isActive: viewModel.practicePhase == .listening,
                isComplete: viewModel.practicePhase == .singing || viewModel.practicePhase == .evaluated
            )
            Image(systemName: "arrow.right")
                .font(.caption)
                .foregroundColor(.secondary)
            PhaseIndicatorView(
                label: "Sing",
                isActive: viewModel.practicePhase == .singing,
                isComplete: viewModel.practicePhase == .evaluated
            )
            Image(systemName: "arrow.right")
                .font(.caption)
                .foregroundColor(.secondary)
            PhaseIndicatorView(
                label: "Score",
                isActive: viewModel.practicePhase == .evaluated,
                isComplete: false
            )
            Spacer()
        }
    }

    // MARK: - Summary

    private var summaryView: some View {
        SingafterSummaryView(
            phrasePairs: viewModel.phrasePairs,
            completedResults: viewModel.completedResults,
            onPracticeAgain: viewModel.reset
        )
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
                    Image(systemName: "checkmark")
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
