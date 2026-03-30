---
sidebar_position: 12
---

# SonixAudioUtils

Utility functions for manipulating audio data: concatenation, peak normalization, offline mixing, and frame/time conversions.

## Quick Start

### Kotlin

```kotlin
// Concatenate audio clips
val combined = SonixAudioUtils.concatenate(listOf(intro, verse, chorus))

// Peak-normalize audio
val normalized = SonixAudioUtils.normalize(decoded)

// Mix two tracks with custom gains
val mixed = SonixAudioUtils.mix(
    audioList = listOf(vocal, backing),
    gains = floatArrayOf(1.0f, 0.5f)
)
```

### Swift

```swift
let combined = SonixAudioUtils.concatenate([intro, verse, chorus])
let normalized = SonixAudioUtils.normalize(audio: decoded)
let mixed = SonixAudioUtils.mix(audioList: [vocal, backing], gains: [1.0, 0.5])
```

## Methods

### concatenate

Join multiple audio clips end-to-end. All inputs must have the same sample rate and channel count.

```kotlin
val combined = SonixAudioUtils.concatenate(listOf(audio1, audio2, audio3))
```

### normalize

Peak-normalize audio so the maximum absolute sample value reaches 1.0. Preserves dynamic range while maximizing signal level. Silent audio is returned unchanged.

```kotlin
val normalized = SonixAudioUtils.normalize(audio)
```

### mix

Mix multiple mono audio tracks into a single output. Tracks are resampled to a common sample rate if needed, shorter tracks are zero-padded, and output is soft-clipped to prevent distortion.

For real-time playback mixing, use [SonixMixer](./mixer) instead.

```kotlin
// Equal gain
val mixed = SonixAudioUtils.mix(listOf(vocal, backing))

// Custom gains and target sample rate
val mixed = SonixAudioUtils.mix(
    audioList = listOf(vocal, backing),
    gains = floatArrayOf(1.0f, 0.5f),
    targetSampleRate = 44100
)
```

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `audioList` | `List<AudioRawData>` | required | Mono tracks to mix |
| `gains` | `FloatArray?` | `null` (1.0 for all) | Per-track gain factors |
| `targetSampleRate` | `Int` | First track's rate | Output sample rate in Hz |

### framesToTime / timeToFrames

Convert between frame indices and time in seconds.

```kotlin
val times = SonixAudioUtils.framesToTime(intArrayOf(0, 10, 20), sampleRate = 16000, hopLength = 512)
val frames = SonixAudioUtils.timeToFrames(floatArrayOf(0.0f, 0.32f), sampleRate = 16000, hopLength = 512)
```

### framesToSegments

Convert a boolean frame mask to time segment pairs. Useful for extracting voiced regions from VAD output.

```kotlin
val mask = booleanArrayOf(false, true, true, true, false)
val segments = SonixAudioUtils.framesToSegments(mask, sampleRate = 16000, hopLength = 512)
// segments: [(0.032, 0.128)]
```

## Method Summary

| Method | Description |
|--------|-------------|
| `concatenate` | Join audio clips sequentially |
| `normalize` | Peak-normalize to amplitude 1.0 |
| `mix` | Offline mixing with gains and resampling |
| `framesToTime` | Frame indices to seconds |
| `timeToFrames` | Seconds to frame indices |
| `framesToSegments` | Boolean mask to time segments |

## Next Steps

- [SonixToneGenerator](./tone-generator) — Generate synthetic waveforms
- [SonixEncoder](./encoder) — Encode results to M4A, MP3, or WAV
- [SonixDecoder](./decoder) — Decode audio files to AudioRawData
- [SonixMixer](./mixer) — Real-time playback mixing
