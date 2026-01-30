# VoxaTrace

A cross-platform audio SDK for singing and music apps, built with Kotlin Multiplatform.

## Overview

VoxaTrace provides production-ready APIs for building music education apps, karaoke apps, vocal training tools, and similar audio-intensive applications.

| Module | Description |
|--------|-------------|
| **Sonix** | Audio playback, recording, effects, mixing, and synthesis |
| **Calibra** | Pitch detection, voice activity detection, and singing evaluation |

## Platform Support

| Platform | Minimum Version | Distribution |
|----------|-----------------|--------------|
| Android | API 26 (8.0) | Maven Central |
| iOS | iOS 15.0 | Swift Package Manager |

## Installation

### Android (Gradle Kotlin DSL)

```kotlin
dependencies {
    implementation("com.musicmuni:voxatrace:<version>")
}
```

### iOS (Swift Package Manager)

1. In Xcode, go to **File > Add Package Dependencies**
2. Enter: `https://github.com/musicmuni/voxatrace`
3. Select the version and add to your target

Or add to your `Package.swift`:

```swift
dependencies: [
    .package(url: "https://github.com/musicmuni/voxatrace", from: "<version>")
]
```

## Quick Start

### Initialize the SDK

```kotlin
// Kotlin
VT.initialize(apiKey = "sk_live_your_api_key")
```

```swift
// Swift
VT.companion.initialize(apiKey: "sk_live_your_api_key")
```

### Detect Pitch

```kotlin
// Kotlin
val detector = CalibraPitch.createDetector()
val point = detector.detect(samples, sampleRate = 16000)
println("Pitch: ${point.pitch} Hz")
detector.close()
```

```swift
// Swift
let detector = CalibraPitch.companion.createDetector()
let point = detector.detect(samples: samples, sampleRate: 16000)
print("Pitch: \(point.pitch) Hz")
detector.close()
```

### Play Audio

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

## Features

### Sonix (Audio Engine)

- **SonixPlayer** - Playback with pitch shifting and tempo control
- **SonixRecorder** - Recording to M4A/MP3 formats
- **SonixMixer** - Multi-track audio mixing
- **SonixMetronome** - Visual metronome with timing callbacks
- **SonixMidiSynthesizer** - MIDI synthesis with SoundFont support

### Calibra (Audio Analysis)

- **CalibraPitch** - Real-time pitch detection (YIN, SwiftF0)
- **CalibraVAD** - Voice activity detection (singing, speech, general)
- **CalibraVocalRange** - Detect vocal range capabilities
- **CalibraLiveEval** - Real-time singing evaluation with live feedback
- **CalibraNoteEval** - Individual note scoring and accuracy
- **CalibraEffects** - Reverb, compressor, noise gate

## Documentation

Full documentation is available at [docs/](./docs) or online:

- [Installation Guide](./docs/docs/getting-started/installation.md)
- [Android Quickstart](./docs/docs/getting-started/android-quickstart.md)
- [iOS Quickstart](./docs/docs/getting-started/ios-quickstart.md)
- [API Concepts](./docs/docs/concepts/api-patterns.md)

## Demo Apps

Working sample applications demonstrating VoxaTrace features:

- **Android**: [`demo-apps/android/`](./demo-apps/android/)
- **iOS**: [`demo-apps/ios/`](./demo-apps/ios/)

To run the demo apps:

### Android

1. Open `demo-apps/android/` in Android Studio
2. Add your API key to `local.properties`
3. Build and run on a device

### iOS

1. Open `demo-apps/ios/VoxaTraceDemo/VoxaTraceDemo.xcodeproj` in Xcode
2. Copy `Config.swift.template` to `Config.swift` and add your API key
3. Build and run on a device or simulator

## API Key

VoxaTrace requires an API key for initialization. Contact [support@musicmuni.com](mailto:support@musicmuni.com) to obtain one.

## License

VoxaTrace is proprietary software. See [LICENSE](./LICENSE) for details.

## Support

- **Issues**: [GitHub Issues](https://github.com/musicmuni/voxatrace/issues)
- **Email**: [support@musicmuni.com](mailto:support@musicmuni.com)
- **Website**: [musicmuni.com](https://musicmuni.com)
