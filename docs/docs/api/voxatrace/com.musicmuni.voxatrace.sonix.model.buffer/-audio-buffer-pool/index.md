//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.model.buffer](../index.md)/[AudioBufferPool](index.md)

# AudioBufferPool

class [AudioBufferPool](index.md)(poolSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), bufferSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))

Pre-allocated buffer pool for zero-allocation audio processing.

This pool provides reusable FloatArray buffers for real-time DSP operations, avoiding garbage collection pauses in the hot path.

## Usage

```kotlin
val pool = AudioBufferPool(poolSize = 4, bufferSize = 2048)

// Acquire a buffer for processing
val buffer = pool.acquire()

// Fill with audio samples
audioBuffer.fillFloatSamples(buffer)

// Process with Calibra
calibra.detectPitch(buffer, sampleCount)

// Release back to pool
pool.release(buffer)
```

## Thread Safety

All operations are thread-safe. Multiple threads can acquire and release buffers concurrently.

#### Parameters

common

| | |
|---|---|
| poolSize | Number of buffers to pre-allocate |
| bufferSize | Size of each buffer in samples |

## Constructors

| | |
|---|---|
| [AudioBufferPool](-audio-buffer-pool.md) | [common]<br/>constructor(poolSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), bufferSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [available](available.md) | [common]<br/>val [available](available.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of buffers currently available in the pool. |
| [totalAllocated](total-allocated.md) | [common]<br/>val [totalAllocated](total-allocated.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Total buffers created (includes overflow allocations). If this exceeds poolSize, the pool was exhausted at some point. |
| [wasExhausted](was-exhausted.md) | [common]<br/>val [wasExhausted](was-exhausted.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if the pool has been exhausted (more allocations than poolSize). Use this in debug builds to tune poolSize. |

## Functions

| Name | Summary |
|---|---|
| [acquire](acquire.md) | [common]<br/>fun [acquire](acquire.md)(): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Acquire a buffer from the pool. |
| [release](release.md) | [common]<br/>fun [release](release.md)(buffer: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html))<br/>Release a buffer back to the pool. |
| [resetStats](reset-stats.md) | [common]<br/>fun [resetStats](reset-stats.md)()<br/>Reset pool statistics (for debugging). |
