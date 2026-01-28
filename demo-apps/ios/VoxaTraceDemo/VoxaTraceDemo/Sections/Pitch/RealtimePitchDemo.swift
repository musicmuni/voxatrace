import SwiftUI
import Charts
import VoxaTrace

// MARK: - Scrolling Pitch Contour View

/// A real-time scrolling pitch contour visualization using Canvas for performance.
/// Current pitch is fixed at horizontal center, historical values scroll left.
struct ScrollingPitchContourView: View {
    let pitchHistory: [Float]  // Recent pitch values (Hz), newest last
    let maxPoints: Int         // Number of points to display
    let height: CGFloat

    // Fixed Y-axis range: B2 (MIDI 47) to B4 (MIDI 71) - two octaves
    private let minMidi: Float = 47  // B2
    private let maxMidi: Float = 71  // B4

    // Note labels to display on Y-axis (every 2 semitones for readability)
    private var noteLabels: [(midi: Float, label: String)] {
        var labels: [(Float, String)] = []
        var midi: Float = minMidi
        while midi <= maxMidi {
            labels.append((midi, CalibraMusic.midiToNoteLabel(midi)))
            midi += 2  // Every whole tone
        }
        return labels
    }

    var body: some View {
        Canvas { context, size in
            let leftMargin: CGFloat = 30  // Space for Y-axis labels
            let rightMargin: CGFloat = 10
            let topMargin: CGFloat = 8
            let bottomMargin: CGFloat = 8

            let graphWidth = size.width - leftMargin - rightMargin
            let graphHeight = size.height - topMargin - bottomMargin
            let graphRect = CGRect(x: leftMargin, y: topMargin, width: graphWidth, height: graphHeight)

            // Center X position (current pitch location)
            let centerX = leftMargin + graphWidth / 2

            // Helper to convert MIDI to Y coordinate
            func midiToY(_ midi: Float) -> CGFloat {
                let normalizedMidi = (midi - minMidi) / (maxMidi - minMidi)
                return graphRect.maxY - CGFloat(normalizedMidi) * graphHeight
            }

            // Draw background
            context.fill(
                Path(roundedRect: CGRect(origin: .zero, size: size), cornerRadius: 8),
                with: .color(Color(.secondarySystemBackground))
            )

            // Draw horizontal grid lines and Y-axis labels
            for (midi, label) in noteLabels {
                let y = midiToY(midi)

                // Grid line
                var gridPath = Path()
                gridPath.move(to: CGPoint(x: leftMargin, y: y))
                gridPath.addLine(to: CGPoint(x: size.width - rightMargin, y: y))
                context.stroke(gridPath, with: .color(Color.gray.opacity(0.2)), lineWidth: 0.5)

                // Y-axis label
                let text = Text(label).font(.system(size: 8)).foregroundColor(.secondary)
                context.draw(text, at: CGPoint(x: leftMargin - 4, y: y), anchor: .trailing)
            }

            // Draw center vertical line (current position indicator)
            var centerLine = Path()
            centerLine.move(to: CGPoint(x: centerX, y: topMargin))
            centerLine.addLine(to: CGPoint(x: centerX, y: size.height - bottomMargin))
            context.stroke(centerLine, with: .color(Color.blue.opacity(0.3)), lineWidth: 1)

            // Draw pitch contour
            guard !pitchHistory.isEmpty else { return }

            // Calculate point spacing - history fills left half, current at center
            // Points are positioned such that the newest point (last in array) is at center
            let pointSpacing = graphWidth / CGFloat(maxPoints)

            // Start drawing from the oldest visible point
            var contourPath = Path()
            var isDrawing = false

            for (index, pitchHz) in pitchHistory.enumerated() {
                // X position: newest point at center, older points to the left
                let pointsFromEnd = pitchHistory.count - 1 - index
                let x = centerX - CGFloat(pointsFromEnd) * pointSpacing

                // Skip points outside visible area
                if x < leftMargin { continue }

                // Check if voiced (valid pitch)
                let midi = CalibraMusic.hzToMidi(pitchHz)
                let isVoiced = pitchHz > 0 && midi > 0 && !midi.isNaN

                if isVoiced {
                    // Clamp MIDI to visible range
                    let clampedMidi = min(max(midi, minMidi), maxMidi)
                    let y = midiToY(clampedMidi)

                    if isDrawing {
                        contourPath.addLine(to: CGPoint(x: x, y: y))
                    } else {
                        contourPath.move(to: CGPoint(x: x, y: y))
                        isDrawing = true
                    }
                } else {
                    // Gap in pitch - break the line
                    isDrawing = false
                }
            }

            // Stroke the contour
            context.stroke(contourPath, with: .color(.blue), style: StrokeStyle(lineWidth: 2, lineCap: .round, lineJoin: .round))

            // Draw current pitch indicator (dot at center)
            if let lastPitch = pitchHistory.last {
                let midi = CalibraMusic.hzToMidi(lastPitch)
                let isVoiced = lastPitch > 0 && midi > 0 && !midi.isNaN

                if isVoiced {
                    let clampedMidi = min(max(midi, minMidi), maxMidi)
                    let y = midiToY(clampedMidi)

                    // Draw a dot at the current pitch position
                    let dotPath = Path(ellipseIn: CGRect(x: centerX - 4, y: y - 4, width: 8, height: 8))
                    context.fill(dotPath, with: .color(.blue))
                }
            }
        }
        .frame(height: height)
    }
}

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

    // Live scrolling contour
    @State private var pitchHistory: [Float] = []
    private let maxHistoryPoints = 200  // ~2 seconds of history at 10ms hop

    private let algorithms: [(algorithm: PitchAlgorithm, name: String, description: String)] = [
        (.yin, "YIN", "Traditional algorithm, no model needed"),
        (.swiftF0, "SwiftF0", "Neural network, higher accuracy")
    ]

    private let presets: [(config: PitchDetectorConfig, name: String)] = [
        (PitchDetectorConfig.Builder().bufferSize(1024).tolerance(0.20).build(), "Responsive"),
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

            // Live scrolling pitch contour (only show when recording)
            if isRecording {
                ScrollingPitchContourView(
                    pitchHistory: pitchHistory,
                    maxPoints: maxHistoryPoints,
                    height: 120
                )
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

            Text("• CalibraPitch.createDetector(config: .balanced, modelProvider: { ... })")
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
        let detectorConfig = PitchDetectorConfig.Builder()
            .preset(presets[selectedPreset].config)
            .algorithm(algorithm)
            .voiceType(voiceTypes[selectedVoiceType].type)
            .quietHandling(quietHandlings[selectedQuietHandling].handling)
            .strictness(strictnesses[selectedStrictness].strictness)
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
    }

    private func recreateDetectorIfRecording() {
        if isRecording {
            stopRecording()
        }
        recreateDetector()
    }

    private func setupAudioIfNeeded() {
        // Configure audio session for recording
        AudioSessionManager.configure(.recording)

        if detector == nil {
            recreateDetector()
        }

        if recorder == nil {
            let tempPath = FileManager.default.temporaryDirectory
                .appendingPathComponent("pitch_realtime_temp.m4a").path
            recorder = SonixRecorder.create(outputPath: tempPath, config: .voice)
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
        pitchHistory = []
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
                    self.recordedPitches.append(result.pitch)

                    // Update rolling buffer for live visualization
                    self.pitchHistory.append(result.pitch)
                    if self.pitchHistory.count > self.maxHistoryPoints {
                        self.pitchHistory.removeFirst()
                    }
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
