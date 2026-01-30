//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraEffectsConfig](../index.md)/[Builder](index.md)

# Builder

[common]\
class [Builder](index.md)

Builder for flexible CalibraEffectsConfig construction.

Returns CalibraEffectsConfig (ADR-001 compliant: Builder builds Config).

## Constructors

| | |
|---|---|
| [Builder](-builder.md) | [common]<br/>constructor() |

## Functions

| Name | Summary |
|---|---|
| [addCompressor](add-compressor.md) | [common]<br/>fun [addCompressor](add-compressor.md)(): &lt;Error class: unknown class&gt;<br/>Add compressor with default settings<br/>[common]<br/>fun [addCompressor](add-compressor.md)(preset: [CompressorPreset](../../../com.musicmuni.voxatrace.calibra.model/-compressor-preset/index.md)): &lt;Error class: unknown class&gt;<br/>Add compressor with preset<br/>[common]<br/>fun [addCompressor](add-compressor.md)(thresholdDb: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = -20f, ratio: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 4.0f, attackMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 10.0f, releaseMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 100.0f, autoMakeup: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, makeupDb: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f): &lt;Error class: unknown class&gt;<br/>Add compressor with custom parameters |
| [addNoiseGate](add-noise-gate.md) | [common]<br/>fun [addNoiseGate](add-noise-gate.md)(): &lt;Error class: unknown class&gt;<br/>Add noise gate with default settings<br/>[common]<br/>fun [addNoiseGate](add-noise-gate.md)(preset: [NoiseGatePreset](../../../com.musicmuni.voxatrace.calibra.model/-noise-gate-preset/index.md)): &lt;Error class: unknown class&gt;<br/>Add noise gate with preset<br/>[common]<br/>fun [addNoiseGate](add-noise-gate.md)(thresholdDb: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = -40f, holdTimeMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 100.0f, timeConstMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 10.0f): &lt;Error class: unknown class&gt;<br/>Add noise gate with custom parameters |
| [addReverb](add-reverb.md) | [common]<br/>fun [addReverb](add-reverb.md)(): &lt;Error class: unknown class&gt;<br/>Add reverb with default settings<br/>[common]<br/>fun [addReverb](add-reverb.md)(preset: [ReverbPreset](../../../com.musicmuni.voxatrace.calibra.model/-reverb-preset/index.md)): &lt;Error class: unknown class&gt;<br/>Add reverb with preset<br/>[common]<br/>fun [addReverb](add-reverb.md)(mix: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.3f, roomSize: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.5f): &lt;Error class: unknown class&gt;<br/>Add reverb with custom parameters |
| [build](build.md) | [common]<br/>fun [build](build.md)(): [CalibraEffectsConfig](../index.md)<br/>Build the configuration |
| [preset](preset.md) | [common]<br/>fun [preset](preset.md)(config: [CalibraEffectsConfig](../index.md)): &lt;Error class: unknown class&gt;<br/>Start from a preset configuration |
| [sampleRate](sample-rate.md) | [common]<br/>fun [sampleRate](sample-rate.md)(rate: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): &lt;Error class: unknown class&gt;<br/>Set sample rate |
