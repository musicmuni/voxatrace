//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.midi](../index.md)/[MidiEventListener](index.md)

# MidiEventListener

[common]\
interface [MidiEventListener](index.md)

Listener interface for MIDI playback events from MidiProcessor.

Implement this interface to receive callbacks during MIDI playback, including individual MIDI events, metronome ticks, and playback state changes.

Usage:

```kotlin
val processor = MidiProcessor(midiFile)

processor.registerEventListener(object : MidiEventListener {
    override fun onStart(fromBeginning: Boolean) {
        println("Playback started")
    }

    override fun onEvent(event: MidiEvent, ms: Long) {
        when (event) {
            is NoteOn -> playNote(event.note, event.velocity)
            is MetronomeTick -> updateBeatDisplay(event.beatNumber)
        }
    }

    override fun onStop(finished: Boolean) {
        println("Playback stopped, finished=$finished")
    }
})

processor.start()
```

## Functions

| Name | Summary |
|---|---|
| [onEvent](on-event.md) | [common]<br/>abstract fun [onEvent](on-event.md)(event: [MidiEvent](../-midi-event/index.md), ms: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Called when a MIDI event occurs during playback. |
| [onStart](on-start.md) | [common]<br/>abstract fun [onStart](on-start.md)(fromBeginning: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html))<br/>Called when MIDI playback starts. |
| [onStop](on-stop.md) | [common]<br/>abstract fun [onStop](on-stop.md)(finished: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html))<br/>Called when MIDI playback stops. |
