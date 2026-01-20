import SwiftUI
import Charts
import vozos

/// Pitch detection and processing demos using Calibra public API.
///
/// Demonstrates:
/// - Real-time pitch detection with configurable presets
/// - Batch pitch extraction from audio files
/// - Post-processing comparison (raw vs processed contours)
struct PitchSection: View {
    @State private var selectedDemo: Int = 0

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Pitch")
                .font(.headline)

            Picker("Demo", selection: $selectedDemo) {
                Text("Realtime").tag(0)
                Text("Extraction").tag(1)
                Text("Post-Process").tag(2)
            }
            .pickerStyle(.segmented)

            Divider()

            switch selectedDemo {
            case 0:
                RealtimePitchDemo()
            case 1:
                PitchExtractionDemo()
            case 2:
                PostProcessingDemo()
            default:
                EmptyView()
            }
        }
    }
}

// MARK: - Demo 1: Realtime Pitch Detection

/// Real-time pitch detection with configurable presets and session graph.
private struct RealtimePitchDemo: View {
    // Configuration
    @State private var selectedPresetIndex: Int = 0
    @State private var enableProcessing: Bool = true

    // Runtime state
    @State private var detector: CalibraPitch.Detector?
    @State private var recorder: SonixRecorder?
    @State private var isRecording = false
    @State private var currentPitchHz: Float = -1.0
    @State private var noteLabel: String = "-"
    @State private var amplitude: Float = 0.0

    // Session history for graph
    @State private var recordedPitches: [Float] = []
    @State private var showGraph = false

    private let presets: [(preset: PitchPreset, name: String)] = [
        (.vocals, "Vocals"),
        (.highPrecision, "High Precision"),
        (.lowLatency, "Low Latency"),
        (.instrument, "Instrument"),
        (.maleVoice, "Male Voice"),
        (.femaleVoice, "Female Voice")
    ]

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            // Configuration section
            configurationSection

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
                Picker("Preset", selection: $selectedPresetIndex) {
                    ForEach(0..<presets.count, id: \.self) { index in
                        Text(presets[index].name).tag(index)
                    }
                }
                .pickerStyle(.menu)
                .onChange(of: selectedPresetIndex) { _ in
                    if isRecording {
                        stopRecording()
                    }
                    recreateDetector()
                }
            }

            Toggle("Enable Processing (smoothing + octave correction)", isOn: $enableProcessing)
                .font(.caption)
                .onChange(of: enableProcessing) { _ in
                    if isRecording {
                        stopRecording()
                    }
                    recreateDetector()
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
                    Text(currentPitchHz > 0 ? String(format: "%.1f Hz", currentPitchHz) : "-")
                        .font(.title2)
                        .fontWeight(.bold)
                        .foregroundColor(currentPitchHz > 0 ? .primary : .secondary)
                }

                Spacer()

                VStack(alignment: .center) {
                    Text("Note")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    Text(noteLabel)
                        .font(.title2)
                        .fontWeight(.bold)
                        .foregroundColor(currentPitchHz > 0 ? .blue : .secondary)
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

            Text("• CalibraPitch.createDetector(preset:, enableProcessing:)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• detector.detect(buffer:) → PitchPoint(pitch, confidence)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• detector.getAmplitude(buffer:) → Float")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• detector.reset(), detector.release()")
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
        detector = CalibraPitch.createDetector(
            preset: presets[selectedPresetIndex].preset,
            enableProcessing: enableProcessing
        )
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
                // Use pure [Float] API throughout
                let samples16k = SonixResampler.resample(
                    samples: buffer.samples,
                    fromRate: hwRate,
                    toRate: 16000
                )

                let result = detector.detect(buffer: samples16k)
                let amp = detector.getAmplitude(buffer: samples16k)

                await MainActor.run {
                    self.currentPitchHz = result.pitch
                    self.amplitude = amp
                    self.noteLabel = MusicUtils.getMidiNoteName(result.pitch)
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
        currentPitchHz = -1.0
        noteLabel = "-"
        amplitude = 0.0
    }
}

// MARK: - Demo 2: Pitch Extraction from Audio

