//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.common](../index.md)/[SignalUtils](index.md)/[trimSilence](trim-silence.md)

# trimSilence

[common]\
fun [trimSilence](trim-silence.md)(audio: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), threshold: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.001f): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)

Trim silence from beginning and end of audio.

#### Return

Trimmed audio samples

#### Parameters

common

| | |
|---|---|
| audio | Input audio samples |
| threshold | Amplitude threshold for silence detection |
