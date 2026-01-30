//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.scoring](../index.md)/[DtwScorer](index.md)

# DtwScorer

[common]\
class [DtwScorer](index.md)

DTW (Dynamic Time Warping) scoring for singing evaluation.

Ported from native/src/calibra/core/evaluation/DtwScorer.cpp for the &quot;Kotlin-max, C-min&quot; migration.

Provides DTW-based distance computation and path extraction for comparing pitch sequences between reference and student performances.

Performance considerations:

- 
   Uses high-water mark allocation strategy for cost matrix
- 
   Pre-allocates buffers to minimize GC pressure
- 
   Uses primitive FloatArray instead of List\<Float\>

## Constructors

| | |
|---|---|
| [DtwScorer](-dtw-scorer.md) | [common]<br/>constructor() |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |
| [DtwParams](-dtw-params/index.md) | [common]<br/>data class [DtwParams](-dtw-params/index.md)(val distType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0, val hasGlobalConst: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, val globalType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0, val bandwidth: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1, val initCostMtx: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true, val reuseCostMtx: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, val delStep: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 2, val moveStep: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1, val diagStep: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1, val initFirstCol: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, val isSubsequence: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false)<br/>Parameters for DTW computation. |
| [DtwPath](-dtw-path/index.md) | [common]<br/>data class [DtwPath](-dtw-path/index.md)(val length: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0, val px: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html) = IntArray(0), val py: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html) = IntArray(0))<br/>DTW path structure. |
| [DtwResult](-dtw-result/index.md) | [common]<br/>data class [DtwResult](-dtw-result/index.md)(val distance: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val pathLength: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val path: [DtwScorer.DtwPath](-dtw-path/index.md))<br/>Result of DTW computation. |

## Functions

| Name | Summary |
|---|---|
| [computeDissimilarity](compute-dissimilarity.md) | [common]<br/>fun [computeDissimilarity](compute-dissimilarity.md)(pitchRef: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), pitchStd: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), params: [DtwScorer.DtwParams](-dtw-params/index.md) = DtwParams()): [DtwScorer.DtwResult](-dtw-result/index.md)<br/>Compute dissimilarity between pitch sequences. |
| [computeDtw](compute-dtw.md) | [common]<br/>fun [computeDtw](compute-dtw.md)(seq1: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), seq2: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), params: [DtwScorer.DtwParams](-dtw-params/index.md) = DtwParams()): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Compute DTW distance between two sequences. |
| [computePathDistance](compute-path-distance.md) | [common]<br/>fun [computePathDistance](compute-path-distance.md)(seq1: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), seq2: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), path: [DtwScorer.DtwPath](-dtw-path/index.md), silValue: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = DEFAULT_SILENCE_VALUE): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Compute distance along a DTW path using octave-invariant metric. |
| [extractPath](extract-path.md) | [common]<br/>fun [extractPath](extract-path.md)(cost: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), n: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), m: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), startX: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), startY: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), params: [DtwScorer.DtwParams](-dtw-params/index.md)): [DtwScorer.DtwPath](-dtw-path/index.md)<br/>Extract path from cost matrix. |
| [getDtwPathForVectors](get-dtw-path-for-vectors.md) | [common]<br/>fun [getDtwPathForVectors](get-dtw-path-for-vectors.md)(seq1: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)&gt;, seq2: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)&gt;, diagonalWeight: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f, bandWidth: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = -1): [DtwScorer.DtwPath](-dtw-path/index.md)<br/>Get DTW path for multi-dimensional vector sequences. |
| [getLastPath](get-last-path.md) | [common]<br/>fun [getLastPath](get-last-path.md)(): [DtwScorer.DtwPath](-dtw-path/index.md)<br/>Get the path from the last DTW computation. |
