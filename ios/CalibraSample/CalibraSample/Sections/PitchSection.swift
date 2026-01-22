import SwiftUI
import Charts
import vozos
import vozosAI

/// Pitch detection and processing demos using Calibra public API.
///
/// Demonstrates:
/// - Real-time pitch detection with tiered builder API
/// - Batch pitch extraction with ContourCleanup presets
/// - Cleanup comparison (RAW vs SCORING vs DISPLAY)
/// - PitchPoint computed properties explorer
/// - SwiftF0 neural pitch detection
struct PitchSection: View {
    @State private var selectedDemo: Int = 0

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Pitch")
                .font(.headline)

            Picker("Demo", selection: $selectedDemo) {
                Text("Realtime").tag(0)
                Text("SwiftF0").tag(1)
                Text("Extraction").tag(2)
                Text("Cleanup").tag(3)
                Text("PitchPoint").tag(4)
            }
            .pickerStyle(.segmented)

            Divider()

            switch selectedDemo {
            case 0:
                RealtimePitchDemo()
            case 1:
                SwiftF0Demo()
            case 2:
                PitchExtractionDemo()
            case 3:
                ContourCleanupDemo()
            case 4:
                PitchPointExplorerDemo()
            default:
                EmptyView()
            }
        }
    }
}

// MARK: - Demo 1: Realtime Pitch Detection

/// Real-time pitch detection with tiered builder API.
///
/// Showcases:
/// - PitchPreset (RESPONSIVE, BALANCED, PRECISE)
/// - VoiceType (Western, Carnatic, Hindustani, Pop, IndianFilm)
/// - QuietHandling (SENSITIVE, NORMAL, NOISY)
/// - DetectionStrictness (STRICT, BALANCED, LENIENT)
/// - DetectorBuilder API
private struct RealtimePitchDemo: View {
    // Configuration
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

