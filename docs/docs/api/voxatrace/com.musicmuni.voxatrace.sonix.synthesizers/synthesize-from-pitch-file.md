//[voxatrace](../../index.md)/[com.musicmuni.voxatrace.sonix.synthesizers](index.md)/[synthesizeFromPitchFile](synthesize-from-pitch-file.md)

# synthesizeFromPitchFile

[common]\
fun [MidiSynthesizer](-midi-synthesizer/index.md).[synthesizeFromPitchFile](synthesize-from-pitch-file.md)(fileSystem: FileSystem, outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), pitchFilePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), soundFontPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), lessonTonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), parentTonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = lessonTonicHz, sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44100, tempMidiPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Synthesize audio from a pitch contour file.

The pitch file format is whitespace-separated with columns: startTime(sec) endTime(sec) frequencyHz

Timing in the pitch file is in seconds and will be converted to absolute millisecond positions in the output audio.

#### Return

true if synthesis was successful

#### Parameters

common

| | |
|---|---|
| fileSystem | The FileSystem to use for file operations |
| outputPath | Output audio file path (.wav) |
| pitchFilePath | Path to pitch contour file |
| soundFontPath | SoundFont file path (.sf2 or .sf3) |
| lessonTonicHz | Tonic frequency of the lesson |
| parentTonicHz | Reference tonic frequency (for transposition) |
| sampleRate | Output sample rate (default: 44100) |
| tempMidiPath | Optional path for temporary MIDI file |
