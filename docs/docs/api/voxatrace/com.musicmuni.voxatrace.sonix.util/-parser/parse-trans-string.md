//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.util](../index.md)/[Parser](index.md)/[parseTransString](parse-trans-string.md)

# parseTransString

[common]\
fun [parseTransString](parse-trans-string.md)(content: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [TransData](../../com.musicmuni.voxatrace.sonix.model/-trans-data/index.md)?

Parse transcription data from a string (.trans JSON format).

JSON format: array of segments, each with:

- 
   id: segment identifier
- 
   lyrics: text lyrics
- 
   time_stamp: start, end timestamps in seconds
- 
   trans: array of notes with t_start, t_end, freqHz, label

Example:

```json
[{"id": 3, "lyrics": "S... R...", "time_stamp": [3.18, 14.18], "trans": [...]}]
```

#### Return

Parsed transcription data, or null if parsing failed

#### Parameters

common

| | |
|---|---|
| content | String containing JSON transcription data |
