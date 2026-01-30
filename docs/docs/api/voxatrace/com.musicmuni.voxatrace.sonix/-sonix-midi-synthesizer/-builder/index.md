//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.sonix](../../index.md)/[SonixMidiSynthesizer](../index.md)/[Builder](index.md)

# Builder

[common]\
class [Builder](index.md)

Builder for advanced SonixMidiSynthesizer configuration.

```kotlin
val synth = SonixMidiSynthesizer.Builder()
    .soundFontPath("/path/to/soundfont.sf2")
    .sampleRate(48000)
    .onError { error -> println("Error: $error") }
    .build()
```

## Constructors

| | |
|---|---|
| [Builder](-builder.md) | [common]<br/>constructor() |

## Functions

| Name | Summary |
|---|---|
| [build](build.md) | [common]<br/>fun [build](build.md)(): [SonixMidiSynthesizer](../index.md)<br/>Build the SonixMidiSynthesizer. |
| [onError](on-error.md) | [common]<br/>fun [onError](on-error.md)(callback: (message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)): &lt;Error class: unknown class&gt;<br/>Set error callback. |
| [sampleRate](sample-rate.md) | [common]<br/>fun [sampleRate](sample-rate.md)(rate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set output sample rate. |
| [soundFontPath](sound-font-path.md) | [common]<br/>fun [soundFontPath](sound-font-path.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): &lt;Error class: unknown class&gt;<br/>Set the SoundFont file path (required). |
