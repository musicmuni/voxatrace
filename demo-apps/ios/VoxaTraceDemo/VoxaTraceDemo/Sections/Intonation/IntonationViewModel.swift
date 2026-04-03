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
/// let eqResult = Accura.analyzePitching(contour: contour, tonicHz: 261.63, intonationSystem: .eq)
/// let jiResult = Accura.analyzePitching(contour: contour, tonicHz: 261.63, intonationSystem: .ji)
///
/// // 4. Calculate scores
/// let eqScore = Accura.calculateScore(result: eqResult)
/// let jiScore = Accura.calculateScore(result: jiResult)
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

            let tonicHz: Float = 261.63

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

            // Calculate scores
            let eqSc = eq.flatMap { Accura.calculateScore(result: $0) }
            let jiSc = ji.flatMap { Accura.calculateScore(result: $0) }

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
