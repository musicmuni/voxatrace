//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../index.md)/[NoiseGatePreset](index.md)

# NoiseGatePreset

[common]\
enum [NoiseGatePreset](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[NoiseGatePreset](index.md)&gt; 

Pre-configured noise gate settings for common scenarios.

## Entries

| | |
|---|---|
| [LIGHT](-l-i-g-h-t/index.md) | [common]<br/>[LIGHT](-l-i-g-h-t/index.md)<br/>Light gating for quiet environments |
| [STANDARD](-s-t-a-n-d-a-r-d/index.md) | [common]<br/>[STANDARD](-s-t-a-n-d-a-r-d/index.md)<br/>Standard gating for typical background noise |
| [AGGRESSIVE](-a-g-g-r-e-s-s-i-v-e/index.md) | [common]<br/>[AGGRESSIVE](-a-g-g-r-e-s-s-i-v-e/index.md)<br/>Aggressive gating for noisy environments |
| [TIGHT](-t-i-g-h-t/index.md) | [common]<br/>[TIGHT](-t-i-g-h-t/index.md)<br/>Very tight gate for maximum noise reduction |

## Properties

| Name | Summary |
|---|---|
| [name](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827) | [common]<br/>val [name](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827) | [common]<br/>val [ordinal](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br/>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [NoiseGatePreset](index.md)<br/>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br/>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[NoiseGatePreset](index.md)&gt;<br/>Returns an array containing the constants of this enum type, in the order they're declared. |
