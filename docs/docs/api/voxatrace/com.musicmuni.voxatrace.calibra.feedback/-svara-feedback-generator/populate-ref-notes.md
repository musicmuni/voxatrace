//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.feedback](../index.md)/[SvaraFeedbackGenerator](index.md)/[populateRefNotes](populate-ref-notes.md)

# populateRefNotes

[common]\
fun [populateRefNotes](populate-ref-notes.md)(refFreqsHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), refDurationsMs: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html), refLabels: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt;? = null): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SvaraFeedbackGenerator.Note](-note/index.md)&gt;

Populates reference note segments from input arrays.

#### Return

Array of populated notes

#### Parameters

common

| | |
|---|---|
| refFreqsHz | Reference svara frequencies in Hz |
| refDurationsMs | Reference svara durations in milliseconds |
| refLabels | Reference svara labels (optional) |
