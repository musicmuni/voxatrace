//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.util](../index.md)/[Parser](index.md)/[parseNotesString](parse-notes-string.md)

# parseNotesString

[common]\
fun [parseNotesString](parse-notes-string.md)(content: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [NotesData](../../com.musicmuni.voxatrace.sonix.model/-notes-data/index.md)?

Parse notes data from a string (.notes format).

File format: tab-separated &quot;start_time end_time freq_hz label&quot; per line. Example:

```kotlin
4.899410	5.224489	138.591315	Ṇ
4.980680	7.047256	145.986691	S
```

#### Return

Parsed notes data, or null if parsing failed

#### Parameters

common

| | |
|---|---|
| content | String containing notes data |
