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
┌──────────────┬──────────────┬──────────────┬──────────────┬────────────┐
│    Sonix     │     Tona     │   Tessera    │    Accura    │  Calibra   │
│  Audio I/O   │    Pitch     │ Voice metrics│ Intonation   │  Singing   │
│              │              │              │ scoring      │  eval      │
├──────────────┼──────────────┼──────────────┼──────────────┼────────────┤
│ • Player     │ • Detection  │ • Breath     │ • EQ / JI    │ • LiveEval │
│ • Recorder   │ • Processing │ • Agility    │   per-note   │ • MelodyEv │
│ • Mixer      │ • Analysis   │ • Range      │   deviation  │ • NoteEval │
│ • Encoder    │   (histogram │ • Speaking   │ • 0–100      │ • VAD      │
│ • Decoder    │   transcr.)  │   pitch      │   scoring    │ • Effects  │
│ • Metronome  │              │              │              │            │
│ • MIDI synth │              │              │              │            │
└──────────────┴──────────────┴──────────────┴──────────────┴────────────┘

           Common: MusicTheory (Hz/MIDI/cents conversions, shruti alignment)
```

## Installation

### Android

```kotlin
dependencies {
    implementation("com.musicmuni:voxatrace:3.0.2")
}
```

### iOS (Swift Package Manager)

```swift
dependencies: [
    .package(url: "https://github.com/musicmuni/voxatrace", from: "3.0.2")
]
```

### iOS (CocoaPods)

```ruby
pod 'VoxaTrace', :podspec => 'https://raw.githubusercontent.com/musicmuni/voxatrace/main/VoxaTrace.podspec'
```

## Quick Start

### Kotlin

```kotlin
VT.initializeForServer("sk_live_…")  // see docs for proxy / attestation flows on mobile
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

## Performance

| Metric | Specification |
| ------ | ------------- |
| Pitch detection latency | ~64 ms (1024-sample window at 16 kHz, BALANCED) |
| Default frequency range | 80 Hz – 1000 Hz (configurable per `VoiceType`) |
| Sample rates | Auto-resampling to 16 kHz internally (ADR-017) |
| Minimum Android | API 24 (Android 7.0) |
| Minimum iOS | iOS 15 |

## Documentation

**[voxatrace.ai](https://voxatrace.ai)**

## License

Commercial. See [LICENSE](LICENSE).

## Support

[support@musicmuni.com](mailto:support@musicmuni.com)
