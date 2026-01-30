//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixMixer](index.md)/[addTrack](add-track.md)

# addTrack

[common]\
suspend fun [addTrack](add-track.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), filePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Add track from file path (auto-decodes).

This is the recommended way to add tracks. The file is automatically decoded from its format (MP3, M4A, WAV, etc.) to PCM.

#### Return

true if track was added successfully

#### Parameters

common

| | |
|---|---|
| name | Unique name for this track |
| filePath | Path to audio file |

[common]\
fun [addTrack](add-track.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Add track from raw PCM data (for advanced use cases).

Use this when you have pre-decoded audio data or need to load audio from a custom source.

#### Return

true if track was added successfully

#### Parameters

common

| | |
|---|---|
| name | Unique name for this track |
| data | PCM audio data (16-bit signed samples) |
| sampleRate | Sample rate in Hz |
| channels | Number of channels (1 = mono, 2 = stereo) |
