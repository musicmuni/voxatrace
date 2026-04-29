---
sidebar_position: 2
---

# Accura Intonation Analysis

Per-swara deviation analysis and 0–100 scoring for vocal performances.

## Quick Start

### Kotlin

```kotlin
val extractor = PitchDetection.createContourExtractor(ContourExtractorConfig.SCORING)
val contour = extractor.extract(audioSamples, sampleRate = 16000)
extractor.release()

val result = Accura.analyzePitching(
    contour = contour,
    tonicHz = 196f,                          // G3
    intonationSystem = IntonationSystem.EQ,
    scaleNoteNames = listOf("C", "D", "E", "G", "A"),  // optional pentatonic filter
    noteLabelTradition = NoteLabelTradition.CARNATIC,
    alignTuning = true,
)

if (result.error == null) {
    val score = Accura.calculateScore(result, weightingMethod = WeightingMethod.EQUAL)
    println("Score: ${score.score}/100 over ${score.swaraCount} swaras")
    for (swara in result.swaras) {
        println("${swara.label}: ${swara.deviationCents} c (${swara.deviationRemark})")
    }
} else {
    println("Inconclusive: ${result.error}")
}
```

### Swift

```swift
let result = Accura.analyzePitching(
    contour: contour,
    tonicHz: 196,
    intonationSystem: .eq,
    scaleNoteNames: ["C", "D", "E", "G", "A"],
    noteLabelTradition: .carnatic,
    alignTuning: true
)
if result.error == nil {
    let score = Accura.calculateScore(result: result, weightingMethod: .equal)
    print("Score: \(score.score)/100")
}
```

## analyzePitching

```kotlin
fun analyzePitching(
    contour: PitchContour,
    tonicHz: Float,
    intonationSystem: IntonationSystem,
    scaleNoteNames: List<String>? = null,
    noteLabelTradition: NoteLabelTradition = NoteLabelTradition.CARNATIC,
    alignTuning: Boolean = true,
): IntonationAnalysisResult
```

### Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `contour` | `PitchContour` | — | Input contour. Must be non-empty. |
| `tonicHz` | `Float` | — | Tonic frequency in Hz. Must be `> 0`. |
| `intonationSystem` | `IntonationSystem` | — | `EQ` (12-TET) or `JI` (Just Intonation) |
| `scaleNoteNames` | `List<String>?` | `null` | Optional Western note-name filter (e.g. pentatonic). `null` = all 12 intervals. If non-null, must be non-empty. |
| `noteLabelTradition` | `NoteLabelTradition` | `CARNATIC` | Naming tradition for swara labels |
| `alignTuning` | `Boolean` | `true` | If true, estimate and correct global tuning offset before analysis |

### Returns

Always returns a non-null `IntonationAnalysisResult`. Inspect `result.error` first:
- **Success**: `error == null`, `swaras` populated.
- **Inconclusive**: `error != null` (string explaining why), `swaras` empty.

### Throws

`IllegalArgumentException` when:
- `contour` is empty
- `tonicHz <= 0`
- `scaleNoteNames` is non-null but empty

## calculateScore

```kotlin
fun calculateScore(
    result: IntonationAnalysisResult,
    weightingMethod: WeightingMethod = WeightingMethod.EQUAL,
): PitchingScore
```

Grading scale (piecewise linear on `|deviation|` as % of interval to nearest neighbor):
- ≤ 20%: 90–100
- 20–40%: 70–90
- 40–80%: 50–70
- &gt; 80%: 0–50 (clamped at 0)

For small samples (≤ 4 swaras), the score is blended with a re-weighted score that drops the worst note (so a single flubbed note doesn't dominate). Result rounded to one decimal.

### Throws

`IllegalArgumentException` when `result.error != null` or `result.swaras` is empty — inconclusive analyses cannot be scored.

## Result types

### IntonationAnalysisResult

```kotlin
data class IntonationAnalysisResult(
    val intonationSystem: IntonationSystem,
    val tonicHz: Float,
    val shrutiLabel: String,                  // e.g., "G3"
    val analysisParameters: Map<String, Any>, // reproducibility metadata
    val swaras: List<SwaraAnalysis>,          // empty when error != null
    val error: String? = null,
)
```

`analysisParameters` keys: `"histogram_smooth_sigma"`, `"peak_amp_thresh"`, `"valley_thresh"`, `"peak_stats_max_peakwidth_cents"`, `"peak_stats_min_peakwidth_cents"`, `"tuning_offset_cents"`, `"align_tuning"`.

### SwaraAnalysis

```kotlin
data class SwaraAnalysis(
    val label: String,                // e.g., "S", "R2", "C4"
    val targetIntervalCents: Float,
    val targetPitchHz: Float,
    val detectedPeakCents: Float,
    val detectedPeakHz: Float,
    val deviationCents: Float,        // signed: + sharp, − flat
    val deviationPercent: Float,      // % of interval to nearest neighbor
    val deviationRemark: String,      // "Excellent" ≤20%, "Good" ≤50%, "Fair" ≤100%, else "Poor"
    val amplitude: Float,             // histogram peak amplitude (proxy for duration)
)
```

### PitchingScore

```kotlin
data class PitchingScore(
    val intonationSystem: IntonationSystem,
    val score: Float,        // [0, 100], 1 decimal
    val swaraCount: Int,
) : Comparable<PitchingScore>
```

`Comparable` ordering is by `score` ascending (within `1e-6` treated as equal). Useful when grading the same performance against EQ and JI to pick the better fit:

```kotlin
val eq = Accura.analyzePitching(contour, tonicHz, IntonationSystem.EQ).let {
    if (it.error == null) Accura.calculateScore(it) else null
}
val ji = Accura.analyzePitching(contour, tonicHz, IntonationSystem.JI).let {
    if (it.error == null) Accura.calculateScore(it) else null
}
val best = listOfNotNull(eq, ji).maxOrNull()
```

## Enums

```kotlin
enum class IntonationSystem { EQ, JI }
enum class NoteLabelTradition { CARNATIC, HINDUSTANI, WESTERN }
enum class WeightingMethod { EQUAL, DURATION }
```

`WeightingMethod.DURATION` weights swaras by histogram peak amplitude (longer-held notes count more), falling back to `EQUAL` when all amplitudes are ≈ 0.

## Common Pitfalls

1. **Always check `result.error` before calling `calculateScore`.** Passing an inconclusive analysis throws.
2. **`scaleNoteNames` filters by Western note name** even when `noteLabelTradition = CARNATIC` (naming and filtering are independent).
3. **`alignTuning` shifts the histogram, not `tonicHz`.** The reported `tonicHz` is unchanged; the offset goes into `analysisParameters["tuning_offset_cents"]`.
4. **`intonationSystem` selects target intervals**, not an input-tuning assumption. Pick `EQ` to grade against 12-TET, `JI` to grade against just-intonation ratios.

## See also

- [PitchDetection](../tona/pitch-detection) — produces the input contour
- [PitchAnalysis](../tona/pitch-analysis) — lower-level histogram + transcription
- [MusicTheory](../common/music-theory) — interval generators and conversions
