//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.features](../index.md)/[PitchHpcpExtractor](index.md)/[createHandle](create-handle.md)

# createHandle

[common]\
expect fun [createHandle](create-handle.md)(frameSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), pitchTolerance: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), yinMinPitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), yinMaxPitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), hpcpSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 12): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)?

Create a native handle for feature extraction (pitch + HPCP).

#### Return

Native handle (Long), or null if creation fails

#### Parameters

common

| | |
|---|---|
| frameSize | Frame size in samples (typically 1024) |
| sampleRate | Sample rate in Hz (typically 16000) |
| pitchTolerance | YIN tolerance parameter (typically 0.15) |
| yinMinPitch | Minimum detectable pitch in Hz (-1 = auto) |
| yinMaxPitch | Maximum detectable pitch in Hz (-1 = auto) |
| hpcpSize | HPCP vector size (typically 12) |

[android, ios]\
[android, ios]\
actual fun [createHandle](create-handle.md)(frameSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), pitchTolerance: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), yinMinPitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), yinMaxPitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), hpcpSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)?
