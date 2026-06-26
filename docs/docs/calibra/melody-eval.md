---
sidebar_position: 6
---

# CalibraMelodyEval

Offline melody evaluation that compares a complete recorded performance against a reference melody for post-recording scoring and analysis.

## Quick Start

### Kotlin

```kotlin
val reference = LessonMaterial.fromAudio(
    samples = referenceAudio,
    sampleRate = 16000,
    segments = listOf(
        Segment(0, 0.0f, 3.5f, "First line"),
        Segment(1, 3.5f, 7.0f, "Second line")
    ),
    keyHz = 261.63f  // Middle C
)

val student = LessonMaterial.fromAudio(
    samples = studentAudio,
    sampleRate = 16000,
    segments = emptyList(),  // Uses reference segments
    keyHz = 261.63f
)

val extractor = PitchDetection.createContourExtractor(ContourExtractorConfig.SCORING)
val result = CalibraMelodyEval.evaluate(reference, student, extractor)
extractor.release()

println("Overall: ${result.overallScorePercent}%")
result.segmentResults.forEach { (index, attempts) ->
    println("Segment $index: ${attempts.last().scorePercent}%")
}
```

### Swift

```swift
let reference = LessonMaterial.fromAudio(
    samples: referenceAudio,
    sampleRate: 16000,
    segments: [
        Segment.create(index: 0, startSeconds: 0.0, endSeconds: 3.5, lyrics: "First line"),
        Segment.create(index: 1, startSeconds: 3.5, endSeconds: 7.0, lyrics: "Second line")
    ],
    keyHz: 261.63
)

let student = LessonMaterial.fromAudio(
    samples: studentAudio,
    sampleRate: 16000,
    segments: [],
    keyHz: 261.63
)

let extractor = PitchDetection.createContourExtractor(
    config: .scoring
)
defer { extractor.release() }

do {
    let result = try CalibraMelodyEval.evaluate(
        reference: reference,
        student: student,
        contourExtractor: extractor
    )

    print("Overall: \(result.overallScorePercent)%")
    for (index, attempts) in result.sortedSegmentResults {
        print("Segment \(index): \(attempts.last!.scorePercent)%")
    }
} catch {
    print("Evaluation failed: \(error)")
}
```

## Evaluate Method

The single public method on `CalibraMelodyEval`.

### Kotlin

```kotlin
fun evaluate(
    reference: LessonMaterial,
    student: LessonMaterial,
    contourExtractor: PitchContourExtractor
): SingingResult
```

### Swift

```swift
static func evaluate(
    reference: LessonMaterial,
    student: LessonMaterial,
    contourExtractor: PitchContourExtractor
) throws -> SingingResult
```

### Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `reference` | `LessonMaterial` | Reference material with audio, segments, and key |
| `student` | `LessonMaterial` | Student recording with audio and key. If `segments` is empty, reference segments are used. If `pitchContour` is pre-computed, pitch extraction is skipped. |
| `contourExtractor` | `PitchContourExtractor` (from `tona.detection`) | Pre-built contour extractor for pitch extraction. Caller owns the lifecycle and must call `release()` when done. |

### Sample Rate Requirement

Audio in `LessonMaterial` must be **16kHz mono**. How this is enforced depends on the `AudioSource`:

- `AudioSource.Samples` (built via `fromAudio`): `evaluate` throws `IllegalArgumentException` if `sampleRate != 16000`. Decode/resample first with `SonixDecoder`, or call `SonixResampler.resample()` before creating the `LessonMaterial`.
- `AudioSource.File` (built via `fromFile`): the file is decoded (and downmixed/resampled to 16kHz mono) internally via `SonixDecoder` — no pre-check needed.
- `AudioSource.Url`: not supported — `evaluate` throws `IllegalArgumentException`.

### Return Value

Returns `SingingResult.EMPTY` if reference segments are empty, or if either the reference or student pitch contour could not be extracted.

## Creating LessonMaterial

### From Audio Samples

#### Kotlin

```kotlin
LessonMaterial.fromAudio(
    samples: FloatArray,
    sampleRate: Int,
    segments: List<Segment>,
    keyHz: Float,
    pitchContour: PitchContour? = null,
    hpcpFrames: List<FloatArray>? = null
): LessonMaterial
```

#### Swift

```swift
LessonMaterial.fromAudio(
    samples: [Float],
    sampleRate: Int,
    segments: [Segment],
    keyHz: Float,
    pitchContour: PitchContour? = nil,
    hpcpFrames: [[Float]]? = nil
) -> LessonMaterial
```

### Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `samples` | `FloatArray` / `[Float]` | required | Mono audio samples (normalized -1.0 to 1.0) |
| `sampleRate` | `Int` | required | Sample rate in Hz (must be 16000 for melody eval) |
| `segments` | `List<Segment>` / `[Segment]` | required | Segment boundaries with timing and lyrics |
| `keyHz` | `Float` | required | Musical key frequency in Hz (e.g., 261.63 for middle C) |
| `pitchContour` | `PitchContour?` | `null` / `nil` | Pre-computed pitch contour (skips extraction if provided) |
| `hpcpFrames` | `List<FloatArray>?` / `[[Float]]?` | `null` / `nil` | Pre-computed HPCP frames for DTW alignment |

