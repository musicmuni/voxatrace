import Foundation
import VoxaTrace

/// ViewModel for song matching using TesseraRange search vectors.
///
/// ## VoxaTrace Integration
/// ```swift
/// // 1. Decode audio files
/// let singerData = SonixDecoder.decode(path: singerPath)
/// let songData = SonixDecoder.decode(path: songPath)
///
/// // 2. Extract pitch contours
/// let extractor = PitchDetection.createContourExtractor()
/// let singerContour = extractor.extract(audio: singerData.samples, sampleRate: singerData.sampleRate)
/// extractor.close()
///
/// // 3. Compute search vectors
/// let singerVec = TesseraRange.computeSearchVector(contour: singerContour, normalize: false)
/// let songVec = TesseraRange.computeSearchVector(contour: songContour, normalize: true)
///
/// // 4. Match
/// let match = TesseraRange.computeMatch(singerVector: singerVec, songVector: songVec, singerGender: .female)
/// // match.similarity (0-1), match.difficulty (1-5)
/// ```
@MainActor
final class SongMatchingViewModel: ObservableObject {

    // MARK: - Published State

    @Published private(set) var isAnalyzing = false
    @Published private(set) var similarity: Float?
    @Published private(set) var difficulty: Int32?
    @Published private(set) var singerVectorDims: Int = 0
    @Published private(set) var songVectorDims: Int = 0
    @Published private(set) var errorMessage: String?

    // MARK: - Computed Properties

    var hasResult: Bool { similarity != nil && difficulty != nil }

    // MARK: - Actions

    func matchSingerToSong() {
        isAnalyzing = true
        similarity = nil
        difficulty = nil
        errorMessage = nil

        Task {
            // 1. Load and decode both audio files
            guard let singerURL = Bundle.main.url(forResource: "Chalan_voice", withExtension: "m4a"),
                  let songURL = Bundle.main.url(forResource: "Alankaar 01", withExtension: "m4a") else {
                await MainActor.run {
                    errorMessage = "Audio asset(s) not found in bundle"
                    isAnalyzing = false
                }
                return
            }

            guard let singerAudio = SonixDecoder.decode(path: singerURL.path),
                  let songAudio = SonixDecoder.decode(path: songURL.path) else {
                await MainActor.run {
                    errorMessage = "Failed to decode audio file(s)"
                    isAnalyzing = false
                }
                return
            }

            // 2. Extract pitch contours
            let singerExtractor = PitchDetection.createContourExtractor()
            let singerContour = singerExtractor.extract(audio: singerAudio.samples, sampleRate: singerAudio.sampleRate)
            singerExtractor.close()

            let songExtractor = PitchDetection.createContourExtractor()
            let songContour = songExtractor.extract(audio: songAudio.samples, sampleRate: songAudio.sampleRate)
            songExtractor.close()

            // 3. Compute search vectors
            // Extension returns native [Float] (converts from KotlinFloatArray)
            let singerVec: [Float] = TesseraRange.computeSearchVector(contour: singerContour, normalize: false)
            let songVec: [Float] = TesseraRange.computeSearchVector(contour: songContour, normalize: true)

            // 4. Compute match (assume female for Chalan)
            let match = TesseraRange.computeMatch(
                singerVector: singerVec,
                songVector: songVec,
                singerGender: .female
            )

            await MainActor.run {
                similarity = match.similarity
                difficulty = match.difficulty
                singerVectorDims = singerVec.count
                songVectorDims = songVec.count
                isAnalyzing = false
            }
        }
    }
}
