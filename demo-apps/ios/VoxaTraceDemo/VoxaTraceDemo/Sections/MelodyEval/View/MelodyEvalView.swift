import SwiftUI
import VoxaTrace

/// Melody evaluation view demonstrating CalibraMelodyEval for offline evaluation.
struct MelodyEvalView: View {
    @StateObject private var viewModel = MelodyEvalViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Offline Melody Evaluation")
                .font(.headline)

            Text(viewModel.status)
                .font(.caption)
                .foregroundColor(.secondary)

            // Step 1: Load reference
            if !viewModel.referenceLoaded {
                loadReferenceSection
            } else {
                // Reference info
                referenceInfoSection

                // Step 2: Record performance
                recordingSection

                // Step 3: Evaluate
                if viewModel.hasRecording && !viewModel.isRecording {
                    evaluateSection
                }

                // Step 4: Results
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

            Button("Load \(viewModel.referenceName)") {
                viewModel.loadReference()
            }
            .buttonStyle(.borderedProminent)
        }
    }

    private var referenceInfoSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack {
                Image(systemName: "checkmark.circle.fill")
                    .foregroundColor(.green)
                Text("Reference: \(viewModel.referenceName)")
                    .font(.subheadline)
            }

            Text("\(viewModel.segments.count) segments loaded")
                .font(.caption)
                .foregroundColor(.secondary)
        }
        .padding(8)
        .background(Color.green.opacity(0.1))
        .cornerRadius(8)
    }

    private var recordingSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Step 2: Record Your Performance")
                .font(.subheadline)
                .fontWeight(.medium)

            HStack {
                if viewModel.isRecording {
                    Button("Stop Recording") {
                        viewModel.stopRecording()
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
                } else {
                    Button(viewModel.hasRecording ? "Re-record" : "Start Recording") {
                        viewModel.startRecording()
                    }
                    .buttonStyle(.borderedProminent)

                    if viewModel.hasRecording {
                        Spacer()
                        Image(systemName: "checkmark.circle.fill")
                            .foregroundColor(.green)
                        Text("Recording ready")
                            .font(.caption)
                    }
                }
            }

            if viewModel.isRecording {
                HStack {
                    Text("Level:")
                        .font(.caption)
                    ProgressView(value: Double(min(max(viewModel.recordingLevel, 0), 1)))
                }
            }
        }
    }

    private var evaluateSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Step 3: Evaluate")
                .font(.subheadline)
                .fontWeight(.medium)

            Button(viewModel.isEvaluating ? "Evaluating..." : "Evaluate Performance") {
                viewModel.evaluate()
            }
            .buttonStyle(.borderedProminent)
            .disabled(viewModel.isEvaluating)
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
                Text("Per-Segment Scores")
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
            if result.overallScore >= 0.8 {
                return .green
            } else if result.overallScore >= 0.6 {
                return .orange
            } else {
                return .red
            }
        }()

        return VStack(spacing: 4) {
            Text("Overall Score")
                .font(.caption)
                .foregroundColor(.white.opacity(0.8))
            Text("\(Int(result.overallScore * 100))%")
                .font(.largeTitle)
                .fontWeight(.bold)
                .foregroundColor(.white)
        }
        .frame(maxWidth: .infinity)
        .padding(16)
        .background(backgroundColor)
        .cornerRadius(12)
    }

    private func segmentRow(index: Int, result: SegmentResult) -> some View {
        let backgroundColor: Color = {
            if result.score >= 0.8 {
                return .green.opacity(0.2)
            } else if result.score >= 0.6 {
                return .orange.opacity(0.2)
            } else {
                return .red.opacity(0.2)
            }
        }()

        return HStack {
            Text("Segment \(index + 1)")
                .font(.caption)
            if !result.segment.lyrics.isEmpty {
                Text("(\(result.segment.lyrics))")
                    .font(.caption)
                    .foregroundColor(.secondary)
                    .lineLimit(1)
            }
            Spacer()
            Text("\(Int(result.score * 100))%")
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
