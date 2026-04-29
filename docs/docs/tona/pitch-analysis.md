---
sidebar_position: 4
---

# PitchAnalysis

Histograms, tuning estimation, quantization, and melodic transcription on a `PitchContour`. Use after detection ([`PitchDetection`](./pitch-detection)) and optional cleanup ([`PitchProcessing`](./pitch-processing)).

For full intonation scoring (per-swara deviation + 0–100 score), use [`Accura.analyzePitching`](../accura/intonation) — Accura wraps this facade.

## Quick Start

### Kotlin

```kotlin
val extractor = PitchDetection.createContourExtractor(ContourExtractorConfig.SCORING)
val contour = extractor.extract(audioSamples, 16000)
extractor.release()

val histogram = PitchAnalysis.computeHistogram(contour, tonicHz = 196f)         // G3
val tuningOffset = PitchAnalysis.estimateTuningOffset(contour, refFreqHz = 196f)

val targetIntervals = MusicTheory.EQ_TEMPERED_INTERVALS_CENTS_BASE
    .map { it.toFloat() }.toFloatArray()
val segments = PitchAnalysis.labelByMeanPitch(contour, tonicHz = 196f, targetIntervals)
for (seg in segments) {
    println("${seg.label}: ${seg.startSeconds}s – ${seg.endSeconds}s")
}
```

### Swift

```swift
let histogram = PitchAnalysis.computeHistogram(contour: contour, tonicHz: 196)
let offset = PitchAnalysis.estimateTuningOffset(contour: contour, refFreqHz: 196)

let targets = MusicTheory.eqTemperedIntervalsCentsBase.map { Float($0) }
let segments = PitchAnalysis.labelByMeanPitch(
    contour: contour, tonicHz: 196, targetIntervalsCents: targets
)
```

## Methods

### Histogram

| Method | Description |
|--------|-------------|
| `computeHistogram(contour, tonicHz, config = HistogramConfig.DEFAULT): PitchHistogram` | Bins pitch in cents relative to tonic. Optional fold-octaves, density normalization, smoothing. |
| `estimateTuningOffset(contour, refFreqHz, centTolerance: Float = 50f): Float` | Aligns histogram peaks to the 12-TET grid; returns offset in cents. |

### Transcription

| Method | Description |
|--------|-------------|
| `quantize(contour, tonicHz, targetIntervalsCents, config = QuantizationConfig.DEFAULT): PitchContour` | Snap stable frames to nearest target interval; non-stable frames become unvoiced. |
| `labelByMeanPitch(contour, tonicHz, targetIntervalsCents, config = LabellingConfig.DEFAULT): List<TonalSegment>` | Sliding-window mean-pitch labelling against target intervals. |
| `fitLinearSegments(contour, tonicHz, config = LinearFitConfig.DEFAULT): List<TonalSegment>` | Piecewise linear regression (gamaka / ornament analysis). |

## Config classes

### HistogramConfig

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `numBins` | `Int?` | `null` (auto) | Number of bins; `null` selects from range |
| `density` | `Boolean` | `true` | Normalize so total area = 1 |
| `foldOctaves` | `Boolean` | `false` | Fold all pitches into one octave |
| `mode` | `HistogramMode` | `DURATION` | `DURATION` (time-weighted) or `INSTANCE_COUNT` |
| `smoothSigma` | `Float?` | `5f` | Gaussian smoothing sigma; `null` = no smoothing |

Presets: `DEFAULT`, `FOLDED` (foldOctaves=true), `RAW` (density=false, smoothSigma=null).

### QuantizationConfig

| Property | Type | Default |
|----------|------|---------|
| `slopeThresholdCentsPerSec` | `Float` | `150` |
| `maxDeviationCents` | `Float` | `50` |
| `medianFilterWindowSamples` | `Int` | `7` |
| `applyMedianFilter` | `Boolean` | `true` |
| `minSegmentDurationMs` | `Int?` | `null` |

