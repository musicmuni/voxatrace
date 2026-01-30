//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.features](../index.md)/[PitchHpcpExtractor](index.md)/[extractFeatures](extract-features.md)

# extractFeatures

[common]\
expect fun [extractFeatures](extract-features.md)(handle: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)?

Extract frame features (pitch + HPCP) from a native handle.

#### Return

FloatArray of 13 elements [pitchHz, hpcp0..11], or null on error

#### Parameters

common

| | |
|---|---|
| handle | Native feature extractor handle |
| samples | Audio samples (mono, float, -1.0 to 1.0) |

[android, ios]\
[android, ios]\
actual fun [extractFeatures](extract-features.md)(handle: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)?
