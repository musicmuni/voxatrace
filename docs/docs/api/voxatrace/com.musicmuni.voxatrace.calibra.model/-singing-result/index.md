//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../index.md)/[SingingResult](index.md)

# SingingResult

[common]\
data class [SingingResult](index.md)(val overallScore: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val segmentResults: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[SegmentResult](../-segment-result/index.md)&gt;&gt;, val aggregation: [ResultAggregation](../-result-aggregation/index.md) = ResultAggregation.LATEST)

Complete result of a singing evaluation session.

## Constructors

| | |
|---|---|
| [SingingResult](-singing-result.md) | [common]<br/>constructor(overallScore: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), segmentResults: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[SegmentResult](../-segment-result/index.md)&gt;&gt;, aggregation: [ResultAggregation](../-result-aggregation/index.md) = ResultAggregation.LATEST) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [aggregation](aggregation.md) | [common]<br/>val [aggregation](aggregation.md): [ResultAggregation](../-result-aggregation/index.md)<br/>How the overall score was calculated |
| [allPassing](all-passing.md) | [common]<br/>val [allPassing](all-passing.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>True if all segments pass (score >= 0.5) |
| [overallScore](overall-score.md) | [common]<br/>val [overallScore](overall-score.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Aggregate score across all segments (0.0 - 1.0) |
| [overallScorePercent](overall-score-percent.md) | [common]<br/>val [overallScorePercent](overall-score-percent.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Overall score as a percentage (0-100) |
| [segmentCount](segment-count.md) | [common]<br/>val [segmentCount](segment-count.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of segments evaluated |
| [segmentResults](segment-results.md) | [common]<br/>val [segmentResults](segment-results.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[SegmentResult](../-segment-result/index.md)&gt;&gt;<br/>Map of segment index to list of attempts (for retry history) |
| [totalAttempts](total-attempts.md) | [common]<br/>val [totalAttempts](total-attempts.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of total attempts across all segments |

## Functions

| Name | Summary |
|---|---|
| [averageScorePerSegment](average-score-per-segment.md) | [common]<br/>fun [averageScorePerSegment](average-score-per-segment.md)(): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)&gt;<br/>Get the average score for each segment. |
| [bestScorePerSegment](best-score-per-segment.md) | [common]<br/>fun [bestScorePerSegment](best-score-per-segment.md)(): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)&gt;<br/>Get the best score for each segment. |
| [getAllFeedback](get-all-feedback.md) | [common]<br/>fun [getAllFeedback](get-all-feedback.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt;<br/>Get feedback messages for all segments. |
| [latestResultPerSegment](latest-result-per-segment.md) | [common]<br/>fun [latestResultPerSegment](latest-result-per-segment.md)(): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), [SegmentResult](../-segment-result/index.md)&gt;<br/>Get the latest result for each segment. |
| [latestScorePerSegment](latest-score-per-segment.md) | [common]<br/>fun [latestScorePerSegment](latest-score-per-segment.md)(): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)&gt;<br/>Get the latest score for each segment. |
