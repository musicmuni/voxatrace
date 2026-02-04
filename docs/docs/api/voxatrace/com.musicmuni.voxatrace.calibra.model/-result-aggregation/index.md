---
sidebar_label: "ResultAggregation"
---


# ResultAggregation

[common]\
enum [ResultAggregation](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[ResultAggregation](index.md)&gt; 

How to aggregate multiple attempts per segment into a final score.

## Entries

| | |
|---|---|
| [LATEST](-l-a-t-e-s-t/index.md) | [common]<br/>[LATEST](-l-a-t-e-s-t/index.md)<br/>Use the most recent attempt's score |
| [BEST](-b-e-s-t/index.md) | [common]<br/>[BEST](-b-e-s-t/index.md)<br/>Use the highest score across all attempts |
| [AVERAGE](-a-v-e-r-a-g-e/index.md) | [common]<br/>[AVERAGE](-a-v-e-r-a-g-e/index.md)<br/>Use the average of all attempts |

## Properties

| Name | Summary |
|---|---|
| [name](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-372974862%2FProperties%2F-204059827) | [common]<br/>val [name](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-372974862%2FProperties%2F-204059827): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-739389684%2FProperties%2F-204059827) | [common]<br/>val [ordinal](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-739389684%2FProperties%2F-204059827): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br/>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [ResultAggregation](index.md)<br/>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br/>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[ResultAggregation](index.md)&gt;<br/>Returns an array containing the constants of this enum type, in the order they're declared. |
