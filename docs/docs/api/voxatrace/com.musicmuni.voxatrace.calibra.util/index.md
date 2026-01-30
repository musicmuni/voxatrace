//[voxatrace](../../index.md)/[com.musicmuni.voxatrace.calibra.util](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [OverlappingSampleAccumulator](-overlapping-sample-accumulator/index.md) | [common]<br/>class [OverlappingSampleAccumulator](-overlapping-sample-accumulator/index.md)(val frameSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val hopSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))<br/>Sample accumulator with overlapping window support. |
| [PitchContourAccumulator](-pitch-contour-accumulator/index.md) | [common]<br/>class [PitchContourAccumulator](-pitch-contour-accumulator/index.md)(sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), hopSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), maxDurationSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 10.0f)<br/>Accumulates pitch points into a live contour with ring buffer support. |
| [SampleAccumulator](-sample-accumulator/index.md) | [common]<br/>class [SampleAccumulator](-sample-accumulator/index.md)&lt;[T](-sample-accumulator/index.md)&gt;(val frameSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), createArray: ([Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) -&gt; [T](-sample-accumulator/index.md), copyInto: (src: [T](-sample-accumulator/index.md), dest: [T](-sample-accumulator/index.md), destOffset: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), startIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), endIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html), size: ([T](-sample-accumulator/index.md)) -&gt; [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))<br/>Accumulates audio samples and invokes a callback when a complete frame is ready. |

## Functions

| Name | Summary |
|---|---|
| [FloatSampleAccumulator](-float-sample-accumulator.md) | [common]<br/>fun [FloatSampleAccumulator](-float-sample-accumulator.md)(frameSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [SampleAccumulator](-sample-accumulator/index.md)&lt;[FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)&gt;<br/>Create a SampleAccumulator for FloatArray. |
| [ShortSampleAccumulator](-short-sample-accumulator.md) | [common]<br/>fun [ShortSampleAccumulator](-short-sample-accumulator.md)(frameSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [SampleAccumulator](-sample-accumulator/index.md)&lt;[ShortArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-short-array/index.html)&gt;<br/>Create a SampleAccumulator for ShortArray (Int16 samples). |
