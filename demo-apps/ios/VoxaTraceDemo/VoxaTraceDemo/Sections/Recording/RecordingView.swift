import SwiftUI
import VoxaTrace

/// Recording view demonstrating SonixRecorder with Config + Factory pattern.
struct RecordingView: View {
    @StateObject private var viewModel = RecordingViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Recording")
                .font(.headline)

            Text("Status: \(viewModel.status)")
                .font(.caption)
                .foregroundColor(.secondary)

            // Format selector
            formatSelector

            Text("Duration: \(viewModel.formattedDuration)")
                .font(.body)

            // Audio level indicator
            audioLevelIndicator

            // Buffer pool status (for Calibra integration)
            if viewModel.isRecording {
                Text("Buffer Pool: \(viewModel.bufferPoolAvailable)/4 available" +
                     (viewModel.bufferPoolWasExhausted ? " (overflow occurred)" : " (zero-alloc)"))
                    .font(.caption)
                    .foregroundColor(viewModel.bufferPoolWasExhausted ? .red : .blue)
            }

            // Control buttons
            recordingControls

            // Playback section
            if let _ = viewModel.savedFilePath, !viewModel.isRecording {
                savedFileSection
            }
        }
        .onDisappear {
            viewModel.onDisappear()
        }
    }

    private var formatSelector: some View {
        HStack {
            Text("Format:")
                .font(.caption)
            ForEach(viewModel.formats, id: \.self) { format in
                formatButton(format: format)
            }
        }
    }

    @ViewBuilder
    private func formatButton(format: String) -> some View {
        let isSelected = viewModel.selectedFormat == format
        if isSelected {
            Button(format.uppercased()) {
                viewModel.selectedFormat = format
            }
            .buttonStyle(.borderedProminent)
            .controlSize(.small)
            .disabled(viewModel.isRecording)
        } else {
            Button(format.uppercased()) {
                viewModel.selectedFormat = format
            }
            .buttonStyle(.bordered)
            .controlSize(.small)
            .disabled(viewModel.isRecording)
        }
    }

    private var audioLevelIndicator: some View {
        HStack {
            Text("Level:")
                .font(.caption)
            ProgressView(value: Double(viewModel.audioLevel), total: 1.0)
        }
    }

    private var recordingControls: some View {
        HStack(spacing: 12) {
            Button("Record") {
                viewModel.startRecording()
            }
            .disabled(viewModel.isRecording)
            .buttonStyle(.borderedProminent)

            Button("Stop & Save") {
                viewModel.stopRecording()
            }
            .disabled(!viewModel.isRecording)
            .buttonStyle(.bordered)
        }
    }

    private var savedFileSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            if let path = viewModel.savedFilePath {
                let url = URL(fileURLWithPath: path)
                if let attrs = try? FileManager.default.attributesOfItem(atPath: path),
                   let fileSize = attrs[.size] as? Int64 {
                    Text("Saved: \(url.lastPathComponent) (\(fileSize / 1024) KB)")
                        .font(.caption)
                        .foregroundColor(.secondary)
                }
            }

            // Playback time display
            if viewModel.playbackDurationMs > 0 {
                Text("Playback: \(viewModel.formattedPlaybackTime) / \(viewModel.formattedPlaybackDuration)")
                    .font(.caption)
                    .foregroundColor(.secondary)
            }

            // Playback controls
            HStack(spacing: 12) {
                Button("Play") {
                    viewModel.playRecording()
                }
                .disabled(!viewModel.canPlay)
                .buttonStyle(.bordered)
                .controlSize(.small)

                Button("Pause") {
                    viewModel.pausePlayback()
                }
                .disabled(!viewModel.isPlaying)
                .buttonStyle(.bordered)
                .controlSize(.small)

                Button("Stop") {
                    viewModel.stopPlayback()
                }
                .disabled(viewModel.savedFilePath == nil)
                .buttonStyle(.bordered)
                .controlSize(.small)
            }
        }
    }
}

/// Backward compatibility alias.
typealias RecordingSection = RecordingView

#Preview {
    RecordingView()
        .padding()
}
