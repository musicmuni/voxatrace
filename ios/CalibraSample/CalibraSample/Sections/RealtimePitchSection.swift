import SwiftUI
import calibra
import sonix

/// Real-time pitch detection section using Calibra public API.
///
/// Demonstrates:
/// - Direct `CalibraPitch` API with `@State` primitives (iOS 15+ pattern)
/// - `SonixRecorder` for audio capture
/// - `MainActor.run` for thread-safe UI updates
struct RealtimePitchSection: View {
    // Direct Calibra API + @State primitives (recommended iOS 15+ pattern)
    @State private var detector: CalibraPitch?
    @State private var currentPitchHz: Float = -1.0
    @State private var noteLabel: String = "-"
    @State private var amplitude: Float = 0.0
    @State private var isRecording = false

    // Sonix recorder for audio input
    @State private var recorder: SonixRecorder?

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Real-time Pitch")
                .font(.headline)

            Text("Pitch: \(String(format: "%.2f", currentPitchHz)) Hz (\(noteLabel))")
                .font(.body)

            HStack {
                Text("RMS:")
                    .font(.caption)
                ProgressView(value: Double(amplitude), total: 1.0)
            }

            Button(isRecording ? "Stop" : "Start") {
                if isRecording {
                    stopRecording()
                } else {
                    startRecording()
                }
            }
            .buttonStyle(.borderedProminent)
        }
        .onDisappear {
            stopRecording()
            cleanup()
        }
    }

    private func setupAudioIfNeeded() {
        guard detector == nil else { return }

        // Create pitch detector with processing (smoothing + octave correction)
        detector = CalibraPitch.create(enableProcessing: true)

        // Create recorder using Sonix
        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("pitch_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, format: "m4a", quality: "voice")
    }

    private func cleanup() {
        recorder?.stop()
        recorder?.release()
        recorder = nil

        detector?.release()
        detector = nil
    }

    private func startRecording() {
        setupAudioIfNeeded()
        guard let recorder = recorder, let detector = detector else { return }

        // Reset detector state before starting new session
        detector.reset()

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

                // Detect pitch using direct API
                let result = detector.detectWithConfidence(samples: samples16k)
                let amp = detector.getAmplitude(samples: samples16k)

                // Update @State primitives on main thread
                await MainActor.run {
                    self.currentPitchHz = result.pitchHz
                    self.amplitude = amp
                    self.noteLabel = MusicUtils.getMidiNoteName(result.pitchHz)
                }
            }
        }

        recorder.start()
    }

    private func stopRecording() {
        recorder?.stop()
        isRecording = false

        // Reset state for next session
        detector?.reset()
        currentPitchHz = -1.0
        noteLabel = "-"
        amplitude = 0.0
    }
}
