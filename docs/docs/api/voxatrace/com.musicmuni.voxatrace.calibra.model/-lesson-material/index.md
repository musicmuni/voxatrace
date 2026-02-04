---
sidebar_label: "LessonMaterial"
---


# LessonMaterial

[common]\
data class [LessonMaterial](index.md)(val audioSource: [AudioSource](../-audio-source/index.md), val segments: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Segment](../-segment/index.md)&gt;, val keyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val pitchContour: [PitchContour](../-pitch-contour/index.md)? = null, val hpcpFrames: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)&gt;? = null)

Reference material for singing evaluation.

Contains the reference audio, segment boundaries, and musical key. Optionally includes pre-computed pitch contour and HPCP frames for fast initialization.

## Constructors

| | |
|---|---|
| [LessonMaterial](-lesson-material.md) | [common]<br/>constructor(audioSource: [AudioSource](../-audio-source/index.md), segments: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Segment](../-segment/index.md)&gt;, keyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), pitchContour: [PitchContour](../-pitch-contour/index.md)? = null, hpcpFrames: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)&gt;? = null) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [audioSource](audio-source.md) | [common]<br/>val [audioSource](audio-source.md): [AudioSource](../-audio-source/index.md) |
| [duration](duration.md) | [common]<br/>val [duration](duration.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Total duration based on the last segment's end time |
| [hpcpFrames](hpcp-frames.md) | [common]<br/>val [hpcpFrames](hpcp-frames.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)&gt;? = null |
| [keyHz](key-hz.md) | [common]<br/>val [keyHz](key-hz.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) |
| [pitchContour](pitch-contour.md) | [common]<br/>val [pitchContour](pitch-contour.md): [PitchContour](../-pitch-contour/index.md)? = null |
| [segmentCount](segment-count.md) | [common]<br/>val [segmentCount](segment-count.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of segments |
| [segments](segments.md) | [common]<br/>val [segments](segments.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Segment](../-segment/index.md)&gt; |
