import SwiftUI
import VoxaTrace

// MARK: - Demo 4: PitchPoint Explorer

/// Showcases all PitchPoint computed properties in real-time.
struct PitchPointExplorerDemo: View {
    @State private var selectedAlgorithm: Int = 0 // YIN default
    @State private var detector: CalibraPitch.Detector?
    @State private var recorder: SonixRecorder?
    @State private var isRecording = false
    @State private var currentPitch: PitchPoint?
    @State private var amplitude: Float = 0.0
    @State private var modelLoaderConfigured = false

    private let algorithms: [(algorithm: PitchAlgorithm, name: String)] = [
        (.yin, "YIN"),
        (.swiftF0, "SwiftF0")
    ]

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("PitchPoint Properties")
                .font(.subheadline)
                .fontWeight(.semibold)

            Text("See all computed properties of PitchPoint in real-time.")
                .font(.caption)
                .foregroundColor(.secondary)

            // Algorithm picker
            HStack {
                Text("Algorithm:")
                    .font(.caption)
                Picker("Algorithm", selection: $selectedAlgorithm) {
                    ForEach(0..<algorithms.count, id: \.self) { index in
                        Text(algorithms[index].name).tag(index)
                    }
                }
                .pickerStyle(.segmented)
                .onChange(of: selectedAlgorithm) { _ in
                    if isRecording {
                        stopRecording()
                    }
                    detector?.release()
                    detector = nil
                }
            }

            // Main display
            pitchDisplaySection

            Divider()

            // Properties inspector
            propertiesInspector

            Divider()

            // Control
            HStack {
                Button(isRecording ? "Stop" : "Start") {
                    if isRecording {
                        stopRecording()
                    } else {
                        startRecording()
                    }
                }
                .buttonStyle(.borderedProminent)
            }

            Divider()

            apiInfoSection
        }
        .onDisappear {
            stopRecording()
            cleanup()
        }
    }

    private var pitchDisplaySection: some View {
        VStack(spacing: 16) {
            // Large note display
            VStack {
                if let pitch = currentPitch, pitch.isSinging, let note = pitch.note {
                    Text(note)
                        .font(.system(size: 60, weight: .bold, design: .rounded))
                        .foregroundColor(.blue)

                    Text(String(format: "%.1f Hz", pitch.pitch))
                        .font(.title3)
                        .foregroundColor(.secondary)
                } else {
                    Text("-")
                        .font(.system(size: 60, weight: .bold, design: .rounded))
                        .foregroundColor(.gray)

                    Text("Not singing")
                        .font(.title3)
                        .foregroundColor(.secondary)
                }
            }
            .frame(maxWidth: .infinity)
            .padding()
            .background(Color(.secondarySystemBackground))
            .cornerRadius(12)

            // Tuning indicator
            if let pitch = currentPitch, pitch.isSinging {
                TuningIndicatorView(centsOff: pitch.centsOff, tuning: pitch.tuning)
            }
        }
    }

    private var propertiesInspector: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Properties Inspector")
                .font(.subheadline)
                .fontWeight(.semibold)

            LazyVGrid(columns: [
                GridItem(.flexible()),
                GridItem(.flexible())
            ], spacing: 8) {
                PropertyRow(name: "pitch", value: currentPitch.map { String(format: "%.1f Hz", $0.pitch) } ?? "-")
                PropertyRow(name: "isSinging", value: currentPitch.map { $0.isSinging ? "true" : "false" } ?? "-")
                PropertyRow(name: "note", value: currentPitch?.note ?? "-")
                PropertyRow(name: "midiNote", value: currentPitch.map { $0.isSinging ? "\($0.midiNote)" : "-" } ?? "-")
                PropertyRow(name: "centsOff", value: currentPitch.map { $0.isSinging ? "\($0.centsOff)" : "-" } ?? "-")
                PropertyRow(name: "tuning", value: currentPitch.map { tuningString($0.tuning) } ?? "-")
                PropertyRow(name: "reliability", value: currentPitch.map { String(format: "%.2f", $0.reliability) } ?? "-")
                PropertyRow(name: "confidence", value: currentPitch.map { String(format: "%.2f", $0.confidence) } ?? "-")
            }
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }

    private func tuningString(_ tuning: PitchPoint.Tuning) -> String {
        switch tuning {
        case .silent: return "SILENT"
        case .flat: return "FLAT"
        case .inTune: return "IN_TUNE"
        case .sharp: return "SHARP"
        default: return "UNKNOWN"
        }
    }

    private var apiInfoSection: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("PitchPoint Properties:")
                .font(.caption)
                .fontWeight(.medium)

            Text("• pitch: Float - Detected pitch in Hz (-1 if not singing)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• isSinging: Bool - True if voiced audio detected")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• note: String? - Musical note name (e.g., \"A4\", \"C#5\")")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• midiNote: Int - MIDI note number (e.g., 69 for A4)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• centsOff: Int - Deviation from note (-50 to +50)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• tuning: Tuning - SILENT, FLAT, IN_TUNE, or SHARP")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• reliability: Float - Detection confidence (0.0-1.0)")
                .font(.caption2)
                .foregroundColor(.secondary)
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }

    // MARK: - Audio Logic

    private func setupAudioIfNeeded() {
        guard detector == nil else { return }

        // Configure audio session for recording
        AudioSessionManager.configure(.recording)

        let algorithm = algorithms[selectedAlgorithm].algorithm
        let detectorConfig = PitchDetectorConfig.Builder()
            .algorithm(algorithm)
            .build()

        // SwiftF0 requires model provider
        var modelProvider: (() -> KotlinByteArray)? = nil
        if algorithm == .swiftF0 {
            if !modelLoaderConfigured {
                ModelLoader.configure()
                modelLoaderConfigured = true
            }
            modelProvider = { ModelLoader.loadSwiftF0() }
        }

        detector = CalibraPitch.createDetector(config: detectorConfig, modelProvider: modelProvider)

        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("pitch_explorer_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, config: .voice)
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

        detector.reset()
        isRecording = true

        Task {
            let hwRate = AudioSessionManager.hardwareSampleRate

            for await buffer in recorder.audioBuffers {
                let samples16k = SonixResampler.resample(
                    samples: buffer.samples,
                    fromRate: hwRate,
                    toRate: 16000
                )

                let result = detector.detect(buffer: samples16k)
                let amp = detector.getAmplitude(buffer: samples16k)

                await MainActor.run {
                    self.currentPitch = result
                    self.amplitude = amp
                }
            }
        }

        recorder.start()
    }

    private func stopRecording() {
        recorder?.stop()
        isRecording = false

        detector?.reset()
        currentPitch = nil
        amplitude = 0.0
    }
}
