//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraBreath](index.md)/[computeMetrics](compute-metrics.md)

# computeMetrics

[common]\
fun [computeMetrics](compute-metrics.md)(refTimes: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), refPitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), studentTimes: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), studentPitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), feedbackSegmentIndices: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html), feedbackStartTimes: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), feedbackEndTimes: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), refSegmentStarts: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), refSegmentEnds: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [BreathMetrics](../../com.musicmuni.voxatrace.calibra.model/-breath-metrics/index.md)

Compute comprehensive breath metrics comparing student to reference.

This method compares the student's breathing patterns against a reference performance to assess breath control quality.

#### Return

BreathMetrics with capacity, control, and validity

#### Parameters

common

| | |
|---|---|
| refTimes | Reference pitch timestamps in seconds |
| refPitchesHz | Reference pitches in Hz |
| studentTimes | Student's pitch timestamps in seconds |
| studentPitchesHz | Student's pitches in Hz |
| feedbackSegmentIndices | Indices of feedback segments |
| feedbackStartTimes | Start times of feedback segments |
| feedbackEndTimes | End times of feedback segments |
| refSegmentStarts | Reference segment start times |
| refSegmentEnds | Reference segment end times |
