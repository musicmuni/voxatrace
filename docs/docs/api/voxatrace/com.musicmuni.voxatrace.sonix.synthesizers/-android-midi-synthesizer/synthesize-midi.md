//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.synthesizers](../index.md)/[AndroidMidiSynthesizer](index.md)/[synthesizeMidi](synthesize-midi.md)

# synthesizeMidi

[android]\
open override fun [synthesizeMidi](synthesize-midi.md)(outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), midiData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), soundFontPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Synthesize audio from raw MIDI file data. This is a Swift-friendly API that doesn't require Okio FileSystem.

#### Return

true if synthesis was successful

#### Parameters

android

| | |
|---|---|
| outputPath | Output audio file path (.wav) |
| midiData | Raw MIDI file bytes (Standard MIDI File format) |
| soundFontPath | SoundFont file path (.sf2 or .sf3) |
| sampleRate | Output sample rate (default: 44100) |
