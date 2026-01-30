//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.features](../index.md)/[HpcpExtractor](index.md)/[extractHpcp](extract-hpcp.md)

# extractHpcp

[common]\
expect fun [extractHpcp](extract-hpcp.md)(handle: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)?

Extract HPCP features from audio samples.

#### Return

FloatArray of hpcpSize elements (typically 12), or null on error

#### Parameters

common

| | |
|---|---|
| handle | Native feature extractor handle |
| samples | Audio samples (mono, float, -1.0 to 1.0) |

[android, ios]\
[android, ios]\
actual fun [extractHpcp](extract-hpcp.md)(handle: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)?
