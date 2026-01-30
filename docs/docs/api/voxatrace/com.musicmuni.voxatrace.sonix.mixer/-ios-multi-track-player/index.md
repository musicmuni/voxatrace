//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.mixer](../index.md)/[IosMultiTrackPlayer](index.md)

# IosMultiTrackPlayer

[ios]\
class [IosMultiTrackPlayer](index.md)

iOS implementation of MultiTrackPlayer using AVAudioEngine.

Each track uses a separate AVAudioPlayerNode connected to a shared AVAudioMixerNode, allowing independent volume control per track.

## Constructors

| | |
|---|---|
| [IosMultiTrackPlayer](-ios-multi-track-player.md) | [ios]<br/>constructor() |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [ios]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [completedLoops](completed-loops.md) | [ios]<br/>open val [completedLoops](completed-loops.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [currentTime](current-time.md) | [ios]<br/>open val [currentTime](current-time.md): StateFlow&lt;[Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)&gt; |
| [currentTimeMs](current-time-ms.md) | [ios]<br/>open val [currentTimeMs](current-time-ms.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
| [duration](duration.md) | [ios]<br/>open val [duration](duration.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [durationMs](duration-ms.md) | [ios]<br/>open val [durationMs](duration-ms.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
| [isCurrentlyPlaying](is-currently-playing.md) | [ios]<br/>open val [isCurrentlyPlaying](is-currently-playing.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [isPlaying](is-playing.md) | [ios]<br/>open val [isPlaying](is-playing.md): StateFlow&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt; |

## Functions

| Name | Summary |
|---|---|
| [fadeTrackVolume](fade-track-volume.md) | [ios]<br/>open fun [fadeTrackVolume](fade-track-volume.md)(trackName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), targetVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>open fun [fadeTrackVolume](fade-track-volume.md)(trackName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), startVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), endVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)) |
| [getTrackNames](get-track-names.md) | [ios]<br/>open fun [getTrackNames](get-track-names.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt; |
| [hasTrack](has-track.md) | [ios]<br/>open fun [hasTrack](has-track.md)(trackName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [loadTrack](load-track.md) | [ios]<br/>open fun [loadTrack](load-track.md)(trackName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [pause](pause.md) | [ios]<br/>open fun [pause](pause.md)() |
| [play](play.md) | [ios]<br/>open fun [play](play.md)() |
| [release](release.md) | [ios]<br/>open fun [release](release.md)() |
| [removeTrack](remove-track.md) | [ios]<br/>open fun [removeTrack](remove-track.md)(trackName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) |
| [reset](reset.md) | [ios]<br/>open fun [reset](reset.md)() |
| [seekTo](seek-to.md) | [ios]<br/>open fun [seekTo](seek-to.md)(timeMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)) |
| [setLoopCount](set-loop-count.md) | [ios]<br/>open fun [setLoopCount](set-loop-count.md)(count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) |
| [setPlaybackListener](set-playback-listener.md) | [ios]<br/>open fun [setPlaybackListener](set-playback-listener.md)(listener: &lt;Error class: unknown class&gt;?) |
| [setTrackVolume](set-track-volume.md) | [ios]<br/>open fun [setTrackVolume](set-track-volume.md)(trackName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), volume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)) |
| [stop](stop.md) | [ios]<br/>open fun [stop](stop.md)() |
