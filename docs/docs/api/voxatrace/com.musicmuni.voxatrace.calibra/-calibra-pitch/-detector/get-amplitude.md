//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraPitch](../index.md)/[Detector](index.md)/[getAmplitude](get-amplitude.md)

# getAmplitude

[common]\
abstract fun [getAmplitude](get-amplitude.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)

Get the amplitude (RMS) of the audio samples.

Automatically resamples to 16kHz internally if needed.

#### Return

RMS amplitude value

#### Parameters

common

| | |
|---|---|
| samples | Audio samples as Float (-1.0 to 1.0 range) |
| sampleRate | Sample rate of the input audio |
