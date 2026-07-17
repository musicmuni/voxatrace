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
| `numChannels` | `Int` | Number of channels in the **decoded output** (always `1` — see note below) |
| `durationMilliSecs` | `Int` | Duration in milliseconds |

`SonixDecoder.decode()` always returns mono (per ADR-017). Multi-channel inputs are downmixed to mono by averaging channels before any optional resampling. Apps that bypass `SonixDecoder` and feed audio directly to processing facades are responsible for mono conversion themselves.

## Supported Formats

| Format | Android | iOS |
|--------|---------|-----|
| WAV | Yes | Yes |
| MP3 | Yes | Yes |
| M4A/AAC | Yes | Yes |
| OGG | Yes | No |
| FLAC | Yes | Yes |

:::note Desktop/JVM
On the desktop-JVM target, only **MP3** and **WAV** (plus **AU**/**AIFF**) decode. There is no JVM-native AAC decoder, so decoding an M4A/AAC file returns `null`. Use MP3 or WAV sources on desktop.
:::

:::note WAV bit depth and layout
WAV decodes at any common sample format (16, 24, and 32-bit integer, plus 32/64-bit float) and tolerates non-canonical chunk layouts, including files that carry `JUNK`, `bext`, or other chunks before `fmt`/`data` (studio and Broadcast-WAV masters). Everything is normalized to 16-bit PCM. iOS and desktop have always handled this; Android gained it in 3.0.3.
:::

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