    // Session history for graph
    @State private var recordedPitches: [Float] = []
    @State private var showGraph = false

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
        detector = CalibraPitch.DetectorBuilder()
            .preset(presets[selectedPreset].preset)
            .voiceType(voiceTypes[selectedVoiceType].type)
            .quietHandling(quietHandlings[selectedQuietHandling].handling)
            .strictness(strictnesses[selectedStrictness].strictness)
            .build()
    }

    private func recreateDetectorIfRecording() {
        if isRecording {
            stopRecording()
        }
        recreateDetector()
    }

    private func setupAudioIfNeeded() {
        guard detector == nil else { return }
        recreateDetector()

        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("pitch_realtime_temp.m4a").path
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

// MARK: - Demo 2: SwiftF0 Neural Pitch Detection

/// SwiftF0 neural network pitch detection.
///
/// Showcases:
/// - Neural network-based F0 estimation using ONNX Runtime
/// - Higher accuracy than traditional YIN algorithm
/// - Real-time performance on mobile devices
private struct SwiftF0Demo: View {
    // Runtime state
    @State private var swiftF0: SwiftF0?
    @State private var recorder: SonixRecorder?
    @State private var isRecording = false
    @State private var isInitializing = false
    @State private var initError: String?

    // Pitch results
    @State private var currentPitchHz: Float = 0
    @State private var confidence: Float = 0
    @State private var amplitude: Float = 0

    // Session history for graph
    @State private var recordedPitches: [Float] = []
    @State private var showGraph = false

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            // Header
            headerSection

            Divider()

            // Live display
            liveDisplaySection

            // Session graph (shown after stopping)
            if showGraph && !recordedPitches.isEmpty {
                sessionGraphSection
            }

            Divider()

            // API info
            apiInfoSection
        }
        .onAppear {
            initializeSwiftF0()
        }
        .onDisappear {
            stopRecording()
            cleanup()
        }
    }

    private var headerSection: some View {
        VStack(alignment: .leading, spacing: 4) {
            HStack {
                Text("SwiftF0 Neural Pitch")
                    .font(.subheadline)
                    .fontWeight(.semibold)

                Spacer()

                if swiftF0 != nil {
                    Label("Ready", systemImage: "checkmark.circle.fill")
                        .font(.caption)
                        .foregroundColor(.green)
                } else if isInitializing {
                    ProgressView()
                        .scaleEffect(0.7)
                } else if let error = initError {
                    Label("Error", systemImage: "exclamationmark.circle.fill")
                        .font(.caption)
                        .foregroundColor(.red)
                }
            }

            Text("Neural network F0 estimation using ONNX Runtime")
                .font(.caption)
                .foregroundColor(.secondary)

            if let error = initError {
                Text(error)
                    .font(.caption2)
                    .foregroundColor(.red)
                    .padding(6)
                    .background(Color.red.opacity(0.1))
                    .cornerRadius(4)
            }
        }
    }

    private var liveDisplaySection: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack {
                VStack(alignment: .leading) {
                    Text("Pitch")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    if currentPitchHz > 0 {
                        Text(String(format: "%.1f Hz", currentPitchHz))
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
                    if currentPitchHz > 0 {
                        Text(MusicUtils.getMidiNoteName(currentPitchHz))
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
                    Text("Confidence")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    Text(String(format: "%.0f%%", confidence * 100))
                        .font(.title2)
                        .fontWeight(.bold)
                        .foregroundColor(confidence > 0.5 ? .green : .orange)
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
                .disabled(swiftF0 == nil)

                if showGraph && !recordedPitches.isEmpty {
                    Button("Clear") {
                        recordedPitches = []
                        showGraph = false
                    }
                    .buttonStyle(.bordered)
                }

                if swiftF0 == nil && initError != nil {
                    Button("Retry") {
                        initializeSwiftF0()
                    }
                    .buttonStyle(.bordered)
                }
            }
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
                title: "SwiftF0 Session"
            )
        }
    }

    private var apiInfoSection: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("APIs Demonstrated:")
                .font(.caption)
                .fontWeight(.medium)

            Text("• ModelLoader.initialize()")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• SwiftF0.Companion.shared.create(sampleRate: 16000) { ModelLoader.loadSwiftF0() }")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• swiftF0.detect(samples:) -> PitchResult(pitchHz, confidence)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• Uses ONNX Runtime for neural network inference")
                .font(.caption2)
                .foregroundColor(.secondary)
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }

    // MARK: - SwiftF0 Logic

    private func initializeSwiftF0() {
        guard swiftF0 == nil else { return }

        isInitializing = true
        initError = nil

        Task {
            do {
                // Initialize model loader (finds vozosAI.bundle)
                ModelLoader.initialize()

                // Check if model is available
                guard ModelLoader.hasSwiftF0Model() else {
                    throw NSError(domain: "SwiftF0", code: 1, userInfo: [
                        NSLocalizedDescriptionKey: "SwiftF0 model not found in ai-models.bundle"
                    ])
                }

                // Create SwiftF0 instance with model provider
                let detector = SwiftF0.Companion.shared.create(
                    sampleRate: 16000,
                    modelProvider: { ModelLoader.loadSwiftF0() }
                )

                await MainActor.run {
                    self.swiftF0 = detector
                    self.isInitializing = false
                }
            } catch {
                await MainActor.run {
                    self.initError = error.localizedDescription
                    self.isInitializing = false
                }
            }
        }
    }

    private func cleanup() {
        recorder?.stop()
        recorder?.release()
        recorder = nil

        swiftF0?.close()
        swiftF0 = nil
    }

    private func startRecording() {
        guard let swiftF0 = swiftF0 else { return }

        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("swiftf0_realtime_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, format: "m4a", quality: "voice")

        swiftF0.reset()
        recordedPitches = []
        isRecording = true

        Task {
            guard let recorder = recorder else { return }
            let hwRate = Int(Sonix.hardwareSampleRate)

            for await buffer in recorder.audioBuffers {
                let samples16k = SonixResampler.resample(
                    samples: buffer.samples,
                    fromRate: hwRate,
                    toRate: 16000
                )

                // Use SwiftF0 for pitch detection
                let result = swiftF0.detect(samples: samples16k)

                // Calculate amplitude for visualization
                let amp = samples16k.map { abs($0) }.max() ?? 0

                await MainActor.run {
                    self.currentPitchHz = result.pitchHz
                    self.confidence = result.confidence
                    self.amplitude = min(amp * 2, 1.0) // Scale for visibility

                    // Record pitch for graph (0 if not voiced)
                    let pitch = result.isVoiced ? result.pitchHz : 0
                    self.recordedPitches.append(pitch)
                }
            }
        }

        recorder?.start()
    }

    private func stopRecording() {
        recorder?.stop()
        isRecording = false

        swiftF0?.reset()
        currentPitchHz = 0
        confidence = 0
        amplitude = 0
    }
}

