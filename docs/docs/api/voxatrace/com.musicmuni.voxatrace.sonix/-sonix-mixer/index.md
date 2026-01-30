//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixMixer](index.md)

# SonixMixer

class [SonixMixer](index.md)

Multi-track audio mixer with synchronized playback.

## Quick Start

### Kotlin

```kotlin
val mixer = SonixMixer.create()
mixer.addTrack("backing", "/path/to/backing.mp3")  // Auto-decodes
mixer.addTrack("vocal", "/path/to/vocal.mp3")
mixer.play()

// Control individual tracks
mixer.setTrackVolume("vocal", 0.5f)  // Reduce vocal volume
mixer.fadeTrackVolume("vocal", 0f, 2000)  // Fade out over 2 seconds

// Release when done
mixer.release()
```

### Swift

```swift
let mixer = SonixMixer.companion.create(
    config: SonixMixerConfig.companion.DEFAULT,
    audioSession: AudioMode.playback
)
await mixer.addTrack(name: "backing", filePath: "/path/to/backing.mp3")
await mixer.addTrack(name: "vocal", filePath: "/path/to/vocal.mp3")
mixer.play()

// Control individual tracks
mixer.setTrackVolume(name: "vocal", volume: 0.5)
mixer.fadeTrackVolume(name: "vocal", targetVolume: 0, durationMs: 2000)

// Release when done
mixer.release()
```

## Builder Pattern (Advanced)

### Kotlin

```kotlin
val config = SonixMixerConfig.Builder()
    .loopCount(3)
    .onPlaybackComplete { println("All loops done!") }
    .onLoopComplete { index -> println("Completed loop $index") }
    .build()
val mixer = SonixMixer.create(config)
```

## StateFlows

| StateFlow | Type | Description |
|---|---|---|
| `currentTime` | Long | Current playback time in ms |
| `isPlaying` | Boolean | True while playing |
| `error` | SonixError? | Error state |

## Platform Notes

### iOS

- 
   Uses AVAudioEngine for synchronized playback
- 
   Automatically configures audio session

### Android

- 
   Uses AudioTrack with synchronized playback
- 
   Automatically configures audio focus

## Common Pitfalls

1. 
   **addTrack is suspend**: Call from coroutine or use `addTrack(name, data, rate, channels)`
2. 
   **Forgetting to release**: Call `release()` when done
3. 
   **Track names must be unique**: Adding a track with same name replaces it
4. 
   **Different sample rates**: Tracks are automatically resampled to match

#### See also

| | |
|---|---|
| [SonixPlayer](../-sonix-player/index.md) | For single-file playback |
| [SonixMetronome](../-sonix-metronome/index.md) | For click track playback |
| [SonixMixerConfig](../-sonix-mixer-config/index.md) | For configuration options |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |
| [PlaybackListener](-playback-listener/index.md) | [common]<br/>interface [PlaybackListener](-playback-listener/index.md)<br/>Listener interface for playback events. |

## Properties

| Name | Summary |
|---|---|
| [completedLoops](completed-loops.md) | [common]<br/>val [completedLoops](completed-loops.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of completed loop iterations. |
| [currentTime](current-time.md) | [common]<br/>val [currentTime](current-time.md): StateFlow&lt;[Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)&gt;<br/>Current playback time in milliseconds. |
| [duration](duration.md) | [common]<br/>val [duration](duration.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br/>Total duration in milliseconds (based on longest track). |
| [error](error.md) | [common]<br/>val [error](error.md): StateFlow&lt;[SonixError](../-sonix-error/index.md)?&gt;<br/>Error state, null if no error. |
| [isPlaying](is-playing.md) | [common]<br/>val [isPlaying](is-playing.md): StateFlow&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt;<br/>Whether playback is currently active. |
| [loopCount](loop-count.md) | [common]<br/>var [loopCount](loop-count.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Current loop count setting. |

## Functions

| Name | Summary |
|---|---|
| [addTrack](add-track.md) | [common]<br/>suspend fun [addTrack](add-track.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), filePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Add track from file path (auto-decodes).<br/>[common]<br/>fun [addTrack](add-track.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Add track from raw PCM data (for advanced use cases). |
| [fadeTrackVolume](fade-track-volume.md) | [common]<br/>fun [fadeTrackVolume](fade-track-volume.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), targetVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Fade a track's volume smoothly from current to target.<br/>[common]<br/>fun [fadeTrackVolume](fade-track-volume.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), startVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), endVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Fade a track's volume smoothly from start to end value. |
| [getTrackNames](get-track-names.md) | [common]<br/>fun [getTrackNames](get-track-names.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt;<br/>Get names of all loaded tracks. |
| [hasTrack](has-track.md) | [common]<br/>fun [hasTrack](has-track.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if a track is loaded. |
| [pause](pause.md) | [common]<br/>fun [pause](pause.md)()<br/>Pause playback of all tracks. |
| [play](play.md) | [common]<br/>fun [play](play.md)()<br/>Start playback of all tracks. |
| [release](release.md) | [common]<br/>fun [release](release.md)()<br/>Release all resources. |
| [removeTrack](remove-track.md) | [common]<br/>fun [removeTrack](remove-track.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html))<br/>Remove a track from the mixer. |
| [reset](reset.md) | [common]<br/>fun [reset](reset.md)()<br/>Reset playback position to the beginning without stopping. |
| [seek](seek.md) | [common]<br/>fun [seek](seek.md)(positionMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Seek all tracks to a specific position. |
| [setPlaybackListener](set-playback-listener.md) | [common]<br/>fun [setPlaybackListener](set-playback-listener.md)(listener: [SonixMixer.PlaybackListener](-playback-listener/index.md)?)<br/>Set a listener for playback events. |
| [setTrackVolume](set-track-volume.md) | [common]<br/>fun [setTrackVolume](set-track-volume.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), volume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Set volume for a specific track. |
| [stop](stop.md) | [common]<br/>fun [stop](stop.md)()<br/>Stop playback and reset to beginning. |
