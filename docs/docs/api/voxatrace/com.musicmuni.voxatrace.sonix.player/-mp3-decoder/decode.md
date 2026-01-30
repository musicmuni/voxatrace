//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.player](../index.md)/[Mp3Decoder](index.md)/[decode](decode.md)

# decode

[common]\
expect fun [decode](decode.md)(inputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [AudioRawData](../../com.musicmuni.voxatrace.sonix.model/-audio-raw-data/index.md)?

Decode an MP3 file to raw PCM audio data.

#### Return

Decoded audio data, or null if decoding failed

#### Parameters

common

| | |
|---|---|
| inputPath | Absolute path to the MP3 file |

[android, ios]\
[android]\
actual fun [decode](decode.md)(inputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [AudioRawData](../../com.musicmuni.voxatrace.sonix.model/-audio-raw-data/index.md)?

[ios]\
actual fun [decode](decode.md)(inputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): AudioRawData?
