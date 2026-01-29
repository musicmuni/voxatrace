import Foundation
import VoxaTrace

// MARK: - Exercise Definition

/// Definition for a note/exercise evaluation exercise.
struct ExerciseDefinition {
    let name: String
    let midiNotes: [Int32]
    let keyMidi: Int32
}

extension ExerciseDefinition {
    static let all: [ExerciseDefinition] = [
        ExerciseDefinition(name: "C Major Scale (ascending)", midiNotes: [48, 50, 52, 53, 55, 57, 59, 60], keyMidi: 48),
        ExerciseDefinition(name: "C Major Scale (descending)", midiNotes: [60, 59, 57, 55, 53, 52, 50, 48], keyMidi: 48),
        ExerciseDefinition(name: "C Minor Scale", midiNotes: [48, 50, 51, 53, 55, 56, 58, 60], keyMidi: 48),
        ExerciseDefinition(name: "C Major Arpeggio", midiNotes: [48, 52, 55, 60], keyMidi: 48),
        ExerciseDefinition(name: "G Major Scale", midiNotes: [55, 57, 59, 60, 62, 64, 66, 67], keyMidi: 55),
        ExerciseDefinition(name: "Sa Re Ga (C)", midiNotes: [48, 50, 52], keyMidi: 48),
        ExerciseDefinition(name: "Sa Re Ga Ma Pa (G)", midiNotes: [55, 57, 59, 60, 62], keyMidi: 55)
    ]
}

// MARK: - UI State

/// Represents the current step in the note evaluation workflow.
enum NoteEvalStep: Int, CaseIterable {
    case selectExercise = 1
    case record = 2
    case evaluate = 3
    case results = 4

    var title: String {
        switch self {
        case .selectExercise: return "Select Exercise"
        case .record: return "Record Performance"
        case .evaluate: return "Evaluate"
        case .results: return "Results"
        }
    }
}
