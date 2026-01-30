//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.midi](../index.md)/[MidiFile](index.md)

# MidiFile

[common]\
class [MidiFile](index.md)(var resolution: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = DEFAULT_RESOLUTION, val tracks: [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-mutable-list/index.html)&lt;[MidiTrack](../-midi-track/index.md)&gt; = ArrayList())

## Constructors

| | |
|---|---|
| [MidiFile](-midi-file.md) | [common]<br/>constructor(resolution: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = DEFAULT_RESOLUTION, tracks: [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-mutable-list/index.html)&lt;[MidiTrack](../-midi-track/index.md)&gt; = ArrayList()) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [resolution](resolution.md) | [common]<br/>var [resolution](resolution.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [tracks](tracks.md) | [common]<br/>val [tracks](tracks.md): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-mutable-list/index.html)&lt;[MidiTrack](../-midi-track/index.md)&gt; |
| [type](type.md) | [common]<br/>var [type](type.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [addTrack](add-track.md) | [common]<br/>fun [addTrack](add-track.md)(track: [MidiTrack](../-midi-track/index.md), pos: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = tracks.size) |
| [getLengthInTicks](get-length-in-ticks.md) | [common]<br/>fun [getLengthInTicks](get-length-in-ticks.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
| [removeTrack](remove-track.md) | [common]<br/>fun [removeTrack](remove-track.md)(pos: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) |
| [toByteArray](to-byte-array.md) | [common]<br/>fun [toByteArray](to-byte-array.md)(): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br/>Convert MIDI file to ByteArray (Standard MIDI File format). Useful for Swift interop where Okio FileSystem is not available. |
| [writeTo](write-to.md) | [common]<br/>fun [writeTo](write-to.md)(sink: BufferedSink)<br/>Write MIDI file to a BufferedSink |
| [writeToFile](write-to-file.md) | [common]<br/>fun [writeToFile](write-to-file.md)(fileSystem: FileSystem, path: Path)<br/>Write to a file path (expects Okio FileSystem + Path) |
