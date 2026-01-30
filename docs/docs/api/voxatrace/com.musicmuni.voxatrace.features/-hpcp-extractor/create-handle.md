//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.features](../index.md)/[HpcpExtractor](index.md)/[createHandle](create-handle.md)

# createHandle

[common]\
expect fun [createHandle](create-handle.md)(frameSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), hpcpSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 12): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)?

Create a native handle for standalone HPCP extraction.

Uses dedicated voxatrace_hpcp_* C API for efficient HPCP extraction without computing pitch. This avoids wastefully computing YIN pitch when pitch is already available from a detector.

#### Return

Native handle (Long), or null if creation fails

#### Parameters

common

| | |
|---|---|
| frameSize | Frame size in samples (typically 2048 for FFT) |
| sampleRate | Sample rate in Hz (typically 16000) |
| hpcpSize | HPCP vector size (typically 12 for semitones) |

[android, ios]\
[android, ios]\
actual fun [createHandle](create-handle.md)(frameSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), hpcpSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)?
