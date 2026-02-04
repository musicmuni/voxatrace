---
sidebar_label: "CalibraMelodyEval"
---


# CalibraMelodyEval

object [CalibraMelodyEval](index.md)

Offline melody/song evaluation for post-recording analysis.

## What is Melody Evaluation?

Melody evaluation compares a **complete recorded performance** against a reference melody after the fact. Unlike live evaluation (real-time), this analyzes pre-recorded audio.

Use it for:

- 
   **Post-recording feedback**: Score a recording after the session ends
- 
   **Batch processing**: Analyze multiple recordings at once
- 
   **Detailed analysis**: More thorough analysis than real-time allows

## When to Use

| Scenario | Use This? | Why |
|---|---|---|
| Analyze recorded audio | Yes | Core use case |
| Real-time scoring | No | Use `CalibraLiveEval` |
| Evaluate scales/exercises | No | Use `CalibraNoteEval` |
| Just detect pitch | No | Use `CalibraPitch` |

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

val extractor = CalibraPitch.createContourExtractor(ContourExtractorConfig.SCORING)
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
        Segment(index: 0, startSeconds: 0.0, endSeconds: 3.5, label: "First line"),
        Segment(index: 1, startSeconds: 3.5, endSeconds: 7.0, label: "Second line")
    ],
    keyHz: 261.63
)

let student = LessonMaterial.fromAudio(
    samples: studentAudio,
    sampleRate: 16000,
    segments: [],
    keyHz: 261.63
)

let extractor = CalibraPitch.createContourExtractor(
    config: .scoring
)
let result = CalibraMelodyEval.evaluate(
    reference: reference,
    student: student,
    contourExtractor: extractor
)
extractor.release()

print("Overall: \(result.overallScorePercent)%")
```

## Platform Notes

### iOS

- 
   Audio must be 16kHz mono; use SonixDecoder to decode and resample
- 
   Contour extractor must be released after use

### Android

- 
   Audio must be 16kHz mono; use SonixDecoder to decode and resample
- 
   Runs on background thread; wrap in coroutine if needed

## Common Pitfalls

1. 
   **Wrong sample rate**: Audio must be 16kHz; use SonixDecoder with default settings
2. 
   **Forgetting to release extractor**: Call `extractor.release()` when done
3. 
   **Empty segments**: If student.segments is empty, reference.segments is used
4. 
   **Key mismatch**: Set studentKeyHz if student sings in different key

#### See also

| | |
|---|---|
| [CalibraLiveEval](../-calibra-live-eval/index.md) | For real-time evaluation during singing |
| [CalibraNoteEval](../-calibra-note-eval/index.md) | For note-by-note exercise evaluation |
| [LessonMaterial](../../com.musicmuni.voxatrace.calibra.model/-lesson-material/index.md) | For input format specification |
| [SingingResult](../../com.musicmuni.voxatrace.calibra.model/-singing-result/index.md) | For understanding evaluation results |

## Functions

| Name | Summary |
|---|---|
| [evaluate](evaluate.md) | [common]<br/>fun [evaluate](evaluate.md)(reference: [LessonMaterial](../../com.musicmuni.voxatrace.calibra.model/-lesson-material/index.md), student: [LessonMaterial](../../com.musicmuni.voxatrace.calibra.model/-lesson-material/index.md), contourExtractor: [CalibraPitch.ContourExtractor](../-calibra-pitch/-contour-extractor/index.md)): [SingingResult](../../com.musicmuni.voxatrace.calibra.model/-singing-result/index.md)<br/>Evaluate a melody performance against a reference. |
