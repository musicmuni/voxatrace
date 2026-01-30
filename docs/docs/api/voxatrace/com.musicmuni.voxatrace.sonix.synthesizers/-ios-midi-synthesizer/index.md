//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.synthesizers](../index.md)/[IosMidiSynthesizer](index.md)

# IosMidiSynthesizer

[ios]\
class [IosMidiSynthesizer](index.md)

iOS implementation of MidiSynthesizer using FluidSynth via cinterop. Converts MIDI files to audio using SoundFont files (.sf2 and .sf3).

## Constructors

| | |
|---|---|
| [IosMidiSynthesizer](-ios-midi-synthesizer.md) | [ios]<br/>constructor() |

## Functions

| Name | Summary |
|---|---|
| [getVersion](get-version.md) | [ios]<br/>open fun [getVersion](get-version.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [synthesize](synthesize.md) | [ios]<br/>open fun [synthesize](synthesize.md)(outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), midiPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), soundFontPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [synthesizeMidi](synthesize-midi.md) | [ios]<br/>open fun [synthesizeMidi](synthesize-midi.md)(outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), midiData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), soundFontPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
