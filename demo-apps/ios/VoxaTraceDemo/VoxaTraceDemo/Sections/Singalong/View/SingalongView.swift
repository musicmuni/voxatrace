import SwiftUI
import VoxaTrace

/// Main view for Singalong Practice demo.
///
/// This view is purely declarative UI - all business logic lives in SingalongViewModel.
/// Demonstrates clean MVVM separation where:
/// - View: Renders UI based on ViewModel state
/// - ViewModel: Manages CalibraLiveEval session and transforms state
/// - Repository: Handles data loading
struct SingalongView: View {
    @StateObject private var viewModel = SingalongViewModel()

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
            Text("Singalong Practice")
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
                segmentCount: viewModel.segments.count,
                currentIndex: viewModel.currentSegmentIndex,
                completedIndices: viewModel.completedSegmentIndices,
                isDisabled: false,  // Allow seeking during practice
                onSegmentTap: viewModel.goToSegment
            )

            Divider()

            // Lyrics
            if !viewModel.currentLyrics.isEmpty {
                LyricsCard(lyrics: viewModel.currentLyrics)
            }

            // Live pitch feedback (only during practice)
            if viewModel.isPracticing {
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
                onPrevious: viewModel.previousSegment,
                onRetry: viewModel.retry,
                onNext: viewModel.nextSegment
            )

            // Session controls
            SessionControls(
                onReset: viewModel.reset,
                onFinish: viewModel.finish
            )
        }
    }

    // MARK: - Summary

    private var summaryView: some View {
        SingalongSummaryView(
            segments: viewModel.segments,
            completedResults: viewModel.completedResults,
            selectedAggregation: .constant(.latest),
            onPracticeAgain: viewModel.reset
        )
    }
}

// MARK: - Backward Compatibility Alias

/// Alias for backward compatibility with existing navigation.
typealias SingalongSection = SingalongView
