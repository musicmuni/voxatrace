---
sidebar_label: "Builder"
---


# Builder

[common]\
class [Builder](index.md)

Builder for VADConfig.

Builds **Config objects**, not VAD instances (ADR-001 compliant).

## Constructors

| | |
|---|---|
| [Builder](-builder.md) | [common]<br/>constructor() |

## Functions

| Name | Summary |
|---|---|
| [backend](backend.md) | [common]<br/>fun [backend](backend.md)(backend: [VADBackend](../../-v-a-d-backend/index.md)): &lt;Error class: unknown class&gt;<br/>Set backend (determines which preset defaults to use) |
| [build](build.md) | [common]<br/>fun [build](build.md)(): [VADConfig](../index.md)<br/>Build the immutable config |
| [minPitch](min-pitch.md) | [common]<br/>fun [minPitch](min-pitch.md)(hz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): &lt;Error class: unknown class&gt;<br/>Set minimum pitch in Hz for GENERAL backend |
| [minSilenceDuration](min-silence-duration.md) | [common]<br/>fun [minSilenceDuration](min-silence-duration.md)(seconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): &lt;Error class: unknown class&gt;<br/>Set minimum silence duration in seconds |
| [minSpeechDuration](min-speech-duration.md) | [common]<br/>fun [minSpeechDuration](min-speech-duration.md)(seconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): &lt;Error class: unknown class&gt;<br/>Set minimum speech duration in seconds |
| [modelPath](model-path.md) | [common]<br/>fun [modelPath](model-path.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)?): &lt;Error class: unknown class&gt;<br/>Set custom model path (null = use bundled model) |
| [numThreads](num-threads.md) | [common]<br/>fun [numThreads](num-threads.md)(threads: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set number of inference threads |
| [pitchProbThreshold](pitch-prob-threshold.md) | [common]<br/>fun [pitchProbThreshold](pitch-prob-threshold.md)(value: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): &lt;Error class: unknown class&gt;<br/>Set pitch probability threshold for GENERAL backend |
| [preset](preset.md) | [common]<br/>fun [preset](preset.md)(config: [VADConfig](../index.md)): &lt;Error class: unknown class&gt;<br/>Start from a preset configuration |
| [rmsThreshold](rms-threshold.md) | [common]<br/>fun [rmsThreshold](rms-threshold.md)(value: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): &lt;Error class: unknown class&gt;<br/>Set RMS threshold for GENERAL backend |
| [sampleRate](sample-rate.md) | [common]<br/>fun [sampleRate](sample-rate.md)(rate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set audio sample rate in Hz |
| [threshold](threshold.md) | [common]<br/>fun [threshold](threshold.md)(value: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): &lt;Error class: unknown class&gt;<br/>Set detection threshold (0.0-1.0) |
| [windowSize](window-size.md) | [common]<br/>fun [windowSize](window-size.md)(samples: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set processing window size in samples |
