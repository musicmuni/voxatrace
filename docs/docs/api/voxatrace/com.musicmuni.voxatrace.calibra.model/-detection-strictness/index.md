---
sidebar_label: "DetectionStrictness"
---


# DetectionStrictness

[common]\
enum [DetectionStrictness](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[DetectionStrictness](index.md)&gt; 

How strict to be about pitch detection.

Controls the YIN confidence threshold - detections below this confidence level are marked as unvoiced.

## Entries

| | |
|---|---|
| [STRICT](-s-t-r-i-c-t/index.md) | [common]<br/>[STRICT](-s-t-r-i-c-t/index.md)<br/>Fewer false positives, may miss some quiet notes |
| [BALANCED](-b-a-l-a-n-c-e-d/index.md) | [common]<br/>[BALANCED](-b-a-l-a-n-c-e-d/index.md)<br/>Balanced detection. Default setting. |
| [LENIENT](-l-e-n-i-e-n-t/index.md) | [common]<br/>[LENIENT](-l-e-n-i-e-n-t/index.md)<br/>Catches more notes, may have some noise |

## Properties

| Name | Summary |
|---|---|
| [name](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-372974862%2FProperties%2F-204059827) | [common]<br/>val [name](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-372974862%2FProperties%2F-204059827): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-739389684%2FProperties%2F-204059827) | [common]<br/>val [ordinal](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-739389684%2FProperties%2F-204059827): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br/>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [DetectionStrictness](index.md)<br/>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br/>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[DetectionStrictness](index.md)&gt;<br/>Returns an array containing the constants of this enum type, in the order they're declared. |
