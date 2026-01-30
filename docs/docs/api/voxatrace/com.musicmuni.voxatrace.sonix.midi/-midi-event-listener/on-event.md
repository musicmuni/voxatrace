//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.midi](../index.md)/[MidiEventListener](index.md)/[onEvent](on-event.md)

# onEvent

[common]\
abstract fun [onEvent](on-event.md)(event: [MidiEvent](../-midi-event/index.md), ms: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))

Called when a MIDI event occurs during playback.

This includes all MIDI events (notes, tempo changes, etc.) as well as synthetic MetronomeTick events for beat tracking.

#### Parameters

common

| | |
|---|---|
| event | The MIDI event that occurred |
| ms | Current playback position in milliseconds |
