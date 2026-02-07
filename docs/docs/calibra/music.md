---
sidebar_position: 8
---

# CalibraMusic

Music theory utilities for converting between pitch representations: Hz, MIDI note numbers, note labels, and cents.

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

## Constants

| Constant | Kotlin | Swift | Type | Value | Description |
|----------|--------|-------|------|-------|-------------|
| A4 frequency | `CalibraMusic.A4_FREQUENCY` | `CalibraMusic.a4Frequency` | `Float` | `440.0` | Reference pitch for A4 (Hz) |
| A4 MIDI number | `CalibraMusic.A4_MIDI_NUMBER` | `CalibraMusic.a4MidiNumber` | `Int` / `Int32` | `69` | MIDI note number for A4 |

## Pitch Reference

| Note | MIDI | Hz |
|------|------|-----|
| C4 (middle C) | 60 | 261.63 |
| A4 (concert pitch) | 69 | 440.00 |
| C5 | 72 | 523.25 |

## Hz ↔MIDI

### `hzToMidi`

Convert frequency in Hz to MIDI note number.

#### Kotlin

```kotlin
val midi = CalibraMusic.hzToMidi(frequency = 440f)  // 69.0
```

#### Swift

```swift
let midi = CalibraMusic.hzToMidi(440)  // 69.0
```

| Parameter | Type | Description |
|-----------|------|-------------|
| `frequency` | `Float` | Frequency in Hz |

**Returns:** `Float` -- MIDI note number (fractional), or `Float.NaN` if `frequency <= 0`.

---

### `midiToHz`

Convert MIDI note number to frequency in Hz.

#### Kotlin

```kotlin
val hz = CalibraMusic.midiToHz(midiNote = 60f)  // 261.63 Hz
```

#### Swift

```swift
let hz = CalibraMusic.midiToHz(60)  // 261.63 Hz
```

| Parameter | Type | Description |
|-----------|------|-------------|
| `midiNote` | `Float` | MIDI note number (can be fractional) |

**Returns:** `Float` -- Frequency in Hz, or `Float.NaN` if `midiNote` is NaN.

## MIDI ↔Note Label

### `midiToNoteLabel`

Convert MIDI note number to note label (e.g., "A4", "C#5").

#### Kotlin

```kotlin
val label = CalibraMusic.midiToNoteLabel(midiNote = 69f)  // "A4"
```

#### Swift

```swift
let label = CalibraMusic.midiToNoteLabel(69)  // "A4"
```

| Parameter | Type | Description |
|-----------|------|-------------|
| `midiNote` | `Float` | MIDI note number (rounded to nearest integer) |

**Returns:** `String` -- Note label such as `"A4"` or `"C#5"`, or `"-"` if invalid.

---

### `noteLabelToMidi`

Convert note label to MIDI note number. Supports sharps (`#`) and flats (`b`).

#### Kotlin

```kotlin
val midi = CalibraMusic.noteLabelToMidi(noteLabel = "C4")   // 60.0
val midi2 = CalibraMusic.noteLabelToMidi(noteLabel = "Bb3") // 58.0
```

#### Swift

```swift
let midi = CalibraMusic.noteLabelToMidi("C4")   // 60.0
let midi2 = CalibraMusic.noteLabelToMidi("Bb3") // 58.0
```

| Parameter | Type | Description |
|-----------|------|-------------|
| `noteLabel` | `String` | Note label (e.g., `"A4"`, `"C#5"`, `"Db3"`) |

**Returns:** `Float` -- MIDI note number, or `Float.NaN` if the label cannot be parsed.

## Hz ↔Note Label

### `hzToNoteLabel`

Convert frequency in Hz to note label.

#### Kotlin

```kotlin
val label = CalibraMusic.hzToNoteLabel(frequency = 440f)  // "A4"
```

#### Swift

```swift
let label = CalibraMusic.hzToNoteLabel(440)  // "A4"
```

| Parameter | Type | Description |
|-----------|------|-------------|
| `frequency` | `Float` | Frequency in Hz |

**Returns:** `String` -- Note label such as `"A4"`, or `"-"` if invalid.

---

### `noteLabelToHz`

Convert note label to frequency in Hz.

#### Kotlin

```kotlin
val hz = CalibraMusic.noteLabelToHz(noteLabel = "C4")  // 261.63 Hz
```

