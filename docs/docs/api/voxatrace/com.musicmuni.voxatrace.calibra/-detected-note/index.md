//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[DetectedNote](index.md)

# DetectedNote

[common]\
data class [DetectedNote](index.md)(val pitch: [VocalPitch](../-vocal-pitch/index.md), val durationSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val isStable: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html))

A detected stable note during range analysis.

## Constructors

| | |
|---|---|
| [DetectedNote](-detected-note.md) | [common]<br/>constructor(pitch: [VocalPitch](../-vocal-pitch/index.md), durationSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), isStable: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [durationSeconds](duration-seconds.md) | [common]<br/>val [durationSeconds](duration-seconds.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>How long this note was held stably |
| [isStable](is-stable.md) | [common]<br/>val [isStable](is-stable.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Whether the note met stability criteria |
| [pitch](pitch.md) | [common]<br/>val [pitch](pitch.md): [VocalPitch](../-vocal-pitch/index.md)<br/>The detected pitch |
