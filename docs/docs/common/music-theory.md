---
sidebar_position: 2
---

# MusicTheory

Pitch ↔ MIDI ↔ note-label ↔ cents conversions, interval generators (12-TET and Just Intonation), and shruti-alignment helpers.

## Quick Start

### Kotlin

```kotlin
val midi = MusicTheory.hzToMidi(440f)              // 69.0
val hz = MusicTheory.midiToHz(69f)                 // 440.0
val label = MusicTheory.hzToNoteLabel(440f)        // "A4"
val cents = MusicTheory.hzToCents(466.16f, tonicHz = 440f)  // ~100

val intervals = MusicTheory.EQ_TEMPERED_INTERVALS_CENTS_BASE  // [0, 100, …, 1100]
```

### Swift

```swift
let midi = MusicTheory.hzToMidi(440.0)             // 69.0
let label = MusicTheory.hzToNoteLabel(440.0)       // "A4"
let names = MusicTheory.noteNames                  // ["C", "C#", "D", …]
```

## Constants

| Constant | Type | Value / Description |
|----------|------|---------------------|
| `A4_FREQUENCY` | `Float` | `440.0` |
| `A4_MIDI_NUMBER` | `Int` | `69` |
| `SEMITONES_IN_OCTAVE` | `Int` | `12` |
| `CENTS_IN_OCTAVE` | `Float` | `1200.0` |
| `CENTS_IN_SEMITONE` | `Float` | `100.0` |
| `NOTE_NAMES` | `List<String>` | Western chromatic names with sharps |
| `NOTE_TO_MIDI_BASE` | `Map<String, Int>` | Both sharps and flats accepted as keys |
| `NOTE_NAMES_HINDUSTANI` | `List<String>` | 12-pitch-class Hindustani sargam |
| `NOTE_NAMES_CARNATIC` | `List<String>` | 12-pitch-class Carnatic swarasthana |
| `JUST_INTONATION_RATIOS` | `Map<String, Pair<Int, Int>>` | 5-limit JI ratios per note name |
| `DEFAULT_MIN_CENTS` | `Int` | `-2400` |
| `DEFAULT_MAX_CENTS` | `Int` | `3600` |
| `EQ_TEMPERED_INTERVALS_CENTS_BASE` | `List<Int>` | `[0, 100, …, 1100]` |
| `EQ_TEMPERED_INTERVALS_CENTS` | `List<Int>` | 12-TET across `[DEFAULT_MIN_CENTS, DEFAULT_MAX_CENTS]` |
| `JUST_INTONATION_INTERVALS_CENTS_BASE` | `List<Float>` | JI within one octave |
| `JUST_INTONATION_INTERVALS_CENTS` | `List<Float>` | JI across the default range |

## Conversions

| Method | Description |
|--------|-------------|
| `hzToMidi(frequency: Float): Float` | Hz → MIDI number |
| `midiToHz(midiNote: Float): Float` | MIDI → Hz |
| `midiToNoteLabel(midiNote: Float): String` | MIDI → e.g. `"A4"`, `"C#5"` (sharps only) |
| `noteLabelToMidi(noteLabel: String): Float` | Note label → MIDI (accepts sharps and flats) |
| `hzToNoteLabel(frequency: Float): String` | Hz → e.g. `"A4"` |
| `noteLabelToHz(noteLabel: String): Float` | Note label → Hz |
| `hzToCents(frequency, tonicHz = A4_FREQUENCY): Float` | Hz → cents relative to tonic |
| `centsToHz(cents, tonicHz = A4_FREQUENCY): Float` | Cents → Hz |
| `centsToMidi(cents, referenceMidi = A4_MIDI_NUMBER): Float` | Cents → MIDI |
| `midiToCents(midiNote, referenceMidi = A4_MIDI_NUMBER): Float` | MIDI → cents |
| `noteLabelToCents(noteLabel, tonicHz: Float): Float` | Note label → cents relative to tonic |
| `centsToNoteLabel(cents, tonicHz: Float): String` | Cents → note label |
| `centsDeviation(frequency: Float): Float` | Cents from nearest 12-TET note (`-50`…`+50`) |

## Interval generators

```kotlin
fun ratioToCents(numerator: Int, denominator: Int): Float
fun generateEqTemperedIntervals(minCents: Int, maxCents: Int, step: Int = 100): List<Int>
fun generateJustIntonationIntervals(
    baseRatios: Map<String, Pair<Int, Int>>,
    minCents: Float,
    maxCents: Float,
): List<Float>
```

## alignShruti

