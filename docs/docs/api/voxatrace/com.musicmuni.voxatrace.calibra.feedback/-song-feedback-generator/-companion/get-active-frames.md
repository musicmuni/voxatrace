//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra.feedback](../../index.md)/[SongFeedbackGenerator](../index.md)/[Companion](index.md)/[getActiveFrames](get-active-frames.md)

# getActiveFrames

[common]\
fun [getActiveFrames](get-active-frames.md)(pitches: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), seg: [SongFeedbackGenerator.MelodySegment](../-melody-segment/index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)

Counts active (non-silence) frames in a segment.

#### Return

Number of frames with valid pitch

#### Parameters

common

| | |
|---|---|
| pitches | Pitch values in MIDI |
| seg | Segment to analyze |
