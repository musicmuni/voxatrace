---
sidebar_position: 14
---

# AudioSessionManager

Cross-platform audio session / focus coordination (ADR-016). Configures the system audio session (iOS `AVAudioSession`) and audio focus (Android `AudioFocus`) so playback and recording cooperate instead of racing (which otherwise causes silent playback or dropped recordings).

:::note Usually automatic
You rarely call this directly. `SonixPlayer.create(...)`, `SonixRecorder.create(...)`, and `SonixMixer.create(...)` each take an `audioSession: AudioMode` parameter and call `AudioSessionManager.configure(...)` for you. Use `AudioSessionManager` directly only when you coordinate the session yourself or need focus-change callbacks.
:::

## Quick Start

### Kotlin

```kotlin
AudioSessionManager.configure(AudioMode.PLAY_AND_RECORD, echoCancellation = true)

AudioSessionManager.addFocusListener(object : AudioFocusListener {
    override fun onFocusGained() { player.play() }
    override fun onFocusLost(transient: Boolean) { player.pause() }
    override fun onDuck() { player.volume = 0.3f }
})

// When done
AudioSessionManager.deactivate()
```

### Swift

```swift
AudioSessionManager.configure(mode: .playAndRecord, echoCancellation: true)

// Closure-based focus listener (returns an opaque token for removeFocusListener)
let token = AudioSessionManager.addFocusListener(
    onFocusGained: { player.play() },
    onFocusLost: { transient in player.pause() },
    onDuck: { player.volume = 0.3 }
)

AudioSessionManager.deactivate()
```

## AudioMode

```kotlin
enum class AudioMode { PLAYBACK, RECORDING, PLAY_AND_RECORD }
```

| Mode | Use for |
|------|---------|
| `PLAYBACK` | Playback only (music, backing tracks) |
| `RECORDING` | Recording only (voice capture) |
| `PLAY_AND_RECORD` | Simultaneous playback + recording (karaoke, singalong) |

## Methods

| Method | Description |
|--------|-------------|
| `configure(mode: AudioMode, echoCancellation: Boolean = false): Boolean` | Configure the session for `mode`. `echoCancellation` applies to recording modes. Returns `true` on success. |
| `deactivate()` | Deactivate the session and release focus. |
| `addFocusListener(listener: AudioFocusListener)` | Register a focus-change listener. |
| `removeFocusListener(listener: AudioFocusListener)` | Remove a previously added listener. |
| `getAudioSessionId(): Int` | Shared audio session ID for player/recorder coordination. **Android**: a real shared id (enables AEC). **iOS**: returns `0` (iOS coordinates via `AVAudioSession`). |

In Swift, `configure` / `deactivate` / `addFocusListener` / `removeFocusListener` are exposed as static methods (no `.shared`); `addFocusListener` takes closures (`onFocusGained:onFocusLost:onDuck:`) and returns an `AudioFocusListener` token.

## Properties

| Property | Type | Kotlin | Swift | Description |
|----------|------|--------|-------|-------------|
| Current mode | `AudioMode?` | `currentMode` | `activeMode` | Active mode, or `null` if not configured |
| Active | `Boolean` | `isActive` | `isSessionActive` | Whether the session is currently active |
| Hardware rate | `Int` | `hardwareSampleRate` | `hardwareSampleRate` | Hardware's preferred capture rate (typically 44100/48000). iOS ignores requested rates and uses this; match your DSP to it (ADR-017). Lazily initialized on first access. |
| AEC available | `Boolean` | `isEchoCancellationAvailable` | `isEchoCancellationAvailable` | Whether acoustic echo cancellation will work when `echoCancellation = true` is passed to `configure`. **Android**: requires API 29+. **iOS**: system-level (VoiceChat mode / `setPrefersEchoCancelledInput`). |

## AudioFocusListener

```kotlin
interface AudioFocusListener {
    fun onFocusGained()
    fun onFocusLost(transient: Boolean)   // transient = can resume; permanent = should stop
    fun onDuck()                          // lower volume temporarily
}
```

## Microphone Permission (iOS only)

iOS exposes async helpers on the Swift side for requesting microphone permission via `AVAudioSession`. There is no Kotlin equivalent — on Android, request `RECORD_AUDIO` through the standard Android permission APIs.

```swift
// Returns true if granted
let granted = await AudioSessionManager.requestMicrophonePermission()

// Or throw MicrophonePermissionDeniedError if denied
do {
    try await AudioSessionManager.ensureMicrophonePermission()
    // proceed with recording
} catch {
    // permission denied
}
```

## See also

- [SonixPlayer](./player) — calls `configure(.playback)` on create
- [SonixRecorder](./recorder) — calls `configure(.recording)` on create (and forwards `echoCancellation`)
- [SonixMixer](./mixer) — calls `configure(.playback)` on create
