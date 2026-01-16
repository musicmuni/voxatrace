import SwiftUI
import vozos

private let RMS_THRESHOLD: Float = 0.02
private let COUNTDOWN_SECONDS = 4
private let FEMALE_THRESHOLD_HZ: Float = 174.61 // F3

enum SpeakingPitchDetectionState {
    case idle
    case listening
    case countdown
    case processing
    case complete
}

enum Gender {
    case male
    case female
}

/// Speaking Pitch & Gender Detector using Calibra public API.
///
/// Demonstrates:
/// - `CalibraSpeakingPitch` for natural speaking pitch detection from speech
/// - `CalibraPitch` for pitch detection and amplitude measurement
/// - `SonixRecorder` for audio capture
struct SpeakingPitchDetectorSection: View {
    @State private var detectionState: SpeakingPitchDetectionState = .idle
    @State private var countdownSeconds: Int = COUNTDOWN_SECONDS
    @State private var currentLevel: Float = 0.0
    @State private var detectedPitchHz: Float = 0.0
    @State private var detectedPitchNote = ""
    @State private var detectedGender: Gender?
    @State private var status = "Speak naturally to detect your speaking pitch"

    @State private var recorder: SonixRecorder?
    @State private var collectedSamples: [Float] = []

    // Calibra public API
    @State private var pitch: CalibraPitch?

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Speaking Pitch & Gender Detector")
                .font(.headline)

            Text(status)
                .font(.subheadline)
                .foregroundColor(.secondary)

            // Main display card
            mainDisplayCard

            // Level meter
            if detectionState == .listening || detectionState == .countdown {
                HStack {
                    Text("Level:")
                        .font(.caption)
                    ProgressView(value: Double(min(max(currentLevel, 0), 1)))
                        .tint(currentLevel > RMS_THRESHOLD ? .green : .blue)
                }
            }

            // Control buttons
            controlButtons

            // Info text
            Text("Speak naturally for a few seconds. Your natural speaking pitch will be detected.")
                .font(.caption)
                .foregroundColor(.secondary)
        }
        .onDisappear {
            stopDetection()
            cleanupAudio()
        }
    }

    private var mainDisplayCard: some View {
        let backgroundColor: Color = {
            switch detectionState {
            case .countdown:
                return .blue
            case .complete:
                return Color(.secondarySystemBackground)
            default:
                return Color(.secondarySystemBackground)
            }
        }()

        return VStack(spacing: 8) {
            switch detectionState {
            case .listening:
                Text("Listening...")
                    .font(.title)

            case .countdown:
                Text("\(countdownSeconds)")
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
                if detectedPitchHz > 0 {
                    Text(detectedPitchNote)
                        .font(.system(size: 48, weight: .bold))
                        .foregroundColor(.blue)
                    Text(String(format: "%.1f Hz", detectedPitchHz))
                        .font(.title3)
                        .foregroundColor(.secondary)

                    Spacer().frame(height: 16)

                    if let gender = detectedGender {
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
            switch detectionState {
            case .idle:
                Button("Start Detection") {
                    startDetection()
                }
                .buttonStyle(.borderedProminent)
                .frame(maxWidth: .infinity)

            case .complete:
                Button("Try Again") {
                    startDetection()
                }
                .buttonStyle(.borderedProminent)
                .frame(maxWidth: .infinity)

            default:
                Button("Cancel") {
                    stopDetection()
                }
                .buttonStyle(.borderedProminent)
                .tint(.red)
                .frame(maxWidth: .infinity)
            }
        }
    }

    private func setupAudioIfNeeded() {
        guard pitch == nil else { return }

        // Create pitch detector using Calibra public API
        pitch = CalibraPitch.create()

        // Create recorder using Sonix (no output file needed, just for buffer access)
        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("speaking_pitch_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, format: "m4a", quality: "voice")
    }

    private func cleanupAudio() {
        recorder?.stop()
        recorder?.release()
        recorder = nil

        pitch?.release()
        pitch = nil
    }

    private func startDetection() {
        setupAudioIfNeeded()
        guard let recorder = recorder else { return }

        // Reset state
        detectionState = .listening
        countdownSeconds = COUNTDOWN_SECONDS
        detectedPitchHz = 0
        detectedPitchNote = ""
        detectedGender = nil
        collectedSamples = []
        status = "Say something..."

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

                // Calculate amplitude using Calibra
                let calculatedRms = pitch?.getAmplitude(samples: samples16k) ?? 0.0

                await MainActor.run {
                    currentLevel = calculatedRms

                    switch detectionState {
                    case .listening:
                        if calculatedRms > RMS_THRESHOLD {
                            detectionState = .countdown
                            status = "Keep speaking..."
                            startCountdown()
                        }

                    case .countdown:
                        // Collect resampled samples (already 16kHz)
                        collectedSamples.append(contentsOf: samples16k)

                    default:
                        break
                    }
                }
            }
        }

        recorder.start()
    }

    private func startCountdown() {
        Task {
            for i in stride(from: COUNTDOWN_SECONDS, through: 1, by: -1) {
                await MainActor.run { countdownSeconds = i }
                try? await Task.sleep(nanoseconds: 1_000_000_000)

                // Check if still in countdown state
                if await MainActor.run(body: { detectionState != .countdown }) {
                    return
                }
            }

            await MainActor.run {
                detectionState = .processing
                status = "Processing..."
                recorder?.stop()
                processAudio()
            }
        }
    }

    private func processAudio() {
        guard !collectedSamples.isEmpty else {
            status = "No audio collected. Try again."
            detectionState = .complete
            return
        }

        // Use CalibraSpeakingPitch public API for speaking pitch detection
        // collectedSamples is already resampled to 16kHz
        let pitchHz = CalibraSpeakingPitch.detectFromAudio(audioMono: collectedSamples)

        if pitchHz > 0 {
            detectedPitchHz = pitchHz
            detectedPitchNote = MusicUtils.getMidiNoteName(pitchHz)

            // Determine gender based on frequency
            detectedGender = pitchHz >= FEMALE_THRESHOLD_HZ ? .female : .male

            status = "Detection complete!"
        } else {
            status = "Could not detect speaking pitch. Try again."
        }

        detectionState = .complete
    }

    private func stopDetection() {
        recorder?.stop()
        detectionState = .idle
        status = "Speak naturally to detect your speaking pitch"
    }
}
