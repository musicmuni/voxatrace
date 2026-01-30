//[voxatrace](../../index.md)/[com.musicmuni.voxatrace.calibra.model](index.md)/[toContour](to-contour.md)

# toContour

[common]\
fun [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[PitchPoint](-pitch-point/index.md)&gt;.[toContour](to-contour.md)(sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000): [PitchContour](-pitch-contour/index.md)

Convert a list of PitchPoints to a PitchContour.

Convenience extension for building contours from accumulated samples.

#### Return

PitchContour containing the points

#### Parameters

common

| | |
|---|---|
| sampleRate | Audio sample rate (default 16kHz) |
