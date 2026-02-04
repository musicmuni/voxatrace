---
sidebar_label: "EvaluatorPreset"
---


# EvaluatorPreset

[common]\
enum [EvaluatorPreset](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[EvaluatorPreset](index.md)&gt; 

Pre-configured evaluator settings for common use cases.

## Entries

| | |
|---|---|
| [STANDARD](-s-t-a-n-d-a-r-d/index.md) | [common]<br/>[STANDARD](-s-t-a-n-d-a-r-d/index.md)<br/>Standard evaluation at original pitch |
| [MALE_TO_FEMALE](-m-a-l-e_-t-o_-f-e-m-a-l-e/index.md) | [common]<br/>[MALE_TO_FEMALE](-m-a-l-e_-t-o_-f-e-m-a-l-e/index.md)<br/>Transpose up one octave for male singers matching female reference |
| [FEMALE_TO_MALE](-f-e-m-a-l-e_-t-o_-m-a-l-e/index.md) | [common]<br/>[FEMALE_TO_MALE](-f-e-m-a-l-e_-t-o_-m-a-l-e/index.md)<br/>Transpose down one octave for female singers matching male reference |
| [TRANSPOSE_UP](-t-r-a-n-s-p-o-s-e_-u-p/index.md) | [common]<br/>[TRANSPOSE_UP](-t-r-a-n-s-p-o-s-e_-u-p/index.md)<br/>Slight pitch adjustment for comfortable range |
| [TRANSPOSE_DOWN](-t-r-a-n-s-p-o-s-e_-d-o-w-n/index.md) | [common]<br/>[TRANSPOSE_DOWN](-t-r-a-n-s-p-o-s-e_-d-o-w-n/index.md)<br/>Slight pitch adjustment for comfortable range |

## Properties

| Name | Summary |
|---|---|
| [name](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-372974862%2FProperties%2F-204059827) | [common]<br/>val [name](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-372974862%2FProperties%2F-204059827): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-739389684%2FProperties%2F-204059827) | [common]<br/>val [ordinal](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-739389684%2FProperties%2F-204059827): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br/>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [EvaluatorPreset](index.md)<br/>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br/>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[EvaluatorPreset](index.md)&gt;<br/>Returns an array containing the constants of this enum type, in the order they're declared. |
