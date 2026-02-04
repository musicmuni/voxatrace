---
sidebar_label: "CalibraBreath"
---


# CalibraBreath

object [CalibraBreath](index.md)

Breath capacity and control analysis for vocal performance assessment.

## What is Breath Analysis?

Breath analysis measures how well a singer manages their breathing:

- 
   **Breath capacity**: Maximum duration of sustained notes without breathing
- 
   **Breath control**: How well breath is managed during phrases

These metrics help identify:

- 
   Singers who run out of breath mid-phrase
- 
   Progress in building breath support
- 
   Comparison with reference performances

## When to Use

| Scenario | Use This? | Why |
|---|---|---|
| Analyze sustained note ability | Yes | Core use case |
| Track breath improvement | Yes | Compare over time |
| Real-time breath feedback | No | Use pitch contour length |
| Detect breathing moments | Partially | Use unvoiced gaps |

## Quick Start

### Kotlin

```kotlin
// Extract pitch contour first
val contour = pitchExtractor.extract(audio, sampleRate = 16000)
val times = contour.toTimesArray()
val pitches = contour.toPitchesArray()

// Check if enough data (needs 5+ seconds of voiced audio)
if (CalibraBreath.hasEnoughData(times, pitches)) {
    val capacity = CalibraBreath.computeCapacity(times, pitches)
    println("Breath capacity: $capacity seconds")
}

// Get total voiced time
val voicedTime = CalibraBreath.getCumulativeVoicedTime(times, pitches)
println("Total sung time: $voicedTime seconds")
```

### Swift

```swift
// Extract pitch contour first
let contour = pitchExtractor.extract(audio: audio, sampleRate: 16000)
let times = contour.toTimesArray()
let pitches = contour.toPitchesArray()

// Check if enough data (needs 5+ seconds of voiced audio)
if CalibraBreath.hasEnoughData(times: times, pitchesHz: pitches) {
    let capacity = CalibraBreath.computeCapacity(times: times, pitchesHz: pitches)
    print("Breath capacity: \(capacity) seconds")
}

// Get total voiced time
let voicedTime = CalibraBreath.getCumulativeVoicedTime(times: times, pitchesHz: pitches)
print("Total sung time: \(voicedTime) seconds")
```

## Understanding Breath Capacity

Breath capacity represents the longest sustained phrase:

- 
   < **3 seconds**: Short breath support, needs work
- 
   **3-5 seconds**: Average, typical for beginners
- 
   **5-8 seconds**: Good breath control
- 
   > **8 seconds**: Excellent, trained singer level

## Platform Notes

### iOS/Android

- 
   Audio must be 16kHz mono
- 
   Requires at least 5 seconds of voiced audio for meaningful analysis
- 
   Works with pitch contours from any CalibraPitch extractor

## Common Pitfalls

1. 
   **Not enough data**: Need 5+ seconds of actual singing (not silence)
2. 
   **Wrong pitch format**: Use -1 for unvoiced frames (not 0)
3. 
   **Timestamps must match**: `times` and `pitchesHz` must be same length

#### See also

| | |
|---|---|
| [CalibraPitch](../-calibra-pitch/index.md) | For extracting pitch contours |
| [BreathMetrics](../../com.musicmuni.voxatrace.calibra.model/-breath-metrics/index.md) | For detailed breath analysis results |

## Functions

| Name | Summary |
|---|---|
| [computeCapacity](compute-capacity.md) | [common]<br/>fun [computeCapacity](compute-capacity.md)(times: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Compute breath capacity from pitch contour. |
| [computeMetrics](compute-metrics.md) | [common]<br/>fun [computeMetrics](compute-metrics.md)(refTimes: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), refPitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), studentTimes: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), studentPitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), feedbackSegmentIndices: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html), feedbackStartTimes: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), feedbackEndTimes: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), refSegmentStarts: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), refSegmentEnds: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [BreathMetrics](../../com.musicmuni.voxatrace.calibra.model/-breath-metrics/index.md)<br/>Compute comprehensive breath metrics comparing student to reference. |
| [getCumulativeVoicedTime](get-cumulative-voiced-time.md) | [common]<br/>fun [getCumulativeVoicedTime](get-cumulative-voiced-time.md)(times: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Get cumulative voiced time from pitch contour. |
| [hasEnoughData](has-enough-data.md) | [common]<br/>fun [hasEnoughData](has-enough-data.md)(times: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if there's enough data for breath analysis. |