```kotlin
fun alignShruti(
    userShrutiHz: Float,
    refKeyHz: Float,
    maxFineTuneSemitones: Float = 2f,
): ShrutiAlignmentResult
```

Computes the practice shruti for a student given their natural shruti and a reference lesson key, working in **pitch-class space (mod 12 semitones)** so octave differences resolve implicitly. Caps the shift at ±`maxFineTuneSemitones`.

```kotlin
val result = MusicTheory.alignShruti(userShrutiHz = 220f, refKeyHz = 261.63f)
liveEval.setStudentKeyHz(result.practiceShrutiHz)
```

### ShrutiAlignmentResult

```kotlin
data class ShrutiAlignmentResult(
    val practiceShrutiHz: Float,
    val shiftSemitones: Float,                  // capped at ±maxFineTuneSemitones
    val options: List<ShrutiOption> = emptyList(),
)
```

### ShrutiOption

`options` lists every viable shruti chip the student can pick (size = `2 * round(maxFineTuneSemitones) + 1`, default 5). Each chip is anchored at the octave nearest to the student's natural shruti.

```kotlin
data class ShrutiOption(
    val hz: Float,
    val pitchClassIndex: Int,         // 0..11 (0 = C)
    val pitchClassName: String,       // e.g., "C", "C#"
    val noteLabel: String,            // e.g., "C4", "G#3"
    val shiftSemitones: Float,        // pass to SonixPlayerConfig.Builder().pitch(...)
)
```

Pass `practiceShrutiHz` to `CalibraLiveEval.setStudentKeyHz(...)` and use `shiftSemitones` to transpose lesson audio with `SonixPlayerConfig.Builder().pitch(shiftSemitones)`.

## deriveUserShruti

```kotlin
fun deriveUserShruti(
    nspHz: Float?,
    rangeLowHz: Float? = null,
    rangeHighHz: Float? = null,
    rangeThresholdSemitones: Float = 18f,
): UserShrutiDerivation
```

Computes a user's practice shruti from their natural speaking pitch (NSP) and most-recent vocal range, applying the policy from Musicmuni research synthesis (§B7).

- If the vocal-range span is below `rangeThresholdSemitones` (default 18 st), the formula isn't reliable; the derivation falls back to NSP and reports `NSP_NARROW_RANGE`.
- If no vocal range is available at all, falls back to NSP with `NSP_NO_RANGE`.
- If span ≥ threshold, returns the formula output: `Sa = max(rangeLow + 7, nspMidi − 2)` clipped to `[rangeHigh − 17, rangeHigh − 12]`, with source `VOCAL_RANGE`.

If `nspHz` is null but a wide vocal range is available, the formula degrades to `Sa = (rangeLow + 7)` clipped to the same band. If both are null, returns `0 Hz` — caller must guard.

### UserShrutiDerivation

```kotlin
data class UserShrutiDerivation(
    val targetHz: Float,
    val source: Source,
)

enum class UserShrutiDerivation.Source {
    NSP_NO_RANGE,        // no vocal range data yet — first NSP wins
    NSP_NARROW_RANGE,    // range exists but span too small for derivation
    VOCAL_RANGE,         // wide enough range, formula applied
}
```

```kotlin
val nsp = TesseraSpeakingPitch.detectFromAudio(speech).takeIf { it > 0 }
val range = TesseraRange.computeVocalRange(contour)?.range
val derivation = MusicTheory.deriveUserShruti(
    nspHz = nsp,
    rangeLowHz = range?.lower?.frequencyHz,
    rangeHighHz = range?.upper?.frequencyHz,
)
println("Practice shruti: ${derivation.targetHz} Hz (${derivation.source})")
```

## Common Pitfalls

1. **`hzToCents` default tonic is A4 (440 Hz).** When working relative to the singer's shruti, pass `tonicHz` explicitly.
2. **Note labels use sharps only.** `midiToNoteLabel` returns `"C#4"`, never `"Db4"`. `noteLabelToMidi` accepts both.
3. **`centsDeviation` measures from the nearest 12-TET note**, not from a custom tonic.
4. **`deriveUserShruti` returns `0 Hz` if both `nspHz` and the range are null.** Caller must guard before using.

## See also

- [Accura](../accura/intonation) — uses MusicTheory for intonation analysis
- [PitchAnalysis](../tona/pitch-analysis) — uses MusicTheory for histogram computation
- [TesseraRangeSession](../tessera/range#tesserarangesession) — alternative shruti calculation purely from range
- [TesseraSpeakingPitch](../tessera/speaking-pitch) — produces the NSP input
