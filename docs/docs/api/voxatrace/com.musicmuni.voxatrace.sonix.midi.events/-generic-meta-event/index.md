//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.midi.events](../index.md)/[GenericMetaEvent](index.md)

# GenericMetaEvent

[common]\
class [GenericMetaEvent](index.md)(var tick: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), var delta: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), val type: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val length: [VariableLengthInt](../../com.musicmuni.voxatrace.sonix.midi/-variable-length-int/index.md), val data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)) : [MetaEvent](../-meta-event/index.md)

## Constructors

| | |
|---|---|
| [GenericMetaEvent](-generic-meta-event.md) | [common]<br/>constructor(tick: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), delta: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), type: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), length: [VariableLengthInt](../../com.musicmuni.voxatrace.sonix.midi/-variable-length-int/index.md), data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [data](data.md) | [common]<br/>val [data](data.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html) |
| [delta](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/delta.md) | [common]<br/>var [delta](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/delta.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
| [length](../-meta-event/length.md) | [common]<br/>val [length](../-meta-event/length.md): [VariableLengthInt](../../com.musicmuni.voxatrace.sonix.midi/-variable-length-int/index.md) |
| [tick](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/tick.md) | [common]<br/>var [tick](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/tick.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
| [type](../-meta-event/type.md) | [common]<br/>val [type](../-meta-event/type.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [compareTo](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/compare-to.md) | [common]<br/>open operator override fun [compareTo](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/compare-to.md)(other: [MidiEvent](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [getEventSize](get-event-size.md) | [common]<br/>open override fun [getEventSize](get-event-size.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [getSize](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/get-size.md) | [common]<br/>fun [getSize](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/get-size.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [requiresStatusByte](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/requires-status-byte.md) | [common]<br/>open fun [requiresStatusByte](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/requires-status-byte.md)(prevEvent: [MidiEvent](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/index.md)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [writeToFile](write-to-file.md) | [common]<br/>open override fun [writeToFile](write-to-file.md)(sink: BufferedSink, writeType: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)) |
