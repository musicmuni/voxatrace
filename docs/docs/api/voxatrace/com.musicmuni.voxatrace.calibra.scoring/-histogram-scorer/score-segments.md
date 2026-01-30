//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.scoring](../index.md)/[HistogramScorer](index.md)/[scoreSegments](score-segments.md)

# scoreSegments

[common]\
fun [scoreSegments](score-segments.md)(refPitch: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), stdPitch: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), refSegs: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html), stdSegs: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)

Score pitch segments using histogram comparison.

For each segment pair (reference vs student), computes pitch histograms and scores similarity using cosine distance. The final score is weighted by segment length.

#### Return

Score in range 0, 1, where 1 is perfect match

#### Parameters

common

| | |
|---|---|
| refPitch | Reference pitch contour (MIDI values) |
| stdPitch | Student pitch contour (MIDI values) |
| refSegs | Reference segment boundaries (indices into refPitch) |
| stdSegs | Student segment boundaries (indices into stdPitch) |
