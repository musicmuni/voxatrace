//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra.feedback](../../index.md)/[SongFeedbackGenerator](../index.md)/[Companion](index.md)/[filterVoiceSegment](filter-voice-segment.md)

# filterVoiceSegment

[common]\
fun [filterVoiceSegment](filter-voice-segment.md)(pitches: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), times: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), seg: [SongFeedbackGenerator.MelodySegment](../-melody-segment/index.md))

Filters a voice segment to remove leading/trailing silence.

#### Parameters

common

| | |
|---|---|
| pitches | Pitch values in MIDI |
| times | Timestamps corresponding to pitches |
| seg | Segment to filter (modified in-place) |
