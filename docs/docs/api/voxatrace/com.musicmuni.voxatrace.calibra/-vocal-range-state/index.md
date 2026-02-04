---
sidebar_label: "VocalRangeState"
---


# VocalRangeState

[common]\
data class [VocalRangeState](index.md)(val phase: [VocalRangePhase](../-vocal-range-phase/index.md) = VocalRangePhase.IDLE, val countdownSeconds: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0, val phaseMessage: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) = &quot;Ready to detect your vocal range&quot;, val currentPitch: [VocalPitch](../-vocal-pitch/index.md)? = null, val currentAmplitude: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, val stabilityProgress: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, val bestLowNote: [DetectedNote](../-detected-note/index.md)? = null, val bestHighNote: [DetectedNote](../-detected-note/index.md)? = null, val lowNote: [DetectedNote](../-detected-note/index.md)? = null, val highNote: [DetectedNote](../-detected-note/index.md)? = null, val result: [VocalRangeResult](../-vocal-range-result/index.md)? = null, val error: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null)

Observable state emitted during vocal range detection.

This state object contains everything needed to build the UI:

- 
   Current phase and progress
- 
   Real-time pitch for display
- 
   Stability progress toward detection
- 
   Best detected notes so far (updates as user finds lower/higher notes)
- 
   Final locked notes and results

## Constructors

| | |
|---|---|
| [VocalRangeState](-vocal-range-state.md) | [common]<br/>constructor(phase: [VocalRangePhase](../-vocal-range-phase/index.md) = VocalRangePhase.IDLE, countdownSeconds: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0, phaseMessage: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) = &quot;Ready to detect your vocal range&quot;, currentPitch: [VocalPitch](../-vocal-pitch/index.md)? = null, currentAmplitude: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, stabilityProgress: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, bestLowNote: [DetectedNote](../-detected-note/index.md)? = null, bestHighNote: [DetectedNote](../-detected-note/index.md)? = null, lowNote: [DetectedNote](../-detected-note/index.md)? = null, highNote: [DetectedNote](../-detected-note/index.md)? = null, result: [VocalRangeResult](../-vocal-range-result/index.md)? = null, error: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null) |

## Properties

| Name | Summary |
|---|---|
| [bestHighNote](best-high-note.md) | [common]<br/>val [bestHighNote](best-high-note.md): [DetectedNote](../-detected-note/index.md)? = null<br/>Best high note detected so far (highest stable note found) - updates as user explores |
| [bestLowNote](best-low-note.md) | [common]<br/>val [bestLowNote](best-low-note.md): [DetectedNote](../-detected-note/index.md)? = null<br/>Best low note detected so far (lowest stable note found) - updates as user explores |
| [countdownSeconds](countdown-seconds.md) | [common]<br/>val [countdownSeconds](countdown-seconds.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0 |
| [currentAmplitude](current-amplitude.md) | [common]<br/>val [currentAmplitude](current-amplitude.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f |
| [currentPitch](current-pitch.md) | [common]<br/>val [currentPitch](current-pitch.md): [VocalPitch](../-vocal-pitch/index.md)? = null |
| [error](error.md) | [common]<br/>val [error](error.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null |
| [highNote](high-note.md) | [common]<br/>val [highNote](high-note.md): [DetectedNote](../-detected-note/index.md)? = null<br/>Locked high note (confirmed by user) |
| [lowNote](low-note.md) | [common]<br/>val [lowNote](low-note.md): [DetectedNote](../-detected-note/index.md)? = null<br/>Locked low note (confirmed by user) |
| [phase](phase.md) | [common]<br/>val [phase](phase.md): [VocalRangePhase](../-vocal-range-phase/index.md) |
| [phaseMessage](phase-message.md) | [common]<br/>val [phaseMessage](phase-message.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [result](result.md) | [common]<br/>val [result](result.md): [VocalRangeResult](../-vocal-range-result/index.md)? = null |
| [stabilityProgress](stability-progress.md) | [common]<br/>val [stabilityProgress](stability-progress.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f |
