---
sidebar_position: 2
---

# CalibraPitch (removed in 2.0.0)

:::danger Removed — migration required
`CalibraPitch` was **removed** in 2.0.0. There is no source-compat shell. 1.x callers must migrate imports and (for some methods) call shapes to the new tona facades below before they will compile against 2.0.
:::

## Where to find each piece

| Old API | New API |
|---------|---------|
| `CalibraPitch.createDetector(config, modelProvider)` | [`PitchDetection.createDetector(config, modelProvider)`](../tona/pitch-detection) |
| `CalibraPitch.createContourExtractor(config, modelProvider)` | [`PitchDetection.createContourExtractor(config, modelProvider)`](../tona/pitch-detection) |
| `CalibraPitch.PostProcess.cleanup(contour, options)` | [`PitchProcessing.process(contour, config)`](../tona/pitch-processing) |
| `CalibraPitch.PostProcess.fixOctaveErrors(contour)` | [`PitchProcessing.process(contour, config)`](../tona/pitch-processing) with `fixOctaveErrors = true` |
| `CalibraPitch.PostProcess.fixBoundaryOctaves(contour)` | [`PitchProcessing.process(contour, config)`](../tona/pitch-processing) with `fixBoundaryOctaves = true` |
| `CalibraPitch.PostProcess.removeBlips(contour, …)` | [`PitchProcessing.removeBlips(...)`](../tona/pitch-processing) or `process(contour, config)` |
| `CalibraPitch.PostProcess.smooth(...)` | [`PitchProcessing.smooth(...)`](../tona/pitch-processing) |
| `CalibraPitch.PostProcess.medianFilter(...)` | [`PitchProcessing.medianFilter(...)`](../tona/pitch-processing) |
| `CalibraPitch.PostProcess.correctOctaveErrors(...)` | [`PitchProcessing.correctOctaveErrors(...)`](../tona/pitch-processing) |
| `CalibraPitch.PostProcess.rejectOutliers(...)` | [`PitchProcessing.removeBlips(...)`](../tona/pitch-processing) (renamed) |
| `CalibraPitch.Detector` (nested type) | `com.musicmuni.voxatrace.tona.detection.PitchDetector` (interface) |
| `ContourExtractorConfig` | `com.musicmuni.voxatrace.tona.model.ContourExtractorConfig` (re-import from `tona.model`; no calibra-side alias) |
| `PitchPoint`, `PitchContour`, `Tuning`, `PitchAlgorithm`, `VoiceType`, `QuietHandling`, `DetectionStrictness`, `PitchPreset`, `PitchDetectorConfig`, `PitchProcessingConfig`, `OctaveCorrectionConfig`, `InterpolationMethod` | All under `com.musicmuni.voxatrace.tona.model` |

## Migration

```kotlin
// Before
import com.musicmuni.voxatrace.calibra.CalibraPitch
val detector = CalibraPitch.createDetector(PitchDetectorConfig.BALANCED)
val cleaned = CalibraPitch.PostProcess.cleanup(contour, ContourCleanup.SCORING)

// After
import com.musicmuni.voxatrace.tona.PitchDetection
import com.musicmuni.voxatrace.tona.PitchProcessing
import com.musicmuni.voxatrace.tona.model.PitchDetectorConfig
import com.musicmuni.voxatrace.tona.model.PitchProcessingConfig

val detector = PitchDetection.createDetector(PitchDetectorConfig.BALANCED)
val cleaned = PitchProcessing.process(contour, PitchProcessingConfig.SCORING)
```

Notable rename: there is no `ContourCleanup` enum in the new API. The cleanup field on `ContourExtractorConfig` is typed as `PitchProcessingConfig`; presets are `PitchProcessingConfig.RAW`, `SCORING`, `DISPLAY`.

For full reference, see:
- [Tona overview](../tona/overview)
- [PitchDetection](../tona/pitch-detection)
- [PitchProcessing](../tona/pitch-processing)
- [PitchAnalysis](../tona/pitch-analysis)
