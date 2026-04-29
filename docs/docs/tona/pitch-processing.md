---
sidebar_position: 3
---

# PitchProcessing

Batch cleanup of an existing pitch contour: octave correction, smoothing, blip removal, masking, segmentation, resampling, interpolation. Use when you already have a contour (from [`PitchDetection`](./pitch-detection)) and want to clean it up before scoring or visualization.

## Quick Start

### Kotlin

```kotlin
// Tier 1: Preset
val cleaned = PitchProcessing.process(contour, PitchProcessingConfig.SCORING)

// Tier 2: Builder
val config = PitchProcessingConfig.Builder()
    .fixOctaveErrors(true)
    .removeSpuriousJumps(false)
    .smoothingWindowSize(9)
    .build()
val cleaned = PitchProcessing.process(contour, config)

// Tier 3: .copy()
val cleaned = PitchProcessing.process(contour, PitchProcessingConfig.DISPLAY.copy(hopMs = 20))
```

### Swift

```swift
let cleaned = PitchProcessing.process(contour: contour, config: .scoring)

let config = PitchProcessingConfig.Builder()
    .fixOctaveErrors(true)
    .removeSpuriousJumps(false)
    .smoothingWindowSize(9)
    .build()
let cleaned = PitchProcessing.process(contour: contour, config: config)
```

## Pipeline

`process(contour, config)` runs three stages in order. Each stage is skipped when its toggle is `false`:

```text
input ─► octave correction ─► blip removal ─► smoothing ─► output
```

For per-frame realtime processing (smoothing + octave correction), set `enableProcessing()` on `PitchDetectorConfig.Builder` instead — it runs inside the detector at hop rate.

## PitchProcessingConfig

### Presets

| Preset | Octave fix | Spurious jumps | Boundary octaves | Smoothing | Blip removal |
|--------|-----------|----------------|-------------------|-----------|--------------|
| `RAW` | off | off | off | off | off |
| `SCORING` | on | off | on | off | on (≥80 ms) |
| `DISPLAY` (Builder default) | on | on | on | on (window 7) | on (≥50 ms) |

### Properties

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `fixOctaveErrors` | `Boolean` | `true` | Snap-back octave correction |
| `removeSpuriousJumps` | `Boolean` | `true` | Pair-based jump classification (tona) |
| `octaveThresholdCents` | `Float` | `150` | Distance to 1200 c that counts as "octave-jumpy" |
| `referencePitchHz` | `Float` | `0` | Reference for snap-back (0 = auto) |
| `fixBoundaryOctaves` | `Boolean` | `true` | Onset/offset octave correction |
| `boundaryWindowMs` | `Float` | `50` | Edge window checked for boundary octaves |
| `smoothPitch` | `Boolean` | `true` | Apply smoothing filter |
| `smoothingWindowSize` | `Int` | `7` | Smoothing window (must be odd) |
| `removeBlips` | `Boolean` | `true` | Drop short voiced runs |
| `minimumNoteDurationMs` | `Float` | `80` | Min duration for valid run |
| `hopMs` | `Int` | `10` | Hop between frames (ms) |

## Methods

### Pipeline (config-driven)

| Method | Description |
|--------|-------------|
| `process(contour: PitchContour, config = DISPLAY): PitchContour` | Run pipeline; preserves contour metadata |
| `process(pitchesHz: FloatArray, config = DISPLAY): FloatArray` | Run pipeline on a raw array |

### Octave correction

| Method | Description |
|--------|-------------|
| `correctOctaveErrors(pitchesHz, config: OctaveCorrectionConfig = FULL): FloatArray` | Up to three stages: spurious-jump removal, snap-back, boundary correction. Each toggleable in config. |

### Smoothing & filters

