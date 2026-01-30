//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.scoring](../index.md)/[DtwScorer](index.md)/[computeDtw](compute-dtw.md)

# computeDtw

[common]\
fun [computeDtw](compute-dtw.md)(seq1: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), seq2: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), params: [DtwScorer.DtwParams](-dtw-params/index.md) = DtwParams()): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)

Compute DTW distance between two sequences.

#### Return

DTW distance value

#### Parameters

common

| | |
|---|---|
| seq1 | First sequence (reference) |
| seq2 | Second sequence (student) |
| params | DTW parameters |
