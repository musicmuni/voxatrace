//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../index.md)/[SessionPhase](index.md)

# SessionPhase

[common]\
enum [SessionPhase](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[SessionPhase](index.md)&gt; 

Current phase of a CalibraLiveEval.

## Entries

| | |
|---|---|
| [IDLE](-i-d-l-e/index.md) | [common]<br/>[IDLE](-i-d-l-e/index.md)<br/>Session created but not started |
| [READY](-r-e-a-d-y/index.md) | [common]<br/>[READY](-r-e-a-d-y/index.md)<br/>Reference loaded, ready to begin practicing |
| [PRACTICING](-p-r-a-c-t-i-c-i-n-g/index.md) | [common]<br/>[PRACTICING](-p-r-a-c-t-i-c-i-n-g/index.md)<br/>Actively capturing and evaluating audio for a segment |
| [BETWEEN_SEGMENTS](-b-e-t-w-e-e-n_-s-e-g-m-e-n-t-s/index.md) | [common]<br/>[BETWEEN_SEGMENTS](-b-e-t-w-e-e-n_-s-e-g-m-e-n-t-s/index.md)<br/>Finished one segment, waiting before next (for auto-advance delay) |
| [COMPLETED](-c-o-m-p-l-e-t-e-d/index.md) | [common]<br/>[COMPLETED](-c-o-m-p-l-e-t-e-d/index.md)<br/>All segments completed or session manually finished |
| [CANCELLED](-c-a-n-c-e-l-l-e-d/index.md) | [common]<br/>[CANCELLED](-c-a-n-c-e-l-l-e-d/index.md)<br/>Session was cancelled |
| [ERROR](-e-r-r-o-r/index.md) | [common]<br/>[ERROR](-e-r-r-o-r/index.md)<br/>An error occurred |

## Properties

| Name | Summary |
|---|---|
| [name](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827) | [common]<br/>val [name](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827) | [common]<br/>val [ordinal](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br/>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [SessionPhase](index.md)<br/>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br/>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SessionPhase](index.md)&gt;<br/>Returns an array containing the constants of this enum type, in the order they're declared. |
