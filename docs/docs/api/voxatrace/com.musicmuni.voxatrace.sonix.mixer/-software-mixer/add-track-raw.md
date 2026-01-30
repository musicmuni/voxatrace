//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.mixer](../index.md)/[SoftwareMixer](index.md)/[addTrackRaw](add-track-raw.md)

# addTrackRaw

[common]\
open override fun [addTrackRaw](add-track-raw.md)(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))

Add a track from raw PCM data.

#### Parameters

common

| | |
|---|---|
| id | Unique identifier for this track |
| samples | PCM samples as floats (-1.0 to 1.0) |
| sampleRate | Sample rate of the audio |
| channels | Number of channels (1=mono, 2=stereo) |
