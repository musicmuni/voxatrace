---
sidebar_position: 1
---

# Playing Audio

A complete guide to audio playback with SonixPlayer.

## What You'll Learn

- Load and play audio files
- Control pitch, tempo, and volume
- Handle looping and seeking
- React to playback events
- Use processing taps for real-time effects

## Prerequisites

- VoxaTrace installed
- Audio file accessible (in app bundle or filesystem)

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
let player = try await SonixPlayer.companion.create(source: "path/to/song.mp3")
player.play()

// Later...
player.pause()
player.release()
```

## Loading Audio

### From File Path

```kotlin
// Absolute path
val player = SonixPlayer.create("/storage/emulated/0/Music/song.mp3")

// Relative to app data directory
val player = SonixPlayer.create("${context.filesDir}/song.mp3")
```

### From Assets (Android)

```kotlin
// Copy asset to cache first, then load
val assetPath = copyAssetToCache("songs/song.mp3")
val player = SonixPlayer.create(assetPath)
```

### From Bundle (iOS)

```swift
if let url = Bundle.main.url(forResource: "song", withExtension: "mp3") {
    let player = try await SonixPlayer.companion.create(source: url.path)
}
```

### From Raw PCM Data

```kotlin
val pcmData: ByteArray = ...
val player = SonixPlayer.createFromPcm(
    data = pcmData,
    sampleRate = 44100,
    channels = 1
)
```

## Playback Controls

### Basic Controls

```kotlin
player.play()    // Start or resume
player.pause()   // Pause playback
player.stop()    // Stop and reset to beginning
```

### Seeking

```kotlin
// Seek to 30 seconds
player.seek(30_000)  // milliseconds

// Get current position
player.currentTime.collect { timeMs ->
    updateSeekBar(timeMs)
}

// Get total duration
val totalMs = player.duration
```

## Pitch and Tempo

### Pitch Shifting

Change the key without affecting speed:

```kotlin
player.pitch = -2f   // 2 semitones down (lower key)
player.pitch = 0f    // Original key
player.pitch = 3f    // 3 semitones up (higher key)
```

Range: -12 to +12 semitones (one octave each direction).

### Tempo Control

Change the speed without affecting pitch:

```kotlin
player.tempo = 0.5f   // Half speed (slow practice)
player.tempo = 1.0f   // Normal speed
player.tempo = 1.25f  // 25% faster
player.tempo = 2.0f   // Double speed
```

Range: 0.25x to 4.0x.

### Combine Both

```kotlin
// Transpose to user's key AND slow down for practice
player.pitch = userKeyOffset
player.tempo = 0.75f
```

## Volume and Fading

### Set Volume

```kotlin
player.volume = 0.5f  // 50% volume
player.volume = 1.0f  // Full volume
player.volume = 0.0f  // Muted
```

### Fade In/Out

```kotlin
// Start silent, then fade in
player.volume = 0f
player.play()
player.fadeIn(targetVolume = 1f, durationMs = 500)

// Before stopping, fade out
player.fadeOut(durationMs = 500)
player.pause()
```

### Smooth Volume Transitions

```kotlin
// Smoothly transition with easing
player.setVolumeSmooth(
    targetVolume = 0.5f,
    durationMs = 300,
    easing = VolumeEasing.EaseInOut
)
```

## Looping

### Fixed Loop Count

```kotlin
val config = SonixPlayerConfig.Builder()
    .loopCount(3)  // Play 3 times total
    .onLoopComplete { loopIndex, total ->
        println("Completed loop ${loopIndex + 1} of $total")
    }
    .build()

val player = SonixPlayer.create("song.mp3", config)
```

### Infinite Loop

```kotlin
// Option 1: Preset
val player = SonixPlayer.create("song.mp3", SonixPlayerConfig.LOOPING)

// Option 2: Builder
val config = SonixPlayerConfig.Builder()
    .loopForever()
    .build()

