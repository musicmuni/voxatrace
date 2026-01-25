import SwiftUI
import Charts
import VoxaTrace

// MARK: - Realtime Pitch Detection

/// Real-time pitch detection with tiered builder API.
///
/// Showcases:
/// - PitchPreset (RESPONSIVE, BALANCED, PRECISE)
/// - VoiceType (Western, Carnatic, Hindustani, Pop, IndianFilm)
/// - QuietHandling (SENSITIVE, NORMAL, NOISY)
/// - DetectionStrictness (STRICT, BALANCED, LENIENT)
/// - DetectorBuilder API with YIN and SwiftF0 algorithms
struct RealtimePitchDemo: View {
    // Configuration
    @State private var selectedAlgorithm: Int = 0 // YIN (default, no model needed)
    @State private var selectedPreset: Int = 1 // BALANCED
    @State private var selectedVoiceType: Int = 0 // Auto
    @State private var selectedQuietHandling: Int = 1 // NORMAL
    @State private var selectedStrictness: Int = 1 // BALANCED

    // Runtime state
    @State private var detector: CalibraPitch.Detector?
    @State private var recorder: SonixRecorder?
    @State private var isRecording = false
    @State private var currentPitch: PitchPoint?
    @State private var amplitude: Float = 0.0
    @State private var modelLoaderConfigured = false

    // Session history for graph
    @State private var recordedPitches: [Float] = []
    @State private var showGraph = false

    private let algorithms: [(algorithm: PitchAlgorithm, name: String, description: String)] = [
        (.yin, "YIN", "Traditional algorithm, no model needed"),
        (.swiftF0, "SwiftF0", "Neural network, higher accuracy")
    ]

    private let presets: [(preset: PitchPreset, name: String)] = [
        (.responsive, "Responsive"),
        (.balanced, "Balanced"),
        (.precise, "Precise")
    ]

    private let voiceTypes: [(type: VoiceType, name: String)] = [
        (.auto, "Auto"),
        (.westernSoprano, "Western Soprano"),
        (.westernAlto, "Western Alto"),
        (.westernTenor, "Western Tenor"),
        (.westernBass, "Western Bass"),
        (.carnaticMale, "Carnatic Male"),
        (.carnaticFemale, "Carnatic Female"),
        (.hindustaniMale, "Hindustani Male"),
        (.hindustaniFemale, "Hindustani Female"),
        (.popMale, "Pop Male"),
        (.popFemale, "Pop Female"),
        (.indianFilmMale, "Indian Film Male"),
        (.indianFilmFemale, "Indian Film Female")
    ]

    private let quietHandlings: [(handling: QuietHandling, name: String)] = [
        (.sensitive, "Sensitive"),
        (.normal, "Normal"),
        (.noisy, "Noisy")
    ]

    private let strictnesses: [(strictness: DetectionStrictness, name: String)] = [
        (.strict, "Strict"),
        (.balanced, "Balanced"),
        (.lenient, "Lenient")
    ]

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            // Configuration section
            configurationSection

            Divider()

            // Live display with PitchPoint properties
            liveDisplaySection

            // Session graph (shown after stopping)
            if showGraph && !recordedPitches.isEmpty {
                sessionGraphSection
            }

            Divider()

