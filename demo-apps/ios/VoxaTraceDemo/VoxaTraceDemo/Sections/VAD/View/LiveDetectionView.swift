import SwiftUI
import VoxaTrace

/// Real-time voice activity detection view with all 4 backends.
///
/// Showcases:
/// - SPEECH (Silero neural network)
/// - GENERAL (RMS-based, no model)
/// - SINGING_REALTIME (SwiftF0 pitch-based)
/// - SINGING (Essentia YAMNet classifier)
struct LiveDetectionView: View {
    @StateObject private var viewModel = LiveVADViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            // Backend picker
            BackendPicker(
                selectedIndex: $viewModel.selectedBackendIndex,
                backends: viewModel.backends
            )

            // Backend info card
            BackendInfoCard(info: viewModel.backends[viewModel.selectedBackendIndex])

            Divider()

            // Waveform visualization
            VADWaveformView(samples: viewModel.waveformSamples)

            // VAD display
            VADDisplayCard(vadRatio: viewModel.vadRatio, activityLevel: viewModel.activityLevel)

            // Voice indicator
            if viewModel.isRecording {
                VoiceIndicator(isVoiceDetected: viewModel.vadRatio > viewModel.threshold)
            }

            // Statistics
            VADStatsRow(
                voiceTime: viewModel.voiceTimeSeconds,
                silenceTime: viewModel.silenceTimeSeconds,
                latencyMs: viewModel.lastProcessingLatencyMs
            )

            // Sensitivity slider
            SensitivitySlider(threshold: $viewModel.threshold)

            Spacer()

            // Control button
            Button(action: { viewModel.toggleRecording() }) {
                HStack {
                    Image(systemName: viewModel.isRecording ? "stop.fill" : "mic.fill")
                    Text(viewModel.isRecording ? "Stop" : "Start Detection")
                }
                .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)
            .tint(viewModel.isRecording ? .red : .blue)

            // API usage info
            apiInfoSection
        }
        .onDisappear {
            viewModel.onDisappear()
        }
    }

    private var apiInfoSection: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("API Usage:")
                .font(.caption)
                .fontWeight(.medium)

            Text(viewModel.apiCodeExample)
                .font(.caption2)
                .foregroundColor(.secondary)
                .padding(8)
                .background(Color(.tertiarySystemBackground))
                .cornerRadius(6)
        }
    }
}
