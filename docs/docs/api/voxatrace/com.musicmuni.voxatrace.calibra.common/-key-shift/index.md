//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.common](../index.md)/[KeyShift](index.md)

# KeyShift

[common]\
object [KeyShift](index.md)

Key transposition utilities for pitch and HPCP data.

Ported from native/src/calibra/core/common/KeyShift.cpp for the &quot;Kotlin-max, C-min&quot; migration.

Provides functions to compute and apply key shifts between reference and student performances. Uses explicit key frequencies (Hz) instead of confusing semitone shift integers.

Example: Reference recorded at C4 (261.63 Hz) Student singing at D4 (293.66 Hz) Shift = 12 * log2(293.66 / 261.63) ~ 2.0 semitones -> Student pitch normalized DOWN by 2 semitones for comparison

## Functions

| Name | Summary |
|---|---|
| [computeShiftSemitones](compute-shift-semitones.md) | [common]<br/>fun [computeShiftSemitones](compute-shift-semitones.md)(referenceKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), studentKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Compute the semitone shift from reference to student key. |
| [normalizeHpcpToReferenceKey](normalize-hpcp-to-reference-key.md) | [common]<br/>fun [normalizeHpcpToReferenceKey](normalize-hpcp-to-reference-key.md)(studentHpcp: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)&gt;, referenceKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), studentKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), hpcpSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 12)<br/>Normalize student HPCP to reference key. |
| [normalizeToReferenceKey](normalize-to-reference-key.md) | [common]<br/>fun [normalizeToReferenceKey](normalize-to-reference-key.md)(studentPitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), referenceKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), studentKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Normalize student pitch to reference key. |
| [rotateHpcp](rotate-hpcp.md) | [common]<br/>fun [rotateHpcp](rotate-hpcp.md)(hpcpFrames: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)&gt;, semitones: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), hpcpSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 12)<br/>Rotate HPCP frames to compensate for key difference. |
| [shiftPitchCents](shift-pitch-cents.md) | [common]<br/>fun [shiftPitchCents](shift-pitch-cents.md)(pitchesCents: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), semitones: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), silenceValue: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = DEFAULT_SILENCE_CENTS)<br/>Shift pitch values in cents by a given number of semitones. |
| [shiftPitchHz](shift-pitch-hz.md) | [common]<br/>fun [shiftPitchHz](shift-pitch-hz.md)(pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), semitones: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Shift pitch values in Hz by a given number of semitones. |
| [shiftPitchHzCopy](shift-pitch-hz-copy.md) | [common]<br/>fun [shiftPitchHzCopy](shift-pitch-hz-copy.md)(pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), semitones: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Shift pitch values in Hz by a given number of semitones (returns new array). |
| [shiftPitchMidi](shift-pitch-midi.md) | [common]<br/>fun [shiftPitchMidi](shift-pitch-midi.md)(pitchesMidi: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), semitones: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), silenceValue: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = DEFAULT_SILENCE_MIDI)<br/>Shift pitch values in MIDI note numbers by a given number of semitones. |
