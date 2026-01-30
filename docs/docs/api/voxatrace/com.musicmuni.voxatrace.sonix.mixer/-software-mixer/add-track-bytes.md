//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.mixer](../index.md)/[SoftwareMixer](index.md)/[addTrackBytes](add-track-bytes.md)

# addTrackBytes

[common]\
open override fun [addTrackBytes](add-track-bytes.md)(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), pcmData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))

Add a track from raw PCM bytes.

#### Parameters

common

| | |
|---|---|
| id | Unique identifier for this track |
| pcmData | Raw PCM bytes (16-bit signed little-endian) |
| sampleRate | Sample rate of the audio |
| channels | Number of channels (1=mono, 2=stereo) |
