//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../index.md)/[NoiseGateConfig](index.md)

# NoiseGateConfig

[common]\
data class [NoiseGateConfig](index.md)(val thresholdDb: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = -40f, val holdTimeMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 100.0f, val timeConstMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 10.0f)

Configuration for noise gate effect.

A noise gate attenuates audio signals below a threshold, useful for reducing background noise during silent passages.

## Constructors

| | |
|---|---|
| [NoiseGateConfig](-noise-gate-config.md) | [common]<br/>constructor(thresholdDb: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = -40f, holdTimeMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 100.0f, timeConstMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 10.0f) |

## Properties

| Name | Summary |
|---|---|
| [holdTimeMs](hold-time-ms.md) | [common]<br/>val [holdTimeMs](hold-time-ms.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 100.0f<br/>Time to hold gate open after signal drops (milliseconds) |
| [thresholdDb](threshold-db.md) | [common]<br/>val [thresholdDb](threshold-db.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Signal level below which gating occurs (dB) |
| [timeConstMs](time-const-ms.md) | [common]<br/>val [timeConstMs](time-const-ms.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 10.0f<br/>Time constant for envelope follower (milliseconds) |
