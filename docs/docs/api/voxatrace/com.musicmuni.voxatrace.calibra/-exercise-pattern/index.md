---
sidebar_label: "ExercisePattern"
---


# ExercisePattern

[common]\
data class [ExercisePattern](index.md)(val noteFrequencies: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)&gt;, val noteDurations: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)&gt;, val notesPerLoop: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = noteFrequencies.size)

Pattern for exercise evaluation (scales, arpeggios, etc.).

## Constructors

| | |
|---|---|
| [ExercisePattern](-exercise-pattern.md) | [common]<br/>constructor(noteFrequencies: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)&gt;, noteDurations: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)&gt;, notesPerLoop: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = noteFrequencies.size) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [noteCount](note-count.md) | [common]<br/>val [noteCount](note-count.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of notes in the pattern |
| [noteDurations](note-durations.md) | [common]<br/>val [noteDurations](note-durations.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)&gt;<br/>Duration in milliseconds for each note |
| [noteFrequencies](note-frequencies.md) | [common]<br/>val [noteFrequencies](note-frequencies.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)&gt;<br/>Frequencies in Hz for each note in the pattern |
| [notesPerLoop](notes-per-loop.md) | [common]<br/>val [notesPerLoop](notes-per-loop.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of notes per loop/cycle (for repeating patterns) |
| [totalDurationMs](total-duration-ms.md) | [common]<br/>val [totalDurationMs](total-duration-ms.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Total duration of the pattern in milliseconds |
