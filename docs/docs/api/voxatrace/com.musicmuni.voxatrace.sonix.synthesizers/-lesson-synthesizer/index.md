//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.synthesizers](../index.md)/[LessonSynthesizer](index.md)

# LessonSynthesizer

[common]\
class [LessonSynthesizer](index.md)(svaraList: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[LessonSvara](../../com.musicmuni.voxatrace.sonix.model/-lesson-svara/index.md)&gt;, beatLengthMilliSec: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), beatsSilenceStart: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), beatsSilenceEnd: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000, channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1)

## Constructors

| | |
|---|---|
| [LessonSynthesizer](-lesson-synthesizer.md) | [common]<br/>constructor(svaraList: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[LessonSvara](../../com.musicmuni.voxatrace.sonix.model/-lesson-svara/index.md)&gt;, beatLengthMilliSec: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), beatsSilenceStart: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), beatsSilenceEnd: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000, channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1) |

## Functions

| Name | Summary |
|---|---|
| [loadAudio](load-audio.md) | [common]<br/>fun [loadAudio](load-audio.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Loads audio for all svaras. |
| [synthesize](synthesize.md) | [common]<br/>fun [synthesize](synthesize.md)(): [AudioRawData](../../com.musicmuni.voxatrace.sonix.model/-audio-raw-data/index.md)?<br/>Synthesizes the lesson track. |
