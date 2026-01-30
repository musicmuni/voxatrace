//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.midi](../index.md)/[SystemExclusiveEvent](index.md)

# SystemExclusiveEvent

[common]\
class [SystemExclusiveEvent](index.md)(val typeId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), var tick: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), var delta: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), val data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)) : [MidiEvent](../-midi-event/index.md)

## Constructors

| | |
|---|---|
| [SystemExclusiveEvent](-system-exclusive-event.md) | [common]<br/>constructor(typeId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), tick: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), delta: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [data](data.md) | [common]<br/>val [data](data.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html) |
| [delta](../-midi-event/delta.md) | [common]<br/>var [delta](../-midi-event/delta.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
| [tick](../-midi-event/tick.md) | [common]<br/>var [tick](../-midi-event/tick.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
| [typeId](type-id.md) | [common]<br/>val [typeId](type-id.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [compareTo](../-midi-event/compare-to.md) | [common]<br/>open operator override fun [compareTo](../-midi-event/compare-to.md)(other: [MidiEvent](../-midi-event/index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [getEventSize](get-event-size.md) | [common]<br/>open override fun [getEventSize](get-event-size.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [getSize](../-midi-event/get-size.md) | [common]<br/>fun [getSize](../-midi-event/get-size.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [requiresStatusByte](../-midi-event/requires-status-byte.md) | [common]<br/>open fun [requiresStatusByte](../-midi-event/requires-status-byte.md)(prevEvent: [MidiEvent](../-midi-event/index.md)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [writeToFile](write-to-file.md) | [common]<br/>open override fun [writeToFile](write-to-file.md)(sink: BufferedSink, writeType: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)) |
