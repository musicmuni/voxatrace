//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[ExerciseResult](index.md)

# ExerciseResult

[common]\
data class [ExerciseResult](index.md)(val score: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val noteResults: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[NoteResult](../-note-result/index.md)&gt;, val keyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))

Result of an exercise evaluation.

## Constructors

| | |
|---|---|
| [ExerciseResult](-exercise-result.md) | [common]<br/>constructor(score: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), noteResults: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[NoteResult](../-note-result/index.md)&gt;, keyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [keyHz](key-hz.md) | [common]<br/>val [keyHz](key-hz.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Key frequency used for evaluation |
| [noteCount](note-count.md) | [common]<br/>val [noteCount](note-count.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of notes evaluated |
| [noteResults](note-results.md) | [common]<br/>val [noteResults](note-results.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[NoteResult](../-note-result/index.md)&gt;<br/>Per-note evaluation results |
| [passingNotes](passing-notes.md) | [common]<br/>val [passingNotes](passing-notes.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of notes with passing score (>= 0.5) |
| [passingRatio](passing-ratio.md) | [common]<br/>val [passingRatio](passing-ratio.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Ratio of passing notes |
| [score](score.md) | [common]<br/>val [score](score.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Overall score (0.0 - 1.0) |
| [scorePercent](score-percent.md) | [common]<br/>val [scorePercent](score-percent.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Score as a percentage (0-100) |
