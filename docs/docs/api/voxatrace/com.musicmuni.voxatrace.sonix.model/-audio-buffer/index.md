//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.model](../index.md)/[AudioBuffer](index.md)

# AudioBuffer

[common]\
class [AudioBuffer](index.md)(val data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), val timestamp: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), val durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) = 0)

Audio buffer containing PCM data from recording.

## Constructors

| | |
|---|---|
| [AudioBuffer](-audio-buffer.md) | [common]<br/>constructor(data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), timestamp: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) = 0) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [data](data.md) | [common]<br/>val [data](data.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br/>Raw PCM bytes (16-bit signed, little-endian) |
| [durationMs](duration-ms.md) | [common]<br/>val [durationMs](duration-ms.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) = 0<br/>Duration of this buffer in milliseconds |
| [sampleCount](sample-count.md) | [common]<br/>val [sampleCount](sample-count.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of samples in this buffer. For mono: frameCount = sampleCount For stereo: frameCount = sampleCount / 2 |
| [samples](samples.md) | [common]<br/>val [samples](samples.md): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Lazily computed float samples, normalized to -1.0, 1.0. This avoids allocation if float data is not needed. |
| [size](size.md) | [common]<br/>val [size](size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [timestamp](timestamp.md) | [common]<br/>val [timestamp](timestamp.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br/>Buffer timestamp in milliseconds from recording start |

## Functions

| Name | Summary |
|---|---|
| [fillFloatSamples](fill-float-samples.md) | [common]<br/>fun [fillFloatSamples](fill-float-samples.md)(output: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Fill a pre-allocated FloatArray with normalized samples. This is the zero-allocation path for real-time DSP. |