### LessonMaterial Properties

| Property | Type | Description |
|----------|------|-------------|
| `audioSource` | `AudioSource` | Source of the audio data |
| `segments` | `List<Segment>` | Segment boundaries |
| `keyHz` | `Float` | Musical key frequency in Hz |
| `pitchContour` | `PitchContour?` | Pre-computed pitch contour |
| `hpcpFrames` | `List<FloatArray>?` | Pre-computed HPCP frames |
| `duration` | `Float` | Total duration based on last segment end time |
| `segmentCount` | `Int` | Number of segments |

### From File Path

```kotlin
LessonMaterial.fromFile(
    audioPath: String,
    segments: List<Segment>,
    keyHz: Float
): LessonMaterial
```

`AudioSource.File` (created via `fromFile`) is supported: `CalibraMelodyEval.evaluate` decodes the file to 16kHz mono internally via `SonixDecoder`. You can also decode yourself and use `fromAudio` if you already have samples.

## Creating Segments

### Kotlin

```kotlin
// Individual segment
val segment = Segment(
    index = 0,
    startSeconds = 0.0f,
    endSeconds = 3.5f,
    lyrics = "Sa Re Ga Ma"
)

// From parallel arrays
val segments = Segment.fromArrays(
    starts = floatArrayOf(0.0f, 3.5f, 7.0f),
    ends = floatArrayOf(3.5f, 7.0f, 10.5f),
    lyrics = listOf("First line", "Second line", "Third line")
)

// Singafter segment (student sings after reference)
val singafter = Segment(
    index = 0,
    startSeconds = 0.0f,
    endSeconds = 5.0f,
    lyrics = "Sa Re Ga Ma",
    studentStartSeconds = 2.5f,
    studentEndSeconds = 5.0f
)
```

### Swift

```swift
// Individual segment
let segment = Segment.create(
    index: 0,
    startSeconds: 0.0,
    endSeconds: 3.5,
    lyrics: "Sa Re Ga Ma"
)

// Singafter segment
let singafter = Segment.create(
    index: 0,
    startSeconds: 0.0,
    endSeconds: 5.0,
    lyrics: "Sa Re Ga Ma",
    studentStartSeconds: 2.5,
    studentEndSeconds: 5.0
)
```

### Segment Properties

| Property | Type | Description |
|----------|------|-------------|
| `index` | `Int` | Zero-based segment index |
| `startSeconds` | `Float` (Kotlin) / `Double` (Swift) | Reference audio start time |
| `endSeconds` | `Float` (Kotlin) / `Double` (Swift) | Reference audio end time |
| `lyrics` | `String` | Text/lyrics for this segment (default: empty) |
| `studentStartSeconds` | `Float?` | When student recording starts (null = same as `startSeconds`) |
| `studentEndSeconds` | `Float?` | When student recording ends (null = same as `endSeconds`) |
| `duration` | `Float` (Kotlin) / `Double` (Swift) | Duration of the segment in seconds |
| `isSingafter` | `Boolean` | True if student starts after reference |
| `effectiveStudentStart` | `Float` (Kotlin) / `Double` (Swift) | Student start time (falls back to `startSeconds`) |
| `effectiveStudentEnd` | `Float` (Kotlin) / `Double` (Swift) | Student end time (falls back to `endSeconds`) |
| `studentDuration` | `Float` (Kotlin) / `Double` (Swift) | Duration of student recording portion |

## Result Types

### SingingResult

The top-level result returned by `evaluate`.

| Property | Type | Description |
|----------|------|-------------|
| `overallScore` | `Float` | Aggregate score across all segments (0.0 - 1.0) |
| `overallScorePercent` | `Int` | Overall score as percentage (0 - 100) |
| `segmentResults` | `Map<Int, List<SegmentResult>>` | Map of segment index to list of attempt results |
| `aggregation` | `ResultAggregation` | How the overall score was calculated |
| `segmentCount` | `Int` | Number of segments evaluated |
| `totalAttempts` | `Int` | Total attempts across all segments |

#### SingingResult Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| `latestScore(segmentIndex)` | `Float?` | Latest attempt's score for a single segment, or null |
| `bestScore(segmentIndex)` | `Float?` | Best score across attempts for a single segment, or null |
| `latestScorePerSegment()` | `Map<Int, Float>` | Latest score for each segment |
| `bestScorePerSegment()` | `Map<Int, Float>` | Best score for each segment |
| `averageScorePerSegment()` | `Map<Int, Float>` | Average score for each segment |
| `latestResultPerSegment()` | `Map<Int, SegmentResult>` | Latest result for each segment |

#### Swift-Only Extensions

