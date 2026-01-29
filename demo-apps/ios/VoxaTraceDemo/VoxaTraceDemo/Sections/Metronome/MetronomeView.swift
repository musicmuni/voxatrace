import SwiftUI

/// Metronome view demonstrating SonixMetronome.Builder pattern.
struct MetronomeView: View {
    @StateObject private var viewModel = MetronomeViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack {
                Text("Metronome")
                    .font(.headline)
                Spacer()
                if !viewModel.isInitialized {
                    ProgressView()
                        .scaleEffect(0.8)
                }
            }

            Text("Status: \(viewModel.status)")
                .font(.caption)
                .foregroundColor(.secondary)

            // BPM slider
            bpmSlider

            // Beats per cycle
            beatsSelector

            // Volume
            volumeSlider

            // Beat indicators
            beatIndicators

            // Control buttons
            controls
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

    private var bpmSlider: some View {
        HStack {
            Text("BPM:")
                .frame(width: 40, alignment: .leading)
            Slider(
                value: Binding(
                    get: { Double(viewModel.bpm - 60) / 140 },
                    set: { viewModel.setBpm(Float(60 + $0 * 140)) }
                )
            )
            Text("\(Int(viewModel.bpm))")
                .frame(width: 40)
        }
    }

    private var beatsSelector: some View {
        HStack {
            Text("Beats:")
                .frame(width: 50, alignment: .leading)
            beatsButton(count: 3)
            beatsButton(count: 4)
            beatsButton(count: 6)
            beatsButton(count: 8)
        }
    }

    @ViewBuilder
    private func beatsButton(count: Int) -> some View {
        let isSelected = count == Int(viewModel.beatsPerCycle)
        if isSelected {
            Button("\(count)") {
                viewModel.setBeatsPerCycle(Int32(count))
            }
            .buttonStyle(.borderedProminent)
            .controlSize(.small)
            .disabled(viewModel.isRunning)
        } else {
            Button("\(count)") {
                viewModel.setBeatsPerCycle(Int32(count))
            }
            .buttonStyle(.bordered)
            .controlSize(.small)
            .disabled(viewModel.isRunning)
        }
    }

    private var volumeSlider: some View {
        HStack {
            Text("Vol:")
                .frame(width: 40, alignment: .leading)
            Slider(
                value: Binding(
                    get: { Double(viewModel.volume) },
                    set: { viewModel.setVolume(Float($0)) }
                )
            )
            Text("\(Int(viewModel.volume * 100))%")
                .frame(width: 40)
        }
    }

    private var beatIndicators: some View {
        HStack {
            Text("Beat:")
                .frame(width: 50, alignment: .leading)
            ForEach(0..<Int(viewModel.beatsPerCycle), id: \.self) { beat in
                let isCurrentBeat = beat == Int(viewModel.currentBeat) && viewModel.isRunning
                let isSama = beat == 0
                Circle()
                    .fill(beatColor(isCurrentBeat: isCurrentBeat, isSama: isSama))
                    .frame(width: 24, height: 24)
            }
        }
    }

    private func beatColor(isCurrentBeat: Bool, isSama: Bool) -> Color {
        if isCurrentBeat && isSama {
            return .red
        } else if isCurrentBeat {
            return .green
        } else if isSama {
            return .red.opacity(0.3)
        } else {
            return .gray.opacity(0.3)
        }
    }

    private var controls: some View {
        HStack(spacing: 12) {
            Button("Start") {
                viewModel.start()
            }
            .disabled(!viewModel.isInitialized || viewModel.isRunning)
            .buttonStyle(.borderedProminent)

            Button("Stop") {
                viewModel.stop()
            }
            .disabled(!viewModel.isRunning)
            .buttonStyle(.bordered)
        }
    }
}

/// Backward compatibility alias.
typealias MetronomeSection = MetronomeView

#Preview {
    MetronomeView()
        .padding()
}
