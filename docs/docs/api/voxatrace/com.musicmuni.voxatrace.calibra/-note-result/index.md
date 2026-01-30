//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[NoteResult](index.md)

# NoteResult

[common]\
data class [NoteResult](index.md)(val noteIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val expectedFrequencyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val score: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val level: [PerformanceLevel](../../com.musicmuni.voxatrace.calibra.model/-performance-level/index.md) = PerformanceLevel.fromScore(score))

Result for a single note in an exercise.

## Constructors

| | |
|---|---|
| [NoteResult](-note-result.md) | [common]<br/>constructor(noteIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), expectedFrequencyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), score: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), level: [PerformanceLevel](../../com.musicmuni.voxatrace.calibra.model/-performance-level/index.md) = PerformanceLevel.fromScore(score)) |

## Properties

| Name | Summary |
|---|---|
| [expectedFrequencyHz](expected-frequency-hz.md) | [common]<br/>val [expectedFrequencyHz](expected-frequency-hz.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Expected frequency in Hz |
| [isPassing](is-passing.md) | [common]<br/>val [isPassing](is-passing.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Is note passing (>= 0.5) |
| [level](level.md) | [common]<br/>val [level](level.md): [PerformanceLevel](../../com.musicmuni.voxatrace.calibra.model/-performance-level/index.md)<br/>Performance level classification |
| [noteIndex](note-index.md) | [common]<br/>val [noteIndex](note-index.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Index of the note in the pattern |
| [score](score.md) | [common]<br/>val [score](score.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Score for this note (0.0 - 1.0) |
| [scorePercent](score-percent.md) | [common]<br/>val [scorePercent](score-percent.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Score as percentage |
