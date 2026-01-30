//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraPitch](../index.md)/[Detector](index.md)/[detect](detect.md)

# detect

[common]\
abstract fun [detect](detect.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [PitchPoint](../../../com.musicmuni.voxatrace.calibra.model/-pitch-point/index.md)

Detect pitch from audio samples.

Automatically resamples to 16kHz internally if needed.

#### Return

PitchPoint containing pitch (Hz) and confidence (0.0-1.0)

#### Parameters

common

| | |
|---|---|
| samples | Audio samples as Float (-1.0 to 1.0 range) |
| sampleRate | Sample rate of the input audio |
