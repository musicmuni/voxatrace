import SwiftUI
import Charts
import VoxaTrace

/// Real-time pitch detection view with tiered builder API.
///
/// Showcases:
/// - PitchPreset (RESPONSIVE, BALANCED, PRECISE)
/// - VoiceType (Western, Carnatic, Hindustani, Pop, IndianFilm)
/// - QuietHandling (SENSITIVE, NORMAL, NOISY)
/// - DetectionStrictness (STRICT, BALANCED, LENIENT)
/// - DetectorBuilder API with YIN and SwiftF0 algorithms
struct RealtimePitchView: View {
    @StateObject private var viewModel = RealtimePitchViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            configurationSection
            Divider()
            liveDisplaySection

            if viewModel.showGraph && !viewModel.recordedPitches.isEmpty {
                sessionGraphSection
            }

            Divider()
            apiInfoSection
        }
        .onDisappear {
            viewModel.onDisappear()
        }
    }

    private var configurationSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Configuration")
                .font(.subheadline)
                .fontWeight(.semibold)

            VStack(alignment: .leading, spacing: 4) {
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
                Text(viewModel.algorithms[viewModel.selectedAlgorithm].description)
                    .font(.caption2)
                    .foregroundColor(.secondary)
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
                        Text(viewModel.voiceTypes[index].name)
                            .lineLimit(1)
                            .fixedSize(horizontal: true, vertical: false)
                            .tag(index)
                    }
                }
                .pickerStyle(.menu)
                .fixedSize(horizontal: true, vertical: false)
            }

            HStack {
                Text("Environment:")
                    .font(.caption)
                Picker("Environment", selection: $viewModel.selectedQuietHandling) {
                    ForEach(0..<viewModel.quietHandlings.count, id: \.self) { index in
                        Text(viewModel.quietHandlings[index].name).tag(index)
                    }
                }
                .pickerStyle(.segmented)
            }

            HStack {
                Text("Strictness:")
                    .font(.caption)
                Picker("Strictness", selection: $viewModel.selectedStrictness) {
                    ForEach(0..<viewModel.strictnesses.count, id: \.self) { index in
                        Text(viewModel.strictnesses[index].name).tag(index)
                    }
                }
                .pickerStyle(.segmented)
            }
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }

    private var liveDisplaySection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Real-time Detection")
                .font(.subheadline)
                .fontWeight(.semibold)

            HStack {
                VStack(alignment: .leading) {
                    Text("Pitch")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    if let pitch = viewModel.currentPitch, pitch.isSinging {
                        Text(String(format: "%.1f Hz", pitch.pitch))
                            .font(.title2)
                            .fontWeight(.bold)
                    } else {
                        Text("-")
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundColor(.secondary)
                    }
                }

                Spacer()

                VStack(alignment: .center) {
                    Text("Note")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    if let pitch = viewModel.currentPitch, let note = pitch.note {
                        Text(note)
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundColor(.blue)
                    } else {
                        Text("-")
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundColor(.secondary)
                    }
                }

                Spacer()

                VStack(alignment: .center) {
                    Text("Tuning")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    if let pitch = viewModel.currentPitch, pitch.isSinging {
                        let tuningColor = tuningColorFor(pitch.tuning)
                        Text("\(pitch.centsOff > 0 ? "+" : "")\(pitch.centsOff)¢")
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundColor(tuningColor)
                    } else {
                        Text("-")
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundColor(.secondary)
                    }
                }

                Spacer()

                VStack(alignment: .trailing) {
                    Text("Samples")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    Text("\(viewModel.recordedPitches.count)")
                        .font(.title2)
                }
            }

            HStack {
                Text("RMS:")
                    .font(.caption)
                ProgressView(value: Double(viewModel.amplitude), total: 1.0)
            }

            if viewModel.isRecording {
                ScrollingPitchContourView(
                    pitchHistory: viewModel.pitchHistory,
                    maxPoints: viewModel.maxHistoryPoints,
                    height: 120
                )
            }

            HStack {
                Button(viewModel.isRecording ? "Stop" : "Start") {
                    viewModel.toggleRecording()
                }
                .buttonStyle(.borderedProminent)

                if viewModel.showGraph && !viewModel.recordedPitches.isEmpty {
                    Button("Clear") {
                        viewModel.clearRecording()
                    }
                    .buttonStyle(.bordered)
                }
            }
        }
    }

    private func tuningColorFor(_ tuning: PitchPoint.Tuning) -> Color {
        switch tuning {
        case .inTune: return .green
        case .flat, .sharp: return .orange
        case .silent: return .gray
        default: return .gray
        }
    }

    private var sessionGraphSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Session Pitch Contour")
                .font(.subheadline)
                .fontWeight(.semibold)

            let voicedCount = viewModel.recordedPitches.filter { $0 > 0 }.count
            Text("\(voicedCount) voiced frames out of \(viewModel.recordedPitches.count) total")
                .font(.caption)
                .foregroundColor(.secondary)

            PitchGraphView(
                pitchesHz: viewModel.recordedPitches,
                title: "Recorded Session"
            )
        }
    }

    private var apiInfoSection: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("APIs Demonstrated:")
                .font(.caption)
                .fontWeight(.medium)

            Text("• CalibraPitch.createDetector(config: .balanced)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• detector.detect(samples:sampleRate:) → PitchPoint")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• PitchPoint: pitch, isSinging, note, midiNote, centsOff, tuning, reliability")
                .font(.caption2)
                .foregroundColor(.secondary)
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }
}
