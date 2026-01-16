import SwiftUI
import vozos

/// Voice Activity Detection section using Calibra public API.
///
/// Demonstrates:
/// - Direct `CalibraVAD` API with `@State` primitives (iOS 15+ pattern)
/// - `CalibraVAD` with multiple presets (DEFAULT, LOW_LATENCY, HIGH_ACCURACY, SINGING, FAST_HEURISTIC)
/// - Real-time voice activity detection
/// - `SonixRecorder` for audio capture
/// - `MainActor.run` for thread-safe UI updates
struct VADSection: View {
    // Direct Calibra API + @State primitives (recommended iOS 15+ pattern)
    @State private var vad: CalibraVAD?
    @State private var vadRatio: Float = 0.0
    @State private var activityLevel: VoiceActivityLevel = .none
    @State private var isRecording = false
    @State private var selectedPresetIndex = 0

    // Sonix recorder
    @State private var recorder: SonixRecorder?

    private let presetNames = ["Default", "Low Latency", "High Accuracy", "Singing", "Fast Heuristic"]

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Voice Activity Detection")
                .font(.headline)

            Text("Detects speech/singing in audio using neural network VAD")
                .font(.caption)
                .foregroundColor(.secondary)

            // Preset picker
            VStack(alignment: .leading, spacing: 4) {
                Text("VAD Preset:")
                    .font(.caption)
                    .foregroundColor(.secondary)

                Picker("Preset", selection: $selectedPresetIndex) {
                    ForEach(0..<presetNames.count, id: \.self) { index in
                        Text(presetNames[index]).tag(index)
                    }
                }
                .pickerStyle(.segmented)
                .onChange(of: selectedPresetIndex) { _ in
                    recreateVAD()
                }
            }

            // VAD display card
            vadDisplayCard

            // Real-time indicator
            if isRecording {
                HStack {
                    Circle()
                        .fill(vadRatio > 0.5 ? Color.green : Color.gray)
                        .frame(width: 12, height: 12)
                    Text(vadRatio > 0.5 ? "Voice Detected" : "Silence")
                        .font(.subheadline)
                }
            }

            // Control button
            Button(isRecording ? "Stop" : "Start Detection") {
                if isRecording {
                    stopRecording()
                } else {
                    startRecording()
                }
            }
            .buttonStyle(.borderedProminent)
            .frame(maxWidth: .infinity)

            // Info about presets
            presetInfo
        }
        .onDisappear {
            stopRecording()
            cleanup()
        }
    }

    // Convenience computed properties for display
    private var activityColor: Color {
        switch activityLevel {
        case .none: return .gray
        case .partial: return .orange
        case .full: return .green
        @unknown default: return .gray
        }
    }

    private var activityLevelText: String {
        switch activityLevel {
        case .none: return "None"
        case .partial: return "Partial"
        case .full: return "Full"
        @unknown default: return "Unknown"
        }
    }

    private var vadDisplayCard: some View {
        VStack(spacing: 8) {
            // VAD ratio as percentage
            Text(String(format: "%.0f%%", vadRatio * 100))
                .font(.system(size: 48, weight: .bold))
                .foregroundColor(activityColor)

            // Activity level
            Text(activityLevelText)
                .font(.title3)
                .foregroundColor(.secondary)

            // Progress bar
            ProgressView(value: Double(vadRatio))
                .progressViewStyle(.linear)
                .tint(activityColor)
        }
        .frame(maxWidth: .infinity)
        .padding(20)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(12)
    }

    private var presetInfo: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("Preset Info:")
                .font(.caption)
                .fontWeight(.medium)

            Text(presetDescription)
                .font(.caption)
                .foregroundColor(.secondary)
        }
        .padding(12)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(8)
    }

    private var presetDescription: String {
        switch selectedPresetIndex {
        case 0:
            return "Balanced settings using Silero neural network. Good trade-off between accuracy and responsiveness."
        case 1:
            return "Smaller window, faster response. Best for real-time applications needing quick detection."
        case 2:
            return "Conservative thresholds, reduces false positives. Best when accuracy is critical."
        case 3:
            return "Tuned for musical content with longer sustained notes and shorter silence detection."
        case 4:
            return "RMS-based detection without neural network. Fastest option, basic accuracy."
        default:
            return "Unknown preset"
        }
    }

    private func getPreset(for index: Int) -> VADPreset {
        switch index {
        case 0: return .default
        case 1: return .lowLatency
        case 2: return .highAccuracy
        case 3: return .singing
        case 4: return .fastHeuristic
        default: return .default
        }
    }

    private func setupAudioIfNeeded() {
        guard vad == nil else { return }

        // Create VAD directly
        vad = CalibraVAD.create(preset: getPreset(for: selectedPresetIndex))

        // Create recorder using Sonix
        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("vad_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, format: "m4a", quality: "voice")
    }

    private func recreateVAD() {
        vad?.release()
        vad = CalibraVAD.create(preset: getPreset(for: selectedPresetIndex))
        // Reset display state
        vadRatio = 0.0
        activityLevel = .none
    }

    private func cleanup() {
        recorder?.stop()
        recorder?.release()
        recorder = nil

        vad?.release()
        vad = nil
    }

    private func startRecording() {
        setupAudioIfNeeded()
        guard let recorder = recorder, let vad = vad else { return }

        isRecording = true

        // Collect audio buffers from Sonix
        Task {
            let hwRate = Int(Sonix.hardwareSampleRate)

            for await buffer in recorder.audioBuffers {
                // Resample to 16kHz for Calibra (expects 16kHz input)
                let samples16k = SonixResampler.resample(
                    samples: buffer.floatSamples,
                    fromRate: hwRate,
                    toRate: 16000
                )

                // Get VAD ratio using direct API
                let ratio = vad.getVADRatio(samples: samples16k)

                // Skip if no chunk was processed (ratio == -1)
                if ratio < 0 {
                    continue
                }

                // Classify activity level
                let level: VoiceActivityLevel
                if ratio < CalibraVAD.noSingingThreshold {
                    level = .none
                } else if ratio < CalibraVAD.partialSingingThreshold {
                    level = .partial
                } else {
                    level = .full
                }

                // Update @State primitives on main thread
                await MainActor.run {
                    self.vadRatio = ratio
                    self.activityLevel = level
                }
            }
        }

        recorder.start()
    }

    private func stopRecording() {
        recorder?.stop()
        isRecording = false
        vad?.reset()
        vadRatio = 0.0
        activityLevel = .none
    }
}
