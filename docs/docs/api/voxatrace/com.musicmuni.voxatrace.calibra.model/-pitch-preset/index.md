//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../index.md)/[PitchPreset](index.md)

# PitchPreset

[common]\
enum [PitchPreset](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[PitchPreset](index.md)&gt; 

Pre-configured pitch detection presets.

Controls the resolution/accuracy trade-off:

- 
   Buffer size: larger = better frequency resolution, higher latency
- 
   Tolerance: lower = more accurate pitch detection, may miss quiet notes

Use [VoiceType](../-voice-type/index.md) to set frequency range for specific voice types.

## Entries

| | |
|---|---|
| [RESPONSIVE](-r-e-s-p-o-n-s-i-v-e/index.md) | [common]<br/>[RESPONSIVE](-r-e-s-p-o-n-s-i-v-e/index.md)<br/>Fast response, good for live visualization. Lower resolution, higher recall. |
| [BALANCED](-b-a-l-a-n-c-e-d/index.md) | [common]<br/>[BALANCED](-b-a-l-a-n-c-e-d/index.md)<br/>Balanced for most use cases (karaoke, games). |
| [PRECISE](-p-r-e-c-i-s-e/index.md) | [common]<br/>[PRECISE](-p-r-e-c-i-s-e/index.md)<br/>High precision for analysis and scoring. Best resolution, higher accuracy. |

## Properties

| Name | Summary |
|---|---|
| [name](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827) | [common]<br/>val [name](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827) | [common]<br/>val [ordinal](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br/>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [PitchPreset](index.md)<br/>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br/>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[PitchPreset](index.md)&gt;<br/>Returns an array containing the constants of this enum type, in the order they're declared. |
