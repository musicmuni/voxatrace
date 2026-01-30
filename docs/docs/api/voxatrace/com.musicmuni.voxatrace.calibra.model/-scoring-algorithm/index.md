//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../index.md)/[ScoringAlgorithm](index.md)

# ScoringAlgorithm

[common]\
enum [ScoringAlgorithm](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[ScoringAlgorithm](index.md)&gt; 

Algorithm for computing note accuracy scores.

## Entries

| | |
|---|---|
| [SIMPLE](-s-i-m-p-l-e/index.md) | [common]<br/>[SIMPLE](-s-i-m-p-l-e/index.md)<br/>Simple threshold counting. Counts what percentage of pitch samples fall within 35 cents of target. Good for beginners and practice. |
| [WEIGHTED](-w-e-i-g-h-t-e-d/index.md) | [common]<br/>[WEIGHTED](-w-e-i-g-h-t-e-d/index.md)<br/>Weighted duration-aware scoring. Uses tighter thresholds and considers note duration. Stricter on longer notes, more forgiving on short notes. Good for advanced evaluation. |

## Properties

| Name | Summary |
|---|---|
| [name](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827) | [common]<br/>val [name](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827) | [common]<br/>val [ordinal](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br/>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [ScoringAlgorithm](index.md)<br/>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br/>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[ScoringAlgorithm](index.md)&gt;<br/>Returns an array containing the constants of this enum type, in the order they're declared. |
