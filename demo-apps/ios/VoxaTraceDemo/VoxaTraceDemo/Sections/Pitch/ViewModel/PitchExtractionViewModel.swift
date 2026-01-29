import Foundation
import Combine
import VoxaTrace

/// ViewModel for batch pitch extraction.
///
/// ## VoxaTrace Integration (~15 lines)
/// ```swift
/// // 1. Create extractor with configuration
/// let config = ContourExtractorConfig.Builder()
///     .algorithm(.swiftF0)
///     .pitchPreset(.balanced)
///     .cleanup(.scoring)
///     .hopMs(10)
///     .build()
/// let extractor = CalibraPitch.createContourExtractor(config: config)
///
/// // 2. Extract pitch contour
/// let contour = extractor.extract(audio: samples, sampleRate: sampleRate)
/// ```
@MainActor
final class PitchExtractionViewModel: ObservableObject {

    // MARK: - Configuration

    @Published var selectedAlgorithm: Int = 0
    @Published var selectedPreset: Int = 1
    @Published var selectedVoiceType: Int = 0
    @Published var selectedCleanup: Int = 1
    @Published var hopMs: Int = 10

    // MARK: - Published State

    @Published private(set) var isExtracting = false
    @Published private(set) var pitchesHz: [Float] = []
    @Published private(set) var times: [Float] = []
    @Published private(set) var duration: Float = 0
    @Published private(set) var voicedRatio: Float = 0
    @Published private(set) var meanPitchHz: Float = 0
    @Published private(set) var minPitchHz: Float = 0
    @Published private(set) var maxPitchHz: Float = 0
    @Published private(set) var rangeSemitones: Float = 0

    // MARK: - Configuration Data

    let algorithms = PitchAlgorithmInfo.all
    let presets = ExtractionPresetInfo.all
    let voiceTypes = VoiceTypeInfo.all
    let cleanupPresets = CleanupPresetInfo.all

    // MARK: - Actions

    func extractPitch() {
        isExtracting = true
        pitchesHz = []
        times = []

        Task {
            guard let audioURL = Bundle.main.url(forResource: "Alankaar 01_voice", withExtension: "m4a"),
                  let audioData = SonixDecoder.decode(path: audioURL.path) else {
                await MainActor.run { isExtracting = false }
                return
            }

            let algorithm = algorithms[selectedAlgorithm].algorithm
            let extractorConfig = ContourExtractorConfig.Builder()
                .algorithm(algorithm)
                .pitchPreset(presets[selectedPreset].preset)
                .voiceType(voiceTypes[selectedVoiceType].type)
                .cleanup(cleanupPresets[selectedCleanup].cleanup)
                .hopMs(Int32(hopMs))
                .build()

            let extractor = CalibraPitch.createContourExtractor(config: extractorConfig)
            let contour = extractor.extract(audio: audioData.samples, sampleRate: Int32(audioData.sampleRate))
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