            // API info
            apiInfoSection
        }
        .onDisappear {
            stopRecording()
            cleanup()
        }
    }

    private var configurationSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Configuration")
                .font(.subheadline)
                .fontWeight(.semibold)

            VStack(alignment: .leading, spacing: 4) {
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
                        recreateDetectorIfRecording()
                    }
                }
                Text(algorithms[selectedAlgorithm].description)
                    .font(.caption2)
                    .foregroundColor(.secondary)
            }

            HStack {
                Text("Preset:")
                    .font(.caption)
                Picker("Preset", selection: $selectedPreset) {
                    ForEach(0..<presets.count, id: \.self) { index in
                        Text(presets[index].name).tag(index)
                    }
                }
                .pickerStyle(.segmented)
                .onChange(of: selectedPreset) { _ in
                    recreateDetectorIfRecording()
                }
            }

            HStack {
                Text("Voice Type:")
                    .font(.caption)
                Picker("Voice Type", selection: $selectedVoiceType) {
                    ForEach(0..<voiceTypes.count, id: \.self) { index in
                        Text(voiceTypes[index].name)
                            .lineLimit(1)
                            .fixedSize(horizontal: true, vertical: false)
                            .tag(index)
                    }
                }
                .pickerStyle(.menu)
                .fixedSize(horizontal: true, vertical: false)
                .onChange(of: selectedVoiceType) { _ in
                    recreateDetectorIfRecording()
                }
            }

            HStack {
                Text("Environment:")
                    .font(.caption)
                Picker("Environment", selection: $selectedQuietHandling) {
                    ForEach(0..<quietHandlings.count, id: \.self) { index in
                        Text(quietHandlings[index].name).tag(index)
                    }
                }
                .pickerStyle(.segmented)
                .onChange(of: selectedQuietHandling) { _ in
                    recreateDetectorIfRecording()
                }
            }

            HStack {
                Text("Strictness:")
                    .font(.caption)
                Picker("Strictness", selection: $selectedStrictness) {
                    ForEach(0..<strictnesses.count, id: \.self) { index in
                        Text(strictnesses[index].name).tag(index)
                    }
                }
                .pickerStyle(.segmented)
                .onChange(of: selectedStrictness) { _ in
                    recreateDetectorIfRecording()
                }
            }
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }

    private var liveDisplaySection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Real-time Detection")
                .font(.subheadline)
                .fontWeight(.semibold)

            HStack {
                VStack(alignment: .leading) {
                    Text("Pitch")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    if let pitch = currentPitch, pitch.isSinging {
                        Text(String(format: "%.1f Hz", pitch.pitch))
                            .font(.title2)
                            .fontWeight(.bold)
                    } else {
                        Text("-")
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundColor(.secondary)
                    }
                }

                Spacer()

                VStack(alignment: .center) {
                    Text("Note")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    if let pitch = currentPitch, let note = pitch.note {
                        Text(note)
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundColor(.blue)
                    } else {
                        Text("-")
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundColor(.secondary)
                    }
                }

                Spacer()

                VStack(alignment: .center) {
                    Text("Tuning")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    if let pitch = currentPitch, pitch.isSinging {
                        let tuningColor = tuningColorFor(pitch.tuning)
                        Text("\(pitch.centsOff > 0 ? "+" : "")\(pitch.centsOff)¢")
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundColor(tuningColor)
                    } else {
                        Text("-")
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundColor(.secondary)
                    }
                }

                Spacer()

                VStack(alignment: .trailing) {
                    Text("Samples")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    Text("\(recordedPitches.count)")
                        .font(.title2)
                }
            }

            HStack {
                Text("RMS:")
                    .font(.caption)
                ProgressView(value: Double(amplitude), total: 1.0)
            }

            HStack {
                Button(isRecording ? "Stop" : "Start") {
                    if isRecording {
                        stopRecording()
                        showGraph = true
                    } else {
                        startRecording()
                        showGraph = false
                    }
                }
                .buttonStyle(.borderedProminent)

                if showGraph && !recordedPitches.isEmpty {
                    Button("Clear") {
                        recordedPitches = []
                        showGraph = false
                    }
                    .buttonStyle(.bordered)
                }
            }
        }
    }

    private func tuningColorFor(_ tuning: PitchPoint.Tuning) -> Color {
        switch tuning {
        case .inTune: return .green
        case .flat, .sharp: return .orange
        case .silent: return .gray
        default: return .gray
        }
    }

    private var sessionGraphSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Session Pitch Contour")
                .font(.subheadline)
                .fontWeight(.semibold)

            let voicedCount = recordedPitches.filter { $0 > 0 }.count
            Text("\(voicedCount) voiced frames out of \(recordedPitches.count) total")
                .font(.caption)
                .foregroundColor(.secondary)

            PitchGraphView(
                pitchesHz: recordedPitches,
                title: "Recorded Session"
            )
        }
    }

    private var apiInfoSection: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("APIs Demonstrated:")
                .font(.caption)
                .fontWeight(.medium)

            Text("• CalibraPitch.DetectorBuilder().preset(.balanced).voiceType(.carnaticMale).build()")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• detector.detect(buffer:) → PitchPoint")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• PitchPoint: pitch, isSinging, note, midiNote, centsOff, tuning, reliability")
                .font(.caption2)
                .foregroundColor(.secondary)
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }

    // MARK: - Audio Logic

    private func recreateDetector() {
        detector?.release()

        let algorithm = algorithms[selectedAlgorithm].algorithm
        var builder = CalibraPitch.DetectorBuilder()
            .algorithm(algorithm)
            .preset(presets[selectedPreset].preset)
            .voiceType(voiceTypes[selectedVoiceType].type)
            .quietHandling(quietHandlings[selectedQuietHandling].handling)
            .strictness(strictnesses[selectedStrictness].strictness)

        // SwiftF0 requires model provider
        if algorithm == .swiftF0 {
            if !modelLoaderConfigured {
                ModelLoader.configure()
                modelLoaderConfigured = true
            }
            builder = builder.modelProvider { ModelLoader.loadSwiftF0() }
        }

        detector = builder.build()
    }

    private func recreateDetectorIfRecording() {
        if isRecording {
            stopRecording()
        }
        recreateDetector()
    }

    private func setupAudioIfNeeded() {
        if detector == nil {
            recreateDetector()
        }

        if recorder == nil {
            let tempPath = FileManager.default.temporaryDirectory
                .appendingPathComponent("pitch_realtime_temp.m4a").path
            recorder = SonixRecorder.create(outputPath: tempPath, format: "m4a", quality: "voice")
        }
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
        recordedPitches = []
        isRecording = true

        Task {
            let hwRate = Int(Sonix.hardwareSampleRate)

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
                    self.recordedPitches.append(result.pitch)
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
