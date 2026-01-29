import SwiftUI
import Charts
import VoxaTrace

/// Contour cleanup comparison view - compares RAW vs SCORING vs DISPLAY presets.
struct ContourCleanupView: View {
    @StateObject private var viewModel = ContourCleanupViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            algorithmPickerSection
            recordingSection

            if viewModel.rawContour != nil {
                Divider()
                displayTogglesSection
                graphSection
                statisticsSection
            }

            Divider()
            apiInfoSection
        }
        .onDisappear {
            viewModel.onDisappear()
        }
    }

    private var algorithmPickerSection: some View {
        HStack {
            Text("Algorithm:")
                .font(.caption)
            Picker("Algorithm", selection: $viewModel.selectedAlgorithm) {
                ForEach(0..<viewModel.algorithms.count, id: \.self) { index in
                    Text(viewModel.algorithms[index].name).tag(index)
                }
            }
            .pickerStyle(.segmented)
        }
    }

    private var recordingSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Record Audio Sample")
                .font(.subheadline)
                .fontWeight(.semibold)

            Text("Record a vocal sample to compare cleanup presets.")
                .font(.caption)
                .foregroundColor(.secondary)

            HStack {
                Button(viewModel.isRecording ? "Stop Recording" : "Start Recording") {
                    viewModel.toggleRecording()
                }
                .buttonStyle(.borderedProminent)
                .disabled(viewModel.isProcessing)

                if viewModel.isRecording {
                    Circle()
                        .fill(.red)
                        .frame(width: 12, height: 12)
                    Text("Recording...")
                        .font(.caption)
                        .foregroundColor(.red)
                }

                if viewModel.isProcessing {
                    ProgressView()
                        .padding(.leading, 8)
                    Text("Processing...")
                        .font(.caption)
                }
            }

            if viewModel.rawContour != nil {
                Button("Clear & Record New") {
                    viewModel.clearResults()
                }
                .buttonStyle(.bordered)
            }
        }
    }

    private var displayTogglesSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Display Presets")
                .font(.subheadline)
                .fontWeight(.semibold)

            HStack(spacing: 12) {
                ToggleChip(label: "RAW", color: .gray, isOn: $viewModel.showRaw)
                ToggleChip(label: "SCORING", color: .green, isOn: $viewModel.showScoring)
                ToggleChip(label: "DISPLAY", color: .blue, isOn: $viewModel.showDisplay)
            }
        }
    }

    private var graphSection: some View {
        VStack(alignment: .leading, spacing: 4) {
            var contours: [(pitches: [Float], color: Color, label: String)] = []

            let _ = {
                if viewModel.showRaw, let raw = viewModel.rawContour {
                    contours.append((raw.pitchesHz, .gray, "RAW"))
                }
                if viewModel.showScoring, let scoring = viewModel.scoringContour {
                    contours.append((scoring.pitchesHz, .green, "SCORING"))
                }
                if viewModel.showDisplay, let display = viewModel.displayContour {
                    contours.append((display.pitchesHz, .blue, "DISPLAY"))
                }
            }()

            if contours.isEmpty {
                Text("Select at least one preset to display")
                    .foregroundColor(.secondary)
                    .frame(height: 200)
                    .frame(maxWidth: .infinity)
                    .background(Color(.secondarySystemBackground))
                    .cornerRadius(8)
            } else {
                PitchGraphView(
                    contours: contours,
                    title: "Cleanup Comparison",
                    height: 250
                )
            }
        }
    }

    private var statisticsSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Comparison Statistics")
                .font(.subheadline)
                .fontWeight(.semibold)

            let rawStats = viewModel.contourStats(viewModel.rawContour)
            let scoringStats = viewModel.contourStats(viewModel.scoringContour)
            let displayStats = viewModel.contourStats(viewModel.displayContour)

            LazyVGrid(columns: [
                GridItem(.flexible()),
                GridItem(.flexible()),
                GridItem(.flexible()),
                GridItem(.flexible())
            ], spacing: 8) {
                Text("Preset").font(.caption2).fontWeight(.semibold)
                Text("Voiced").font(.caption2).fontWeight(.semibold)
                Text("Octave Err").font(.caption2).fontWeight(.semibold)
                Text("Blips").font(.caption2).fontWeight(.semibold)

                Text("RAW").font(.caption2)
                Text("\(rawStats.voicedCount)").font(.caption2)
                Text("\(rawStats.octaveErrors)").font(.caption2)
                Text("\(rawStats.blips)").font(.caption2)

                Text("SCORING").font(.caption2)
                Text("\(scoringStats.voicedCount)").font(.caption2)
                Text("\(scoringStats.octaveErrors)").font(.caption2).foregroundColor(scoringStats.octaveErrors < rawStats.octaveErrors ? .green : .primary)
                Text("\(scoringStats.blips)").font(.caption2).foregroundColor(scoringStats.blips < rawStats.blips ? .green : .primary)

                Text("DISPLAY").font(.caption2)
                Text("\(displayStats.voicedCount)").font(.caption2)
                Text("\(displayStats.octaveErrors)").font(.caption2).foregroundColor(displayStats.octaveErrors < rawStats.octaveErrors ? .green : .primary)
                Text("\(displayStats.blips)").font(.caption2).foregroundColor(displayStats.blips < rawStats.blips ? .green : .primary)
            }
        }
        .padding(8)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(8)
    }

    private var apiInfoSection: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("APIs Demonstrated:")
                .font(.caption)
                .fontWeight(.medium)

            Text("• CalibraPitch.PostProcess.cleanup(contour, options: .scoring)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• ContourCleanup.raw, .scoring, .display")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• CalibraPitch.PostProcess.fixOctaveErrors(contour)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• CalibraPitch.PostProcess.removeBlips(contour, minDurationMs:)")
                .font(.caption2)
                .foregroundColor(.secondary)
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }
}
