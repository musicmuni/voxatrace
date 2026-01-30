//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.evaluation](../index.md)/[MelodyEvaluator](index.md)

# MelodyEvaluator

[common]\
class [MelodyEvaluator](index.md)

Pure Kotlin melody evaluator.

Ported from native/src/calibra/core/evaluation/MelodyEvaluator.cpp for the &quot;Kotlin-max, C-min&quot; migration.

Evaluates how well a student's singing matches a reference melody using DTW alignment and segment-by-segment scoring.

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [destroy](destroy.md) | [common]<br/>fun [destroy](destroy.md)()<br/>Release resources (no-op for pure Kotlin implementation). |
| [evaluate](evaluate.md) | [common]<br/>fun [evaluate](evaluate.md)(refTimes: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), refPitches: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), studentTimes: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), studentPitches: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), refSegStarts: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), refSegEnds: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), stdSegStarts: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), stdSegEnds: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): &lt;Error class: unknown class&gt;&lt;[Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[FeedbackSegment](../-feedback-segment/index.md)&gt;&gt;<br/>Evaluate a melody performance. |
| [evaluateStudent](evaluate-student.md) | [common]<br/>fun [evaluateStudent](evaluate-student.md)(studentTimes: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), studentPitches: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): &lt;Error class: unknown class&gt;&lt;[Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[FeedbackSegment](../-feedback-segment/index.md)&gt;&gt;?<br/>Evaluate student pitch against stored reference. |
| [getDtwScorer](get-dtw-scorer.md) | [common]<br/>fun [getDtwScorer](get-dtw-scorer.md)(): [DtwScorer](../../com.musicmuni.voxatrace.calibra.scoring/-dtw-scorer/index.md)<br/>Get the DTW scorer for direct DTW operations. |
| [getKeyShift](get-key-shift.md) | [common]<br/>fun [getKeyShift](get-key-shift.md)(): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Get the key shift in semitones. |
| [getNumSegments](get-num-segments.md) | [common]<br/>fun [getNumSegments](get-num-segments.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Get the number of segments in stored reference. |
| [hasStoredReference](has-stored-reference.md) | [common]<br/>fun [hasStoredReference](has-stored-reference.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if evaluator has stored reference data. |
