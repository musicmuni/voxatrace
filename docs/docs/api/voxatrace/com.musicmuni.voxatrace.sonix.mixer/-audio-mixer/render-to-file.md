//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.mixer](../index.md)/[AudioMixer](index.md)/[renderToFile](render-to-file.md)

# renderToFile

[common]\
abstract suspend fun [renderToFile](render-to-file.md)(outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), encoder: [AudioEncoder](../../com.musicmuni.voxatrace.sonix.recorder/-audio-encoder/index.md), outputSampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44100): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Render all tracks directly to an encoded file.

#### Return

true if rendering and encoding succeeded

#### Parameters

common

| | |
|---|---|
| outputPath | Path for output file |
| encoder | AudioEncoder to use for encoding |
| outputSampleRate | Target sample rate |