| Method | Description |
|--------|-------------|
| `smooth(pitchesHz, windowSize: Int = 7): FloatArray` | Weighted average smoothing in cents space. `windowSize` must be odd and ≥ 1 (`IllegalArgumentException` otherwise). |
| `smoothGaussian(pitchesHz, sigma: Float = 3f): FloatArray` | Gaussian smoothing with NaN-aware gap interpolation |
| `medianFilter(pitchesHz, kernelSize: Int = 5): FloatArray` | Median filter. `kernelSize` must be odd and ≥ 1. |
| `rangeFilter(pitchesHz, minHz, maxHz): FloatArray` | Out-of-range values replaced with -1 |
| `iqrFilter(pitchesHz, multiplier: Float = 2.5f): FloatArray` | IQR-based outlier filter; outliers set to -1 |
| `dbscanFilter(pitchesHz, epsCents: Float = 100f, minSamples: Int = 200): FloatArray` | DBSCAN-based outlier filter |

### Blip removal & interpolation

| Method | Description |
|--------|-------------|
| `removeBlips(pitchesHz, hopMs: Int = 10, minDurationMs: Float = 80f): FloatArray` | Mark voiced runs shorter than `minDurationMs` as unvoiced |
| `interpolateSilence(pitchesHz, hopMs: Int = 10, method = LINEAR, maxGapMs: Float = 250f, forceRemaining: Boolean = false): FloatArray` | Fill short gaps in cents space (or Hz / exponential, see `InterpolationMethod`) |

### Masks & segmentation

| Method | Description |
|--------|-------------|
| `getValidPitchMask(pitchesHz): BooleanArray` | True where pitch is voiced |
| `getDurationMask(pitchesHz, hopMs = 10, minDurationMs = 80f): BooleanArray` | True where voiced run ≥ duration |
| `getStableSlopeMask(pitchesHz, hopMs = 10, maxSlopeCentsPerSec = 500f): BooleanArray` | True where pitch slope is below threshold |
| `findSegments(pitchesHz, mask, minGapMs = 0f, hopMs = 10): List<IntRange>` | Contiguous segments where `mask` is true |

### Resampling

| Method | Description |
|--------|-------------|
| `resample(pitchesHz, confidences, sourceHopMs, targetHopMs): FloatArray` | Resample pitch sequence to a different hop rate |

## OctaveCorrectionConfig

Standalone config for `correctOctaveErrors`.

| Preset | Spurious jumps | Snap-back | Boundary |
|--------|----------------|-----------|----------|
| `FULL` (default) | on | on | on |
| `BASIC` | off | on | off |
| `OFF` | off | off | off |

| Property | Default |
|----------|---------|
| `removeSpuriousJumps` | `true` |
| `snapBackCorrection` | `true` |
| `snapBackThresholdCents` | `150` |
| `referencePitchHz` | `0` (auto) |
| `correctBoundaries` | `true` |
| `boundaryWindowMs` | `50` |
| `hopMs` | `10` |

## InterpolationMethod

```kotlin
enum class InterpolationMethod { LINEAR, EXPONENTIAL, LINEAR_HZ }
```

`LINEAR` interpolates in cents (log-frequency); `LINEAR_HZ` in linear Hz; `EXPONENTIAL` decays toward target.

## Common Pitfalls

1. **`smooth` and `medianFilter` require odd window/kernel size.** Even values throw `IllegalArgumentException` (per ADR-022).
2. **Unvoiced frames use -1, not 0.** All operations preserve the `-1` sentinel.
3. **`hopMs` matters for duration-based ops.** `removeBlips`, `interpolateSilence`, and the duration mask convert ms thresholds to frame counts via `hopMs`. Wrong hop → wrong durations.
4. **This is a batch facade.** For per-frame realtime processing, set `enableProcessing()` on `PitchDetectorConfig.Builder`.

## See also

- [PitchDetection](./pitch-detection) — produces contours
- [PitchAnalysis](./pitch-analysis) — runs on cleaned contours
- [Accura](../accura/overview) — wraps `PitchAnalysis` for intonation scoring
