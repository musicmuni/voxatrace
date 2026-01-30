//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.util](../index.md)/[MidiUtils](index.md)/[generateMidiFile](generate-midi-file.md)

# generateMidiFile

[common]\
fun [generateMidiFile](generate-midi-file.md)(notes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[MidiNote](../../com.musicmuni.voxatrace.sonix.midi/-midi-note/index.md)&gt;): [MidiFile](../../com.musicmuni.voxatrace.sonix.midi/-midi-file/index.md)

Generate a MIDI file from a list of MidiNotes.

Note timing is specified in absolute milliseconds. A note with startTime=500 will be positioned to play at 500ms in the output.

#### Return

MidiFile ready to be written

#### Parameters

common

| | |
|---|---|
| notes | List of notes with timing in milliseconds |
