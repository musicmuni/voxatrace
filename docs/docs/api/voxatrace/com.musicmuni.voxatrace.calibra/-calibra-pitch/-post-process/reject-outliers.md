//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraPitch](../index.md)/[PostProcess](index.md)/[rejectOutliers](reject-outliers.md)

# rejectOutliers

[common]\
fun [rejectOutliers](reject-outliers.md)(pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), hopMs: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 10, minDurationMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 80.0f): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)

Reject short pitch runs (blips) based on minimum duration.

Finds contiguous runs of voiced pitch and rejects runs shorter than the minimum duration. This removes transient artifacts at phrase boundaries without limiting melodic range.

#### Return

Filtered pitch array with short runs marked as unvoiced (-1)

#### Parameters

common

| | |
|---|---|
| pitchesHz | Input pitch array in Hz (-1 for unvoiced) |
| hopMs | Hop size between frames in milliseconds (default: 10) |
| minDurationMs | Minimum duration for a valid pitch run in ms (default: 80) |
