//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.util](../index.md)/[PitchContourAccumulator](index.md)/[addPoint](add-point.md)

# addPoint

[common]\
fun [addPoint](add-point.md)(pitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), confidence: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))

Add a pitch point to the accumulator.

Automatically:

- 
   Assigns timestamp based on hop size
- 
   Removes oldest point if max duration exceeded
- 
   Updates the contour StateFlow

#### Parameters

common

| | |
|---|---|
| pitch | Detected pitch in Hz (-1 for unvoiced) |
| confidence | Detection confidence (0.0-1.0) |
