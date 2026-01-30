//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.model](../index.md)/[TransSegment](index.md)

# TransSegment

[common]\
@Serializable

data class [TransSegment](index.md)(val id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val lyrics: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val timeStamp: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-double/index.html)&gt;, val trans: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[TransNote](../-trans-note/index.md)&gt;)

A segment within a transcription file.

## Constructors

| | |
|---|---|
| [TransSegment](-trans-segment.md) | [common]<br/>constructor(id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), lyrics: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), timeStamp: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-double/index.html)&gt;, trans: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[TransNote](../-trans-note/index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [endTime](end-time.md) | [common]<br/>val [endTime](end-time.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-double/index.html) |
| [id](id.md) | [common]<br/>val [id](id.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Segment identifier |
| [lyrics](lyrics.md) | [common]<br/>val [lyrics](lyrics.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Text lyrics for this segment |
| [startTime](start-time.md) | [common]<br/>val [startTime](start-time.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-double/index.html) |
| [timeStamp](time-stamp.md) | [common]<br/>@SerialName(value = &quot;time_stamp&quot;)<br/>val [timeStamp](time-stamp.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-double/index.html)&gt;<br/>Start and end timestamps start, end in seconds |
| [trans](trans.md) | [common]<br/>val [trans](trans.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[TransNote](../-trans-note/index.md)&gt;<br/>List of notes in this segment |
