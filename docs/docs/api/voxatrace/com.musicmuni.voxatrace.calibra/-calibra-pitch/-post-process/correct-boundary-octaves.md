//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraPitch](../index.md)/[PostProcess](index.md)/[correctBoundaryOctaves](correct-boundary-octaves.md)

# correctBoundaryOctaves

[common]\
fun [correctBoundaryOctaves](correct-boundary-octaves.md)(pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), hopMs: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 10, boundaryWindowMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 50.0f): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)

Correct octave errors at phrase boundaries (onset/offset).

YIN sometimes detects the wrong octave at the start or end of a phrase where the signal is weaker. This corrects by comparing boundary regions to the stable interior of each phrase.

#### Return

Corrected pitch array

#### Parameters

common

| | |
|---|---|
| pitchesHz | Input pitch array in Hz (-1 for unvoiced) |
| hopMs | Hop size between frames in milliseconds (default: 10) |
| boundaryWindowMs | How much of phrase edges to check (default: 50) |
