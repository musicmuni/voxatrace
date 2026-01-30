//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraVAD](index.md)/[acceptWaveform](accept-waveform.md)

# acceptWaveform

[common]\
fun [acceptWaveform](accept-waveform.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000)

Feed audio samples for streaming detection. Use with isSpeechDetected for real-time detection.

ADR-017 compliant: Accepts any sample rate and resamples internally to 16kHz.

#### Parameters

common

| | |
|---|---|
| samples | Audio samples (mono, normalized -1.0 to 1.0) |
| sampleRate | Sample rate of the input audio in Hz (default: 16000) |
