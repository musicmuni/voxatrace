import SwiftUI

/// Audio effects view demonstrating CalibraEffectsConfig.Builder pattern (ADR-001).
struct EffectsView: View {
    @StateObject private var viewModel = EffectsViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Audio Effects Playground")
                .font(.headline)

            Text(viewModel.status)
                .font(.caption)
                .foregroundColor(.secondary)

            // Preset selector
            presetSelector

            // Control buttons
            controlButtons

            // Reverb controls
            reverbControls

            // Compressor controls
            compressorControls

            // Noise gate controls
            noiseGateControls
        }
        .onDisappear {
            viewModel.onDisappear()
        }
    }

    private var presetSelector: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("Preset:")
                .font(.caption)
                .foregroundColor(.secondary)

            Picker("Preset", selection: $viewModel.selectedPresetIndex) {
                ForEach(0..<viewModel.presetNames.count, id: \.self) { index in
                    Text(viewModel.presetNames[index]).tag(index)
                }
            }
            .pickerStyle(.segmented)
            .onChange(of: viewModel.selectedPresetIndex) { _ in
                viewModel.applyPreset()
            }
        }
    }

    private var controlButtons: some View {
        HStack {
            Button(viewModel.isRecording ? "Stop" : "Record") {
                viewModel.toggleRecording()
            }
            .buttonStyle(.borderedProminent)
            .disabled(viewModel.isPlaying)

            Button(viewModel.isPlaying ? "Pause" : "Play") {
                viewModel.togglePlayback()
            }
            .buttonStyle(.bordered)
            .disabled(viewModel.isRecording)
        }
    }

    private var reverbControls: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("Reverb")
                .font(.subheadline)
                .fontWeight(.medium)

            Text("Mix: \(String(format: "%.2f", viewModel.reverbMix))")
                .font(.caption)
            Slider(
                value: Binding(
                    get: { Double(viewModel.reverbMix) },
                    set: { viewModel.updateReverbMix(Float($0)) }
                ),
                in: 0...1
            )

            Text("Room Size: \(String(format: "%.2f", viewModel.reverbRoomSize))")
                .font(.caption)
            Slider(
                value: Binding(
                    get: { Double(viewModel.reverbRoomSize) },
                    set: { viewModel.updateReverbRoomSize(Float($0)) }
                ),
                in: 0...1
            )
        }
    }

    private var compressorControls: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("Compressor")
                .font(.subheadline)
                .fontWeight(.medium)

            Text("Threshold: \(String(format: "%.1f", viewModel.compressorThreshold)) dB")
                .font(.caption)
            Slider(
                value: Binding(
                    get: { Double(viewModel.compressorThreshold) },
                    set: { viewModel.updateCompressorThreshold(Float($0)) }
                ),
                in: -60...0
            )

            Text("Ratio: \(String(format: "%.1f", viewModel.compressorRatio)):1")
                .font(.caption)
            Slider(
                value: Binding(
                    get: { Double(viewModel.compressorRatio) },
                    set: { viewModel.updateCompressorRatio(Float($0)) }
                ),
                in: 1...20
            )
        }
    }

    private var noiseGateControls: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("Noise Gate")
                .font(.subheadline)
                .fontWeight(.medium)

            Text("Threshold: \(String(format: "%.1f", viewModel.noiseGateThreshold)) dB")
                .font(.caption)
            Slider(
                value: Binding(
                    get: { Double(viewModel.noiseGateThreshold) },
                    set: { viewModel.updateNoiseGateThreshold(Float($0)) }
                ),
                in: -80...0
            )
        }
    }
}

/// Backward compatibility alias.
typealias EffectsSection = EffectsView

#Preview {
    EffectsView()
        .padding()
}
