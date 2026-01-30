//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.util](../index.md)/[Parser](index.md)

# Parser

[common]\
object [Parser](index.md)

Parser for pitch, notes, and transcription data files.

These files are used in music education apps to store:

- 
   Pitch contours (.pitchPP) - time-pitch pairs
- 
   Note transcriptions (.notes) - tab-separated start, end, frequency, label
- 
   JSON transcriptions (.trans) - JSON format with segments and notes

## Usage

```kotlin
// Parse a pitch file
val pitchData = Parser.parsePitchString(fileContent)
if (pitchData != null) {
    for (i in 0 until pitchData.count) {
        println("Time: ${pitchData.times[i]}, Pitch: ${pitchData.pitchesHz[i]}")
    }
}

// Parse a notes file (.notes format)
val notesData = Parser.parseNotesString(fileContent)

// Parse a trans file (.trans JSON format)
val transData = Parser.parseTransString(fileContent)
```

## Functions

| Name | Summary |
|---|---|
| [parseNotesString](parse-notes-string.md) | [common]<br/>fun [parseNotesString](parse-notes-string.md)(content: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [NotesData](../../com.musicmuni.voxatrace.sonix.model/-notes-data/index.md)?<br/>Parse notes data from a string (.notes format). |
| [parsePitchString](parse-pitch-string.md) | [common]<br/>fun [parsePitchString](parse-pitch-string.md)(content: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [PitchData](../../com.musicmuni.voxatrace.sonix.model/-pitch-data/index.md)?<br/>Parse pitch data from a string (.pitchPP format). |
| [parseTransString](parse-trans-string.md) | [common]<br/>fun [parseTransString](parse-trans-string.md)(content: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [TransData](../../com.musicmuni.voxatrace.sonix.model/-trans-data/index.md)?<br/>Parse transcription data from a string (.trans JSON format). |
