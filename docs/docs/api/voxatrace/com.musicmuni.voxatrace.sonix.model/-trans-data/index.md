//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.model](../index.md)/[TransData](index.md)

# TransData

[common]\
data class [TransData](index.md)(val segments: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[TransSegment](../-trans-segment/index.md)&gt;)

Parsed transcription data from a .trans JSON file.

## Constructors

| | |
|---|---|
| [TransData](-trans-data.md) | [common]<br/>constructor(segments: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[TransSegment](../-trans-segment/index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [count](count.md) | [common]<br/>val [count](count.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [segments](segments.md) | [common]<br/>val [segments](segments.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[TransSegment](../-trans-segment/index.md)&gt;<br/>List of transcription segments |

## Functions

| Name | Summary |
|---|---|
| [toNotesData](to-notes-data.md) | [common]<br/>fun [toNotesData](to-notes-data.md)(): [NotesData](../-notes-data/index.md)<br/>Get all notes across all segments as a flat NotesData structure. |