### LabellingConfig

| Property | Type | Default |
|----------|------|---------|
| `windowSeconds` | `Float` | `0.150` |
| `hopSeconds` | `Float` | `0.030` |

### LinearFitConfig

| Property | Type | Default |
|----------|------|---------|
| `windowSeconds` | `Float` | `1.5` |
| `breakThresholdSeconds` | `Float` | `1.5` |
| `hopSeconds` | `Float?` | `null` (= window/2) |

## Result types

### PitchHistogram

```kotlin
data class PitchHistogram(
    val binCenters: FloatArray,
    val values: FloatArray,
    val tonicHz: Float,
    val isDensity: Boolean,
    val isFolded: Boolean,
    val mode: HistogramMode
)
```

| Method | Description |
|--------|-------------|
| `smooth(sigma = 5f): PitchHistogram` | New histogram with Gaussian-smoothed values |
| `normalizeArea(): PitchHistogram` | New histogram normalized so total area = 1 |
| `getPeaksValleys(targetIntervalsCents, config = PeakDetectionConfig.DEFAULT): PeakData` | Peak detection — `"hybrid"`, `"slope"`, or `"interval"` methods |
| `computePeakStats(peakData, rawPitchCents, refIntervalsCents, config = PeakStatsConfig.DEFAULT): PeakStatsCollection` | Per-peak distribution stats |

### TonalSegment

```kotlin
data class TonalSegment(
    val startSeconds: Float,
    val endSeconds: Float,
    val label: String? = null,            // labelByMeanPitch only
    val meanCents: Float? = null,         // labelByMeanPitch only
    val slopeCentsPerSec: Float? = null,  // fitLinearSegments only
    val interceptCents: Float? = null,    // fitLinearSegments only
)
```

`duration: Float` (computed) returns `endSeconds - startSeconds`.

`TonalSegment` is distinct from `calibra.model.Segment` (which models song structure with `index` / `lyrics` / student timing).

### PeakStats / PeakStatsCollection

`PeakStats` carries per-peak statistics: `referenceInterval`, `peakPosition`, `peakAmplitude`, `mean`, `median`, `stdDev`, `variance`, `coeffOfVariation`, `skewness`, `kurtosis`, `pearsonSkew2`. `PeakStatsCollection` is a `Map<Float, PeakStats>` keyed by reference interval, iterable.

### PeakDetectionConfig

| Property | Default |
|----------|---------|
| `method` | `"hybrid"` (also `"slope"`, `"interval"`) |
| `peakAmpThresh` | `0.00005f` |
| `valleyThresh` | `0.00003f` |
| `lookahead` | `20` |
| `avgIntervalHint` | `null` |

### PeakStatsConfig

| Property | Default |
|----------|---------|
| `maxPeakwidthCents` | `50` |
| `minPeakwidthCents` | `25` |
| `symmetricBounds` | `true` |

## Common Pitfalls

1. **`tonicHz` must be > 0.** All methods convert Hz → cents relative to tonic; a zero or negative tonic produces NaN.
2. **Cleanup the contour first.** Raw contours with octave errors produce misleading histograms; run through `PitchProcessing.process(contour, PitchProcessingConfig.SCORING)` before analyzing.
3. **`targetIntervalsCents` is in cents, not Hz.** Use `MusicTheory.EQ_TEMPERED_INTERVALS_CENTS_BASE` for 12-TET (returns `List<Int>`; convert to `FloatArray`).
4. **Histogram smoothing is on by default** (`smoothSigma = 5f`). Use `HistogramConfig.RAW` to disable.

## See also

- [PitchDetection](./pitch-detection) — extract a contour
- [PitchProcessing](./pitch-processing) — clean before analyzing
- [Accura](../accura/intonation) — full intonation analysis + 0–100 scoring
- [MusicTheory](../common/music-theory) — interval generators, conversions
