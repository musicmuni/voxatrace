//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixLessonSynthesizer](index.md)

# SonixLessonSynthesizer

class [SonixLessonSynthesizer](index.md)

Synthesizes audio lessons from svara (note) sequences.

## What is Lesson Synthesis?

In Indian classical music education, lessons are built from **svaras** (notes like Sa, Re, Ga, Ma, Pa, Dha, Ni). This synthesizer:

- 
   Combines individual svara recordings into a complete lesson
- 
   Applies proper timing based on beat length
- 
   Handles crossfades and loop smoothing
- 
   Adds configurable silence at start/end

## When to Use

| Scenario | Use This? | Why |
|---|---|---|
| Generate lesson audio | Yes | Core use case |
| Create practice tracks | Yes | From svara sequence |
| Synthesize from MIDI | No | Use `SonixMidiSynthesizer` |
| Play existing audio | No | Use `SonixPlayer` |

## Quick Start

### Kotlin

```kotlin
val svaras = listOf(
    LessonSvara(
        svaraName = "Sa",
        svaraLabel = "S",
        svaraAudioFilePath = "/path/to/sa.wav",
        numBeats = 2,
        numSamplesConsonant = 100
    ),
    LessonSvara(
        svaraName = "Re",
        svaraLabel = "R",
        svaraAudioFilePath = "/path/to/re.wav",
        numBeats = 2,
        numSamplesConsonant = 100
    )
)

val synth = SonixLessonSynthesizer.create(
    svaras = svaras,
    beatLengthMs = 500
)

if (synth.loadAudioSync()) {
    val audioData = synth.synthesize()
    // audioData is ready to play or save
}

synth.release()
```

### Swift

```swift
let svaras = [
    LessonSvara(
        svaraName: "Sa",
        svaraLabel: "S",
        svaraAudioFilePath: "/path/to/sa.wav",
        numBeats: 2,
        numSamplesConsonant: 100
    ),
    LessonSvara(
        svaraName: "Re",
        svaraLabel: "R",
        svaraAudioFilePath: "/path/to/re.wav",
        numBeats: 2,
        numSamplesConsonant: 100
    )
]

let synth = SonixLessonSynthesizer.companion.create(
    svaras: svaras,
    beatLengthMs: 500
)

// Load audio (async)
if await synth.loadAudio() {
    if let audioData = synth.synthesize() {
        // audioData is ready to play or save
    }
}

synth.release()
```

## Builder Pattern (Advanced)

### Kotlin

```kotlin
val synth = SonixLessonSynthesizer.Builder()
    .svaras(svaraList)
    .beatLengthMs(500)
    .silenceBeats(start = 2, end = 2)
    .sampleRate(44100)
    .onError { error -> println("Error: $error") }
    .build()
```

## StateFlows

| StateFlow | Type | Description |
|---|---|---|
| `isLoading` | Boolean | True while loading audio files |
| `isLoaded` | Boolean | True when ready to synthesize |
| `error` | SonixError? | Error state |

## Platform Notes

### iOS/Android

- 
   Audio files must be accessible at specified paths
- 
   Loading happens asynchronously (use `loadAudio()` suspend function)
- 
   Synthesis runs synchronously after loading

## Common Pitfalls

1. 
   **Call loadAudio first**: Must load before synthesize
2. 
   **Audio file paths**: Must be absolute paths to valid audio files
3. 
   **Memory usage**: All svara files loaded into memory
4. 
   **Sample rate**: Output defaults to 16kHz (for Calibra compatibility)

#### See also

| | |
|---|---|
| [LessonSvara](../../com.musicmuni.voxatrace.sonix.model/-lesson-svara/index.md) | For svara specification format |
| [SonixMidiSynthesizer](../-sonix-midi-synthesizer/index.md) | For MIDI-based synthesis |
| [AudioRawData](../../com.musicmuni.voxatrace.sonix.model/-audio-raw-data/index.md) | For output format |

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [common]<br/>class [Builder](-builder/index.md)<br/>Builder for advanced SonixLessonSynthesizer configuration. |
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [beatLengthMs](beat-length-ms.md) | [common]<br/>val [beatLengthMs](beat-length-ms.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Beat length in milliseconds. |
| [error](error.md) | [common]<br/>val [error](error.md): StateFlow&lt;[SonixError](../-sonix-error/index.md)?&gt;<br/>Error state, null if no error. |
| [isLoaded](is-loaded.md) | [common]<br/>val [isLoaded](is-loaded.md): StateFlow&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt;<br/>Whether audio has been successfully loaded and ready for synthesis. |
| [isLoading](is-loading.md) | [common]<br/>val [isLoading](is-loading.md): StateFlow&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt;<br/>Whether audio is currently being loaded. |
| [sampleRate](sample-rate.md) | [common]<br/>val [sampleRate](sample-rate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Sample rate for output audio. |
| [silenceBeatsEnd](silence-beats-end.md) | [common]<br/>val [silenceBeatsEnd](silence-beats-end.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of silence beats at the end. |
| [silenceBeatsStart](silence-beats-start.md) | [common]<br/>val [silenceBeatsStart](silence-beats-start.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of silence beats at the start. |

## Functions

| Name | Summary |
|---|---|
| [loadAudio](load-audio.md) | [common]<br/>suspend fun [loadAudio](load-audio.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Load audio files for all svaras. |
| [loadAudioSync](load-audio-sync.md) | [common]<br/>fun [loadAudioSync](load-audio-sync.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Load audio files synchronously (blocking). |
| [release](release.md) | [common]<br/>fun [release](release.md)()<br/>Release resources. |
| [synthesize](synthesize.md) | [common]<br/>fun [synthesize](synthesize.md)(): [AudioRawData](../../com.musicmuni.voxatrace.sonix.model/-audio-raw-data/index.md)?<br/>Synthesize the lesson track from loaded audio. |
