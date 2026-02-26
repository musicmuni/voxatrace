# VoxaTrace

Voice AI today understands what you say: the words, the language, the text. But it's deaf to **how you actually sound** — pitch, timbre, emotion, vocal quality, rhythm, melody. Everything that makes a voice a voice, not just a transcript.

**VoxaTrace is an on-device SDK that makes any application acoustically intelligent.**

Eight years of R&D. Five million users in production. All running natively on Android and iOS, without a single server call.

[![Maven Central](https://img.shields.io/maven-central/v/com.musicmuni/voxatrace)](https://central.sonatype.com/artifact/com.musicmuni/voxatrace)
[![Swift Package Manager](https://img.shields.io/badge/SPM-compatible-brightgreen)](https://github.com/musicmuni/voxatrace)

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

## What You Get

```
┌──────────────────────────────┬──────────────────────────────────┐
│           Sonix              │            Calibra               │
│       (Audio Engine)         │      (Acoustic Analysis)         │
├──────────────────────────────┼──────────────────────────────────┤
│  • Multi-track playback (8)  │  • Pitch detection (YIN/SwiftF0) │
│  • Recording (M4A/MP3)       │  • Voice activity detection      │
│  • Pitch shifting ±12 semi   │  • Singing evaluation & scoring  │
│  • Tempo control 0.5x–2x     │  • Vocal range detection         │
│  • MIDI synthesis (SoundFont)│  • Audio effects chain           │
└──────────────────────────────┴──────────────────────────────────┘
```

## Installation

### Android

```kotlin
dependencies {
    implementation("com.musicmuni:voxatrace:0.9.2")
}
```

### iOS (Swift Package Manager)

```swift
dependencies: [
    .package(url: "https://github.com/musicmuni/voxatrace", from: "0.9.2")
]
```

### iOS (CocoaPods)

```ruby
pod 'VoxaTrace', :podspec => 'https://raw.githubusercontent.com/musicmuni/voxatrace/main/VoxaTrace.podspec'
```

## Quick Start

### Kotlin

```kotlin
val detector = CalibraPitch.createDetector()
val point = detector.detect(audioSamples, sampleRate = 16000)
println("${point.pitch} Hz @ ${(point.confidence * 100).toInt()}% confidence")
detector.close()
```

### Swift

```swift
let detector = CalibraPitch.createDetector()
let point = detector.detect(samples: audioSamples, sampleRate: 16000)
print("\(point.pitch) Hz @ \(Int(point.confidence * 100))% confidence")
detector.close()
```

**Output:**

```text
440.0 Hz @ 92% confidence
```

## Performance

| Metric | Specification |
| ------ | ------------- |
| Pitch detection latency | ~50ms |
| Frequency range | 50 Hz – 2000 Hz |
| Simultaneous tracks | Up to 8 synchronized |
| Minimum Android | API 24 (Android 7.0) |
| Minimum iOS | iOS 14 |

## Documentation

**[voxatrace.ai](https://voxatrace.ai)**

## License

Commercial. See [LICENSE](LICENSE).

## Support

[support@musicmuni.com](mailto:support@musicmuni.com)
