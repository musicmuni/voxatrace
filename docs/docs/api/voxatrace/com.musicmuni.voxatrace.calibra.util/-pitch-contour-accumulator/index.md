//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.util](../index.md)/[PitchContourAccumulator](index.md)

# PitchContourAccumulator

class [PitchContourAccumulator](index.md)(sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), hopSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), maxDurationSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 10.0f)

Accumulates pitch points into a live contour with ring buffer support.

DRY component for all detector implementations. Provides:

- 
   Ring buffer to limit memory usage
- 
   StateFlow for real-time UI updates
- 
   Configurable max duration (set per segment)

Usage:

```kotlin
val accumulator = PitchContourAccumulator(sampleRate = 16000, hopSeconds = 0.01f)

// On each pitch detection
accumulator.addPoint(pitchHz, confidence)

// Observe in UI
accumulator.contour.collect { contour -> updateUI(contour) }

// On new segment
accumulator.setMaxDuration(segmentDurationSeconds)
accumulator.clear()
```

#### Parameters

common

| | |
|---|---|
| sampleRate | Audio sample rate (for PitchContour metadata) |
| hopSeconds | Time between pitch samples in seconds (default 10ms) |
| maxDurationSeconds | Initial max duration for the contour (default 10s) |

## Constructors

| | |
|---|---|
| [PitchContourAccumulator](-pitch-contour-accumulator.md) | [common]<br/>constructor(sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), hopSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), maxDurationSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 10.0f) |

## Properties

| Name | Summary |
|---|---|
| [contour](contour.md) | [common]<br/>val [contour](contour.md): StateFlow&lt;[PitchContour](../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md)&gt;<br/>The accumulated pitch contour as a StateFlow. |
| [duration](duration.md) | [common]<br/>val [duration](duration.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Current duration of the accumulated contour in seconds. |
| [size](size.md) | [common]<br/>val [size](size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of points currently accumulated. |

## Functions

| Name | Summary |
|---|---|
| [addPoint](add-point.md) | [common]<br/>fun [addPoint](add-point.md)(pitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), confidence: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Add a pitch point to the accumulator. |
| [clear](clear.md) | [common]<br/>fun [clear](clear.md)()<br/>Clear all accumulated points. |
| [setMaxDuration](set-max-duration.md) | [common]<br/>fun [setMaxDuration](set-max-duration.md)(seconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Set the maximum duration for the contour. |
