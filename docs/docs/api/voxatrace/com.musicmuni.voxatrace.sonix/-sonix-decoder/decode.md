//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixDecoder](index.md)/[decode](decode.md)

# decode

[common]\
fun [decode](decode.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), targetSampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)? = 16000): [AudioRawData](../../com.musicmuni.voxatrace.sonix.model/-audio-raw-data/index.md)?

Decodes an audio file to raw PCM data.

By default, resamples to 16kHz for compatibility with Calibra APIs. Set `targetSampleRate = null` to decode at native sample rate.

#### Return

Decoded audio data with PCM bytes, sample rate, and channel info,     or null if decoding failed

#### Parameters

common

| | |
|---|---|
| path | Absolute path to the audio file |
| targetSampleRate | Target sample rate in Hz (default: 16000 for Calibra compatibility).     Set to null to decode at native sample rate without resampling. |
