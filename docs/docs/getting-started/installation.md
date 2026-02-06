---
sidebar_position: 1
---

# Installation

VoxaTrace is distributed as a Kotlin Multiplatform library. Follow the platform-specific instructions below.

## Android

### Gradle (Kotlin DSL)

Add the repository and dependency to your app's `build.gradle.kts`:

```kotlin
repositories {
    mavenCentral()
    // Or your private Maven repository
}

dependencies {
    implementation("com.musicmuni:voxatrace:0.9.1")
}
```

### Gradle (Groovy)

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'com.musicmuni:voxatrace:0.9.1'
}
```

### Permissions

Add to your `AndroidManifest.xml`:

```xml
<!-- For recording -->
<uses-permission android:name="android.permission.RECORD_AUDIO" />

<!-- For file access (if loading from external storage) -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

## iOS

### Swift Package Manager (Recommended)

1. In Xcode, go to **File > Add Package Dependencies**
2. Enter the repository URL: `https://github.com/musicmuni/voxatrace`
3. Select version and add to your target

Or add to your `Package.swift`:

```swift
dependencies: [
    .package(url: "https://github.com/musicmuni/voxatrace", from: "0.9.1")
]
```

### CocoaPods

Add to your `Podfile`:

```ruby
pod 'VoxaTrace', :podspec => 'https://raw.githubusercontent.com/musicmuni/voxatrace/main/VoxaTrace.podspec'
```

Then run `pod install`.

### XCFramework (Manual)

1. Download `VoxaTrace.xcframework` from the [releases page](https://github.com/musicmuni/voxatrace/releases)
2. Drag it into your Xcode project
3. In your target's **General** tab, ensure it's listed under "Frameworks, Libraries, and Embedded Content"
4. Set embedding to "Embed & Sign"

### Info.plist

Add microphone usage description for recording:

```xml
<key>NSMicrophoneUsageDescription</key>
<string>We need microphone access to record your singing.</string>
```

## Version Compatibility

| VoxaTrace | Android Min SDK | iOS Min | Kotlin |
|-----------|-----------------|---------|--------|
| 0.9.x     | API 24 (7.0)    | iOS 14  | 1.9+   |

## Verifying Installation

### Kotlin

```kotlin
import com.musicmuni.voxatrace.sonix.SonixPlayer

// If this compiles, you're set!
suspend fun test() {
    val player = SonixPlayer.create("test.mp3")
    println("VoxaTrace installed!")
}
```

### Swift

```swift
import VoxaTrace

// If this compiles, you're set!
func test() async throws {
    let player = try await SonixPlayer.companion.create(source: "test.mp3")
    print("VoxaTrace installed!")
}
```

## Next Steps

- [Android Quickstart](./android-quickstart) - Build your first Android app
- [iOS Quickstart](./ios-quickstart) - Build your first iOS app
