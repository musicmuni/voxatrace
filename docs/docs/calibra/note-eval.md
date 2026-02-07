---
sidebar_position: 7
---

# CalibraNoteEval

Offline note/exercise evaluation for scales, arpeggios, and svara patterns. Scores how accurately a student performs **individual notes** in a sequence, with per-note feedback and performance level classification.

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
let pattern = ExercisePattern.create(
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

## When to Use

| Scenario | Use This? | Why |
|----------|-----------|-----|
| Evaluate scales/exercises | Yes | Per-note scoring |
| Evaluate complete songs | No | Use `CalibraMelodyEval` |
| Real-time scoring | No | Use `CalibraLiveEval` |
| Just detect pitch | No | Use `CalibraPitch` |

## Configuration

### Presets

| Preset | Kotlin | Swift | Algorithm | Boundary Tolerance | Description |
|--------|--------|-------|-----------|--------------------|-------------|
| Lenient | `NoteEvalPreset.LENIENT` | `.lenient` | Simple | 200ms | Beginner-friendly, forgiving on timing |
| Balanced | `NoteEvalPreset.BALANCED` | `.balanced` | Simple | 100ms | Standard practice scoring |
| Strict | `NoteEvalPreset.STRICT` | `.strict` | Weighted | 0ms | Advanced/performance scoring |

#### Kotlin

```kotlin
// Use a preset (recommended)
val result = CalibraNoteEval.evaluate(
    pattern = pattern,
    student = studentContour,
    referenceKeyHz = 261.63f,
    preset = NoteEvalPreset.LENIENT
)
```

#### Swift

```swift
// Use a preset (recommended - default is .balanced)
let result = CalibraNoteEval.evaluate(
    pattern: pattern,
    student: studentContour,
    referenceKeyHz: 261.63,
    preset: .lenient
)
```

### NoteEvalConfig

For fine-grained control, create a `NoteEvalConfig` directly.

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `algorithm` | `ScoringAlgorithm` | `SIMPLE` | Scoring algorithm (`SIMPLE` or `WEIGHTED`) |
| `boundaryToleranceMs` | `Int` | `0` | Milliseconds to skip at note start/end boundaries |

#### Kotlin

```kotlin
val config = NoteEvalConfig(
    algorithm = ScoringAlgorithm.WEIGHTED,
    boundaryToleranceMs = 150
)

val result = CalibraNoteEval.evaluate(
    pattern = pattern,
    student = studentContour,
    referenceKeyHz = 261.63f,
    config = config
)
```

#### Swift

```swift
let config = NoteEvalConfig(
    algorithm: .weighted,
    boundaryToleranceMs: 150
)

let result = CalibraNoteEval.evaluate(
    pattern: pattern,
    student: studentContour,
    referenceKeyHz: 261.63,
    config: config
)
```

### ScoringAlgorithm

| Algorithm | Description |
|-----------|-------------|
| `SIMPLE` | Counts percentage of pitch samples within 35 cents of target. Good for beginners and practice. |
| `WEIGHTED` | Duration-aware scoring with tighter thresholds. Stricter on longer notes, more forgiving on short notes. Good for advanced evaluation. |

## ExercisePattern

Defines the note sequence for evaluation, including frequencies, durations, and loop structure.

### Constructor

#### Kotlin

```kotlin
val pattern = ExercisePattern(
    noteFrequencies = listOf(261.63f, 293.66f, 329.63f, 349.23f, 392.00f),
    noteDurations = listOf(500, 500, 500, 500, 500),
    notesPerLoop = 5  // defaults to noteFrequencies.size
)
```

#### Swift

```swift
let pattern = ExercisePattern.create(
    noteFrequencies: [261.63, 293.66, 329.63, 349.23, 392.00],
    noteDurations: [500, 500, 500, 500, 500],
    notesPerLoop: 5  // defaults to noteFrequencies.count
)
```

### Constructor Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `noteFrequencies` | `List<Float>` | required | Frequencies in Hz for each note in the pattern |
| `noteDurations` | `List<Int>` | required | Duration in milliseconds for each note |
| `notesPerLoop` | `Int` | `noteFrequencies.size` | Number of notes per loop/cycle (for repeating patterns) |

Validation rules:
- `noteFrequencies` and `noteDurations` must have the same size
- Pattern must have at least one note
- `notesPerLoop` must be between 1 and pattern length

### Properties

| Property | Type | Description |
|----------|------|-------------|
| `totalDurationMs` | `Int` | Total duration of the pattern in milliseconds |
| `noteCount` | `Int` | Number of notes in the pattern |

### Factory Methods

#### `scale` -- Create from Uniform Durations

```kotlin
// Kotlin
val pattern = ExercisePattern.scale(
    frequencies = listOf(261.63f, 293.66f, 329.63f, 349.23f, 392.00f),
    noteDurationMs = 500  // default: 500
)
```

```swift
// Swift
let pattern = ExercisePattern.scale(
    frequencies: [261.63, 293.66, 329.63, 349.23, 392.00],
    noteDurationMs: 500  // default: 500
)
```

#### `fromMidiNotes` -- Create from MIDI Note Numbers

```kotlin
// Kotlin — MIDI 60 = middle C
val pattern = ExercisePattern.fromMidiNotes(
    midiNotes = listOf(60, 62, 64, 65, 67),
    noteDurationMs = 500  // default: 500
)
```

```swift
// Swift
let pattern = ExercisePattern.fromMidiNotes(
    midiNotes: [60, 62, 64, 65, 67],
    noteDurationMs: 500  // default: 500
)
```

## Evaluation

### `evaluate` with Preset

#### Kotlin

```kotlin
fun evaluate(
    pattern: ExercisePattern,
    student: PitchContour,
    referenceKeyHz: Float,
    studentKeyHz: Float = 0f,
    preset: NoteEvalPreset
): ExerciseResult
```

#### Swift

```swift
static func evaluate(
    pattern: ExercisePattern,
    student: PitchContour,
    referenceKeyHz: Float,
    studentKeyHz: Float = 0,
    preset: NoteEvalPreset = .balanced
) -> ExerciseResult
```

### `evaluate` with Config

#### Kotlin

```kotlin
fun evaluate(
    pattern: ExercisePattern,
    student: PitchContour,
    referenceKeyHz: Float,
    studentKeyHz: Float = 0f,
    config: NoteEvalConfig = NoteEvalConfig.DEFAULT
): ExerciseResult
```

#### Swift

```swift
static func evaluate(
    pattern: ExercisePattern,
    student: PitchContour,
    referenceKeyHz: Float,
    studentKeyHz: Float = 0,
    config: NoteEvalConfig
) -> ExerciseResult
```

### `evaluate` with Raw Parameters

#### Kotlin

```kotlin
fun evaluate(
    pattern: ExercisePattern,
    student: PitchContour,
    referenceKeyHz: Float,
    studentKeyHz: Float = 0f,
    scoreType: Int = 0,
    leewaySamples: Int = 0
): ExerciseResult
```

#### Swift

```swift
static func evaluate(
    pattern: ExercisePattern,
    student: PitchContour,
    referenceKeyHz: Float,
    studentKeyHz: Float = 0,
    scoreType: Int32 = 0,
    leewaySamples: Int32 = 0
) -> ExerciseResult
```

### Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `pattern` | `ExercisePattern` | required | Exercise pattern with note frequencies and durations |
| `student` | `PitchContour` | required | Pitch contour from the student's performance |
| `referenceKeyHz` | `Float` | required | Reference key/tonic frequency in Hz |
| `studentKeyHz` | `Float` | `0` | Student's key frequency in Hz (0 = same as reference) |
| `preset` | `NoteEvalPreset` | `.balanced` (Swift) | Evaluation preset |
| `config` | `NoteEvalConfig` | `NoteEvalConfig.DEFAULT` | Evaluation configuration |
| `scoreType` | `Int` | `0` | Scoring algorithm type (0 = simple, 1 = weighted) |
| `leewaySamples` | `Int` | `0` | Tolerance at note boundaries in pitch samples |

### Key Transposition

When a student sings in a different key than the reference, set `studentKeyHz` so the evaluator adjusts interval expectations accordingly.

#### Kotlin

```kotlin
val result = CalibraNoteEval.evaluate(
    pattern = pattern,
    student = studentContour,
    referenceKeyHz = 261.63f,  // Reference in C4
    studentKeyHz = 277.18f,    // Student sings in C#4
    preset = NoteEvalPreset.BALANCED
)
```

#### Swift

```swift
let result = CalibraNoteEval.evaluate(
    pattern: pattern,
    student: studentContour,
    referenceKeyHz: 261.63,  // Reference in C4
    studentKeyHz: 277.18,    // Student sings in C#4
    preset: .balanced
)
```

## Result Types

### ExerciseResult

Overall result of an exercise evaluation.

| Property | Type | Description |
|----------|------|-------------|
| `score` | `Float` | Overall score (0.0 -- 1.0) |
| `scorePercent` | `Int` | Score as a percentage (0 -- 100) |
| `noteResults` | `List<NoteResult>` | Per-note evaluation results |
| `keyHz` | `Float` | Key frequency used for evaluation |
| `noteCount` | `Int` | Number of notes evaluated |
| `passingNotes` | `Int` | Number of notes with score >= 0.5 |
| `passingRatio` | `Float` | Ratio of passing notes to total notes |

`ExerciseResult.EMPTY` provides an empty result constant (score 0, no notes, key 261.63 Hz).

### NoteResult

Result for a single note in the exercise.

| Property | Type | Description |
|----------|------|-------------|
| `noteIndex` | `Int` | Index of the note in the pattern |
| `expectedFrequencyHz` | `Float` | Expected frequency in Hz |
| `score` | `Float` | Score for this note (0.0 -- 1.0) |
| `scorePercent` | `Int` | Score as a percentage (0 -- 100) |
| `level` | `PerformanceLevel` | Performance level classification |
| `isPassing` | `Boolean` | Whether the note is passing (score >= 0.5) |

### PerformanceLevel

Score-based classification for each note result.

| Level | Score Range | Display Name |
|-------|-------------|--------------|
| `NEEDS_WORK` | < 0.3 | "Needs Work" |
| `FAIR` | 0.3 -- 0.6 | "Fair" |
| `GOOD` | 0.6 -- 0.8 | "Good" |
| `VERY_GOOD` | 0.8 -- 0.95 | "Very Good" |
| `EXCELLENT` | >= 0.95 | "Excellent" |
| `NOT_EVALUATED` | N/A | "Not Evaluated" |
| `NOT_DETECTED` | < 0 | "No Voice" |

| Property / Method | Type | Description |
|-------------------|------|-------------|
| `displayName` | `String` | Human-readable label for UI display |
| `fromScore(score)` | `PerformanceLevel` | Classify a score (0.0 -- 1.0) into a level |
| `fromCode(code)` | `PerformanceLevel` | Convert from integer code (for native interop) |

## Common Patterns

### Scale Practice with Per-Note Feedback

```kotlin
// Kotlin
val cMajorScale = ExercisePattern.scale(
    frequencies = listOf(261.63f, 293.66f, 329.63f, 349.23f, 392.00f, 440.00f, 493.88f, 523.25f),
    noteDurationMs = 600
)

val result = CalibraNoteEval.evaluate(
    pattern = cMajorScale,
    student = studentContour,
    referenceKeyHz = 261.63f,
    preset = NoteEvalPreset.BALANCED
)

println("Overall: ${result.scorePercent}% (${result.passingNotes}/${result.noteCount} passing)")
result.noteResults.forEach { note ->
    val status = if (note.isPassing) "PASS" else "FAIL"
    println("  Note ${note.noteIndex}: ${note.scorePercent}% [${note.level.displayName}] $status")
}
```

```swift
// Swift
let cMajorScale = ExercisePattern.scale(
    frequencies: [261.63, 293.66, 329.63, 349.23, 392.00, 440.00, 493.88, 523.25],
    noteDurationMs: 600
)

let result = CalibraNoteEval.evaluate(
    pattern: cMajorScale,
    student: studentContour,
    referenceKeyHz: 261.63,
    preset: .balanced
)

print("Overall: \(result.scorePercent)% (\(result.passingNotes)/\(result.noteCount) passing)")
for note in result.noteResults {
    let status = note.isPassing ? "PASS" : "FAIL"
    print("  Note \(note.noteIndex): \(note.scorePercent)% [\(note.level.displayName)] \(status)")
}
```

### MIDI-Based Arpeggio Exercise

```kotlin
// Kotlin — C major arpeggio: C4, E4, G4, C5
val arpeggio = ExercisePattern.fromMidiNotes(
    midiNotes = listOf(60, 64, 67, 72),
    noteDurationMs = 700
)

val result = CalibraNoteEval.evaluate(
    pattern = arpeggio,
    student = studentContour,
    referenceKeyHz = 261.63f,
    preset = NoteEvalPreset.STRICT
)
```

```swift
// Swift
let arpeggio = ExercisePattern.fromMidiNotes(
    midiNotes: [60, 64, 67, 72],
    noteDurationMs: 700
)

let result = CalibraNoteEval.evaluate(
    pattern: arpeggio,
    student: studentContour,
    referenceKeyHz: 261.63,
    preset: .strict
)
```

### Beginner-Friendly Evaluation

```kotlin
// Kotlin — lenient preset with key transposition
val result = CalibraNoteEval.evaluate(
    pattern = pattern,
    student = studentContour,
    referenceKeyHz = 261.63f,
    studentKeyHz = 246.94f,    // Student sings in B3
    preset = NoteEvalPreset.LENIENT
)

// Show only encouragement for beginners
if (result.scorePercent >= 70) {
    println("Great job!")
} else {
    val weakNotes = result.noteResults.filter { !it.isPassing }
    println("Practice notes: ${weakNotes.map { it.noteIndex }}")
}
```

```swift
// Swift
let result = CalibraNoteEval.evaluate(
    pattern: pattern,
    student: studentContour,
    referenceKeyHz: 261.63,
    studentKeyHz: 246.94,
    preset: .lenient
)

if result.scorePercent >= 70 {
    print("Great job!")
} else {
    let weakNotes = result.noteResults.filter { !$0.isPassing }
    print("Practice notes: \(weakNotes.map { $0.noteIndex })")
}
```

## Platform Notes

### iOS
- Audio must be 16kHz mono; use `SonixDecoder`/`SonixResampler` to convert
- Swift extensions provide idiomatic static methods on `CalibraNoteEval` (no `.companion`, no `.shared`)
- Presets use lowercase Swift enum style (`.lenient`, `.balanced`, `.strict`)

### Android
- Audio must be 16kHz mono; use `SonixDecoder`/`SonixResampler` to convert
- Works with any `PitchContour` from `CalibraPitch`

## Common Pitfalls

1. **Wrong audio sample rate** -- Audio must be 16kHz. Use `SonixResampler` if your source is 44.1kHz or 48kHz.
2. **Mismatched array sizes** -- `noteFrequencies` and `noteDurations` must have the same length.
3. **Forgetting key transposition** -- Set `studentKeyHz` if the student sings in a different key than the reference.
4. **Using raw parameters when presets suffice** -- Prefer `NoteEvalPreset` or `NoteEvalConfig` over raw `scoreType`/`leewaySamples` for readability.

## Next Steps

- [CalibraPitch](./pitch) -- Pitch detection for extracting `PitchContour`
- [Calibra Overview](./overview) -- Full overview of the Calibra analysis module
