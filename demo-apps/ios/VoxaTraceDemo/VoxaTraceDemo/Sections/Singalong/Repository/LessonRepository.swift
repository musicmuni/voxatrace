import Foundation
import VoxaTrace

// MARK: - Lesson Repository Protocol

/// Protocol for lesson data access. Enables testing with mock implementations.
protocol LessonRepositoryProtocol {
    func loadLesson(name: String) async throws -> LessonData
}

// MARK: - Bundle Lesson Repository

/// Loads lessons from the app bundle.
final class BundleLessonRepository: LessonRepositoryProtocol, @unchecked Sendable {
    nonisolated init() {}

    enum LessonError: LocalizedError {
        case transFileNotFound
        case audioFileNotFound
        case decodingFailed
        case invalidTransData

        var errorDescription: String? {
            switch self {
            case .transFileNotFound: return "Lesson transcript file not found"
            case .audioFileNotFound: return "Lesson audio file not found"
            case .decodingFailed: return "Failed to decode audio"
            case .invalidTransData: return "Invalid transcript data"
            }
        }
    }

    func loadLesson(name: String) async throws -> LessonData {
        // 1. Load transcript
        guard let transURL = Bundle.main.url(forResource: name, withExtension: "trans"),
              let transContent = try? String(contentsOf: transURL),
              let transData = Parser.parseTransString(content: transContent) else {
            throw LessonError.transFileNotFound
        }

        // 2. Load audio
        guard let audioURL = Bundle.main.url(forResource: name, withExtension: "m4a") else {
            throw LessonError.audioFileNotFound
        }

        guard let audioData = SonixDecoder.decode(path: audioURL.path) else {
            throw LessonError.decodingFailed
        }

        // 3. Parse segments
        let segments: [Segment] = transData.segments.enumerated().map { index, seg in
            .create(
                index: index,
                startSeconds: seg.startTime,
                endSeconds: seg.endTime,
                lyrics: seg.lyrics
            )
        }

        // 4. Load pitch contour (optional optimization)
        var pitchContour: PitchContour? = nil
        if let pitchURL = Bundle.main.url(forResource: name, withExtension: "pitchPP"),
           let pitchContent = try? String(contentsOf: pitchURL),
           let pitchData = Parser.parsePitchString(content: pitchContent) {
            pitchContour = PitchContour.fromArrays(
                times: pitchData.times,
                pitches: pitchData.pitchesHz
            )
        }

        // 5. Create LessonMaterial
        let reference = LessonMaterial.fromAudio(
            samples: audioData.samples,
            sampleRate: audioData.sampleRate,
            segments: segments,
            keyHz: 196.0,
            pitchContour: pitchContour
        )

        return LessonData(reference: reference, audioPath: audioURL.path)
    }
}
