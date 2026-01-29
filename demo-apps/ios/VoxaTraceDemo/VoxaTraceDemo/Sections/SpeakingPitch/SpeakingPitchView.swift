import SwiftUI

/// Speaking pitch detection view demonstrating CalibraSpeakingPitch API.
struct SpeakingPitchView: View {
    @StateObject private var viewModel = SpeakingPitchViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Speaking Pitch & Gender Detector")
                .font(.headline)

            Text(viewModel.status)
                .font(.subheadline)
                .foregroundColor(.secondary)

            // Main display card
            mainDisplayCard

            // Level meter
            if viewModel.detectionState == .listening || viewModel.detectionState == .countdown {
                HStack {
                    Text("Level:")
                        .font(.caption)
                    ProgressView(value: Double(min(max(viewModel.currentLevel, 0), 1)))
                        .tint(viewModel.currentLevel > 0.02 ? .green : .blue)
                }
            }

            // Control buttons
            controlButtons

            // Info text
            Text("Speak naturally for a few seconds. Your natural speaking pitch will be detected.")
                .font(.caption)
                .foregroundColor(.secondary)

            Divider()
                .padding(.vertical, 8)

            // Offline Analysis Section
            offlineAnalysisSection
        }
        .onDisappear {
            viewModel.onDisappear()
        }
    }

    private var mainDisplayCard: some View {
        let backgroundColor: Color = {
            switch viewModel.detectionState {
            case .countdown:
                return .blue
            default:
                return Color(.secondarySystemBackground)
            }
        }()

        return VStack(spacing: 8) {
            switch viewModel.detectionState {
            case .listening:
                Text("Listening...")
                    .font(.title)

            case .countdown:
                Text("\(viewModel.countdownSeconds)")
                    .font(.system(size: 72, weight: .bold))
                    .foregroundColor(.white)
                Text("Keep speaking")
                    .foregroundColor(.white.opacity(0.8))

            case .processing:
                ProgressView()
                    .scaleEffect(1.5)
                Spacer().frame(height: 8)
                Text("Analyzing...")

            case .complete:
                if viewModel.detectedPitchHz > 0 {
                    Text(viewModel.detectedPitchNote)
                        .font(.system(size: 48, weight: .bold))
                        .foregroundColor(.blue)
                    Text(String(format: "%.1f Hz", viewModel.detectedPitchHz))
                        .font(.title3)
                        .foregroundColor(.secondary)

                    Spacer().frame(height: 16)

                    if let gender = viewModel.detectedGender {
                        HStack(spacing: 8) {
                            Text("Inferred Voice Type:")
                                .font(.subheadline)
                            Text(gender == .female ? "FEMALE" : "MALE")
                                .font(.subheadline)
                                .fontWeight(.medium)
                                .foregroundColor(.white)
                                .padding(.horizontal, 12)
                                .padding(.vertical, 4)
                                .background(gender == .female ? Color.pink : Color.blue)
                                .cornerRadius(4)
                        }
                    }
                } else {
                    Text("No pitch detected")
                        .font(.title2)
                }

            case .idle:
                Text("Ready")
                    .font(.title)
                    .foregroundColor(.secondary)
            }
        }
        .frame(maxWidth: .infinity)
        .padding(24)
        .background(backgroundColor)
        .cornerRadius(12)
    }

    private var controlButtons: some View {
        HStack {
            switch viewModel.detectionState {
            case .idle:
                Button("Start Detection") {
                    viewModel.startDetection()
                }
                .buttonStyle(.borderedProminent)
                .frame(maxWidth: .infinity)

            case .complete:
                Button("Try Again") {
                    viewModel.startDetection()
                }
                .buttonStyle(.borderedProminent)
                .frame(maxWidth: .infinity)

            default:
                Button("Cancel") {
                    viewModel.stopDetection()
                }
                .buttonStyle(.borderedProminent)
                .tint(.red)
                .frame(maxWidth: .infinity)
            }
        }
    }

    private var offlineAnalysisSection: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Offline Analysis")
                .font(.headline)

            Text("Analyze speaking pitch from audio file")
                .font(.caption)
                .foregroundColor(.secondary)

            Button("Analyze Alankaar Voice") {
                viewModel.analyzeOffline()
            }
            .buttonStyle(.bordered)
            .disabled(viewModel.isAnalyzingOffline)

            if viewModel.isAnalyzingOffline {
                ProgressView()
                    .frame(maxWidth: .infinity)
            }

            if viewModel.offlinePitchHz > 0 {
                offlineResultCard
            }

            apiInfoCard
        }
    }

    private var offlineResultCard: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Offline Result")
                .font(.subheadline)
                .fontWeight(.semibold)

            HStack {
                VStack(alignment: .leading) {
                    Text("Detected Note")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    Text(viewModel.offlinePitchNote)
                        .font(.title)
                        .fontWeight(.bold)
                }

                Spacer()

                VStack(alignment: .trailing) {
                    Text("Frequency")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    Text(String(format: "%.1f Hz", viewModel.offlinePitchHz))
                        .font(.title2)
                }

                Spacer()

                if let gender = viewModel.offlineGender {
                    VStack(alignment: .trailing) {
                        Text("Voice Type")
                            .font(.caption)
                            .foregroundColor(.secondary)
                        Text(gender == .female ? "FEMALE" : "MALE")
                            .font(.caption)
                            .fontWeight(.medium)
                            .foregroundColor(.white)
                            .padding(.horizontal, 8)
                            .padding(.vertical, 4)
                            .background(gender == .female ? Color.pink : Color.blue)
                            .cornerRadius(4)
                    }
                }
            }
        }
        .padding(12)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(8)
    }

    private var apiInfoCard: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("APIs Demonstrated:")
                .font(.caption)
                .fontWeight(.medium)

            Text("- SonixDecoder.decode() - Load audio from file")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("- SonixResampler.resample() - Resample to 16kHz")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("- CalibraSpeakingPitch.detectFromAudio() - Detect speaking pitch")
                .font(.caption2)
                .foregroundColor(.secondary)
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }
}

/// Backward compatibility alias.
typealias SpeakingPitchDetectorSection = SpeakingPitchView

#Preview {
    SpeakingPitchView()
        .padding()
}
