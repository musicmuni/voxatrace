---
sidebar_label: "VocalRangeConfig"
---


# VocalRangeConfig

[common]\
data class [VocalRangeConfig](index.md)(val minNoteDurationSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f, val minConfidence: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.5f, val stabilityWindowMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 50.0f, val maxDeviationSemitones: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f, val sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000)

Configuration for vocal range detection.

Based on ASHA 2018 guidelines and voice science research.

## Constructors

| | |
|---|---|
| [VocalRangeConfig](-vocal-range-config.md) | [common]<br/>constructor(minNoteDurationSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f, minConfidence: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.5f, stabilityWindowMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 50.0f, maxDeviationSemitones: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f, sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [maxDeviationSemitones](max-deviation-semitones.md) | [common]<br/>val [maxDeviationSemitones](max-deviation-semitones.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f<br/>Maximum deviation for &quot;stable&quot; pitch (default: 1.0) |
| [minConfidence](min-confidence.md) | [common]<br/>val [minConfidence](min-confidence.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.5f<br/>Minimum confidence for pitch to be valid (default: 0.5) |
| [minNoteDurationSeconds](min-note-duration-seconds.md) | [common]<br/>val [minNoteDurationSeconds](min-note-duration-seconds.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f<br/>Minimum duration for a note to be included (default: 1.0s per ASHA) |
| [sampleRate](sample-rate.md) | [common]<br/>val [sampleRate](sample-rate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000<br/>Expected audio sample rate (default: 16000) |
| [stabilityWindowMs](stability-window-ms.md) | [common]<br/>val [stabilityWindowMs](stability-window-ms.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 50.0f<br/>Window for stability checking (default: 50ms) |