/// Batch pitch extraction from audio file with statistics and visualization.
private struct PitchExtractionDemo: View {
    // Configuration
    @State private var selectedPresetIndex: Int = 0
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
        (.vocals, "Vocals"),
        (.highPrecision, "High Precision"),
        (.lowLatency, "Low Latency"),
        (.instrument, "Instrument"),
        (.maleVoice, "Male Voice"),
        (.femaleVoice, "Female Voice")
    ]

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            // Configuration
            configurationSection

            Divider()

            // Extract button
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

            // Results
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
                Picker("Preset", selection: $selectedPresetIndex) {
                    ForEach(0..<presets.count, id: \.self) { index in
                        Text(presets[index].name).tag(index)
                    }
                }
                .pickerStyle(.menu)
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

            Text("• SonixDecoder.decode(path:) → AudioData")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• SonixResampler.resample(samples:, fromRate:, toRate:)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• CalibraPitch.createContourExtractor(preset:, hopMs:)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• extractor.extract(audio:) → PitchContour")
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

            let extractor = CalibraPitch.createContourExtractor(
                preset: presets[selectedPresetIndex].preset,
                hopMs: Int32(hopMs)
            )
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

// MARK: - Demo 3: Post-Processing Comparison

/// Demonstrates raw vs post-processed pitch contours.
private struct PostProcessingDemo: View {
    // Recording state
    @State private var recorder: SonixRecorder?
    @State private var isRecording = false
    @State private var collectedSamples: [Float] = []

    // Processing state
    @State private var isProcessing = false
    @State private var rawPitches: [Float] = []
    @State private var smoothedPitches: [Float] = []
    @State private var octaveCorrectedPitches: [Float] = []
    @State private var medianFilteredPitches: [Float] = []
    @State private var fullyProcessedPitches: [Float] = []

    // Configuration
    @State private var smoothWindowSize: Int = 15
    @State private var octaveThresholdCents: Int = 500
    @State private var medianKernelSize: Int = 15

    // Display toggles
    @State private var showRaw = true
    @State private var showSmoothed = false
    @State private var showOctaveCorrected = false
    @State private var showMedianFiltered = false
    @State private var showFullyProcessed = true

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            // Recording section
            recordingSection

