---
sidebar_label: "CompressorConfig"
---


# CompressorConfig

[common]\
data class [CompressorConfig](index.md)(val thresholdDb: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = -20f, val ratio: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 4.0f, val attackMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 10.0f, val releaseMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 100.0f, val autoMakeup: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, val makeupDb: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f)

Configuration for audio compressor effect.

A compressor reduces the dynamic range of audio by attenuating loud sounds and optionally boosting quiet sounds.

## Constructors

| | |
|---|---|
| [CompressorConfig](-compressor-config.md) | [common]<br/>constructor(thresholdDb: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = -20f, ratio: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 4.0f, attackMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 10.0f, releaseMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 100.0f, autoMakeup: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, makeupDb: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f) |

## Properties

| Name | Summary |
|---|---|
| [attackMs](attack-ms.md) | [common]<br/>val [attackMs](attack-ms.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 10.0f<br/>Time to reach full compression (milliseconds) |
| [autoMakeup](auto-makeup.md) | [common]<br/>val [autoMakeup](auto-makeup.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false<br/>Automatically compensate for gain reduction |
| [makeupDb](makeup-db.md) | [common]<br/>val [makeupDb](makeup-db.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f<br/>Manual makeup gain when autoMakeup is false (dB) |
| [ratio](ratio.md) | [common]<br/>val [ratio](ratio.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 4.0f<br/>Compression ratio (e.g., 4.0 means 4:1 compression) |
| [releaseMs](release-ms.md) | [common]<br/>val [releaseMs](release-ms.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 100.0f<br/>Time to release compression (milliseconds) |
| [thresholdDb](threshold-db.md) | [common]<br/>val [thresholdDb](threshold-db.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Signal level above which compression begins (dB) |
