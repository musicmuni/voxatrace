---
sidebar_label: "CalibraMusic"
---


# CalibraMusic

object [CalibraMusic](index.md)

Music theory utilities for pitch and note conversions.

## What is CalibraMusic?

CalibraMusic provides **conversion functions** between different musical pitch representations:

- 
   **Hz (Hertz)**: Physical frequency (e.g., 440.0 Hz)
- 
   **MIDI note numbers**: Integer scale (60 = middle C, 69 = A4)
- 
   **Note labels**: Human-readable (e.g., &quot;C4&quot;, &quot;A#5&quot;, &quot;Bb3&quot;)
- 
   **Cents**: Fine pitch deviation (100 cents = 1 semitone)

Use it when you need to:

- 
   Display detected pitch as a note name
- 
   Calculate pitch deviation from a target note
- 
   Convert between different pitch units for analysis

## When to Use

| Scenario | Use This? | Why |
|---|---|---|
| Convert Hz to note name | Yes | `hzToNoteLabel(440f)` → &quot;A4&quot; |
| Show pitch deviation | Yes | `centsDeviation(442f)` → +7.8 cents |
| Create note frequencies | Yes | `noteLabelToHz("C4")` → 261.63 Hz |
| Detect pitch from audio | No | Use `CalibraPitch` |

## Quick Start

### Kotlin

```kotlin
// Convert frequency to note name
val noteLabel = CalibraMusic.hzToNoteLabel(440f)  // "A4"

// Convert note name to frequency
val frequency = CalibraMusic.noteLabelToHz("C4")  // 261.63 Hz

// Get cents deviation from nearest note
val deviation = CalibraMusic.centsDeviation(442f)  // +7.8 cents (sharp)

// Convert between MIDI and Hz
val midi = CalibraMusic.hzToMidi(440f)  // 69.0
val hz = CalibraMusic.midiToHz(60f)     // 261.63 Hz (middle C)
```

### Swift

```swift
// Convert frequency to note name
let noteLabel = CalibraMusic.hzToNoteLabel(440)  // "A4"

// Convert note name to frequency
let frequency = CalibraMusic.noteLabelToHz("C4")  // 261.63 Hz

// Get cents deviation from nearest note
let deviation = CalibraMusic.centsDeviation(442)  // +7.8 cents

// Convert between MIDI and Hz
let midi = CalibraMusic.hzToMidi(440)  // 69.0
let hz = CalibraMusic.midiToHz(60)      // 261.63 Hz
```

## Pitch Reference

| Note | MIDI | Hz |
|---|---|---|
| C4 (middle C) | 60 | 261.63 |
| A4 (concert pitch) | 69 | 440.00 |
| C5 | 72 | 523.25 |

## Common Pitfalls

1. 
   **Invalid frequencies**: Functions return `Float.NaN` or &quot;-&quot; for invalid input
2. 
   **Flat notation**: Use lowercase 'b' for flats (e.g., &quot;Bb4&quot;, not &quot;BB4&quot;)
3. 
   **Octave numbering**: Middle C is C4 (scientific pitch notation)
4. 
   **Cents interpretation**: Positive = sharp, negative = flat

#### See also

| | |
|---|---|
| [CalibraPitch](../-calibra-pitch/index.md) | For detecting pitch from audio |
| [CalibraLiveEval](../-calibra-live-eval/index.md) | For scoring pitch accuracy |

## Properties

| Name | Summary |
|---|---|
| [A4_FREQUENCY](-a4_-f-r-e-q-u-e-n-c-y.md) | [common]<br/>const val [A4_FREQUENCY](-a4_-f-r-e-q-u-e-n-c-y.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 440.0f<br/>Reference pitch for A4 (Hz) |
| [A4_MIDI_NUMBER](-a4_-m-i-d-i_-n-u-m-b-e-r.md) | [common]<br/>const val [A4_MIDI_NUMBER](-a4_-m-i-d-i_-n-u-m-b-e-r.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 69<br/>MIDI note number for A4 |

## Functions

| Name | Summary |
|---|---|
| [centsDeviation](cents-deviation.md) | [common]<br/>fun [centsDeviation](cents-deviation.md)(frequency: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Get cents deviation from nearest note (-50 to +50). |
| [centsToHz](cents-to-hz.md) | [common]<br/>fun [centsToHz](cents-to-hz.md)(cents: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), tonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = A4_FREQUENCY): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Convert cents to frequency in Hz (relative to tonic). |
| [centsToMidi](cents-to-midi.md) | [common]<br/>fun [centsToMidi](cents-to-midi.md)(cents: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), referenceMidi: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = A4_MIDI_NUMBER): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Convert cents to MIDI note number. |
| [centsToNoteLabel](cents-to-note-label.md) | [common]<br/>fun [centsToNoteLabel](cents-to-note-label.md)(cents: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), tonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Convert cents to note label (relative to tonic). |
| [hzToCents](hz-to-cents.md) | [common]<br/>fun [hzToCents](hz-to-cents.md)(frequency: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), tonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = A4_FREQUENCY): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Convert frequency in Hz to cents (relative to tonic). |
| [hzToMidi](hz-to-midi.md) | [common]<br/>fun [hzToMidi](hz-to-midi.md)(frequency: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Convert frequency in Hz to MIDI note number. |
| [hzToNoteLabel](hz-to-note-label.md) | [common]<br/>fun [hzToNoteLabel](hz-to-note-label.md)(frequency: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Convert frequency in Hz to note label. |
| [midiToCents](midi-to-cents.md) | [common]<br/>fun [midiToCents](midi-to-cents.md)(midiNote: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), referenceMidi: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = A4_MIDI_NUMBER): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Convert MIDI note number to cents. |
| [midiToHz](midi-to-hz.md) | [common]<br/>fun [midiToHz](midi-to-hz.md)(midiNote: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Convert MIDI note number to frequency in Hz. |
| [midiToNoteLabel](midi-to-note-label.md) | [common]<br/>fun [midiToNoteLabel](midi-to-note-label.md)(midiNote: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Convert MIDI note number to note label (e.g., &quot;A4&quot;, &quot;C#5&quot;). |
| [noteLabelToCents](note-label-to-cents.md) | [common]<br/>fun [noteLabelToCents](note-label-to-cents.md)(noteLabel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), tonicHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Convert note label to cents (relative to tonic). |
| [noteLabelToHz](note-label-to-hz.md) | [common]<br/>fun [noteLabelToHz](note-label-to-hz.md)(noteLabel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Convert note label to frequency in Hz. |
| [noteLabelToMidi](note-label-to-midi.md) | [common]<br/>fun [noteLabelToMidi](note-label-to-midi.md)(noteLabel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Convert note label to MIDI note number. Supports sharps (#), flats (b). |
