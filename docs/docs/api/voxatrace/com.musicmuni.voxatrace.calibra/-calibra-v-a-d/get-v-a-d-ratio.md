//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraVAD](index.md)/[getVADRatio](get-v-a-d-ratio.md)

# getVADRatio

[common]\
fun [getVADRatio](get-v-a-d-ratio.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)

Get ratio of voiced frames in audio (0.0 to 1.0). Higher values indicate more speech/singing content.

ADR-017 compliant: Accepts any sample rate and resamples internally to 16kHz.

#### Return

Voiced frame ratio (0.0 = silence, 1.0 = continuous voice)

#### Parameters

common

| | |
|---|---|
| samples | Audio samples (mono, normalized -1.0 to 1.0) |
| sampleRate | Sample rate of the input audio in Hz (default: 16000) |
