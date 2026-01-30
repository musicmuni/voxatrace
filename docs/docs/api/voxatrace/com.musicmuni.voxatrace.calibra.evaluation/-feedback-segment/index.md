//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.evaluation](../index.md)/[FeedbackSegment](index.md)

# FeedbackSegment

[common]\
data class [FeedbackSegment](index.md)(val segmentIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val tStart: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val tEnd: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val score: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val feedbackType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))

Feedback for a single evaluated segment.

## Constructors

| | |
|---|---|
| [FeedbackSegment](-feedback-segment.md) | [common]<br/>constructor(segmentIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), tStart: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), tEnd: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), score: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), feedbackType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [feedbackType](feedback-type.md) | [common]<br/>val [feedbackType](feedback-type.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Type of feedback (platform-specific enum) |
| [score](score.md) | [common]<br/>val [score](score.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Score for this segment (0.0 - 1.0) |
| [segmentIndex](segment-index.md) | [common]<br/>val [segmentIndex](segment-index.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Index of the segment in the evaluation |
| [tEnd](t-end.md) | [common]<br/>val [tEnd](t-end.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>End time in seconds |
| [tStart](t-start.md) | [common]<br/>val [tStart](t-start.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Start time in seconds |
