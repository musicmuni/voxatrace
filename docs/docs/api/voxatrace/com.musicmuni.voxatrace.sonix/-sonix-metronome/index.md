//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixMetronome](index.md)

# SonixMetronome

class [SonixMetronome](index.md)

Metronome for practice mode with click track playback.

## What is SonixMetronome?

A metronome provides a steady **click track** to help musicians keep time. SonixMetronome supports:

- 
   Configurable **BPM** (beats per minute)
- 
   Distinct **sama** (downbeat) and regular **beat** sounds
- 
   **Talam** support with configurable beats per cycle
- 
   Runtime tempo and volume adjustments

## When to Use

| Scenario | Use This? | Why |
|---|---|---|
| Practice mode timing | Yes | Core use case |
| Sync UI to beat | Yes | Use `onBeat` callback |
| Play backing track | No | Use `SonixPlayer` or `SonixMixer` |
| Record with metronome | Yes | Combine with `SonixRecorder` |

## Quick Start

### Kotlin

```kotlin
// Create metronome with default settings
val metronome = SonixMetronome.create(
    samaSamplePath = "/path/to/sama.wav",
    beatSamplePath = "/path/to/beat.wav"
)

// Wait for samples to load
metronome.isInitialized.collect { ready ->
    if (ready) metronome.start()
}

// Control playback
metronome.stop()
metronome.setBpm(140f)
metronome.volume = 0.5f

// Release when done
metronome.release()
```

### Swift

```swift
// Create metronome with default settings
let metronome = SonixMetronome.companion.create(
    samaSamplePath: "/path/to/sama.wav",
    beatSamplePath: "/path/to/beat.wav",
    bpm: 120,
    beatsPerCycle: 4
)

// Observe initialization
for await ready in metronome.isInitialized {
    if ready { metronome.start() }
}

// Control playback
metronome.stop()
metronome.setBpm(bpm: 140)
metronome.volume = 0.5

// Release when done
metronome.release()
```

## Builder Pattern (Advanced)

### Kotlin

```kotlin
val metronome = SonixMetronome.Builder()
    .samaSamplePath("/path/to/sama.wav")
    .beatSamplePath("/path/to/beat.wav")
    .bpm(120f)
    .beatsPerCycle(4)
    .volume(0.8f)
    .onBeat { beatIndex -> updateUI(beatIndex) }
    .onError { error -> showError(error) }
    .build()
```

### Swift

```swift
let metronome = SonixMetronome.Builder()
    .samaSamplePath(path: "/path/to/sama.wav")
    .beatSamplePath(path: "/path/to/beat.wav")
    .bpm(bpm: 120)
    .beatsPerCycle(count: 4)
    .volume(volume: 0.8)
    .build()
```

## StateFlows

| StateFlow | Type | Description |
|---|---|---|
| `isInitialized` | Boolean | True when samples are loaded |
| `isPlaying` | Boolean | True while metronome is running |
| `currentBeat` | Int | Current beat index (0-based) |
| `bpm` | Float | Current tempo |
| `error` | SonixError? | Error state, null if no error |

## Platform Notes

### iOS

- 
   Uses AVAudioEngine for low-latency playback
- 
   Audio samples must be WAV format

### Android

- 
   Uses OpenSL ES or AAudio for low-latency playback
- 
   Audio samples must be WAV format

## Common Pitfalls

1. 
   **Starting before initialized**: Wait for `isInitialized` to be true
2. 
   **Forgetting to release**: Call `release()` when done to free resources
3. 
   **Invalid sample paths**: Ensure audio files exist at specified paths
4. 
   **BPM range**: Valid range is 30-300 BPM

#### See also

| | |
|---|---|
| [SonixPlayer](../-sonix-player/index.md) | For audio file playback |
| [SonixMixer](../-sonix-mixer/index.md) | For multi-track mixing |

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [common]<br/>class [Builder](-builder/index.md)<br/>Builder for advanced SonixMetronome configuration. |
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [beatsPerCycle](beats-per-cycle.md) | [common]<br/>val [beatsPerCycle](beats-per-cycle.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of beats per cycle (set at creation time). |
| [bpm](bpm.md) | [common]<br/>val [bpm](bpm.md): StateFlow&lt;[Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)&gt;<br/>Current BPM as observable state. |
| [currentBeat](current-beat.md) | [common]<br/>val [currentBeat](current-beat.md): StateFlow&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)&gt;<br/>Current beat index (0-based, wraps at beatsPerCycle). |
| [error](error.md) | [common]<br/>val [error](error.md): StateFlow&lt;[SonixError](../-sonix-error/index.md)?&gt;<br/>Error state, null if no error. |
| [isInitialized](is-initialized.md) | [common]<br/>val [isInitialized](is-initialized.md): StateFlow&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt;<br/>Whether metronome samples are loaded and ready to start. |
| [isPlaying](is-playing.md) | [common]<br/>val [isPlaying](is-playing.md): StateFlow&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt;<br/>Whether metronome is currently running. |
| [volume](volume.md) | [common]<br/>var [volume](volume.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Current volume setting. |

## Functions

| Name | Summary |
|---|---|
| [release](release.md) | [common]<br/>fun [release](release.md)()<br/>Release all resources. |
| [setBpm](set-bpm.md) | [common]<br/>fun [setBpm](set-bpm.md)(bpm: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Set tempo in beats per minute. |
| [start](start.md) | [common]<br/>fun [start](start.md)()<br/>Start the metronome. |
| [stop](stop.md) | [common]<br/>fun [stop](stop.md)()<br/>Stop the metronome. |
