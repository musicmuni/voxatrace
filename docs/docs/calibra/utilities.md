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
    audioPath: "/path/to/reference.mp3",
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
| Ready | `SessionState.ready()` | `SessionState.ready()` | Create a ready state |
| Error | `SessionState.error(message)` | `SessionState.error(message:)` | Create an error state |

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
| `hopSize` | `Int` | `160` | Hop size between frames in samples (160 = 10ms at 16kHz) |
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

## EvaluatorConfig

Configuration for singing evaluation.

### Properties

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `sampleRate` | `Int` | `16000` | Audio sample rate in Hz |
| `semitoneShift` | `Int` | `0` | Transpose reference by this many semitones |

### Presets

| Preset | Kotlin | Swift | Semitone Shift | Description |
|--------|--------|-------|----------------|-------------|
| Standard | `EvaluatorPreset.STANDARD` | `.standard` | 0 | Standard evaluation at original pitch |
| Male to Female | `EvaluatorPreset.MALE_TO_FEMALE` | `.maleToFemale` | +12 | Transpose up one octave |
| Female to Male | `EvaluatorPreset.FEMALE_TO_MALE` | `.femaleToMale` | -12 | Transpose down one octave |
| Transpose Up | `EvaluatorPreset.TRANSPOSE_UP` | `.transposeUp` | +2 | Slight pitch adjustment up |
| Transpose Down | `EvaluatorPreset.TRANSPOSE_DOWN` | `.transposeDown` | -2 | Slight pitch adjustment down |

## BreathMetrics

Result of breath analysis containing capacity and control metrics.

| Property | Type | Description |
|----------|------|-------------|
| `capacity` | `Float` | Breath capacity in seconds - measures longest sustained phrase |
| `control` | `Float` | Breath control score (0.0 to 1.0) - measures breathing pattern consistency |
| `isValid` | `Boolean` | Whether the result is valid (enough data was available) |

## Error Types

### Kotlin

Calibra uses `CalibraException` with a `CalibraErrorType` enum:

| Error Type | Description |
|-----------|-------------|
| `INITIALIZATION_FAILED` | Failed to initialize native library |
| `INVALID_CONFIG` | Invalid configuration parameters |
| `ALLOCATION_FAILED` | Native resource allocation failed |
| `PROCESSING_ERROR` | Processing error |
| `FILE_ERROR` | File not found or inaccessible |
| `UNKNOWN` | Unknown error |

```kotlin
try {
    val detector = CalibraPitch.createDetector(config)
} catch (e: CalibraException) {
    when (e.type) {
        CalibraErrorType.INITIALIZATION_FAILED -> println("Init failed: ${e.message}")
        CalibraErrorType.INVALID_CONFIG -> println("Bad config: ${e.message}")
        else -> println("Error: ${e.message}")
    }
}
```

### Swift

Calibra provides typed error enums on iOS for each subsystem. All conform to `LocalizedError` and provide descriptive `errorDescription` messages.

### PitchDetectorError

| Case | Parameters | Description |
|------|-----------|-------------|
| `initializationFailed` | `reason: String` | Failed to initialize pitch detector |
| `detectionFailed` | `reason: String` | Pitch detection failed |
| `invalidSampleRate` | `rate: Int` | Invalid sample rate (expected 16000 Hz) |
| `bufferTooSmall` | `size: Int, required: Int` | Buffer too small for detection |

```swift
do {
    let detector = try CalibraPitch.createDetector(config: config)
} catch let error as PitchDetectorError {
    print(error.localizedDescription)
}
```

### EvaluatorError

| Case | Parameters | Description |
|------|-----------|-------------|
| `initializationFailed` | `reason: String` | Failed to initialize evaluator |
| `invalidReference` | `reason: String` | Invalid reference audio |
| `evaluationFailed` | `reason: String` | Evaluation failed |
| `segmentNotFound` | `index: Int` | Segment not found at given index |
| `captureNotStarted` | (none) | Capture was not started before evaluation |
| `yamlParsingFailed` | `path: String` | Failed to parse YAML file |

```swift
do {
    let session = try CalibraLiveEval.create(reference: material, config: config)
} catch let error as EvaluatorError {
    switch error {
    case .invalidReference(let reason):
        print("Invalid reference: \(reason)")
    default:
        print(error.localizedDescription)
    }
}
```

### EffectsError

| Case | Parameters | Description |
|------|-----------|-------------|
| `initializationFailed` | `effect: String, reason: String` | Failed to initialize effect |
| `processingFailed` | `effect: String, reason: String` | Effect processing failed |
| `invalidParameter` | `name: String, value: Float, range: String` | Invalid parameter value |

### AnalysisError

| Case | Parameters | Description |
|------|-----------|-------------|
| `insufficientData` | `required: String, actual: String` | Not enough data for analysis |
| `detectionFailed` | `type: String, reason: String` | Detection failed |
| `invalidInput` | `parameter: String, reason: String` | Invalid input parameter |

## TimeUtils

Platform-specific current time utility used internally for benchmarking in public APIs.

```kotlin
// Internal API — not directly consumed by SDK users
internal expect fun currentTimeMillis(): Long
```

`currentTimeMillis()` is an internal `expect`/`actual` function that returns the current time in milliseconds. Each platform provides its own implementation. This is used internally by Calibra APIs for performance timing and benchmarking; it is not part of the public API surface.

## Next Steps

- [CalibraPitch](./pitch) -- Real-time pitch detection
- [CalibraVAD](./vad) -- Voice activity detection
- [CalibraVocalRange](./vocal-range) -- Detect singer's comfortable range
