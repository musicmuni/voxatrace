//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.feedback](../index.md)/[SongFeedbackGenerator](index.md)

# SongFeedbackGenerator

[common]\
class [SongFeedbackGenerator](index.md)(dtwScorer: [DtwScorer](../../com.musicmuni.voxatrace.calibra.scoring/-dtw-scorer/index.md))

Feedback generation for song (melody) evaluation using DTW.

Ported from native/src/calibra/core/feedback/SongFeedbackGenerator.cpp for the &quot;Kotlin-max, C-min&quot; migration.

Evaluates student singing against reference melodies using DTW (Dynamic Time Warping) for pitch alignment and comparison.

## Constructors

| | |
|---|---|
| [SongFeedbackGenerator](-song-feedback-generator.md) | [common]<br/>constructor(dtwScorer: [DtwScorer](../../com.musicmuni.voxatrace.calibra.scoring/-dtw-scorer/index.md)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |
| [MelodyResult](-melody-result/index.md) | [common]<br/>data class [MelodyResult](-melody-result/index.md)(var score: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, var level: [PerformanceLevel](../../com.musicmuni.voxatrace.calibra.model/-performance-level/index.md) = PerformanceLevel.NOT_EVALUATED, var tStart: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, var tEnd: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f)<br/>Result of evaluating a single melody segment. |
| [MelodySegment](-melody-segment/index.md) | [common]<br/>data class [MelodySegment](-melody-segment/index.md)(var tStart: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, var tEnd: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, var indStartPitch: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = -1, var indEndPitch: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = -1, var lyrics: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) = &quot;&quot;)<br/>Represents a segment of a melody. |

## Functions

| Name | Summary |
|---|---|
| [computeGlobalScore](compute-global-score.md) | [common]<br/>fun [computeGlobalScore](compute-global-score.md)(results: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SongFeedbackGenerator.MelodyResult](-melody-result/index.md)&gt;): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Computes the global score from melody segment results. |
| [evaluateSegments](evaluate-segments.md) | [common]<br/>fun [evaluateSegments](evaluate-segments.md)(segsRef: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SongFeedbackGenerator.MelodySegment](-melody-segment/index.md)&gt;, segsStd: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SongFeedbackGenerator.MelodySegment](-melody-segment/index.md)&gt;, pitchRef: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), timesRef: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), pitchStd: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), timesStd: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SongFeedbackGenerator.MelodyResult](-melody-result/index.md)&gt;<br/>Evaluates song segments using DTW-based pitch comparison in MIDI space. |
