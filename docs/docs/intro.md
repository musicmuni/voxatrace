---
sidebar_position: 1
slug: /
---

# VoxaTrace

Voice AI today understands what you say: the words, the language, the text. But it's deaf to **how you actually sound** — pitch, timbre, emotion, vocal quality, rhythm, melody. Everything that makes a voice a voice, not just a transcript.

**VoxaTrace is an on-device SDK that makes any application acoustically intelligent.**

Eight years of R&D. Five million users in production. All running natively on Android and iOS, without a single server call.

## What Speech AI Misses

| Speech AI | VoxaTrace |
| --------- | --------- |
| "The user said 'hello'" | "The user sang A4 at 440 Hz with 92% confidence" |
| Words and language | Pitch, melody, rhythm, vocal quality |
| Transcription | Acoustic analysis |
| Cloud-dependent | On-device, real-time |

## What You Can Build

| Application | What VoxaTrace Enables |
| ----------- | ---------------------- |
| **Singing apps** | Pitch detection, real-time scoring, performance feedback |
| **Vocal training** | Intonation analysis, progress tracking, guided exercises |
| **Music education** | Ear training, sight-singing evaluation, pitch matching |
| **Voice games** | Pitch as input — sing to jump, hum to control |
| **Accessibility** | Voice-based input beyond speech recognition |
| **Health & wellness** | Vocal health monitoring, breathing exercises |

## What You Get

```text
┌─────────────────────────────────────────────────────────────────┐
│                         VoxaTrace                               │
├──────────────────────────────┬──────────────────────────────────┤
│           Sonix              │            Calibra               │
│       (Audio Engine)         │      (Acoustic Analysis)         │
├──────────────────────────────┼──────────────────────────────────┤
│  • Multi-track playback (8)  │  • Pitch detection (YIN/SwiftF0) │
│  • Recording (M4A/MP3)       │  • Voice activity detection      │
│  • Pitch shifting ±12 semi   │  • Singing evaluation & scoring  │
│  • Tempo control 0.5x–2x     │  • Vocal range detection         │
│  • MIDI synthesis (SoundFont)│  • Audio effects chain           │
│  • Metronome with callbacks  │  • Octave error correction       │
└──────────────────────────────┴──────────────────────────────────┘
```

## Performance

| Metric | Specification |
| ------ | ------------- |
| Pitch detection latency | ~50ms |
| Frequency range | 50 Hz – 2000 Hz |
| Confidence threshold | 0.0 – 1.0 (recommended > 0.7) |
| Simultaneous tracks | Up to 8 synchronized |
| Sample rates | Auto-resampling to 16kHz |
| Minimum Android | API 24 (Android 7.0) |
| Minimum iOS | iOS 14 |

## Hello, Pitch Detection

### Kotlin

```kotlin
val detector = CalibraPitch.createDetector()
val point = detector.detect(audioSamples, sampleRate = 16000)
println("${point.pitch} Hz @ ${(point.confidence * 100).toInt()}% confidence")
detector.close()
```

### Swift

```swift
let detector = CalibraPitch.companion.createDetector()
let point = detector.detect(samples: audioSamples, sampleRate: 16000)
print("\(point.pitch) Hz @ \(Int(point.confidence * 100))% confidence")
detector.close()
```

**Output:**

```text
440.0 Hz @ 92% confidence
```

## Why VoxaTrace?

**On-device, not cloud:**

- Zero latency from network calls
- Works offline
- User data stays on device

**Battle-tested:**

- 8 years of R&D
- 5 million users in production
- Same API on Android and iOS

**Three-tier API:**

```kotlin
// Tier 1: Just works
val detector = CalibraPitch.createDetector()

// Tier 2: Configurable
val detector = CalibraPitch.createDetector(
    PitchDetectorConfig.Builder()
        .algorithm(PitchAlgorithm.SWIFT_F0)
        .build()
)

// Tier 3: Full control
val config = PitchDetectorConfig.PRECISE.copy(confidenceThreshold = 0.6f)
```

[Read more about why VoxaTrace →](./why-voxatrace)

## Start Building

### 5-Minute Quickstarts

- [Android Quickstart](./getting-started/android-quickstart) – Pitch detector in Compose
- [iOS Quickstart](./getting-started/ios-quickstart) – Pitch detector in SwiftUI

### Complete App Recipes

- [Karaoke App](./cookbook/karaoke-app) – Play + record + score
- [Tuner App](./cookbook/tuner-app) – Chromatic tuner with cents display
- [Voice Recorder](./cookbook/voice-recorder) – Record + encode + save
- [Practice Tracker](./cookbook/practice-tracker) – Store scores, track progress

### Understand the Concepts

- [Pitch Detection](./concepts/pitch-detection) – YIN vs SwiftF0, when to use each
- [Live Evaluation](./concepts/live-evaluation) – How singing scoring works
- [Voice Activity](./concepts/voice-activity) – Detecting when someone is singing
- [API Patterns](./concepts/api-patterns) – The three-tier API design

### Module Deep Dives

- [Calibra Overview](./calibra/overview) – Acoustic analysis features
- [Sonix Overview](./sonix/overview) – Audio engine features

## Installation

```kotlin
// build.gradle.kts
dependencies {
    implementation("com.musicmuni:voxatrace:{{version}}")
}
```

```swift
// Package.swift
dependencies: [
    .package(url: "https://github.com/musicmuni/voxatrace-spm", from: "{{version}}")
]
```

[Full installation guide →](./getting-started/installation)

## Support

- [GitHub Issues](https://github.com/musicmuni/voxatrace/issues) – Bug reports and feature requests
- [Demo Apps](https://github.com/musicmuni/voxatrace-demos) – Working examples
