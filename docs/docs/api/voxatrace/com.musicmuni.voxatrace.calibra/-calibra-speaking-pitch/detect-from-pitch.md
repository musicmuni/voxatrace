//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraSpeakingPitch](index.md)/[detectFromPitch](detect-from-pitch.md)

# detectFromPitch

[common]\
fun [detectFromPitch](detect-from-pitch.md)(pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)

Detect natural speaking pitch from pitch contour.

Analyzes an existing pitch contour to find the median pitch. This is useful when you've already extracted pitches from audio.

#### Return

Detected frequency in Hz, or -1 if detection failed

#### Parameters

common

| | |
|---|---|
| pitchesHz | Pitch values in Hz (-1 for unvoiced frames) |
