//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.metronome](../index.md)/[MetronomePlayer](index.md)

# MetronomePlayer

interface [MetronomePlayer](index.md)

Metronome audio player for practice mode.

Generates a click track using two audio samples: one for the downbeat (sama) and one for other beats. Supports configurable BPM, beats per cycle, and volume control.

## Usage

```kotlin
val metronome = createMetronomePlayer()

// Initialize with config
metronome.initialize(MetronomePlayer.Config(
    samaSamplePath = "/path/to/sama.wav",
    beatSamplePath = "/path/to/beat.wav",
    bpm = 120f,
    beatsPerCycle = 4,
    volume = 0.8f
))

// Start metronome
metronome.start()

// Observe current beat
metronome.currentBeat.collect { beat ->
    updateBeatIndicator(beat)
}

// Change tempo while playing
metronome.setBpm(140f)

// Stop and cleanup
metronome.stop()
metronome.release()
```

## Platform Implementation

- 
   **Android**: Uses AudioTrack with pre-rendered click buffer
- 
   **iOS**: Uses AVAudioEngine with AVAudioPlayerNode

#### Inheritors

| |
|---|
| AndroidMetronomePlayer |

## Types

| Name | Summary |
|---|---|
| [Config](-config/index.md) | [common]<br/>data class [Config](-config/index.md)(val samaSamplePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val beatSamplePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val bpm: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val beatsPerCycle: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 4, val volume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f)<br/>Configuration for the metronome. |

## Properties

| Name | Summary |
|---|---|
| [bpm](bpm.md) | [common]<br/>abstract val [bpm](bpm.md): StateFlow&lt;[Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)&gt;<br/>Current BPM |
| [currentBeat](current-beat.md) | [common]<br/>abstract val [currentBeat](current-beat.md): StateFlow&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)&gt;<br/>Current beat index (0-based, wraps at beatsPerCycle) |
| [isPlaying](is-playing.md) | [common]<br/>abstract val [isPlaying](is-playing.md): StateFlow&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt;<br/>Whether metronome is playing |

## Functions

| Name | Summary |
|---|---|
| [initialize](initialize.md) | [common]<br/>abstract suspend fun [initialize](initialize.md)(config: [MetronomePlayer.Config](-config/index.md))<br/>Initialize metronome with config. Loads the sample files. |
| [release](release.md) | [common]<br/>abstract fun [release](release.md)()<br/>Release resources. |
| [setBpm](set-bpm.md) | [common]<br/>abstract fun [setBpm](set-bpm.md)(bpm: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Update BPM while playing. |
| [setVolume](set-volume.md) | [common]<br/>abstract fun [setVolume](set-volume.md)(volume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Set volume. |
| [start](start.md) | [common]<br/>abstract fun [start](start.md)()<br/>Start metronome. |
| [stop](stop.md) | [common]<br/>abstract fun [stop](stop.md)()<br/>Stop metronome. |
