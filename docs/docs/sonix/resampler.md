---
sidebar_position: 8
---

# SonixResampler

High-quality audio resampling using libsamplerate for converting between sample rates.

## Quick Start

### Kotlin

```kotlin
// Resample from 44100 Hz to 16000 Hz
val resampled: FloatArray = SonixResampler.resample(
    samples = audioSamples,
    fromRate = 44100,
    toRate = 16000
)
```

### Swift

```swift
// Resample from 44100 Hz to 16000 Hz
let resampled: [Float] = SonixResampler.resample(
    samples: audioSamples,
    fromRate: 44100,
    toRate: 16000
)
```

## Method

| Method | Parameters | Returns | Description |
|--------|-----------|---------|-------------|
| `resample` | `samples: FloatArray`, `fromRate: Int`, `toRate: Int` | `FloatArray` | Resample audio to target rate |

### Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `samples` | `FloatArray` / `[Float]` | Input audio samples (mono, float in [-1.0, 1.0] range) |
| `fromRate` | `Int` | Source sample rate in Hz (e.g., 44100) |
| `toRate` | `Int` | Target sample rate in Hz (e.g., 16000) |

## Common Sample Rates

| Rate | Use Case |
|------|----------|
| 16000 Hz | Calibra APIs (pitch detection, evaluation) |
| 22050 Hz | Speech analysis |
| 44100 Hz | CD quality, standard playback |
| 48000 Hz | Video, professional audio |

## When to Use

In most cases you don't need to use `SonixResampler` directly — `SonixDecoder.decode()` resamples automatically to 16kHz by default. Use it when:

- You have raw float samples at the wrong rate
- You need a specific target rate that isn't 16kHz
- You're doing custom audio processing

```kotlin
// Usually NOT needed — SonixDecoder handles resampling
val audioData = SonixDecoder.decode("audio.mp3")  // Already at 16kHz

// Needed when you have raw samples from another source
val recordedAt48k: FloatArray = ...
val at16k = SonixResampler.resample(recordedAt48k, fromRate = 48000, toRate = 16000)
```

## Quality

Uses **libsamplerate** (Secret Rabbit Code) which provides:
- High-quality sinc interpolation
- Minimal aliasing artifacts
- Fast performance on mobile devices

## Next Steps

- [SonixDecoder](./decoder) — Decode audio files (resamples automatically)
- [SonixEncoder](./encoder) — Encode resampled audio to files
