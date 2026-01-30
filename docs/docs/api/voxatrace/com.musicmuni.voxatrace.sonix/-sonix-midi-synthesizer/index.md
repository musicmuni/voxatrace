//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixMidiSynthesizer](index.md)

# SonixMidiSynthesizer

class [SonixMidiSynthesizer](index.md)

MIDI synthesizer for converting MIDI to audio using SoundFont files.

## What is MIDI Synthesis?

MIDI is a **note description format** - it says &quot;play note C4 for 500ms&quot; but doesn't contain actual audio. A **synthesizer** converts MIDI to audio using instrument samples from a **SoundFont** file.

Use it for:

- 
   Generating reference tracks from note sequences
- 
   Creating practice audio from lesson data
- 
   Converting MIDI files to audio

## When to Use

| Scenario | Use This? | Why |
|---|---|---|
| Generate reference audio | Yes | From lesson notes |
| Convert MIDI to audio | Yes | Core use case |
| Play audio file | No | Use `SonixPlayer` |
| Real-time MIDI playback | No | Not supported yet |

## Quick Start

### Kotlin

```kotlin
val synth = SonixMidiSynthesizer.create(soundFontPath = "/path/to/soundfont.sf2")

// Synthesize from MIDI file
synth.synthesize(midiPath = "input.mid", outputPath = "output.wav")

// Or synthesize from notes (timing in milliseconds)
val notes = listOf(
    MidiNote(note = 60, startTime = 0f, endTime = 500f),    // C4 at 0-500ms
    MidiNote(note = 62, startTime = 500f, endTime = 1000f)  // D4 at 500-1000ms
)
synth.synthesizeFromNotes(notes = notes, outputPath = "output.wav")

synth.release()  // Optional - synth is stateless
```

### Swift

```swift
let synth = SonixMidiSynthesizer.companion.create(soundFontPath: "/path/to/soundfont.sf2")

// Synthesize from MIDI file
synth.synthesize(midiPath: "input.mid", outputPath: "output.wav")

// Or synthesize from notes
let notes = [
    MidiNote(note: 60, startTime: 0, endTime: 500),    // C4 at 0-500ms
    MidiNote(note: 62, startTime: 500, endTime: 1000)  // D4 at 500-1000ms
]
synth.synthesizeFromNotes(notes: notes, outputPath: "output.wav")
```

## Builder Pattern (Advanced)

### Kotlin

```kotlin
val synth = SonixMidiSynthesizer.Builder()
    .soundFontPath("/path/to/soundfont.sf2")
    .sampleRate(48000)
    .onError { error -> println("Error: $error") }
    .build()
```

### Swift

```swift
let synth = SonixMidiSynthesizer.Builder()
    .soundFontPath(path: "/path/to/soundfont.sf2")
    .sampleRate(rate: 48000)
    .build()
```

## SoundFont Files

SoundFont (.sf2 or .sf3) files contain instrument samples:

- 
   Use high-quality SoundFonts for better audio
- 
   File size affects memory usage
- 
   SF3 format is compressed (smaller files)

## Platform Notes

### iOS/Android

- 
   Uses FluidSynth library for synthesis
- 
   Runs synchronously (consider background thread for large files)
- 
   Output is always WAV format

## Common Pitfalls

1. 
   **Missing SoundFont**: Must provide valid .sf2 or .sf3 file
2. 
   **Large MIDI files**: Synthesis time scales with MIDI duration
3. 
   **Note timing**: MidiNote times are in milliseconds (not seconds)
4. 
   **Output format**: Always outputs WAV; use SonixEncoder to convert

#### See also

| | |
|---|---|
| [SonixLessonSynthesizer](../-sonix-lesson-synthesizer/index.md) | For lesson-specific synthesis |
| [SonixEncoder](../-sonix-encoder/index.md) | For converting WAV to MP3/M4A |
| [MidiNote](../../com.musicmuni.voxatrace.sonix.midi/-midi-note/index.md) | For note specification format |

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [common]<br/>class [Builder](-builder/index.md)<br/>Builder for advanced SonixMidiSynthesizer configuration. |
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [sampleRate](sample-rate.md) | [common]<br/>val [sampleRate](sample-rate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Sample rate for output audio. |
| [soundFontPath](sound-font-path.md) | [common]<br/>val [soundFontPath](sound-font-path.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Path to the SoundFont file being used. |
| [version](version.md) | [common]<br/>val [version](version.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>FluidSynth version string for debugging. |

## Functions

| Name | Summary |
|---|---|
| [release](release.md) | [common]<br/>fun [release](release.md)()<br/>Release resources. Currently a no-op as MidiSynthesizer is stateless, but included for API consistency. |
| [synthesize](synthesize.md) | [common]<br/>fun [synthesize](synthesize.md)(midiPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Synthesize a MIDI file to audio. |
| [synthesizeFromNotes](synthesize-from-notes.md) | [common]<br/>fun [synthesizeFromNotes](synthesize-from-notes.md)(notes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[MidiNote](../../com.musicmuni.voxatrace.sonix.midi/-midi-note/index.md)&gt;, outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Synthesize audio from a list of MIDI notes. |
| [synthesizeFromPitchFile](synthesize-from-pitch-file.md) | [common]<br/>fun [synthesizeFromPitchFile](synthesize-from-pitch-file.md)(pitchFilePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), lessonTonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), parentTonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = lessonTonicHz): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Synthesize audio from a pitch contour file. |
| [synthesizeMidi](synthesize-midi.md) | [common]<br/>fun [synthesizeMidi](synthesize-midi.md)(midiData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Synthesize audio from raw MIDI data bytes. |
