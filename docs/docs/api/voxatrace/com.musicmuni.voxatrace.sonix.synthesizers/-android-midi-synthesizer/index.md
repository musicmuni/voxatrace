//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.synthesizers](../index.md)/[AndroidMidiSynthesizer](index.md)

# AndroidMidiSynthesizer

[android]\
class [AndroidMidiSynthesizer](index.md) : [MidiSynthesizer](../-midi-synthesizer/index.md)

Android implementation of MidiSynthesizer using FluidSynth via JNI. Converts MIDI files to audio using SoundFont files (.sf2 and .sf3).

## Constructors

| | |
|---|---|
| [AndroidMidiSynthesizer](-android-midi-synthesizer.md) | [android]<br/>constructor() |

## Functions

| Name | Summary |
|---|---|
| [getVersion](get-version.md) | [android]<br/>open override fun [getVersion](get-version.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Get FluidSynth version string for debugging. |
| [synthesize](synthesize.md) | [android]<br/>open override fun [synthesize](synthesize.md)(outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), midiPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), soundFontPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Synthesize a MIDI file to audio using a SoundFont. |
| [synthesizeFromNotes](../synthesize-from-notes.md) | [common]<br/>fun [MidiSynthesizer](../-midi-synthesizer/index.md).[synthesizeFromNotes](../synthesize-from-notes.md)(fileSystem: FileSystem, outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), notes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[MidiNote](../../com.musicmuni.voxatrace.sonix.midi/-midi-note/index.md)&gt;, soundFontPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44100, tempMidiPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Synthesize audio from a list of MIDI notes. Generates a temporary MIDI file and synthesizes it. |
| [synthesizeFromPitchFile](../synthesize-from-pitch-file.md) | [common]<br/>fun [MidiSynthesizer](../-midi-synthesizer/index.md).[synthesizeFromPitchFile](../synthesize-from-pitch-file.md)(fileSystem: FileSystem, outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), pitchFilePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), soundFontPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), lessonTonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), parentTonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = lessonTonicHz, sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44100, tempMidiPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Synthesize audio from a pitch contour file. |
| [synthesizeMidi](synthesize-midi.md) | [android]<br/>open override fun [synthesizeMidi](synthesize-midi.md)(outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), midiData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), soundFontPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Synthesize audio from raw MIDI file data. This is a Swift-friendly API that doesn't require Okio FileSystem. |