// MARK: - Demo 3: Pitch Extraction from Audio

/// Batch pitch extraction with ContourCleanup presets.
private struct PitchExtractionDemo: View {
    // Configuration
    @State private var selectedPreset: Int = 1 // BALANCED
    @State private var selectedVoiceType: Int = 0 // Auto
    @State private var selectedCleanup: Int = 1 // SCORING
    @State private var hopMs: Int = 10

    // Results
    @State private var isExtracting = false
    @State private var pitchesHz: [Float] = []
    @State private var times: [Float] = []

    // Statistics
    @State private var duration: Float = 0
    @State private var voicedRatio: Float = 0
    @State private var meanPitchHz: Float = 0
    @State private var minPitchHz: Float = 0
    @State private var maxPitchHz: Float = 0
    @State private var rangeSemitones: Float = 0

    private let presets: [(preset: PitchPreset, name: String)] = [
        (.responsive, "Responsive"),
        (.balanced, "Balanced"),
        (.precise, "Precise")
    ]

    private let voiceTypes: [(type: VoiceType, name: String)] = [
        (.auto, "Auto"),
        (.carnaticMale, "Carnatic Male"),
        (.carnaticFemale, "Carnatic Female"),
        (.hindustaniMale, "Hindustani Male"),
        (.hindustaniFemale, "Hindustani Female"),
        (.westernTenor, "Western Tenor"),
        (.westernSoprano, "Western Soprano")
    ]

    private let cleanupPresets: [(cleanup: ContourCleanup, name: String, description: String)] = [
        (.raw, "RAW", "No post-processing"),
        (.scoring, "SCORING", "Octave + boundary + blip fix"),
        (.display, "DISPLAY", "Scoring + smoothing")
    ]

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            configurationSection

            Divider()

            HStack {
                Button("Extract from Alankaar Voice") {
                    extractPitch()
                }
                .buttonStyle(.borderedProminent)
                .disabled(isExtracting)

                if isExtracting {
                    ProgressView()
                        .padding(.leading, 8)
                }
            }

            if !pitchesHz.isEmpty {
                statisticsSection
                graphSection
            }

            Divider()

