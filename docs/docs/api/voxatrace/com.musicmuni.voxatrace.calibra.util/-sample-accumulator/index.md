//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.util](../index.md)/[SampleAccumulator](index.md)

# SampleAccumulator

class [SampleAccumulator](index.md)&lt;[T](index.md)&gt;(val frameSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), createArray: ([Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) -&gt; [T](index.md), copyInto: (src: [T](index.md), dest: [T](index.md), destOffset: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), startIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), endIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html), size: ([T](index.md)) -&gt; [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))

Accumulates audio samples and invokes a callback when a complete frame is ready.

Handles variable-sized input buffers by accumulating samples until the required frame size is reached. Designed for real-time audio processing where:

- 
   Input buffer sizes may vary (e.g., iOS delivers hardware-rate audio, Android varies by device)
- 
   Algorithms require fixed frame sizes (e.g., YIN pitch detection needs 2048, Silero VAD needs 512)

Following [Essentia's streaming architecture](https://essentia.upf.edu/streaming_architecture.html):

- 
   Algorithms enforce strict frame contracts
- 
   Buffering is an explicit adapter stage

Usage:

```kotlin
val accumulator = FloatSampleAccumulator(frameSize = 1024)

// Variable-sized input automatically buffered
accumulator.addSamples(audioChunk) { completeFrame ->
    // Called only when 1024 samples are ready
    processor.process(completeFrame)
}

// Reset between sessions
accumulator.clear()
```

#### Parameters

common

| | |
|---|---|
| T | The array type (FloatArray or ShortArray) |
| frameSize | Required number of samples per frame |
| createArray | Factory function to create arrays of type T |
| copyInto | Function to copy elements between arrays |
| size | Function to get array size |

## Constructors

| | |
|---|---|
| [SampleAccumulator](-sample-accumulator.md) | [common]<br/>constructor(frameSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), createArray: ([Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) -&gt; [T](index.md), copyInto: (src: [T](index.md), dest: [T](index.md), destOffset: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), startIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), endIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html), size: ([T](index.md)) -&gt; [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [frameSize](frame-size.md) | [common]<br/>val [frameSize](frame-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [hasPending](has-pending.md) | [common]<br/>val [hasPending](has-pending.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Whether there are pending samples that haven't been processed. |
| [pending](pending.md) | [common]<br/>val [pending](pending.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of pending samples not yet forming a complete frame. |

## Functions

| Name | Summary |
|---|---|
| [addSamples](add-samples.md) | [common]<br/>fun [addSamples](add-samples.md)(samples: [T](index.md), onFrame: ([T](index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Add samples to the accumulator. |
| [clear](clear.md) | [common]<br/>fun [clear](clear.md)()<br/>Clear the accumulator buffer. |
