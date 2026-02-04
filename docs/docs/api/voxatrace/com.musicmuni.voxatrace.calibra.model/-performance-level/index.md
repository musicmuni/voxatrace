---
sidebar_label: "PerformanceLevel"
---


# PerformanceLevel

[common]\
enum [PerformanceLevel](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[PerformanceLevel](index.md)&gt; 

Performance levels for singing evaluation.

Simple, score-based classification without detailed diagnostic feedback. The C++ native layer no longer provides rich feedback types like PITCH_LOW, TIMING_EARLY, etc. - those would require additional analysis that belongs at the app layer if needed.

## Entries

| | |
|---|---|
| [NEEDS_WORK](-n-e-e-d-s_-w-o-r-k/index.md) | [common]<br/>[NEEDS_WORK](-n-e-e-d-s_-w-o-r-k/index.md)<br/>Score < 0.3 - Significant improvement needed |
| [FAIR](-f-a-i-r/index.md) | [common]<br/>[FAIR](-f-a-i-r/index.md)<br/>Score 0.3 - 0.6 - Room for improvement |
| [GOOD](-g-o-o-d/index.md) | [common]<br/>[GOOD](-g-o-o-d/index.md)<br/>Score 0.6 - 0.8 - Solid performance |
| [VERY_GOOD](-v-e-r-y_-g-o-o-d/index.md) | [common]<br/>[VERY_GOOD](-v-e-r-y_-g-o-o-d/index.md)<br/>Score 0.8 - 0.95 - Very strong performance |
| [EXCELLENT](-e-x-c-e-l-l-e-n-t/index.md) | [common]<br/>[EXCELLENT](-e-x-c-e-l-l-e-n-t/index.md)<br/>Score >= 0.95 - Outstanding performance |
| [NOT_EVALUATED](-n-o-t_-e-v-a-l-u-a-t-e-d/index.md) | [common]<br/>[NOT_EVALUATED](-n-o-t_-e-v-a-l-u-a-t-e-d/index.md)<br/>Could not evaluate (insufficient data, etc.) |
| [NOT_DETECTED](-n-o-t_-d-e-t-e-c-t-e-d/index.md) | [common]<br/>[NOT_DETECTED](-n-o-t_-d-e-t-e-c-t-e-d/index.md)<br/>No voice detected during segment |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [displayName](display-name.md) | [common]<br/>val [displayName](display-name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Human-readable display name for UI |
| [name](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-372974862%2FProperties%2F-204059827) | [common]<br/>val [name](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-372974862%2FProperties%2F-204059827): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-739389684%2FProperties%2F-204059827) | [common]<br/>val [ordinal](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-739389684%2FProperties%2F-204059827): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br/>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [PerformanceLevel](index.md)<br/>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br/>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[PerformanceLevel](index.md)&gt;<br/>Returns an array containing the constants of this enum type, in the order they're declared. |
