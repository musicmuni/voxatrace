//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.features](../index.md)/[PitchHpcpExtractor](index.md)

# PitchHpcpExtractor

[common]\
expect object [PitchHpcpExtractor](index.md)

Platform-specific Pitch+HPCP feature extraction (expect/actual).

Provides the &quot;C-min&quot; part of the &quot;Kotlin-max, C-min&quot; architecture, wrapping native PitchHpcpExtractor for per-frame feature extraction.

[android]\
actual object [PitchHpcpExtractor](index.md)

Android actual implementation for Pitch+HPCP feature extraction. Delegates to PitchHpcpExtractorJni.

[ios]\
actual object [PitchHpcpExtractor](index.md)

## Functions

| Name | Summary |
|---|---|
| [createHandle](create-handle.md) | [common]<br/>expect fun [createHandle](create-handle.md)(frameSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), pitchTolerance: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), yinMinPitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), yinMaxPitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), hpcpSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 12): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)?<br/>Create a native handle for feature extraction (pitch + HPCP).<br/>[android, ios]<br/>[android, ios]<br/>actual fun [createHandle](create-handle.md)(frameSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), pitchTolerance: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), yinMinPitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), yinMaxPitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), hpcpSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)? |
| [destroyHandle](destroy-handle.md) | [common]<br/>expect fun [destroyHandle](destroy-handle.md)(handle: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Destroy a native feature handle.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [destroyHandle](destroy-handle.md)(handle: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)) |
| [extractFeatures](extract-features.md) | [common]<br/>expect fun [extractFeatures](extract-features.md)(handle: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)?<br/>Extract frame features (pitch + HPCP) from a native handle.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [extractFeatures](extract-features.md)(handle: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)? |
