---
sidebar_position: 1
---

# Getting Started

VozOS is a cross-platform audio SDK for mobile apps, built with Kotlin Multiplatform. It provides two main modules:

- **Sonix** - Audio playback, recording, and synthesis
- **Calibra** - Pitch detection and singing evaluation

## Installation

### Android

Add the dependency to your app's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.musicmuni:vozos:<version>")
}
```

### iOS

Add the XCFramework to your Xcode project:

1. Download `VozOS.xcframework` from the releases page
2. Drag it into your Xcode project
3. Ensure it's added to "Frameworks, Libraries, and Embedded Content"

## Quick Start

### Sonix - Audio Playback

```kotlin
// Kotlin
val player = Sonix.createPlayer()
player.load("path/to/audio.m4a")
player.play()
```

```swift
// Swift
let player = Sonix.shared.createPlayer()
player.load("path/to/audio.m4a")
player.play()
```

### Calibra - Pitch Detection

```kotlin
// Kotlin
val detector = Calibra.createPitchDetector()
detector.start { pitch ->
    println("Detected pitch: ${pitch.frequency} Hz")
}
```

```swift
// Swift
let detector = Calibra.shared.createPitchDetector()
detector.start { pitch in
    print("Detected pitch: \(pitch.frequency) Hz")
}
```

## Next Steps

- [Sonix Overview](/docs/sonix/overview) - Learn about audio playback and recording
- [Calibra Overview](/docs/calibra/overview) - Learn about pitch detection and evaluation
- [API Reference](/api) - Full API documentation
