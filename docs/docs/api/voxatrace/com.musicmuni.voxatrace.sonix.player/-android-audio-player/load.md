//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.player](../index.md)/[AndroidAudioPlayer](index.md)/[load](load.md)

# load

[android]\
open suspend override fun [load](load.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Loads audio from a file path.

Supports WAV, MP3, M4A, and other platform-supported formats. The file is decoded to PCM for playback.

#### Return

`true` if loading succeeded, `false` otherwise

#### Parameters

android

| | |
|---|---|
| path | Absolute path to the audio file |

[android]\
open override fun [load](load.md)(data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))

Loads audio from raw PCM data.

#### Parameters

android

| | |
|---|---|
| data | PCM audio data (16-bit signed integers, little-endian) |
| sampleRate | Sample rate in Hz (default: 44100) |
| channels | Number of channels (1 = mono, 2 = stereo, default: 1) |
