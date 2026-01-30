//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixEncoder](index.md)

# SonixEncoder

object [SonixEncoder](index.md)

Encodes audio to compressed formats (M4A/AAC, MP3).

## What is Audio Encoding?

Raw PCM audio uses a lot of space (~10MB per minute at 44kHz stereo). Encoding **compresses** audio to efficient formats like M4A or MP3 for storage and sharing.

Use it when you need to:

- 
   Save recorded audio to a file
- 
   Export synthesized audio
- 
   Convert between audio formats

## When to Use

| Scenario | Use This? | Why |
|---|---|---|
| Save recording to file | Yes | Core use case |
| Export synthesized audio | Yes | After synthesis |
| Convert audio formats | Yes | Decode + encode |
| Record audio directly | No | Use `SonixRecorder` (encodes internally) |

## Quick Start

### Kotlin

```kotlin
// Encode from AudioRawData (e.g., after decoding or synthesis)
val rawData = SonixDecoder.decode("input.wav")
if (rawData != null) {
    val success = SonixEncoder.encode(
        data = rawData,
        outputPath = "/path/to/output.mp3",
        format = "mp3"
    )
}

// Encode from float samples
val success = SonixEncoder.encode(
    samples = floatArray,
    sampleRate = 44100,
    channels = 1,
    outputPath = "/path/to/output.m4a"
)

// Check format availability
if (SonixEncoder.isFormatAvailable("mp3")) {
    // MP3 encoding is supported
}
```

### Swift

```swift
// Encode from AudioRawData
if let rawData = SonixDecoder.companion.decode(path: "input.wav", targetSampleRate: nil) {
    let success = SonixEncoder.companion.encode(
        data: rawData,
        outputPath: "/path/to/output.mp3",
        format: "mp3",
        bitrateKbps: 128
    )
}

// Encode from float samples
let success = SonixEncoder.companion.encode(
    samples: floatArray,
    sampleRate: 44100,
    channels: 1,
    outputPath: "/path/to/output.m4a",
    format: "m4a",
    bitrateKbps: 128
)
```

## Supported Formats

| Format | Quality | Compatibility | Use Case |
|---|---|---|---|
| M4A/AAC | Best | iOS/Android native | Default choice |
| MP3 | Good | Universal | Maximum compatibility |

## Bitrate Guide

| Bitrate | Quality | File Size |
|---|---|---|
| 64 kbps | Low (voice) | ~0.5 MB/min |
| 128 kbps | Standard | ~1 MB/min |
| 192 kbps | High | ~1.5 MB/min |
| 256 kbps | Very high | ~2 MB/min |

## Platform Notes

### iOS

- 
   M4A/AAC uses AVFoundation (hardware accelerated)
- 
   MP3 uses LAME library

### Android

- 
   M4A/AAC uses MediaCodec (hardware accelerated)
- 
   MP3 uses LAME library

## Common Pitfalls

1. 
   **False return**: Encoding failed - check file permissions and path validity
2. 
   **Large memory usage**: Encoding processes entire audio in memory
3. 
   **Wrong sample format**: Float samples must be in -1.0, 1.0 range
4. 
   **Blocking call**: Runs synchronously; consider calling from background thread

#### See also

| | |
|---|---|
| [SonixDecoder](../-sonix-decoder/index.md) | For decoding audio files to raw PCM |
| [SonixRecorder](../-sonix-recorder/index.md) | For recording with automatic encoding |
| [AudioRawData](../../com.musicmuni.voxatrace.sonix.model/-audio-raw-data/index.md) | For the raw audio data structure |

## Functions

| Name | Summary |
|---|---|
| [encode](encode.md) | [common]<br/>fun [encode](encode.md)(data: [AudioRawData](../../com.musicmuni.voxatrace.sonix.model/-audio-raw-data/index.md), outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), format: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) = &quot;m4a&quot;, bitrateKbps: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 128): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Encode AudioRawData to a compressed audio file.<br/>[common]<br/>fun [encode](encode.md)(pcmData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), format: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) = &quot;m4a&quot;, bitrateKbps: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 128): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Encode PCM bytes to a compressed audio file.<br/>[common]<br/>fun [encode](encode.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), format: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) = &quot;m4a&quot;, bitrateKbps: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 128): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Encode float samples to a compressed audio file. |
| [isFormatAvailable](is-format-available.md) | [common]<br/>fun [isFormatAvailable](is-format-available.md)(format: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if a format is available on this platform. |
