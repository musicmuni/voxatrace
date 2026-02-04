---
sidebar_position: 2
---

# Why VoxaTrace?

## The Gap in Voice AI

Voice AI has made incredible progress understanding **what you say**. Speech-to-text is solved. But none of it understands **how you sound**.

Pitch. Timbre. Melody. Rhythm. Emotion. Vocal quality. These acoustic qualities are invisible to speech AI — but they're everything for singing apps, vocal training, music education, and any application where the sound of a voice matters.

**VoxaTrace fills that gap.**

## What Makes VoxaTrace Different

### Acoustic Intelligence, Not Speech Recognition

| Feature | Speech AI | VoxaTrace |
| ------- | --------- | --------- |
| Output | Text transcript | Pitch (Hz), confidence, vocal activity |
| Focus | Words and language | How the voice sounds |
| Use case | Dictation, commands | Singing, training, games |
| Latency | 100ms–1s (cloud) | ~50ms (on-device) |

### On-Device, Real-Time

No server calls. No network latency. No data leaving the device.

- **~50ms latency** — fast enough for real-time feedback
- **Works offline** — no internet required
- **Privacy by design** — audio never leaves the device

### Battle-Tested at Scale

- **8 years of R&D** — not a weekend project
- **5 million users** — proven in production
- **Same API on Android and iOS** — write once, ship everywhere

### Three-Tier API Design

Use as much complexity as you need:

```kotlin
// Tier 1: Just works (80% of users)
val detector = CalibraPitch.createDetector()

// Tier 2: Discoverable options (15% of users)
val detector = CalibraPitch.createDetector(
    PitchDetectorConfig.Builder()
        .algorithm(PitchAlgorithm.SWIFT_F0)
        .voiceType(VoiceType.SOPRANO)
        .build()
)

// Tier 3: Full control (5% of users)
val config = PitchDetectorConfig.PRECISE.copy(
    confidenceThreshold = 0.6f,
    amplitudeGateDb = -50f
)
```

## Algorithm Performance

Two pitch detection algorithms for different needs:

| Metric | YIN | SwiftF0 |
| ------ | --- | ------- |
| Latency | ~50ms | ~50ms |
| Accuracy (clean audio) | Good | Excellent |
| Accuracy (noisy audio) | Fair | Good |
| Memory overhead | Minimal | ~10MB (model) |
| Battery impact | Low | Low-Medium |
| Dependencies | None | ONNX Runtime |

### When to Use Each

**Choose YIN when:**

- Building a simple tuner
- Targeting low-end devices
- Audio is clean and controlled
- You want minimal dependencies

**Choose SwiftF0 when:**

- Building singing or vocal training apps
- Users are in noisy environments
- Accuracy matters more than app size
- You need robust vibrato detection

## What You Can Build

VoxaTrace enables any application where the **acoustic qualities of voice** matter:

| Application | What VoxaTrace Provides |
| ----------- | ----------------------- |
| Singing apps | Pitch detection, scoring, performance feedback |
| Vocal training | Real-time intonation analysis, progress tracking |
| Music education | Ear training, sight-singing, pitch matching |
| Voice games | Pitch as game input (sing to jump, hum to control) |
| Accessibility | Voice-based input beyond speech recognition |
| Wellness | Vocal health monitoring, breathing exercises |

## Ready to Start?

- [Installation](./getting-started/installation) — Add VoxaTrace to your project
- [Android Quickstart](./getting-started/android-quickstart) — First app in 5 minutes
- [iOS Quickstart](./getting-started/ios-quickstart) — First app in 5 minutes
- [Karaoke App Recipe](./cookbook/karaoke-app) — Complete example with scoring
