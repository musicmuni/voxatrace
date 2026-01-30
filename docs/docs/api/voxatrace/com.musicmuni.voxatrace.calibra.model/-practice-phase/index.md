//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../index.md)/[PracticePhase](index.md)

# PracticePhase

[common]\
enum [PracticePhase](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[PracticePhase](index.md)&gt; 

Practice phase during a CalibraLiveEval session.

## Phase Progressions

**Singalong:**`IDLE → SINGING → EVALUATED`

- 
   Student sings with the reference audio simultaneously

**Singafter:**`IDLE → LISTENING → SINGING → EVALUATED`

- 
   Student listens to reference first, then sings during their turn

## Entries

| | |
|---|---|
| [IDLE](-i-d-l-e/index.md) | [common]<br/>[IDLE](-i-d-l-e/index.md)<br/>Not practicing - waiting to start |
| [LISTENING](-l-i-s-t-e-n-i-n-g/index.md) | [common]<br/>[LISTENING](-l-i-s-t-e-n-i-n-g/index.md)<br/>Reference playing, student not recording yet (singafter only) |
| [SINGING](-s-i-n-g-i-n-g/index.md) | [common]<br/>[SINGING](-s-i-n-g-i-n-g/index.md)<br/>Student is being recorded and evaluated |
| [EVALUATED](-e-v-a-l-u-a-t-e-d/index.md) | [common]<br/>[EVALUATED](-e-v-a-l-u-a-t-e-d/index.md)<br/>Segment complete, score available |

## Properties

| Name | Summary |
|---|---|
| [name](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827) | [common]<br/>val [name](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827) | [common]<br/>val [ordinal](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br/>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [PracticePhase](index.md)<br/>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br/>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[PracticePhase](index.md)&gt;<br/>Returns an array containing the constants of this enum type, in the order they're declared. |
