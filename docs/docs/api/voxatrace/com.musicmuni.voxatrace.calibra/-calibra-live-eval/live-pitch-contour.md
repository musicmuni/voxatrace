//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraLiveEval](index.md)/[livePitchContour](live-pitch-contour.md)

# livePitchContour

[common]\
val [livePitchContour](live-pitch-contour.md): StateFlow&lt;[PitchContour](../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md)&gt;

Live pitch contour accumulated during the current segment.

This is forwarded from the LiveEvaluator (which gets it from the detector), the single source of truth for pitch detection. The contour provides access to the growing pitch with timestamps for real-time visualization.

The contour is reset when a new segment begins and updated every audio frame. Use this for scrolling/canvas visualizations. For simple pitch displays, use [state](state.md)`.currentPitch` instead.
