//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.common](../index.md)/[KeyShift](index.md)/[computeShiftSemitones](compute-shift-semitones.md)

# computeShiftSemitones

[common]\
fun [computeShiftSemitones](compute-shift-semitones.md)(referenceKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), studentKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)

Compute the semitone shift from reference to student key.

#### Return

Semitones to shift student data to match reference key.     Positive = student is higher, negative = student is lower.

#### Parameters

common

| | |
|---|---|
| referenceKeyHz | Reference key frequency in Hz (e.g., 261.63 for C4) |
| studentKeyHz | Student key frequency in Hz (e.g., 293.66 for D4) |