            apiInfoSection
        }
    }

    private var configurationSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Configuration")
                .font(.subheadline)
                .fontWeight(.semibold)

            HStack {
                Text("Preset:")
                    .font(.caption)
                Picker("Preset", selection: $selectedPreset) {
                    ForEach(0..<presets.count, id: \.self) { index in
                        Text(presets[index].name).tag(index)
                    }
                }
                .pickerStyle(.segmented)
            }

            HStack {
                Text("Voice Type:")
                    .font(.caption)
                Picker("Voice Type", selection: $selectedVoiceType) {
                    ForEach(0..<voiceTypes.count, id: \.self) { index in
                        Text(voiceTypes[index].name).tag(index)
                    }
                }
                .pickerStyle(.menu)
            }

            VStack(alignment: .leading, spacing: 4) {
                HStack {
                    Text("Cleanup:")
                        .font(.caption)
                    Picker("Cleanup", selection: $selectedCleanup) {
                        ForEach(0..<cleanupPresets.count, id: \.self) { index in
                            Text(cleanupPresets[index].name).tag(index)
                        }
                    }
                    .pickerStyle(.segmented)
                }
                Text(cleanupPresets[selectedCleanup].description)
                    .font(.caption2)
                    .foregroundColor(.secondary)
            }

            HStack {
                Text("Hop Size:")
                    .font(.caption)
                Stepper("\(hopMs) ms", value: $hopMs, in: 5...50, step: 5)
                    .font(.caption)
            }
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }

    private var statisticsSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Statistics")
                .font(.subheadline)
                .fontWeight(.semibold)

            LazyVGrid(columns: [
                GridItem(.flexible()),
                GridItem(.flexible()),
                GridItem(.flexible())
            ], spacing: 12) {
                StatCard(title: "Duration", value: String(format: "%.2fs", duration))
                StatCard(title: "Voiced", value: String(format: "%.0f%%", voicedRatio * 100))
                StatCard(title: "Samples", value: "\(pitchesHz.count)")
                StatCard(title: "Mean", value: "\(Int(meanPitchHz)) Hz")
                StatCard(title: "Min", value: "\(Int(minPitchHz)) Hz\n(\(MusicUtils.getMidiNoteName(minPitchHz)))")
                StatCard(title: "Max", value: "\(Int(maxPitchHz)) Hz\n(\(MusicUtils.getMidiNoteName(maxPitchHz)))")
            }

            HStack {
                Text("Pitch Range:")
                    .font(.caption)
                Text(String(format: "%.1f semitones", rangeSemitones))
                    .font(.caption)
                    .fontWeight(.medium)
            }
        }
        .padding(8)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(8)
    }

    private var graphSection: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("Pitch Contour")
                .font(.subheadline)
                .fontWeight(.semibold)

            PitchGraphView(
                pitchesHz: pitchesHz,
                times: times,
                title: "Alankaar Voice"
            )
        }
    }

    private var apiInfoSection: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("APIs Demonstrated:")
                .font(.caption)
                .fontWeight(.medium)

            Text("• CalibraPitch.ContourExtractorBuilder().preset(.precise).cleanup(.scoring).build()")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• extractor.extract(audio:) → PitchContour")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• ContourCleanup: .raw, .scoring, .display")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• PitchContour: duration, voicedRatio, pitchesHz, times")
                .font(.caption2)
                .foregroundColor(.secondary)
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }

    private func extractPitch() {
        isExtracting = true
        pitchesHz = []
        times = []

        Task {
            guard let audioURL = Bundle.main.url(forResource: "Alankaar 01_voice", withExtension: "m4a"),
                  let audioData = SonixDecoder.decode(path: audioURL.path) else {
                await MainActor.run { isExtracting = false }
                return
            }

            let samples16k = SonixResampler.resample(
                samples: audioData.samples,
                fromRate: Int(audioData.sampleRate),
                toRate: 16000
            )

            let extractor = CalibraPitch.ContourExtractorBuilder()
                .preset(presets[selectedPreset].preset)
                .voiceType(voiceTypes[selectedVoiceType].type)
                .cleanup(cleanupPresets[selectedCleanup].cleanup)
                .hopMs(Int32(hopMs))
                .build()

            let contour = extractor.extract(audio: samples16k)
            extractor.release()

            let extractedPitches = contour.pitchesHz
            let extractedTimes = contour.times

            // Calculate statistics
            let voiced = extractedPitches.filter { $0 > 0 }
            let voicedRatioCalc = Float(voiced.count) / Float(extractedPitches.count)
            let meanCalc = voiced.isEmpty ? 0 : voiced.reduce(0, +) / Float(voiced.count)
            let minCalc = voiced.min() ?? 0
            let maxCalc = voiced.max() ?? 0
            let rangeCalc = (minCalc > 0 && maxCalc > 0) ? 12 * log2(maxCalc / minCalc) : 0

            await MainActor.run {
                pitchesHz = extractedPitches
                times = extractedTimes
                duration = contour.duration
                voicedRatio = voicedRatioCalc
                meanPitchHz = meanCalc
                minPitchHz = minCalc
                maxPitchHz = maxCalc
                rangeSemitones = rangeCalc
                isExtracting = false
            }
        }
    }
}

// MARK: - Demo 3: Contour Cleanup Comparison

/// Compares RAW vs SCORING vs DISPLAY ContourCleanup presets.
private struct ContourCleanupDemo: View {
    // Recording state
    @State private var recorder: SonixRecorder?
    @State private var isRecording = false
    @State private var collectedSamples: [Float] = []

    // Processing state
    @State private var isProcessing = false
    @State private var rawContour: PitchContour?
    @State private var scoringContour: PitchContour?
    @State private var displayContour: PitchContour?

