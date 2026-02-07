---
sidebar_position: 9
---

# CalibraBreath

Breath capacity and control analysis for vocal performance assessment. Measures how well a singer manages their breathing by analyzing pitch contour data.

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

## When to Use

| Scenario | Use This? | Why |
|----------|-----------|-----|
| Analyze sustained note ability | Yes | Core use case |
| Track breath improvement | Yes | Compare over time |
| Compare student vs. reference | Yes | Use `computeMetrics` |
| Real-time breath feedback | No | Use pitch contour length instead |
| Detect breathing moments | Partially | Use unvoiced gaps in pitch data |

## Methods

All methods are static on the `CalibraBreath` object (Kotlin) / class (Swift).

### hasEnoughData

Check if there is enough data for breath analysis. Requires at least 5 seconds of cumulative voiced audio to produce meaningful results.

#### Kotlin

```kotlin
val enough: Boolean = CalibraBreath.hasEnoughData(times, pitchesHz)
```

#### Swift

```swift
let enough: Bool = CalibraBreath.hasEnoughData(times: times, pitchesHz: pitches)
```

#### Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `times` | `FloatArray` / `[Float]` | Timestamps in seconds |
| `pitchesHz` | `FloatArray` / `[Float]` | Pitch values in Hz (-1 for unvoiced frames) |

**Returns:** `Boolean` / `Bool` -- `true` if there are at least 5 seconds of voiced audio.

### computeCapacity

Compute breath capacity from a pitch contour. Measures the maximum duration of sustained voiced segments, indicating how long the singer can hold notes without breathing.

Internally resamples the pitch contour to a 10 Hz feature rate using `SonixResampler` (libsamplerate), eliminates short non-breath silences, and models breath reserve as an exponential function.

#### Kotlin

```kotlin
val capacity: Float = CalibraBreath.computeCapacity(times, pitchesHz)
```

#### Swift

```swift
let capacity: Float = CalibraBreath.computeCapacity(times: times, pitchesHz: pitches)
```

#### Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `times` | `FloatArray` / `[Float]` | Timestamps in seconds |
| `pitchesHz` | `FloatArray` / `[Float]` | Pitch values in Hz (-1 for unvoiced frames) |

**Returns:** `Float` -- Breath capacity in seconds (longest sustained phrase). Returns `-1` on failure. Returns at least `1` on success.

### getCumulativeVoicedTime

Calculate the total amount of time where pitch was detected (i.e., the singer was producing voiced sound).

#### Kotlin

```kotlin
val voicedTime: Float = CalibraBreath.getCumulativeVoicedTime(times, pitchesHz)
```

#### Swift

```swift
let voicedTime: Float = CalibraBreath.getCumulativeVoicedTime(times: times, pitchesHz: pitches)
```

#### Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `times` | `FloatArray` / `[Float]` | Timestamps in seconds |
| `pitchesHz` | `FloatArray` / `[Float]` | Pitch values in Hz (-1 for unvoiced frames) |

**Returns:** `Float` -- Total voiced time in seconds. Returns `-1` on failure (fewer than 2 samples or mismatched array lengths).

### computeMetrics

Compute comprehensive breath metrics comparing a student performance to a reference. Internally merges consecutive feedback segments into sung regions, then computes per-region breath capacity and control scores using FFT-based cross-correlation alignment and peak detection.

#### Kotlin

```kotlin
val metrics: BreathMetrics = CalibraBreath.computeMetrics(
    refTimes = refTimes,
    refPitchesHz = refPitchesHz,
    studentTimes = studentTimes,
    studentPitchesHz = studentPitchesHz,
    feedbackSegmentIndices = feedbackSegmentIndices,
    feedbackStartTimes = feedbackStartTimes,
    feedbackEndTimes = feedbackEndTimes,
    refSegmentStarts = refSegmentStarts,
    refSegmentEnds = refSegmentEnds
)
```

#### Swift

```swift
let metrics: BreathMetrics = CalibraBreath.computeMetrics(
    refTimes: refTimes,
    refPitchesHz: refPitchesHz,
    studentTimes: studentTimes,
    studentPitchesHz: studentPitchesHz,
    feedbackSegmentIndices: feedbackSegmentIndices,
    feedbackStartTimes: feedbackStartTimes,
    feedbackEndTimes: feedbackEndTimes,
    refSegmentStarts: refSegmentStarts,
    refSegmentEnds: refSegmentEnds
)
```

#### Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `refTimes` | `FloatArray` / `[Float]` | Reference pitch timestamps in seconds |
| `refPitchesHz` | `FloatArray` / `[Float]` | Reference pitches in Hz |
| `studentTimes` | `FloatArray` / `[Float]` | Student's pitch timestamps in seconds |
| `studentPitchesHz` | `FloatArray` / `[Float]` | Student's pitches in Hz |
| `feedbackSegmentIndices` | `IntArray` / `[Int]` | Indices of feedback segments |
| `feedbackStartTimes` | `FloatArray` / `[Float]` | Start times of feedback segments |
| `feedbackEndTimes` | `FloatArray` / `[Float]` | End times of feedback segments |
| `refSegmentStarts` | `FloatArray` / `[Float]` | Reference segment start times |
| `refSegmentEnds` | `FloatArray` / `[Float]` | Reference segment end times |

**Returns:** `BreathMetrics` with capacity, control, and validity.

## Result Types

### BreathMetrics

| Property | Type | Description |
|----------|------|-------------|
| `capacity` | `Float` | Breath capacity in seconds -- longest sustained phrase |
| `control` | `Float` | Breath control score (0.0 to 1.0) -- breathing pattern consistency vs. reference |
| `isValid` | `Boolean` | Whether the result is valid (enough data was available) |

When data is insufficient or computation fails, `computeMetrics` returns `BreathMetrics(capacity = -1, control = -1, isValid = false)`.

## Understanding Breath Capacity

Breath capacity represents the longest sustained phrase duration:

| Capacity | Level | Interpretation |
|----------|-------|----------------|
| < 3 seconds | Needs work | Short breath support |
| 3--5 seconds | Beginner | Average, typical for untrained singers |
| 5--8 seconds | Good | Solid breath control |
| > 8 seconds | Excellent | Trained singer level |

## Understanding Breath Control

Breath control (from `computeMetrics`) measures how well the student's breathing patterns match the reference performance:

| Score | Interpretation |
|-------|----------------|
| 0.8--1.0 | Excellent match to reference breathing |
| 0.5--0.8 | Moderate alignment, room for improvement |
| < 0.5 | Poor match, breathing patterns differ significantly |

The score is computed by aligning the student's breath function against the reference using FFT cross-correlation, then comparing peak positions and amplitudes with a 0.5-second tolerance and 30% amplitude similarity threshold.

## Common Patterns

### Post-Lesson Breath Report

```kotlin
class BreathReportViewModel(
    private val pitchExtractor: CalibraPitch.ContourExtractor
) : ViewModel() {

    fun analyzeRecording(audio: FloatArray, sampleRate: Int) {
        viewModelScope.launch {
            val contour = pitchExtractor.extract(audio, sampleRate)
            val times = contour.toTimesArray()
            val pitches = contour.toPitchesArray()

            if (!CalibraBreath.hasEnoughData(times, pitches)) {
                showMessage("Not enough singing data. Need at least 5 seconds.")
                return@launch
            }

            val capacity = CalibraBreath.computeCapacity(times, pitches)
            val voicedTime = CalibraBreath.getCumulativeVoicedTime(times, pitches)

            showResults(
                capacitySeconds = capacity,
                totalSungTime = voicedTime
            )
        }
    }
}
```

### Compare Student to Reference

```kotlin
fun evaluateBreath(
    refTimes: FloatArray, refPitches: FloatArray,
    studentTimes: FloatArray, studentPitches: FloatArray,
    feedbackIndices: IntArray,
    feedbackStarts: FloatArray, feedbackEnds: FloatArray,
    refStarts: FloatArray, refEnds: FloatArray
) {
    val metrics = CalibraBreath.computeMetrics(
        refTimes = refTimes,
        refPitchesHz = refPitches,
        studentTimes = studentTimes,
        studentPitchesHz = studentPitches,
        feedbackSegmentIndices = feedbackIndices,
        feedbackStartTimes = feedbackStarts,
        feedbackEndTimes = feedbackEnds,
        refSegmentStarts = refStarts,
        refSegmentEnds = refEnds
    )

    if (metrics.isValid) {
        println("Breath capacity: ${metrics.capacity}s")
        println("Breath control: ${(metrics.control * 100).toInt()}%")
    }
}
```

## Common Pitfalls

1. **Not enough data** -- Need 5+ seconds of actual singing (not silence). Always check with `hasEnoughData` first.
2. **Wrong pitch format** -- Use `-1` for unvoiced frames, not `0`. Values at or below 50 Hz are treated as silence internally.
3. **Mismatched array lengths** -- `times` and `pitchesHz` must be the same length. Methods return `-1` if they differ.
4. **Timestamps must be evenly spaced** -- The sample rate is inferred from the gap between the first two timestamps. Irregular spacing will produce incorrect results.

## Next Steps

- [CalibraPitch](./pitch) -- Extract pitch contours to feed into breath analysis
- [CalibraVAD](./vad) -- Detect voice activity before pitch extraction
- [CalibraVocalRange](./vocal-range) -- Detect singer's pitch range
