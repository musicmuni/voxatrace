//[voxatrace](../../index.md)/[com.musicmuni.voxatrace.sonix.midi](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [MetronomeTick](-metronome-tick/index.md) | [common]<br/>class [MetronomeTick](-metronome-tick/index.md)(resolution: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), signature: [TimeSignature](../com.musicmuni.voxatrace.sonix.midi.events/-time-signature/index.md) = TimeSignature.default()) : [MidiEvent](-midi-event/index.md)<br/>A synthetic MIDI event that represents a metronome tick. |
| [MidiEvent](-midi-event/index.md) | [common]<br/>abstract class [MidiEvent](-midi-event/index.md)(var tick: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), var delta: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)) : [Comparable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-comparable/index.html)&lt;[MidiEvent](-midi-event/index.md)&gt; |
| [MidiEventListener](-midi-event-listener/index.md) | [common]<br/>interface [MidiEventListener](-midi-event-listener/index.md)<br/>Listener interface for MIDI playback events from MidiProcessor. |
| [MidiFile](-midi-file/index.md) | [common]<br/>class [MidiFile](-midi-file/index.md)(var resolution: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = DEFAULT_RESOLUTION, val tracks: [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-mutable-list/index.html)&lt;[MidiTrack](-midi-track/index.md)&gt; = ArrayList()) |
| [MidiNote](-midi-note/index.md) | [common]<br/>data class [MidiNote](-midi-note/index.md)(var note: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0, var startTime: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, var endTime: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f)<br/>Represents a MIDI note with timing information. Used for MIDI synthesis and generation. |
| [MidiProcessor](-midi-processor/index.md) | [common]<br/>class [MidiProcessor](-midi-processor/index.md)(midiFile: [MidiFile](-midi-file/index.md))<br/>Real-time MIDI event processor that dispatches events at correct timestamps. |
| [MidiTrack](-midi-track/index.md) | [common]<br/>class [MidiTrack](-midi-track/index.md) |
| [MidiUtil](-midi-util/index.md) | [common]<br/>object [MidiUtil](-midi-util/index.md) |
| [SystemExclusiveEvent](-system-exclusive-event/index.md) | [common]<br/>class [SystemExclusiveEvent](-system-exclusive-event/index.md)(val typeId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), var tick: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), var delta: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), val data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)) : [MidiEvent](-midi-event/index.md) |
| [VariableLengthInt](-variable-length-int/index.md) | [common]<br/>class [VariableLengthInt](-variable-length-int/index.md)(val value: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) |
