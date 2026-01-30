//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../index.md)/[PitchPoint](index.md)

# PitchPoint

[common]\
data class [PitchPoint](index.md)(val pitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val confidence: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val timeSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f)

A pitch detection result or timestamped pitch sample.

Used for both single-frame detection output and as elements in pitch contours.

## Constructors

| | |
|---|---|
| [PitchPoint](-pitch-point.md) | [common]<br/>constructor(pitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), confidence: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), timeSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |
| [Tuning](-tuning/index.md) | [common]<br/>enum [Tuning](-tuning/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[PitchPoint.Tuning](-tuning/index.md)&gt; <br/>Tuning status relative to nearest musical note |

## Properties

| Name | Summary |
|---|---|
| [centsOff](cents-off.md) | [common]<br/>val [centsOff](cents-off.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Cents deviation from nearest note (-50 to +50). Returns 0 if not singing. |
| [confidence](confidence.md) | [common]<br/>val [confidence](confidence.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Detection confidence (0.0-1.0), derived from YIN aperiodicity |
| [isSinging](is-singing.md) | [common]<br/>val [isSinging](is-singing.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>True if singing/voiced audio was detected |
| [isVoiced](is-voiced.md) | [common]<br/>val [isVoiced](is-voiced.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [midiNote](midi-note.md) | [common]<br/>val [midiNote](midi-note.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>MIDI note number (e.g., 69 for A4). Returns -1 if not singing. |
| [note](note.md) | [common]<br/>val [note](note.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)?<br/>Musical note name (e.g., &quot;A4&quot;, &quot;C#5&quot;). Returns null if not singing. |
| [pitch](pitch.md) | [common]<br/>val [pitch](pitch.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Detected pitch in Hz, or -1.0 if no pitch detected |
| [reliability](reliability.md) | [common]<br/>val [reliability](reliability.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Detection reliability (0.0-1.0). Higher = more confident. |
| [timeSeconds](time-seconds.md) | [common]<br/>val [timeSeconds](time-seconds.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f<br/>Timestamp in seconds (0 when not relevant, e.g., single-frame detection) |
| [tuning](tuning.md) | [common]<br/>val [tuning](tuning.md): [PitchPoint.Tuning](-tuning/index.md)<br/>Tuning status relative to nearest note |
