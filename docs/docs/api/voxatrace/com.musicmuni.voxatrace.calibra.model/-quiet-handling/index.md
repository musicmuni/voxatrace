---
sidebar_label: "QuietHandling"
---


# QuietHandling

[common]\
enum [QuietHandling](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[QuietHandling](index.md)&gt; 

How to handle quiet audio.

Controls the amplitude gate threshold - frames below this RMS level are marked as unvoiced (silent).

## Entries

| | |
|---|---|
| [SENSITIVE](-s-e-n-s-i-t-i-v-e/index.md) | [common]<br/>[SENSITIVE](-s-e-n-s-i-t-i-v-e/index.md)<br/>Very quiet room, soft singing. Picks up quieter sounds. |
| [NORMAL](-n-o-r-m-a-l/index.md) | [common]<br/>[NORMAL](-n-o-r-m-a-l/index.md)<br/>Typical environment. Default setting. |
| [NOISY](-n-o-i-s-y/index.md) | [common]<br/>[NOISY](-n-o-i-s-y/index.md)<br/>Loud environment, only detect strong signal. |

## Properties

| Name | Summary |
|---|---|
| [name](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-372974862%2FProperties%2F-204059827) | [common]<br/>val [name](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-372974862%2FProperties%2F-204059827): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-739389684%2FProperties%2F-204059827) | [common]<br/>val [ordinal](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-739389684%2FProperties%2F-204059827): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br/>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [QuietHandling](index.md)<br/>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br/>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[QuietHandling](index.md)&gt;<br/>Returns an array containing the constants of this enum type, in the order they're declared. |
