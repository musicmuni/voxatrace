---
sidebar_label: "AudioDecoder"
---


# AudioDecoder

expect object [AudioDecoder](index.md)actual object [AudioDecoder](index.md)actual object [AudioDecoder](index.md)

Decodes audio files to raw PCM data.

Supports common audio formats including WAV, MP3, M4A, AAC, and other platform-supported formats.

## Usage

```kotlin
val audioData = AudioDecoder.decode("/path/to/audio.mp3")
if (audioData != null) {
    println("Sample rate: ${audioData.sampleRate}")
    println("Channels: ${audioData.channels}")
    println("Duration: ${audioData.durationMs}ms")

    // Use raw PCM data
    val pcmBytes = audioData.data
}
```

## Platform Implementation

- 
   **Android**: Uses MediaExtractor and MediaCodec for hardware-accelerated decoding
- 
   **iOS**: Uses AVFoundation for decoding

#### See also

| | |
|---|---|
| [AudioRawData](../../com.musicmuni.voxatrace.sonix.model/-audio-raw-data/index.md) | For the decoded audio data structure |

## Functions

| Name | Summary |
|---|---|
| [decode](decode.md) | [common]<br/>expect fun [decode](decode.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [AudioRawData](../../com.musicmuni.voxatrace.sonix.model/-audio-raw-data/index.md)?<br/>Decodes an audio file to raw PCM data.<br/>[android, ios]<br/>[android]<br/>actual fun [decode](decode.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [AudioRawData](../../com.musicmuni.voxatrace.sonix.model/-audio-raw-data/index.md)?<br/>[ios]<br/>actual fun [decode](decode.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): AudioRawData? |
