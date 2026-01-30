//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.player](../index.md)/[IosAudioPlayer](index.md)

# IosAudioPlayer

[ios]\
class [IosAudioPlayer](index.md)

## Constructors

| | |
|---|---|
| [IosAudioPlayer](-ios-audio-player.md) | [ios]<br/>constructor() |

## Properties

| Name | Summary |
|---|---|
| [currentTime](current-time.md) | [ios]<br/>open val [currentTime](current-time.md): StateFlow&lt;[Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)&gt; |
| [currentTimeMs](current-time-ms.md) | [ios]<br/>open val [currentTimeMs](current-time-ms.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
| [duration](duration.md) | [ios]<br/>open val [duration](duration.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [durationMs](duration-ms.md) | [ios]<br/>open val [durationMs](duration-ms.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
| [isCurrentlyPlaying](is-currently-playing.md) | [ios]<br/>open val [isCurrentlyPlaying](is-currently-playing.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [isPlaying](is-playing.md) | [ios]<br/>open val [isPlaying](is-playing.md): StateFlow&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt; |

## Functions

| Name | Summary |
|---|---|
| [fadeIn](fade-in.md) | [ios]<br/>open suspend fun [fadeIn](fade-in.md)(targetVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)) |
| [fadeOut](fade-out.md) | [ios]<br/>open suspend fun [fadeOut](fade-out.md)(durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)) |
| [load](load.md) | [ios]<br/>open suspend fun [load](load.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>open fun [load](load.md)(data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) |
| [pause](pause.md) | [ios]<br/>open fun [pause](pause.md)() |
| [play](play.md) | [ios]<br/>open fun [play](play.md)() |
| [release](release.md) | [ios]<br/>open fun [release](release.md)() |
| [seekTo](seek-to.md) | [ios]<br/>open fun [seekTo](seek-to.md)(timeMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)) |
| [setLoopCount](set-loop-count.md) | [ios]<br/>open fun [setLoopCount](set-loop-count.md)(count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) |
| [setPitch](set-pitch.md) | [ios]<br/>open fun [setPitch](set-pitch.md)(semitones: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)) |
| [setPlaybackListener](set-playback-listener.md) | [ios]<br/>open fun [setPlaybackListener](set-playback-listener.md)(listener: &lt;Error class: unknown class&gt;?) |
| [setProcessingTap](set-processing-tap.md) | [ios]<br/>open fun [setProcessingTap](set-processing-tap.md)(callback: ([FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)?) |
| [setTempo](set-tempo.md) | [ios]<br/>open fun [setTempo](set-tempo.md)(tempo: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)) |
| [setVolume](set-volume.md) | [ios]<br/>open fun [setVolume](set-volume.md)(volume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)) |
| [setVolumeSmooth](set-volume-smooth.md) | [ios]<br/>open suspend fun [setVolumeSmooth](set-volume-smooth.md)(targetVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), easing: &lt;Error class: unknown class&gt;) |
| [stop](stop.md) | [ios]<br/>open fun [stop](stop.md)() |
