//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.util](../index.md)/[MidiUtils](index.md)

# MidiUtils

[common]\
object [MidiUtils](index.md)

Utility functions for MIDI generation and manipulation. Ported from lib_audio Utils.java.

## Functions

| Name | Summary |
|---|---|
| [generateMidiFile](generate-midi-file.md) | [common]<br/>fun [generateMidiFile](generate-midi-file.md)(notes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[MidiNote](../../com.musicmuni.voxatrace.sonix.midi/-midi-note/index.md)&gt;): [MidiFile](../../com.musicmuni.voxatrace.sonix.midi/-midi-file/index.md)<br/>Generate a MIDI file from a list of MidiNotes. |
| [hzToMidiNote](hz-to-midi-note.md) | [common]<br/>fun [hzToMidiNote](hz-to-midi-note.md)(pitchHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), parentTonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), lessonTonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Convert pitch in Hz to MIDI note number, relative to lesson tonic. Note: Uses C4 (middle C) = 261.63 Hz as reference for MIDI note 60. |
| [loadMidiNotesFromFile](load-midi-notes-from-file.md) | [common]<br/>fun [loadMidiNotesFromFile](load-midi-notes-from-file.md)(fileSystem: FileSystem, path: Path, parentTonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), lessonTonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[MidiNote](../../com.musicmuni.voxatrace.sonix.midi/-midi-note/index.md)&gt;<br/>Load MIDI notes from a file path using the given FileSystem. |
| [loadMidiNotesInfo](load-midi-notes-info.md) | [common]<br/>fun [loadMidiNotesInfo](load-midi-notes-info.md)(source: BufferedSource, parentTonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), lessonTonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[MidiNote](../../com.musicmuni.voxatrace.sonix.midi/-midi-note/index.md)&gt;<br/>Load MIDI notes from a pitch file. Format: startTime endTime pitchHz (whitespace separated) |
