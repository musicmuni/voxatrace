---
sidebar_label: "player"
---


# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [AudioDecoder](-audio-decoder/index.md) | [common]<br/>expect object [AudioDecoder](-audio-decoder/index.md)<br/>Decodes audio files to raw PCM data.<br/>[android, ios]<br/>[android, ios]<br/>actual object [AudioDecoder](-audio-decoder/index.md) |
| [AudioPlayer](-audio-player/index.md) | [common]<br/>interface [AudioPlayer](-audio-player/index.md) : [PlaybackInfoProvider](-playback-info-provider/index.md)<br/>Audio playback interface with pitch shifting, looping, and volume control. |
| [PlaybackInfoProvider](-playback-info-provider/index.md) | [common]<br/>interface [PlaybackInfoProvider](-playback-info-provider/index.md)<br/>Interface for providing playback timing information. |
| [VolumeEasing](-volume-easing/index.md) | [common]<br/>enum [VolumeEasing](-volume-easing/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[VolumeEasing](-volume-easing/index.md)&gt; <br/>Easing curves for volume transitions. |

## Functions

| Name | Summary |
|---|---|
| [createAudioPlayer](create-audio-player.md) | [common]<br/>expect fun [createAudioPlayer](create-audio-player.md)(): [AudioPlayer](-audio-player/index.md)<br/>Creates a platform-specific [AudioPlayer](-audio-player/index.md) instance.<br/>[android, ios]<br/>[android]<br/>actual fun [createAudioPlayer](create-audio-player.md)(): [AudioPlayer](-audio-player/index.md)<br/>[ios]<br/>actual fun [createAudioPlayer](create-audio-player.md)(): AudioPlayer |
