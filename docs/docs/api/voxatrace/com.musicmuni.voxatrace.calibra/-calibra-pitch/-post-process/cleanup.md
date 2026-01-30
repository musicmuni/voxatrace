//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraPitch](../index.md)/[PostProcess](index.md)/[cleanup](cleanup.md)

# cleanup

[common]\
fun [cleanup](cleanup.md)(contour: [PitchContour](../../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md), options: [ContourCleanup](../../../com.musicmuni.voxatrace.calibra.model/-contour-cleanup/index.md), hopMs: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 10): [PitchContour](../../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md)

Apply cleanup options to a pitch contour.

Processing order:

1. 
   fixOctaveErrors - Mid-phrase frame-to-frame octave jumps
2. 
   fixBoundaryOctaves - Onset/offset octave errors
3. 
   removeBlips - Short runs below minimumNoteDurationMs
4. 
   smoothPitch - Optional pitch smoothing

#### Return

Cleaned pitch contour

#### Parameters

common

| | |
|---|---|
| contour | Input pitch contour |
| options | Cleanup options (use ContourCleanup.SCORING, .DISPLAY, or .RAW) |
| hopMs | Hop size between frames in milliseconds (default: 10) |
