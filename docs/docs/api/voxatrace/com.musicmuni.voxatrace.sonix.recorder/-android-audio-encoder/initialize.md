//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.recorder](../index.md)/[AndroidAudioEncoder](index.md)/[initialize](initialize.md)

# initialize

[android]\
open suspend override fun [initialize](initialize.md)(sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), bitRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), format: [AudioEncoder.Format](../-audio-encoder/-format/index.md))

Initialize the encoder with audio parameters.

#### Parameters

android

| | |
|---|---|
| sampleRate | Input sample rate in Hz (e.g., 44100) |
| channels | Number of channels (1=mono, 2=stereo) |
| bitRate | Target bitrate in bps (e.g., 128000 for 128 kbps) |
| format | Output format (AAC or MP3) |
