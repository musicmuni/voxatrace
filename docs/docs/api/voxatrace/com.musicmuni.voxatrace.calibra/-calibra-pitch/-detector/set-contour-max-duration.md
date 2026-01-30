//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraPitch](../index.md)/[Detector](index.md)/[setContourMaxDuration](set-contour-max-duration.md)

# setContourMaxDuration

[common]\
abstract fun [setContourMaxDuration](set-contour-max-duration.md)(seconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))

Set the maximum duration for the live pitch contour.

Older points are removed when the contour exceeds this duration. Call this when starting a new segment to match segment duration.

#### Parameters

common

| | |
|---|---|
| seconds | Maximum duration in seconds |
