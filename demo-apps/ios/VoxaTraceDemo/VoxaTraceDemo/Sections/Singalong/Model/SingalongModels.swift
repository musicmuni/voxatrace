import Foundation
import VoxaTrace

// MARK: - Session Preset

/// Available session presets for practice modes.
enum SessionPreset: String, CaseIterable, Identifiable {
    case `default` = "Default"
    case practice = "Practice"
    case karaoke = "Karaoke"
    case performance = "Performance"

    var id: String { rawValue }

    var config: SessionConfig {
        switch self {
        case .default: return .default
        case .practice: return .practice
        case .karaoke: return .karaoke
        case .performance: return .performance
        }
    }

    var description: String {
        switch self {
        case .default: return "Balanced, auto-advancing"
        case .practice: return "70% threshold, 3 attempts, best score"
        case .karaoke: return "Relaxed, always advances"
        case .performance: return "Precise, one attempt"
        }
    }
}

// MARK: - Lesson Data

/// Parsed lesson data ready for CalibraLiveEval.
struct LessonData {
    let reference: LessonMaterial
    let audioPath: String
}

// MARK: - UI State

/// Represents the current state of the Singalong practice UI.
enum SingalongUIState: Equatable {
    case idle
    case loading
    case ready
    case practicing
    case summary
    case error(String)
}
