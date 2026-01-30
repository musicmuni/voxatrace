//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.sonix](../../index.md)/[SonixLessonSynthesizer](../index.md)/[Builder](index.md)

# Builder

[common]\
class [Builder](index.md)

Builder for advanced SonixLessonSynthesizer configuration.

```kotlin
val synth = SonixLessonSynthesizer.Builder()
    .svaras(svaraList)
    .beatLengthMs(500)
    .silenceBeats(start = 2, end = 2)
    .sampleRate(44100)
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
| [beatLengthMs](beat-length-ms.md) | [common]<br/>fun [beatLengthMs](beat-length-ms.md)(ms: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set beat length in milliseconds (required). |
| [build](build.md) | [common]<br/>fun [build](build.md)(): [SonixLessonSynthesizer](../index.md)<br/>Build the SonixLessonSynthesizer. |
| [onError](on-error.md) | [common]<br/>fun [onError](on-error.md)(callback: (message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)): &lt;Error class: unknown class&gt;<br/>Set error callback. |
| [sampleRate](sample-rate.md) | [common]<br/>fun [sampleRate](sample-rate.md)(rate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set output sample rate. |
| [silenceBeats](silence-beats.md) | [common]<br/>fun [silenceBeats](silence-beats.md)(start: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 2, end: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 2): &lt;Error class: unknown class&gt;<br/>Set silence beats at start and end. |
| [svaras](svaras.md) | [common]<br/>fun [svaras](svaras.md)(svaras: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[LessonSvara](../../../com.musicmuni.voxatrace.sonix.model/-lesson-svara/index.md)&gt;): &lt;Error class: unknown class&gt;<br/>Set the list of svaras to synthesize (required). |
