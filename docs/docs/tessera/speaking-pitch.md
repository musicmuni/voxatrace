---
sidebar_position: 6
---

# TesseraSpeakingPitch

Detects a speaker's natural speaking pitch (the median F0 of voiced frames in conversational speech). Useful for voice profiling and shruti suggestion.

## Quick Start

### Kotlin

```kotlin
val hz = TesseraSpeakingPitch.detectFromAudio(audioSamples, sampleRate = 16000)
if (hz > 0) println("Speaking pitch: $hz Hz")
```

### Swift

```swift
let hz = TesseraSpeakingPitch.detectFromAudio(audioMono: samples, sampleRate: 16000)
if hz > 0 { print("Speaking pitch: \(hz) Hz") }
```

## Methods

| Method | Description |
|--------|-------------|
| `detect(contour: PitchContour): Float` | Median F0 from a pre-extracted contour. Throws `IllegalArgumentException` if the contour is empty (per ADR-022). |
| `detectFromAudio(audioMono, sampleRate = 16000): Float` | One-shot from raw audio. Resamples to 16 kHz internally (per ADR-017). Throws if `audioMono` is empty or `sampleRate <= 0`. |
| `detectFromPitch(pitchesHz): Float` | One-shot from a raw pitch array. Returns `0` (not throws) for empty input. |

All three return **`0` on detection failure** (e.g., not enough voiced frames). Always check `> 0` before using the result.

## Common Pitfalls

1. **Returns 0, not -1, on failure.** Sentinel value, not an exception (the empty-input throw on `detect` and `detectFromAudio` is a separate caller-bug case).
2. **Tuned for speech, not singing.** For singing, use [`TesseraBreath`](./breath) or [`Tessera.analyze`](./overview).
3. **Don't pass an empty `PitchContour` to `detect`.** It throws (caller bug). `detectFromPitch` is the silent variant for already-known-empty input.

## Use with shruti derivation

The speaking-pitch reading feeds directly into [`MusicTheory.deriveUserShruti`](../common/music-theory#deriveusershruti):

```kotlin
val nspHz = TesseraSpeakingPitch.detectFromAudio(speechAudio)
val derivation = MusicTheory.deriveUserShruti(
    nspHz = nspHz.takeIf { it > 0 },
    rangeLowHz = userRange?.lower?.frequencyHz,
    rangeHighHz = userRange?.upper?.frequencyHz,
)
println("Practice shruti: ${derivation.targetHz} Hz (source = ${derivation.source})")
```

## See also

- [MusicTheory.deriveUserShruti](../common/music-theory#deriveusershruti)
- [TesseraRange](./range) — for vocal range
- [Tessera (multi-metric)](./overview)
