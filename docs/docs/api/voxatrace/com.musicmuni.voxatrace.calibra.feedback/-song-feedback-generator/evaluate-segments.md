//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.feedback](../index.md)/[SongFeedbackGenerator](index.md)/[evaluateSegments](evaluate-segments.md)

# evaluateSegments

[common]\
fun [evaluateSegments](evaluate-segments.md)(segsRef: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SongFeedbackGenerator.MelodySegment](-melody-segment/index.md)&gt;, segsStd: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SongFeedbackGenerator.MelodySegment](-melody-segment/index.md)&gt;, pitchRef: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), timesRef: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), pitchStd: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), timesStd: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SongFeedbackGenerator.MelodyResult](-melody-result/index.md)&gt;

Evaluates song segments using DTW-based pitch comparison in MIDI space.

#### Return

Array of melody evaluation results

#### Parameters

common

| | |
|---|---|
| segsRef | Array of reference segments |
| segsStd | Array of student segments |
| pitchRef | Reference pitch contour (in MIDI) |
| timesRef | Reference timestamps |
| pitchStd | Student pitch contour (in MIDI) |
| timesStd | Student timestamps |
