//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.sonix](../../index.md)/[SonixMetronome](../index.md)/[Builder](index.md)

# Builder

[common]\
class [Builder](index.md)

Builder for advanced SonixMetronome configuration.

Usage:

```kotlin
val metronome = SonixMetronome.Builder()
    .samaSamplePath(samaPath)
    .beatSamplePath(beatPath)
    .bpm(120f)
    .beatsPerCycle(4)
    .volume(0.8f)
    .onBeat { beatIndex -> updateUI(beatIndex) }
    .onError { error -> showError(error) }
    .build()
```

## Constructors

| | |
|---|---|
| [Builder](-builder.md) | [common]<br/>constructor() |

## Functions

| Name | Summary |
|---|---|
| [beatSamplePath](beat-sample-path.md) | [common]<br/>fun [beatSamplePath](beat-sample-path.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): &lt;Error class: unknown class&gt;<br/>Set the path to the regular beat audio sample. |
| [beatsPerCycle](beats-per-cycle.md) | [common]<br/>fun [beatsPerCycle](beats-per-cycle.md)(count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set beats per cycle (talam). |
| [bpm](bpm.md) | [common]<br/>fun [bpm](bpm.md)(bpm: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): &lt;Error class: unknown class&gt;<br/>Set initial tempo in beats per minute. |
| [build](build.md) | [common]<br/>fun [build](build.md)(): [SonixMetronome](../index.md)<br/>Build the SonixMetronome with configured options. |
| [onBeat](on-beat.md) | [common]<br/>fun [onBeat](on-beat.md)(callback: (beatIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)): &lt;Error class: unknown class&gt;<br/>Called on each beat. |
| [onError](on-error.md) | [common]<br/>fun [onError](on-error.md)(callback: (error: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)): &lt;Error class: unknown class&gt;<br/>Called when an error occurs (e.g., sample loading failure). |
| [samaSamplePath](sama-sample-path.md) | [common]<br/>fun [samaSamplePath](sama-sample-path.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): &lt;Error class: unknown class&gt;<br/>Set the path to the downbeat (first beat) audio sample. |
| [volume](volume.md) | [common]<br/>fun [volume](volume.md)(volume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): &lt;Error class: unknown class&gt;<br/>Set initial volume. |
