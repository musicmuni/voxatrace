//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixMidiSynthesizer](index.md)/[synthesizeFromNotes](synthesize-from-notes.md)

# synthesizeFromNotes

[common]\
fun [synthesizeFromNotes](synthesize-from-notes.md)(notes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[MidiNote](../../com.musicmuni.voxatrace.sonix.midi/-midi-note/index.md)&gt;, outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Synthesize audio from a list of MIDI notes.

Note timing is specified in absolute milliseconds. A note with startTime=500 will play at 500ms in the output audio.

#### Return

true if synthesis was successful

#### Parameters

common

| | |
|---|---|
| notes | List of MidiNote objects with timing in milliseconds |
| outputPath | Path for output WAV file |
