import SwiftUI
import Charts
import VoxaTrace

/// Batch pitch extraction view with ContourCleanup presets.
struct PitchExtractionView: View {
    @StateObject private var viewModel = PitchExtractionViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            configurationSection

            Divider()

            HStack {
                Button("Extract from Alankaar Voice") {
                    viewModel.extractPitch()
                }
                .buttonStyle(.borderedProminent)
                .disabled(viewModel.isExtracting)

                if viewModel.isExtracting {
                    ProgressView()
                        .padding(.leading, 8)
                }
            }

            if !viewModel.pitchesHz.isEmpty {
                statisticsSection
                graphSection
            }

            Divider()

            apiInfoSection
        }
    }

    private var configurationSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Configuration")
                .font(.subheadline)
                .fontWeight(.semibold)

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

            HStack {
                Text("Preset:")
                    .font(.caption)
                Picker("Preset", selection: $viewModel.selectedPreset) {
                    ForEach(0..<viewModel.presets.count, id: \.self) { index in
                        Text(viewModel.presets[index].name).tag(index)
                    }
                }
                .pickerStyle(.segmented)
            }

            HStack {
                Text("Voice Type:")
                    .font(.caption)
                Picker("Voice Type", selection: $viewModel.selectedVoiceType) {
                    ForEach(0..<viewModel.voiceTypes.count, id: \.self) { index in
                        Text(viewModel.voiceTypes[index].name).tag(index)
                    }
                }
                .pickerStyle(.menu)
            }

            VStack(alignment: .leading, spacing: 4) {
                HStack {
                    Text("Cleanup:")
                        .font(.caption)
                    Picker("Cleanup", selection: $viewModel.selectedCleanup) {
                        ForEach(0..<viewModel.cleanupPresets.count, id: \.self) { index in
                            Text(viewModel.cleanupPresets[index].name).tag(index)
                        }
                    }
                    .pickerStyle(.segmented)
                }
                Text(viewModel.cleanupPresets[viewModel.selectedCleanup].description)
                    .font(.caption2)
                    .foregroundColor(.secondary)
            }

            HStack {
                Text("Hop Size:")
                    .font(.caption)
                Stepper("\(viewModel.hopMs) ms", value: $viewModel.hopMs, in: 5...50, step: 5)
                    .font(.caption)
            }
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }

    private var statisticsSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Statistics")
                .font(.subheadline)
                .fontWeight(.semibold)

            LazyVGrid(columns: [
                GridItem(.flexible()),
                GridItem(.flexible()),
                GridItem(.flexible())
            ], spacing: 12) {
                StatCard(title: "Duration", value: String(format: "%.2fs", viewModel.duration))
                StatCard(title: "Voiced", value: String(format: "%.0f%%", viewModel.voicedRatio * 100))
                StatCard(title: "Samples", value: "\(viewModel.pitchesHz.count)")
                StatCard(title: "Mean", value: "\(Int(viewModel.meanPitchHz)) Hz")
                StatCard(title: "Min", value: "\(Int(viewModel.minPitchHz)) Hz\n(\(MusicUtils.getMidiNoteName(viewModel.minPitchHz)))")
                StatCard(title: "Max", value: "\(Int(viewModel.maxPitchHz)) Hz\n(\(MusicUtils.getMidiNoteName(viewModel.maxPitchHz)))")
            }

            HStack {
                Text("Pitch Range:")
                    .font(.caption)
                Text(String(format: "%.1f semitones", viewModel.rangeSemitones))
                    .font(.caption)
                    .fontWeight(.medium)
            }
        }
        .padding(8)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(8)
    }

    private var graphSection: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("Pitch Contour")
                .font(.subheadline)
                .fontWeight(.semibold)

            PitchGraphView(
                pitchesHz: viewModel.pitchesHz,
                times: viewModel.times,
                title: "Alankaar Voice"
            )
        }
    }

    private var apiInfoSection: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("APIs Demonstrated:")
                .font(.caption)
                .fontWeight(.medium)

            Text("• CalibraPitch.createContourExtractor(config: .scoring)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• extractor.extract(audio:sampleRate:) → PitchContour")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• ContourCleanup: .raw, .scoring, .display")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• PitchContour: duration, voicedRatio, pitchesHz, times")
                .font(.caption2)
                .foregroundColor(.secondary)
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }
}
