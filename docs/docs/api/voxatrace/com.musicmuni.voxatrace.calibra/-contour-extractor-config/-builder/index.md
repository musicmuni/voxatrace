---
sidebar_label: "Builder"
---


# Builder

[common]\
class [Builder](index.md)

Builder for ContourExtractorConfig.

Builds **Config objects**, not extractor instances (ADR-001 compliant).

## Constructors

| | |
|---|---|
| [Builder](-builder.md) | [common]<br/>constructor() |

## Functions

| Name | Summary |
|---|---|
| [algorithm](algorithm.md) | [common]<br/>fun [algorithm](algorithm.md)(algorithm: [PitchAlgorithm](../../../com.musicmuni.voxatrace.calibra.model/-pitch-algorithm/index.md)): &lt;Error class: unknown class&gt;<br/>Set pitch detection algorithm |
| [build](build.md) | [common]<br/>fun [build](build.md)(): [ContourExtractorConfig](../index.md)<br/>Build the immutable config |
| [cleanup](cleanup.md) | [common]<br/>fun [cleanup](cleanup.md)(cleanup: [ContourCleanup](../../../com.musicmuni.voxatrace.calibra.model/-contour-cleanup/index.md)): &lt;Error class: unknown class&gt;<br/>Set post-processing cleanup options |
| [hopMs](hop-ms.md) | [common]<br/>fun [hopMs](hop-ms.md)(hop: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set hop size between pitch samples in milliseconds |
| [pitchPreset](pitch-preset.md) | [common]<br/>fun [pitchPreset](pitch-preset.md)(preset: [PitchPreset](../../../com.musicmuni.voxatrace.calibra.model/-pitch-preset/index.md)): &lt;Error class: unknown class&gt;<br/>Set pitch preset (resolution/accuracy trade-off) |
| [preset](preset.md) | [common]<br/>fun [preset](preset.md)(config: [ContourExtractorConfig](../index.md)): &lt;Error class: unknown class&gt;<br/>Start from a preset configuration |
| [quietHandling](quiet-handling.md) | [common]<br/>fun [quietHandling](quiet-handling.md)(handling: [QuietHandling](../../../com.musicmuni.voxatrace.calibra.model/-quiet-handling/index.md)): &lt;Error class: unknown class&gt;<br/>Set how to handle quiet audio |
| [sampleRate](sample-rate.md) | [common]<br/>fun [sampleRate](sample-rate.md)(rate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set input audio sample rate in Hz |
| [strictness](strictness.md) | [common]<br/>fun [strictness](strictness.md)(strictness: [DetectionStrictness](../../../com.musicmuni.voxatrace.calibra.model/-detection-strictness/index.md)): &lt;Error class: unknown class&gt;<br/>Set detection strictness |
| [voiceType](voice-type.md) | [common]<br/>fun [voiceType](voice-type.md)(voiceType: [VoiceType](../../../com.musicmuni.voxatrace.calibra.model/-voice-type/index.md)): &lt;Error class: unknown class&gt;<br/>Set voice type for frequency range optimization |
