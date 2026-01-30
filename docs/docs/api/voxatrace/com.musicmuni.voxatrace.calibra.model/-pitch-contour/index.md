//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../index.md)/[PitchContour](index.md)

# PitchContour

[common]\
data class [PitchContour](index.md)(val samples: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[PitchPoint](../-pitch-point/index.md)&gt;, val sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000)

A sequence of pitch points representing a pitch contour over time.

Replaces parallel arrays (times[], pitches[]) with a structured representation.

## Constructors

| | |
|---|---|
| [PitchContour](-pitch-contour.md) | [common]<br/>constructor(samples: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[PitchPoint](../-pitch-point/index.md)&gt;, sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [duration](duration.md) | [common]<br/>val [duration](duration.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Duration of the contour in seconds |
| [isEmpty](is-empty.md) | [common]<br/>val [isEmpty](is-empty.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>True if contour has no samples |
| [pitchesHz](pitches-hz.md) | [common]<br/>val [pitchesHz](pitches-hz.md): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Pitches in Hz (fast path). Unvoiced frames use -1. |
| [pitchesMidi](pitches-midi.md) | [common]<br/>val [pitchesMidi](pitches-midi.md): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Pitches in MIDI for native evaluator (fast path). Unvoiced frames use -1. |
| [sampleRate](sample-rate.md) | [common]<br/>val [sampleRate](sample-rate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000<br/>Original audio sample rate (default 16kHz) |
| [samples](samples.md) | [common]<br/>val [samples](samples.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[PitchPoint](../-pitch-point/index.md)&gt;<br/>List of pitch points in chronological order |
| [size](size.md) | [common]<br/>val [size](size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of samples in the contour |
| [times](times.md) | [common]<br/>val [times](times.md): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Times array for native evaluator (fast path) |
| [voicedRatio](voiced-ratio.md) | [common]<br/>val [voicedRatio](voiced-ratio.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Ratio of voiced samples to total samples (0.0 - 1.0) |

## Functions

| Name | Summary |
|---|---|
| [slice](slice.md) | [common]<br/>fun [slice](slice.md)(startTime: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), endTime: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), relativeTimes: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true): [PitchContour](index.md)<br/>Extract a slice of the contour within a time range. |
| [toPitchesArray](to-pitches-array.md) | [common]<br/>fun [toPitchesArray](to-pitches-array.md)(): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Get pitches as a FloatArray (for compatibility with internal APIs). |
| [toTimesArray](to-times-array.md) | [common]<br/>fun [toTimesArray](to-times-array.md)(): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Get times as a FloatArray (for compatibility with internal APIs). |
