---
sidebar_label: "LessonSvara"
---


# LessonSvara

[common]\
data class [LessonSvara](index.md)(var svaraName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), var svaraLabel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), var svaraAudioFilePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), var numBeats: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), var numSamplesConsonant: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), var audioLengthMilliSecs: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0)

## Constructors

| | |
|---|---|
| [LessonSvara](-lesson-svara.md) | [common]<br/>constructor(svaraName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), svaraLabel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), svaraAudioFilePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), numBeats: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), numSamplesConsonant: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), audioLengthMilliSecs: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0) |

## Properties

| Name | Summary |
|---|---|
| [audioLengthMilliSecs](audio-length-milli-secs.md) | [common]<br/>var [audioLengthMilliSecs](audio-length-milli-secs.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Audio length in milliseconds. Populated when loading. |
| [numBeats](num-beats.md) | [common]<br/>var [numBeats](num-beats.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of beats in the svara. |
| [numSamplesConsonant](num-samples-consonant.md) | [common]<br/>var [numSamplesConsonant](num-samples-consonant.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of samples of the audio that are consonants. |
| [svaraAudioFilePath](svara-audio-file-path.md) | [common]<br/>var [svaraAudioFilePath](svara-audio-file-path.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>The path of the audio file related to the svara. |
| [svaraLabel](svara-label.md) | [common]<br/>var [svaraLabel](svara-label.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>The label for the svara. Something that we will show on the screens. |
| [svaraName](svara-name.md) | [common]<br/>var [svaraName](svara-name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>The name of the svara to be shown. This is the identifier. |
