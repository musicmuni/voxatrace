//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.common](../index.md)/[SignalUtils](index.md)/[sliceAudio](slice-audio.md)

# sliceAudio

[common]\
fun [sliceAudio](slice-audio.md)(audio: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), startTime: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), endTime: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)

Extract a time slice from audio.

#### Return

Audio samples in the specified time range

#### Parameters

common

| | |
|---|---|
| audio | Input audio samples |
| startTime | Start time in seconds |
| endTime | End time in seconds |
| sampleRate | Sample rate in Hz |
