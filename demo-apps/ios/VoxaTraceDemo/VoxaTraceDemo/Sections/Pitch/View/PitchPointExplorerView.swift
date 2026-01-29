import SwiftUI
import VoxaTrace

/// PitchPoint properties explorer view - shows all computed properties in real-time.
struct PitchPointExplorerView: View {
    @StateObject private var viewModel = PitchPointExplorerViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("PitchPoint Properties")
                .font(.subheadline)
                .fontWeight(.semibold)

            Text("See all computed properties of PitchPoint in real-time.")
                .font(.caption)
                .foregroundColor(.secondary)

            // Algorithm picker
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

            // Main display
            pitchDisplaySection

            Divider()

            // Properties inspector
            propertiesInspector

            Divider()

            // Control
            HStack {
                Button(viewModel.isRecording ? "Stop" : "Start") {
                    viewModel.toggleRecording()
                }
                .buttonStyle(.borderedProminent)
            }

            Divider()

            apiInfoSection
        }
        .onDisappear {
            viewModel.onDisappear()
        }
    }

    private var pitchDisplaySection: some View {
        VStack(spacing: 16) {
            // Large note display
            VStack {
                if let pitch = viewModel.currentPitch, pitch.isSinging, let note = pitch.note {
                    Text(note)
                        .font(.system(size: 60, weight: .bold, design: .rounded))
                        .foregroundColor(.blue)

                    Text(String(format: "%.1f Hz", pitch.pitch))
                        .font(.title3)
                        .foregroundColor(.secondary)
                } else {
                    Text("-")
                        .font(.system(size: 60, weight: .bold, design: .rounded))
                        .foregroundColor(.gray)

                    Text("Not singing")
                        .font(.title3)
                        .foregroundColor(.secondary)
                }
            }
            .frame(maxWidth: .infinity)
            .padding()
            .background(Color(.secondarySystemBackground))
            .cornerRadius(12)

            // Tuning indicator
            if let pitch = viewModel.currentPitch, pitch.isSinging {
                TuningIndicatorView(centsOff: pitch.centsOff, tuning: pitch.tuning)
            }
        }
    }

    private var propertiesInspector: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Properties Inspector")
                .font(.subheadline)
                .fontWeight(.semibold)

            LazyVGrid(columns: [
                GridItem(.flexible()),
                GridItem(.flexible())
            ], spacing: 8) {
                PropertyRow(name: "pitch", value: viewModel.currentPitch.map { String(format: "%.1f Hz", $0.pitch) } ?? "-")
                PropertyRow(name: "isSinging", value: viewModel.currentPitch.map { $0.isSinging ? "true" : "false" } ?? "-")
                PropertyRow(name: "note", value: viewModel.currentPitch?.note ?? "-")
                PropertyRow(name: "midiNote", value: viewModel.currentPitch.map { $0.isSinging ? "\($0.midiNote)" : "-" } ?? "-")
                PropertyRow(name: "centsOff", value: viewModel.currentPitch.map { $0.isSinging ? "\($0.centsOff)" : "-" } ?? "-")
                PropertyRow(name: "tuning", value: viewModel.currentPitch.map { viewModel.tuningString($0.tuning) } ?? "-")
                PropertyRow(name: "reliability", value: viewModel.currentPitch.map { String(format: "%.2f", $0.reliability) } ?? "-")
                PropertyRow(name: "confidence", value: viewModel.currentPitch.map { String(format: "%.2f", $0.confidence) } ?? "-")
            }
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }

    private var apiInfoSection: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("PitchPoint Properties:")
                .font(.caption)
                .fontWeight(.medium)

            Text("• pitch: Float - Detected pitch in Hz (-1 if not singing)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• isSinging: Bool - True if voiced audio detected")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• note: String? - Musical note name (e.g., \"A4\", \"C#5\")")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• midiNote: Int - MIDI note number (e.g., 69 for A4)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• centsOff: Int - Deviation from note (-50 to +50)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• tuning: Tuning - SILENT, FLAT, IN_TUNE, or SHARP")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• reliability: Float - Detection confidence (0.0-1.0)")
                .font(.caption2)
                .foregroundColor(.secondary)
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }
}
