//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.midi](../index.md)/[MidiEvent](index.md)

# MidiEvent

abstract class [MidiEvent](index.md)(var tick: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), var delta: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)) : [Comparable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-comparable/index.html)&lt;[MidiEvent](index.md)&gt; 

#### Inheritors

| |
|---|
| [MetronomeTick](../-metronome-tick/index.md) |
| [SystemExclusiveEvent](../-system-exclusive-event/index.md) |
| [ChannelEvent](../../com.musicmuni.voxatrace.sonix.midi.events/-channel-event/index.md) |
| [MetaEvent](../../com.musicmuni.voxatrace.sonix.midi.events/-meta-event/index.md) |

## Constructors

| | |
|---|---|
| [MidiEvent](-midi-event.md) | [common]<br/>constructor(tick: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), delta: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [delta](delta.md) | [common]<br/>var [delta](delta.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
| [tick](tick.md) | [common]<br/>var [tick](tick.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |

## Functions

| Name | Summary |
|---|---|
| [compareTo](compare-to.md) | [common]<br/>open operator override fun [compareTo](compare-to.md)(other: [MidiEvent](index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [getEventSize](get-event-size.md) | [common]<br/>abstract fun [getEventSize](get-event-size.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [getSize](get-size.md) | [common]<br/>fun [getSize](get-size.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [requiresStatusByte](requires-status-byte.md) | [common]<br/>open fun [requiresStatusByte](requires-status-byte.md)(prevEvent: [MidiEvent](index.md)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [writeToFile](write-to-file.md) | [common]<br/>open fun [writeToFile](write-to-file.md)(sink: BufferedSink, writeType: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)) |
