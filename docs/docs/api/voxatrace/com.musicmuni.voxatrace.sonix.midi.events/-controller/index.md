//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.midi.events](../index.md)/[Controller](index.md)

# Controller

[common]\
class [Controller](index.md)(var tick: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), var delta: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), val channel: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), controllerType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), value: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) : [ChannelEvent](../-channel-event/index.md)

## Constructors

| | |
|---|---|
| [Controller](-controller.md) | [common]<br/>constructor(tick: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), delta: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), channel: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), controllerType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), value: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [channel](../-channel-event/channel.md) | [common]<br/>val [channel](../-channel-event/channel.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [delta](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/delta.md) | [common]<br/>var [delta](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/delta.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
| [param1](../-channel-event/param1.md) | [common]<br/>var [param1](../-channel-event/param1.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [param2](../-channel-event/param2.md) | [common]<br/>var [param2](../-channel-event/param2.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [tick](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/tick.md) | [common]<br/>var [tick](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/tick.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
| [type](../-channel-event/type.md) | [common]<br/>val [type](../-channel-event/type.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [compareTo](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/compare-to.md) | [common]<br/>open operator override fun [compareTo](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/compare-to.md)(other: [MidiEvent](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [getEventSize](../-channel-event/get-event-size.md) | [common]<br/>open override fun [getEventSize](../-channel-event/get-event-size.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [getSize](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/get-size.md) | [common]<br/>fun [getSize](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/get-size.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [requiresStatusByte](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/requires-status-byte.md) | [common]<br/>open fun [requiresStatusByte](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/requires-status-byte.md)(prevEvent: [MidiEvent](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/index.md)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [writeToFile](../-channel-event/write-to-file.md) | [common]<br/>open override fun [writeToFile](../-channel-event/write-to-file.md)(sink: BufferedSink, writeType: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)) |
