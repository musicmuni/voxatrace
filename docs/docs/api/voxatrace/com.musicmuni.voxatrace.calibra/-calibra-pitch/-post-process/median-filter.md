//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraPitch](../index.md)/[PostProcess](index.md)/[medianFilter](median-filter.md)

# medianFilter

[common]\
fun [medianFilter](median-filter.md)(pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), kernelSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 5): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)

Apply median filter for spike removal.

#### Return

Filtered pitch array

#### Parameters

common

| | |
|---|---|
| pitchesHz | Input pitch array in Hz |
| kernelSize | Median filter kernel size (must be odd, default 5) |
