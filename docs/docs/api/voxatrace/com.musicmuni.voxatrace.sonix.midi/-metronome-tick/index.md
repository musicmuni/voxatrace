//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.midi](../index.md)/[MetronomeTick](index.md)

# MetronomeTick

[common]\
class [MetronomeTick](index.md)(resolution: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), signature: [TimeSignature](../../com.musicmuni.voxatrace.sonix.midi.events/-time-signature/index.md) = TimeSignature.default()) : [MidiEvent](../-midi-event/index.md)

A synthetic MIDI event that represents a metronome tick.

MidiProcessor generates these events at regular beat intervals based on the current time signature. Listeners can use these to synchronize visual beat displays or other beat-synchronized operations.

Unlike other MidiEvents, MetronomeTick is not parsed from or written to MIDI files. It's generated in real-time during playback.

Usage:

```kotlin
processor.registerEventListener(object : MidiEventListener {
    override fun onEvent(event: MidiEvent, ms: Long) {
        if (event is MetronomeTick) {
            updateBeatDisplay(event.measure, event.beatNumber)
        }
    }
})
```

## Constructors

| | |
|---|---|
| [MetronomeTick](-metronome-tick.md) | [common]<br/>constructor(resolution: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), signature: [TimeSignature](../../com.musicmuni.voxatrace.sonix.midi.events/-time-signature/index.md) = TimeSignature.default()) |

## Properties

| Name | Summary |
|---|---|
| [beatNumber](beat-number.md) | [common]<br/>val [beatNumber](beat-number.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Current beat number within the measure (1-based) |
| [beatsPerMeasure](beats-per-measure.md) | [common]<br/>val [beatsPerMeasure](beats-per-measure.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of beats per measure from current time signature |
| [delta](../-midi-event/delta.md) | [common]<br/>var [delta](../-midi-event/delta.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
| [measure](measure.md) | [common]<br/>val [measure](measure.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Current measure number (1-based) |
| [tick](../-midi-event/tick.md) | [common]<br/>var [tick](../-midi-event/tick.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |

## Functions

| Name | Summary |
|---|---|
| [compareTo](../-midi-event/compare-to.md) | [common]<br/>open operator override fun [compareTo](../-midi-event/compare-to.md)(other: [MidiEvent](../-midi-event/index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [getEventSize](get-event-size.md) | [common]<br/>open override fun [getEventSize](get-event-size.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [getSize](../-midi-event/get-size.md) | [common]<br/>fun [getSize](../-midi-event/get-size.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [requiresStatusByte](../-midi-event/requires-status-byte.md) | [common]<br/>open fun [requiresStatusByte](../-midi-event/requires-status-byte.md)(prevEvent: [MidiEvent](../-midi-event/index.md)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [reset](reset.md) | [common]<br/>fun [reset](reset.md)()<br/>Reset metronome to initial state. |
| [setTimeSignature](set-time-signature.md) | [common]<br/>fun [setTimeSignature](set-time-signature.md)(sig: [TimeSignature](../../com.musicmuni.voxatrace.sonix.midi.events/-time-signature/index.md))<br/>Update the time signature and reset beat tracking. |
| [toString](to-string.md) | [common]<br/>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [update](update.md) | [common]<br/>fun [update](update.md)(ticksElapsed: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-double/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Update metronome state based on elapsed ticks. |
| [writeToFile](../-midi-event/write-to-file.md) | [common]<br/>open fun [writeToFile](../-midi-event/write-to-file.md)(sink: BufferedSink, writeType: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)) |
