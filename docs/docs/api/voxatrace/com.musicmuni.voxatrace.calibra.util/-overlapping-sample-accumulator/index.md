//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.util](../index.md)/[OverlappingSampleAccumulator](index.md)

# OverlappingSampleAccumulator

class [OverlappingSampleAccumulator](index.md)(val frameSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val hopSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))

Sample accumulator with overlapping window support.

Enables high-resolution pitch extraction (~10ms hop) from streaming audio by emitting overlapping frames every hopSize samples instead of waiting for a complete non-overlapping frame.

This solves the frame resolution mismatch problem:

- 
   Reference pitch: ~10ms hop (160 samples at 16kHz) → ~100 frames/sec
- 
   Without overlap: ~64ms/frame (1024 samples) → ~15 frames/sec
- 
   With overlap: ~10ms hop → ~100 frames/sec (matching reference)

Usage:

```kotlin
val accumulator = OverlappingSampleAccumulator(
    frameSize = 1024,  // YIN analysis window
    hopSize = 160      // 10ms at 16kHz
)

accumulator.addSamples(audioChunk) { overlappingFrame ->
    // Called every 10ms with 1024-sample frame
    processor.process(overlappingFrame)
}
```

#### Parameters

common

| | |
|---|---|
| frameSize | Analysis frame size in samples (e.g., 1024 for YIN) |
| hopSize | Samples between frame starts (e.g., 160 = 10ms at 16kHz) |

## Constructors

| | |
|---|---|
| [OverlappingSampleAccumulator](-overlapping-sample-accumulator.md) | [common]<br/>constructor(frameSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), hopSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [frameSize](frame-size.md) | [common]<br/>val [frameSize](frame-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [hopSize](hop-size.md) | [common]<br/>val [hopSize](hop-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [isReady](is-ready.md) | [common]<br/>val [isReady](is-ready.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Whether the buffer has enough samples to start emitting frames. |
| [pendingSamples](pending-samples.md) | [common]<br/>val [pendingSamples](pending-samples.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of samples accumulated since last frame emission. |

## Functions

| Name | Summary |
|---|---|
| [addSamples](add-samples.md) | [common]<br/>fun [addSamples](add-samples.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), onFrame: ([FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Add samples and emit overlapping frames. |
| [clear](clear.md) | [common]<br/>fun [clear](clear.md)()<br/>Clear the accumulator buffer. |
