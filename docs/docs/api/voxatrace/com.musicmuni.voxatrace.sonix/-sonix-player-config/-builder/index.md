//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.sonix](../../index.md)/[SonixPlayerConfig](../index.md)/[Builder](index.md)

# Builder

[common]\
class [Builder](index.md)

Builder for SonixPlayerConfig.

Builds **Config objects**, not player instances (ADR-001 compliant).

## Constructors

| | |
|---|---|
| [Builder](-builder.md) | [common]<br/>constructor() |

## Functions

| Name | Summary |
|---|---|
| [build](build.md) | [common]<br/>fun [build](build.md)(): [SonixPlayerConfig](../index.md)<br/>Build the immutable config |
| [loopCount](loop-count.md) | [common]<br/>fun [loopCount](loop-count.md)(count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set loop count (1 = play once, -1 = infinite) |
| [loopForever](loop-forever.md) | [common]<br/>fun [loopForever](loop-forever.md)(): &lt;Error class: unknown class&gt;<br/>Enable infinite looping (equivalent to loopCount(-1)) |
| [onComplete](on-complete.md) | [common]<br/>fun [onComplete](on-complete.md)(callback: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)): &lt;Error class: unknown class&gt;<br/>Called when playback completes (all loops finished) |
| [onError](on-error.md) | [common]<br/>fun [onError](on-error.md)(callback: (message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)): &lt;Error class: unknown class&gt;<br/>Called when a playback error occurs |
| [onLoopComplete](on-loop-complete.md) | [common]<br/>fun [onLoopComplete](on-loop-complete.md)(callback: (loopIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), totalLoops: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)): &lt;Error class: unknown class&gt;<br/>Called when a loop iteration completes |
| [onPlaybackStateChanged](on-playback-state-changed.md) | [common]<br/>fun [onPlaybackStateChanged](on-playback-state-changed.md)(callback: (isPlaying: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)): &lt;Error class: unknown class&gt;<br/>Called when playback state changes (play/pause/stop) |
| [pitch](pitch.md) | [common]<br/>fun [pitch](pitch.md)(semitones: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): &lt;Error class: unknown class&gt;<br/>Set pitch shift in semitones (-12 to +12) |
| [preset](preset.md) | [common]<br/>fun [preset](preset.md)(config: [SonixPlayerConfig](../index.md)): &lt;Error class: unknown class&gt;<br/>Start from a preset configuration |
| [tempo](tempo.md) | [common]<br/>fun [tempo](tempo.md)(value: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): &lt;Error class: unknown class&gt;<br/>Set tempo/speed multiplier (0.25 to 4.0, 1.0 = normal speed) |
| [volume](volume.md) | [common]<br/>fun [volume](volume.md)(volume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): &lt;Error class: unknown class&gt;<br/>Set initial volume (0.0 to 1.0) |
