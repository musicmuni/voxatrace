---
sidebar_position: 7
---

# SonixEncoder

Encode raw PCM audio to compressed formats (M4A/AAC or MP3) for storage and sharing.

## Quick Start

### Kotlin

```kotlin
// Encode from AudioRawData (e.g., after decoding or synthesis)
val rawData = SonixDecoder.decode("input.wav", targetSampleRate = null)
if (rawData != null) {
    val success = SonixEncoder.encode(
        data = rawData,
        outputPath = "/path/to/output.mp3",
        format = "mp3"
    )
}
```

### Swift

```swift
if let rawData = SonixDecoder.decode(path: "input.wav", targetSampleRate: nil) {
    let success = SonixEncoder.encode(
        data: rawData,
        outputPath: "/path/to/output.mp3",
        format: "mp3"
    )
}
```

## Encoding Methods

### From AudioRawData

```kotlin
SonixEncoder.encode(
    data = audioRawData,
    outputPath = "/path/to/output.m4a",
    format = "m4a",         // "m4a" (default) or "mp3"
    bitrateKbps = 128       // default: 128
)
```

```swift
SonixEncoder.encode(
    data: audioRawData,
    outputPath: "/path/to/output.m4a",
    format: "m4a",
    bitrateKbps: 128
)
```

### From Float Samples

#### Kotlin

```kotlin
SonixEncoder.encode(
    samples = floatArray,      // [-1.0, 1.0] range
    sampleRate = 44100,
    channels = 1,
    outputPath = "/path/to/output.m4a",
    format = "m4a",
    bitrateKbps = 128
)
```

#### Swift

```swift
SonixEncoder.encode(
    samples: floatArray,
    sampleRate: 44100,
    channels: 1,
    outputPath: "/path/to/output.m4a",
    format: "m4a",
    bitrateKbps: 128
)
```

### From PCM Bytes

#### Kotlin

```kotlin
SonixEncoder.encode(
    pcmData = byteArray,       // 16-bit signed, little-endian
    sampleRate = 44100,
    channels = 1,
    outputPath = "/path/to/output.mp3",
    format = "mp3",
    bitrateKbps = 192
)
```

#### Swift

```swift
SonixEncoder.encode(
    pcmData: data,
    sampleRate: 44100,
    channels: 1,
    outputPath: "/path/to/output.mp3",
    format: "mp3",
    bitrateKbps: 192
)
```

## Method Summary

| Method | Input Type | Parameters |
|--------|-----------|------------|
| `encode` | `AudioRawData` | `data`, `outputPath`, `format`, `bitrateKbps` |
| `encode` | `FloatArray` / `[Float]` | `samples`, `sampleRate`, `channels`, `outputPath`, `format`, `bitrateKbps` |
| `encode` | `ByteArray` / `Data` | `pcmData`, `sampleRate`, `channels`, `outputPath`, `format`, `bitrateKbps` |
| `isFormatAvailable` | — | `format: String` → `Boolean` |

## Format Availability

```kotlin
if (SonixEncoder.isFormatAvailable("mp3")) {
    // MP3 encoding is supported on this platform
}
```

## Supported Formats

| Format | String | Quality | Use Case |
|--------|--------|---------|----------|
| M4A/AAC | `"m4a"` or `"aac"` | Best | Default choice, native hardware encoding |
| MP3 | `"mp3"` | Good | Universal compatibility |

## Bitrate Guide

| Bitrate | Quality | File Size |
|---------|---------|-----------|
| 64 kbps | Low (voice) | ~0.5 MB/min |
| 128 kbps | Standard | ~1 MB/min |
| 192 kbps | High | ~1.5 MB/min |
| 256 kbps | Very high | ~2 MB/min |

## Common Patterns

### Convert Audio Format

```kotlin
// Decode MP3, re-encode as M4A
val audioData = SonixDecoder.decode("input.mp3", targetSampleRate = null)
if (audioData != null) {
    SonixEncoder.encode(data = audioData, outputPath = "output.m4a")
}
```

### Export Synthesized Audio

```kotlin
val synth = SonixMidiSynthesizer.create(soundFontPath = sfPath)
synth.synthesizeFromNotes(notes, outputPath = "output.wav")

// Convert WAV to compressed MP3
val wavData = SonixDecoder.decode("output.wav", targetSampleRate = null)
if (wavData != null) {
    SonixEncoder.encode(data = wavData, outputPath = "output.mp3", format = "mp3")
}
```

## Next Steps

- [SonixDecoder](./decoder) — Decode audio files to raw PCM
- [SonixRecorder](./recorder) — Record with automatic encoding
- [SonixResampler](./resampler) — Resample before encoding