            if !rawPitches.isEmpty {
                Divider()

                // Configuration
                configurationSection

                Divider()

                // Display toggles
                displayTogglesSection

                // Graph
                graphSection

                // Statistics
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

            Text("Record a short vocal sample to compare raw vs processed pitch contours.")
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

            if !rawPitches.isEmpty {
                Button("Clear & Record New") {
                    clearResults()
                }
                .buttonStyle(.bordered)
            }
        }
    }

    private var configurationSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Post-Processing Parameters")
                .font(.subheadline)
                .fontWeight(.semibold)

            HStack {
                Text("Smooth Window:")
                    .font(.caption)
                Stepper("\(smoothWindowSize)", value: $smoothWindowSize, in: 5...35, step: 2)
                    .font(.caption)
            }
            .onChange(of: smoothWindowSize) { _ in reprocessIfNeeded() }

            HStack {
                Text("Octave Threshold:")
                    .font(.caption)
                Stepper("\(octaveThresholdCents) cents", value: $octaveThresholdCents, in: 200...1000, step: 100)
                    .font(.caption)
            }
            .onChange(of: octaveThresholdCents) { _ in reprocessIfNeeded() }

            HStack {
                Text("Median Kernel:")
                    .font(.caption)
                Stepper("\(medianKernelSize)", value: $medianKernelSize, in: 5...35, step: 2)
                    .font(.caption)
            }
            .onChange(of: medianKernelSize) { _ in reprocessIfNeeded() }
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }

    private var displayTogglesSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Display Contours")
                .font(.subheadline)
                .fontWeight(.semibold)

            LazyVGrid(columns: [GridItem(.flexible()), GridItem(.flexible())], spacing: 8) {
                ToggleChip(label: "Raw", color: .gray, isOn: $showRaw)
                ToggleChip(label: "Smoothed", color: .blue, isOn: $showSmoothed)
                ToggleChip(label: "Octave Corrected", color: .orange, isOn: $showOctaveCorrected)
                ToggleChip(label: "Median Filtered", color: .purple, isOn: $showMedianFiltered)
                ToggleChip(label: "Fully Processed", color: .green, isOn: $showFullyProcessed)
            }
        }
    }

    private var graphSection: some View {
        VStack(alignment: .leading, spacing: 4) {
            let contours: [(pitches: [Float], color: Color, label: String)] = [
                showRaw ? (rawPitches, .gray, "Raw") : nil,
                showSmoothed ? (smoothedPitches, .blue, "Smoothed") : nil,
                showOctaveCorrected ? (octaveCorrectedPitches, .orange, "Octave Corrected") : nil,
                showMedianFiltered ? (medianFilteredPitches, .purple, "Median Filtered") : nil,
                showFullyProcessed ? (fullyProcessedPitches, .green, "Fully Processed") : nil
            ].compactMap { $0 }

            if contours.isEmpty {
                Text("Select at least one contour to display")
                    .foregroundColor(.secondary)
                    .frame(height: 200)
                    .frame(maxWidth: .infinity)
                    .background(Color(.secondarySystemBackground))
                    .cornerRadius(8)
            } else {
                PitchGraphView(
                    contours: contours,
                    title: "Pitch Comparison",
                    height: 250
                )
            }
        }
    }

    private var statisticsSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Octave Error Detection")
                .font(.subheadline)
                .fontWeight(.semibold)

            let rawErrors = countOctaveErrors(rawPitches)
            let processedErrors = countOctaveErrors(fullyProcessedPitches)
            let fixed = max(0, rawErrors - processedErrors)

            HStack {
                StatCard(title: "Raw Errors", value: "\(rawErrors)")
                StatCard(title: "After Processing", value: "\(processedErrors)")
                StatCard(title: "Fixed", value: "\(fixed)", highlight: fixed > 0)
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

            Text("• CalibraPitch.createContourExtractor().extract(audio:)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• CalibraPitch.PostProcess.process(pitchesHz:)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• CalibraPitch.PostProcess.smooth(pitchesHz:, windowSize:)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• CalibraPitch.PostProcess.correctOctaveErrors(pitchesHz:, thresholdCents:)")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• CalibraPitch.PostProcess.medianFilter(pitchesHz:, kernelSize:)")
                .font(.caption2)
                .foregroundColor(.secondary)
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }

    // MARK: - Audio Logic

    private func setupRecorderIfNeeded() {
        guard recorder == nil else { return }
        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("pitch_postprocess_temp.m4a").path
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
                // Use [Float] API - buffer.samples returns native Swift array
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
        rawPitches = []
        smoothedPitches = []
        octaveCorrectedPitches = []
        medianFilteredPitches = []
        fullyProcessedPitches = []
        collectedSamples = []
    }

    private func processRecording() {
        guard !collectedSamples.isEmpty else { return }
        isProcessing = true

        Task {
            // Extract raw pitch contour
            let extractor = CalibraPitch.createContourExtractor(hopMs: 10)
            let contour = extractor.extract(audio: collectedSamples)
            extractor.release()

            let raw = contour.pitchesHz

            // Apply each post-processing function
            let smoothed = CalibraPitch.PostProcess.smooth(
                pitchesHz: raw,
                windowSize: Int32(smoothWindowSize)
            )
            let octaveCorrected = CalibraPitch.PostProcess.correctOctaveErrors(
                pitchesHz: raw,
                thresholdCents: Float(octaveThresholdCents)
            )
            let medianFiltered = CalibraPitch.PostProcess.medianFilter(
                pitchesHz: raw,
                kernelSize: Int32(medianKernelSize)
            )
            let fullyProcessed = CalibraPitch.PostProcess.process(
                pitchesHz: raw,
                smoothingWindowSize: Int32(smoothWindowSize),
                octaveThresholdCents: Float(octaveThresholdCents)
            )

            await MainActor.run {
                rawPitches = raw
                smoothedPitches = smoothed
                octaveCorrectedPitches = octaveCorrected
                medianFilteredPitches = medianFiltered
                fullyProcessedPitches = fullyProcessed
                isProcessing = false
            }
        }
    }

    private func reprocessIfNeeded() {
        guard !rawPitches.isEmpty else { return }

        Task {
            let smoothed = CalibraPitch.PostProcess.smooth(
                pitchesHz: rawPitches,
                windowSize: Int32(smoothWindowSize)
            )
            let octaveCorrected = CalibraPitch.PostProcess.correctOctaveErrors(
                pitchesHz: rawPitches,
                thresholdCents: Float(octaveThresholdCents)
            )
            let medianFiltered = CalibraPitch.PostProcess.medianFilter(
                pitchesHz: rawPitches,
                kernelSize: Int32(medianKernelSize)
            )
            let fullyProcessed = CalibraPitch.PostProcess.process(
                pitchesHz: rawPitches,
                smoothingWindowSize: Int32(smoothWindowSize),
                octaveThresholdCents: Float(octaveThresholdCents)
            )

            await MainActor.run {
                smoothedPitches = smoothed
                octaveCorrectedPitches = octaveCorrected
                medianFilteredPitches = medianFiltered
                fullyProcessedPitches = fullyProcessed
            }
        }
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

// MARK: - PitchContour Extensions

private extension PitchContour {
    var pitchesHz: [Float] {
        toPitchesArray()
    }

    var times: [Float] {
        toTimesArray()
    }
}
