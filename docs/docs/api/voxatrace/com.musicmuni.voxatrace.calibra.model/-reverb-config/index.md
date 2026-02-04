---
sidebar_label: "ReverbConfig"
---


# ReverbConfig

[common]\
data class [ReverbConfig](index.md)(val mix: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.3f, val roomSize: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.5f)

Configuration for reverb effect.

Reverb simulates the natural reflections of sound in a space, adding depth and ambiance to audio.

## Constructors

| | |
|---|---|
| [ReverbConfig](-reverb-config.md) | [common]<br/>constructor(mix: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.3f, roomSize: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.5f) |

## Properties

| Name | Summary |
|---|---|
| [mix](mix.md) | [common]<br/>val [mix](mix.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.3f<br/>Wet/dry mix (0.0 = dry, 1.0 = fully wet) |
| [roomSize](room-size.md) | [common]<br/>val [roomSize](room-size.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.5f<br/>Simulated room size (0.0 = small, 1.0 = large) |
