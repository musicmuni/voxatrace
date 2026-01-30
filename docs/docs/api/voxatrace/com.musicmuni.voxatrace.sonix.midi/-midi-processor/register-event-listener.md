//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.midi](../index.md)/[MidiProcessor](index.md)/[registerEventListener](register-event-listener.md)

# registerEventListener

[common]\
fun [registerEventListener](register-event-listener.md)(listener: [MidiEventListener](../-midi-event-listener/index.md), eventClass: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.reflect/-k-class/index.html)&lt;out [MidiEvent](../-midi-event/index.md)&gt;)

Register a listener for a specific event type.

#### Parameters

common

| | |
|---|---|
| listener | The listener to receive events |
| eventClass | The class of events to listen for (use MidiEvent::class for all events) |
