//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.common](../index.md)/[KeyShift](index.md)/[rotateHpcp](rotate-hpcp.md)

# rotateHpcp

[common]\
fun [rotateHpcp](rotate-hpcp.md)(hpcpFrames: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)&gt;, semitones: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), hpcpSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 12)

Rotate HPCP frames to compensate for key difference.

HPCP is octave-invariant, so we only need to rotate bins within the chroma circle. This is equivalent to transposing the harmonic content without changing octave.

#### Parameters

common

| | |
|---|---|
| hpcpFrames | HPCP frames [numFrames](rotate-hpcp.md) (modified in place) |
| semitones | Semitones to rotate (positive = clockwise) |
| hpcpSize | Number of HPCP bins (default 12 for chromatic) |
