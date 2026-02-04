---
sidebar_label: "ActiveSegmentState"
---


# ActiveSegmentState

[common]\
data class [ActiveSegmentState](index.md)(val segmentIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val segment: [Segment](../-segment/index.md), val currentPitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val currentAmplitude: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val elapsedSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val isCapturing: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html))

State of the currently active segment during practice.

## Constructors

| | |
|---|---|
| [ActiveSegmentState](-active-segment-state.md) | [common]<br/>constructor(segmentIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), segment: [Segment](../-segment/index.md), currentPitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), currentAmplitude: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), elapsedSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), isCapturing: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [currentAmplitude](current-amplitude.md) | [common]<br/>val [currentAmplitude](current-amplitude.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Current audio amplitude (0.0 - 1.0) |
| [currentPitch](current-pitch.md) | [common]<br/>val [currentPitch](current-pitch.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Current detected pitch in Hz (-1 for unvoiced) |
| [elapsedSeconds](elapsed-seconds.md) | [common]<br/>val [elapsedSeconds](elapsed-seconds.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Time elapsed since segment started |
| [hasVoice](has-voice.md) | [common]<br/>val [hasVoice](has-voice.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>True if detected pitch is valid |
| [isCapturing](is-capturing.md) | [common]<br/>val [isCapturing](is-capturing.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Whether audio is currently being captured |
| [progress](progress.md) | [common]<br/>val [progress](progress.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Progress through the segment (0.0 - 1.0) |
| [remainingSeconds](remaining-seconds.md) | [common]<br/>val [remainingSeconds](remaining-seconds.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Time remaining in seconds |
| [segment](segment.md) | [common]<br/>val [segment](segment.md): [Segment](../-segment/index.md)<br/>The segment being practiced |
| [segmentIndex](segment-index.md) | [common]<br/>val [segmentIndex](segment-index.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Index of the segment |
