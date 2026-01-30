//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.scoring](../index.md)/[DtwScorer](index.md)/[computePathDistance](compute-path-distance.md)

# computePathDistance

[common]\
fun [computePathDistance](compute-path-distance.md)(seq1: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), seq2: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), path: [DtwScorer.DtwPath](-dtw-path/index.md), silValue: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = DEFAULT_SILENCE_VALUE): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)

Compute distance along a DTW path using octave-invariant metric.

#### Return

Total distance along path

#### Parameters

common

| | |
|---|---|
| seq1 | First sequence |
| seq2 | Second sequence |
| path | DTW path |
| silValue | Value representing silence (skipped in computation) |
