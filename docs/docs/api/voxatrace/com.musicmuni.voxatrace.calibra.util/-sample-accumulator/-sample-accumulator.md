//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.util](../index.md)/[SampleAccumulator](index.md)/[SampleAccumulator](-sample-accumulator.md)

# SampleAccumulator

[common]\
constructor(frameSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), createArray: ([Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) -&gt; [T](index.md), copyInto: (src: [T](index.md), dest: [T](index.md), destOffset: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), startIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), endIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html), size: ([T](index.md)) -&gt; [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))

#### Parameters

common

| | |
|---|---|
| T | The array type (FloatArray or ShortArray) |
| frameSize | Required number of samples per frame |
| createArray | Factory function to create arrays of type T |
| copyInto | Function to copy elements between arrays |
| size | Function to get array size |
