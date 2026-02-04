---
sidebar_label: "EffectsPreset"
---


# EffectsPreset

[common]\
enum [EffectsPreset](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[EffectsPreset](index.md)&gt; 

Pre-configured effects chain combinations for common use cases.

These presets combine noise gate, compressor, and reverb settings optimized for specific scenarios.

## Entries

| | |
|---|---|
| [VOCAL_CHAIN](-v-o-c-a-l_-c-h-a-i-n/index.md) | [common]<br/>[VOCAL_CHAIN](-v-o-c-a-l_-c-h-a-i-n/index.md)<br/>Full vocal chain: gate → compression → reverb |
| [PRACTICE](-p-r-a-c-t-i-c-e/index.md) | [common]<br/>[PRACTICE](-p-r-a-c-t-i-c-e/index.md)<br/>Practice mode: light gate + compression, no reverb |
| [RECORDING](-r-e-c-o-r-d-i-n-g/index.md) | [common]<br/>[RECORDING](-r-e-c-o-r-d-i-n-g/index.md)<br/>Recording: aggressive gate + heavy compression + subtle reverb |
| [DRY](-d-r-y/index.md) | [common]<br/>[DRY](-d-r-y/index.md)<br/>Dry: compression only, no gate or reverb |
| [WET](-w-e-t/index.md) | [common]<br/>[WET](-w-e-t/index.md)<br/>Wet: reverb only |
| [CLEAN](-c-l-e-a-n/index.md) | [common]<br/>[CLEAN](-c-l-e-a-n/index.md)<br/>Clean: gate only for noise reduction |

## Properties

| Name | Summary |
|---|---|
| [name](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-372974862%2FProperties%2F-204059827) | [common]<br/>val [name](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-372974862%2FProperties%2F-204059827): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-739389684%2FProperties%2F-204059827) | [common]<br/>val [ordinal](../../com.musicmuni.voxatrace.sonix.model/-recording-quality/-h-i-g-h/index.md#-739389684%2FProperties%2F-204059827): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br/>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [EffectsPreset](index.md)<br/>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br/>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[EffectsPreset](index.md)&gt;<br/>Returns an array containing the constants of this enum type, in the order they're declared. |
