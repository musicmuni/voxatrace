---
sidebar_position: 12
---

# Utilities & Shared Types

Shared model types, error types, and time utilities used across Calibra APIs.

## LessonMaterial

Reference material for singing evaluation. Contains the reference audio, segment boundaries, and musical key.

### Creating LessonMaterial

#### From File

```kotlin
val material = LessonMaterial.fromFile(
    audioPath = "/path/to/reference.mp3",
    segments = segments,
    keyHz = 261.63f  // Middle C
)
```

```swift
let material = LessonMaterial.fromFile(
    url: URL(fileURLWithPath: "/path/to/reference.mp3"),
    segments: segments,
    keyHz: 261.63
)
```

#### From Audio Samples

```kotlin
val material = LessonMaterial.fromAudio(
    samples = audioSamples,
    sampleRate = 16000,
    segments = segments,
    keyHz = 196.0f,
    pitchContour = precomputedContour,  // Optional: enables fast path
    hpcpFrames = precomputedHpcp        // Optional: for DTW alignment
)
```

```swift
let material = LessonMaterial.fromAudio(
    samples: audioSamples,
    sampleRate: 16000,
    segments: segments,
    keyHz: 196.0,
    pitchContour: precomputedContour,
    hpcpFrames: precomputedHpcp
)
```

### Properties

| Property | Type | Description |
|----------|------|-------------|
| `audioSource` | `AudioSource` | Source of the reference audio |
| `segments` | `List<Segment>` | List of segments with timing and lyrics |
| `keyHz` | `Float` | Musical key frequency in Hz (e.g., 261.63 for middle C) |
| `pitchContour` | `PitchContour?` | Pre-computed pitch contour (enables fast path, skipping YIN extraction) |
| `hpcpFrames` | `List<FloatArray>?` | Pre-computed HPCP frames for DTW alignment (each frame is 12 floats) |
| `duration` | `Float` | Total duration based on the last segment's end time |
| `segmentCount` | `Int` | Number of segments |

## AudioSource

Represents the source of audio data for evaluation. A sealed class with three variants.

| Variant | Properties | Description |
|---------|-----------|-------------|
| `AudioSource.File` | `path: String` | Audio from a local file path |
| `AudioSource.Url` | `url: String` | Audio from a URL (for future streaming support) |
| `AudioSource.Samples` | `samples: FloatArray`, `sampleRate: Int` | Raw audio samples already in memory (default 16000 Hz) |

## Segment

A segment of a song or exercise with timing and optional lyrics. Supports both singalong (student sings with reference) and singafter (student sings after reference) modes.

### Creating Segments

#### Kotlin

```kotlin
// Single segment
val segment = Segment(
    index = 0,
    startSeconds = 0.0f,
    endSeconds = 5.0f,
    lyrics = "Sa Re Ga Ma"
)

// Singafter segment (student sings after reference)
val segment = Segment(
    index = 0,
    startSeconds = 0.0f,
    endSeconds = 5.0f,
    lyrics = "Sa Re Ga Ma",
    studentStartSeconds = 2.5f,
    studentEndSeconds = 5.0f
)

// From parallel arrays
val segments = Segment.fromArrays(
    starts = floatArrayOf(0f, 5f, 10f),
    ends = floatArrayOf(5f, 10f, 15f),
    lyrics = listOf("Line 1", "Line 2", "Line 3")
)
```

#### Swift

```swift
// Single segment
let segment = Segment.create(
    index: 0,
    startSeconds: 0.0,
    endSeconds: 5.0,
    lyrics: "Sa Re Ga Ma"
)

// Singafter segment
let segment = Segment.create(
    index: 0,
    startSeconds: 0.0,
    endSeconds: 5.0,
    lyrics: "Sa Re Ga Ma",
    studentStartSeconds: 2.5,
    studentEndSeconds: 5.0
)
```

### Properties

