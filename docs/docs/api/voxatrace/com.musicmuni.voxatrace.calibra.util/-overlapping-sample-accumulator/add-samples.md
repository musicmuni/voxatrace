//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.util](../index.md)/[OverlappingSampleAccumulator](index.md)/[addSamples](add-samples.md)

# addSamples

[common]\
fun [addSamples](add-samples.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), onFrame: ([FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)

Add samples and emit overlapping frames.

Frames are emitted every hopSize samples once the buffer is full. Each emitted frame contains frameSize samples for analysis.

#### Return

Number of frames emitted

#### Parameters

common

| | |
|---|---|
| samples | Input samples (any size) |
| onFrame | Callback invoked for each overlapping frame |
