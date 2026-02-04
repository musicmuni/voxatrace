---
sidebar_label: "PlaybackInfoProvider"
---


# PlaybackInfoProvider

interface [PlaybackInfoProvider](index.md)

Interface for providing playback timing information.

Used to synchronize audio recording with playback of a backing track. Any playback source (AudioPlayer, MultiTrackPlayer, YouTube player, etc.) can implement this interface to provide timing data to AudioSession.

## Usage

```kotlin
// AudioPlayer and MultiTrackPlayer already implement this
val player = createAudioPlayer(context)
session.setPlaybackInfoProvider(player)

// For custom sources (e.g., YouTube player)
class YouTubePlayerProvider(private val player: YouTubePlayer) : PlaybackInfoProvider {
    override val currentTimeMs: Long get() = player.currentTimeMillis.toLong()
    override val durationMs: Long get() = player.durationMillis.toLong()
    override val isCurrentlyPlaying: Boolean get() = player.isPlaying
}

session.setPlaybackInfoProvider(YouTubePlayerProvider(youtubePlayer))
```

#### See also

| |
|---|
| AudioSession.setPlaybackInfoProvider |

#### Inheritors

| |
|---|
| [AudioPlayer](../-audio-player/index.md) |

## Properties

| Name | Summary |
|---|---|
| [currentTimeMs](current-time-ms.md) | [common]<br/>abstract val [currentTimeMs](current-time-ms.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br/>Current playback position in milliseconds. |
| [durationMs](duration-ms.md) | [common]<br/>abstract val [durationMs](duration-ms.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br/>Total duration of the media in milliseconds. |
| [isCurrentlyPlaying](is-currently-playing.md) | [common]<br/>abstract val [isCurrentlyPlaying](is-currently-playing.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Whether playback is currently active. |