| Property | Type | Kotlin | Swift | Description |
|----------|------|--------|-------|-------------|
| `index` | `Int` | `Int` | `Int` (via extension) | Zero-based index of the segment |
| `startSeconds` | `Float` | `Float` | `Double` (via extension) | Reference audio start time in seconds |
| `endSeconds` | `Float` | `Float` | `Double` (via extension) | Reference audio end time in seconds |
| `lyrics` | `String` | `String` | `String` | Text/lyrics for this segment |
| `studentStartSeconds` | `Float?` | `Float?` | `Float?` | When student recording starts (null = same as startSeconds) |
| `studentEndSeconds` | `Float?` | `Float?` | `Float?` | When student recording ends (null = same as endSeconds) |
| `duration` | `Float` | `Float` | `Double` (via extension) | Duration of the segment in seconds |
| `isSingafter` | `Boolean` | `Boolean` | `Bool` | True if student starts after reference |
| `effectiveStudentStart` | `Float` | `Float` | `Double` (via extension) | Effective student start time (falls back to segment start) |
| `effectiveStudentEnd` | `Float` | `Float` | `Double` (via extension) | Effective student end time (falls back to segment end) |
| `studentDuration` | `Float` | `Float` | `Double` (via extension) | Duration of the student recording portion |

### Factory Methods

| Method | Description |
|--------|-------------|
| `Segment.fromArrays(starts, ends, lyrics, studentStarts, studentEnds)` | Create segments from parallel arrays of start and end times |

## SegmentResult

Result of evaluating a single segment.

### Properties

| Property | Type | Description |
|----------|------|-------------|
| `segment` | `Segment` | The segment that was evaluated |
| `score` | `Float` | Overall score for this segment (0.0 - 1.0) |
| `pitchAccuracy` | `Float` | Pitch accuracy component of the score (0.0 - 1.0) |
| `level` | `PerformanceLevel` | Performance level classification |
| `attemptNumber` | `Int` | Which attempt this is (1-based, for retry tracking) |
| `referencePitch` | `PitchContour` | Reference pitch contour for visualization |
| `studentPitch` | `PitchContour` | Student pitch contour for visualization |
| `isPassing` | `Boolean` | True if score >= 0.5 |
| `isGood` | `Boolean` | True if score >= 0.7 |
| `isExcellent` | `Boolean` | True if score >= 0.9 |
| `scorePercent` | `Int` | Score as a percentage (0-100) |
| `feedbackMessage` | `String` | Human-readable feedback based on performance level |

### Swift Pitch Data Extensions

In Swift, pitch contour data is accessed via tuple extensions:

```swift
let result: SegmentResult = ...

// Reference pitch data
let ref = result.referencePitchData
// ref.times: [Float], ref.pitchesHz: [Float], ref.pitchesMidi: [Float]

// Student pitch data
let student = result.studentPitchData
// student.times: [Float], student.pitchesHz: [Float], student.pitchesMidi: [Float]
```

### Factory Method

```kotlin
val result = SegmentResult.create(
    segment = segment,
    score = 0.85f,
    pitchAccuracy = 0.82f,
    attemptNumber = 2,
    referencePitch = refContour,
    studentPitch = studentContour
)
```

## SingingResult

Complete result of a singing evaluation session, aggregating results across all segments.

### Properties

| Property | Type | Description |
|----------|------|-------------|
| `overallScore` | `Float` | Aggregate score across all segments (0.0 - 1.0) |
| `segmentResults` | `Map<Int, List<SegmentResult>>` | Map of segment index to list of attempts |
| `aggregation` | `ResultAggregation` | How the overall score was calculated |
| `overallScorePercent` | `Int` | Overall score as a percentage (0-100) |
| `segmentCount` | `Int` | Number of segments evaluated |
| `totalAttempts` | `Int` | Number of total attempts across all segments |
| `allPassing` | `Boolean` | True if all segments pass (score >= 0.5) |

### Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| `latestScorePerSegment()` | `Map<Int, Float>` | Get the latest score for each segment |
| `bestScorePerSegment()` | `Map<Int, Float>` | Get the best score for each segment |
| `averageScorePerSegment()` | `Map<Int, Float>` | Get the average score for each segment |
| `latestResultPerSegment()` | `Map<Int, SegmentResult>` | Get the latest result for each segment |
| `getAllFeedback()` | `List<String>` | Get feedback messages for all segments |

### Swift Convenience Methods

```swift
let result: SingingResult = ...

// Latest score for a specific segment
if let score = result.latestScore(forSegment: 0) {
    print("Score: \(Int(score * 100))%")
}

// Best score for a specific segment
if let best = result.bestScore(forSegment: 0) {
    print("Best: \(Int(best * 100))%")
}
```

