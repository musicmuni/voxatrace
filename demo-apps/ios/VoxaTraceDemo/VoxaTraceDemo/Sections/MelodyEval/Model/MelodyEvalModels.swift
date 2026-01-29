import Foundation
import VoxaTrace

// MARK: - UI State

/// Represents the current step in the melody evaluation workflow.
enum MelodyEvalStep: Int, CaseIterable {
    case loadReference = 1
    case record = 2
    case evaluate = 3
    case results = 4

    var title: String {
        switch self {
        case .loadReference: return "Load Reference"
        case .record: return "Record Performance"
        case .evaluate: return "Evaluate"
        case .results: return "Results"
        }
    }
}
