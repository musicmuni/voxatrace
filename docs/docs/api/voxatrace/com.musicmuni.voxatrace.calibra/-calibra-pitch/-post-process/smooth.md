//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraPitch](../index.md)/[PostProcess](index.md)/[smooth](smooth.md)

# smooth

[common]\
fun [smooth](smooth.md)(pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), windowSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 7): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)

Apply smoothing filter only.

#### Return

Smoothed pitch array

#### Parameters

common

| | |
|---|---|
| pitchesHz | Input pitch array in Hz |
| windowSize | Filter window size (must be odd, default 7) |

[common]\
fun [smooth](smooth.md)(contour: [PitchContour](../../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md)): [PitchContour](../../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md)

Apply smoothing to a pitch contour.
