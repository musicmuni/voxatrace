---
sidebar_position: 6
---

# SonixDecoder

Decode audio files (MP3, M4A, WAV, etc.) to raw PCM data for analysis and processing.

## Quick Start

### Kotlin

```kotlin
val audioData = SonixDecoder.decode("/path/to/audio.mp3")
if (audioData != null) {
    println("Sample rate: ${audioData.sampleRate}")
    println("Duration: ${audioData.durationMilliSecs}ms")
    println("Channels: ${audioData.numChannels}")

    val samples: FloatArray = audioData.samples
}
```

### Swift

```swift
if let audioData = SonixDecoder.decode(path: "/path/to/audio.mp3") {
    print("Sample rate: \(audioData.sampleRate)")
    print("Duration: \(audioData.durationMilliSecs)ms")
    print("Channels: \(audioData.numChannels)")

    let samples: [Float] = audioData.samples
}
```

## Methods

| Method | Parameters | Returns | Description |
|--------|-----------|---------|-------------|
| `decode` | `path: String`, `targetSampleRate: Int? = 16000` | `AudioRawData?` | Decode with optional resampling |
| `decodeNative` | `path: String` | `AudioRawData?` | Decode at native sample rate |

### Resampling Behavior

The `decode` method resamples audio to the `targetSampleRate` by default:

```kotlin
// Default: resample to 16kHz (for Calibra APIs)
val calibraReady = SonixDecoder.decode("/path/to/audio.mp3")

// Explicit target rate
val at44k = SonixDecoder.decode("/path/to/audio.mp3", targetSampleRate = 44100)

// No resampling — keep native sample rate
val native = SonixDecoder.decode("/path/to/audio.mp3", targetSampleRate = null)

// Shorthand for no resampling
val native2 = SonixDecoder.decodeNative("/path/to/audio.mp3")
```

```swift
// Default: resample to 16kHz
let calibraReady = SonixDecoder.decode(path: "/path/to/audio.mp3")

// Explicit target rate
let at44k = SonixDecoder.decode(path: "/path/to/audio.mp3", targetSampleRate: 44100)

// No resampling
let native = SonixDecoder.decodeNative(path: "/path/to/audio.mp3")
```

## AudioRawData

The decoded result contains:

| Property | Type | Description |
|----------|------|-------------|
| `audioData` | `ByteArray` | Raw PCM bytes (16-bit signed, little-endian) |
| `samples` | `FloatArray` / `[Float]` | Float samples in [-1.0, 1.0] range |
| `sampleRate` | `Int` | Sample rate in Hz |
| `numChannels` | `Int` | Number of channels (1 = mono, 2 = stereo) |
| `durationMilliSecs` | `Int` | Duration in milliseconds |

## Supported Formats

| Format | Android | iOS |
|--------|---------|-----|
| WAV | Yes | Yes |
| MP3 | Yes | Yes |
| M4A/AAC | Yes | Yes |
| OGG | Yes | No |
| FLAC | Yes | Yes |

## Common Patterns

### Decode for Analysis

```kotlin
val audioData = SonixDecoder.decode(filePath)  // 16kHz by default
if (audioData != null) {
    // Feed to Calibra for pitch detection
    val contour = pitchExtractor.extract(audioData)
}
```

### Decode and Re-encode

```kotlin
val audioData = SonixDecoder.decode(inputPath, targetSampleRate = null)
if (audioData != null) {
    SonixEncoder.encode(data = audioData, outputPath = outputPath, format = "mp3")
}
```

## Next Steps

- [SonixEncoder](./encoder) — Encode PCM back to compressed formats
- [SonixResampler](./resampler) — Explicit sample rate conversion
- [SonixPlayer](./player) — Play audio files directly (no manual decoding needed)
