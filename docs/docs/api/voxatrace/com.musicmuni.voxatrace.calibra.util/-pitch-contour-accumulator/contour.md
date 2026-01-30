//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.util](../index.md)/[PitchContourAccumulator](index.md)/[contour](contour.md)

# contour

[common]\
val [contour](contour.md): StateFlow&lt;[PitchContour](../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md)&gt;

The accumulated pitch contour as a StateFlow.

Updates every time a new point is added. Suitable for:

- 
   Real-time visualization (scrolling pitch display)
- 
   UI binding via collect/observe
