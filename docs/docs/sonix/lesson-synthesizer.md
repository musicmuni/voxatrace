---
sidebar_position: 10
---

# SonixLessonSynthesizer

Synthesize audio lessons from note sequences (e.g. Sa Re Ga in Indian classical practice), each with its own audio recording.

## Quick Start

### Kotlin

```kotlin
val notes = listOf(
    LessonNote(
        noteName = "Sa",
        noteLabel = "S",
        noteAudioFilePath = "/path/to/sa.wav",
        numBeats = 2,
        numSamplesConsonant = 100
    ),
    LessonNote(
        noteName = "Re",
        noteLabel = "R",
        noteAudioFilePath = "/path/to/re.wav",
        numBeats = 2,
        numSamplesConsonant = 100
    )
)

val synth = SonixLessonSynthesizer.create(
    notes = notes,
    beatLengthMs = 500
)

if (synth.loadAudioSync()) {
    val audioData: AudioRawData? = synth.synthesize()
    // audioData is ready to play or save
}

synth.release()
```

### Swift

```swift
let notes = [
    LessonNote(
        noteName: "Sa",
        noteLabel: "S",
        noteAudioFilePath: "/path/to/sa.wav",
        numBeats: 2,
        numSamplesConsonant: 100
    ),
    LessonNote(
        noteName: "Re",
        noteLabel: "R",
        noteAudioFilePath: "/path/to/re.wav",
        numBeats: 2,
        numSamplesConsonant: 100
    )
]

let synth = SonixLessonSynthesizer.create(
    notes: notes,
    beatLengthMs: 500
)

if await synth.loadAudio() {
    if let audioData = synth.synthesize() {
        // audioData is ready to play or save
    }
}

synth.release()
```

## Configuration

### Factory Method

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `notes` | `List<LessonNote>` | — | Note sequence (required) |
| `beatLengthMs` | `Int` | — | Beat duration in milliseconds (required) |

### Builder

For advanced configuration:

#### Kotlin

```kotlin
val synth = SonixLessonSynthesizer.Builder()
    .notes(noteList)
    .beatLengthMs(500)
    .silenceBeats(start = 2, end = 2)
    .sampleRate(44100)
    .onError { error -> println("Error: $error") }
    .build()
```

### Builder Parameters

| Method | Type | Default | Description |
|--------|------|---------|-------------|
| `notes` | `List<LessonNote>` | — | Note sequence (required) |
| `beatLengthMs` | `Int` | — | Beat duration in ms (required) |
| `silenceBeats` | `start: Int, end: Int` | `2, 2` | Silence beats at start/end |
| `sampleRate` | `Int` | `16000` | Output sample rate in Hz |
| `onError` | `(String) -> Unit` | — | Error callback |

## Workflow

The synthesis process has two steps:

### 1. Load Audio

Load note audio files into memory:

```kotlin
// Suspending (preferred)
val success = synth.loadAudio()

// Synchronous (blocking)
val success = synth.loadAudioSync()
```

### 2. Synthesize

Generate the lesson track:

```kotlin
val audioData: AudioRawData? = synth.synthesize()
if (audioData != null) {
    // Play it
    val player = SonixPlayer.createFromPcm(
        data = audioData.audioData,
        sampleRate = audioData.sampleRate,
        channels = audioData.numChannels
    )
    player.play()
}
```

## Observing State

### Kotlin (StateFlow)

```kotlin
synth.isLoading.collect { loading ->
    progressBar.isVisible = loading
}

synth.isLoaded.collect { loaded ->
    synthesizeButton.isEnabled = loaded
}

synth.error.collect { error ->
    error?.let { showError(it.message) }
}
```

### Swift (Observers)

```swift
let loadingTask = synth.observeIsLoading { loading in
    self.isLoading = loading
}

let loadedTask = synth.observeIsLoaded { loaded in
    self.isReady = loaded
}

let errorTask = synth.observeError { error in
    if let error = error { self.showError(error.message) }
}
```

### Swift (Combine)

```swift
synth.isLoadingPublisher
    .receive(on: DispatchQueue.main)
    .sink { self.isLoading = $0 }
    .store(in: &cancellables)
```

### StateFlows

| StateFlow | Type | Description |
|-----------|------|-------------|
| `isLoading` | `StateFlow<Boolean>` | Whether audio files are being loaded |
| `isLoaded` | `StateFlow<Boolean>` | Whether ready for synthesis |
| `error` | `StateFlow<SonixError?>` | Error state |

### Properties

| Property | Type | Description |
|----------|------|-------------|
| `beatLengthMs` | `Int` | Beat duration in milliseconds |
| `silenceBeatsStart` | `Int` | Silence beats at the start |
| `silenceBeatsEnd` | `Int` | Silence beats at the end |
| `sampleRate` | `Int` | Output sample rate |

## LessonNote

Each note in the sequence is defined by:

| Property | Type | Description |
|----------|------|-------------|
| `noteName` | `String` | Full name (e.g., "Sa", "Re") |
| `noteLabel` | `String` | Short label (e.g., "S", "R") |
| `noteAudioFilePath` | `String` | Path to audio recording |
| `numBeats` | `Int` | Duration in beats |
| `numSamplesConsonant` | `Int` | Consonant samples for crossfading |

## Method Summary

| Method | Returns | Description |
|--------|---------|-------------|
| `loadAudio()` | `Boolean` | Load audio files (suspend / async) |
| `loadAudioSync()` | `Boolean` | Load audio files (blocking) |
| `synthesize()` | `AudioRawData?` | Generate lesson audio |
| `release()` | — | Release resources |

## Next Steps

- [SonixMidiSynthesizer](./midi-synthesizer) — MIDI-based synthesis
- [SonixPlayer](./player) — Play synthesized audio
- [SonixEncoder](./encoder) — Save synthesized audio to file
