//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.common](../index.md)/[KeyShift](index.md)/[shiftPitchCents](shift-pitch-cents.md)

# shiftPitchCents

[common]\
fun [shiftPitchCents](shift-pitch-cents.md)(pitchesCents: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), semitones: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), silenceValue: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = DEFAULT_SILENCE_CENTS)

Shift pitch values in cents by a given number of semitones.

#### Parameters

common

| | |
|---|---|
| pitchesCents | Pitch values in cents (modified in place) |
| semitones | Semitones to shift (positive = up, negative = down) |
| silenceValue | Value that indicates silence (default: -10000) |
