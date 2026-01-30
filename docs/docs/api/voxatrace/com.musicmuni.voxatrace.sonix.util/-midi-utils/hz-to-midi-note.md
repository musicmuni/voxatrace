//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.util](../index.md)/[MidiUtils](index.md)/[hzToMidiNote](hz-to-midi-note.md)

# hzToMidiNote

[common]\
fun [hzToMidiNote](hz-to-midi-note.md)(pitchHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), parentTonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), lessonTonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)

Convert pitch in Hz to MIDI note number, relative to lesson tonic. Note: Uses C4 (middle C) = 261.63 Hz as reference for MIDI note 60.
