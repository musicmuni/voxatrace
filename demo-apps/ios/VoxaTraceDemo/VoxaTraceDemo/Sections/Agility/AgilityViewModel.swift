import Foundation
import Combine
import VoxaTrace

/// ViewModel for vocal agility analysis using TesseraAgility.
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
/// // 3. Compute agility contour and score
/// let ac = TesseraAgility.computeContour(contour: contour)
/// let score = TesseraAgility.computeScore(agilityContour: ac)
/// ```
@MainActor
final class AgilityViewModel: ObservableObject {

    // MARK: - Published State

    @Published private(set) var agilityScore: Float = 0
    @Published private(set) var rmsPeak: Float = 0
    @Published private(set) var oscPeak: Float = 0
    @Published private(set) var isAnalyzing: Bool = false
    @Published private(set) var hasResult: Bool = false

    // MARK: - Actions

    func analyzeOffline() {
        isAnalyzing = true
        hasResult = false
        agilityScore = 0
        rmsPeak = 0
        oscPeak = 0

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

            // Compute agility contour (intermediate) and score
            let ac = TesseraAgility.computeContour(contour: contour)
            let score = TesseraAgility.computeScore(agilityContour: ac)

            let rMax = ac.rms.max() ?? 0
            let oMax = ac.rmsOsc.max() ?? 0
            let s = score.scores.first?.floatValue ?? 0

            await MainActor.run {
                agilityScore = s
                rmsPeak = rMax
                oscPeak = oMax
                hasResult = true
                isAnalyzing = false
            }
        }
    }
}