```swift
// Iterate segments in order
for (index, attempts) in result.sortedSegmentResults {
    print("Segment \(index): \(attempts.last!.scorePercent)%")
}

// Access by Swift Int index
if let attempts = result.getSegmentResult(at: 0) {
    print("First segment: \(attempts.last!.scorePercent)%")
}
```

| Property/Method | Type | Description |
|-----------------|------|-------------|
| `sortedSegmentResults` | `[(index: Int, attempts: [SegmentResult])]` | Segment results sorted by index |
| `getSegmentResult(at:)` | `[SegmentResult]?` | Get attempts for a segment by Swift `Int` index |

#### SingingResult Constants

| Constant | Kotlin | Description |
|----------|--------|-------------|
| Empty | `SingingResult.EMPTY` | Empty result with score 0 and no segments |

### SegmentResult

Result for a single evaluated segment.

| Property | Type | Description |
|----------|------|-------------|
| `segment` | `Segment` | The segment that was evaluated |
| `score` | `Float` | Overall score (0.0 - 1.0) |
| `pitchAccuracy` | `Float` | Pitch accuracy component (0.0 - 1.0) |
| `attemptNumber` | `Int` | Which attempt this is (1-based) |
| `referencePitch` | `PitchContour` | Reference pitch contour for visualization |
| `studentPitch` | `PitchContour` | Student pitch contour for visualization |
| `scorePercent` | `Int` | Score as percentage (0 - 100) |

#### Swift Pitch Data Access

```swift
let (times, pitchesHz, pitchesMidi) = segmentResult.referencePitchData
let (studentTimes, studentHz, studentMidi) = segmentResult.studentPitchData
```

### ResultAggregation

How multiple attempts per segment are aggregated into a final score.

| Value | Description |
|-------|-------------|
| `LATEST` | Use the most recent attempt's score (default) |
| `BEST` | Use the highest score across all attempts |
| `AVERAGE` | Use the average of all attempts |

## Common Patterns

### Post-Recording Scoring (Android)

```kotlin
class ScoringViewModel : ViewModel() {
    fun scoreRecording(
        refSamples: FloatArray,
        studentSamples: FloatArray,
        segments: List<Segment>,
        keyHz: Float
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            val reference = LessonMaterial.fromAudio(
                samples = refSamples,
                sampleRate = 16000,
                segments = segments,
                keyHz = keyHz
            )

            val student = LessonMaterial.fromAudio(
                samples = studentSamples,
                sampleRate = 16000,
                segments = emptyList(),
                keyHz = keyHz
            )

            val extractor = PitchDetection.createContourExtractor(
                ContourExtractorConfig.SCORING
            )

            try {
                val result = CalibraMelodyEval.evaluate(reference, student, extractor)
                _score.value = result.overallScorePercent
                _segmentScores.value = result.latestScorePerSegment()
            } finally {
                extractor.release()
            }
        }
    }
}
```

### Pre-Computed Pitch Contour (Skip Extraction)

```kotlin
// Extract pitch once, reuse for multiple evaluations
val extractor = PitchDetection.createContourExtractor(ContourExtractorConfig.SCORING)
val refContour = extractor.extract(referenceAudio, 16000)

val reference = LessonMaterial.fromAudio(
    samples = referenceAudio,
    sampleRate = 16000,
    segments = segments,
    keyHz = 261.63f,
    pitchContour = refContour  // Skip re-extraction
)

// Evaluate multiple student recordings against same reference
for (studentAudio in recordings) {
    val student = LessonMaterial.fromAudio(
        samples = studentAudio,
        sampleRate = 16000,
        segments = emptyList(),
        keyHz = 261.63f
    )
    val result = CalibraMelodyEval.evaluate(reference, student, extractor)
    println("Score: ${result.overallScorePercent}%")
}

extractor.release()
```

### Different Keys (Student vs Reference)

```kotlin
val reference = LessonMaterial.fromAudio(
    samples = refAudio,
    sampleRate = 16000,
    segments = segments,
    keyHz = 261.63f  // Reference in C4
)

val student = LessonMaterial.fromAudio(
    samples = studentAudio,
    sampleRate = 16000,
    segments = emptyList(),
    keyHz = 196.0f   // Student sings in G3
)

// Evaluator normalizes by key difference automatically
val result = CalibraMelodyEval.evaluate(reference, student, extractor)
```

### Per-Segment Scores (iOS)

```swift
do {
    let result = try CalibraMelodyEval.evaluate(
        reference: reference,
        student: student,
        contourExtractor: extractor
    )

    for (index, attempts) in result.sortedSegmentResults {
        guard let latest = attempts.last else { continue }
        print("Segment \(index): \(latest.scorePercent)%")
        print("  Score: \(latest.score)")
    }
} catch {
    print("Evaluation failed: \(error)")
}
```

## Next Steps

- [PitchDetection](../tona/pitch-detection) — pitch detection and contour extraction
- [CalibraVAD](./vad) — voice activity detection
- [TesseraRange](../tessera/range) — vocal range analysis
- [Accura](../accura/intonation) — intonation analysis and 0–100 scoring
