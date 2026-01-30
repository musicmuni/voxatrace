//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.midi.events](../index.md)/[MetaEvent](index.md)

# MetaEvent

abstract class [MetaEvent](index.md)(var tick: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), var delta: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), val type: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val length: [VariableLengthInt](../../com.musicmuni.voxatrace.sonix.midi/-variable-length-int/index.md)) : [MidiEvent](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/index.md)

#### Inheritors

| |
|---|
| [Tempo](../-tempo/index.md) |
| [EndOfTrack](../-end-of-track/index.md) |
| [TimeSignature](../-time-signature/index.md) |
| [TextualMetaEvent](../-textual-meta-event/index.md) |
| [GenericMetaEvent](../-generic-meta-event/index.md) |

## Constructors

| | |
|---|---|
| [MetaEvent](-meta-event.md) | [common]<br/>constructor(tick: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), delta: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), type: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), length: [VariableLengthInt](../../com.musicmuni.voxatrace.sonix.midi/-variable-length-int/index.md)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [delta](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/delta.md) | [common]<br/>var [delta](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/delta.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
| [length](length.md) | [common]<br/>val [length](length.md): [VariableLengthInt](../../com.musicmuni.voxatrace.sonix.midi/-variable-length-int/index.md) |
| [tick](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/tick.md) | [common]<br/>var [tick](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/tick.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
| [type](type.md) | [common]<br/>val [type](type.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [compareTo](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/compare-to.md) | [common]<br/>open operator override fun [compareTo](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/compare-to.md)(other: [MidiEvent](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [getEventSize](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/get-event-size.md) | [common]<br/>abstract fun [getEventSize](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/get-event-size.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [getSize](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/get-size.md) | [common]<br/>fun [getSize](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/get-size.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [requiresStatusByte](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/requires-status-byte.md) | [common]<br/>open fun [requiresStatusByte](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/requires-status-byte.md)(prevEvent: [MidiEvent](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/index.md)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [writeToFile](write-to-file.md) | [common]<br/>open override fun [writeToFile](write-to-file.md)(sink: BufferedSink, writeType: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)) |