### Static Members

| Member | Description |
|--------|-------------|
| `SingingResult.EMPTY` | Empty result constant (score 0, no segments) |
| `SingingResult.calculateOverallScore(segmentResults, aggregation)` | Calculate overall score from segment results |

## ResultAggregation

How to aggregate multiple attempts per segment into a final score.

| Value | Description |
|-------|-------------|
| `LATEST` | Use the most recent attempt's score |
| `BEST` | Use the highest score across all attempts |
| `AVERAGE` | Use the average of all attempts |

## PerformanceLevel

Score-based classification for singing evaluation results.

### Values

| Value | Score Range | Display Name | Description |
|-------|-------------|-------------|-------------|
| `NEEDS_WORK` | < 0.3 | "Needs Work" | Significant improvement needed |
| `FAIR` | 0.3 - 0.6 | "Fair" | Room for improvement |
| `GOOD` | 0.6 - 0.8 | "Good" | Solid performance |
| `VERY_GOOD` | 0.8 - 0.95 | "Very Good" | Very strong performance |
| `EXCELLENT` | >= 0.95 | "Excellent" | Outstanding performance |
| `NOT_EVALUATED` | N/A | "Not Evaluated" | Could not evaluate (insufficient data) |
| `NOT_DETECTED` | < 0 | "No Voice" | No voice detected during segment |

### Properties

| Property | Type | Description |
|----------|------|-------------|
| `displayName` | `String` | Human-readable display name for UI |

### Factory Methods

| Method | Description |
|--------|-------------|
| `PerformanceLevel.fromScore(score)` | Get level based on score (0.0-1.0, negative for NOT_DETECTED) |
| `PerformanceLevel.fromCode(code)` | Convert from integer code (for JNI/C interop) |

#### Kotlin

```kotlin
val level = PerformanceLevel.fromScore(0.85f)
// level == PerformanceLevel.VERY_GOOD
println(level.displayName)  // "Very Good"
```

#### Swift

```swift
let level = PerformanceLevel.fromScore(0.85)
// level == .veryGood
print(level.displayName)  // "Very Good"
```

## PracticePhase

Practice phase during a CalibraLiveEval session.

### Phase Progressions

**Singalong:** `IDLE -> SINGING -> EVALUATED`
- Student sings with the reference audio simultaneously

**Singafter:** `IDLE -> LISTENING -> SINGING -> EVALUATED`
- Student listens to reference first, then sings during their turn

| Value | Description |
|-------|-------------|
| `IDLE` | Not practicing - waiting to start |
| `LISTENING` | Reference playing, student not recording yet (singafter only) |
| `SINGING` | Student is being recorded and evaluated |
| `EVALUATED` | Segment complete, score available |

## SessionPhase

Current phase of a CalibraLiveEval session.

| Value | Description |
|-------|-------------|
| `IDLE` | Session created but not started |
| `READY` | Reference loaded, ready to begin practicing |
| `PRACTICING` | Actively capturing and evaluating audio for a segment |
| `BETWEEN_SEGMENTS` | Finished one segment, waiting before next |
| `COMPLETED` | All segments completed or session manually finished |
| `CANCELLED` | Session was cancelled |
| `ERROR` | An error occurred |

## SessionState

Current state of a CalibraLiveEval session. Exposed as a `StateFlow` from `CalibraLiveEval`.

### Properties

| Property | Type | Description |
|----------|------|-------------|
| `phase` | `SessionPhase` | Current phase of the session |
| `activeSegmentIndex` | `Int?` | Index of segment being practiced, or null if none |
| `activeSegment` | `Segment?` | The segment being practiced, or null if none |
| `currentPitch` | `Float` | Current detected pitch in Hz (-1 for unvoiced) |
| `currentAmplitude` | `Float` | Current audio amplitude (0.0 - 1.0) |
| `segmentProgress` | `Float` | Progress through current segment (0.0 - 1.0) |
| `completedSegments` | `Set<Int>` | Set of segment indices that have been completed |
| `error` | `String?` | Error message if phase is ERROR, null otherwise |
| `isPracticing` | `Boolean` | True if session is actively practicing |
| `canBeginSegment` | `Boolean` | True if session is ready to start or between segments |
| `isFinished` | `Boolean` | True if session is finished (completed, cancelled, or error) |
| `completedCount` | `Int` | Number of completed segments |

