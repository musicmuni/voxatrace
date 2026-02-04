---
sidebar_label: "AudioFormat"
---


# AudioFormat

[common]\
enum [AudioFormat](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[AudioFormat](index.md)&gt; 

Audio output format for recording.

## Entries

| | |
|---|---|
| [M4A](-m4-a/index.md) | [common]<br/>[M4A](-m4-a/index.md)<br/>AAC encoded in M4A container (default, best quality/size ratio) |
| [MP3](-m-p3/index.md) | [common]<br/>[MP3](-m-p3/index.md)<br/>MP3 encoded via LAME (widely compatible) |
| [WAV](-w-a-v/index.md) | [common]<br/>[WAV](-w-a-v/index.md)<br/>Uncompressed WAV (lossless, larger files) |

## Properties

| Name | Summary |
|---|---|
| [name](../-recording-quality/-h-i-g-h/index.md#-372974862%2FProperties%2F-204059827) | [common]<br/>val [name](../-recording-quality/-h-i-g-h/index.md#-372974862%2FProperties%2F-204059827): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../-recording-quality/-h-i-g-h/index.md#-739389684%2FProperties%2F-204059827) | [common]<br/>val [ordinal](../-recording-quality/-h-i-g-h/index.md#-739389684%2FProperties%2F-204059827): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br/>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [AudioFormat](index.md)<br/>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br/>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[AudioFormat](index.md)&gt;<br/>Returns an array containing the constants of this enum type, in the order they're declared. |
