//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.util](../index.md)/[SampleAccumulator](index.md)/[addSamples](add-samples.md)

# addSamples

[common]\
fun [addSamples](add-samples.md)(samples: [T](index.md), onFrame: ([T](index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)

Add samples to the accumulator.

When enough samples have accumulated to form a complete frame, the callback is invoked with a copy of the frame data.

#### Return

Number of complete frames processed

#### Parameters

common

| | |
|---|---|
| samples | Input samples (any size) |
| onFrame | Callback invoked for each complete frame |
