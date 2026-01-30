//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../../index.md)/[PitchContour](../index.md)/[Companion](index.md)/[fromArrays](from-arrays.md)

# fromArrays

[common]\
fun [fromArrays](from-arrays.md)(times: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), pitches: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000): [PitchContour](../index.md)

Create a PitchContour from parallel arrays (for migration from old API).

#### Parameters

common

| | |
|---|---|
| times | Timestamps in seconds |
| pitches | Pitch values in Hz (-1 for unvoiced) |
| sampleRate | Audio sample rate |
