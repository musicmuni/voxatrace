//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../../index.md)/[LessonMaterial](../index.md)/[Companion](index.md)/[fromAudio](from-audio.md)

# fromAudio

[common]\
fun [fromAudio](from-audio.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), segments: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Segment](../../-segment/index.md)&gt;, keyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), pitchContour: [PitchContour](../../-pitch-contour/index.md)? = null, hpcpFrames: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)&gt;? = null): [LessonMaterial](../index.md)

Create a LessonMaterial from raw audio samples.

#### Parameters

common

| | |
|---|---|
| samples | Mono audio samples |
| sampleRate | Sample rate in Hz |
| segments | List of segments |
| keyHz | Key frequency in Hz |
| pitchContour | Pre-computed pitch contour (enables fast path) |
| hpcpFrames | Pre-computed HPCP frames for DTW alignment |
