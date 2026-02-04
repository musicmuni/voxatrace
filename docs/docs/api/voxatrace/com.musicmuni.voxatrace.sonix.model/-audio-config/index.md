---
sidebar_label: "AudioConfig"
---


# AudioConfig

[common]\
data class [AudioConfig](index.md)(val sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44100, val channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1, val bufferSizeMs: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 40, val echoCancellation: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false)

## Constructors

| | |
|---|---|
| [AudioConfig](-audio-config.md) | [common]<br/>constructor(sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44100, channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1, bufferSizeMs: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 40, echoCancellation: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false) |

## Properties

| Name | Summary |
|---|---|
| [bufferSizeMs](buffer-size-ms.md) | [common]<br/>val [bufferSizeMs](buffer-size-ms.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 40 |
| [channels](channels.md) | [common]<br/>val [channels](channels.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1 |
| [echoCancellation](echo-cancellation.md) | [common]<br/>val [echoCancellation](echo-cancellation.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false<br/>Enable acoustic echo cancellation to remove speaker audio from mic input |
| [sampleRate](sample-rate.md) | [common]<br/>val [sampleRate](sample-rate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44100 |
