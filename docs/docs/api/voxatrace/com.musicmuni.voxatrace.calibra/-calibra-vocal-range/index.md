---
sidebar_label: "CalibraVocalRange"
---


# CalibraVocalRange

class [CalibraVocalRange](index.md)

Streaming vocal range analyzer for determining a singer's pitch range.

## What is Vocal Range?

Vocal range is the span between a singer's lowest and highest notes. This analyzer detects the range using **pedagogically-valid criteria** based on ASHA 2018 guidelines and voice science research.

Use it for:

- 
   **Onboarding**: Determine user's comfortable singing range
- 
   **Song selection**: Match songs to user's range
- 
   **Progress tracking**: See if range expands over time
- 
   **Key transposition**: Calculate optimal key for user

## When to Use

| Scenario | Use This? | Why |
|---|---|---|
| Find singer's range | Yes | Core use case |
| Guided range finding | Yes | `getStableNote()` for step-by-step |
| Real-time pitch display | No | Use `CalibraPitch` |
| Score singing | No | Use `CalibraLiveEval` |

## Quick Start

### Kotlin

```kotlin
val analyzer = CalibraVocalRange.create()

// Feed audio from recorder
recorder.audioBuffers.collect { buffer ->
    analyzer.addAudio(buffer.toFloatArray())
}

// Get detected range
val range = analyzer.getRange()
if (range != null) {
    println("Range: ${range.lower.noteLabel} to ${range.upper.noteLabel}")
    println("Octaves: ${range.octaves}")
}

analyzer.release()
```

### Swift

```swift
let analyzer = CalibraVocalRange.create()

// Feed audio from recorder
for await buffer in recorder.audioBuffersStream() {
    analyzer.addAudio(samples: buffer.toFloatArray())
}

// Get detected range
if let range = analyzer.getRange() {
    print("Range: \(range.lower.noteLabel) to \(range.upper.noteLabel)")
    print("Octaves: \(range.octaves)")
}

analyzer.release()
```

## Guided Range Detection

For step-by-step range finding (e.g., &quot;sing your lowest note&quot;):

### Kotlin

```kotlin
val analyzer = CalibraVocalRange.create()

// Phase 1: Detect lowest note
showPrompt("Sing your lowest comfortable note")
// ... user sings, feed audio via addAudio() ...
val lowestNote = analyzer.getStableNote()
analyzer.reset()  // Reset for next phase

// Phase 2: Detect highest note
showPrompt("Sing your highest comfortable note")
// ... user sings, feed audio via addAudio() ...
val highestNote = analyzer.getStableNote()

// Calculate range
val rangeOctaves = (highestNote.pitch.midiNote - lowestNote.pitch.midiNote) / 12f

analyzer.release()
```

## Detection Criteria (ASHA 2018)

The analyzer uses these criteria for pedagogical validity:

- 
   **Minimum 1 second** sustained pitch for note inclusion
- 
   **Confidence threshold** of 0.5 minimum
- 
   **Stability**: pitch deviation < 1 semitone within 50ms
- 
   **Percentile-based** range extraction (5th-95th percentile)

## Platform Notes

### iOS

- 
   Audio is typically 48kHz from microphone; internal resampling handles this
- 
   Uses YIN algorithm for pitch detection (no external dependencies)

### Android

- 
   Audio varies by device (44.1kHz, 48kHz, 16kHz)
- 
   Uses YIN algorithm for pitch detection (pure Kotlin)

## Common Pitfalls

1. 
   **Forgetting to release**: Call `analyzer.release()` to free resources
2. 
   **Not resetting between phases**: Call `reset()` when starting new detection
3. 
   **Not enough sustained notes**: User must hold notes for 1+ second
4. 
   **Background noise**: High noise can trigger false detections

#### See also

| | |
|---|---|
| [VocalRange](../-vocal-range/index.md) | For the result structure |
| [VocalRangeConfig](../-vocal-range-config/index.md) | For customizing detection parameters |
| [CalibraPitch](../-calibra-pitch/index.md) | For general pitch detection |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [addAudio](add-audio.md) | [common]<br/>fun [addAudio](add-audio.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html))<br/>Add audio samples for analysis. |
| [getLowerLimit](get-lower-limit.md) | [common]<br/>fun [getLowerLimit](get-lower-limit.md)(): [VocalPitch](../-vocal-pitch/index.md)?<br/>Get lower limit from accumulated data (5th percentile of stable pitches). |
| [getRange](get-range.md) | [common]<br/>fun [getRange](get-range.md)(): [VocalRange](../-vocal-range/index.md)?<br/>Get complete range analysis. |
| [getStableNote](get-stable-note.md) | [common]<br/>fun [getStableNote](get-stable-note.md)(): [DetectedNote](../-detected-note/index.md)?<br/>Get the stable note detected so far. |
| [getStats](get-stats.md) | [common]<br/>fun [getStats](get-stats.md)(): [RangeStats](../-range-stats/index.md)<br/>Get statistics about accumulated data. |
| [getUpperLimit](get-upper-limit.md) | [common]<br/>fun [getUpperLimit](get-upper-limit.md)(): [VocalPitch](../-vocal-pitch/index.md)?<br/>Get upper limit from accumulated data (95th percentile of stable pitches). |
| [release](release.md) | [common]<br/>fun [release](release.md)()<br/>Release resources. |
| [reset](reset.md) | [common]<br/>fun [reset](reset.md)()<br/>Reset for new detection session. |
