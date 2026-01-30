//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../index.md)/[NoteEvalConfig](index.md)

# NoteEvalConfig

[common]\
data class [NoteEvalConfig](index.md)(val algorithm: [ScoringAlgorithm](../-scoring-algorithm/index.md) = ScoringAlgorithm.SIMPLE, val boundaryToleranceMs: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0)

Configuration for note evaluation scoring.

## Constructors

| | |
|---|---|
| [NoteEvalConfig](-note-eval-config.md) | [common]<br/>constructor(algorithm: [ScoringAlgorithm](../-scoring-algorithm/index.md) = ScoringAlgorithm.SIMPLE, boundaryToleranceMs: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [algorithm](algorithm.md) | [common]<br/>val [algorithm](algorithm.md): [ScoringAlgorithm](../-scoring-algorithm/index.md)<br/>Algorithm for computing scores |
| [boundaryToleranceMs](boundary-tolerance-ms.md) | [common]<br/>val [boundaryToleranceMs](boundary-tolerance-ms.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0<br/>Milliseconds to skip at note start/end (0 = evaluate entire note) |
