//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.model](../index.md)/[AudioRawData](index.md)/[samples](samples.md)

# samples

[common]\
val [samples](samples.md): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)

Audio data as Float32 samples in the range -1.0 to 1.0.

Converts the internal Int16 PCM data to normalized float samples. This is useful for audio processing, analysis, and visualization.

The samples are interleaved for stereo audio (L, R, L, R, ...).
