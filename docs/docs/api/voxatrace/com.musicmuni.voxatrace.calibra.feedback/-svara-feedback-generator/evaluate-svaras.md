//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.feedback](../index.md)/[SvaraFeedbackGenerator](index.md)/[evaluateSvaras](evaluate-svaras.md)

# evaluateSvaras

[common]\
fun [evaluateSvaras](evaluate-svaras.md)(notes: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SvaraFeedbackGenerator.Note](-note/index.md)&gt;, pitches: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), leewaySamples: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SvaraFeedbackGenerator.SvaraResult](-svara-result/index.md)&gt;

Evaluates flat svaras using basic threshold matching in MIDI space.

#### Return

Array of svara evaluation results

#### Parameters

common

| | |
|---|---|
| notes | Array of reference notes (uses freqMidi) |
| pitches | Student's pitch values in MIDI |
| leewaySamples | Number of samples to skip at segment boundaries |
