//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraPitch](../index.md)/[Detector](index.md)/[duplicate](duplicate.md)

# duplicate

[common]\
abstract fun [duplicate](duplicate.md)(): [CalibraPitch.Detector](index.md)

Create a duplicate detector with the same configuration.

Useful for parallel feature extraction (e.g., extracting pitch from reference audio while the primary detector handles student audio).

The duplicate is independent - it has its own internal state and livePitchContour. Closing one does not affect the other.

#### Return

A new Detector instance with identical configuration