    // Display toggles
    @State private var showRaw = true
    @State private var showScoring = true
    @State private var showDisplay = false

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            recordingSection

            if rawContour != nil {
                Divider()
                displayTogglesSection
                graphSection
                statisticsSection
            }

            Divider()
            apiInfoSection
        }
        .onDisappear {
            stopRecording()
            cleanup()
        }
    }

    private var recordingSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Record Audio Sample")
                .font(.subheadline)
                .fontWeight(.semibold)

            Text("Record a vocal sample to compare cleanup presets.")
                .font(.caption)
                .foregroundColor(.secondary)

            HStack {
                Button(isRecording ? "Stop Recording" : "Start Recording") {
                    if isRecording {
                        stopRecording()
                        processRecording()
                    } else {
                        startRecording()
                    }
                }
                .buttonStyle(.borderedProminent)
                .disabled(isProcessing)

                if isRecording {
                    Circle()
                        .fill(.red)
                        .frame(width: 12, height: 12)
                    Text("Recording...")
                        .font(.caption)
                        .foregroundColor(.red)
                }

                if isProcessing {
                    ProgressView()
                        .padding(.leading, 8)
                    Text("Processing...")
                        .font(.caption)
                }
            }

            if rawContour != nil {
                Button("Clear & Record New") {
                    clearResults()
                }
                .buttonStyle(.bordered)
            }
        }
    }

    private var displayTogglesSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Display Presets")
                .font(.subheadline)
                .fontWeight(.semibold)

            HStack(spacing: 12) {
                ToggleChip(label: "RAW", color: .gray, isOn: $showRaw)
                ToggleChip(label: "SCORING", color: .green, isOn: $showScoring)
                ToggleChip(label: "DISPLAY", color: .blue, isOn: $showDisplay)
            }
        }
    }

    private var graphSection: some View {
        VStack(alignment: .leading, spacing: 4) {
            var contours: [(pitches: [Float], color: Color, label: String)] = []

            let _ = {
                if showRaw, let raw = rawContour {
                    contours.append((raw.pitchesHz, .gray, "RAW"))
                }
                if showScoring, let scoring = scoringContour {
                    contours.append((scoring.pitchesHz, .green, "SCORING"))
                }
                if showDisplay, let display = displayContour {
                    contours.append((display.pitchesHz, .blue, "DISPLAY"))
                }
            }()

            if contours.isEmpty {
                Text("Select at least one preset to display")
                    .foregroundColor(.secondary)
                    .frame(height: 200)
                    .frame(maxWidth: .infinity)
                    .background(Color(.secondarySystemBackground))
                    .cornerRadius(8)
            } else {
                PitchGraphView(
                    contours: contours,
                    title: "Cleanup Comparison",
                    height: 250
                )
            }
        }
    }

    private var statisticsSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Comparison Statistics")
                .font(.subheadline)
                .fontWeight(.semibold)

            let rawStats = contourStats(rawContour)
            let scoringStats = contourStats(scoringContour)
            let displayStats = contourStats(displayContour)

            LazyVGrid(columns: [
                GridItem(.flexible()),
                GridItem(.flexible()),
                GridItem(.flexible()),
                GridItem(.flexible())
            ], spacing: 8) {
                Text("Preset").font(.caption2).fontWeight(.semibold)
                Text("Voiced").font(.caption2).fontWeight(.semibold)
                Text("Octave Err").font(.caption2).fontWeight(.semibold)
                Text("Blips").font(.caption2).fontWeight(.semibold)

                Text("RAW").font(.caption2)
                Text("\(rawStats.voicedCount)").font(.caption2)
                Text("\(rawStats.octaveErrors)").font(.caption2)
                Text("\(rawStats.blips)").font(.caption2)

                Text("SCORING").font(.caption2)
                Text("\(scoringStats.voicedCount)").font(.caption2)
                Text("\(scoringStats.octaveErrors)").font(.caption2).foregroundColor(scoringStats.octaveErrors < rawStats.octaveErrors ? .green : .primary)
                Text("\(scoringStats.blips)").font(.caption2).foregroundColor(scoringStats.blips < rawStats.blips ? .green : .primary)

                Text("DISPLAY").font(.caption2)
                Text("\(displayStats.voicedCount)").font(.caption2)
                Text("\(displayStats.octaveErrors)").font(.caption2).foregroundColor(displayStats.octaveErrors < rawStats.octaveErrors ? .green : .primary)
                Text("\(displayStats.blips)").font(.caption2).foregroundColor(displayStats.blips < rawStats.blips ? .green : .primary)
            }
        }
        .padding(8)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(8)
    }

    private var apiInfoSection: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("APIs Demonstrated:")
                .font(.caption)
                .fontWeight(.medium)

            Text("• CalibraPitch.PostProcess.cleanup(contour, options: .scoring)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• ContourCleanup.raw, .scoring, .display")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• CalibraPitch.PostProcess.fixOctaveErrors(contour)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• CalibraPitch.PostProcess.removeBlips(contour, minDurationMs:)")
                .font(.caption2)
                .foregroundColor(.secondary)
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }

    // MARK: - Statistics

    private struct ContourStats {
        let voicedCount: Int
        let octaveErrors: Int
        let blips: Int
    }

    private func contourStats(_ contour: PitchContour?) -> ContourStats {
        guard let contour = contour else {
            return ContourStats(voicedCount: 0, octaveErrors: 0, blips: 0)
        }
        let pitches = contour.pitchesHz
        let voiced = pitches.filter { $0 > 0 }.count
        let octaveErrors = countOctaveErrors(pitches)
        let blips = countBlips(pitches, minFrames: 8) // 80ms at 10ms hop
        return ContourStats(voicedCount: voiced, octaveErrors: octaveErrors, blips: blips)
    }

    private func countOctaveErrors(_ pitches: [Float]) -> Int {
        var count = 0
        for i in 1..<pitches.count {
            if pitches[i] > 0 && pitches[i-1] > 0 {
                let ratio = pitches[i] / pitches[i-1]
                if ratio > 1.8 || ratio < 0.55 {
                    count += 1
                }
            }
        }
        return count
    }

    private func countBlips(_ pitches: [Float], minFrames: Int) -> Int {
        var blipCount = 0
        var runLength = 0
        for pitch in pitches {
            if pitch > 0 {
                runLength += 1
            } else {
                if runLength > 0 && runLength < minFrames {
                    blipCount += 1
                }
                runLength = 0
            }
        }
        if runLength > 0 && runLength < minFrames {
            blipCount += 1
        }
        return blipCount
    }

    // MARK: - Audio Logic

    private func setupRecorderIfNeeded() {
        guard recorder == nil else { return }
        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("pitch_cleanup_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, format: "m4a", quality: "voice")
    }

    private func cleanup() {
        recorder?.stop()
        recorder?.release()
        recorder = nil
    }

    private func startRecording() {
        setupRecorderIfNeeded()
        guard let recorder = recorder else { return }

        collectedSamples = []
        isRecording = true

        Task {
            let hwRate = Int(Sonix.hardwareSampleRate)

            for await buffer in recorder.audioBuffers {
                let samples16k = SonixResampler.resample(
                    samples: buffer.samples,
                    fromRate: hwRate,
                    toRate: 16000
                )

                await MainActor.run {
                    collectedSamples.append(contentsOf: samples16k)
                }
            }
        }

        recorder.start()
    }

    private func stopRecording() {
        recorder?.stop()
        isRecording = false
    }

    private func clearResults() {
        rawContour = nil
        scoringContour = nil
        displayContour = nil
        collectedSamples = []
    }

    private func processRecording() {
        guard !collectedSamples.isEmpty else { return }
        isProcessing = true

        Task {
            // Extract raw contour
            let extractor = CalibraPitch.ContourExtractorBuilder()
                .preset(.balanced)
                .cleanup(.raw)
                .hopMs(10)
                .build()
            let raw = extractor.extract(audio: collectedSamples)
            extractor.release()

            // Apply cleanup presets using PostProcess
            let scoring = CalibraPitch.PostProcess.cleanup(raw, options: .scoring)
            let display = CalibraPitch.PostProcess.cleanup(raw, options: .display)

            await MainActor.run {
                rawContour = raw
                scoringContour = scoring
                displayContour = display
                isProcessing = false
            }
        }
    }
}

