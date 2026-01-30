//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.recorder](../index.md)/[AndroidAudioEncoder](index.md)

# AndroidAudioEncoder

[android]\
class [AndroidAudioEncoder](index.md) : [AudioEncoder](../-audio-encoder/index.md)

Android implementation of AudioEncoder using MediaCodec and MediaMuxer. Encodes PCM audio to AAC format in an M4A container.

## Constructors

| | |
|---|---|
| [AndroidAudioEncoder](-android-audio-encoder.md) | [android]<br/>constructor() |

## Properties

| Name | Summary |
|---|---|
| [state](state.md) | [android]<br/>open override val [state](state.md): StateFlow&lt;[AudioEncoder.State](../-audio-encoder/-state/index.md)&gt;<br/>Current state of the encoder. |

## Functions

| Name | Summary |
|---|---|
| [finalize](finalize.md) | [android]<br/>open suspend override fun [finalize](finalize.md)(outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Signal end of input and finalize encoding to file. This drains all queued buffers and writes the final file. |
| [initialize](initialize.md) | [android]<br/>open suspend override fun [initialize](initialize.md)(sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), bitRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), format: [AudioEncoder.Format](../-audio-encoder/-format/index.md))<br/>Initialize the encoder with audio parameters. |
| [queueBuffer](queue-buffer.md) | [android]<br/>open override fun [queueBuffer](queue-buffer.md)(pcmData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html))<br/>Queue PCM samples for encoding. Call this repeatedly as audio is recorded. Thread-safe - can be called from any thread. |
| [release](release.md) | [android]<br/>open override fun [release](release.md)()<br/>Release all resources. After calling this, the encoder cannot be reused. |
