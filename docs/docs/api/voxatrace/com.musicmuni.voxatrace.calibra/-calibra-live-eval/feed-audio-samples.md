//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraLiveEval](index.md)/[feedAudioSamples](feed-audio-samples.md)

# feedAudioSamples

[common]\
fun [feedAudioSamples](feed-audio-samples.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000)

Feed audio samples to the session.

ADR-017 compliant: Accepts any sample rate and resamples internally to 16kHz.

All feature extraction (pitch detection, HPCP extraction) is delegated to the LiveEvaluator via processAudioFrame.

#### Parameters

common

| | |
|---|---|
| samples | Mono audio samples (normalized -1.0 to 1.0) |
| sampleRate | Sample rate of the input audio in Hz (default: 16000) |
