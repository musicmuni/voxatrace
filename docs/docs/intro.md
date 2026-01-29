---
sidebar_position: 1
slug: /
---

# VoxaTrace

VoxaTrace is a cross-platform audio SDK for mobile apps, built with Kotlin Multiplatform.

## What is VoxaTrace?

VoxaTrace provides everything you need to build singing and music apps:

| Module | What It Does |
|--------|--------------|
| **Sonix** | Audio playback, recording, effects, and synthesis |
| **Calibra** | Pitch detection, voice activity, and singing evaluation |

## Who is it for?

- **Karaoke apps** - Play backing tracks, record vocals, score singing
- **Music education** - Real-time pitch feedback, practice tracking
- **Tuner apps** - Detect pitch for instruments and voice
- **Voice recorders** - Capture and encode audio

## Quick Start

### Install

See [Installation](./getting-started/installation) for Gradle and SPM setup.

### Hello Pitch

#### Kotlin

```kotlin
val detector = CalibraPitch.createDetector()
val point = detector.detect(samples, sampleRate = 16000)
println("Pitch: ${point.pitch} Hz")
detector.close()
```

#### Swift

```swift
let detector = CalibraPitch.companion.createDetector()
let point = detector.detect(samples: samples, sampleRate: 16000)
print("Pitch: \(point.pitch) Hz")
detector.close()
```

## Documentation Structure

### Getting Started

Step-by-step guides to get up and running:

- [Installation](./getting-started/installation) - Add VoxaTrace to your project
- [Android Quickstart](./getting-started/android-quickstart) - Build your first Android app
- [iOS Quickstart](./getting-started/ios-quickstart) - Build your first iOS app

### Concepts

Understand the "what" and "why":

- [Pitch Detection](./concepts/pitch-detection) - How pitch detection works
- [Voice Activity](./concepts/voice-activity) - Detecting when someone is singing
- [Live Evaluation](./concepts/live-evaluation) - Scoring singing in real-time
- [Audio Effects](./concepts/audio-effects) - Noise gate, compressor, reverb
- [API Patterns](./concepts/api-patterns) - The three-tier API pattern

### Guides

Implementation details and best practices:

- [Playing Audio](./guides/playing-audio) - SonixPlayer features
- [Recording Audio](./guides/recording-audio) - SonixRecorder features
- [Detecting Pitch](./guides/detecting-pitch) - CalibraPitch features
- [Live Evaluation](./guides/live-evaluation) - CalibraLiveEval features

### Cookbook

Complete app recipes:

- [Karaoke App](./cookbook/karaoke-app) - Player + Recorder + LiveEval
- [Tuner App](./cookbook/tuner-app) - Pitch detector + visualization
- [Voice Recorder](./cookbook/voice-recorder) - Record + encode + save
- [Practice Tracker](./cookbook/practice-tracker) - Store scores, show progress

### API Reference

Detailed class documentation:

- [Sonix](/docs/sonix/overview) - Audio engine APIs
- [Calibra](/docs/calibra/overview) - Analysis APIs

## Design Principles

### Three-Tier API

Every API supports three usage levels:

```kotlin
// Tier 1: Presets (80% of users)
val detector = CalibraPitch.createDetector()

// Tier 2: Builder (15% of users)
val config = PitchDetectorConfig.Builder()
    .algorithm(PitchAlgorithm.SWIFT_F0)
    .build()
val detector = CalibraPitch.createDetector(config)

// Tier 3: .copy() (5% of users)
val config = PitchDetectorConfig.PRECISE.copy(tolerance = 0.08f)
```

### Dual Platform

Every example in both Kotlin and Swift:

```kotlin
// Kotlin
val player = SonixPlayer.create("song.mp3")
player.play()
```

```swift
// Swift
let player = try await SonixPlayer.companion.create(source: "song.mp3")
player.play()
```

### Source-First Documentation

KDoc is the ground truth. This documentation links to API reference, not vice versa.

## Support

- [GitHub Issues](https://github.com/musicmuni/voxatrace/issues) - Bug reports and feature requests
- [Demo Apps](https://github.com/musicmuni/voxatrace-demos) - Working examples
