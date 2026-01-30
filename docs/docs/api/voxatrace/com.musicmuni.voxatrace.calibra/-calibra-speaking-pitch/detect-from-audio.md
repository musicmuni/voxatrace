//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraSpeakingPitch](index.md)/[detectFromAudio](detect-from-audio.md)

# detectFromAudio

[common]\
fun [detectFromAudio](detect-from-audio.md)(audioMono: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)

Detect natural speaking pitch from audio samples.

Analyzes spoken audio to find the median fundamental frequency. Works best with natural speech samples.

**Note:** Audio must be 16kHz mono. Use SonixResampler to resample if needed.

#### Return

Detected frequency in Hz, or -1 if detection failed

#### Parameters

common

| | |
|---|---|
| audioMono | Mono audio samples (normalized -1 to 1), 16kHz |
