---
sidebar_position: 9
---

# SonixMidiSynthesizer

Convert MIDI notes or files to audio using SoundFont instruments.

## Quick Start

### Kotlin

```kotlin
val synth = SonixMidiSynthesizer.create(soundFontPath = "/path/to/soundfont.sf2")

// Synthesize from a list of notes
val notes = listOf(
    MidiNote(note = 60, startTime = 0f, endTime = 500f),      // C4 at 0-500ms
    MidiNote(note = 62, startTime = 500f, endTime = 1000f),    // D4 at 500-1000ms
    MidiNote(note = 64, startTime = 1000f, endTime = 1500f)    // E4 at 1000-1500ms
)
synth.synthesizeFromNotes(notes = notes, outputPath = "/path/to/output.wav")

synth.release()
```

### Swift

```swift
let synth = SonixMidiSynthesizer.create(soundFontPath: "/path/to/soundfont.sf2")

let notes = [
    MidiNote(note: 60, startTime: 0, endTime: 500),
    MidiNote(note: 62, startTime: 500, endTime: 1000),
    MidiNote(note: 64, startTime: 1000, endTime: 1500)
]
synth.synthesizeFromNotes(notes: notes, outputPath: "/path/to/output.wav")

synth.release()
```

## Configuration

### Factory Method

```kotlin
val synth = SonixMidiSynthesizer.create(soundFontPath = "/path/to/soundfont.sf2")
```

```swift
let synth = SonixMidiSynthesizer.create(soundFontPath: "/path/to/soundfont.sf2")
```

### Builder

For custom sample rate or error handling:

#### Kotlin

```kotlin
val synth = SonixMidiSynthesizer.Builder()
    .soundFontPath("/path/to/soundfont.sf2")
    .sampleRate(48000)
    .onError { error -> println("Error: $error") }
    .build()
```

#### Swift

```swift
let synth = SonixMidiSynthesizer.Builder()
    .soundFontPath(path: "/path/to/soundfont.sf2")
    .sampleRate(rate: 48000)
    .build()
```

### Builder Parameters

| Method | Type | Default | Description |
|--------|------|---------|-------------|
| `soundFontPath` | `String` | — | Path to .sf2 or .sf3 file (required) |
| `sampleRate` | `Int` | `44100` | Output sample rate in Hz |
| `onError` | `(String) -> Unit` | — | Error callback |

## Synthesis Methods

### From MIDI File

```kotlin
val success: Boolean = synth.synthesize(
    midiPath = "/path/to/input.mid",
    outputPath = "/path/to/output.wav"
)
```

### From MIDI Data Bytes

```kotlin
val midiData: ByteArray = ...  // Standard MIDI File bytes
val success: Boolean = synth.synthesizeMidi(
    midiData = midiData,
    outputPath = "/path/to/output.wav"
)
```

### From Note List

Note timing is in absolute milliseconds:

```kotlin
val notes = listOf(
    MidiNote(note = 60, startTime = 0f, endTime = 500f),
    MidiNote(note = 62, startTime = 500f, endTime = 1000f)
)
val success: Boolean = synth.synthesizeFromNotes(
    notes = notes,
    outputPath = "/path/to/output.wav"
)
```

### From Pitch File

Generate audio from a pitch contour file (used in music education):

```kotlin
val success: Boolean = synth.synthesizeFromPitchFile(
    pitchFilePath = "/path/to/melody.pitchPP",
    outputPath = "/path/to/output.wav",
    lessonTonicHz = 261.63f,                  // C4 tonic
    parentTonicHz = 261.63f                   // defaults to lessonTonicHz
)
```

## Method Summary

| Method | Input | Output | Description |
|--------|-------|--------|-------------|
| `synthesize` | MIDI file path | WAV file | Convert MIDI file to audio |
| `synthesizeMidi` | MIDI bytes | WAV file | Convert raw MIDI data to audio |
| `synthesizeFromNotes` | `List<MidiNote>` | WAV file | Convert note list to audio |
| `synthesizeFromPitchFile` | Pitch file path | WAV file | Convert pitch contour to audio |
| `release` | — | — | Release resources |

## Properties

| Property | Type | Description |
|----------|------|-------------|
| `soundFontPath` | `String` | Path to the SoundFont file |
| `sampleRate` | `Int` | Output sample rate |
| `version` | `String` | FluidSynth version string |

## MidiNote

| Property | Type | Description |
|----------|------|-------------|
| `note` | `Int` | MIDI note number (60 = C4, 72 = C5) |
| `startTime` | `Float` | Start time in milliseconds |
| `endTime` | `Float` | End time in milliseconds |

## Common Patterns

### Generate and Play Reference Audio

```kotlin
val synth = SonixMidiSynthesizer.create(soundFontPath = sfPath)

val notes = listOf(
    MidiNote(note = 60, startTime = 0f, endTime = 500f),
    MidiNote(note = 64, startTime = 500f, endTime = 1000f),
    MidiNote(note = 67, startTime = 1000f, endTime = 1500f)
)

val outputPath = "${cacheDir}/reference.wav"
if (synth.synthesizeFromNotes(notes, outputPath)) {
    val player = SonixPlayer.create(outputPath)
    player.play()
}
```

### Convert to Compressed Format

```kotlin
// Synthesize to WAV
synth.synthesizeFromNotes(notes, outputPath = "output.wav")

// Convert to MP3 for sharing
val wavData = SonixDecoder.decode("output.wav", targetSampleRate = null)
if (wavData != null) {
    SonixEncoder.encode(data = wavData, outputPath = "output.mp3", format = "mp3")
}
```

## Next Steps

- [SonixLessonSynthesizer](./lesson-synthesizer) — Synthesize lessons from svara sequences
- [SonixPlayer](./player) — Play synthesized audio
- [SonixEncoder](./encoder) — Convert WAV output to MP3/M4A
