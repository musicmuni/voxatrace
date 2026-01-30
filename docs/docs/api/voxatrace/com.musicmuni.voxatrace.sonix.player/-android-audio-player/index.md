//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.player](../index.md)/[AndroidAudioPlayer](index.md)

# AndroidAudioPlayer

[android]\
class [AndroidAudioPlayer](index.md) : [AudioPlayer](../-audio-player/index.md)

## Constructors

| | |
|---|---|
| [AndroidAudioPlayer](-android-audio-player.md) | [android]<br/>constructor() |

## Properties

| Name | Summary |
|---|---|
| [currentTime](current-time.md) | [android]<br/>open override val [currentTime](current-time.md): StateFlow&lt;[Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)&gt;<br/>Current playback position as a reactive StateFlow. |
| [currentTimeMs](current-time-ms.md) | [android]<br/>open override val [currentTimeMs](current-time-ms.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br/>Current playback position in milliseconds. |
| [duration](duration.md) | [android]<br/>open override val [duration](duration.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Total duration of the loaded audio in milliseconds. |
| [durationMs](duration-ms.md) | [android]<br/>open override val [durationMs](duration-ms.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br/>Total duration of the media in milliseconds. |
| [isCurrentlyPlaying](is-currently-playing.md) | [android]<br/>open override val [isCurrentlyPlaying](is-currently-playing.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Whether playback is currently active. |
| [isPlaying](is-playing.md) | [android]<br/>open override val [isPlaying](is-playing.md): StateFlow&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt;<br/>Playback state as a reactive StateFlow. |

## Functions

| Name | Summary |
|---|---|
| [fadeIn](fade-in.md) | [android]<br/>open suspend override fun [fadeIn](fade-in.md)(targetVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Fades in from current volume (or 0 if stopped) to target volume. |
| [fadeOut](fade-out.md) | [android]<br/>open suspend override fun [fadeOut](fade-out.md)(durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Fades out from current volume to 0. |
| [load](load.md) | [android]<br/>open suspend override fun [load](load.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Loads audio from a file path.<br/>[android]<br/>open override fun [load](load.md)(data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))<br/>Loads audio from raw PCM data. |
| [pause](pause.md) | [android]<br/>open override fun [pause](pause.md)()<br/>Pauses playback at the current position. |
| [play](play.md) | [android]<br/>open override fun [play](play.md)()<br/>Starts or resumes playback. |
| [release](release.md) | [android]<br/>open override fun [release](release.md)()<br/>Releases all resources associated with this player. |
| [seekTo](seek-to.md) | [android]<br/>open override fun [seekTo](seek-to.md)(timeMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Seeks to a specific position in the audio. |
| [setLoopCount](set-loop-count.md) | [android]<br/>open override fun [setLoopCount](set-loop-count.md)(count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))<br/>Sets the number of times to loop playback. |
| [setPitch](set-pitch.md) | [android]<br/>open override fun [setPitch](set-pitch.md)(semitones: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Sets pitch shift in semitones. |
| [setPlaybackListener](set-playback-listener.md) | [android]<br/>open override fun [setPlaybackListener](set-playback-listener.md)(listener: [AudioPlayer.PlaybackListener](../-audio-player/-playback-listener/index.md)?)<br/>Sets a listener for playback events. |
| [setProcessingTap](set-processing-tap.md) | [android]<br/>open override fun [setProcessingTap](set-processing-tap.md)(callback: ([FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)?)<br/>Installs a processing tap to receive and modify audio buffers during playback. |
| [setTempo](set-tempo.md) | [android]<br/>open override fun [setTempo](set-tempo.md)(tempo: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Sets playback tempo (speed without pitch change). |
| [setVolume](set-volume.md) | [android]<br/>open override fun [setVolume](set-volume.md)(volume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Sets the playback volume. |
| [setVolumeSmooth](set-volume-smooth.md) | [android]<br/>open suspend override fun [setVolumeSmooth](set-volume-smooth.md)(targetVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), easing: [VolumeEasing](../-volume-easing/index.md))<br/>Smoothly transitions volume to target value over specified duration. |
| [stop](stop.md) | [android]<br/>open override fun [stop](stop.md)()<br/>Stops playback and resets to the beginning. |
