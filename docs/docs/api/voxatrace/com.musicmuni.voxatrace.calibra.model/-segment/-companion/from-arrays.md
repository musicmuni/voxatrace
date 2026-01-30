//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../../index.md)/[Segment](../index.md)/[Companion](index.md)/[fromArrays](from-arrays.md)

# fromArrays

[common]\
fun [fromArrays](from-arrays.md)(starts: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), ends: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), lyrics: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt;? = null, studentStarts: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)? = null, studentEnds: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)? = null): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Segment](../index.md)&gt;

Create segments from parallel arrays of start and end times.

#### Parameters

common

| | |
|---|---|
| starts | Segment start times in seconds |
| ends | Segment end times in seconds |
| lyrics | Optional lyrics for each segment |
| studentStarts | Optional student start times (for singafter mode) |
| studentEnds | Optional student end times (for singafter mode) |