// MARK: - Demo 4: PitchPoint Explorer

/// Showcases all PitchPoint computed properties in real-time.
private struct PitchPointExplorerDemo: View {
    @State private var detector: CalibraPitch.Detector?
    @State private var recorder: SonixRecorder?
    @State private var isRecording = false
    @State private var currentPitch: PitchPoint?
    @State private var amplitude: Float = 0.0

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("PitchPoint Properties")
                .font(.subheadline)
                .fontWeight(.semibold)

            Text("See all computed properties of PitchPoint in real-time.")
                .font(.caption)
                .foregroundColor(.secondary)

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
        detector = CalibraPitch.createDetector()

        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("pitch_explorer_temp.m4a").path
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

        detector.reset()
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

// MARK: - Helper Views

private struct StatCard: View {
    let title: String
    let value: String
    var highlight: Bool = false

    var body: some View {
        VStack {
            Text(title)
                .font(.caption2)
                .foregroundColor(.secondary)
            Text(value)
                .font(.caption)
                .fontWeight(.semibold)
                .foregroundColor(highlight ? .green : .primary)
                .multilineTextAlignment(.center)
        }
        .frame(maxWidth: .infinity)
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }
}

private struct ToggleChip: View {
    let label: String
    let color: Color
    @Binding var isOn: Bool

