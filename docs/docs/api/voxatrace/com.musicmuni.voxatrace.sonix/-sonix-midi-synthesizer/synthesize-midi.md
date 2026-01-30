//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixMidiSynthesizer](index.md)/[synthesizeMidi](synthesize-midi.md)

# synthesizeMidi

[common]\
fun [synthesizeMidi](synthesize-midi.md)(midiData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Synthesize audio from raw MIDI data bytes.

#### Return

true if synthesis was successful

#### Parameters

common

| | |
|---|---|
| midiData | Raw MIDI file bytes (Standard MIDI File format) |
| outputPath | Path for output WAV file |
