//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.util](../index.md)/[WavUtils](index.md)/[applyFadeOut](apply-fade-out.md)

# applyFadeOut

[common]\
fun [applyFadeOut](apply-fade-out.md)(samples: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), fadeSamples: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)

Apply fade-out envelope to audio samples.

#### Return

New byte array with fade applied

#### Parameters

common

| | |
|---|---|
| samples | PCM bytes (16-bit) |
| fadeSamples | Number of samples to fade |
