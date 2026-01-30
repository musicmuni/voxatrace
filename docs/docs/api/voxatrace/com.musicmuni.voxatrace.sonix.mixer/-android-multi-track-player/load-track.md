//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.mixer](../index.md)/[AndroidMultiTrackPlayer](index.md)/[loadTrack](load-track.md)

# loadTrack

[android]\
open override fun [loadTrack](load-track.md)(trackName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Load a track from decoded PCM data.

#### Return

true if track was loaded successfully

#### Parameters

android

| | |
|---|---|
| trackName | Unique identifier for this track |
| data | PCM audio data (16-bit samples) |
| sampleRate | Sample rate in Hz |
| channels | Number of audio channels (1 or 2) |
