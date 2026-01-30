//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[VocalPitch](index.md)

# VocalPitch

[common]\
data class [VocalPitch](index.md)(val frequencyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val midiNote: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val noteLabel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val confidence: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f)

A detected vocal pitch with quality metrics.

## Constructors

| | |
|---|---|
| [VocalPitch](-vocal-pitch.md) | [common]<br/>constructor(frequencyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), midiNote: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), noteLabel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), confidence: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f) |

## Properties

| Name | Summary |
|---|---|
| [confidence](confidence.md) | [common]<br/>val [confidence](confidence.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f<br/>Detection confidence (0.0-1.0) |
| [frequencyHz](frequency-hz.md) | [common]<br/>val [frequencyHz](frequency-hz.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Frequency in Hz |
| [midiNote](midi-note.md) | [common]<br/>val [midiNote](midi-note.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>MIDI note number (0-127) |
| [noteLabel](note-label.md) | [common]<br/>val [noteLabel](note-label.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Note name with octave (e.g., &quot;C4&quot;, &quot;F#3&quot;) |
