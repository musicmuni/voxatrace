//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.common](../index.md)/[KeyShift](index.md)/[normalizeToReferenceKey](normalize-to-reference-key.md)

# normalizeToReferenceKey

[common]\
fun [normalizeToReferenceKey](normalize-to-reference-key.md)(studentPitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), referenceKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), studentKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))

Normalize student pitch to reference key.

Convenience function that computes shift and applies it. Student pitches are shifted to match reference key for comparison.

#### Parameters

common

| | |
|---|---|
| studentPitchesHz | Student pitch values in Hz (modified in place) |
| referenceKeyHz | Reference key frequency in Hz |
| studentKeyHz | Student key frequency in Hz |
