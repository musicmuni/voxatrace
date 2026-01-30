//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.mixer](../index.md)/[AndroidMultiTrackPlayer](index.md)

# AndroidMultiTrackPlayer

[android]\
class [AndroidMultiTrackPlayer](index.md) : [MultiTrackPlayer](../-multi-track-player/index.md)

Android implementation of MultiTrackPlayer.

Uses multiple AudioTrack instances with synchronized buffer writes for simultaneous playback. Supports VolumeShaper for smooth volume transitions on Android O+.

## Constructors

| | |
|---|---|
| [AndroidMultiTrackPlayer](-android-multi-track-player.md) | [android]<br/>constructor() |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [android]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [completedLoops](completed-loops.md) | [android]<br/>open override val [completedLoops](completed-loops.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Current completed loop count. |
| [currentTime](current-time.md) | [android]<br/>open override val [currentTime](current-time.md): StateFlow&lt;[Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)&gt;<br/>Current playback time as a StateFlow. |
| [currentTimeMs](current-time-ms.md) | [android]<br/>open override val [currentTimeMs](current-time-ms.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br/>Current playback position in milliseconds. |
| [duration](duration.md) | [android]<br/>open override val [duration](duration.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Total duration in milliseconds (based on longest track). |
| [durationMs](duration-ms.md) | [android]<br/>open override val [durationMs](duration-ms.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br/>Total duration of the media in milliseconds. |
| [isCurrentlyPlaying](is-currently-playing.md) | [android]<br/>open override val [isCurrentlyPlaying](is-currently-playing.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Whether playback is currently active. |
| [isPlaying](is-playing.md) | [android]<br/>open override val [isPlaying](is-playing.md): StateFlow&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt;<br/>Playback state as a StateFlow. |

## Functions

| Name | Summary |
|---|---|
| [fadeTrackVolume](fade-track-volume.md) | [android]<br/>open override fun [fadeTrackVolume](fade-track-volume.md)(trackName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), targetVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Smoothly transition a track's volume over time.<br/>[android]<br/>open override fun [fadeTrackVolume](fade-track-volume.md)(trackName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), startVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), endVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Smoothly transition a track's volume from start to end value. |
| [getTrackNames](get-track-names.md) | [android]<br/>open override fun [getTrackNames](get-track-names.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt;<br/>Get list of all loaded track names. |
| [hasTrack](has-track.md) | [android]<br/>open override fun [hasTrack](has-track.md)(trackName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if a track is loaded. |
| [loadTrack](load-track.md) | [android]<br/>open override fun [loadTrack](load-track.md)(trackName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Load a track from decoded PCM data. |
| [pause](pause.md) | [android]<br/>open override fun [pause](pause.md)()<br/>Pause playback of all tracks. |
| [play](play.md) | [android]<br/>open override fun [play](play.md)()<br/>Start playback of all tracks. |
| [release](release.md) | [android]<br/>open override fun [release](release.md)()<br/>Release all resources. |
| [removeTrack](remove-track.md) | [android]<br/>open override fun [removeTrack](remove-track.md)(trackName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html))<br/>Remove a track from the player. |
| [reset](reset.md) | [android]<br/>open override fun [reset](reset.md)()<br/>Reset playback position to the beginning without stopping. |
| [seekTo](seek-to.md) | [android]<br/>open override fun [seekTo](seek-to.md)(timeMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Seek to a specific time position. |
| [setLoopCount](set-loop-count.md) | [android]<br/>open override fun [setLoopCount](set-loop-count.md)(count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))<br/>Set the number of times to loop playback. |
| [setPlaybackListener](set-playback-listener.md) | [android]<br/>open override fun [setPlaybackListener](set-playback-listener.md)(listener: [MultiTrackPlayer.PlaybackListener](../-multi-track-player/-playback-listener/index.md)?)<br/>Set listener for playback events. |
| [setTrackVolume](set-track-volume.md) | [android]<br/>open override fun [setTrackVolume](set-track-volume.md)(trackName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), volume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Set the volume for a specific track. |
| [stop](stop.md) | [android]<br/>open override fun [stop](stop.md)()<br/>Stop playback and reset to beginning. |
