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
    implementation("com.musicmuni:voxatrace:{{version}}")
}
```

### Gradle (Groovy)

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'com.musicmuni:voxatrace:{{version}}'
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
    .package(url: "https://github.com/musicmuni/voxatrace", from: "{{version}}")
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

| VoxaTrace | Android Min SDK | iOS Min | JVM                     | Kotlin |
|-----------|-----------------|---------|-------------------------|--------|
| 3.0.x     | API 26 (8.0)    | iOS 15  | Java 17+ (macOS, Linux) | 1.9+   |
| 2.0.x     | API 26 (8.0)    | iOS 15  | —                       | 1.9+   |

## Authentication

VoxaTrace requires initialization with valid credentials before any SDK APIs can be used. Authentication works via an **API key**, a **proxy**, or **platform attestation**. The quickest way to get started is with your API key directly:

### Kotlin

```kotlin
// In Application.onCreate() or before using any VoxaTrace API
VT.initializeForServer(apiKey = "sk_live_your_key_here")
```

### Swift

```swift
// In AppDelegate or App init
VT.initializeForServer(apiKey: "sk_live_your_key_here")
```

:::tip
For production mobile apps, use **Proxy** or **App Attestation** instead of embedding API keys directly. See the [Authentication guide](../guides/authentication) for all three methods, proxy server setup, and security best practices.
:::

## Desktop & Server (JVM)

VoxaTrace runs on the JVM for desktop apps and server-side analysis or lesson
authoring (macOS and Linux). The library jar is platform-agnostic; the native
code ships as a per-platform artifact you select with a classifier. AI-backed
features (neural pitch and voice activity detection) are an opt-in extra.

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    // SDK classes (required)
    implementation("com.musicmuni:voxatrace-jvm:{{version}}")

    // Native libraries for your platform (required) — pick one:
    runtimeOnly("com.musicmuni:voxatrace-jvm:{{version}}:natives-macos-arm64")
    // runtimeOnly("com.musicmuni:voxatrace-jvm:{{version}}:natives-linux-x64")

    // Optional: AI-backed features (neural pitch / VAD). Larger download.
    // runtimeOnly("com.musicmuni:voxatrace-jvm:{{version}}:natives-ai-macos-arm64")
}
```

The native libraries are loaded automatically from the classpath at runtime; no
`java.library.path` configuration is needed.

Initialize for server/desktop use with your API key:

```kotlin
VT.initializeForServer(apiKey = "sk_live_your_key_here")
```

See the [JVM Quickstart](./jvm-quickstart) to build your first desktop/server
program, and the [Lesson Authoring guide](../guides/lesson-authoring) to
pre-compute reference lesson bundles.

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
    let player = try await SonixPlayer.create(source: "test.mp3")
    print("VoxaTrace installed!")
}
```

## Next Steps

- [Authentication](../guides/authentication) - Set up secure authentication for production
- [Android Quickstart](./android-quickstart) - Build your first Android app
- [iOS Quickstart](./ios-quickstart) - Build your first iOS app
- [JVM Quickstart](./jvm-quickstart) - Build your first desktop/server program
