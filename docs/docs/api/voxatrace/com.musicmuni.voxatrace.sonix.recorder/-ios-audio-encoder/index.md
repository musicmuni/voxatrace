//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.recorder](../index.md)/[IosAudioEncoder](index.md)

# IosAudioEncoder

[ios]\
class [IosAudioEncoder](index.md)

iOS implementation of AudioEncoder using AVAudioFile and AVAssetExportSession.

This implementation writes PCM data to a temporary WAV file first, then uses AVAssetExportSession to convert to the target format (AAC in M4A container).

The workflow is:

1. 
   Queue PCM buffers during recording
2. 
   On finalize(), write all PCM to a temporary WAV file using AVAudioFile
3. 
   Use AVAssetExportSession to convert WAV -> AAC/M4A
4. 
   Delete temporary file

## Constructors

| | |
|---|---|
| [IosAudioEncoder](-ios-audio-encoder.md) | [ios]<br/>constructor() |

## Properties

| Name | Summary |
|---|---|
| [state](state.md) | [ios]<br/>open val [state](state.md): StateFlow&lt;&lt;Error class: unknown class&gt;&gt; |

## Functions

| Name | Summary |
|---|---|
| [finalize](finalize.md) | [ios]<br/>open suspend fun [finalize](finalize.md)(outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [initialize](initialize.md) | [ios]<br/>open suspend fun [initialize](initialize.md)(sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), bitRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), format: &lt;Error class: unknown class&gt;) |
| [queueBuffer](queue-buffer.md) | [ios]<br/>open fun [queueBuffer](queue-buffer.md)(pcmData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)) |
| [release](release.md) | [ios]<br/>open fun [release](release.md)() |
