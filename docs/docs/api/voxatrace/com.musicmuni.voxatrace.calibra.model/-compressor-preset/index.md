//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../index.md)/[CompressorPreset](index.md)

# CompressorPreset

[common]\
enum [CompressorPreset](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[CompressorPreset](index.md)&gt; 

Pre-configured compressor settings for common use cases.

## Entries

| | |
|---|---|
| [VOCALS](-v-o-c-a-l-s/index.md) | [common]<br/>[VOCALS](-v-o-c-a-l-s/index.md)<br/>Balanced compression for vocals with natural dynamics |
| [VOICE_LEVELER](-v-o-i-c-e_-l-e-v-e-l-e-r/index.md) | [common]<br/>[VOICE_LEVELER](-v-o-i-c-e_-l-e-v-e-l-e-r/index.md)<br/>Aggressive leveling for consistent voice volume |
| [SPEECH](-s-p-e-e-c-h/index.md) | [common]<br/>[SPEECH](-s-p-e-e-c-h/index.md)<br/>Gentle compression for spoken word |
| [BROADCAST](-b-r-o-a-d-c-a-s-t/index.md) | [common]<br/>[BROADCAST](-b-r-o-a-d-c-a-s-t/index.md)<br/>Heavy limiting for broadcast consistency |
| [TRANSPARENT](-t-r-a-n-s-p-a-r-e-n-t/index.md) | [common]<br/>[TRANSPARENT](-t-r-a-n-s-p-a-r-e-n-t/index.md)<br/>Subtle compression that preserves dynamics |

## Properties

| Name | Summary |
|---|---|
| [name](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827) | [common]<br/>val [name](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827) | [common]<br/>val [ordinal](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br/>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [CompressorPreset](index.md)<br/>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br/>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[CompressorPreset](index.md)&gt;<br/>Returns an array containing the constants of this enum type, in the order they're declared. |
