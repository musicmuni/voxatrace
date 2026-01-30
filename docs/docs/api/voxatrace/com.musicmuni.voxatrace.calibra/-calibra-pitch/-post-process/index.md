//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraPitch](../index.md)/[PostProcess](index.md)

# PostProcess

[common]\
object [PostProcess](index.md)

Post-processing utilities for pitch arrays.

Use this for processing entire pitch contours (e.g., from recorded audio). For realtime processing, use [Detector](../-detector/index.md) with `enableProcessing = true`.

Usage:

```kotlin
val rawPitches = floatArrayOf(440f, 442f, 880f, 438f) // Has octave error

// Full processing
val processed = CalibraPitch.PostProcess.process(rawPitches)

// Individual operations
val smoothed = CalibraPitch.PostProcess.smooth(rawPitches)
val corrected = CalibraPitch.PostProcess.correctOctaveErrors(rawPitches)
val filtered = CalibraPitch.PostProcess.medianFilter(rawPitches)
```

## Functions

| Name | Summary |
|---|---|
| [cleanup](cleanup.md) | [common]<br/>fun [cleanup](cleanup.md)(contour: [PitchContour](../../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md), options: [ContourCleanup](../../../com.musicmuni.voxatrace.calibra.model/-contour-cleanup/index.md), hopMs: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 10): [PitchContour](../../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md)<br/>Apply cleanup options to a pitch contour. |
| [correctBoundaryOctaves](correct-boundary-octaves.md) | [common]<br/>fun [correctBoundaryOctaves](correct-boundary-octaves.md)(pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), hopMs: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 10, boundaryWindowMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 50.0f): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Correct octave errors at phrase boundaries (onset/offset). |
| [correctOctaveErrors](correct-octave-errors.md) | [common]<br/>fun [correctOctaveErrors](correct-octave-errors.md)(pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), thresholdCents: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 500.0f, referencePitchHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Apply octave error correction only. |
| [fixBoundaryOctaves](fix-boundary-octaves.md) | [common]<br/>fun [fixBoundaryOctaves](fix-boundary-octaves.md)(contour: [PitchContour](../../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md), boundaryWindowMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 50.0f): [PitchContour](../../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md)<br/>Fix boundary octave errors in a pitch contour. |
| [fixOctaveErrors](fix-octave-errors.md) | [common]<br/>fun [fixOctaveErrors](fix-octave-errors.md)(contour: [PitchContour](../../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md)): [PitchContour](../../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md)<br/>Fix octave errors in a pitch contour. |
| [medianFilter](median-filter.md) | [common]<br/>fun [medianFilter](median-filter.md)(pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), kernelSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 5): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Apply median filter for spike removal. |
| [process](process.md) | [common]<br/>fun [process](process.md)(pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Process a pitch contour with smoothing and octave correction using default settings.<br/>[common]<br/>fun [process](process.md)(pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), smoothingWindowSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 7, octaveThresholdCents: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 500.0f, enableSmoothing: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true, enableOctaveCorrection: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Process a pitch contour with configurable smoothing and octave correction. |
| [rejectOutliers](reject-outliers.md) | [common]<br/>fun [rejectOutliers](reject-outliers.md)(pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), hopMs: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 10, minDurationMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 80.0f): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Reject short pitch runs (blips) based on minimum duration. |
| [removeBlips](remove-blips.md) | [common]<br/>fun [removeBlips](remove-blips.md)(contour: [PitchContour](../../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md), minDurationMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 80.0f): [PitchContour](../../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md)<br/>Remove short pitch runs (blips) from a contour. |
| [smooth](smooth.md) | [common]<br/>fun [smooth](smooth.md)(contour: [PitchContour](../../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md)): [PitchContour](../../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md)<br/>Apply smoothing to a pitch contour.<br/>[common]<br/>fun [smooth](smooth.md)(pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), windowSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 7): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Apply smoothing filter only. |
