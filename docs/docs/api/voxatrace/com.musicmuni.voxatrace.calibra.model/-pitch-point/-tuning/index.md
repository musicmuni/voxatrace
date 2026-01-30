//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../../index.md)/[PitchPoint](../index.md)/[Tuning](index.md)

# Tuning

[common]\
enum [Tuning](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[PitchPoint.Tuning](index.md)&gt; 

Tuning status relative to nearest musical note

## Entries

| | |
|---|---|
| [SILENT](-s-i-l-e-n-t/index.md) | [common]<br/>[SILENT](-s-i-l-e-n-t/index.md)<br/>No pitch detected (silent/unvoiced) |
| [FLAT](-f-l-a-t/index.md) | [common]<br/>[FLAT](-f-l-a-t/index.md)<br/>More than 10 cents below the note |
| [IN_TUNE](-i-n_-t-u-n-e/index.md) | [common]<br/>[IN_TUNE](-i-n_-t-u-n-e/index.md)<br/>Within 10 cents of the note |
| [SHARP](-s-h-a-r-p/index.md) | [common]<br/>[SHARP](-s-h-a-r-p/index.md)<br/>More than 10 cents above the note |

## Properties

| Name | Summary |
|---|---|
| [name](../../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827) | [common]<br/>val [name](../../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827) | [common]<br/>val [ordinal](../../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br/>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [PitchPoint.Tuning](index.md)<br/>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br/>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[PitchPoint.Tuning](index.md)&gt;<br/>Returns an array containing the constants of this enum type, in the order they're declared. |
