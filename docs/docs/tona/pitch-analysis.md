---
sidebar_position: 4
---

# PitchAnalysis

Histograms, tuning estimation, quantization, and melodic transcription on a `PitchContour`. Use after detection ([`PitchDetection`](./pitch-detection)) and optional cleanup ([`PitchProcessing`](./pitch-processing)).

For full intonation scoring (per-note deviation + 0–100 score), use [`Accura.analyzePitching`](../accura/intonation) — Accura wraps this facade.

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
| `computeSvaraTemplate(contour, tonicHz, genre, svaras, config = SvaraTemplateConfig.DEFAULT): SvaraTemplate` | Data-driven svara grid for a raga from the contour's folded histogram (see below). |
| `transcribeNotes(contour, tonicHz, template, config = NoteTranscriptionConfig.DEFAULT): List<NoteEvent>` | Transcribe the contour into discrete, svara-labelled notes against a `SvaraTemplate`. |

### Svara template + note transcription

`computeSvaraTemplate` and `transcribeNotes` are a two-step path for turning a raga performance into labelled notes:

```kotlin
val template = PitchAnalysis.computeSvaraTemplate(
    contour = contour,
    tonicHz = 196f,                                 // G3
    genre = MusicGenre.CARNATIC,                    // from common.model
    svaras = listOf("S", "R2", "G3", "M1", "P", "D2", "N3"),  // svara NAMES the raga uses
)
val notes = PitchAnalysis.transcribeNotes(contour, tonicHz = 196f, template = template)
for (note in notes) {
    println("${note.label}: ${note.tStartSeconds}s – ${note.tEndSeconds}s @ ${note.freqHz} Hz")
}
```

```kotlin
fun computeSvaraTemplate(
    contour: PitchContour,
    tonicHz: Float,
    genre: MusicGenre,
    svaras: List<String>,
    config: SvaraTemplateConfig = SvaraTemplateConfig.DEFAULT,
): SvaraTemplate
```

`computeSvaraTemplate` folds the contour's pitch histogram into one octave, peak-picks it, and for each svara the raga uses takes the measured peak near its theory position (falling back to the theory default when no peak is close), then extends the grid across octaves. The grid is selected by `genre`, not a boolean mask:

- **`MusicGenre.HINDUSTANI`** → 12-TET grid, names `S r R g G m M P d D n N`.
- **`MusicGenre.CARNATIC`** → just-intonation grid, 16 swarasthanas `S R1 R2 R3 G1 G2 G3 M1 M2 P D1 D2 D3 N1 N2 N3`.

Pass the svara **names** the raga uses (not a `svaraMask` / boolean array). Octave labels in the resulting `symbols` use combining-dot accents (e.g. `S`, `Ṡ` for the octave up, `Ṣ` for the octave down).

Throws `IllegalArgumentException` when `tonicHz <= 0`, `genre == MusicGenre.WESTERN`, or a name in `svaras` is not a valid svara for the genre.

```kotlin
fun transcribeNotes(
    contour: PitchContour,
    tonicHz: Float,
    template: SvaraTemplate,
    config: NoteTranscriptionConfig = NoteTranscriptionConfig.DEFAULT,
): List<NoteEvent>
```

`transcribeNotes` assigns each contour frame to a template svara when within `config.vicinityCents`, groups consecutive frames into notes, and drops notes shorter than `config.minDurationSeconds`. Each `NoteEvent` carries onset, offset, svara frequency (`tonic · 2^(cents/1200)`), and label. Notes are returned sorted ascending by onset (may be empty). Throws `IllegalArgumentException` when `tonicHz <= 0`.

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

### SvaraTemplateConfig

Presets: `DEFAULT`. Also has a `Builder` and `.copy()` (ADR-001).

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `smoothSigmaCents` | `Float` | `15` | Gaussian smoothing applied to the folded histogram before peak detection |
| `peakToleranceCents` | `Float` | `50` | A detected peak is used as a svara's measured position only if within this many cents of its theory default; otherwise the default is used |
| `octaveSpan` | `Int` | `1` | Octaves to extend the grid on each side of the middle octave (`1` → lower, middle, upper) |

### NoteTranscriptionConfig

Presets: `DEFAULT`. Also has a `Builder` and `.copy()` (ADR-001).

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `vicinityCents` | `Float` | `50` | A frame is assigned to a svara when its cents value is within this many cents of the svara's position |
| `minDurationSeconds` | `Float` | `0.05` | Notes shorter than this are discarded |

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

### SvaraTemplate

A raga's measured intonation grid, produced by `computeSvaraTemplate` and consumed by `transcribeNotes`. `cents` and `symbols` are parallel arrays of equal length, sorted ascending by cents.

```kotlin
data class SvaraTemplate(
    val cents: FloatArray,       // svara positions in cents relative to tonic (across octaves)
    val symbols: List<String>,   // parallel labels, e.g. "S", "g", "Ṡ", "ṇ"
)
```

`size: Int` (computed) returns the number of svara entries (across all octaves). Octave accents in `symbols` use combining dots (e.g. `S`, `Ṡ` one octave up, `Ṣ` one octave down).

### NoteEvent

A transcribed note: a time-bounded span labelled with a svara and its frequency, produced by `transcribeNotes`.

```kotlin
data class NoteEvent(
    val tStartSeconds: Float,    // note onset in seconds
    val tEndSeconds: Float,      // note offset in seconds
    val freqHz: Float,           // svara frequency in Hz (tonic · 2^(cents/1200))
    val label: String,           // svara label, e.g. "S", "g", "Ṡ"
)
```

`durationSeconds: Float` (computed) returns `tEndSeconds - tStartSeconds`.

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
| `minPeakAreaFraction` | `0f` (relative valley-to-valley area gate; `0` = off) |

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
