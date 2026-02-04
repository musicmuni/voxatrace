# VoxaTrace

Cross-platform audio SDK for music apps. Pitch detection, voice activity, playback, recording, and real-time evaluation.

[![Maven Central](https://img.shields.io/maven-central/v/com.musicmuni/voxatrace)](https://central.sonatype.com/artifact/com.musicmuni/voxatrace)
[![Swift Package Manager](https://img.shields.io/badge/SPM-compatible-brightgreen)](https://github.com/musicmuni/voxatrace)

## Installation

### Android

```kotlin
dependencies {
    implementation("com.musicmuni:voxatrace:<version>")
}
```

### iOS (Swift Package Manager)

```
https://github.com/musicmuni/voxatrace
```

### iOS (CocoaPods)

```ruby
pod 'VoxaTrace'
```

## Quick Example

```kotlin
// Initialize
VT.initialize(apiKey = "your_api_key")

// Detect pitch
val detector = CalibraPitch.createDetector()
val result = detector.detect(samples, sampleRate = 16000)
println("Pitch: ${result.pitch} Hz")
```

## Documentation

**[musicmuni.github.io](https://musicmuni.github.io/)**

## License

Commercial. See [LICENSE](LICENSE).

## Support

[support@musicmuni.com](mailto:support@musicmuni.com)
