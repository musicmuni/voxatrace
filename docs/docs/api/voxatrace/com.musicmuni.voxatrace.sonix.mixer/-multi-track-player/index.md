//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.mixer](../index.md)/[MultiTrackPlayer](index.md)

# MultiTrackPlayer

interface [MultiTrackPlayer](index.md) : [PlaybackInfoProvider](../../com.musicmuni.voxatrace.sonix.player/-playback-info-provider/index.md)

Multi-track audio player for synchronized playback of multiple audio tracks.

Each track is identified by a unique string name and can have independent volume control. All tracks play in sync with shared playback position.

Usage:

```kotlin
val player = createMultiTrackPlayer()

// Load tracks
player.loadTrack("backing", "/path/to/backing.wav")
player.loadTrack("vocal", "/path/to/vocal.wav")

// Set individual volumes
player.setTrackVolume("backing", 0.8f)
player.setTrackVolume("vocal", 1.0f)

// Control playback
player.play()
player.seekTo(5000) // seek to 5 seconds
player.pause()

// Fade track volume
player.fadeTrackVolume("vocal", targetVolume = 0.5f, durationMs = 200)

// Cleanup
player.release()
```

#### Inheritors

| |
|---|
| AndroidMultiTrackPlayer |

## Types

| Name | Summary |
|---|---|
| [PlaybackListener](-playback-listener/index.md) | [common]<br/>interface [PlaybackListener](-playback-listener/index.md)<br/>Listener for multi-track playback events. |

## Properties

| Name | Summary |
|---|---|
| [completedLoops](completed-loops.md) | [common]<br/>abstract val [completedLoops](completed-loops.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Current completed loop count. |
| [currentTime](current-time.md) | [common]<br/>abstract val [currentTime](current-time.md): StateFlow&lt;[Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)&gt;<br/>Current playback time as a StateFlow. |
| [currentTimeMs](../../com.musicmuni.voxatrace.sonix.player/-playback-info-provider/current-time-ms.md) | [common]<br/>abstract val [currentTimeMs](../../com.musicmuni.voxatrace.sonix.player/-playback-info-provider/current-time-ms.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br/>Current playback position in milliseconds. |
| [duration](duration.md) | [common]<br/>abstract val [duration](duration.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Total duration in milliseconds (based on longest track). |
| [durationMs](../../com.musicmuni.voxatrace.sonix.player/-playback-info-provider/duration-ms.md) | [common]<br/>abstract val [durationMs](../../com.musicmuni.voxatrace.sonix.player/-playback-info-provider/duration-ms.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br/>Total duration of the media in milliseconds. |
| [isCurrentlyPlaying](../../com.musicmuni.voxatrace.sonix.player/-playback-info-provider/is-currently-playing.md) | [common]<br/>abstract val [isCurrentlyPlaying](../../com.musicmuni.voxatrace.sonix.player/-playback-info-provider/is-currently-playing.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Whether playback is currently active. |
| [isPlaying](is-playing.md) | [common]<br/>abstract val [isPlaying](is-playing.md): StateFlow&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt;<br/>Playback state as a StateFlow. |

## Functions

| Name | Summary |
|---|---|
| [fadeTrackVolume](fade-track-volume.md) | [common]<br/>abstract fun [fadeTrackVolume](fade-track-volume.md)(trackName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), targetVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Smoothly transition a track's volume over time.<br/>[common]<br/>abstract fun [fadeTrackVolume](fade-track-volume.md)(trackName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), startVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), endVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Smoothly transition a track's volume from start to end value. |
| [getTrackNames](get-track-names.md) | [common]<br/>abstract fun [getTrackNames](get-track-names.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt;<br/>Get list of all loaded track names. |
| [hasTrack](has-track.md) | [common]<br/>abstract fun [hasTrack](has-track.md)(trackName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if a track is loaded. |
| [loadTrack](load-track.md) | [common]<br/>abstract fun [loadTrack](load-track.md)(trackName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Load a track from decoded PCM data. |
| [pause](pause.md) | [common]<br/>abstract fun [pause](pause.md)()<br/>Pause playback of all tracks. |
| [play](play.md) | [common]<br/>abstract fun [play](play.md)()<br/>Start playback of all tracks. |
| [release](release.md) | [common]<br/>abstract fun [release](release.md)()<br/>Release all resources. |
| [removeTrack](remove-track.md) | [common]<br/>abstract fun [removeTrack](remove-track.md)(trackName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html))<br/>Remove a track from the player. |
| [reset](reset.md) | [common]<br/>abstract fun [reset](reset.md)()<br/>Reset playback position to the beginning without stopping. |
| [seekTo](seek-to.md) | [common]<br/>abstract fun [seekTo](seek-to.md)(timeMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Seek to a specific time position. |
| [setLoopCount](set-loop-count.md) | [common]<br/>abstract fun [setLoopCount](set-loop-count.md)(count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))<br/>Set the number of times to loop playback. |
| [setPlaybackListener](set-playback-listener.md) | [common]<br/>abstract fun [setPlaybackListener](set-playback-listener.md)(listener: [MultiTrackPlayer.PlaybackListener](-playback-listener/index.md)?)<br/>Set listener for playback events. |
| [setTrackVolume](set-track-volume.md) | [common]<br/>abstract fun [setTrackVolume](set-track-volume.md)(trackName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), volume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Set the volume for a specific track. |
| [stop](stop.md) | [common]<br/>abstract fun [stop](stop.md)()<br/>Stop playback and reset to beginning. |
