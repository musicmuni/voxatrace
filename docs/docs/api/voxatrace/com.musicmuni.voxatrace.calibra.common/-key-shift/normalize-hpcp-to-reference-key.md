//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.common](../index.md)/[KeyShift](index.md)/[normalizeHpcpToReferenceKey](normalize-hpcp-to-reference-key.md)

# normalizeHpcpToReferenceKey

[common]\
fun [normalizeHpcpToReferenceKey](normalize-hpcp-to-reference-key.md)(studentHpcp: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)&gt;, referenceKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), studentKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), hpcpSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 12)

Normalize student HPCP to reference key.

Convenience function that computes shift and rotates HPCP.

#### Parameters

common

| | |
|---|---|
| studentHpcp | Student HPCP frames (modified in place) |
| referenceKeyHz | Reference key frequency in Hz |
| studentKeyHz | Student key frequency in Hz |
| hpcpSize | Number of HPCP bins (default 12) |
