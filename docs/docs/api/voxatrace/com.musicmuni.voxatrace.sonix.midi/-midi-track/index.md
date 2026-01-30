//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.midi](../index.md)/[MidiTrack](index.md)

# MidiTrack

[common]\
class [MidiTrack](index.md)

## Constructors

| | |
|---|---|
| [MidiTrack](-midi-track.md) | [common]<br/>constructor() |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [endOfTrackDelta](end-of-track-delta.md) | [common]<br/>var [endOfTrackDelta](end-of-track-delta.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
| [events](events.md) | [common]<br/>val [events](events.md): &lt;Error class: unknown class&gt; |
| [size](size.md) | [common]<br/>val [size](size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [closeTrack](close-track.md) | [common]<br/>fun [closeTrack](close-track.md)() |
| [getLengthInTicks](get-length-in-ticks.md) | [common]<br/>fun [getLengthInTicks](get-length-in-ticks.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
| [insertEvent](insert-event.md) | [common]<br/>fun [insertEvent](insert-event.md)(newEvent: [MidiEvent](../-midi-event/index.md)) |
| [insertNote](insert-note.md) | [common]<br/>fun [insertNote](insert-note.md)(channel: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), pitch: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), velocity: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), tick: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), duration: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Insert a note with automatic NoteOn and NoteOff events |
| [removeEvent](remove-event.md) | [common]<br/>fun [removeEvent](remove-event.md)(event: [MidiEvent](../-midi-event/index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [writeTo](write-to.md) | [common]<br/>fun [writeTo](write-to.md)(sink: BufferedSink)<br/>Write track to BufferedSink |
