---
sidebar_position: 7
---

# Audio Latency

Mobile audio pipelines introduce latency between the application and the hardware. VoxaTrace uses the OS audio engine's hardware-clock timestamps at both ends so mic capture moments and player audible positions live in the same monotonic domain. No app-layer offset math is required (see ADR-021).

## What VoxaTrace Handles

- **`SonixPlayer.currentTime`** reports **presentation time** — when audio actually reaches the DAC — not the internal write position. The now-line in a karaoke UI tracks audible audio, not a buffered-ahead position. (Changed in 1.0.0.)
- **`AudioBuffer.timestamp`** is absolute monotonic nanoseconds at the moment the **last** sample in the buffer was captured at the mic, with input latency already subtracted. (1.0.1+.)
- **`CalibraLiveEval.feedAudioSamples`** accepts a `captureTimestampNanos` parameter that maps a mic-capture moment to player-audible time via the player's own hardware clock. Pass `AudioBuffer.timestamp` straight through; live pitch contour timestamps then align with the reference contour without manual offset math.

No action is needed for standard `SonixPlayer` + `CalibraLiveEval` usage. If you migrated from earlier versions, **remove** any manual `outputLatencyMs` / `inputLatencyMs` corrections you applied — the SDK accounts for both at the source layer now.

## Public API

```kotlin
// Output latency diagnostic (1.0.0+)
val outputMs: Long = player.outputLatencyMs

// Input latency diagnostic — already factored into AudioBuffer.timestamp; do not subtract again
val inputMs: Long = recorder.inputLatencyMs

// Map a mic-capture wall moment to player audible time (1.0.1+)
val anchorMs: Long = player.audibleTimeMsAtWallNanos(buffer.timestamp)
// returns -1L if the player isn't running yet
```

`AudioBuffer.timestamp` is in `CLOCK_MONOTONIC` nanoseconds on Android (same domain as `System.nanoTime()`) and `AVAudioTime.hostTime` converted to nanoseconds on iOS.

### Platform Behavior

| Property | Android | iOS |
|----------|---------|-----|
| `SonixPlayer.currentTime` | DAC presentation time via `AudioTrack.getTimestamp(AudioTimestamp)` | `AVAudioPlayerNode.lastRenderTime` + `playerTimeForNodeTime`, anchored to `AVAudioTime.hostTime` |
| `outputLatencyMs` | Measured at runtime during playback (from `AudioTimestamp`). Returns 0 before playback starts. | Available immediately from `AVAudioSession.outputLatency`. |
| `inputLatencyMs` | Diagnostic; runtime estimate. Authoritative capture time is `AudioBuffer.timestamp`. | Diagnostic; `AVAudioSession.inputLatency`. Authoritative capture time is `AudioBuffer.timestamp`. |
| `AudioBuffer.timestamp` | `AudioRecord.getTimestamp.nanoTime` (`CLOCK_MONOTONIC` nanos) | `AVAudioTime.hostTime` → nanos via mach timebase |
| `audibleTimeMsAtWallNanos` | `AudioTrack.getTimestamp` mapper | `AVAudioPlayerNode.lastRenderTime` + `playerTimeForNodeTime` |

### Typical Values

| Device Class | Output | Input | Total |
|--------------|--------|-------|-------|
| Budget Android (speaker) | 150–300 ms | 40–100 ms | 200–400 ms |
| Mid-range Android (speaker) | 80–200 ms | 30–80 ms | 110–280 ms |
| iOS (built-in speaker) | 15–30 ms | 5–15 ms | 20–45 ms |
| Any device (wired headphones) | Lower | Lower | Lower |
| Any device (Bluetooth) | Higher | Higher | Higher |

Latency changes with audio route. On iOS, observe `AVAudioSession.routeChangeNotification` to react.

## Recipe — correlate recorded audio with playback

```kotlin
recorder.audioBuffers.collect { buffer ->
    // Option A — let CalibraLiveEval do it
    liveEval.feedAudioSamples(
        samples = buffer.toFloatArray(),
        sampleRate = buffer.sampleRate,
        captureTimestampNanos = buffer.timestamp,
    )

    // Option B — do it manually (e.g., custom sync feature outside CalibraLiveEval)
    val anchorMs = player.audibleTimeMsAtWallNanos(buffer.timestamp)
    if (anchorMs >= 0) {
        // anchorMs is the player's audible time at the moment this buffer
        // was captured at the mic.
    }
}
```

Pre-1.0.1 callers who subtracted `inputLatencyMs` manually from a captured timestamp must remove that subtraction — `AudioBuffer.timestamp` already accounts for input latency.
