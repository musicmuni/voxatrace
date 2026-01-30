//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.synthesizers](../index.md)/[AndroidMidiSynthesizer](index.md)/[synthesize](synthesize.md)

# synthesize

[android]\
open override fun [synthesize](synthesize.md)(outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), midiPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), soundFontPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Synthesize a MIDI file to audio using a SoundFont.

#### Return

true if synthesis was successful

#### Parameters

android

| | |
|---|---|
| outputPath | Output audio file path (.wav) |
| midiPath | Input MIDI file path |
| soundFontPath | SoundFont file path (.sf2 or .sf3) |
| sampleRate | Output sample rate (default: 44100) |
