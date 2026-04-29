---
sidebar_position: 3
---

# TesseraAgility

Vocal agility scoring — how dynamically the singer varies pitch (fast ornaments, gamakas, runs). Operates on a `PitchContour`.

## Quick Start

### Kotlin

```kotlin
val score = TesseraAgility.computeScore(contour)
println("Agility: ${score.scores.first()}")

// Per-segment scoring
val segments = listOf(0f to 5f, 5f to 10f)
val perSegment = TesseraAgility.computeScore(contour, segments = segments)
```

### Swift

```swift
let score = TesseraAgility.computeScore(contour: contour)
print("Agility: \(score.scores.first ?? 0)")
```

## Methods

| Method | Description |
|--------|-------------|
| `computeContour(contour, config = AgilityConfig.DEFAULT): AgilityContour` | 10-stage pipeline intermediate (RMS, RMS-oscillation, smoothed pitch, times) |
| `computeScore(agilityContour, segments = null, config = DEFAULT): AgilityScore` | Score from a pre-computed contour |
| `computeScore(contour, segments = null, config = DEFAULT): AgilityScore` | One-shot from a `PitchContour` |

`segments: List<Pair<Float, Float>>?`. When `null`, the score is the mean of the top 50% of L-second windows with L/2 overlap. When provided, returns one score per segment.

## Result types

### AgilityScore

```kotlin
data class AgilityScore(val scores: FloatArray)
```

Each score is in `[0, 1]`. Values below 0.5 indicate little to no vocal agility.

### AgilityContour

```kotlin
data class AgilityContour(
    val rms: FloatArray,         // log-derivative RMS of pitch
    val rmsOsc: FloatArray,      // oscillation RMS with boundary cleaning
    val pitchSmooth: FloatArray, // pitch in MIDI after smoothing + OneEuro
    val times: FloatArray,
)
```

## AgilityConfig

Currently only one preset (`DEFAULT`); parameters tuned for Indian classical music.

| Property | Type | Default |
|----------|------|---------|
| `featureRate` | `Float` | `10` |
| `tau` | `Float` | `3` |
| `logK` | `Float` | `0.6` |
| `logM` | `Float` | `4.8` |
| `classifierA` | `Float` | `5` |
| `classifierB` | `Float` | `18` |
| `classifierC` | `Float` | `10.5` |
| `sigmoidAlpha` | `Float` | `0.6` |
| `windowSeconds` | `Float` | `8` |

Builder mirrors the field names.

## Common Pitfalls

1. **Contour must have ≥ 2 samples** (throws per ADR-022).
2. **Segment times are in seconds.** A `Pair(startSec, endSec)`. Zero/negative-duration segments return an explicit 0.5 fallback.
3. **Segments with no data points score near 0**, not 0.5 — only the explicit-fallback case returns 0.5.

## See also

- [Tessera (multi-metric)](./overview)
- [TesseraSession](./session) — streaming counterpart
