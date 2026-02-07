---
sidebar_position: 2
---

# SonixPlayer

Play audio files with real-time pitch shifting, tempo control, volume management, and looping.

## Quick Start

### Kotlin

```kotlin
val player = SonixPlayer.create("path/to/song.mp3")
player.play()

// Later...
player.pause()
player.release()
```

### Swift

```swift
let player = try await SonixPlayer.create(source: "path/to/song.mp3")
player.play()

// Later...
player.pause()
player.release()
```

## Configuration

### Presets

| Preset | Kotlin | Swift | Description |
|--------|--------|-------|-------------|
| Default | `SonixPlayerConfig.DEFAULT` | `.default` | Play once at normal volume |
| Looping | `SonixPlayerConfig.LOOPING` | `.looping` | Infinite loop at normal volume |

### Builder

#### Kotlin

```kotlin
val config = SonixPlayerConfig.Builder()
    .preset(SonixPlayerConfig.LOOPING)
    .volume(0.8f)
    .pitch(-2f)
    .tempo(0.75f)
    .onComplete { println("Done!") }
    .build()

val player = SonixPlayer.create("song.mp3", config)
```

#### Swift

```swift
let config = SonixPlayerConfig.Builder()
    .preset(.looping)
    .volume(0.8)
    .pitch(-2)
    .tempo(0.75)
    .onComplete { print("Done!") }
    .build()

let player = try await SonixPlayer.create(source: "song.mp3", config: config)
```

### Config Properties

| Property | Type | Default | Range | Description |
|----------|------|---------|-------|-------------|
| `volume` | `Float` | `1.0` | 0.0 – 1.0 | Initial playback volume |
| `pitch` | `Float` | `0.0` | -12 – +12 | Pitch shift in semitones |
| `tempo` | `Float` | `1.0` | 0.25 – 4.0 | Speed multiplier |
| `loopCount` | `Int` | `1` | 1+ or -1 | Times to play (-1 = infinite) |

### Callbacks

| Builder Method | Kotlin Signature | Swift Signature |
|---------------|-----------------|-----------------|
| `onComplete` | `() -> Unit` | `() -> Void` |
| `onLoopComplete` | `(loopIndex: Int, totalLoops: Int) -> Unit` | `(Int32, Int32) -> Void` |
| `onPlaybackStateChanged` | `(isPlaying: Boolean) -> Unit` | `(Bool) -> Void` |
| `onError` | `(message: String) -> Unit` | `(String) -> Void` |

## Creating a Player

### From File Path

```kotlin
val player = SonixPlayer.create("path/to/song.mp3")
```

```swift
let player = try await SonixPlayer.create(source: "path/to/song.mp3")
```

### From Raw PCM Data

#### Kotlin

```kotlin
val pcmData: ByteArray = ...
val player = SonixPlayer.createFromPcm(
    data = pcmData,
    sampleRate = 44100,
    channels = 1
)
```

#### Swift

```swift
let pcmData: Data = ...
let player = SonixPlayer.createFromPcm(
    data: pcmData,
    sampleRate: 44100,
    channels: 1
)
```

### Factory Methods

| Method | Parameters | Description |
|--------|-----------|-------------|
| `create` | `source`, `config`, `audioSession` | Create from file path (suspending) |
| `createFromPcm` | `data`, `sampleRate`, `channels`, `config`, `audioSession` | Create from raw PCM bytes |

## Playback Controls

```kotlin
player.play()              // Start or resume
player.pause()             // Pause playback
player.stop()              // Stop and reset to beginning
player.seek(positionMs)    // Seek to position in milliseconds
player.release()           // Release all resources
```

## Pitch and Tempo

### Runtime Control

```kotlin
player.pitch = -2f    // 2 semitones down (lower key)
player.pitch = 0f     // Original key
player.pitch = 3f     // 3 semitones up (higher key)

player.tempo = 0.5f   // Half speed
player.tempo = 1.0f   // Normal speed
player.tempo = 2.0f   // Double speed
```

| Property | Range | Description |
|----------|-------|-------------|
| `pitch` | -12 to +12 | Semitones shift without affecting speed |
| `tempo` | 0.25 to 4.0 | Speed multiplier without affecting pitch |

## Volume and Fading

```kotlin
player.volume = 0.5f   // Set to 50%

// Fade in from silence
player.volume = 0f
player.play()
player.fadeIn(targetVolume = 1f, durationMs = 500)

// Fade out before stopping
player.fadeOut(durationMs = 500)
player.pause()

// Smooth transition with easing
player.setVolumeSmooth(
    targetVolume = 0.5f,
    durationMs = 300,
    easing = VolumeEasing.EaseInOut
)
```

