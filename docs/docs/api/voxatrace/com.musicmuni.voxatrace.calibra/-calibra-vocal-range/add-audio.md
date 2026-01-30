//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraVocalRange](index.md)/[addAudio](add-audio.md)

# addAudio

[common]\
fun [addAudio](add-audio.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html))

Add audio samples for analysis.

Internally buffers samples to reach the required pitch detection window size, then performs pitch detection with confidence and accumulates stable pitches into the histogram for range analysis.

#### Parameters

common

| | |
|---|---|
| samples | Audio samples as Float (-1.0 to 1.0 range) |
