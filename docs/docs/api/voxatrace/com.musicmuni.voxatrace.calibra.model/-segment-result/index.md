//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../index.md)/[SegmentResult](index.md)

# SegmentResult

[common]\
data class [SegmentResult](index.md)(val segment: [Segment](../-segment/index.md), val score: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val pitchAccuracy: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = score, val level: [PerformanceLevel](../-performance-level/index.md) = PerformanceLevel.fromScore(score), val attemptNumber: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1, val referencePitch: [PitchContour](../-pitch-contour/index.md) = PitchContour.EMPTY, val studentPitch: [PitchContour](../-pitch-contour/index.md) = PitchContour.EMPTY)

Result of evaluating a single segment.

## Constructors

| | |
|---|---|
| [SegmentResult](-segment-result.md) | [common]<br/>constructor(segment: [Segment](../-segment/index.md), score: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), pitchAccuracy: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = score, level: [PerformanceLevel](../-performance-level/index.md) = PerformanceLevel.fromScore(score), attemptNumber: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1, referencePitch: [PitchContour](../-pitch-contour/index.md) = PitchContour.EMPTY, studentPitch: [PitchContour](../-pitch-contour/index.md) = PitchContour.EMPTY) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [attemptNumber](attempt-number.md) | [common]<br/>val [attemptNumber](attempt-number.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1<br/>Which attempt this is (1-based, for retry tracking) |
| [feedbackMessage](feedback-message.md) | [common]<br/>val [feedbackMessage](feedback-message.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Human-readable feedback message based on performance level. |
| [isExcellent](is-excellent.md) | [common]<br/>val [isExcellent](is-excellent.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>True if score is excellent (>= 0.9) |
| [isGood](is-good.md) | [common]<br/>val [isGood](is-good.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>True if score is good (>= 0.7) |
| [isPassing](is-passing.md) | [common]<br/>val [isPassing](is-passing.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>True if score is passing (>= 0.5) |
| [level](level.md) | [common]<br/>val [level](level.md): [PerformanceLevel](../-performance-level/index.md)<br/>Performance level classification |
| [pitchAccuracy](pitch-accuracy.md) | [common]<br/>val [pitchAccuracy](pitch-accuracy.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Pitch accuracy component of the score (0.0 - 1.0) |
| [referencePitch](reference-pitch.md) | [common]<br/>val [referencePitch](reference-pitch.md): [PitchContour](../-pitch-contour/index.md)<br/>Reference pitch contour with times and Hz values (for visualization) |
| [score](score.md) | [common]<br/>val [score](score.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Overall score for this segment (0.0 - 1.0) |
| [scorePercent](score-percent.md) | [common]<br/>val [scorePercent](score-percent.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Score as a percentage (0-100) |
| [segment](segment.md) | [common]<br/>val [segment](segment.md): [Segment](../-segment/index.md)<br/>The segment that was evaluated |
| [studentPitch](student-pitch.md) | [common]<br/>val [studentPitch](student-pitch.md): [PitchContour](../-pitch-contour/index.md)<br/>Student pitch contour with times and Hz values (for visualization) |
