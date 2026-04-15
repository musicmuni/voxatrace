import Foundation
import Combine
import VoxaTrace

/// ViewModel for intonation analysis using Accura.
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
/// // 3. Analyze intonation against EQ and JI
/// let eqResult = Accura.analyzePitching(contour: contour, tonicHz: 146.83, intonationSystem: .eq)
/// let jiResult = Accura.analyzePitching(contour: contour, tonicHz: 146.83, intonationSystem: .ji)
///
/// // 4. Calculate scores (only when analysis succeeded — ADR-022)
/// let eqScore = eqResult.error == nil ? Accura.calculateScore(result: eqResult) : nil
/// let jiScore = jiResult.error == nil ? Accura.calculateScore(result: jiResult) : nil
/// ```
@MainActor
final class IntonationViewModel: ObservableObject {

    // MARK: - Published State

    @Published private(set) var eqResult: IntonationAnalysisResult?
    @Published private(set) var jiResult: IntonationAnalysisResult?
    @Published private(set) var eqScore: PitchingScore?
    @Published private(set) var jiScore: PitchingScore?
    @Published private(set) var isAnalyzing = false

    // MARK: - Actions

    func analyzeOffline() {
        isAnalyzing = true
        eqResult = nil
        jiResult = nil
        eqScore = nil
        jiScore = nil

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

            let tonicHz: Float = 146.83

            // Analyze against Equal Temperament
            let eq = Accura.analyzePitching(
                contour: contour,
                tonicHz: tonicHz,
                intonationSystem: .eq,
                noteLabelTradition: .carnatic
            )

            // Analyze against Just Intonation
            let ji = Accura.analyzePitching(
                contour: contour,
                tonicHz: tonicHz,
                intonationSystem: .ji,
                noteLabelTradition: .carnatic
            )

            // Calculate scores (only when analysis succeeded — ADR-022)
            let eqSc = eq.error == nil ? Accura.calculateScore(result: eq) : nil
            let jiSc = ji.error == nil ? Accura.calculateScore(result: ji) : nil

            await MainActor.run {
                eqResult = eq
                jiResult = ji
                eqScore = eqSc
                jiScore = jiSc
                isAnalyzing = false
            }
        }
    }
}
