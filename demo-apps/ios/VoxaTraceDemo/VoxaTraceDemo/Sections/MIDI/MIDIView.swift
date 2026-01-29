import SwiftUI
import VoxaTrace

/// MIDI synthesis view demonstrating SonixMidiSynthesizer.Builder pattern.
struct MIDIView: View {
    @StateObject private var viewModel = MIDIViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("MIDI Synthesis")
                .font(.headline)

            Text("Status: \(viewModel.status)")
                .font(.caption)
                .foregroundColor(.secondary)

            // Show FluidSynth version if available
            if let version = viewModel.synthesizerVersion {
                Text("FluidSynth: \(version)")
                    .font(.caption)
                    .foregroundColor(.secondary)
            }

            // C major scale notes
            Text("Notes: C4 - D4 - E4 - F4 - G4 - A4 - B4 - C5")
                .font(.caption)
                .foregroundColor(.secondary)

            // Sample rate selection
            sampleRateSelector

            // Synthesize button
            Button(viewModel.isSynthesizing ? "Synthesizing..." : "Synthesize") {
                viewModel.synthesize()
            }
            .disabled(viewModel.isSynthesizing)
            .buttonStyle(.borderedProminent)

            // Playback controls
            playbackControls
        }
        .onDisappear {
            viewModel.onDisappear()
        }
    }

    private var sampleRateSelector: some View {
        HStack {
            Text("Sample Rate:")
                .font(.caption)
            ForEach(viewModel.sampleRates, id: \.self) { rate in
                sampleRateButton(rate: rate)
            }
        }
    }

    @ViewBuilder
    private func sampleRateButton(rate: Int32) -> some View {
        let label = "\(rate / 1000)kHz"
        let isSelected = viewModel.selectedSampleRate == rate
        if isSelected {
            Button(label) {
                viewModel.selectedSampleRate = rate
            }
            .buttonStyle(.borderedProminent)
            .controlSize(.small)
            .disabled(viewModel.isSynthesizing)
        } else {
            Button(label) {
                viewModel.selectedSampleRate = rate
            }
            .buttonStyle(.bordered)
            .controlSize(.small)
            .disabled(viewModel.isSynthesizing)
        }
    }

    private var playbackControls: some View {
        HStack(spacing: 12) {
            Button("Play") {
                viewModel.play()
            }
            .disabled(!viewModel.canPlay)
            .buttonStyle(.bordered)

            Button("Stop") {
                viewModel.stop()
            }
            .disabled(!viewModel.isPlaying)
            .buttonStyle(.bordered)
        }
    }
}

/// Backward compatibility alias.
typealias MidiSection = MIDIView

#Preview {
    MIDIView()
        .padding()
}
