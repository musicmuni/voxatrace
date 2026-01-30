//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraEffectsConfig](index.md)

# CalibraEffectsConfig

[common]\
data class [CalibraEffectsConfig](index.md)(val noiseGate: [NoiseGateConfig](../../com.musicmuni.voxatrace.calibra.model/-noise-gate-config/index.md)? = null, val compressor: [CompressorConfig](../../com.musicmuni.voxatrace.calibra.model/-compressor-config/index.md)? = null, val reverb: [ReverbConfig](../../com.musicmuni.voxatrace.calibra.model/-reverb-config/index.md)? = null, val sampleRate: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 16000.0f)

Configuration for CalibraEffects audio processing chain.

## Constructors

| | |
|---|---|
| [CalibraEffectsConfig](-calibra-effects-config.md) | [common]<br/>constructor(noiseGate: [NoiseGateConfig](../../com.musicmuni.voxatrace.calibra.model/-noise-gate-config/index.md)? = null, compressor: [CompressorConfig](../../com.musicmuni.voxatrace.calibra.model/-compressor-config/index.md)? = null, reverb: [ReverbConfig](../../com.musicmuni.voxatrace.calibra.model/-reverb-config/index.md)? = null, sampleRate: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 16000.0f) |

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [common]<br/>class [Builder](-builder/index.md)<br/>Builder for flexible CalibraEffectsConfig construction. |
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [compressor](compressor.md) | [common]<br/>val [compressor](compressor.md): [CompressorConfig](../../com.musicmuni.voxatrace.calibra.model/-compressor-config/index.md)? = null<br/>Compressor configuration (null = disabled) |
| [noiseGate](noise-gate.md) | [common]<br/>val [noiseGate](noise-gate.md): [NoiseGateConfig](../../com.musicmuni.voxatrace.calibra.model/-noise-gate-config/index.md)? = null<br/>Noise gate configuration (null = disabled) |
| [reverb](reverb.md) | [common]<br/>val [reverb](reverb.md): [ReverbConfig](../../com.musicmuni.voxatrace.calibra.model/-reverb-config/index.md)? = null<br/>Reverb configuration (null = disabled) |
| [sampleRate](sample-rate.md) | [common]<br/>val [sampleRate](sample-rate.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 16000.0f<br/>Audio sample rate in Hz (default: 16000) |
