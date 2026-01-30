//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.evaluation](../index.md)/[NoteEvaluator](index.md)/[evaluate](evaluate.md)

# evaluate

[common]\
fun [evaluate](evaluate.md)(refFreqsHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), refDurationsMs: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html), studentTimes: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), studentPitches: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), notesPerLoop: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;&lt;[Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[FeedbackSegment](../-feedback-segment/index.md)&gt;&gt;

Evaluate a note/svara exercise performance.

#### Return

Pair of overall score and per-note feedback

#### Parameters

common

| | |
|---|---|
| refFreqsHz | Reference frequencies in Hz for each note |
| refDurationsMs | Reference durations in milliseconds for each note |
| studentTimes | Student pitch timestamps in seconds |
| studentPitches | Student pitch values in Hz |
| notesPerLoop | Number of notes per loop (for repeating patterns) |
