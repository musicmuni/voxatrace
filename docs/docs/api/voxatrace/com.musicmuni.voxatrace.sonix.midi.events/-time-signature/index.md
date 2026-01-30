//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.midi.events](../index.md)/[TimeSignature](index.md)

# TimeSignature

[common]\
class [TimeSignature](index.md)(var tick: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), var delta: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), val numerator: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val denominator: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val metronome: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val thirtysecondNotes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) : [MetaEvent](../-meta-event/index.md)

## Constructors

| | |
|---|---|
| [TimeSignature](-time-signature.md) | [common]<br/>constructor(tick: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), delta: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), numerator: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), denominator: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), metronome: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), thirtysecondNotes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [delta](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/delta.md) | [common]<br/>var [delta](../../com.musicmuni.voxatrace.sonix.midi/-midi-event/delta.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
| [denominator](denominator.md) | [common]<br/>val [denominator](denominator.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [length](../-meta-event/length.md) | [common]<br/>val [length](../-meta-event/length.md): [VariableLengthInt](../../com.musicmuni.voxatrace.sonix.midi/-variable-length-int/index.md) |
| [meter](meter.md) | [common]<br/>val [meter](meter.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Get the meter type based on denominator |
| [metronome](metronome.md) | [common]<br/>val [metronome](metronome.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [numerator](numerator.md) | [common]<br/>val [numerator](numerator.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [thirtysecondNotes](thirtysecond-notes.md) | [common]<br/>val [thirtysecondNotes](thirtysecond-notes.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
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
