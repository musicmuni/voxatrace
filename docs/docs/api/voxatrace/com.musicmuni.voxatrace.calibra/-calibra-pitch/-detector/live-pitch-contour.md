//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraPitch](../index.md)/[Detector](index.md)/[livePitchContour](live-pitch-contour.md)

# livePitchContour

[common]\
abstract val [livePitchContour](live-pitch-contour.md): StateFlow&lt;[PitchContour](../../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md)&gt;

Live pitch contour accumulated during recording.

This is the single source of truth for real-time pitch visualization. Updates every time [detect](detect.md) is called with a valid frame.

Use [setContourMaxDuration](set-contour-max-duration.md) to set the window size (typically segment duration). Use [clearPitchContour](clear-pitch-contour.md) when starting a new recording segment.

Example Swift usage:

```swift
detector.observeLivePitchContour { contour in
    // Update scrolling pitch display
    self.pitchContour = contour
}
```
