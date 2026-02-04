---
sidebar_label: "PitchData"
---


# PitchData

[common]\
data class [PitchData](index.md)(val times: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), val pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html))

Parsed pitch data from a pitch file.

## Constructors

| | |
|---|---|
| [PitchData](-pitch-data.md) | [common]<br/>constructor(times: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [count](count.md) | [common]<br/>val [count](count.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [pitchesHz](pitches-hz.md) | [common]<br/>val [pitchesHz](pitches-hz.md): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Array of pitch values in Hz (-1 = unvoiced/silence) |
| [times](times.md) | [common]<br/>val [times](times.md): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Array of timestamps in seconds |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [common]<br/>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [common]<br/>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
