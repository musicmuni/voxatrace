//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.util](../index.md)/[PitchContourAccumulator](index.md)/[setMaxDuration](set-max-duration.md)

# setMaxDuration

[common]\
fun [setMaxDuration](set-max-duration.md)(seconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))

Set the maximum duration for the contour.

Older points are removed when the contour exceeds this duration. Call this when starting a new segment to match segment duration.

#### Parameters

common

| | |
|---|---|
| seconds | Maximum duration in seconds |
