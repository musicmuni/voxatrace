import Foundation
import VoxaTrace

// MARK: - Singafter Repository Protocol

/// Protocol for singafter lesson data access. Enables testing with mock implementations.
protocol SingafterRepositoryProtocol {
    func loadLesson(name: String) async throws -> SingafterLessonData
}

// MARK: - Bundle Singafter Repository

/// Loads singafter lessons from the app bundle.
final class BundleSingafterRepository: SingafterRepositoryProtocol, @unchecked Sendable {
    nonisolated init() {}

    enum LessonError: LocalizedError {
        case transFileNotFound
        case audioFileNotFound
        case decodingFailed
        case invalidTransData
        case noPhrasePairs

        var errorDescription: String? {
            switch self {
            case .transFileNotFound: return "Lesson transcript file not found"
            case .audioFileNotFound: return "Lesson audio file not found"
            case .decodingFailed: return "Failed to decode audio"
            case .invalidTransData: return "Invalid transcript data"
            case .noPhrasePairs: return "No phrase pairs found in lesson"
            }
        }
    }

    func loadLesson(name: String) async throws -> SingafterLessonData {
        // 1. Load and parse trans file (JSON format for singafter)
        guard let transURL = Bundle.main.url(forResource: name, withExtension: "trans"),
              let transData = try? Data(contentsOf: transURL),
              let rawSegments = try? JSONDecoder().decode([SingafterSegment].self, from: transData) else {
            throw LessonError.transFileNotFound
        }

        // 2. Group into phrase pairs (teacher + student)
        let teacherSegments = rawSegments.filter { $0.type == "teacher_vocal" }
        let phrasePairs: [PhrasePair] = teacherSegments.compactMap { teacher in
            guard let student = rawSegments.first(where: { $0.id == teacher.relatedSeg && $0.type == "student_vocal" }) else {
                return nil
            }
            return PhrasePair(
                index: teacher.id,
                lyrics: teacher.lyrics,
                teacherStartTime: teacher.startTime,
                teacherEndTime: teacher.endTime,
                studentStartTime: student.startTime,
                studentEndTime: student.endTime,
                teacherId: teacher.id
            )
        }

        guard !phrasePairs.isEmpty else {
            throw LessonError.noPhrasePairs
        }

        // 3. Load audio
        guard let audioURL = Bundle.main.url(forResource: name, withExtension: "m4a") else {
            throw LessonError.audioFileNotFound
        }

        guard let audioData = SonixDecoder.decode(path: audioURL.path) else {
            throw LessonError.decodingFailed
        }

        // 4. Create segments with student time windows
        let segments: [Segment] = phrasePairs.enumerated().map { (index, pair) in
            .create(
                index: index,
                startSeconds: Float(pair.teacherStartTime),
                endSeconds: Float(pair.studentEndTime),
                lyrics: pair.lyrics,
                studentStartSeconds: Float(pair.studentStartTime),
                studentEndSeconds: Float(pair.studentEndTime)
            )
        }

        // 5. Load pitch contour (optional optimization)
        var pitchContour: PitchContour? = nil
        if let pitchURL = Bundle.main.url(forResource: name, withExtension: "pitchPP"),
           let pitchContent = try? String(contentsOf: pitchURL),
           let pitchData = Parser.parsePitchString(content: pitchContent) {
            pitchContour = PitchContour.fromArrays(
                times: pitchData.times,
                pitches: pitchData.pitchesHz
            )
        }

        // 6. Create LessonMaterial
        let reference = LessonMaterial.fromAudio(
            samples: audioData.samples,
            sampleRate: audioData.sampleRate,
            segments: segments,
            keyHz: 196.0,
            pitchContour: pitchContour
        )

        return SingafterLessonData(
            reference: reference,
            audioPath: audioURL.path,
            phrasePairs: phrasePairs
        )
    }
}
