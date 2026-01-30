//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixDecoder](index.md)

# SonixDecoder

object [SonixDecoder](index.md)

Decodes audio files to raw PCM data.

## What is Audio Decoding?

Audio files (MP3, M4A, etc.) are **compressed** - they store audio efficiently but can't be processed directly. Decoding converts compressed audio to **raw PCM samples** that Calibra APIs can analyze.

## When to Use

| Scenario | Use This? | Why |
|---|---|---|
| Load audio file for pitch detection | Yes | Calibra needs raw PCM |
| Load audio for melody evaluation | Yes | Core use case |
| Play audio file | No | Use `SonixPlayer` (decodes internally) |
| Record audio | No | Use `SonixRecorder` |

## Quick Start

### Kotlin

```kotlin
// Decode to 16kHz (default) for use with Calibra
val audioData = SonixDecoder.decode("/path/to/audio.mp3")
if (audioData != null) {
    println("Sample rate: ${audioData.sampleRate}")  // Always 16000
    println("Channels: ${audioData.numChannels}")
    println("Duration: ${audioData.durationMilliSecs}ms")

    // Use with Calibra APIs (expects 16kHz)
    val samples = audioData.samples
}

// Decode at native sample rate (no resampling)
val nativeAudio = SonixDecoder.decode("/path/to/audio.mp3", targetSampleRate = null)
```

### Swift

```swift
// Decode to 16kHz (default) for use with Calibra
if let audioData = SonixDecoder.companion.decode(path: "/path/to/audio.mp3", targetSampleRate: 16000) {
    print("Sample rate: \(audioData.sampleRate)")  // Always 16000
    print("Channels: \(audioData.numChannels)")
    print("Duration: \(audioData.durationMilliSecs)ms")

    // Use with Calibra APIs
    let samples = audioData.samples
}

// Decode at native sample rate
let nativeAudio = SonixDecoder.companion.decodeNative(path: "/path/to/audio.mp3")
```

## Supported Formats

| Format | Android | iOS |
|---|---|---|
| WAV | Yes | Yes |
| MP3 | Yes | Yes |
| M4A/AAC | Yes | Yes |
| OGG | Yes | No |
| FLAC | Yes | Yes |

## Platform Notes

### iOS

- 
   Uses AVFoundation for hardware-accelerated decoding
- 
   Supports any format supported by Core Audio

### Android

- 
   Uses MediaExtractor and MediaCodec for hardware decoding
- 
   Falls back to software decoding if needed

## Common Pitfalls

1. 
   **Null return**: Check for null - decoding fails if file doesn't exist or format unsupported
2. 
   **Wrong sample rate for Calibra**: Use default (16kHz) or explicitly set targetSampleRate=16000
3. 
   **Memory for large files**: Decoding loads entire audio into memory
4. 
   **Relative paths**: Use absolute file paths

#### See also

| | |
|---|---|
| [AudioRawData](../../com.musicmuni.voxatrace.sonix.model/-audio-raw-data/index.md) | For the decoded audio data structure |
| [SonixEncoder](../-sonix-encoder/index.md) | For encoding raw audio back to compressed formats |
| [SonixResampler](../-sonix-resampler/index.md) | For explicit resampling control |
| CalibraPitch | For analyzing decoded audio |

## Functions

| Name | Summary |
|---|---|
| [decode](decode.md) | [common]<br/>fun [decode](decode.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), targetSampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)? = 16000): [AudioRawData](../../com.musicmuni.voxatrace.sonix.model/-audio-raw-data/index.md)?<br/>Decodes an audio file to raw PCM data. |
| [decodeNative](decode-native.md) | [common]<br/>fun [decodeNative](decode-native.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [AudioRawData](../../com.musicmuni.voxatrace.sonix.model/-audio-raw-data/index.md)?<br/>Decodes an audio file at its native sample rate (no resampling). |
