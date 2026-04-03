import Foundation
import Combine
import VoxaTrace

/// ViewModel for Voice Profile - multi-metric batch analysis via Tessera.analyze().
///
/// ## VoxaTrace Integration
/// ```swift
/// // 1. Decode audio
/// let audioData = SonixDecoder.decode(path: filePath)
///
/// // 2. Extract pitch contour
/// let extractor = PitchDetection.createContourExtractor()
/// let contour = extractor.extract(audio: samples, sampleRate: sampleRate)
/// extractor.release()
///
/// // 3. Batch analysis
/// let result = Tessera.analyze(contour: contour)
/// // result.breath, result.agility, result.vocalRange
/// ```
@MainActor
final class VoiceProfileViewModel: ObservableObject {

    // MARK: - Published State

    @Published private(set) var breathCapacity: Float = 0
    @Published private(set) var breathControl: Float = 0
    @Published private(set) var agilityScore: Float = 0
    @Published private(set) var vocalRangeLow: String = ""
    @Published private(set) var vocalRangeHigh: String = ""
    @Published private(set) var vocalRangeOctaves: Float = 0
    @Published private(set) var isAnalyzing = false

    // MARK: - Actions

    func analyzeOffline() {
        isAnalyzing = true
        breathCapacity = 0
        breathControl = 0
        agilityScore = 0
        vocalRangeLow = ""
        vocalRangeHigh = ""
        vocalRangeOctaves = 0

        Task {
            guard let audioURL = Bundle.main.url(forResource: "Alankaar 01_voice", withExtension: "m4a"),
                  let audioData = SonixDecoder.decode(path: audioURL.path) else {
                await MainActor.run { isAnalyzing = false }
                return
            }

            // Extract pitch contour
            let extractor = PitchDetection.createContourExtractor()
            let contour = extractor.extract(audio: audioData.samples, sampleRate: audioData.sampleRate)
            extractor.release()

            // Batch analysis via Tessera.analyze()
            let result = Tessera.analyze(contour: contour)

            var cap: Float = 0
            var ctrl: Float = 0
            var agil: Float = 0
            var low = ""
            var high = ""
            var octaves: Float = 0

            if let breath = result.breath {
                cap = breath.capacity
                ctrl = breath.controlScore
            }
            if let agility = result.agility {
                agil = agility.scores.first?.floatValue ?? 0
            }
            if let vocalRange = result.vocalRange {
                low = vocalRange.range.lower.noteLabel
                high = vocalRange.range.upper.noteLabel
                octaves = vocalRange.range.octaves
            }

            await MainActor.run {
                breathCapacity = cap
                breathControl = ctrl
                agilityScore = agil
                vocalRangeLow = low
                vocalRangeHigh = high
                vocalRangeOctaves = octaves
                isAnalyzing = false
            }
        }
    }
}
