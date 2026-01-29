import SwiftUI

/// Multi-track view demonstrating SonixMixer with Config + Factory pattern.
struct MultiTrackView: View {
    @StateObject private var viewModel = MultiTrackViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Multi-Track")
                .font(.headline)

            Text("Status: \(viewModel.status)")
                .font(.caption)
                .foregroundColor(.secondary)

            // Time display
            Text("\(viewModel.formattedCurrentTime) / \(viewModel.formattedDuration)")
                .font(.body)

            // Seek slider
            seekSlider

            // Backing volume
            backingVolumeSlider

            // Vocal volume
            vocalVolumeSlider

            // Playback controls
            playbackControls

            // Fade buttons
            fadeControls
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

    private var seekSlider: some View {
        Slider(
            value: Binding(
                get: { viewModel.seekProgress },
                set: { viewModel.seek(to: $0) }
            )
        )
        .disabled(!viewModel.isLoaded)
    }

    private var backingVolumeSlider: some View {
        HStack {
            Text("Backing:")
                .frame(width: 70, alignment: .leading)
            Slider(
                value: Binding(
                    get: { Double(viewModel.backingVolume) },
                    set: { viewModel.setBackingVolume(Float($0)) }
                )
            )
            Text("\(Int(viewModel.backingVolume * 100))%")
                .frame(width: 40)
        }
    }

    private var vocalVolumeSlider: some View {
        HStack {
            Text("Vocal:")
                .frame(width: 70, alignment: .leading)
            Slider(
                value: Binding(
                    get: { Double(viewModel.vocalVolume) },
                    set: { viewModel.setVocalVolume(Float($0)) }
                )
            )
            Text("\(Int(viewModel.vocalVolume * 100))%")
                .frame(width: 40)
        }
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

    private var fadeControls: some View {
        HStack(spacing: 12) {
            Button("Fade Vocal Down") {
                viewModel.fadeVocalDown()
            }
            .disabled(!viewModel.isLoaded)
            .buttonStyle(.bordered)
            .controlSize(.small)

            Button("Fade Vocal Up") {
                viewModel.fadeVocalUp()
            }
            .disabled(!viewModel.isLoaded)
            .buttonStyle(.bordered)
            .controlSize(.small)
        }
    }
}

/// Backward compatibility alias.
typealias MultiTrackSection = MultiTrackView

#Preview {
    MultiTrackView()
        .padding()
}
