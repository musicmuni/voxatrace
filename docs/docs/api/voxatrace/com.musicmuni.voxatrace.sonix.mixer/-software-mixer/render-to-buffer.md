//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.mixer](../index.md)/[SoftwareMixer](index.md)/[renderToBuffer](render-to-buffer.md)

# renderToBuffer

[common]\
open suspend override fun [renderToBuffer](render-to-buffer.md)(outputSampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), outputChannels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)

Render all tracks to a float sample buffer. All tracks are mixed and resampled to the output sample rate.

#### Return

Mixed audio as float samples (-1.0 to 1.0)

#### Parameters

common

| | |
|---|---|
| outputSampleRate | Target sample rate for output |
| outputChannels | Target channel count (1=mono, 2=stereo) |
