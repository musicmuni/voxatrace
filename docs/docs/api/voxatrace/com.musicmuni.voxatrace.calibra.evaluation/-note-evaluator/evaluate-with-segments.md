//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.evaluation](../index.md)/[NoteEvaluator](index.md)/[evaluateWithSegments](evaluate-with-segments.md)

# evaluateWithSegments

[common]\
fun [evaluateWithSegments](evaluate-with-segments.md)(notes: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SvaraFeedbackGenerator.Note](../../com.musicmuni.voxatrace.calibra.feedback/-svara-feedback-generator/-note/index.md)&gt;, studentTimes: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), studentPitchesMidi: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): &lt;Error class: unknown class&gt;&lt;[Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SvaraFeedbackGenerator.SvaraResult](../../com.musicmuni.voxatrace.calibra.feedback/-svara-feedback-generator/-svara-result/index.md)&gt;&gt;

Evaluate notes with pre-computed segment indices.

#### Return

Pair of overall score and array of results

#### Parameters

common

| | |
|---|---|
| notes | Array of pre-populated notes with segment indices |
| studentTimes | Student pitch timestamps |
| studentPitchesMidi | Student pitch values in MIDI |
