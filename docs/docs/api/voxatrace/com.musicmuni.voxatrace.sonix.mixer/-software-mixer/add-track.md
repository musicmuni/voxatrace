//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.mixer](../index.md)/[SoftwareMixer](index.md)/[addTrack](add-track.md)

# addTrack

[common]\
open suspend override fun [addTrack](add-track.md)(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), filePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Add a track from an audio file. The file will be decoded and cached in memory.

#### Return

true if track was added successfully

#### Parameters

common

| | |
|---|---|
| id | Unique identifier for this track |
| filePath | Path to audio file (MP3, M4A, WAV, etc.) |