| Method | Parameters | Description |
|--------|-----------|-------------|
| `fadeIn` | `targetVolume: Float = 1f`, `durationMs: Long = 500` | Fade in (suspending) |
| `fadeOut` | `durationMs: Long = 500` | Fade out to silence (suspending) |
| `setVolumeSmooth` | `targetVolume`, `durationMs`, `easing` | Volume transition with easing (suspending) |

## Looping

```kotlin
// Fixed loop count
val config = SonixPlayerConfig.Builder()
    .loopCount(3)  // Play 3 times total
    .onLoopComplete { loopIndex, total ->
        println("Completed loop ${loopIndex + 1} of $total")
    }
    .build()

// Infinite loop via preset
val player = SonixPlayer.create("song.mp3", SonixPlayerConfig.LOOPING)

// Infinite loop via builder
val config = SonixPlayerConfig.Builder()
    .loopForever()
    .build()

// Change at runtime
player.loopCount = -1  // Infinite
player.loopCount = 5   // Play 5 times
```

## Observing State

### Kotlin (StateFlow)

```kotlin
// Current time (for seek bar)
player.currentTime.collect { timeMs ->
    seekBar.progress = timeMs.toInt()
}

// Playing state (for play/pause button)
player.isPlaying.collect { playing ->
    playButton.icon = if (playing) pauseIcon else playIcon
}

// Errors
player.error.collect { error ->
    error?.let { showError(it.message) }
}

// Duration (not a flow - read directly)
val totalMs = player.duration
```

### Swift (Observers)

```swift
let playingTask = player.observeIsPlaying { isPlaying in
    self.isPlaying = isPlaying
}

let timeTask = player.observeCurrentTime { timeMs in
    self.currentTimeMs = timeMs
}

let errorTask = player.observeError { error in
    if let error = error {
        self.showError(error.message)
    }
}

// Cancel when done
playingTask.cancel()
timeTask.cancel()
errorTask.cancel()
```

### Swift (Combine)

```swift
player.isPlayingPublisher
    .receive(on: DispatchQueue.main)
    .sink { isPlaying in
        self.isPlaying = isPlaying
    }
    .store(in: &cancellables)

player.currentTimePublisher
    .receive(on: DispatchQueue.main)
    .sink { timeMs in
        self.currentTimeMs = timeMs
    }
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
| `duration` | `Long` | Total duration in milliseconds |
| `asPlaybackInfoProvider` | `PlaybackInfoProvider` | For recording sync |

## Processing Tap

Access and modify audio buffers during playback for real-time effects:

### Kotlin

```kotlin
player.setProcessingTap { buffer ->
    // buffer is FloatArray — modify in-place
    for (i in buffer.indices) {
        buffer[i] *= 0.5f  // Reduce volume by half
    }
}

// Remove tap
player.setProcessingTap(null)
```

### Swift

```swift
player.setProcessingTap { samples in
    // samples is inout [Float] — modify in-place
    for i in samples.indices {
        samples[i] *= 0.5
    }
}

// Remove tap
player.setProcessingTap(nil)
```

## Listener Interface

Alternative to StateFlow observation and Builder callbacks:

```kotlin
player.setPlaybackListener(object : SonixPlayer.PlaybackListener {
    override fun onPlaybackCompleted() {
        println("Done!")
    }
    override fun onLoopCompleted(loopIndex: Int, totalLoops: Int) {
        println("Loop $loopIndex of $totalLoops")
    }
    override fun onPlaybackStateChanged(isPlaying: Boolean) {
        updatePlayButton(isPlaying)
    }
    override fun onError(message: String) {
        showError(message)
    }
})
```

## Common Patterns

### Music Player ViewModel

```kotlin
class MusicPlayerViewModel : ViewModel() {
    private var player: SonixPlayer? = null

    fun load(path: String) {
        viewModelScope.launch {
            player?.release()
            player = SonixPlayer.create(path)
        }
    }

    fun playPause() {
        val p = player ?: return
        if (p.isPlaying.value) p.pause() else p.play()
    }

    fun seek(positionMs: Long) {
        player?.seek(positionMs)
    }

    override fun onCleared() {
        player?.release()
    }
}
```

## Next Steps

- [SonixRecorder](./recorder) — Record audio
- [SonixMixer](./mixer) — Multi-track mixing
- [Playing Audio Guide](../guides/playing-audio) — In-depth playback guide
