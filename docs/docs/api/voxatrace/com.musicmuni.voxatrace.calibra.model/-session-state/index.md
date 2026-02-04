---
sidebar_label: "SessionState"
---


# SessionState

[common]\
data class [SessionState](index.md)(val phase: [SessionPhase](../-session-phase/index.md) = SessionPhase.IDLE, val activeSegmentIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)? = null, val activeSegment: [Segment](../-segment/index.md)? = null, val currentPitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = -1f, val currentAmplitude: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, val segmentProgress: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, val completedSegments: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)&gt; = emptySet(), val error: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null)

Current state of a CalibraLiveEval.

## Constructors

| | |
|---|---|
| [SessionState](-session-state.md) | [common]<br/>constructor(phase: [SessionPhase](../-session-phase/index.md) = SessionPhase.IDLE, activeSegmentIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)? = null, activeSegment: [Segment](../-segment/index.md)? = null, currentPitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = -1f, currentAmplitude: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, segmentProgress: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, completedSegments: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)&gt; = emptySet(), error: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [activeSegment](active-segment.md) | [common]<br/>val [activeSegment](active-segment.md): [Segment](../-segment/index.md)? = null<br/>The segment being practiced, or null if none |
| [activeSegmentIndex](active-segment-index.md) | [common]<br/>val [activeSegmentIndex](active-segment-index.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)? = null<br/>Index of segment being practiced, or null if none |
| [canBeginSegment](can-begin-segment.md) | [common]<br/>val [canBeginSegment](can-begin-segment.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>True if session is ready to start or between segments |
| [completedCount](completed-count.md) | [common]<br/>val [completedCount](completed-count.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of completed segments |
| [completedSegments](completed-segments.md) | [common]<br/>val [completedSegments](completed-segments.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)&gt;<br/>Set of segment indices that have been completed |
| [currentAmplitude](current-amplitude.md) | [common]<br/>val [currentAmplitude](current-amplitude.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f<br/>Current audio amplitude (0.0 - 1.0) |
| [currentPitch](current-pitch.md) | [common]<br/>val [currentPitch](current-pitch.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Current detected pitch in Hz (-1 for unvoiced) |
| [error](error.md) | [common]<br/>val [error](error.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null<br/>Error message if phase is ERROR, null otherwise |
| [isFinished](is-finished.md) | [common]<br/>val [isFinished](is-finished.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>True if session is finished (completed or cancelled) |
| [isPracticing](is-practicing.md) | [common]<br/>val [isPracticing](is-practicing.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>True if session is actively practicing |
| [phase](phase.md) | [common]<br/>val [phase](phase.md): [SessionPhase](../-session-phase/index.md)<br/>Current phase of the session |
| [segmentProgress](segment-progress.md) | [common]<br/>val [segmentProgress](segment-progress.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f<br/>Progress through current segment (0.0 - 1.0) |
