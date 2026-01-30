//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.util](../index.md)/[WavUtils](index.md)

# WavUtils

[common]\
object [WavUtils](index.md)

Utilities for reading and writing WAV files.

## Types

| Name | Summary |
|---|---|
| [WavHeader](-wav-header/index.md) | [common]<br/>data class [WavHeader](-wav-header/index.md)(val sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val bitsPerSample: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val dataSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))<br/>Parsed WAV header information. |

## Properties

| Name | Summary |
|---|---|
| [WAV_HEADER_SIZE](-w-a-v_-h-e-a-d-e-r_-s-i-z-e.md) | [common]<br/>const val [WAV_HEADER_SIZE](-w-a-v_-h-e-a-d-e-r_-s-i-z-e.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44 |

## Functions

| Name | Summary |
|---|---|
| [applyFadeOut](apply-fade-out.md) | [common]<br/>fun [applyFadeOut](apply-fade-out.md)(samples: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), fadeSamples: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br/>Apply fade-out envelope to audio samples. |
| [checkDataUsingWavHeader](check-data-using-wav-header.md) | [common]<br/>fun [checkDataUsingWavHeader](check-data-using-wav-header.md)(header: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), dataSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [createWavBytes](create-wav-bytes.md) | [common]<br/>fun [createWavBytes](create-wav-bytes.md)(pcmData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1, bitsPerSample: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br/>Create complete WAV file bytes from PCM data. |
| [createWavHeader](create-wav-header.md) | [common]<br/>fun [createWavHeader](create-wav-header.md)(totalAudioLen: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), bitsPerSample: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html) |
| [extractPcmData](extract-pcm-data.md) | [common]<br/>fun [extractPcmData](extract-pcm-data.md)(wavBytes: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)?<br/>Extract raw PCM data from WAV bytes (skips header). |
| [getDataSizeFromHeader](get-data-size-from-header.md) | [common]<br/>fun [getDataSizeFromHeader](get-data-size-from-header.md)(header: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), headerSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [parseHeader](parse-header.md) | [common]<br/>fun [parseHeader](parse-header.md)(data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)): [WavUtils.WavHeader](-wav-header/index.md)?<br/>Parse WAV header from bytes. |
