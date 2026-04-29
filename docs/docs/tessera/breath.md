---
sidebar_position: 2
---

# TesseraBreath

Breath capacity, control, and reference-vs-student comparison from a `PitchContour`.

## Quick Start

### Kotlin

```kotlin
// One-shot scoring
val score = TesseraBreath.computeScore(contour, BreathConfig.PRACTICE)
println("Capacity: ${score.capacity}s, Control: ${score.controlScore}")

// Composable: reuse the breath function for both score and comparison
val bf = TesseraBreath.computeBreathFunction(contour)
val score = TesseraBreath.computeScore(bf)
val comparison = TesseraBreath.compare(refBf, bf)
```

### Swift

```swift
let score = TesseraBreath.computeScore(contour: contour, config: .practice)
print("Capacity: \(score.capacity ?? 0)s, Control: \(score.controlScore)")
```

## Methods

| Method | Description |
|--------|-------------|
| `computeBreathFunction(contour, config = BreathConfig.DEFAULT): BreathFunction` | Build the shared intermediate (values, times, equivalent sustain time) |
| `computeScore(breathFunction, config = BreathConfig.DEFAULT): BreathScore` | Score from a pre-computed function |
| `computeScore(contour, config = BreathConfig.DEFAULT): BreathScore` | One-shot from a contour |
| `compare(reference, student, config = BreathConfig.DEFAULT): BreathComparison` | FFT cross-correlation peak-matching of two breath functions |
| `compare(refContour, studentContour, config = BreathConfig.DEFAULT): BreathComparison` | One-shot comparison from contours |

## Result types

### BreathScore

```kotlin
data class BreathScore(
    val capacity: Float?,    // longest phrase duration (seconds), null when too-short audio
    val controlScore: Float, // sigmoid-scaled control score in [0, 1)
)
```

`capacity` is **nullable** — null when there are no detectable phrase boundaries (e.g., very short audio with no pauses). Callers must guard.

### BreathFunction

```kotlin
data class BreathFunction(
    val values: FloatArray,       // exponential growth on voiced, decay on unvoiced
    val times: FloatArray,        // same length as values
    val equivalentSustainTime: Float, // input to the control sigmoid
)
```

### BreathComparison

```kotlin
data class BreathComparison(val matchScore: Float)  // [0, 1]
```

## BreathConfig

### Presets

| Preset | tauRise | tauFall | sigmoidK | sigmoidM | minUnvoiced |
|--------|---------|---------|----------|----------|-------------|
| `DEFAULT` / `SINGING` | 8.0 | 0.4 | 0.3 | 10 | 0.10 |
| `PRACTICE` | 8.0 | 0.15 | 0.3 | 15 | 0.05 |
| `SPEECH` | 5.0 | 0.4 | 0.3 | 6 | 0.15 |
| `CLINICAL` | 8.0 | 0.1 | 0.25 | 20 | 0.05 |

Use `PRACTICE` for sustained alankaar/scales, `SPEECH` for spoken word, `CLINICAL` for sustained-phonation tests.

### Properties

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `featureRate` | `Float` | `30` | Resampling rate for analysis (Hz) |
| `tauRise` | `Float` | `8.0` | Time constant for growth during voicing (s) |
| `tauFall` | `Float` | `0.4` | Time constant for decay during pauses (s) |
| `sigmoidK` | `Float` | `0.3` | Sigmoid steepness for control score |
| `sigmoidM` | `Float` | `10` | Sigmoid midpoint (s) |
| `minUnvoicedDuration` | `Float` | `0.1` | Min gap (s) treated as a real pause |
| `controlThreshold` | `Float` | `0.55` | Peak detection amplitude threshold |
| `lofNeighbors` | `Int` | `25` | Neighbors for LOF outlier detection |
| `minAlignmentDuration` | `Float` | `6.0` | Min length (s) for cross-correlation comparison |
| `peakTimeTolerance` | `Float` | `0.5` | Max time offset for matching peaks (s) |
| `peakAmplitudeTolerance` | `Float` | `0.3` | Max amplitude ratio difference (30%) |
| `alignmentSnippets` | `Int` | `6` | Random snippets for cross-correlation estimation |
| `alignmentSnippetDuration` | `Int` | `5` | Duration of each snippet (s) |

### Builder

```kotlin
val config = BreathConfig.Builder()
    .preset(BreathConfig.PRACTICE)
    .sigmoidM(12f)
    .minUnvoicedDuration(0.08f)
    .build()
```

## Common Pitfalls

1. **Contour must have ≥ 2 samples.** Throws `IllegalArgumentException` per ADR-022.
2. **`capacity` is nullable.** Always check `capacity != null` before using.
3. **Match the preset to the audio.** `SINGING` for songs, `PRACTICE` for alankaar, `SPEECH` for spoken word, `CLINICAL` for sustained-tone tests.

## See also

- [Tessera (multi-metric)](./overview)
- [TesseraSession](./session) — streaming counterpart
- [PitchDetection](../tona/pitch-detection) — produces the input contour
