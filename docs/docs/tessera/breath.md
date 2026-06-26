---
sidebar_position: 2
---

# TesseraBreath

Breath control score, phrase-level structure, and reference-vs-student alignment from a `PitchContour`.

## Quick Start

### Kotlin

```kotlin
// One-shot analysis: control score + phrase summary
val metrics = TesseraBreath.analyze(contour, config = BreathConfig.PRACTICE)
println("Control: ${metrics.controlScore}")
println("Longest phrase: ${metrics.phrases?.longestDuration}s")
println("Comfortable range: ${metrics.phrases?.comfortableRange}")

// With a reference recording: also populates alignmentScore
val metricsVsRef = TesseraBreath.analyze(studentContour, reference = referenceContour)
println("Alignment: ${metricsVsRef.alignmentScore}")

// Composable: reuse the breath function for analysis + comparison
val bf = TesseraBreath.computeBreathFunction(contour)
val metrics = TesseraBreath.analyze(bf)
val refBf = TesseraBreath.computeBreathFunction(referenceContour)
val alignment = TesseraBreath.compare(refBf, bf)
```

### Swift

```swift
let metrics = TesseraBreath.analyze(contour: contour, config: .practice)
print("Control: \(metrics.controlScore ?? 0), Longest: \(metrics.phrases?.longestDuration ?? 0)")
```

## Methods

| Method | Description |
|--------|-------------|
| `computeBreathFunction(contour, config = DEFAULT): BreathFunction` | Build the shared intermediate (values, times, equivalent sustain time) |
| `analyze(contour, reference = null, config = DEFAULT): BreathMetrics` | Control score + phrase summary; pass `reference` to also populate `alignmentScore` |
| `analyze(breathFunction, reference = null, config = DEFAULT): BreathMetrics` | Same, but reusing pre-computed breath functions |
| `compare(reference, student, config = DEFAULT): Float?` | FFT cross-correlation peak-matching of two breath functions; returns alignment score alone. Null when too short to align, or when the reference has no detectable breath peaks. |
| `compare(refContour, studentContour, config = DEFAULT): Float?` | One-shot comparison from contours |

## Result types

### BreathMetrics

```kotlin
data class BreathMetrics(
    val controlScore: Float?,           // sigmoid-scaled control score in [0, 1); null when no breath signal
    val phrases: PhraseSummary?,        // phrase-level structure; null when audio has no detectable phrase boundaries
    val alignmentScore: Float? = null,  // populated only when `analyze(..., reference = ..., ...)` is used
)
```

All three fields are nullable. `controlScore` is null when equivalent sustain time is zero (no voicing detected) — the sigmoid output for that case is mathematically valid but semantically meaningless. `phrases` is null when the recording has fewer than two pause boundaries (e.g., very short audio or unbroken voicing). `alignmentScore` is null on no-reference calls, when either recording is shorter than `BreathConfig.minAlignmentDuration`, or when the reference has no detectable breath peaks to align against.

### PhraseSummary

```kotlin
data class PhraseSummary(
    val totalPhrases: Int,
    val phrases: List<Phrase>,                   // each (startTime, duration) in seconds
    val comfortableRange: PhraseRange?,          // middle two bins of the 5-bin phrase-duration histogram
    val avgDuration: Float,                      // mean phrase duration (s)
    val shortestDuration: Float,                 // (s)
    val longestDuration: Float,                  // (s) — LOF-filtered peak phrase, headline value for UI
    val longestDurationUnfiltered: Float,        // (s) — raw maximum (no outlier filtering)
    val phraseToBreathRatios: FloatArray,        // index-aligned with `phrases`; phrase ÷ preceding pause
    val avgPhraseToBreathRatio: Float,           // headline efficiency value
)

data class Phrase(val startTime: Float, val duration: Float)
data class PhraseRange(val lower: Float, val upper: Float)
```

`longestDuration` excludes phrases flagged as statistical outliers by Local Outlier Factor on the phrase-to-breath ratios — it's resilient to single fluky phrases. Use `longestDurationUnfiltered` for the raw maximum.

### BreathFunction

```kotlin
data class BreathFunction(
    val values: FloatArray,            // exponential growth on voiced, decay on unvoiced
    val times: FloatArray,             // same length as values
    val equivalentSustainTime: Float,  // input to the control sigmoid
)
```

## BreathConfig

### Presets

| Preset | tauRise | tauFall | sigmoidK | sigmoidM | minUnvoicedDuration |
|--------|---------|---------|----------|----------|---------------------|
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
2. **`controlScore` is nullable.** Null when no voicing was detected. Always guard before scaling for display.
3. **`phrases` is nullable.** Always check before using `longestDuration`, `comfortableRange`, etc.
4. **`alignmentScore` is nullable.** Populated only when you pass `reference = ...`, and only when both recordings exceed `minAlignmentDuration` and the reference has detectable breath peaks.
5. **Match the preset to the audio.** `SINGING` for songs, `PRACTICE` for alankaar, `SPEECH` for spoken word, `CLINICAL` for sustained-tone tests.

## See also

- [Tessera (multi-metric)](./overview)
- [TesseraSession](./session) — streaming counterpart
- [PitchDetection](../tona/pitch-detection) — produces the input contour
