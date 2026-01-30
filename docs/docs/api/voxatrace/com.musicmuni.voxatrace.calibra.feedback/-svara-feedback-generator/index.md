//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.feedback](../index.md)/[SvaraFeedbackGenerator](index.md)

# SvaraFeedbackGenerator

[common]\
object [SvaraFeedbackGenerator](index.md)

Feedback generation for svara (note) evaluation in Indian classical music.

Ported from native/src/calibra/core/feedback/SvaraFeedbackGenerator.cpp for the &quot;Kotlin-max, C-min&quot; migration.

Contains svara-level evaluation logic for flat note exercises.

## Types

| Name | Summary |
|---|---|
| [Note](-note/index.md) | [common]<br/>data class [Note](-note/index.md)(var freqHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, var freqMidi: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, var tStart: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, var tEnd: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, var svarSymbol: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) = &quot;Sa&quot;, var indStartPitch: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = -1, var indEndPitch: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = -1)<br/>Represents a single note/svara in the reference. |
| [SvaraResult](-svara-result/index.md) | [common]<br/>data class [SvaraResult](-svara-result/index.md)(var score: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, var level: [PerformanceLevel](../../com.musicmuni.voxatrace.calibra.model/-performance-level/index.md) = PerformanceLevel.NOT_EVALUATED, var svarSymbol: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) = &quot;&quot;, var tStart: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, var tEnd: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f)<br/>Result of evaluating a single svara segment. |

## Properties

| Name | Summary |
|---|---|
| [COULD_NOT_EVAL](-c-o-u-l-d_-n-o-t_-e-v-a-l.md) | [common]<br/>const val [COULD_NOT_EVAL](-c-o-u-l-d_-n-o-t_-e-v-a-l.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Value indicating evaluation could not be performed |

## Functions

| Name | Summary |
|---|---|
| [computeGlobalScore](compute-global-score.md) | [common]<br/>fun [computeGlobalScore](compute-global-score.md)(results: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SvaraFeedbackGenerator.SvaraResult](-svara-result/index.md)&gt;): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Computes the global score from svara results. |
| [evaluateSvaras](evaluate-svaras.md) | [common]<br/>fun [evaluateSvaras](evaluate-svaras.md)(notes: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SvaraFeedbackGenerator.Note](-note/index.md)&gt;, pitches: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), leewaySamples: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SvaraFeedbackGenerator.SvaraResult](-svara-result/index.md)&gt;<br/>Evaluates flat svaras using basic threshold matching in MIDI space. |
| [evaluateSvarasWeighted](evaluate-svaras-weighted.md) | [common]<br/>fun [evaluateSvarasWeighted](evaluate-svaras-weighted.md)(notes: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SvaraFeedbackGenerator.Note](-note/index.md)&gt;, pitches: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), nSvarasPerLoop: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SvaraFeedbackGenerator.SvaraResult](-svara-result/index.md)&gt;<br/>Evaluates flat svaras using weighted threshold matching in MIDI space. |
| [getIndicesSvarSegments](get-indices-svar-segments.md) | [common]<br/>fun [getIndicesSvarSegments](get-indices-svar-segments.md)(notes: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SvaraFeedbackGenerator.Note](-note/index.md)&gt;, timestamps: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html))<br/>Maps note segments to indices in the pitch sequence by timestamp. |
| [populateRefNotes](populate-ref-notes.md) | [common]<br/>fun [populateRefNotes](populate-ref-notes.md)(refFreqsHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), refDurationsMs: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html), refLabels: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt;? = null): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[SvaraFeedbackGenerator.Note](-note/index.md)&gt;<br/>Populates reference note segments from input arrays. |
