//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.feedback](../index.md)/[SvaraFeedbackGenerator](index.md)/[evaluateSvarasWeighted](evaluate-svaras-weighted.md)

# evaluateSvarasWeighted

[common]\
fun [evaluateSvarasWeighted](evaluate-svaras-weighted.md)(notes: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SvaraFeedbackGenerator.Note](-note/index.md)&gt;, pitches: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), nSvarasPerLoop: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SvaraFeedbackGenerator.SvaraResult](-svara-result/index.md)&gt;

Evaluates flat svaras using weighted threshold matching in MIDI space.

More sophisticated evaluation that:

- 
   Uses multiple MIDI thresholds (tight, medium, loose)
- 
   Applies duration-based weighting
- 
   Considers only a fraction of best samples

#### Return

Array of svara evaluation results

#### Parameters

common

| | |
|---|---|
| notes | Array of reference notes (uses freqMidi) |
| pitches | Student's pitch values in MIDI |
| nSvarasPerLoop | Number of svaras per loop (affects weighting) |