### Static Members

| Member | Kotlin | Swift | Description |
|--------|--------|-------|-------------|
| Idle | `SessionState.IDLE` | `.idle` | Initial idle state |

## ActiveSegmentState

State of the currently active segment during practice.

### Properties

| Property | Type | Description |
|----------|------|-------------|
| `segmentIndex` | `Int` | Index of the segment |
| `segment` | `Segment` | The segment being practiced |
| `currentPitch` | `Float` | Current detected pitch in Hz (-1 for unvoiced) |
| `currentAmplitude` | `Float` | Current audio amplitude (0.0 - 1.0) |
| `elapsedSeconds` | `Float` | Time elapsed since segment started |
| `isCapturing` | `Boolean` | Whether audio is currently being captured |
| `progress` | `Float` | Progress through the segment (0.0 - 1.0) |
| `remainingSeconds` | `Float` | Time remaining in seconds |
| `hasVoice` | `Boolean` | True if detected pitch is valid |

## SessionConfig

Configuration for a CalibraLiveEval session.

### Presets

| Preset | Kotlin | Swift | Description |
|--------|--------|-------|-------------|
| Default | `SessionConfig.DEFAULT` | `.default` | Balanced, auto-advancing |
| Practice | `SessionConfig.PRACTICE` | `.practice` | Repeats until 70% or 3 attempts, best score |
| Karaoke | `SessionConfig.KARAOKE` | `.karaoke` | Always advances, one attempt |
| Performance | `SessionConfig.PERFORMANCE` | `.performance` | Strict, one attempt, no repetition |

### Builder

#### Kotlin

```kotlin
val config = SessionConfig.Builder()
    .preset(SessionConfig.PRACTICE)
    .scoreThreshold(0.6f)
    .maxAttempts(5)
    .resultAggregation(ResultAggregation.BEST)
    .build()
```

#### Swift

```swift
let config = SessionConfig.Builder()
    .preset(.practice)
    .scoreThreshold(0.6)
    .maxAttempts(5)
    .resultAggregation(.best)
    .build()
```

### Config Properties

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `autoAdvance` | `Boolean` | `true` | Automatically advance to next segment |
| `scoreThreshold` | `Float` | `0` | Min score to auto-advance (0 = disabled) |
| `maxAttempts` | `Int` | `0` | Max attempts before forced advance (0 = unlimited) |
| `resultAggregation` | `ResultAggregation` | `LATEST` | How to aggregate multiple attempts |
| `hopSize` | `Int` | `320` | Hop size between frames in samples (320 = 20 ms at 16 kHz, 2 frames per buffer per ADR-020) |
| `autoPhaseTransition` | `Boolean` | `true` | Auto transition LISTENING to SINGING in singafter mode |
| `autoSegmentDetection` | `Boolean` | `true` | Auto detect segment end from player time |

### Builder Methods

| Method | Description |
|--------|-------------|
| `preset(config)` | Start from a preset configuration |
| `autoAdvance(enabled)` | Enable or disable auto-advance |
| `scoreThreshold(threshold)` | Set minimum score threshold (0 = disabled) |
| `maxAttempts(max)` | Set maximum attempts (0 = unlimited) |
| `resultAggregation(agg)` | Set how to aggregate multiple attempts |
| `hopSize(samples)` | Set hop size between frames |
| `autoPhaseTransition(enabled)` | Enable or disable auto phase transition |
| `autoSegmentDetection(enabled)` | Enable or disable auto segment end detection |

## ScoringAlgorithm

Algorithm for computing note accuracy scores.

| Value | Description |
|-------|-------------|
| `SIMPLE` | Simple threshold counting. Counts percentage of pitch samples within 35 cents of target. Good for beginners. |
| `WEIGHTED` | Weighted duration-aware scoring. Tighter thresholds, considers note duration. Good for advanced evaluation. |

## NoteEvalConfig

Configuration for note evaluation scoring.

### Properties

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `algorithm` | `ScoringAlgorithm` | `SIMPLE` | Algorithm for computing scores |
| `boundaryToleranceMs` | `Int` | `0` | Milliseconds to skip at note start/end |

### Presets

