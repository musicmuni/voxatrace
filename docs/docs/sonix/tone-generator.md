---
sidebar_position: 13
---

# SonixToneGenerator

Generate raw audio waveforms as `AudioRawData`. Useful for tuning references, test signals, and note playback.

## Quick Start

### Kotlin

```kotlin
// Generate a 440 Hz sine tone for 1 second
val tone = SonixToneGenerator.generate(440f, durationMs = 1000)

// Generate a square wave at C4
val square = SonixToneGenerator.generate(
    frequencyHz = 261.63f,
    durationMs = 500,
    waveType = WaveType.SQUARE
)
```

### Swift

```swift
let tone = SonixToneGenerator.generate(frequencyHz: 440, durationMs: 1000)

let square = SonixToneGenerator.generate(
    frequencyHz: 261.63,
    durationMs: 500,
    waveType: .square
)
```

## Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `frequencyHz` | `Float` | required | Tone frequency in Hz |
| `durationMs` | `Int` | required | Duration in milliseconds |
| `waveType` | `WaveType` | `SINE` | Waveform shape |
| `amplitude` | `Float` | `0.8` | Peak amplitude (0.0 to 1.0) |
| `sampleRate` | `Int` | `16000` | Sample rate in Hz |

## Waveform Types

| Type | Shape | Use Case |
|------|-------|----------|
| `SINE` | Smooth sinusoidal | Tuning references, pure tones |
| `SQUARE` | Flat top and bottom | Rich harmonics, retro sounds |
| `SAWTOOTH` | Linear ramp | Bright, buzzy tones |
| `TRIANGLE` | Linear peak | Softer than square, mellower |

## Common Patterns

### Tuning Reference

```kotlin
// A4 = 440 Hz, 2 seconds
val reference = SonixToneGenerator.generate(440f, durationMs = 2000)
SonixEncoder.encode(data = reference, outputPath = "a440.wav", format = "wav")
```

### Test Signal with Normalization

```kotlin
val tone = SonixToneGenerator.generate(1000f, durationMs = 500, amplitude = 0.5f)
val normalized = SonixAudioUtils.normalize(tone)
```

## Next Steps

- [SonixAudioUtils](./audio-utils) — Manipulate generated audio (normalize, mix, concatenate)
- [SonixEncoder](./encoder) — Save generated audio to file
- [CalibraMusic](../calibra/music) — Music theory constants and frequency conversions
