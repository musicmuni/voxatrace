---
sidebar_label: "CalibraNoteEval"
---


# CalibraNoteEval

object [CalibraNoteEval](index.md)

Offline note/exercise evaluation for scales, arpeggios, and svara patterns.

## What is Note Evaluation?

Note evaluation scores how accurately a student performs **individual notes** in a sequence. Unlike melody evaluation (which compares complete phrases), note evaluation scores each note separately.

Use it for:

- 
   **Scale practice**: Sa Re Ga Ma Pa... with per-note feedback
- 
   **Arpeggio exercises**: Broken chord practice
- 
   **Interval training**: Jumping between specific notes
- 
   **Svara patterns**: Indian classical music exercises

## When to Use

| Scenario | Use This? | Why |
|---|---|---|
| Evaluate scales/exercises | Yes | Per-note scoring |
| Evaluate complete songs | No | Use `CalibraMelodyEval` |
| Real-time scoring | No | Use `CalibraLiveEval` |
| Just detect pitch | No | Use `CalibraPitch` |

## Quick Start

### Kotlin

```kotlin
val pattern = ExercisePattern(
    noteFrequencies = listOf(261.63f, 293.66f, 329.63f),  // C4, D4, E4
    noteDurations = listOf(500, 500, 500),  // 500ms each
    notesPerLoop = 3
)

val studentContour = pitchExtractor.extract(studentAudio, 16000)
val result = CalibraNoteEval.evaluate(pattern, studentContour, referenceKeyHz = 261.63f)

println("Score: ${result.scorePercent}%")
result.noteResults.forEach { note ->
    println("Note ${note.noteIndex}: ${note.scorePercent}%")
}
```

### Swift

```swift
let pattern = ExercisePattern(
    noteFrequencies: [261.63, 293.66, 329.63],  // C4, D4, E4
    noteDurations: [500, 500, 500],
    notesPerLoop: 3
)

let studentContour = pitchExtractor.extract(audio: studentAudio, sampleRate: 16000)
let result = CalibraNoteEval.evaluate(
    pattern: pattern,
    student: studentContour,
    referenceKeyHz: 261.63
)

print("Score: \(result.scorePercent)%")
for note in result.noteResults {
    print("Note \(note.noteIndex): \(note.scorePercent)%")
}
```

## Key Transposition

When students sing in a different key than the reference:

### Kotlin

```kotlin
val result = CalibraNoteEval.evaluate(
    pattern = pattern,
    student = studentContour,
    referenceKeyHz = 261.63f,  // Reference in C
    studentKeyHz = 277.18f,    // Student sings in C#
    leewaySamples = 100        // Allow timing flexibility
)
```

## Platform Notes

### iOS

- 
   Audio must be 16kHz mono; use SonixDecoder/SonixResampler to convert
- 
   Use `@ShouldRefineInSwift` methods with provided Swift extensions

### Android

- 
   Audio must be 16kHz mono; use SonixDecoder/SonixResampler to convert
- 
   Works with any pitch contour from CalibraPitch

## Common Pitfalls

1. 
   **Wrong audio sample rate**: Audio must be 16kHz; use SonixResampler if needed
2. 
   **Mismatched note count**: Pattern and student must have same number of notes
3. 
   **Forgetting key transposition**: Set studentKeyHz if student sings in different key

#### See also

| | |
|---|---|
| [CalibraMelodyEval](../-calibra-melody-eval/index.md) | For complete melody/song evaluation |
| [CalibraLiveEval](../-calibra-live-eval/index.md) | For real-time evaluation during singing |
| [ExercisePattern](../-exercise-pattern/index.md) | For defining note patterns |
| [ExerciseResult](../-exercise-result/index.md) | For understanding evaluation results |

## Functions

| Name | Summary |
|---|---|
| [evaluate](evaluate.md) | [common]<br/>fun [evaluate](evaluate.md)(pattern: [ExercisePattern](../-exercise-pattern/index.md), student: [PitchContour](../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md), referenceKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), studentKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, config: [NoteEvalConfig](../../com.musicmuni.voxatrace.calibra.model/-note-eval-config/index.md) = NoteEvalConfig.DEFAULT): [ExerciseResult](../-exercise-result/index.md)<br/>Evaluate with configuration object.<br/>[common]<br/>fun [evaluate](evaluate.md)(pattern: [ExercisePattern](../-exercise-pattern/index.md), student: [PitchContour](../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md), referenceKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), studentKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, preset: [NoteEvalPreset](../../com.musicmuni.voxatrace.calibra.model/-note-eval-preset/index.md)): [ExerciseResult](../-exercise-result/index.md)<br/>Evaluate with preset.<br/>[common]<br/>fun [evaluate](evaluate.md)(pattern: [ExercisePattern](../-exercise-pattern/index.md), student: [PitchContour](../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md), referenceKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), studentKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, scoreType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0, leewaySamples: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0): [ExerciseResult](../-exercise-result/index.md)<br/>Evaluate a note/exercise performance. |