| Preset | Kotlin | Swift | Algorithm | Boundary Tolerance | Description |
|--------|--------|-------|-----------|--------------------|-------------|
| Lenient | `NoteEvalPreset.LENIENT` | `.lenient` | SIMPLE | 200ms | Beginner-friendly |
| Balanced | `NoteEvalPreset.BALANCED` | `.balanced` | SIMPLE | 100ms | Standard practice |
| Strict | `NoteEvalPreset.STRICT` | `.strict` | WEIGHTED | 0ms | Advanced/performance |

For student key transposition, set `studentKeyHz` on the evaluation call
(`CalibraNoteEval.evaluate`, `CalibraMelodyEval` via `student.keyHz`) or
`CalibraLiveEval.setStudentKeyHz(...)` at runtime.

## Breath types (moved to tessera)

The legacy calibra `BreathMetrics { capacity, control, isValid }` and the short-lived 2.0.0 `BreathScore` were both replaced by the unified `tessera.model.BreathMetrics`:

| Old type | New type |
|----------|----------|
| `BreathMetrics { capacity: Float, control: Float, isValid: Boolean }` (1.x calibra) | [`BreathMetrics { controlScore, phrases, alignmentScore }`](../tessera/breath#breathmetrics) |
| `BreathScore { capacity: Float?, controlScore: Float }` (2.0.0-only intermediate) | [`BreathMetrics`](../tessera/breath#breathmetrics) — capacity moved to `phrases?.longestDuration` |
| (none) | [`BreathFunction`](../tessera/breath#breathfunction) — composable intermediate |
| (none) | [`PhraseSummary`](../tessera/breath#phrasesummary) — phrase-level structure |
| (none) | Alignment match score is now `BreathMetrics.alignmentScore: Float?` |

## Error Types

Calibra follows the SDK-wide failure-semantics contract (ADR-022) and the
exception hierarchy in ADR-011. There is no Calibra-specific exception type.

### Failure semantics (ADR-022)

| Failure kind | How it surfaces |
|--------------|-----------------|
| SDK not initialized | Throws `VoxaTraceNotInitializedException` (every facade calls `VT.ensureInitialized()` first) |
| License invalid / revoked | Throws `VoxaTraceKilledException` |
| Caller bug / invalid input (empty samples, non-16kHz audio, malformed config) | Throws `IllegalArgumentException` (via `require()`) |
| Domain inconclusive (valid input, no usable result) | Encoded in the return value — e.g. `CalibraLiveEval.finishPracticingSegment()` returns `null`, `CalibraMelodyEval.evaluate(...)` returns `SingingResult.EMPTY` — never thrown |

All thrown types extend `VoxaTraceException`. See [Authentication](../guides/authentication) for `VoxaTraceNotInitializedException` / `VoxaTraceKilledException` details.

### Kotlin

```kotlin
try {
    val result = CalibraMelodyEval.evaluate(reference, student, extractor)
    // Domain outcome: empty result when nothing could be scored.
    if (result == SingingResult.EMPTY) {
        println("Nothing to score (silent or unalignable recording)")
    }
} catch (e: VoxaTraceNotInitializedException) {
    println("Call VT.initialize(...) first")
} catch (e: IllegalArgumentException) {
    // Caller bug, e.g. audio was not 16kHz.
    println("Invalid input: ${e.message}")
}
```

### Swift

`IllegalArgumentException` bridges through SKIE as a caller-catchable error
(ADR-010 / ADR-022); domain outcomes come back as typed results, not optionals
of optionals.

```swift
do {
    let result = CalibraMelodyEval.evaluate(
        reference: reference,
        student: student,
        contourExtractor: extractor
    )
    if result == SingingResult.companion.EMPTY {
        print("Nothing to score")
    }
} catch {
    // IllegalArgumentException (invalid input) or an uninitialized-SDK error.
    print("Evaluation failed: \(error.localizedDescription)")
}
```

## Next Steps

- [PitchDetection](../tona/pitch-detection) — real-time pitch detection (canonical)
- [CalibraVAD](./vad) — voice activity detection
- [TesseraRange](../tessera/range) — vocal range
- [Common: MusicTheory](../common/music-theory) — `ShrutiAlignmentResult`, `ShrutiOption`, `UserShrutiDerivation`
