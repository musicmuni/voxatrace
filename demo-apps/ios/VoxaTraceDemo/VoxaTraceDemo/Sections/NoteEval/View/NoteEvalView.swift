import SwiftUI
import VoxaTrace

/// Note/Exercise evaluation view demonstrating CalibraNoteEval.
struct NoteEvalView: View {
    @StateObject private var viewModel = NoteEvalViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Note/Exercise Evaluation")
                .font(.headline)

            Text(viewModel.status)
                .font(.caption)
                .foregroundColor(.secondary)

            // Step 1: Select exercise
            exercisePickerSection

            // Visual pattern display
            patternDisplaySection

            // Step 2: Record
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
        .onDisappear {
            viewModel.onDisappear()
        }
    }

    private var exercisePickerSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Step 1: Select Exercise")
                .font(.subheadline)
                .fontWeight(.medium)

            Picker("Exercise", selection: Binding(
                get: { viewModel.selectedExercise },
                set: { viewModel.selectExercise($0) }
            )) {
                ForEach(0..<viewModel.exercises.count, id: \.self) { index in
                    Text(viewModel.exercises[index].0).tag(index)
                }
            }
            .pickerStyle(.menu)
        }
    }

    private var patternDisplaySection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Notes to Play:")
                .font(.caption)
                .foregroundColor(.secondary)

            ScrollView(.horizontal, showsIndicators: false) {
                HStack(spacing: 8) {
                    ForEach(0..<viewModel.currentMidiNotes.count, id: \.self) { index in
                        noteChip(
                            index: index,
                            midiNote: Int(viewModel.currentMidiNotes[index]),
                            result: viewModel.noteResult(at: index)
                        )
                    }
                }
            }
        }
        .padding(12)
        .background(Color.blue.opacity(0.1))
        .cornerRadius(8)
    }

    private func noteChip(index: Int, midiNote: Int, result: NoteResult?) -> some View {
        let noteName = MusicUtils.getMidiNoteName(MusicUtils.midiToHz(midiNote))

        let backgroundColor: Color = {
            guard let result = result else {
                return Color(.secondarySystemBackground)
            }
            if result.score >= 0.8 {
                return .green
            } else if result.score >= 0.5 {
                return .orange
            } else {
                return .red
            }
        }()

        let foregroundColor: Color = result != nil ? .white : .primary

        return VStack(spacing: 2) {
            Text(noteName)
                .font(.caption)
                .fontWeight(.medium)
            if let result = result {
                Text("\(Int(result.score * 100))%")
                    .font(.system(size: 10))
            }
        }
        .padding(.horizontal, 10)
        .padding(.vertical, 6)
        .background(backgroundColor)
        .foregroundColor(foregroundColor)
        .cornerRadius(6)
    }

    private var recordingSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Step 2: Record Your Performance")
                .font(.subheadline)
                .fontWeight(.medium)

            Text("Play each note for about 0.5 seconds")
                .font(.caption)
                .foregroundColor(.secondary)

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

    private func resultsSection(_ result: ExerciseResult) -> some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Results")
                .font(.subheadline)
                .fontWeight(.medium)

            // Overall score
            overallScoreCard(result)

            // Stats
            HStack {
                statBox(title: "Notes", value: "\(result.noteCount)")
                statBox(title: "Passing", value: "\(result.passingNotes)/\(result.noteCount)")
                statBox(title: "Pass Rate", value: "\(Int(result.passingRatio * 100))%")
            }
        }
    }

    private func overallScoreCard(_ result: ExerciseResult) -> some View {
        let backgroundColor: Color = {
            if result.score >= 0.8 {
                return .green
            } else if result.score >= 0.6 {
                return .orange
            } else {
                return .red
            }
        }()

        let feedbackMessage: String = {
            if result.score >= 0.9 {
                return "Excellent! Perfect performance."
            } else if result.score >= 0.8 {
                return "Great job! Almost perfect."
            } else if result.score >= 0.7 {
                return "Good job! Keep practicing."
            } else if result.score >= 0.5 {
                return "Not bad. Focus on pitch accuracy."
            } else {
                return "Keep practicing! Match each note carefully."
            }
        }()

        return VStack(spacing: 4) {
            Text("Overall Score")
                .font(.caption)
                .foregroundColor(.white.opacity(0.8))
            Text("\(result.scorePercent)%")
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

    private func statBox(title: String, value: String) -> some View {
        VStack(spacing: 2) {
            Text(title)
                .font(.caption2)
                .foregroundColor(.secondary)
            Text(value)
                .font(.caption)
                .fontWeight(.medium)
        }
        .frame(maxWidth: .infinity)
        .padding(8)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(6)
    }
}

/// Backward compatibility alias.
typealias NoteEvalSection = NoteEvalView

#Preview {
    NoteEvalView()
        .padding()
}