// Option 3: Runtime
player.loopCount = -1  // -1 means infinite
```

## Observing State

### StateFlow (Reactive)

```kotlin
// Current time (for seek bar)
player.currentTime.collect { timeMs ->
    seekBar.progress = timeMs.toInt()
    timeLabel.text = formatTime(timeMs)
}

// Playing state (for play/pause button)
player.isPlaying.collect { playing ->
    playButton.icon = if (playing) pauseIcon else playIcon
}

// Errors
player.error.collect { error ->
    error?.let { showError(it.message) }
}
```

### Callbacks (Event-based)

```kotlin
val config = SonixPlayerConfig.Builder()
    .onComplete { println("Playback finished") }
    .onLoopComplete { loop, total -> println("Loop $loop/$total") }
    .onPlaybackStateChanged { playing -> updateUI(playing) }
    .onError { message -> showError(message) }
    .build()
```

### Listener Interface

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

## Processing Tap

Access audio buffers during playback for real-time processing:

```kotlin
// Apply effects during playback
val effects = CalibraEffects.create(EffectsPreset.VOCAL_CHAIN)

player.setProcessingTap { buffer ->
    effects.process(buffer)  // Modifies buffer in-place
}

player.play()

// Remove tap when done
player.setProcessingTap(null)
effects.release()
```

Use cases:
- Apply reverb/compression to backing tracks
- Visualize audio waveform
- Extract features for analysis

## Integration with Recording

Sync recording with playback for karaoke:

```kotlin
val player = SonixPlayer.create("backing-track.mp3")
val recorder = SonixRecorder.create(
    "recording.m4a",
    SonixRecorderConfig.Builder()
        .playbackSyncProvider(player.asPlaybackInfoProvider)
        .build()
)

// Start synchronized
player.play()
recorder.start()

// Recording timestamps align with playback position
```

## Platform Considerations

### iOS

```swift
// Audio session is automatically configured
// Supports: M4A, MP3, WAV, AAC, and other system formats
```

### Android

```kotlin
// ExoPlayer is used internally
// Supports: M4A, MP3, WAV, OGG, FLAC, and other ExoPlayer formats
```

### Audio Formats

| Format | iOS | Android | Notes |
|--------|-----|---------|-------|
| M4A/AAC | Yes | Yes | Recommended for compression |
| MP3 | Yes | Yes | Universal compatibility |
| WAV | Yes | Yes | Uncompressed, large files |
| OGG | No | Yes | Android only |
| FLAC | Yes | Yes | Lossless compression |

## Common Patterns

### Music Player

```kotlin
class MusicPlayerViewModel {
    private var player: SonixPlayer? = null

    suspend fun load(path: String) {
        player?.release()
        player = SonixPlayer.create(path)
    }

    fun playPause() {
        val p = player ?: return
        if (p.isPlaying.value) p.pause() else p.play()
    }

    fun seek(positionMs: Long) {
        player?.seek(positionMs)
    }

    fun cleanup() {
        player?.release()
        player = null
    }
}
```

### Karaoke Player

```kotlin
class KaraokePlayer(
    private val backingTrack: String,
    private val vocalGuide: String
) {
    private var backing: SonixPlayer? = null
    private var guide: SonixPlayer? = null

    suspend fun prepare() {
        backing = SonixPlayer.create(backingTrack)
        guide = SonixPlayer.create(vocalGuide)
    }

    fun play(withGuide: Boolean) {
        backing?.play()
        if (withGuide) {
            guide?.volume = 0.3f
            guide?.play()
        }
    }

    fun setKey(semitones: Float) {
        backing?.pitch = semitones
        guide?.pitch = semitones
    }

    fun setTempo(factor: Float) {
        backing?.tempo = factor
        guide?.tempo = factor
    }

    fun toggleGuide(enabled: Boolean) {
        guide?.volume = if (enabled) 0.3f else 0f
    }

    fun release() {
        backing?.release()
        guide?.release()
    }
}
```

## Next Steps

- [Recording Audio Guide](./recording-audio) - Capture audio
- [Karaoke App Recipe](../cookbook/karaoke-app) - Complete example
