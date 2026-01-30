//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraBreath](index.md)/[computeCapacity](compute-capacity.md)

# computeCapacity

[common]\
fun [computeCapacity](compute-capacity.md)(times: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)

Compute breath capacity from pitch contour.

Measures the maximum duration of sustained voiced segments, indicating how long the singer can hold notes without breathing.

#### Return

Breath capacity in seconds (longest sustained phrase)

#### Parameters

common

| | |
|---|---|
| times | Array of timestamps in seconds |
| pitchesHz | Array of pitch values in Hz (-1 for unvoiced frames) |
