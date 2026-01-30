//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.evaluation](../index.md)/[NoteEvaluator](index.md)

# NoteEvaluator

[common]\
class [NoteEvaluator](index.md)

Pure Kotlin note/svara evaluator.

Ported from native/src/calibra/core/evaluation/NoteEvaluator.cpp for the &quot;Kotlin-max, C-min&quot; migration.

Evaluates how well a student hits individual notes (svaras) by comparing their pitch contour against expected note frequencies.

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |
| [NoteScoreType](-note-score-type/index.md) | [common]<br/>enum [NoteScoreType](-note-score-type/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[NoteEvaluator.NoteScoreType](-note-score-type/index.md)&gt; <br/>Scoring types for note evaluation. |

## Functions

| Name | Summary |
|---|---|
| [destroy](destroy.md) | [common]<br/>fun [destroy](destroy.md)()<br/>Release resources (no-op for pure Kotlin implementation). |
| [evaluate](evaluate.md) | [common]<br/>fun [evaluate](evaluate.md)(refFreqsHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), refDurationsMs: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html), studentTimes: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), studentPitches: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), notesPerLoop: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;&lt;[Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[FeedbackSegment](../-feedback-segment/index.md)&gt;&gt;<br/>Evaluate a note/svara exercise performance. |
| [evaluateWithSegments](evaluate-with-segments.md) | [common]<br/>fun [evaluateWithSegments](evaluate-with-segments.md)(notes: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SvaraFeedbackGenerator.Note](../../com.musicmuni.voxatrace.calibra.feedback/-svara-feedback-generator/-note/index.md)&gt;, studentTimes: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), studentPitchesMidi: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): &lt;Error class: unknown class&gt;&lt;[Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SvaraFeedbackGenerator.SvaraResult](../../com.musicmuni.voxatrace.calibra.feedback/-svara-feedback-generator/-svara-result/index.md)&gt;&gt;<br/>Evaluate notes with pre-computed segment indices. |
| [getKeyShift](get-key-shift.md) | [common]<br/>fun [getKeyShift](get-key-shift.md)(): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Get the key shift in semitones (student - reference). |
| [getLeewaySamples](get-leeway-samples.md) | [common]<br/>fun [getLeewaySamples](get-leeway-samples.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Get the configured leeway samples. |
| [getScoreType](get-score-type.md) | [common]<br/>fun [getScoreType](get-score-type.md)(): [NoteEvaluator.NoteScoreType](-note-score-type/index.md)<br/>Get the configured score type. |
