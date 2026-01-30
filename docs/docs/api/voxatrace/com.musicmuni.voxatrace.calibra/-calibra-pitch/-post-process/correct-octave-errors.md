//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraPitch](../index.md)/[PostProcess](index.md)/[correctOctaveErrors](correct-octave-errors.md)

# correctOctaveErrors

[common]\
fun [correctOctaveErrors](correct-octave-errors.md)(pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), thresholdCents: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 500.0f, referencePitchHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)

Apply octave error correction only.

#### Return

Corrected pitch array

#### Parameters

common

| | |
|---|---|
| pitchesHz | Input pitch array in Hz |
| thresholdCents | Jump threshold in cents (default 500) |
| referencePitchHz | Reference pitch (0 = auto-detect) |
