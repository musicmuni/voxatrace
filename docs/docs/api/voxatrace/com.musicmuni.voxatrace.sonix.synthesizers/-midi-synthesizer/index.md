//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.synthesizers](../index.md)/[MidiSynthesizer](index.md)

# MidiSynthesizer

interface [MidiSynthesizer](index.md)

MIDI synthesizer for converting MIDI to audio using SoundFont files.

Supports both .sf2 and .sf3 SoundFont formats.

Usage:

```kotlin
val synthesizer = createMidiSynthesizer()

// Option 1: Synthesize from existing MIDI file
synthesizer.synthesize(
    outputPath = "/path/to/output.wav",
    midiPath = "/path/to/input.mid",
    soundFontPath = "/path/to/soundfont.sf3"
)

// Option 2: Synthesize from notes (generates MIDI internally)
val notes = listOf(
    MidiNote(note = 60, startTime = 0f, endTime = 500f),
    MidiNote(note = 62, startTime = 500f, endTime = 1000f)
)
synthesizer.synthesizeFromNotes(
    fileSystem = FileSystem.SYSTEM,
    outputPath = "/path/to/output.wav",
    notes = notes,
    soundFontPath = "/path/to/soundfont.sf3",
    bpm = 120
)

// Option 3: Synthesize from pitch contour file (lesson reference)
synthesizer.synthesizeFromPitchFile(
    fileSystem = FileSystem.SYSTEM,
    outputPath = "/path/to/output.wav",
    pitchFilePath = "/path/to/notes.txt",
    soundFontPath = "/path/to/soundfont.sf3",
    lessonTonicHz = 261.63f,
    parentTonicHz = 261.63f,
    bpm = 120
)
```

#### Inheritors

| |
|---|
| [AndroidMidiSynthesizer](../-android-midi-synthesizer/index.md#208028448%2FMain%2F-204059827) |

## Functions

| Name | Summary |
|---|---|
| [getVersion](get-version.md) | [common]<br/>abstract fun [getVersion](get-version.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Get FluidSynth version string for debugging. |
| [synthesize](synthesize.md) | [common]<br/>abstract fun [synthesize](synthesize.md)(outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), midiPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), soundFontPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44100): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Synthesize a MIDI file to audio using a SoundFont. |
| [synthesizeFromNotes](../synthesize-from-notes.md) | [common]<br/>fun [MidiSynthesizer](index.md).[synthesizeFromNotes](../synthesize-from-notes.md)(fileSystem: FileSystem, outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), notes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[MidiNote](../../com.musicmuni.voxatrace.sonix.midi/-midi-note/index.md)&gt;, soundFontPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44100, tempMidiPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Synthesize audio from a list of MIDI notes. Generates a temporary MIDI file and synthesizes it. |
| [synthesizeFromPitchFile](../synthesize-from-pitch-file.md) | [common]<br/>fun [MidiSynthesizer](index.md).[synthesizeFromPitchFile](../synthesize-from-pitch-file.md)(fileSystem: FileSystem, outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), pitchFilePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), soundFontPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), lessonTonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), parentTonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = lessonTonicHz, sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44100, tempMidiPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Synthesize audio from a pitch contour file. |
| [synthesizeMidi](synthesize-midi.md) | [common]<br/>abstract fun [synthesizeMidi](synthesize-midi.md)(outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), midiData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), soundFontPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44100): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Synthesize audio from raw MIDI file data. This is a Swift-friendly API that doesn't require Okio FileSystem. |
