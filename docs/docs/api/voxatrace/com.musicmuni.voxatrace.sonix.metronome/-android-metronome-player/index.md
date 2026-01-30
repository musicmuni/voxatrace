//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.metronome](../index.md)/[AndroidMetronomePlayer](index.md)

# AndroidMetronomePlayer

[android]\
class [AndroidMetronomePlayer](index.md) : [MetronomePlayer](../-metronome-player/index.md)

Android MetronomePlayer using MODE_STATIC AudioTracks for low-latency click playback.

Uses separate pre-loaded AudioTracks for sama and beat sounds to avoid blocking writes that cause missed clicks at low BPM.

## Constructors

| | |
|---|---|
| [AndroidMetronomePlayer](-android-metronome-player.md) | [android]<br/>constructor() |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [android]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [bpm](bpm.md) | [android]<br/>open override val [bpm](bpm.md): StateFlow&lt;[Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)&gt;<br/>Current BPM |
| [currentBeat](current-beat.md) | [android]<br/>open override val [currentBeat](current-beat.md): StateFlow&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)&gt;<br/>Current beat index (0-based, wraps at beatsPerCycle) |
| [isPlaying](is-playing.md) | [android]<br/>open override val [isPlaying](is-playing.md): StateFlow&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt;<br/>Whether metronome is playing |

## Functions

| Name | Summary |
|---|---|
| [initialize](initialize.md) | [android]<br/>open suspend override fun [initialize](initialize.md)(config: [MetronomePlayer.Config](../-metronome-player/-config/index.md))<br/>Initialize metronome with config. Loads the sample files. |
| [release](release.md) | [android]<br/>open override fun [release](release.md)()<br/>Release resources. |
| [setBpm](set-bpm.md) | [android]<br/>open override fun [setBpm](set-bpm.md)(bpm: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Update BPM while playing. |
| [setVolume](set-volume.md) | [android]<br/>open override fun [setVolume](set-volume.md)(volume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Set volume. |
| [start](start.md) | [android]<br/>open override fun [start](start.md)()<br/>Start metronome. |
| [stop](stop.md) | [android]<br/>open override fun [stop](stop.md)()<br/>Stop metronome. |
