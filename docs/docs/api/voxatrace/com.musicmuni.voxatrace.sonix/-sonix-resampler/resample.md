//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixResampler](index.md)/[resample](resample.md)

# resample

[common]\
expect fun [resample](resample.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), fromRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), toRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)

Resample audio from one sample rate to another.

Uses high-quality sinc interpolation for best audio quality.

#### Return

Resampled audio samples at target rate

#### Parameters

common

| | |
|---|---|
| samples | Input audio samples (mono, float in -1.0, 1.0 range) |
| fromRate | Source sample rate in Hz (e.g., 44100) |
| toRate | Target sample rate in Hz (e.g., 16000) |

[android, ios]\
[android, ios]\
actual fun [resample](resample.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), fromRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), toRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)
