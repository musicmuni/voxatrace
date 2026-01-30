//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraPitch](../index.md)/[PostProcess](index.md)/[process](process.md)

# process

[common]\
fun [process](process.md)(pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)

Process a pitch contour with smoothing and octave correction using default settings.

#### Return

Processed pitch array

#### Parameters

common

| | |
|---|---|
| pitchesHz | Input pitch array in Hz (-1 for unvoiced) |

[common]\
fun [process](process.md)(pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), smoothingWindowSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 7, octaveThresholdCents: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 500.0f, enableSmoothing: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true, enableOctaveCorrection: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)

Process a pitch contour with configurable smoothing and octave correction.

#### Return

Processed pitch array

#### Parameters

common

| | |
|---|---|
| pitchesHz | Input pitch array in Hz (-1 for unvoiced) |
| smoothingWindowSize | Smoothing filter window (must be odd, default 7) |
| octaveThresholdCents | How close to 1200 cents a jump must be to be corrected (default 500) |
| enableSmoothing | Enable smoothing filter (default true) |
| enableOctaveCorrection | Enable octave error correction (default true) |
