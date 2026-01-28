import SwiftUI
import Charts
import VoxaTrace

// MARK: - Pitch Extraction from Audio

/// Batch pitch extraction with ContourCleanup presets.
struct PitchExtractionDemo: View {
    // Configuration
    @State private var selectedAlgorithm: Int = 0 // YIN default
    @State private var selectedPreset: Int = 1 // BALANCED
    @State private var selectedVoiceType: Int = 0 // Auto
    @State private var selectedCleanup: Int = 1 // SCORING
    @State private var hopMs: Int = 10
    @State private var modelLoaderConfigured = false

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

    private let algorithms: [(algorithm: PitchAlgorithm, name: String)] = [
        (.yin, "YIN"),
        (.swiftF0, "SwiftF0")
    ]

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
                Text("Algorithm:")
                    .font(.caption)
                Picker("Algorithm", selection: $selectedAlgorithm) {
                    ForEach(0..<algorithms.count, id: \.self) { index in
                        Text(algorithms[index].name).tag(index)
                    }
                }
                .pickerStyle(.segmented)
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

            Text("• CalibraPitch.createContourExtractor(config: .scoring) { ModelLoader.loadSwiftF0() }")
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

            let algorithm = algorithms[selectedAlgorithm].algorithm
            let extractorConfig = ContourExtractorConfig.Builder()
                .algorithm(algorithm)
                .pitchPreset(presets[selectedPreset].preset)
                .voiceType(voiceTypes[selectedVoiceType].type)
                .cleanup(cleanupPresets[selectedCleanup].cleanup)
                .hopMs(Int32(hopMs))
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
