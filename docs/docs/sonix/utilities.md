---
sidebar_position: 11
---

# Utilities

Helper classes for parsing music data files, converting between frame indices and time, and error handling.

## SonixParser

Parses pitch, note transcription, and transcription data from text files used in music education.

### Methods

| Method | Input | Returns | Description |
|--------|-------|---------|-------------|
| `parsePitchString` | `content: String` | `PitchData?` | Parse .pitchPP file content |
| `parseNotesString` | `content: String` | `NotesData?` | Parse .notes file content |
| `parseTransString` | `content: String` | `TransData?` | Parse .trans JSON file content |

### Parsing Pitch Data

Pitch files (`.pitchPP`) contain time-pitch pairs:

```text
0.0 440.0
0.01 442.5
0.02 -1.0
```

#### Kotlin

```kotlin
val content = File("melody.pitchPP").readText()
val pitchData = SonixParser.parsePitchString(content)
if (pitchData != null) {
    for (i in 0 until pitchData.count) {
        println("Time: ${pitchData.times[i]}, Pitch: ${pitchData.pitchesHz[i]}")
    }
}
```

#### Swift

```swift
let content = try String(contentsOf: pitchFileURL)
if let pitchData = SonixParser.parsePitchString(content: content) {
    print("Found \(pitchData.count) pitch points")
}
```

### Parsing Notes Data

Notes files (`.notes`) contain tab-separated note transcriptions:

```text
4.899410	5.224489	138.591315	Ṇ
4.980680	7.047256	145.986691	S
```

```kotlin
val notesData = SonixParser.parseNotesString(content)
```

```swift
if let notesData = SonixParser.parseNotesString(content: content) {
    print("Found \(notesData.count) notes")
}
```

### Parsing Transcription Data

Transcription files (`.trans`) use JSON format with segments:

```json
[{"id": 3, "lyrics": "S... R...", "time_stamp": [3.18, 14.18], "trans": [...]}]
```

```kotlin
val transData = SonixParser.parseTransString(jsonContent)
```

```swift
if let transData = SonixParser.parseTransString(content: jsonContent) {
    print("Found \(transData.segments.count) segments")
}
```

### Data Types

#### PitchData

| Property | Type | Description |
|----------|------|-------------|
| `times` | `FloatArray` | Timestamps in seconds |
| `pitchesHz` | `FloatArray` | Pitch values in Hz (-1.0 = unvoiced) |
| `count` | `Int` | Number of data points |

#### NotesData

| Property | Type | Description |
|----------|------|-------------|
| `startTimes` | `FloatArray` | Note start times in seconds |
| `endTimes` | `FloatArray` | Note end times in seconds |
| `frequencies` | `FloatArray` | Note frequencies in Hz |
| `labels` | `Array<String>` | Note labels (svara names) |

#### TransData

| Property | Type | Description |
|----------|------|-------------|
| `segments` | `List<TransSegment>` | Transcription segments |

## SonixFrames

Convert between audio frame indices and time values. Useful for aligning analysis results with audio timestamps.

### Methods

| Method | Parameters | Returns | Description |
|--------|-----------|---------|-------------|
| `framesToTime` | `frame: Int, sampleRate: Int, hopLength: Int` | `Double` | Frame index to seconds |
| `timeToFrames` | `time: Double, sampleRate: Int, hopLength: Int` | `Int` | Seconds to frame index |
| `framesToTime` | `frames: List<Int>, ...` | `List<Double>` | Batch frame-to-time |
| `timeToFrames` | `times: List<Double>, ...` | `List<Int>` | Batch time-to-frame |

### Usage

```kotlin
// Convert frame index to time
val timeSeconds = SonixFrames.framesToTime(
    frame = 100,
    sampleRate = 16000,
    hopLength = 160
)

// Convert time to frame index
val frameIndex = SonixFrames.timeToFrames(
    time = 1.5,
    sampleRate = 16000,
    hopLength = 160
)

// Batch conversion
val times = SonixFrames.framesToTime(
    frames = listOf(0, 10, 20, 30),
    sampleRate = 16000,
    hopLength = 160
)
```

### Parameters

| Parameter | Description | Typical Values |
|-----------|-------------|----------------|
| `sampleRate` | Audio sample rate in Hz | 16000, 44100 |
| `hopLength` | Hop length in samples | 160, 512 |

## SonixError

Error information reported by Sonix APIs via StateFlows. Used instead of throwing exceptions for reactive error handling.

### Properties

| Property | Type | Description |
|----------|------|-------------|
| `message` | `String` | Human-readable error description |
| `cause` | `Throwable?` | Original exception (if available) |

### Usage

```kotlin
player.error.collect { error ->
    error?.let {
        println("Error: ${it.message}")
        it.cause?.let { cause -> cause.printStackTrace() }
    }
}
```

```swift
let task = player.observeError { error in
    if let error = error {
        print("Error: \(error.message)")
    }
}
```

## Next Steps

- [Sonix Overview](./overview) — Full module overview
- [SonixDecoder](./decoder) — Decode audio for parsing
- [SonixPlayer](./player) — Play audio files
