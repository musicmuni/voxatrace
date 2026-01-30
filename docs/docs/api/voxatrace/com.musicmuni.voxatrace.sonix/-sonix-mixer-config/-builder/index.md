//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.sonix](../../index.md)/[SonixMixerConfig](../index.md)/[Builder](index.md)

# Builder

[common]\
class [Builder](index.md)

Builder for SonixMixerConfig.

Builds **Config objects**, not mixer instances (ADR-001 compliant).

## Constructors

| | |
|---|---|
| [Builder](-builder.md) | [common]<br/>constructor() |

## Functions

| Name | Summary |
|---|---|
| [build](build.md) | [common]<br/>fun [build](build.md)(): [SonixMixerConfig](../index.md)<br/>Build the immutable config |
| [loopCount](loop-count.md) | [common]<br/>fun [loopCount](loop-count.md)(count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set how many times to play through all tracks (1 = play once, -1 = infinite) |
| [loopForever](loop-forever.md) | [common]<br/>fun [loopForever](loop-forever.md)(): &lt;Error class: unknown class&gt;<br/>Enable infinite looping (equivalent to loopCount(-1)) |
| [onError](on-error.md) | [common]<br/>fun [onError](on-error.md)(callback: (error: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)): &lt;Error class: unknown class&gt;<br/>Called when a playback error occurs |
| [onLoopComplete](on-loop-complete.md) | [common]<br/>fun [onLoopComplete](on-loop-complete.md)(callback: (loopIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)): &lt;Error class: unknown class&gt;<br/>Called when a single loop iteration completes |
| [onPlaybackComplete](on-playback-complete.md) | [common]<br/>fun [onPlaybackComplete](on-playback-complete.md)(callback: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)): &lt;Error class: unknown class&gt;<br/>Called when all loops have completed and playback stops |
| [preset](preset.md) | [common]<br/>fun [preset](preset.md)(config: [SonixMixerConfig](../index.md)): &lt;Error class: unknown class&gt;<br/>Start from a preset configuration |
