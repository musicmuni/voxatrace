//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.midi](../index.md)/[MidiProcessor](index.md)

# MidiProcessor

[common]\
class [MidiProcessor](index.md)(midiFile: [MidiFile](../-midi-file/index.md))

Real-time MIDI event processor that dispatches events at correct timestamps.

MidiProcessor reads a MidiFile and dispatches events to registered listeners as time progresses. It handles tempo changes dynamically and generates MetronomeTick events for beat synchronization.

Usage:

```kotlin
val midiFile = MidiFile.parse(source)
val processor = MidiProcessor(midiFile)

// Listen for all events
processor.registerEventListener(myListener, MidiEvent::class)

// Or listen for specific event types
processor.registerEventListener(myListener, NoteOn::class)
processor.registerEventListener(myListener, MetronomeTick::class)

// Start playback
processor.start()

// Stop when done
processor.stop()
processor.reset()
```

## Constructors

| | |
|---|---|
| [MidiProcessor](-midi-processor.md) | [common]<br/>constructor(midiFile: [MidiFile](../-midi-file/index.md)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [currentTimeMs](current-time-ms.md) | [common]<br/>val [currentTimeMs](current-time-ms.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br/>Current playback position in milliseconds. |
| [currentTimeTicks](current-time-ticks.md) | [common]<br/>val [currentTimeTicks](current-time-ticks.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-double/index.html)<br/>Current playback position in MIDI ticks. |

## Functions

| Name | Summary |
|---|---|
| [isRunning](is-running.md) | [common]<br/>fun [isRunning](is-running.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if playback is currently running. |
| [isStarted](is-started.md) | [common]<br/>fun [isStarted](is-started.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if playback has been started (position 0). |
| [registerEventListener](register-event-listener.md) | [common]<br/>fun [registerEventListener](register-event-listener.md)(listener: [MidiEventListener](../-midi-event-listener/index.md), eventClass: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.reflect/-k-class/index.html)&lt;out [MidiEvent](../-midi-event/index.md)&gt;)<br/>Register a listener for a specific event type. |
| [release](release.md) | [common]<br/>fun [release](release.md)()<br/>Release resources. |
| [reset](reset.md) | [common]<br/>fun [reset](reset.md)()<br/>Reset playback to the beginning. |
| [start](start.md) | [common]<br/>fun [start](start.md)()<br/>Start MIDI playback. |
| [stop](stop.md) | [common]<br/>fun [stop](stop.md)()<br/>Stop MIDI playback. |
| [unregisterAllEventListeners](unregister-all-event-listeners.md) | [common]<br/>fun [unregisterAllEventListeners](unregister-all-event-listeners.md)()<br/>Unregister all listeners. |
| [unregisterEventListener](unregister-event-listener.md) | [common]<br/>fun [unregisterEventListener](unregister-event-listener.md)(listener: [MidiEventListener](../-midi-event-listener/index.md))<br/>Unregister a listener from all event types.<br/>[common]<br/>fun [unregisterEventListener](unregister-event-listener.md)(listener: [MidiEventListener](../-midi-event-listener/index.md), eventClass: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.reflect/-k-class/index.html)&lt;out [MidiEvent](../-midi-event/index.md)&gt;)<br/>Unregister a listener from a specific event type. |
