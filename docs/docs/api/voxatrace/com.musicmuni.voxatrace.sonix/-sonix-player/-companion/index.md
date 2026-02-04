---
sidebar_label: "Companion"
---


# Companion

[common]\
object [Companion](index.md)

## Functions

| Name | Summary |
|---|---|
| [create](create.md) | [common]<br/>suspend fun [create](create.md)(source: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), config: [SonixPlayerConfig](../../-sonix-player-config/index.md) = SonixPlayerConfig.DEFAULT, audioSession: [AudioMode](../../-audio-mode/index.md) = AudioMode.PLAYBACK): [SonixPlayer](../index.md)<br/>Create player from source with configuration. |
| [createFromPcm](create-from-pcm.md) | [common]<br/>fun [createFromPcm](create-from-pcm.md)(data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44100, channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1, config: [SonixPlayerConfig](../../-sonix-player-config/index.md) = SonixPlayerConfig.DEFAULT, audioSession: [AudioMode](../../-audio-mode/index.md) = AudioMode.PLAYBACK): [SonixPlayer](../index.md)<br/>Create player from raw PCM data with configuration. |
