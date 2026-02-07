---
sidebar_position: 5
---

# SonixMetronome

Programmable metronome with distinct downbeat and beat sounds, configurable BPM, and visual beat tracking.

## Quick Start

### Kotlin

```kotlin
val metronome = SonixMetronome.create(
    samaSamplePath = "/path/to/sama.wav",
    beatSamplePath = "/path/to/beat.wav"
)

// Wait for samples to load, then start
metronome.isInitialized.collect { ready ->
    if (ready) metronome.start()
}

// Later...
metronome.stop()
metronome.release()
```

### Swift

```swift
let metronome = SonixMetronome.create(
    samaSamplePath: "/path/to/sama.wav",
    beatSamplePath: "/path/to/beat.wav"
)

// Wait for samples to load, then start
let initTask = metronome.observeIsInitialized { ready in
    if ready { metronome.start() }
}

// Later...
metronome.stop()
metronome.release()
```

## Configuration

### Factory Method

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `samaSamplePath` | `String` | — | Path to downbeat audio sample (required) |
| `beatSamplePath` | `String` | — | Path to regular beat audio sample (required) |
| `bpm` | `Float` | `120` | Initial tempo (30–300) |
| `beatsPerCycle` | `Int` | `4` | Beats per cycle |

### Builder

For advanced configuration with callbacks:

#### Kotlin

```kotlin
val metronome = SonixMetronome.Builder()
    .samaSamplePath("/path/to/sama.wav")
    .beatSamplePath("/path/to/beat.wav")
    .bpm(120f)
    .beatsPerCycle(4)
    .volume(0.8f)
    .onBeat { beatIndex -> updateBeatIndicator(beatIndex) }
    .onError { error -> showError(error) }
    .build()
```

#### Swift

```swift
let metronome = SonixMetronome.Builder()
    .samaSamplePath(path: "/path/to/sama.wav")
    .beatSamplePath(path: "/path/to/beat.wav")
    .bpm(bpm: 120)
    .beatsPerCycle(count: 4)
    .volume(volume: 0.8)
    .build()
```

### Builder Parameters

| Method | Type | Default | Description |
|--------|------|---------|-------------|
| `samaSamplePath` | `String` | — | Downbeat audio path (required) |
| `beatSamplePath` | `String` | — | Beat audio path (required) |
| `bpm` | `Float` | `120` | Tempo (clamped 30–300) |
| `beatsPerCycle` | `Int` | `4` | Beats per cycle |
| `volume` | `Float` | `0.8` | Initial volume (clamped 0–1) |
| `onBeat` | `(beatIndex: Int) -> Unit` | — | Called on each beat |
| `onError` | `(error: String) -> Unit` | — | Called on error |

## Playback Controls

```kotlin
metronome.start()         // Start (only works when isInitialized is true)
metronome.stop()          // Stop
metronome.release()       // Release all resources
```

## Runtime Adjustments

```kotlin
metronome.setBpm(140f)     // Change tempo (30–300)
metronome.volume = 0.5f    // Change volume (0.0–1.0)
```

```swift
metronome.setBpm(bpm: 140)
metronome.volume = 0.5
```

| Property/Method | Range | Notes |
|----------------|-------|-------|
| `setBpm(bpm)` | 30–300 | Takes effect on next beat |
| `volume` | 0.0–1.0 | Immediate effect |
| `beatsPerCycle` | — | Read-only; set at creation time |

## Observing State

### Kotlin (StateFlow)

```kotlin
metronome.isInitialized.collect { ready ->
    startButton.isEnabled = ready
}

metronome.isPlaying.collect { playing ->
    playButton.icon = if (playing) stopIcon else playIcon
}

metronome.currentBeat.collect { beat ->
    highlightBeat(beat)  // 0-based index
}

metronome.bpm.collect { currentBpm ->
    bpmLabel.text = "${currentBpm.toInt()} BPM"
}

metronome.error.collect { error ->
    error?.let { showError(it.message) }
}
```

### Swift (Observers)

```swift
let beatTask = metronome.observeCurrentBeat { beat in
    self.currentBeat = beat
}

let playingTask = metronome.observeIsPlaying { isPlaying in
    self.isPlaying = isPlaying
}

let initTask = metronome.observeIsInitialized { ready in
    self.isReady = ready
}

let bpmTask = metronome.observeBpm { bpm in
    self.currentBpm = bpm
}

let errorTask = metronome.observeError { error in
    if let error = error { self.showError(error.message) }
}
```

### Swift (Combine)

```swift
metronome.currentBeatPublisher
    .receive(on: DispatchQueue.main)
    .sink { self.currentBeat = $0 }
    .store(in: &cancellables)

metronome.isPlayingPublisher
    .receive(on: DispatchQueue.main)
    .sink { self.isPlaying = $0 }
    .store(in: &cancellables)
```

### StateFlows

| StateFlow | Type | Description |
|-----------|------|-------------|
| `currentBeat` | `StateFlow<Int>` | Current beat index (0-based, wraps at beatsPerCycle) |
| `isPlaying` | `StateFlow<Boolean>` | Whether metronome is running |
| `bpm` | `StateFlow<Float>` | Current tempo |
| `isInitialized` | `StateFlow<Boolean>` | Whether samples are loaded and ready |
| `error` | `StateFlow<SonixError?>` | Error state |

### Properties

| Property | Type | Description |
|----------|------|-------------|
| `beatsPerCycle` | `Int` | Number of beats per cycle (read-only) |
| `volume` | `Float` | Current volume (0.0–1.0, mutable) |

## Common Patterns

### Metronome ViewModel

```kotlin
class MetronomeViewModel : ViewModel() {
    private var metronome: SonixMetronome? = null

    val currentBeat = MutableStateFlow(0)
    val isPlaying = MutableStateFlow(false)
    val isReady = MutableStateFlow(false)

    fun initialize(samaPath: String, beatPath: String) {
        metronome = SonixMetronome.Builder()
            .samaSamplePath(samaPath)
            .beatSamplePath(beatPath)
            .bpm(120f)
            .beatsPerCycle(4)
            .onBeat { beat -> currentBeat.value = beat }
            .build()

        viewModelScope.launch {
            metronome!!.isInitialized.collect { isReady.value = it }
        }
        viewModelScope.launch {
            metronome!!.isPlaying.collect { isPlaying.value = it }
        }
    }

    fun togglePlayback() {
        val m = metronome ?: return
        if (m.isPlaying.value) m.stop() else m.start()
    }

    fun setBpm(bpm: Float) {
        metronome?.setBpm(bpm)
    }

    override fun onCleared() {
        metronome?.release()
    }
}
```

## Next Steps

- [SonixPlayer](./player) — Play audio files
- [SonixMixer](./mixer) — Multi-track mixing
- [SonixRecorder](./recorder) — Record alongside the metronome
