//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.recorder](../index.md)/[AudioEncoder](index.md)

# AudioEncoder

interface [AudioEncoder](index.md)

Encodes PCM audio to compressed formats (AAC, MP3). Thread-safe: queueBuffer can be called from any thread.

Usage:

```kotlin
val encoder = createAudioEncoder()
encoder.initialize(44100, 1, 128000, Format.AAC)

// Queue PCM data as it becomes available
recorder.audioStream.collect { buffer ->
    encoder.queueBuffer(buffer.data)
}

// Finalize and save to file
val success = encoder.finalize("/path/to/output.m4a")
encoder.release()
```

#### Inheritors

| |
|---|
| AndroidAudioEncoder |

## Types

| Name | Summary |
|---|---|
| [Format](-format/index.md) | [common]<br/>enum [Format](-format/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[AudioEncoder.Format](-format/index.md)&gt; |
| [State](-state/index.md) | [common]<br/>enum [State](-state/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[AudioEncoder.State](-state/index.md)&gt; |

## Properties

| Name | Summary |
|---|---|
| [state](state.md) | [common]<br/>abstract val [state](state.md): StateFlow&lt;[AudioEncoder.State](-state/index.md)&gt;<br/>Current state of the encoder. |

## Functions

| Name | Summary |
|---|---|
| [finalize](finalize.md) | [common]<br/>abstract suspend fun [finalize](finalize.md)(outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Signal end of input and finalize encoding to file. This drains all queued buffers and writes the final file. |
| [initialize](initialize.md) | [common]<br/>abstract suspend fun [initialize](initialize.md)(sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1, bitRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 128000, format: [AudioEncoder.Format](-format/index.md) = Format.AAC)<br/>Initialize the encoder with audio parameters. |
| [queueBuffer](queue-buffer.md) | [common]<br/>abstract fun [queueBuffer](queue-buffer.md)(pcmData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html))<br/>Queue PCM samples for encoding. Call this repeatedly as audio is recorded. Thread-safe - can be called from any thread. |
| [release](release.md) | [common]<br/>abstract fun [release](release.md)()<br/>Release all resources. After calling this, the encoder cannot be reused. |
