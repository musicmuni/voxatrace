//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.model](../index.md)/[AudioRawData](index.md)

# AudioRawData

[common]\
class [AudioRawData](index.md)(val audioData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), val durationMilliSecs: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val numChannels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1, val sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000)

## Constructors

| | |
|---|---|
| [AudioRawData](-audio-raw-data.md) | [common]<br/>constructor(audioData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), durationMilliSecs: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), numChannels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1, sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000) |

## Properties

| Name | Summary |
|---|---|
| [audioData](audio-data.md) | [common]<br/>val [audioData](audio-data.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html) |
| [durationMilliSecs](duration-milli-secs.md) | [common]<br/>val [durationMilliSecs](duration-milli-secs.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [numChannels](num-channels.md) | [common]<br/>val [numChannels](num-channels.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1 |
| [sampleRate](sample-rate.md) | [common]<br/>val [sampleRate](sample-rate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000 |
| [samples](samples.md) | [common]<br/>val [samples](samples.md): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Audio data as Float32 samples in the range -1.0 to 1.0. |
