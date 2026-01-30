//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.sonix.metronome](../../index.md)/[MetronomePlayer](../index.md)/[Config](index.md)

# Config

[common]\
data class [Config](index.md)(val samaSamplePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val beatSamplePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val bpm: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val beatsPerCycle: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 4, val volume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f)

Configuration for the metronome.

## Constructors

| | |
|---|---|
| [Config](-config.md) | [common]<br/>constructor(samaSamplePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), beatSamplePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), bpm: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), beatsPerCycle: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 4, volume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f) |

## Properties

| Name | Summary |
|---|---|
| [beatSamplePath](beat-sample-path.md) | [common]<br/>val [beatSamplePath](beat-sample-path.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Sample for other beats |
| [beatsPerCycle](beats-per-cycle.md) | [common]<br/>val [beatsPerCycle](beats-per-cycle.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 4<br/>Beats per cycle (talam) |
| [bpm](bpm.md) | [common]<br/>val [bpm](bpm.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Beats per minute |
| [samaSamplePath](sama-sample-path.md) | [common]<br/>val [samaSamplePath](sama-sample-path.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Sample for first beat (sama/downbeat) |
| [volume](volume.md) | [common]<br/>val [volume](volume.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f<br/>Volume (0.0 to 1.0) |
