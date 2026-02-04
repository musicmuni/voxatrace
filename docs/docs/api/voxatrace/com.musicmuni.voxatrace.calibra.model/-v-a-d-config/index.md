---
sidebar_label: "VADConfig"
---


# VADConfig

[common]\
data class [VADConfig](index.md)(val backend: [VADBackend](../-v-a-d-backend/index.md) = VADBackend.SPEECH, val sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000, val threshold: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.5f, val minSpeechDuration: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.25f, val minSilenceDuration: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.25f, val windowSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 512, val numThreads: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1, val modelPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null, val rmsThreshold: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.05f, val pitchProbThreshold: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.5f, val minPitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 50.0f)

Configuration for Voice Activity Detection.

## Constructors

| | |
|---|---|
| [VADConfig](-v-a-d-config.md) | [common]<br/>constructor(backend: [VADBackend](../-v-a-d-backend/index.md) = VADBackend.SPEECH, sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000, threshold: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.5f, minSpeechDuration: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.25f, minSilenceDuration: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.25f, windowSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 512, numThreads: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1, modelPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null, rmsThreshold: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.05f, pitchProbThreshold: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.5f, minPitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 50.0f) |

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [common]<br/>class [Builder](-builder/index.md)<br/>Builder for VADConfig. |
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [backend](backend.md) | [common]<br/>val [backend](backend.md): [VADBackend](../-v-a-d-backend/index.md)<br/>VAD engine to use (SPEECH, GENERAL, SINGING, or SINGING_REALTIME) |
| [minPitch](min-pitch.md) | [common]<br/>val [minPitch](min-pitch.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 50.0f<br/>Minimum pitch in Hz for GENERAL backend (default: 50) |
| [minSilenceDuration](min-silence-duration.md) | [common]<br/>val [minSilenceDuration](min-silence-duration.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.25f<br/>Minimum silence duration in seconds (default: 0.25) |
| [minSpeechDuration](min-speech-duration.md) | [common]<br/>val [minSpeechDuration](min-speech-duration.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.25f<br/>Minimum speech duration in seconds (default: 0.25) |
| [modelPath](model-path.md) | [common]<br/>val [modelPath](model-path.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null<br/>Custom model path, null for bundled model |
| [numThreads](num-threads.md) | [common]<br/>val [numThreads](num-threads.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1<br/>Number of inference threads (default: 1) |
| [pitchProbThreshold](pitch-prob-threshold.md) | [common]<br/>val [pitchProbThreshold](pitch-prob-threshold.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.5f<br/>Pitch probability threshold for GENERAL backend (default: 0.5) |
| [rmsThreshold](rms-threshold.md) | [common]<br/>val [rmsThreshold](rms-threshold.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.05f<br/>RMS threshold for GENERAL backend (default: 0.05) |
| [sampleRate](sample-rate.md) | [common]<br/>val [sampleRate](sample-rate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000<br/>Audio sample rate in Hz (default: 16000) |
| [threshold](threshold.md) | [common]<br/>val [threshold](threshold.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.5f<br/>Detection threshold 0.0-1.0 (default: 0.5) |
| [windowSize](window-size.md) | [common]<br/>val [windowSize](window-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 512<br/>Processing window size in samples (default: 512 = 32ms at 16kHz) |
