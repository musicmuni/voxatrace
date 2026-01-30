//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixDecoder](index.md)/[decodeNative](decode-native.md)

# decodeNative

[common]\
fun [decodeNative](decode-native.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [AudioRawData](../../com.musicmuni.voxatrace.sonix.model/-audio-raw-data/index.md)?

Decodes an audio file at its native sample rate (no resampling).

Use this when you need the original audio quality or will handle resampling yourself via [SonixResampler](../-sonix-resampler/index.md).

#### Return

Decoded audio data at native sample rate, or null if decoding failed

#### Parameters

common

| | |
|---|---|
| path | Absolute path to the audio file |
