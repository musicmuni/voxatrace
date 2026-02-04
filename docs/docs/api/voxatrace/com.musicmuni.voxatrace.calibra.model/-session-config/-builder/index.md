---
sidebar_label: "Builder"
---


# Builder

[common]\
class [Builder](index.md)

Builder for SessionConfig.

Provides Tier 2 API with discoverability via autocomplete. Builds **Config objects**, not session instances.

## Constructors

| | |
|---|---|
| [Builder](-builder.md) | [common]<br/>constructor() |

## Functions

| Name | Summary |
|---|---|
| [autoAdvance](auto-advance.md) | [common]<br/>fun [autoAdvance](auto-advance.md)(enabled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)): &lt;Error class: unknown class&gt;<br/>Enable or disable auto-advance to next segment |
| [autoPhaseTransition](auto-phase-transition.md) | [common]<br/>fun [autoPhaseTransition](auto-phase-transition.md)(enabled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)): &lt;Error class: unknown class&gt;<br/>Enable or disable automatic phase transition (LISTENING → SINGING) in singafter mode |
| [autoSegmentDetection](auto-segment-detection.md) | [common]<br/>fun [autoSegmentDetection](auto-segment-detection.md)(enabled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)): &lt;Error class: unknown class&gt;<br/>Enable or disable automatic segment end detection from player time |
| [build](build.md) | [common]<br/>fun [build](build.md)(): [SessionConfig](../index.md)<br/>Build the immutable config |
| [hopSize](hop-size.md) | [common]<br/>fun [hopSize](hop-size.md)(samples: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set hop size between frames in samples |
| [maxAttempts](max-attempts.md) | [common]<br/>fun [maxAttempts](max-attempts.md)(max: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set maximum attempts before forced advance (0 = unlimited) |
| [preset](preset.md) | [common]<br/>fun [preset](preset.md)(config: [SessionConfig](../index.md)): &lt;Error class: unknown class&gt;<br/>Start from a preset configuration |
| [resultAggregation](result-aggregation.md) | [common]<br/>fun [resultAggregation](result-aggregation.md)(agg: [ResultAggregation](../../-result-aggregation/index.md)): &lt;Error class: unknown class&gt;<br/>Set how to aggregate multiple attempts |
| [scoreThreshold](score-threshold.md) | [common]<br/>fun [scoreThreshold](score-threshold.md)(threshold: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): &lt;Error class: unknown class&gt;<br/>Set minimum score threshold to auto-advance (0 = disabled) |
