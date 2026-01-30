//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraLiveEval](index.md)/[startPracticingSegment](start-practicing-segment.md)

# startPracticingSegment

[common]\
fun [startPracticingSegment](start-practicing-segment.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))

Start practicing a specific segment.

This is the main entry point for both singalong and singafter modes.

- 
   Singalong: seek → play → start recording immediately
- 
   Singafter: seek → play → wait for studentStartSeconds → start recording

If currently practicing another segment, that attempt is discarded.

#### Parameters

common

| | |
|---|---|
| index | Index of segment to practice |
