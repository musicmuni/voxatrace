//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.scoring](../index.md)/[HistogramScorer](index.md)

# HistogramScorer

[common]\
object [HistogramScorer](index.md)

Histogram-based pitch segment scoring.

Ported from native/src/calibra/core/evaluation/HistogramScorer.cpp for the &quot;Kotlin-max, C-min&quot; migration.

Compares reference and student pitch contours by building histograms of pitch values within each segment and measuring similarity via cosine distance.

## Functions

| Name | Summary |
|---|---|
| [scoreSegment](score-segment.md) | [common]<br/>fun [scoreSegment](score-segment.md)(refPitch: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), stdPitch: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Score a single segment pair using histogram comparison. |
| [scoreSegments](score-segments.md) | [common]<br/>fun [scoreSegments](score-segments.md)(refPitch: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), stdPitch: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), refSegs: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html), stdSegs: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Score pitch segments using histogram comparison. |
