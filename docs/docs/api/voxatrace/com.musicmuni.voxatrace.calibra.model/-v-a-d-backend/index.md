---
sidebar_label: "VADBackend"
---


# VADBackend

[common]\
enum [VADBackend](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[VADBackend](index.md)&gt; 

Available Voice Activity Detection backend engines.

Different backends offer trade-offs between accuracy, speed, and resource usage.

## Entries

| | |
|---|---|
| [SPEECH](-s-p-e-e-c-h/index.md) | [common]<br/>[SPEECH](-s-p-e-e-c-h/index.md)<br/>Speech-optimized VAD using Silero neural network. |
| [GENERAL](-g-e-n-e-r-a-l/index.md) | [common]<br/>[GENERAL](-g-e-n-e-r-a-l/index.md)<br/>General-purpose VAD using RMS-based heuristics. |
| [SINGING](-s-i-n-g-i-n-g/index.md) | [common]<br/>[SINGING](-s-i-n-g-i-n-g/index.md)<br/>Singing-optimized VAD using Essentia YAMNet. |
| [SINGING_REALTIME](-s-i-n-g-i-n-g_-r-e-a-l-t-i-m-e/index.md) | [common]<br/>[SINGING_REALTIME](-s-i-n-g-i-n-g_-r-e-a-l-t-i-m-e/index.md)<br/>Real-time singing VAD using pitch detection. |

## Properties

| Name | Summary |
|---|---|
| [name](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-372974862%2FProperties%2F-204059827) | [common]<br/>val [name](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-372974862%2FProperties%2F-204059827): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-739389684%2FProperties%2F-204059827) | [common]<br/>val [ordinal](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-739389684%2FProperties%2F-204059827): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br/>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [VADBackend](index.md)<br/>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br/>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[VADBackend](index.md)&gt;<br/>Returns an array containing the constants of this enum type, in the order they're declared. |
