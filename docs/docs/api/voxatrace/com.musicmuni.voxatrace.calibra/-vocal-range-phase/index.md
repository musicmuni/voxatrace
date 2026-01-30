//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[VocalRangePhase](index.md)

# VocalRangePhase

[common]\
enum [VocalRangePhase](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[VocalRangePhase](index.md)&gt; 

Phases in the vocal range detection flow.

## Entries

| | |
|---|---|
| [IDLE](-i-d-l-e/index.md) | [common]<br/>[IDLE](-i-d-l-e/index.md)<br/>Session not started |
| [COUNTDOWN](-c-o-u-n-t-d-o-w-n/index.md) | [common]<br/>[COUNTDOWN](-c-o-u-n-t-d-o-w-n/index.md)<br/>Countdown before detection starts |
| [DETECTING_LOW](-d-e-t-e-c-t-i-n-g_-l-o-w/index.md) | [common]<br/>[DETECTING_LOW](-d-e-t-e-c-t-i-n-g_-l-o-w/index.md)<br/>Detecting lowest comfortable note |
| [TRANSITION](-t-r-a-n-s-i-t-i-o-n/index.md) | [common]<br/>[TRANSITION](-t-r-a-n-s-i-t-i-o-n/index.md)<br/>Transition between low and high detection |
| [DETECTING_HIGH](-d-e-t-e-c-t-i-n-g_-h-i-g-h/index.md) | [common]<br/>[DETECTING_HIGH](-d-e-t-e-c-t-i-n-g_-h-i-g-h/index.md)<br/>Detecting highest comfortable note |
| [COMPLETE](-c-o-m-p-l-e-t-e/index.md) | [common]<br/>[COMPLETE](-c-o-m-p-l-e-t-e/index.md)<br/>Detection complete, results available |
| [CANCELLED](-c-a-n-c-e-l-l-e-d/index.md) | [common]<br/>[CANCELLED](-c-a-n-c-e-l-l-e-d/index.md)<br/>Detection cancelled or failed |

## Properties

| Name | Summary |
|---|---|
| [name](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827) | [common]<br/>val [name](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827) | [common]<br/>val [ordinal](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br/>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [VocalRangePhase](index.md)<br/>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br/>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[VocalRangePhase](index.md)&gt;<br/>Returns an array containing the constants of this enum type, in the order they're declared. |
