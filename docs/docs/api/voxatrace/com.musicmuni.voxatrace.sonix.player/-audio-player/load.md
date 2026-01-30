//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.player](../index.md)/[AudioPlayer](index.md)/[load](load.md)

# load

[common]\
abstract suspend fun [load](load.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Loads audio from a file path.

Supports WAV, MP3, M4A, and other platform-supported formats. The file is decoded to PCM for playback.

#### Return

`true` if loading succeeded, `false` otherwise

#### Parameters

common

| | |
|---|---|
| path | Absolute path to the audio file |

[common]\
abstract fun [load](load.md)(data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44100, channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1)

Loads audio from raw PCM data.

#### Parameters

common

| | |
|---|---|
| data | PCM audio data (16-bit signed integers, little-endian) |
| sampleRate | Sample rate in Hz (default: 44100) |
| channels | Number of channels (1 = mono, 2 = stereo, default: 1) |
