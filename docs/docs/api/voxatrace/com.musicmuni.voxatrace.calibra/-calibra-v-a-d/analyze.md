//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraVAD](index.md)/[analyze](analyze.md)

# analyze

[common]\
fun [analyze](analyze.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000): [VADResult](../../com.musicmuni.voxatrace.calibra.model/-v-a-d-result/index.md)?

Analyze audio and return rich VAD result.

ADR-017 compliant: Accepts any sample rate and resamples internally to 16kHz.

#### Return

VADResult with ratio, level, and convenience properties, or null if no chunk processed

#### Parameters

common

| | |
|---|---|
| samples | Audio samples (mono, normalized -1.0 to 1.0) |
| sampleRate | Sample rate of the input audio in Hz (default: 16000) |