#### Swift

```swift
let hz = CalibraMusic.noteLabelToHz("C4")  // 261.63 Hz
```

| Parameter | Type | Description |
|-----------|------|-------------|
| `noteLabel` | `String` | Note label (e.g., `"A4"`, `"C#5"`) |

**Returns:** `Float` -- Frequency in Hz, or `Float.NaN` if the label cannot be parsed.

## Hz ↔Cents

### `hzToCents`

Convert frequency in Hz to cents (relative to tonic).

#### Kotlin

```kotlin
val cents = CalibraMusic.hzToCents(frequency = 880f)                     // 1200.0
val cents2 = CalibraMusic.hzToCents(frequency = 880f, tonicHz = 440f)    // 1200.0
```

#### Swift

```swift
let cents = CalibraMusic.hzToCents(880)                    // 1200.0
let cents2 = CalibraMusic.hzToCents(880, tonicHz: 440)     // 1200.0
```

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `frequency` | `Float` | -- | Frequency in Hz |
| `tonicHz` | `Float` | `440.0` (A4) | Reference tonic frequency |

**Returns:** `Float` -- Cents value, or `Float.NaN` if `frequency <= 0` or `tonicHz <= 0`.

---

### `centsToHz`

Convert cents to frequency in Hz (relative to tonic).

#### Kotlin

```kotlin
val hz = CalibraMusic.centsToHz(cents = 1200f)                     // 880.0
val hz2 = CalibraMusic.centsToHz(cents = 1200f, tonicHz = 440f)    // 880.0
```

#### Swift

```swift
let hz = CalibraMusic.centsToHz(1200)                    // 880.0
let hz2 = CalibraMusic.centsToHz(1200, tonicHz: 440)     // 880.0
```

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `cents` | `Float` | -- | Cents value |
| `tonicHz` | `Float` | `440.0` (A4) | Reference tonic frequency |

**Returns:** `Float` -- Frequency in Hz, or `Float.NaN` if `cents` is NaN or `tonicHz <= 0`.

## MIDI ↔Cents

### `centsToMidi`

Convert cents to MIDI note number.

#### Kotlin

```kotlin
val midi = CalibraMusic.centsToMidi(cents = 100f)                          // 70.0
val midi2 = CalibraMusic.centsToMidi(cents = 100f, referenceMidi = 60)     // 61.0
```

#### Swift

```swift
let midi = CalibraMusic.centsToMidi(100)                          // 70.0
let midi2 = CalibraMusic.centsToMidi(100, referenceMidi: 60)      // 61.0
```

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `cents` | `Float` | -- | Cents value |
| `referenceMidi` | `Int` / `Int32` | `69` (A4) | Reference MIDI note number |

**Returns:** `Float` -- MIDI note number, or `Float.NaN` if `cents` is NaN.

---

### `midiToCents`

Convert MIDI note number to cents.

#### Kotlin

```kotlin
val cents = CalibraMusic.midiToCents(midiNote = 70f)                          // 100.0
val cents2 = CalibraMusic.midiToCents(midiNote = 61f, referenceMidi = 60)     // 100.0
```

#### Swift

```swift
let cents = CalibraMusic.midiToCents(70)                          // 100.0
let cents2 = CalibraMusic.midiToCents(61, referenceMidi: 60)      // 100.0
```

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `midiNote` | `Float` | -- | MIDI note number |
| `referenceMidi` | `Int` / `Int32` | `69` (A4) | Reference MIDI note number |

**Returns:** `Float` -- Cents value, or `Float.NaN` if `midiNote` is NaN.

## Note Label ↔Cents

### `noteLabelToCents`

Convert note label to cents (relative to tonic).

#### Kotlin

```kotlin
val cents = CalibraMusic.noteLabelToCents(noteLabel = "A4", tonicHz = 440f)  // 0.0
val cents2 = CalibraMusic.noteLabelToCents(noteLabel = "A5", tonicHz = 440f) // 1200.0
```

#### Swift

```swift
let cents = CalibraMusic.noteLabelToCents("A4", tonicHz: 440)  // 0.0
let cents2 = CalibraMusic.noteLabelToCents("A5", tonicHz: 440) // 1200.0
```

