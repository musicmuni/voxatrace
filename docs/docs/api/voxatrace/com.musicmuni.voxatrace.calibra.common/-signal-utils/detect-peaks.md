//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.common](../index.md)/[SignalUtils](index.md)/[detectPeaks](detect-peaks.md)

# detectPeaks

[common]\
fun [detectPeaks](detect-peaks.md)(signal: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), threshold: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): &lt;Error class: unknown class&gt;&lt;[FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)&gt;

Detect peaks in a signal.

Pure Kotlin implementation (replaces native Essentia PeakDetection).

#### Return

Pair of (positions array, amplitudes array) where positions are sample indices

#### Parameters

common

| | |
|---|---|
| signal | Input signal array |
| threshold | Minimum peak amplitude threshold |
