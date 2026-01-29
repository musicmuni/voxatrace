import SwiftUI

/// Playback view demonstrating SonixPlayer with Config + Factory pattern.
struct PlaybackView: View {
    @StateObject private var viewModel = PlaybackViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Playback")
                .font(.headline)

            Text("Status: \(viewModel.status)")
                .font(.caption)
                .foregroundColor(.secondary)

            // Time display
            Text("\(viewModel.formattedCurrentTime) / \(viewModel.formattedDuration)")
                .font(.body)

            // Seek slider
            seekSlider

            // Playback controls
            playbackControls

            // Volume control
            volumeControl

            // Pitch control
            pitchControl

            // Loop count
            loopControl

            // Fade controls
            fadeControls
        }
        .onAppear {
            viewModel.onAppear()
        }
        .onDisappear {
            viewModel.onDisappear()
        }
    }

    private var seekSlider: some View {
        Slider(
            value: Binding(
                get: { viewModel.seekProgress },
                set: { viewModel.seek(to: $0) }
            )
        )
        .disabled(!viewModel.isLoaded)
    }

    private var playbackControls: some View {
        HStack(spacing: 12) {
            Button("Play") {
                viewModel.play()
            }
            .disabled(!viewModel.isLoaded || viewModel.isPlaying)
            .buttonStyle(.borderedProminent)

            Button("Pause") {
                viewModel.pause()
            }
            .disabled(!viewModel.isPlaying)
            .buttonStyle(.bordered)

            Button("Stop") {
                viewModel.stop()
            }
            .disabled(!viewModel.isLoaded)
            .buttonStyle(.bordered)
        }
    }

    private var volumeControl: some View {
        HStack {
            Text("Volume:")
                .frame(width: 60, alignment: .leading)
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

    private var pitchControl: some View {
        HStack {
            Text("Pitch:")
                .frame(width: 60, alignment: .leading)
            Slider(
                value: Binding(
                    get: { Double(viewModel.pitch + 12) / 24 },
                    set: { newValue in
                        let newPitch = Float(newValue * 24 - 12)
                        viewModel.setPitch(newPitch)
                    }
                )
            )
            Text("\(Int(viewModel.pitch)) st")
                .frame(width: 40)
        }
    }

    private var loopControl: some View {
        HStack {
            Text("Loop:")
                .frame(width: 60, alignment: .leading)
            loopButton(count: 1)
            loopButton(count: 2)
            loopButton(count: 3)
            loopButton(count: -1)
        }
    }

    @ViewBuilder
    private func loopButton(count: Int) -> some View {
        let label = count == -1 ? "inf" : "\(count)"
        let isSelected = count == Int(viewModel.loopCount)

        if isSelected {
            Button(label) {
                viewModel.setLoopCount(Int32(count))
            }
            .buttonStyle(.borderedProminent)
            .controlSize(.small)
        } else {
            Button(label) {
                viewModel.setLoopCount(Int32(count))
            }
            .buttonStyle(.bordered)
            .controlSize(.small)
        }
    }

    private var fadeControls: some View {
        HStack(spacing: 12) {
            Button("Fade In") {
                viewModel.fadeIn()
            }
            .disabled(!viewModel.isLoaded)
            .buttonStyle(.bordered)
            .controlSize(.small)

            Button("Fade Out") {
                viewModel.fadeOut()
            }
            .disabled(!viewModel.isLoaded)
            .buttonStyle(.bordered)
            .controlSize(.small)
        }
    }
}

/// Backward compatibility alias.
typealias PlaybackSection = PlaybackView

#Preview {
    PlaybackView()
        .padding()
}
