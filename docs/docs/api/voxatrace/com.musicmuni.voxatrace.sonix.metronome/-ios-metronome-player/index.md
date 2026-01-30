//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.metronome](../index.md)/[IosMetronomePlayer](index.md)

# IosMetronomePlayer

[ios]\
class [IosMetronomePlayer](index.md)

## Constructors

| | |
|---|---|
| [IosMetronomePlayer](-ios-metronome-player.md) | [ios]<br/>constructor() |

## Properties

| Name | Summary |
|---|---|
| [bpm](bpm.md) | [ios]<br/>open val [bpm](bpm.md): StateFlow&lt;[Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)&gt; |
| [currentBeat](current-beat.md) | [ios]<br/>open val [currentBeat](current-beat.md): StateFlow&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)&gt; |
| [isPlaying](is-playing.md) | [ios]<br/>open val [isPlaying](is-playing.md): StateFlow&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt; |

## Functions

| Name | Summary |
|---|---|
| [initialize](initialize.md) | [ios]<br/>open suspend fun [initialize](initialize.md)(config: &lt;Error class: unknown class&gt;) |
| [release](release.md) | [ios]<br/>open fun [release](release.md)() |
| [setBpm](set-bpm.md) | [ios]<br/>open fun [setBpm](set-bpm.md)(bpm: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)) |
| [setVolume](set-volume.md) | [ios]<br/>open fun [setVolume](set-volume.md)(volume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)) |
| [start](start.md) | [ios]<br/>open fun [start](start.md)() |
| [stop](stop.md) | [ios]<br/>open fun [stop](stop.md)() |
