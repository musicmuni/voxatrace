//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.player](../index.md)/[VolumeEasing](index.md)

# VolumeEasing

[common]\
enum [VolumeEasing](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[VolumeEasing](index.md)&gt; 

Easing curves for volume transitions.

## Entries

| | |
|---|---|
| [Linear](-linear/index.md) | [common]<br/>[Linear](-linear/index.md)<br/>Linear interpolation |
| [EaseIn](-ease-in/index.md) | [common]<br/>[EaseIn](-ease-in/index.md)<br/>Slow start, fast end |
| [EaseOut](-ease-out/index.md) | [common]<br/>[EaseOut](-ease-out/index.md)<br/>Fast start, slow end |
| [EaseInOut](-ease-in-out/index.md) | [common]<br/>[EaseInOut](-ease-in-out/index.md)<br/>Slow start and end |

## Properties

| Name | Summary |
|---|---|
| [name](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827) | [common]<br/>val [name](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827) | [common]<br/>val [ordinal](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br/>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [VolumeEasing](index.md)<br/>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br/>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[VolumeEasing](index.md)&gt;<br/>Returns an array containing the constants of this enum type, in the order they're declared. |
