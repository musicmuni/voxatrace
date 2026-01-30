//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../index.md)/[Segment](index.md)

# Segment

[common]\
data class [Segment](index.md)(val index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val startSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val endSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val lyrics: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) = &quot;&quot;, val studentStartSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)? = null, val studentEndSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)? = null)

A segment of a song or exercise with timing and optional lyrics.

Supports both singalong (student sings with reference) and singafter (student sings after reference) modes via optional student timing fields.

## Constructors

| | |
|---|---|
| [Segment](-segment.md) | [common]<br/>constructor(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), startSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), endSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), lyrics: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) = &quot;&quot;, studentStartSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)? = null, studentEndSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)? = null) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [duration](duration.md) | [common]<br/>val [duration](duration.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Duration of the segment in seconds |
| [effectiveStudentEnd](effective-student-end.md) | [common]<br/>val [effectiveStudentEnd](effective-student-end.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Effective student end time (falls back to segment end for singalong) |
| [effectiveStudentStart](effective-student-start.md) | [common]<br/>val [effectiveStudentStart](effective-student-start.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Effective student start time (falls back to segment start for singalong) |
| [endSeconds](end-seconds.md) | [common]<br/>val [endSeconds](end-seconds.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Reference audio end time in seconds |
| [index](--index--.md) | [common]<br/>val [index](--index--.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Zero-based index of the segment |
| [isSingafter](is-singafter.md) | [common]<br/>val [isSingafter](is-singafter.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>True if this is a singafter segment (student starts after reference) |
| [lyrics](lyrics.md) | [common]<br/>val [lyrics](lyrics.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Text/lyrics for this segment (optional) |
| [startSeconds](start-seconds.md) | [common]<br/>val [startSeconds](start-seconds.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Reference audio start time in seconds |
| [studentDuration](student-duration.md) | [common]<br/>val [studentDuration](student-duration.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Duration of the student recording portion in seconds |
| [studentEndSeconds](student-end-seconds.md) | [common]<br/>val [studentEndSeconds](student-end-seconds.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)? = null<br/>When student recording ends (null = same as endSeconds) |
| [studentStartSeconds](student-start-seconds.md) | [common]<br/>val [studentStartSeconds](student-start-seconds.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)? = null<br/>When student recording starts (null = same as startSeconds) |
