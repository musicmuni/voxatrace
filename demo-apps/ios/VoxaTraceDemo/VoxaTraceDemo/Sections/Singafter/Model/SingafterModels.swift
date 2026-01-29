import Foundation
import VoxaTrace

// MARK: - Singafter Segment

/// Model for singafter segment with type and related_seg from trans file.
struct SingafterSegment: Decodable {
    let id: Int
    let type: String
    let lyrics: String
    let relatedSeg: Int
    let timeStamp: [Double]

    enum CodingKeys: String, CodingKey {
        case id, type, lyrics
        case relatedSeg = "related_seg"
        case timeStamp = "time_stamp"
    }

    var startTime: Double { timeStamp[0] }
    var endTime: Double { timeStamp[1] }
}

// MARK: - Phrase Pair

/// Phrase pair for singafter practice (teacher + student segments).
struct PhrasePair {
    let index: Int
    let lyrics: String
    let teacherStartTime: Double
    let teacherEndTime: Double
    let studentStartTime: Double
    let studentEndTime: Double
    let teacherId: Int
}

// MARK: - Lesson Data

/// Parsed singafter lesson data ready for CalibraLiveEval.
struct SingafterLessonData {
    let reference: LessonMaterial
    let audioPath: String
    let phrasePairs: [PhrasePair]
}

// MARK: - UI State

/// Represents the current state of the Singafter practice UI.
enum SingafterUIState: Equatable {
    case idle
    case loading
    case ready
    case practicing
    case summary
    case error(String)
}

// MARK: - Practice Phase UI State

/// Extended practice phase for UI display.
extension PracticePhase {
    var displayName: String {
        switch self {
        case .idle: return "Ready"
        case .listening: return "Listen"
        case .singing: return "Sing"
        case .evaluated: return "Score"
        default: return "Unknown"
        }
    }
}
