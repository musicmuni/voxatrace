//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.util](../index.md)/[MidiUtils](index.md)/[loadMidiNotesInfo](load-midi-notes-info.md)

# loadMidiNotesInfo

[common]\
fun [loadMidiNotesInfo](load-midi-notes-info.md)(source: BufferedSource, parentTonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), lessonTonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[MidiNote](../../com.musicmuni.voxatrace.sonix.midi/-midi-note/index.md)&gt;

Load MIDI notes from a pitch file. Format: startTime endTime pitchHz (whitespace separated)

#### Return

List of MidiNote objects

#### Parameters

common

| | |
|---|---|
| source | BufferedSource to read from |
| parentTonicHz | Parent tonic frequency |
| lessonTonicHz | Lesson tonic frequency |
