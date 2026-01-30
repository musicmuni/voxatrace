//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra.feedback](../../index.md)/[SongFeedbackGenerator](../index.md)/[Companion](index.md)

# Companion

[common]\
object [Companion](index.md)

## Properties

| Name | Summary |
|---|---|
| [COULD_NOT_EVAL](-c-o-u-l-d_-n-o-t_-e-v-a-l.md) | [common]<br/>const val [COULD_NOT_EVAL](-c-o-u-l-d_-n-o-t_-e-v-a-l.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Value indicating evaluation could not be performed |
| [COULD_NOT_HEAR](-c-o-u-l-d_-n-o-t_-h-e-a-r.md) | [common]<br/>const val [COULD_NOT_HEAR](-c-o-u-l-d_-n-o-t_-h-e-a-r.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Value indicating student couldn't be heard |

## Functions

| Name | Summary |
|---|---|
| [filterVoiceSegment](filter-voice-segment.md) | [common]<br/>fun [filterVoiceSegment](filter-voice-segment.md)(pitches: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), times: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), seg: [SongFeedbackGenerator.MelodySegment](../-melody-segment/index.md))<br/>Filters a voice segment to remove leading/trailing silence. |
| [getActiveFrames](get-active-frames.md) | [common]<br/>fun [getActiveFrames](get-active-frames.md)(pitches: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), seg: [SongFeedbackGenerator.MelodySegment](../-melody-segment/index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Counts active (non-silence) frames in a segment. |
| [getIndicesVocalSegments](get-indices-vocal-segments.md) | [common]<br/>fun [getIndicesVocalSegments](get-indices-vocal-segments.md)(segs: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SongFeedbackGenerator.MelodySegment](../-melody-segment/index.md)&gt;, timestamps: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html))<br/>Maps vocal segments to indices in the pitch sequence. |
