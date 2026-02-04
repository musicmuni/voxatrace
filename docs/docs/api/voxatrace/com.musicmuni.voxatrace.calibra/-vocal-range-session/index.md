---
sidebar_label: "VocalRangeSession"
---


# VocalRangeSession

[common]\
class [VocalRangeSession](index.md)

High-level vocal range detection session with observable state.

This class provides a simplified API that:

- 
   Emits state changes via StateFlow for reactive UI
- 
   Runs auto-flow by default (countdown -> low -> high -> done)
- 
   Supports manual phase control for custom flows

## Auto-Flow Usage (Recommended)

```kotlin
val session = VocalRangeSession.create()

// Observe state in UI
session.state.collect { state ->
    updateUI(state)
}

// Start detection - runs full flow automatically
session.start()

// Feed audio from your recorder (resample to 16kHz)
recorder.audioBuffers.collect { buffer ->
    val samples16k = SonixResampler.resample(buffer.floatSamples, hwRate, 16000)
    session.addAudio(samples16k)
}

// Cleanup
session.release()
```

## Manual Flow Control

```kotlin
val session = VocalRangeSession.create(VocalRangeSessionConfig(autoFlow = false))

session.startPhase(VocalRangePhase.DETECTING_LOW)
// ... feed audio ...
session.advancePhase()  // Move to DETECTING_HIGH
// ... feed audio ...
session.complete()
```

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [state](state.md) | [common]<br/>val [state](state.md): StateFlow&lt;[VocalRangeState](../-vocal-range-state/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [addAudio](add-audio.md) | [common]<br/>fun [addAudio](add-audio.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000)<br/>Add audio samples for processing. |
| [advancePhase](advance-phase.md) | [common]<br/>fun [advancePhase](advance-phase.md)()<br/>Manually advance to the next phase. Captures the current stable note for the current phase. |
| [cancel](cancel.md) | [common]<br/>fun [cancel](cancel.md)()<br/>Cancel the current session. |
| [complete](complete.md) | [common]<br/>fun [complete](complete.md)()<br/>Complete detection and calculate final result. |
| [confirmNote](confirm-note.md) | [common]<br/>fun [confirmNote](confirm-note.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Lock the current best note and advance to next phase. |
| [release](release.md) | [common]<br/>fun [release](release.md)()<br/>Release all resources. Must be called when done. |
| [reset](reset.md) | [common]<br/>fun [reset](reset.md)()<br/>Reset to start a new session. |
| [start](start.md) | [common]<br/>fun [start](start.md)()<br/>Start the detection session (auto-flow mode). |
| [startPhase](start-phase.md) | [common]<br/>fun [startPhase](start-phase.md)(phase: [VocalRangePhase](../-vocal-range-phase/index.md))<br/>Start a specific phase (manual mode). Use when autoFlow = false for custom flows. |