    var body: some View {
        Button {
            isOn.toggle()
        } label: {
            HStack(spacing: 4) {
                Circle()
                    .fill(color)
                    .frame(width: 8, height: 8)
                Text(label)
                    .font(.caption)
            }
            .padding(.horizontal, 10)
            .padding(.vertical, 6)
            .background(isOn ? color.opacity(0.2) : Color(.tertiarySystemBackground))
            .cornerRadius(16)
            .overlay(
                RoundedRectangle(cornerRadius: 16)
                    .stroke(isOn ? color : .clear, lineWidth: 1)
            )
        }
        .buttonStyle(.plain)
    }
}

private struct PropertyRow: View {
    let name: String
    let value: String

    var body: some View {
        HStack {
            Text(name)
                .font(.caption)
                .foregroundColor(.secondary)
            Spacer()
            Text(value)
                .font(.caption)
                .fontWeight(.medium)
                .foregroundColor(.primary)
        }
        .padding(.horizontal, 8)
        .padding(.vertical, 4)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(4)
    }
}

private struct TuningIndicatorView: View {
    let centsOff: Int32
    let tuning: PitchPoint.Tuning

    var body: some View {
        VStack(spacing: 4) {
            // Tuning bar
            GeometryReader { geo in
                ZStack {
                    // Background
                    RoundedRectangle(cornerRadius: 4)
                        .fill(Color(.tertiarySystemBackground))

                    // Center line
                    Rectangle()
                        .fill(Color.green)
                        .frame(width: 2)

                    // Indicator
                    Circle()
                        .fill(tuningColor)
                        .frame(width: 16, height: 16)
                        .offset(x: CGFloat(centsOff) / 50.0 * (geo.size.width / 2 - 8))
                }
            }
            .frame(height: 24)

            // Labels
            HStack {
                Text("-50¢")
                    .font(.caption2)
                    .foregroundColor(.secondary)
                Spacer()
                Text("0")
                    .font(.caption2)
                    .foregroundColor(.green)
                Spacer()
                Text("+50¢")
                    .font(.caption2)
                    .foregroundColor(.secondary)
            }

            // Status
            Text(tuningLabel)
                .font(.caption)
                .fontWeight(.medium)
                .foregroundColor(tuningColor)
        }
        .padding(8)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(8)
    }

    private var tuningColor: Color {
        switch tuning {
        case .inTune: return .green
        case .flat, .sharp: return .orange
        case .silent: return .gray
        default: return .gray
        }
    }

    private var tuningLabel: String {
        switch tuning {
        case .inTune: return "IN TUNE"
        case .flat: return "FLAT"
        case .sharp: return "SHARP"
        case .silent: return "SILENT"
        default: return "-"
        }
    }
}
