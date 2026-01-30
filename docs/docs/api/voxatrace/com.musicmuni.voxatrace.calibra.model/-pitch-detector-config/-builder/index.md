//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../../index.md)/[PitchDetectorConfig](../index.md)/[Builder](index.md)

# Builder

[common]\
class [Builder](index.md)

Builder for PitchDetectorConfig.

Provides Tier 2 API with discoverability via autocomplete. Builds **Config objects**, not detector instances.

## Constructors

| | |
|---|---|
| [Builder](-builder.md) | [common]<br/>constructor() |

## Functions

| Name | Summary |
|---|---|
| [algorithm](algorithm.md) | [common]<br/>fun [algorithm](algorithm.md)(algo: [PitchAlgorithm](../../-pitch-algorithm/index.md)): &lt;Error class: unknown class&gt;<br/>Set pitch detection algorithm |
| [bufferSize](buffer-size.md) | [common]<br/>fun [bufferSize](buffer-size.md)(size: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set buffer size for analysis |
| [build](build.md) | [common]<br/>fun [build](build.md)(): [PitchDetectorConfig](../index.md)<br/>Build the immutable config |
| [enableProcessing](enable-processing.md) | [common]<br/>fun [enableProcessing](enable-processing.md)(): &lt;Error class: unknown class&gt;<br/>Enable post-processing (smoothing + octave correction) |
| [hopSize](hop-size.md) | [common]<br/>fun [hopSize](hop-size.md)(samples: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set hop size between frames |
| [preset](preset.md) | [common]<br/>fun [preset](preset.md)(config: [PitchDetectorConfig](../index.md)): &lt;Error class: unknown class&gt;<br/>Start from a preset configuration |
| [quietHandling](quiet-handling.md) | [common]<br/>fun [quietHandling](quiet-handling.md)(handling: [QuietHandling](../../-quiet-handling/index.md)): &lt;Error class: unknown class&gt;<br/>Set how to handle quiet audio |
| [strictness](strictness.md) | [common]<br/>fun [strictness](strictness.md)(strictness: [DetectionStrictness](../../-detection-strictness/index.md)): &lt;Error class: unknown class&gt;<br/>Set detection strictness |
| [swiftF0BatchSize](swift-f0-batch-size.md) | [common]<br/>fun [swiftF0BatchSize](swift-f0-batch-size.md)(samples: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set SwiftF0 batch size |
| [tolerance](tolerance.md) | [common]<br/>fun [tolerance](tolerance.md)(value: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): &lt;Error class: unknown class&gt;<br/>Set YIN algorithm tolerance |
| [voiceType](voice-type.md) | [common]<br/>fun [voiceType](voice-type.md)(type: [VoiceType](../../-voice-type/index.md)): &lt;Error class: unknown class&gt;<br/>Set voice type for frequency range optimization |
