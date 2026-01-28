import SwiftUI
import Charts
import VoxaTrace

// MARK: - Contour Cleanup Comparison

/// Compares RAW vs SCORING vs DISPLAY ContourCleanup presets.
struct ContourCleanupDemo: View {
    // Algorithm selection
    @State private var selectedAlgorithm: Int = 0 // YIN default
    @State private var modelLoaderConfigured = false

    private let algorithms: [(algorithm: PitchAlgorithm, name: String)] = [
        (.yin, "YIN"),
        (.swiftF0, "SwiftF0")
    ]

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
            algorithmPickerSection
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

    private var algorithmPickerSection: some View {
        HStack {
            Text("Algorithm:")
                .font(.caption)
            Picker("Algorithm", selection: $selectedAlgorithm) {
                ForEach(0..<algorithms.count, id: \.self) { index in
                    Text(algorithms[index].name).tag(index)
                }
            }
            .pickerStyle(.segmented)
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
        // Configure audio session for recording
        AudioSessionManager.configure(.recording)
        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("pitch_cleanup_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, config: .voice)
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
            let hwRate = AudioSessionManager.hardwareSampleRate

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
            let algorithm = algorithms[selectedAlgorithm].algorithm
            let extractorConfig = ContourExtractorConfig.Builder()
                .algorithm(algorithm)
                .pitchPreset(.balanced)
                .cleanup(.raw)
                .hopMs(10)
                .build()

            // SwiftF0 requires model provider
            var modelProvider: (() -> KotlinByteArray)? = nil
            if algorithm == .swiftF0 {
                if !modelLoaderConfigured {
                    ModelLoader.configure()
                    await MainActor.run { modelLoaderConfigured = true }
                }
                modelProvider = { ModelLoader.loadSwiftF0() }
            }

            let extractor = CalibraPitch.createContourExtractor(config: extractorConfig, modelProvider: modelProvider)
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
