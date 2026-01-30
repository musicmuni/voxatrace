//[voxatrace](../../index.md)/[com.musicmuni.voxatrace.sonix.synthesizers](index.md)/[synthesizeFromNotes](synthesize-from-notes.md)

# synthesizeFromNotes

[common]\
fun [MidiSynthesizer](-midi-synthesizer/index.md).[synthesizeFromNotes](synthesize-from-notes.md)(fileSystem: FileSystem, outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), notes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[MidiNote](../com.musicmuni.voxatrace.sonix.midi/-midi-note/index.md)&gt;, soundFontPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44100, tempMidiPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Synthesize audio from a list of MIDI notes. Generates a temporary MIDI file and synthesizes it.

Note timing is specified in absolute milliseconds. A note with startTime=500 will play at 500ms in the output audio.

#### Return

true if synthesis was successful

#### Parameters

common

| | |
|---|---|
| fileSystem | The FileSystem to use for file operations |
| outputPath | Output audio file path (.wav) |
| notes | List of MidiNote objects with timing in milliseconds |
| soundFontPath | SoundFont file path (.sf2 or .sf3) |
| sampleRate | Output sample rate (default: 44100) |
| tempMidiPath | Optional path for temporary MIDI file (defaults to outputPath.mid) |
