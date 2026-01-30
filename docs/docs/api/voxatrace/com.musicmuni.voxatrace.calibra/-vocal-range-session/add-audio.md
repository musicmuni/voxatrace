//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[VocalRangeSession](index.md)/[addAudio](add-audio.md)

# addAudio

[common]\
fun [addAudio](add-audio.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000)

Add audio samples for processing.

ADR-017 compliant: Accepts any sample rate and resamples internally to 16kHz.

#### Parameters

common

| | |
|---|---|
| samples | Audio samples as Float (-1.0 to 1.0 range) |
| sampleRate | Sample rate of the input audio in Hz (default: 16000) |
