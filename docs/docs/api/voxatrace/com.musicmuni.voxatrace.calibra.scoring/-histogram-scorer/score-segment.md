//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.scoring](../index.md)/[HistogramScorer](index.md)/[scoreSegment](score-segment.md)

# scoreSegment

[common]\
fun [scoreSegment](score-segment.md)(refPitch: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), stdPitch: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)

Score a single segment pair using histogram comparison.

#### Return

Score in range 0, 1, where 1 is perfect match

#### Parameters

common

| | |
|---|---|
| refPitch | Reference pitch values (MIDI) |
| stdPitch | Student pitch values (MIDI) |