| Parameter | Type | Description |
|-----------|------|-------------|
| `noteLabel` | `String` | Note label (e.g., `"C4"`, `"G#5"`) |
| `tonicHz` | `Float` | Reference tonic frequency in Hz |

**Returns:** `Float` -- Cents value, or `Float.NaN` if the label is invalid.

---

### `centsToNoteLabel`

Convert cents to note label (relative to tonic).

#### Kotlin

```kotlin
val label = CalibraMusic.centsToNoteLabel(cents = 1200f, tonicHz = 440f)  // "A5"
```

#### Swift

```swift
let label = CalibraMusic.centsToNoteLabel(1200, tonicHz: 440)  // "A5"
```

| Parameter | Type | Description |
|-----------|------|-------------|
| `cents` | `Float` | Cents value |
| `tonicHz` | `Float` | Reference tonic frequency in Hz |

**Returns:** `String` -- Note label such as `"A5"`, or `"-"` if invalid.

## Convenience

### `centsDeviation`

Get cents deviation from the nearest note. The result is in the range -50 to +50, where positive means sharp and negative means flat.

#### Kotlin

```kotlin
val deviation = CalibraMusic.centsDeviation(frequency = 442f)  // ~+7.85 (sharp)
val deviation2 = CalibraMusic.centsDeviation(frequency = 438f) // ~-7.89 (flat)
```

#### Swift

```swift
let deviation = CalibraMusic.centsDeviation(442)  // ~+7.85 (sharp)
let deviation2 = CalibraMusic.centsDeviation(438) // ~-7.89 (flat)
```

| Parameter | Type | Description |
|-----------|------|-------------|
| `frequency` | `Float` | Frequency in Hz |

**Returns:** `Float` -- Cents deviation from nearest semitone (-50 to +50). Returns `0` if `frequency <= 0`.

## Error Handling

CalibraMusic uses sentinel return values rather than exceptions:

| Condition | Return Value |
|-----------|-------------|
| `frequency <= 0` (for Hz-based methods) | `Float.NaN` or `"-"` |
| `midiNote` is NaN | `Float.NaN` or `"-"` |
| Invalid note label | `Float.NaN` |
| `centsDeviation` with `frequency <= 0` | `0f` |

```kotlin
val result = CalibraMusic.hzToMidi(-1f)
if (result.isNaN()) {
    // Handle invalid input
}

val label = CalibraMusic.hzToNoteLabel(-1f)
if (label == "-") {
    // Handle invalid input
}
```

## Common Patterns

### Tuner Display

```kotlin
fun tunerDisplay(frequency: Float): String {
    val noteLabel = CalibraMusic.hzToNoteLabel(frequency)
    val deviation = CalibraMusic.centsDeviation(frequency)
    val direction = when {
        deviation > 2f -> "sharp"
        deviation < -2f -> "flat"
        else -> "in tune"
    }
    return "$noteLabel: ${"%.1f".format(deviation)} cents ($direction)"
}

// tunerDisplay(442f) -> "A4: 7.9 cents (sharp)"
```

### Pitch-to-Note Conversion Pipeline

```kotlin
// Full conversion chain: Hz -> MIDI -> label -> back to Hz
val freq = 440f
val midi = CalibraMusic.hzToMidi(freq)           // 69.0
val label = CalibraMusic.midiToNoteLabel(midi)    // "A4"
val roundTrip = CalibraMusic.noteLabelToHz(label) // 440.0
```

### Interval Calculation in Cents

```kotlin
// Calculate interval between two notes in cents
val noteA = CalibraMusic.noteLabelToHz("C4")
val noteB = CalibraMusic.noteLabelToHz("G4")
val interval = CalibraMusic.hzToCents(noteB, tonicHz = noteA)  // ~702 cents (perfect fifth)
```

## Common Pitfalls

1. **Invalid frequencies**: Methods return `Float.NaN` or `"-"` for invalid input -- always check return values.
2. **Flat notation**: Use lowercase `b` for flats (e.g., `"Bb4"`, not `"BB4"`).
3. **Octave numbering**: Middle C is C4 (scientific pitch notation).
4. **Cents interpretation**: Positive = sharp, negative = flat.

## Next Steps

- [CalibraPitch](./pitch) -- Real-time pitch detection
- [CalibraVAD](./vad) -- Voice activity detection
- [CalibraVocalRange](./vocal-range) -- Vocal range analysis
