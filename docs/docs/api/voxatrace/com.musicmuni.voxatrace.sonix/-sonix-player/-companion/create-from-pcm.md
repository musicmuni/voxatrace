//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.sonix](../../index.md)/[SonixPlayer](../index.md)/[Companion](index.md)/[createFromPcm](create-from-pcm.md)

# createFromPcm

[common]\
fun [createFromPcm](create-from-pcm.md)(data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44100, channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1, config: [SonixPlayerConfig](../../-sonix-player-config/index.md) = SonixPlayerConfig.DEFAULT, audioSession: [AudioMode](../../-audio-mode/index.md) = AudioMode.PLAYBACK): [SonixPlayer](../index.md)

Create player from raw PCM data with configuration.

#### Return

Ready-to-play SonixPlayer

#### Parameters

common

| | |
|---|---|
| data | PCM audio data (16-bit signed integers, little-endian) |
| sampleRate | Sample rate in Hz |
| channels | Number of channels (1 = mono, 2 = stereo) |
| config | Player configuration (default: DEFAULT) |
| audioSession | Audio session mode - configures system audio automatically (default: PLAYBACK) |
