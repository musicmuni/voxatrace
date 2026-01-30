//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.scoring](../index.md)/[DtwScorer](index.md)/[getDtwPathForVectors](get-dtw-path-for-vectors.md)

# getDtwPathForVectors

[common]\
fun [getDtwPathForVectors](get-dtw-path-for-vectors.md)(seq1: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)&gt;, seq2: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)&gt;, diagonalWeight: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f, bandWidth: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = -1): [DtwScorer.DtwPath](-dtw-path/index.md)

Get DTW path for multi-dimensional vector sequences.

#### Return

DTW path

#### Parameters

common

| | |
|---|---|
| seq1 | First sequence (frames x dimensions) |
| seq2 | Second sequence (frames x dimensions) |
| diagonalWeight | Weight for diagonal moves (default 1.0) |
| bandWidth | Band window constraint (-1 to disable) |
