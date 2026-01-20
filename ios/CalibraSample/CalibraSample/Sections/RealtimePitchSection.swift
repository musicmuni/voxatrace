import SwiftUI
import vozos

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

    // Offline analysis state
    @State private var offlineRawCount = 0
    @State private var offlineProcessedCount = 0
    @State private var offlineOctaveErrorsFixed = 0
    @State private var offlineVoicedCount = 0
    @State private var isAnalyzingOffline = false

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

            Divider()
                .padding(.vertical, 8)

            // Offline Analysis Section
            offlineAnalysisSection
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

    // MARK: - Offline Analysis

    private var offlineAnalysisSection: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Offline Pitch Processing")
                .font(.headline)

            Text("Process pitch contour from audio file")
                .font(.caption)
                .foregroundColor(.secondary)

            Button("Process Alankaar Voice") {
                analyzeOffline()
            }
            .buttonStyle(.bordered)
            .disabled(isAnalyzingOffline)

            if isAnalyzingOffline {
                ProgressView()
                    .frame(maxWidth: .infinity)
            }

            if offlineRawCount > 0 {
                VStack(alignment: .leading, spacing: 8) {
                    Text("Processing Result")
                        .font(.subheadline)
                        .fontWeight(.semibold)

                    HStack {
                        VStack(alignment: .leading) {
                            Text("Raw Pitches")
                                .font(.caption)
                                .foregroundColor(.secondary)
                            Text("\(offlineRawCount)")
                                .font(.title2)
                                .fontWeight(.bold)
                        }

                        Spacer()

                        VStack(alignment: .center) {
                            Text("Voiced")
                                .font(.caption)
                                .foregroundColor(.secondary)
                            Text("\(offlineVoicedCount)")
                                .font(.title2)
                        }

                        Spacer()

                        VStack(alignment: .center) {
                            Text("Processed")
                                .font(.caption)
                                .foregroundColor(.secondary)
                            Text("\(offlineProcessedCount)")
                                .font(.title2)
                        }

                        Spacer()

                        VStack(alignment: .trailing) {
                            Text("Octave Fixed")
                                .font(.caption)
                                .foregroundColor(.secondary)
                            Text("\(offlineOctaveErrorsFixed)")
                                .font(.title2)
                                .foregroundColor(.orange)
                        }
                    }
                }
                .padding(12)
                .background(Color(.secondarySystemBackground))
                .cornerRadius(8)
            }

            // API Info
            VStack(alignment: .leading, spacing: 4) {
                Text("APIs Demonstrated:")
                    .font(.caption)
                    .fontWeight(.medium)

                Text("• SonixDecoder.decode() - Load audio from file")
                    .font(.caption2)
                    .foregroundColor(.secondary)

                Text("• PitchContour.fromAudio() - Extract pitch contour")
                    .font(.caption2)
                    .foregroundColor(.secondary)

                Text("• CalibraPitchOffline.process() - Smooth + correct octave errors")
                    .font(.caption2)
                    .foregroundColor(.secondary)

                Text("• CalibraPitchOffline.smooth() - Apply smoothing filter")
                    .font(.caption2)
                    .foregroundColor(.secondary)

                Text("• CalibraPitchOffline.correctOctaveErrors() - Fix octave jumps")
                    .font(.caption2)
                    .foregroundColor(.secondary)
            }
            .padding(8)
            .background(Color(.tertiarySystemBackground))
            .cornerRadius(6)
        }
    }

    private func analyzeOffline() {
        isAnalyzingOffline = true
        offlineRawCount = 0
        offlineProcessedCount = 0
        offlineOctaveErrorsFixed = 0
        offlineVoicedCount = 0

        Task {
            // Load audio from bundle
            guard let audioURL = Bundle.main.url(forResource: "Alankaar 01_voice", withExtension: "m4a"),
                  let audioData = SonixDecoder.decode(path: audioURL.path) else {
                await MainActor.run {
                    isAnalyzingOffline = false
                }
                return
            }

            // Resample to 16kHz for Calibra APIs
            let samples16k = SonixResampler.resample(
                samples: audioData.floatSamples,
                fromRate: Int(audioData.sampleRate),
                toRate: 16000
            )

            // Extract raw pitch contour using PitchContour.fromAudio()
            // This handles chunking internally - no manual array manipulation needed
            // Use enableProcessing: false to get raw pitches for octave error demonstration
            let contour = PitchContour.fromAudio(samples: samples16k)
            let rawPitches = contour.pitchesHz

            // Count octave errors before processing
            var octaveErrorsBefore = 0
            for i in 1..<rawPitches.count {
                if rawPitches[i] > 0 && rawPitches[i-1] > 0 {
                    let ratio = rawPitches[i] / rawPitches[i-1]
                    if ratio > 1.8 || ratio < 0.55 {
                        octaveErrorsBefore += 1
                    }
                }
            }

            // Apply processing using CalibraPitchOffline
            let processed = CalibraPitchOffline.process(pitchesHz: rawPitches)

            // Count octave errors after processing
            var octaveErrorsAfter = 0
            for i in 1..<processed.count {
                if processed[i] > 0 && processed[i-1] > 0 {
                    let ratio = processed[i] / processed[i-1]
                    if ratio > 1.8 || ratio < 0.55 {
                        octaveErrorsAfter += 1
                    }
                }
            }

            let rawVoiced = rawPitches.filter { $0 > 0 }.count
            let processedVoiced = processed.filter { $0 > 0 }.count
            let octaveFixed = max(0, octaveErrorsBefore - octaveErrorsAfter)

            await MainActor.run {
                offlineRawCount = rawPitches.count
                offlineVoicedCount = rawVoiced
                offlineProcessedCount = processedVoiced
                offlineOctaveErrorsFixed = octaveFixed
                isAnalyzingOffline = false
            }
        }
    }
}
