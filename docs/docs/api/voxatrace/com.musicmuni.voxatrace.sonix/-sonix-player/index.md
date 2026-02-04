---
sidebar_label: "SonixPlayer"
---


# SonixPlayer

class [SonixPlayer](index.md)

Unified audio player for playback with pitch shifting and tempo control.

## What is SonixPlayer?

SonixPlayer plays audio files with real-time control over:

- 
   **Pitch**: Shift up/down by semitones without affecting speed
- 
   **Tempo**: Speed up/slow down without affecting pitch
- 
   **Volume**: Control playback volume
- 
   **Looping**: Repeat audio any number of times

Use it for:

- 
   **Karaoke apps**: Play backing tracks with pitch/tempo adjustment
- 
   **Music practice**: Slow down songs to learn parts
- 
   **Audio players**: Standard playback with seek and loop

## Quick Start

### Kotlin

```kotlin
val player = SonixPlayer.create("song.mp3")
player.play()
// Later...
player.pause()
player.release()
```

### Swift

```swift
let player = try await SonixPlayer.create(source: "song.mp3")
player.play()
// Later...
player.pause()
player.release()
```

## Usage Tiers (ADR-001)

### Tier 1: Zero-Config (80% of users)

#### Kotlin

```kotlin
val player = SonixPlayer.create("song.mp3")
player.play()
```

#### Swift

```swift
let player = try await SonixPlayer.create(source: "song.mp3")
player.play()
```

### Tier 2: With Config (15% of users)

#### Kotlin

```kotlin
val config = SonixPlayerConfig.Builder()
    .volume(0.8f)
    .pitch(-2f)        // Lower by 2 semitones
    .tempo(0.75f)      // 75% speed
    .loopCount(3)      // Repeat 3 times
    .onComplete { println("Done!") }
    .build()
val player = SonixPlayer.create("song.mp3", config)
```

#### Swift

```swift
let config = SonixPlayerConfig.Builder()
    .volume(0.8)
    .pitch(-2)
    .tempo(0.75)
    .loopCount(3)
    .onComplete { print("Done!") }
    .build()
let player = try await SonixPlayer.create(source: "song.mp3", config: config)
```

### Tier 3: Runtime Control (5% of users)

#### Kotlin

```kotlin
player.pitch = -3f      // Change key at runtime
player.tempo = 0.5f     // Slow down to half speed
player.volume = 0.5f    // Reduce volume
```

## Platform Notes

### iOS

- 
   Uses AVFoundation under the hood
- 
   Supports M4A, MP3, WAV, and other system-supported formats
- 
   Audio session automatically configured for playback

### Android

- 
   Uses ExoPlayer under the hood
- 
   Supports M4A, MP3, WAV, OGG, and other ExoPlayer-supported formats
- 
   Audio focus handled automatically

## Common Pitfalls

1. 
   **Forgetting to release**: Call `player.release()` to free resources
2. 
   **File not found**: Ensure path is accessible (bundle/assets on iOS/Android)
3. 
   **Extreme tempo values**: Keep between 0.25 and 4.0 for quality audio
4. 
   **Using after release**: Create a new player if you need to play again

#### See also

| | |
|---|---|
| [SonixPlayerConfig](../-sonix-player-config/index.md) | Configuration options for playback |
| [SonixRecorder](../-sonix-recorder/index.md) | For recording audio |
| CalibraLiveEval | For coordinated playback with live evaluation |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |
| [PlaybackListener](-playback-listener/index.md) | [common]<br/>interface [PlaybackListener](-playback-listener/index.md)<br/>Listener interface for playback events. Alternative to StateFlow observation and Builder callbacks. |

## Properties

| Name | Summary |
|---|---|
| [asPlaybackInfoProvider](as-playback-info-provider.md) | [common]<br/>val [asPlaybackInfoProvider](as-playback-info-provider.md): [PlaybackInfoProvider](../../com.musicmuni.voxatrace.sonix.player/-playback-info-provider/index.md)<br/>This player as a PlaybackInfoProvider for recording sync. Use with SonixRecorder.Builder().playbackSyncProvider() |
| [currentTime](current-time.md) | [common]<br/>val [currentTime](current-time.md): StateFlow&lt;[Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)&gt;<br/>Current playback position in milliseconds |
| [duration](duration.md) | [common]<br/>val [duration](duration.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br/>Total duration in milliseconds |
| [error](error.md) | [common]<br/>val [error](error.md): StateFlow&lt;[SonixError](../-sonix-error/index.md)?&gt; |
| [isPlaying](is-playing.md) | [common]<br/>val [isPlaying](is-playing.md): StateFlow&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt;<br/>Whether currently playing |
| [loopCount](loop-count.md) | [common]<br/>var [loopCount](loop-count.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Loop count: 1 = play once, 2 = repeat once, -1 = infinite |
| [pitch](pitch.md) | [common]<br/>var [pitch](pitch.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Pitch shift in semitones (-12 to +12) |
| [tempo](tempo.md) | [common]<br/>var [tempo](tempo.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Tempo/speed multiplier (0.25 to 4.0, 1.0 = normal speed) |
| [volume](volume.md) | [common]<br/>var [volume](volume.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Volume from 0.0 (silent) to 1.0 (full) |

## Functions

| Name | Summary |
|---|---|
| [fadeIn](fade-in.md) | [common]<br/>suspend fun [fadeIn](fade-in.md)(targetVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f, durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) = 500)<br/>Fade in to target volume |
| [fadeOut](fade-out.md) | [common]<br/>suspend fun [fadeOut](fade-out.md)(durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) = 500)<br/>Fade out to silence |
| [load](load.md) | [common]<br/>suspend fun [load](load.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Load audio from file path (for Builder without source)<br/>[common]<br/>fun [load](load.md)(data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44100, channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1)<br/>Load audio from raw PCM data |
| [pause](pause.md) | [common]<br/>fun [pause](pause.md)()<br/>Pause playback |
| [play](play.md) | [common]<br/>fun [play](play.md)()<br/>Start or resume playback |
| [release](release.md) | [common]<br/>fun [release](release.md)()<br/>Release all resources |
| [seek](seek.md) | [common]<br/>fun [seek](seek.md)(positionMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Seek to position in milliseconds |
| [setPlaybackListener](set-playback-listener.md) | [common]<br/>fun [setPlaybackListener](set-playback-listener.md)(listener: [SonixPlayer.PlaybackListener](-playback-listener/index.md)?)<br/>Set a listener for playback events. |
| [setProcessingTap](set-processing-tap.md) | [common]<br/>fun [setProcessingTap](set-processing-tap.md)(callback: ([FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)?)<br/>Installs a processing tap to receive and modify audio buffers during playback. |
| [setVolumeSmooth](set-volume-smooth.md) | [common]<br/>suspend fun [setVolumeSmooth](set-volume-smooth.md)(targetVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), easing: [VolumeEasing](../../com.musicmuni.voxatrace.sonix.player/-volume-easing/index.md) = VolumeEasing.Linear)<br/>Smoothly transition volume with easing curve. |
| [stop](stop.md) | [common]<br/>fun [stop](stop.md)()<br/>Stop playback and reset to beginning |
