//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.util](../index.md)/[Parser](index.md)/[parsePitchString](parse-pitch-string.md)

# parsePitchString

[common]\
fun [parsePitchString](parse-pitch-string.md)(content: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [PitchData](../../com.musicmuni.voxatrace.sonix.model/-pitch-data/index.md)?

Parse pitch data from a string (.pitchPP format).

File format: whitespace-separated &quot;time_seconds pitch_hz&quot; per line. Example:

```kotlin
0.0 440.0
0.01 442.5
0.02 -1.0
```

#### Return

Parsed pitch data, or null if parsing failed

#### Parameters

common

| | |
|---|---|
| content | String containing pitch data |
