//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.common](../index.md)/[KeyShift](index.md)/[shiftPitchMidi](shift-pitch-midi.md)

# shiftPitchMidi

[common]\
fun [shiftPitchMidi](shift-pitch-midi.md)(pitchesMidi: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), semitones: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), silenceValue: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = DEFAULT_SILENCE_MIDI)

Shift pitch values in MIDI note numbers by a given number of semitones.

#### Parameters

common

| | |
|---|---|
| pitchesMidi | Pitch values in MIDI note numbers (modified in place) |
| semitones | Semitones to shift (positive = up, negative = down) |
| silenceValue | Value that indicates silence (default: 0) |
