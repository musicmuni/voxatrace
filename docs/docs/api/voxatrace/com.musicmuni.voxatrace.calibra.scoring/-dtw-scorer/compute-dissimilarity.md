//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.scoring](../index.md)/[DtwScorer](index.md)/[computeDissimilarity](compute-dissimilarity.md)

# computeDissimilarity

[common]\
fun [computeDissimilarity](compute-dissimilarity.md)(pitchRef: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), pitchStd: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), params: [DtwScorer.DtwParams](-dtw-params/index.md) = DtwParams()): [DtwScorer.DtwResult](-dtw-result/index.md)

Compute dissimilarity between pitch sequences.

#### Return

DtwResult with distance, path length, and path

#### Parameters

common

| | |
|---|---|
| pitchRef | Reference pitch sequence |
| pitchStd | Student pitch sequence |
| params | DTW parameters |
