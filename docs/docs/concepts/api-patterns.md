---
sidebar_position: 4
---

# API Patterns

VoxaTrace APIs follow consistent patterns that make them predictable and easy to learn.

## The Three-Tier Pattern

Every VoxaTrace API supports three usage tiers, letting you choose the right level of complexity for your needs.

### Tier 1: Presets (80% of users)

Use predefined configurations. Zero learning curve.

```kotlin
// Just works with sensible defaults
val player = SonixPlayer.create("song.mp3")
val detector = CalibraPitch.createDetector()
val recorder = SonixRecorder.create("/path/to/output.m4a")
```

### Tier 2: Builder (15% of users)

Discover options through IDE autocomplete. Type-safe configuration.

```kotlin
// Explore options with autocomplete
val config = PitchDetectorConfig.Builder()
    .algorithm(PitchAlgorithm.SWIFT_F0)
    .voiceType(VoiceType.WesternSoprano)
    .enableProcessing()
    .build()

val detector = CalibraPitch.createDetector(config, modelProvider = { ... })
```

### Tier 3: .copy() (5% of users)

Direct access to all parameters. For power users who know exactly what they want.

```kotlin
// Precise control when you need it
val config = PitchDetectorConfig.PRECISE.copy(
    confidenceThreshold = 0.6f,
    amplitudeGateDb = -50f
)
```

## Config + Factory Pattern

APIs separate **configuration** from **creation**:

```kotlin
// Config is immutable data
data class SonixPlayerConfig(
    val volume: Float = 1f,
    val pitch: Float = 0f,
    val tempo: Float = 1f
)

// Factory creates the instance
val player = SonixPlayer.create(source, config)
```

Benefits:
- **Reusable configs**: Create once, use many times
- **Testable**: Pass mock configs in tests
- **Discoverable**: Builder shows all options

## Presets

Each config class provides static presets for common use cases:

```kotlin
// Player presets
SonixPlayerConfig.DEFAULT   // Play once at normal volume
SonixPlayerConfig.LOOPING   // Infinite loop

// Recorder presets
SonixRecorderConfig.VOICE     // 16kHz mono for speech/singing
SonixRecorderConfig.STANDARD  // 44.1kHz stereo
SonixRecorderConfig.HIGH      // 48kHz stereo high fidelity

// Detector presets
PitchDetectorConfig.BALANCED  // Good tradeoff
PitchDetectorConfig.PRECISE   // Higher accuracy, more CPU
PitchDetectorConfig.RELAXED      // Lower latency, less accuracy
```

Start with presets, then customize with Builder if needed.

## Resource Lifecycle

All resources follow consistent lifecycle patterns:

### Create → Use → Release

```kotlin
// Create
val player = SonixPlayer.create("song.mp3")

// Use
player.play()
player.pause()

// Release (ALWAYS do this!)
player.release()
```

### AutoCloseable Support

Use Kotlin's `use` block for automatic cleanup:

```kotlin
CalibraPitch.createDetector().use { detector ->
    val point = detector.detect(samples, sampleRate)
    // detector.close() called automatically
}
```

### Suspend Functions

Some creation functions are suspending (marked with `suspend`):

```kotlin
// Player loading is async (decodes audio)
suspend fun createPlayer() {
    val player = SonixPlayer.create("song.mp3")  // suspend
    player.play()
}

// Session preparation is async (extracts features)
suspend fun startSession() {
    val session = CalibraLiveEval.create(...)
    session.prepareSession()  // suspend
    session.startPracticingSegment(0)
}
```

## Ownership

Resources are either **owned** or **borrowed**:

### Owned Resources

The receiving object is responsible for cleanup:

```kotlin
// CalibraLiveEval OWNS the detector
val session = CalibraLiveEval.create(
    reference = material,
    detector = detector  // Session takes ownership
)
session.closeSession()  // Detector is released here
```

### Borrowed Resources

The caller remains responsible for cleanup:

```kotlin
// CalibraLiveEval BORROWS player and recorder
val session = CalibraLiveEval.create(
    reference = material,
    detector = detector,
    player = player,      // Caller owns
    recorder = recorder   // Caller owns
)

session.closeSession()       // Only releases detector
player.release()      // Caller must release
recorder.release()    // Caller must release
```

The documentation and KDoc always specify ownership.

## StateFlow Observation

Real-time state is exposed via Kotlin StateFlows:

```kotlin
// Observe playback position
player.currentTime.collect { timeMs ->
    updateSeekBar(timeMs)
}

// Observe recording level
recorder.level.collect { level ->
    updateVuMeter(level)
}

// Observe session state
session.state.collect { state ->
    updateUI(state)
}
```

In Swift, use the provided async sequences:

```swift
for await timeMs in player.currentTimeStream() {
    updateSeekBar(timeMs)
}
```

## Callbacks vs StateFlows

Both are available. Choose based on preference:

### StateFlows (Reactive)

```kotlin
// Observe all updates
session.phase.collect { phase ->
    updatePhaseUI(phase)
}
```

### Callbacks (Event-based)

```kotlin
// Register specific handlers
session.onPhaseChanged { phase ->
    updatePhaseUI(phase)
}

session.onSegmentComplete { result ->
    showScore(result)
}
```

## Sample Rate Handling

VoxaTrace handles sample rate conversions internally:

```kotlin
// Pass any sample rate - internal resampling to 16kHz
val point = detector.detect(samples, sampleRate = 48000)

// Works the same
val point = detector.detect(samples, sampleRate = 44100)
val point = detector.detect(samples, sampleRate = 16000)
```

This means you don't need to resample audio yourself. Just pass the hardware sample rate.

## Error Handling

Errors are communicated via:

### Return Values

```kotlin
val result = session.finishPracticingSegment()
if (result == null) {
    // Session wasn't in PRACTICING state
}
```

### StateFlow

```kotlin
player.error.collect { error ->
    if (error != null) {
        showError(error.message)
    }
}
```

### Callbacks

```kotlin
SonixPlayerConfig.Builder()
    .onError { message -> showError(message) }
    .build()
```

## Swift Interop

Kotlin Multiplatform generates Swift-friendly APIs. Some patterns to note:

### Companion Objects

```kotlin
// Kotlin
SonixPlayer.create(...)
```

```swift
// Swift
SonixPlayer.companion.create(...)
```

### Named Parameters

```kotlin
// Kotlin
detector.detect(samples, sampleRate = 48000)
```

```swift
// Swift
detector.detect(samples: samples, sampleRate: 48000)
```

### Suspending Functions

```kotlin
// Kotlin
suspend fun create(): SonixPlayer
```

```swift
// Swift (async/await)
func create() async throws -> SonixPlayer
```

## Next Steps

- [Installation](../getting-started/installation) - Set up VoxaTrace
- [Android Quickstart](../getting-started/android-quickstart) - Your first app
- [iOS Quickstart](../getting-started/ios-quickstart) - Your first iOS app
