---
sidebar_label: "AudioPlayer"
---


# AudioPlayer

interface [AudioPlayer](index.md) : [PlaybackInfoProvider](../-playback-info-provider/index.md)

Audio playback interface with pitch shifting, looping, and volume control.

AudioPlayer provides comprehensive audio playback capabilities including:

- 
   Loading from file path or raw PCM data
- 
   Play/pause/stop/seek controls
- 
   Volume control with smooth fading
- 
   Pitch shifting (semitone adjustment)
- 
   Looping with configurable count
- 
   Reactive state via StateFlow

Implements [PlaybackInfoProvider](../-playback-info-provider/index.md) for synchronization with recording components.

## Usage

```kotlin
val player = createAudioPlayer(context)

// Load from file
player.load("/path/to/audio.wav")

// Or load from raw PCM data
player.load(pcmBytes, sampleRate = 44100, channels = 2)

// Configure playback
player.setVolume(0.8f)
player.setPitch(-2f)  // Lower by 2 semitones
player.setLoopCount(3)

// Play with fade-in
player.fadeIn(targetVolume = 1.0f, durationMs = 500)
player.play()

// Observe state reactively
player.isPlaying.collect { playing ->
    updatePlayButton(playing)
}

player.currentTime.collect { timeMs ->
    updateProgressBar(timeMs)
}

// Cleanup
player.release()
```

## Platform Implementation

- 
   **Android**: Uses android.media.AudioTrack with VolumeShaper for smooth fading
- 
   **iOS**: Uses AVAudioEngine with AVAudioPlayerNode

#### See also

| | |
|---|---|
| MultiTrackPlayer | For synchronized multi-track playback |
| [PlaybackInfoProvider](../-playback-info-provider/index.md) | For synchronization interface |

## Types

| Name | Summary |
|---|---|
| [PlaybackListener](-playback-listener/index.md) | [common]<br/>interface [PlaybackListener](-playback-listener/index.md)<br/>Listener interface for playback events. |

## Properties

| Name | Summary |
|---|---|
| [currentTime](current-time.md) | [common]<br/>abstract val [currentTime](current-time.md): StateFlow&lt;[Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)&gt;<br/>Current playback position as a reactive StateFlow. |
| [currentTimeMs](../-playback-info-provider/current-time-ms.md) | [common]<br/>abstract val [currentTimeMs](../-playback-info-provider/current-time-ms.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br/>Current playback position in milliseconds. |
| [duration](duration.md) | [common]<br/>abstract val [duration](duration.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Total duration of the loaded audio in milliseconds. |
| [durationMs](../-playback-info-provider/duration-ms.md) | [common]<br/>abstract val [durationMs](../-playback-info-provider/duration-ms.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br/>Total duration of the media in milliseconds. |
| [isCurrentlyPlaying](../-playback-info-provider/is-currently-playing.md) | [common]<br/>abstract val [isCurrentlyPlaying](../-playback-info-provider/is-currently-playing.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Whether playback is currently active. |
| [isPlaying](is-playing.md) | [common]<br/>abstract val [isPlaying](is-playing.md): StateFlow&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt;<br/>Playback state as a reactive StateFlow. |

## Functions

| Name | Summary |
|---|---|
| [fadeIn](fade-in.md) | [common]<br/>abstract suspend fun [fadeIn](fade-in.md)(targetVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f, durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) = 500)<br/>Fades in from current volume (or 0 if stopped) to target volume. |
| [fadeOut](fade-out.md) | [common]<br/>abstract suspend fun [fadeOut](fade-out.md)(durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) = 500)<br/>Fades out from current volume to 0. |
| [load](load.md) | [common]<br/>abstract suspend fun [load](load.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Loads audio from a file path.<br/>[common]<br/>abstract fun [load](load.md)(data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44100, channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1)<br/>Loads audio from raw PCM data. |
| [pause](pause.md) | [common]<br/>abstract fun [pause](pause.md)()<br/>Pauses playback at the current position. |
| [play](play.md) | [common]<br/>abstract fun [play](play.md)()<br/>Starts or resumes playback. |
| [release](release.md) | [common]<br/>abstract fun [release](release.md)()<br/>Releases all resources associated with this player. |
| [seekTo](seek-to.md) | [common]<br/>abstract fun [seekTo](seek-to.md)(timeMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Seeks to a specific position in the audio. |
| [setLoopCount](set-loop-count.md) | [common]<br/>abstract fun [setLoopCount](set-loop-count.md)(count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))<br/>Sets the number of times to loop playback. |
| [setPitch](set-pitch.md) | [common]<br/>abstract fun [setPitch](set-pitch.md)(semitones: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Sets pitch shift in semitones. |
| [setPlaybackListener](set-playback-listener.md) | [common]<br/>abstract fun [setPlaybackListener](set-playback-listener.md)(listener: [AudioPlayer.PlaybackListener](-playback-listener/index.md)?)<br/>Sets a listener for playback events. |
| [setProcessingTap](set-processing-tap.md) | [common]<br/>open fun [setProcessingTap](set-processing-tap.md)(callback: ([FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)?)<br/>Installs a processing tap to receive and modify audio buffers during playback. |
| [setTempo](set-tempo.md) | [common]<br/>abstract fun [setTempo](set-tempo.md)(tempo: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Sets playback tempo (speed without pitch change). |
| [setVolume](set-volume.md) | [common]<br/>abstract fun [setVolume](set-volume.md)(volume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Sets the playback volume. |
| [setVolumeSmooth](set-volume-smooth.md) | [common]<br/>abstract suspend fun [setVolumeSmooth](set-volume-smooth.md)(targetVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), easing: [VolumeEasing](../-volume-easing/index.md) = VolumeEasing.Linear)<br/>Smoothly transitions volume to target value over specified duration. |
| [stop](stop.md) | [common]<br/>abstract fun [stop](stop.md)()<br/>Stops playback and resets to the beginning. |
