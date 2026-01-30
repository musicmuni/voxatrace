//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../index.md)/[PitchAlgorithm](index.md)

# PitchAlgorithm

[common]\
enum [PitchAlgorithm](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[PitchAlgorithm](index.md)&gt; 

Pitch detection algorithm backend.

## Entries

| | |
|---|---|
| [SWIFT_F0](-s-w-i-f-t_-f0/index.md) | [common]<br/>[SWIFT_F0](-s-w-i-f-t_-f0/index.md)<br/>SwiftF0 neural network - batch-oriented, higher accuracy for vocals. Achieves 91.80% harmonic mean at 10dB SNR with only 95k parameters. Default algorithm. |
| [YIN](-y-i-n/index.md) | [common]<br/>[YIN](-y-i-n/index.md)<br/>YIN algorithm - realtime frame-by-frame, pure DSP with no ML dependency. Good for consumers who prefer a pure DSP approach or specific edge cases. |

## Properties

| Name | Summary |
|---|---|
| [name](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827) | [common]<br/>val [name](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827) | [common]<br/>val [ordinal](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br/>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [PitchAlgorithm](index.md)<br/>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br/>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[PitchAlgorithm](index.md)&gt;<br/>Returns an array containing the constants of this enum type, in the order they're declared. |
