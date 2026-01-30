//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.player](../index.md)/[AudioDecoder](index.md)/[decode](decode.md)

# decode

[common]\
expect fun [decode](decode.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [AudioRawData](../../com.musicmuni.voxatrace.sonix.model/-audio-raw-data/index.md)?

Decodes an audio file to raw PCM data.

#### Return

Decoded audio data with PCM bytes, sample rate, and channel info,     or null if decoding failed

#### Parameters

common

| | |
|---|---|
| path | Absolute path to the audio file |

[android, ios]\
[android]\
actual fun [decode](decode.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [AudioRawData](../../com.musicmuni.voxatrace.sonix.model/-audio-raw-data/index.md)?

[ios]\
actual fun [decode](decode.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): AudioRawData?
