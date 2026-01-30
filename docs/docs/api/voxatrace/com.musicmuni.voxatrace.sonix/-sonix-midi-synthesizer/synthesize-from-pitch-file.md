//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixMidiSynthesizer](index.md)/[synthesizeFromPitchFile](synthesize-from-pitch-file.md)

# synthesizeFromPitchFile

[common]\
fun [synthesizeFromPitchFile](synthesize-from-pitch-file.md)(pitchFilePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), lessonTonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), parentTonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = lessonTonicHz): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Synthesize audio from a pitch contour file.

The pitch file format is whitespace-separated with columns: startTime(sec) endTime(sec) frequencyHz

Timing in the pitch file is in seconds and will be converted to absolute millisecond positions in the output audio.

#### Return

true if synthesis was successful

#### Parameters

common

| | |
|---|---|
| pitchFilePath | Path to pitch contour file |
| outputPath | Path for output WAV file |
| lessonTonicHz | Tonic frequency of the lesson |
| parentTonicHz | Reference tonic frequency for transposition (defaults to lessonTonicHz) |
