---
sidebar_position: 4
---

# SonixMixer

Multi-track audio mixer that plays multiple audio files synchronized with independent per-track volume control.

## Quick Start

### Kotlin

```kotlin
val mixer = SonixMixer.create()
mixer.addTrack("backing", "/path/to/backing.mp3")
mixer.addTrack("vocal", "/path/to/vocal.mp3")
mixer.play()

// Control individual tracks
mixer.setTrackVolume("vocal", 0.5f)

// Release when done
mixer.release()
```

### Swift

```swift
let mixer = SonixMixer.create()
await mixer.addTrack(name: "backing", filePath: "/path/to/backing.mp3")
await mixer.addTrack(name: "vocal", filePath: "/path/to/vocal.mp3")
mixer.play()

// Control individual tracks
mixer.setTrackVolume(name: "vocal", volume: 0.5)

// Release when done
mixer.release()
```

## Configuration

### Presets

| Preset | Kotlin | Swift | Description |
|--------|--------|-------|-------------|
| Default | `SonixMixerConfig.DEFAULT` | `.default` | Play once |
| Looping | `SonixMixerConfig.LOOPING` | `.looping` | Infinite loop |

### Builder

#### Kotlin

```kotlin
val config = SonixMixerConfig.Builder()
    .loopCount(3)
    .onPlaybackComplete { println("All loops done!") }
    .onLoopComplete { index -> println("Completed loop $index") }
    .onError { error -> showError(error) }
    .build()

val mixer = SonixMixer.create(config)
```

#### Swift

```swift
let config = SonixMixerConfig.Builder()
    .loopCount(3)
    .onPlaybackComplete { print("All loops done!") }
    .onLoopComplete { index in print("Completed loop \(index)") }
    .onError { error in print("Error: \(error)") }
    .build()

let mixer = SonixMixer.create(config: config)
```

### Config Properties

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `loopCount` | `Int` | `1` | Times to play (1 = once, -1 = infinite) |

### Callbacks

| Builder Method | Signature | Description |
|---------------|-----------|-------------|
| `onPlaybackComplete` | `() -> Unit` | All loops completed |
| `onLoopComplete` | `(loopIndex: Int) -> Unit` | Single loop iteration completed |
| `onError` | `(error: String) -> Unit` | Playback error occurred |

## Track Management

### Add Tracks

#### From File (auto-decodes)

```kotlin
// Suspending — auto-decodes MP3, M4A, WAV, etc.
val success = mixer.addTrack("backing", "/path/to/backing.mp3")
```

```swift
let success = await mixer.addTrack(name: "backing", filePath: "/path/to/backing.mp3")
```

#### From Raw PCM Data

```kotlin
val pcmData: ByteArray = ...
mixer.addTrack("synth", pcmData, sampleRate = 44100, channels = 1)
```

### Query and Remove Tracks

```kotlin
val names: List<String> = mixer.getTrackNames()
val exists: Boolean = mixer.hasTrack("backing")
mixer.removeTrack("vocal")
```

## Playback Controls

```kotlin
mixer.play()                   // Start all tracks synchronized
mixer.pause()                  // Pause all tracks
mixer.stop()                   // Stop and reset to beginning
mixer.reset()                  // Reset position without stopping
mixer.seek(positionMs = 30000) // Seek all tracks to 30 seconds
```

## Per-Track Volume

```kotlin
// Set immediately
mixer.setTrackVolume("vocal", 0.3f)

// Fade from current volume to target
mixer.fadeTrackVolume("vocal", targetVolume = 0f, durationMs = 2000)

// Fade between specific volumes
mixer.fadeTrackVolume("vocal", startVolume = 1f, endVolume = 0f, durationMs = 2000)
```

```swift
mixer.setTrackVolume(name: "vocal", volume: 0.3)
mixer.fadeTrackVolume(name: "vocal", targetVolume: 0, durationMs: 2000)
mixer.fadeTrackVolume(name: "vocal", startVolume: 1, endVolume: 0, durationMs: 2000)
```

## Looping

```kotlin
// Set at creation
val mixer = SonixMixer.create(SonixMixerConfig.LOOPING)

// Via builder
val config = SonixMixerConfig.Builder()
    .loopForever()
    .build()

// Change at runtime
mixer.loopCount = 3    // Play 3 times
mixer.loopCount = -1   // Infinite

// Check progress
val completed = mixer.completedLoops
```

## Observing State

### Kotlin (StateFlow)

```kotlin
mixer.isPlaying.collect { playing ->
    playButton.icon = if (playing) pauseIcon else playIcon
}

mixer.currentTime.collect { timeMs ->
    seekBar.progress = timeMs.toInt()
}

mixer.error.collect { error ->
    error?.let { showError(it.message) }
}

// Duration (not a flow)
val totalMs = mixer.duration
```

### Swift (Observers)

```swift
let playingTask = mixer.observeIsPlaying { isPlaying in
    self.isPlaying = isPlaying
}

let timeTask = mixer.observeCurrentTime { timeMs in
    self.currentTimeMs = timeMs
}

let errorTask = mixer.observeError { error in
    if let error = error { self.showError(error.message) }
}
```

### Swift (Combine)

```swift
mixer.isPlayingPublisher
    .receive(on: DispatchQueue.main)
    .sink { self.isPlaying = $0 }
    .store(in: &cancellables)
```

### StateFlows

| StateFlow | Type | Description |
|-----------|------|-------------|
| `currentTime` | `StateFlow<Long>` | Playback position in milliseconds |
| `isPlaying` | `StateFlow<Boolean>` | Whether currently playing |
| `error` | `StateFlow<SonixError?>` | Error state |

### Properties

| Property | Type | Description |
|----------|------|-------------|
| `duration` | `Long` | Total duration in ms (longest track) |
| `completedLoops` | `Int` | Number of completed loop iterations |
| `loopCount` | `Int` | Current loop count setting (mutable) |

## Listener Interface

```kotlin
mixer.setPlaybackListener(object : SonixMixer.PlaybackListener {
    override fun onPlaybackStarted(startTimeMs: Long) { println("Started at $startTimeMs") }
    override fun onPlaybackPaused(playbackTimeMs: Long) { println("Paused at $playbackTimeMs") }
    override fun onPlaybackCompleted() { println("All done!") }
    override fun onLoopCompleted(loopIndex: Int) { println("Loop $loopIndex done") }
    override fun onError(error: String) { showError(error) }
})
```

## Common Patterns

### Karaoke Mixer ViewModel

```kotlin
class MultiTrackViewModel : ViewModel() {
    private var mixer: SonixMixer? = null

    fun loadTracks(backingPath: String, vocalPath: String) {
        val config = SonixMixerConfig.Builder()
            .onPlaybackComplete { /* show results */ }
            .build()

        mixer = SonixMixer.create(config)

        viewModelScope.launch {
            mixer!!.addTrack("backing", backingPath)
            mixer!!.addTrack("vocal", vocalPath)
            mixer!!.setTrackVolume("vocal", 0.3f)
        }
    }

    fun playPause() {
        val m = mixer ?: return
        if (m.isPlaying.value) m.pause() else m.play()
    }

    fun toggleVocal(enabled: Boolean) {
        mixer?.fadeTrackVolume("vocal",
            targetVolume = if (enabled) 0.3f else 0f,
            durationMs = 500
        )
    }

    override fun onCleared() {
        mixer?.release()
    }
}
```

## Next Steps

- [SonixPlayer](./player) — Single-file playback
- [SonixMetronome](./metronome) — Click track for practice
- [SonixRecorder](./recorder) — Record audio alongside mixer playback
