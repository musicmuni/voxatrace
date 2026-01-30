//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../index.md)/[PitchContour](index.md)/[slice](slice.md)

# slice

[common]\
fun [slice](slice.md)(startTime: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), endTime: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), relativeTimes: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true): [PitchContour](index.md)

Extract a slice of the contour within a time range.

#### Return

New PitchContour containing only samples within the time range

#### Parameters

common

| | |
|---|---|
| startTime | Start time in seconds (inclusive) |
| endTime | End time in seconds (inclusive) |
| relativeTimes | If true, adjust times to be relative to startTime (0-based) |
