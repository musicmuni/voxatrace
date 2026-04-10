# Audio Latency

Mobile audio pipelines introduce latency between the application and the hardware. VoxaTrace measures and compensates for this automatically, and exposes the measured values for advanced use cases.

## What VoxaTrace Handles

- **`SonixPlayer.currentTime`** reports **presentation time** — when audio actually reaches the speaker — not the internal write position. This means the now-line in a karaoke UI tracks the audible audio, not a buffered-ahead position.

- **`CalibraLiveEval`** automatically compensates for input latency when emitting live pitch points, so the user's pitch contour aligns with the reference contour.

No action is needed for standard `SonixPlayer` + `CalibraLiveEval` usage.

## Public API

For features that correlate recorded audio timing with playback position outside of `CalibraLiveEval`, use these properties:

```kotlin
// Delay from AudioTrack write to speaker output
val player: SonixPlayer = ...
val outputMs = player.outputLatencyMs  // e.g., 210 on a Samsung M21

// Delay from microphone capture to app delivery
val recorder: SonixRecorder = ...
val inputMs = recorder.inputLatencyMs  // e.g., 60 on a Samsung M21
```

### Platform Behavior

| Property | Android | iOS |
|----------|---------|-----|
| `outputLatencyMs` | Measured at runtime during playback (from `AudioTimestamp`). Returns 0 before playback starts. | Available immediately from `AVAudioSession.outputLatency`. |
| `inputLatencyMs` | Measured at runtime from first recording buffer. Returns 0 before recording starts. | Available immediately from `AVAudioSession.inputLatency`. |

### Typical Values

| Device Class | Output | Input | Total |
|-------------|--------|-------|-------|
| Budget Android (speaker) | 150-300ms | 40-100ms | 200-400ms |
| Mid-range Android (speaker) | 80-200ms | 30-80ms | 110-280ms |
| iOS (built-in speaker) | 15-30ms | 5-15ms | 20-45ms |
| Any device (wired headphones) | Lower | Lower | Lower |
| Any device (Bluetooth) | Higher | Higher | Higher |

Latency changes with audio route. On iOS, observe `AVAudioSession.routeChangeNotification` to react to route changes.
