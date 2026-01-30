//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.model](../index.md)/[AudioBuffer](index.md)/[fillFloatSamples](fill-float-samples.md)

# fillFloatSamples

[common]\
fun [fillFloatSamples](fill-float-samples.md)(output: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)

Fill a pre-allocated FloatArray with normalized samples. This is the zero-allocation path for real-time DSP.

#### Return

Number of samples written

#### Parameters

common

| | |
|---|---|
| output | Pre-allocated buffer to fill (must be at least sampleCount size) |
