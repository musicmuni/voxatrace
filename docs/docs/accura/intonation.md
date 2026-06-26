---
sidebar_position: 2
---

# Accura Intonation Analysis

Per-note deviation analysis and 0–100 scoring for vocal performances. Each
detected note is graded against its target interval in a tuning system; notes
the singer dwelt on outside the requested scale are reported separately as
off-scale notes.

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
    scaleIntervals = listOf(                  // optional: explicit scale targets
        TargetInterval(0f, "S"),
        TargetInterval(200f, "R2"),
        TargetInterval(400f, "G3"),
        TargetInterval(700f, "P"),
        TargetInterval(900f, "D2"),
    ),
    genre = MusicGenre.CARNATIC,
    alignTuning = true,
)

if (result.error == null) {
    val score = Accura.calculateScore(result, weightingMethod = WeightingMethod.EQUAL)
    println("Score: ${score.score}/100 (${score.tier}) over ${score.noteCount} notes")
    for (note in result.notes) {
        println("${note.label}: ${note.deviationCents} c, ${note.score}/100 (${note.tier})")
    }
    for (off in result.offScaleNotes) {
        println("off-scale ${off.label}, nearest in-scale ${off.nearestInScaleLabel}")
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
    scaleIntervals: [
        TargetInterval(cents: 0,   label: "S"),
        TargetInterval(cents: 200, label: "R2"),
        TargetInterval(cents: 400, label: "G3"),
        TargetInterval(cents: 700, label: "P"),
        TargetInterval(cents: 900, label: "D2"),
    ],
    genre: .carnatic,
    alignTuning: true
)
if result.error == nil {
    let score = Accura.calculateScore(result: result, weightingMethod: .equal)
    print("Score: \(score.score)/100 (\(score.tier)) over \(score.noteCount) notes")
}
```

## analyzePitching

```kotlin
fun analyzePitching(
    contour: PitchContour,
    tonicHz: Float,
    intonationSystem: IntonationSystem,
    scaleIntervals: List<TargetInterval>? = null,
    genre: MusicGenre = MusicGenre.CARNATIC,
    alignTuning: Boolean = true,
    minNotes: Int = 3,
): IntonationAnalysisResult
```

### Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `contour` | `PitchContour` | — | Input contour. Must be non-empty. |
| `tonicHz` | `Float` | — | Tonic frequency in Hz. Must be `> 0`. |
| `intonationSystem` | `IntonationSystem` | — | `EQ` (12-TET) or `JI` (Just Intonation) |
| `scaleIntervals` | `List<TargetInterval>?` | `null` | Optional explicit `(cents, label)` targets the user is grading against. Multi-octave allowed; labels are used verbatim in the result. `null` grades against the full 12-TET / JI multi-octave grid with chromatic labels. If non-null, must be non-empty. |
| `genre` | `MusicGenre` | `CARNATIC` | Music genre whose naming convention is used for the chromatic fallback labels (only consulted when `scaleIntervals == null`) |
| `alignTuning` | `Boolean` | `true` | If true, estimate and correct a global tuning offset before analysis |
| `minNotes` | `Int` | `3` | Minimum histogram peaks for a conclusive result. Lower to `1` for sustained-note contexts (e.g. tanpura tuning). Must be `>= 1`. |

### Returns

Always returns a non-null `IntonationAnalysisResult`. Inspect `result.error` first:
- **Success**: `error == null`, `notes` populated (and `offScaleNotes` populated when an explicit scale was supplied and the singer dwelt outside it).
- **Inconclusive**: `error != null` (string explaining why), `notes` empty.

### Throws

`IllegalArgumentException` when:
- `contour` is empty
- `tonicHz <= 0`
- `scaleIntervals` is non-null but empty
- `minNotes < 1`

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

For small samples (≤ 4 notes), the score is blended with a re-weighted score that
drops the worst note (so a single flubbed note doesn't dominate). Result rounded
to one decimal. Off-scale notes never contribute to the score.

### Throws

`IllegalArgumentException` when `result.error != null` or `result.notes` is empty — inconclusive analyses cannot be scored.

## Result types

### IntonationAnalysisResult

```kotlin
data class IntonationAnalysisResult(
    val intonationSystem: IntonationSystem,
    val tonicHz: Float,
    val shrutiLabel: String,                      // e.g., "G3"
    val analysisParameters: Map<String, Any>,     // reproducibility metadata
    val notes: List<NoteAnalysis>,                // empty when error != null
    val offScaleNotes: List<OffScaleNote> = emptyList(),
    val error: String? = null,
)
```

`analysisParameters` keys: `"histogram_smooth_sigma"`, `"peak_amp_thresh"`, `"valley_thresh"`, `"min_peak_area_frac"`, `"peak_stats_max_peakwidth_cents"`, `"peak_stats_min_peakwidth_cents"`, `"offscale_capture_radius_cents"`, `"tuning_offset_cents"`, `"align_tuning"`.

### NoteAnalysis

```kotlin
data class NoteAnalysis(
    val label: String,                // e.g., "S", "R2", "C4"
    val targetIntervalCents: Float,
    val targetPitchHz: Float,
    val detectedPeakCents: Float,
    val detectedPeakHz: Float,
    val deviationCents: Float,        // signed: + sharp, − flat
    val deviationPercent: Float,      // % of interval to nearest neighbor
    val score: Float,                 // per-note accuracy, [0, 100]
    val tier: PitchingTier,           // the band score falls in
    val amplitude: Float,             // histogram peak amplitude (proxy for duration)
)
```

`tier` is the banded view of `score`: a note's tier and score can never disagree.

### OffScaleNote

A prominent peak the singer dwelt on that landed on a chromatic degree outside
the requested scale — a note sung "by mistake", not a mis-intoned scale note.
Only populated when an explicit `scaleIntervals` was supplied. Off-scale notes
carry no target, score, or tier, and never affect `PitchingScore`; they are
reported for awareness only.

```kotlin
data class OffScaleNote(
    val label: String,                // chromatic degree it snapped to, e.g. "M2", "G2"
    val detectedPeakCents: Float,
    val detectedPeakHz: Float,
    val offsetFromDegreeCents: Float, // signed cents from the named degree
    val nearestInScaleLabel: String,  // nearest in-scale note ("you reached for P")
    val centsFromNearestInScale: Float,
    val amplitude: Float,
)
```

### PitchingScore

```kotlin
data class PitchingScore(
    val intonationSystem: IntonationSystem,
    val score: Float,        // [0, 100], 1 decimal
    val tier: PitchingTier,  // banded view of score
    val noteCount: Int,
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
enum class WeightingMethod { EQUAL, DURATION }

// from com.musicmuni.voxatrace.common.model
enum class MusicGenre { CARNATIC, HINDUSTANI, WESTERN }

// Each tier carries its minScore — the lowest score that bands into it.
enum class PitchingTier(val minScore: Float) {
    EXCELLENT(85f),   // sung essentially on target
    GOOD(65f),        // clearly the intended note
    FAIR(40f),        // noticeably off, still recognizable
    POOR(0f),         // far enough off to read as the wrong note
}
```

`WeightingMethod.DURATION` weights notes by histogram peak amplitude (longer-held notes count more), falling back to `EQUAL` when all amplitudes are ≈ 0. `PitchingTier.minScore` is the single source of truth for the band boundaries, so a UI can draw a score gauge without hardcoding them.

## Common Pitfalls

1. **Always check `result.error` before calling `calculateScore`.** Passing an inconclusive analysis throws.
2. **`scaleIntervals` are exact `(cents, label)` targets**, not note-name filters. Pass the precise intervals and the labels you want in the result; multi-octave is supported. `null` falls back to the full 12-TET / JI grid.
3. **`alignTuning` shifts the histogram, not `tonicHz`.** The reported `tonicHz` is unchanged; the offset goes into `analysisParameters["tuning_offset_cents"]`.
4. **`intonationSystem` selects target intervals**, not an input-tuning assumption. Pick `EQ` to grade against 12-TET, `JI` to grade against just-intonation ratios.
5. **Off-scale notes are informational.** They appear only when `scaleIntervals` is supplied, never carry a score, and never change `PitchingScore`.

## See also

- [PitchDetection](../tona/pitch-detection) — produces the input contour
- [PitchAnalysis](../tona/pitch-analysis) — lower-level histogram + transcription
- [MusicTheory](../common/music-theory) — interval generators and conversions
