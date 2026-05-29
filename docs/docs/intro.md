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
┌────────────────────────────────────────────────────────────────────────┐
│                              VoxaTrace                                 │
├──────────────┬──────────────┬──────────────┬──────────────┬────────────┤
│    Sonix     │     Tona     │   Tessera    │   Accura     │  Calibra   │
│  Audio I/O   │    Pitch     │ Voice metrics│ Intonation   │  Singing   │
│              │              │              │ scoring      │  eval      │
├──────────────┼──────────────┼──────────────┼──────────────┼────────────┤
│ • Player     │ • Detection  │ • Breath     │ • EQ / JI    │ • LiveEval │
│ • Recorder   │ • Processing │ • Agility    │   per-note   │ • MelodyEval│
│ • Mixer      │ • Analysis   │ • Range      │   deviation  │ • NoteEval │
│ • Encoder    │   (histogram │ • Speaking   │ • 0–100      │ • VAD      │
│ • Decoder    │   transcr.)  │   pitch      │   scoring    │            │
│ • Metronome  │              │              │              │            │
│ • MIDI synth │              │              │              │            │
└──────────────┴──────────────┴──────────────┴──────────────┴────────────┘

           Common: MusicTheory (Hz/MIDI/cents conversions, shruti alignment)
```

## Performance

| Metric | Specification |
| ------ | ------------- |
| Pitch detection latency | ~64 ms (1024-sample window at 16 kHz, BALANCED) |
| Default frequency range | 80 Hz – 1000 Hz (configurable per `VoiceType`) |
| SwiftF0 model range | 46.875 Hz – 2093.75 Hz |
| Confidence threshold | 0.0 – 1.0 (default 0.75 for BALANCED) |
| Sample rates | Auto-resampling to 16 kHz internally (ADR-017) |
| Minimum Android | API 26 (Android 8.0) |
| Minimum iOS | iOS 15 |

## Hello, Pitch Detection

### Kotlin

```kotlin
VT.initializeForServer("sk_live_…")  // see Authentication guide for mobile init

val detector = PitchDetection.createDetector()
val point = detector.detect(audioSamples, sampleRate = 16000)
println("${point.pitch} Hz @ ${(point.confidence * 100).toInt()}% confidence")
detector.close()
```

### Swift

```swift
let detector = PitchDetection.createDetector()
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
val detector = PitchDetection.createDetector()

// Tier 2: Configurable
val detector = PitchDetection.createDetector(
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

- [Sonix](./sonix/overview) – Audio engine (player, recorder, mixer, encoder, decoder, …)
- [Tona](./tona/overview) – Pitch detection / processing / analysis
- [Tessera](./tessera/overview) – Voice metrics (breath, agility, range, speaking pitch)
- [Accura](./accura/overview) – Intonation analysis and 0–100 scoring
- [Calibra](./calibra/overview) – Singing evaluation (LiveEval / MelodyEval / NoteEval / VAD)
- [Common: MusicTheory](./common/music-theory) – Pitch ↔ MIDI ↔ note ↔ cents conversions, shruti alignment

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
    .package(url: "https://github.com/musicmuni/voxatrace", from: "{{version}}")
]
```

```ruby
# Podfile
pod 'VoxaTrace', :podspec => 'https://raw.githubusercontent.com/musicmuni/voxatrace/main/VoxaTrace.podspec'
```

[Full installation guide →](./getting-started/installation)

## Support

- [GitHub Issues](https://github.com/musicmuni/voxatrace/issues) – Bug reports and feature requests
- [Demo Apps](https://github.com/musicmuni/voxatrace/tree/main/public/demo-apps) – Working examples
