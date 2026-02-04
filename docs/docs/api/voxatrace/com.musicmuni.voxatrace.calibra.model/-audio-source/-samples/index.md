---
sidebar_label: "Samples"
---


# Samples

[common]\
data class [Samples](index.md)(val samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), val sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000) : [AudioSource](../index.md)

Raw audio samples already in memory.

## Constructors

| | |
|---|---|
| [Samples](-samples.md) | [common]<br/>constructor(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000) |

## Properties

| Name | Summary |
|---|---|
| [sampleRate](sample-rate.md) | [common]<br/>val [sampleRate](sample-rate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000<br/>Sample rate in Hz |
| [samples](samples.md) | [common]<br/>val [samples](samples.md): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Mono audio samples (normalized -1.0 to 1.0) |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [common]<br/>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [common]<br/>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
