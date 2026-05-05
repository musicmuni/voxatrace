---
sidebar_position: 5
---

# TesseraSession

Streaming multi-metric session — feed audio (or pitch) incrementally, get breath / agility / range results on demand. The streaming counterpart to [`Tessera.analyze`](./overview).

## Quick Start

### Kotlin

```kotlin
val session = TesseraSession.create()

recorder.audioBuffers.collect { buffer ->
    session.addAudio(buffer.toFloatArray(), sampleRate = buffer.sampleRate)
}

val result = session.getResult()
println("Breath: ${result.breath?.controlScore}")
session.release()
```

### Swift

```swift
let session = TesseraSession.create()

for await buffer in recorder.audioBuffers {
    session.addAudio(samples: buffer.samples, sampleRate: Int32(buffer.sampleRate))
}

let result = session.getResult()
session.release()
```

## Factory

```kotlin
fun create(
    metrics: Set<TesseraMetric> = TesseraMetric.ALL,
    breathConfig: BreathConfig = BreathConfig.DEFAULT,
    agilityConfig: AgilityConfig = AgilityConfig.DEFAULT,
    rangeConfig: SearchVectorConfig = SearchVectorConfig.DEFAULT,
    detectorConfig: PitchDetectorConfig = PitchDetectorConfig.BALANCED,
): TesseraSession
```

The session always creates an internal `PitchDetector` from `detectorConfig` for `addAudio`. If `TesseraMetric.VOCAL_RANGE` is in `metrics`, a streaming `RangeDetector` is also created.

## Methods

| Method | Description |
|--------|-------------|
| `addAudio(samples, sampleRate = 16000)` | Feed raw audio. Resamples to 16 kHz internally; runs pitch detection; updates contour and range histogram. |
| `addPitch(time, pitchHz, confidence)` | Feed a pre-extracted pitch sample. Skips internal detection. |
| `getResult(): TesseraResult` | Compute breath/agility on the accumulated contour; reads streaming range histogram. May be called multiple times. |
| `reset()` | Clear accumulated data. |
| `release()` | Free pitch detector and range detector. |

## Result

```kotlin
data class TesseraResult(
    val breath: BreathMetrics?,
    val agility: AgilityScore?,
    val vocalRange: VocalRangeResult?,
)
```

A `null` field means either:
- The metric was not requested (`metrics` set excluded it), or
- For breath/agility: the contour has fewer than 2 samples (caught and converted to `null` so streaming sessions don't throw mid-stream).
- For range: insufficient streaming data so far.

## Common Pitfalls

1. **Always `release()`.** Holds a pitch detector and (when `VOCAL_RANGE` is requested) a range detector.
2. **`getResult()` recomputes breath/agility on the full contour each call.** Range is cheap (incremental histogram).
3. **Max session duration is 10 minutes.** Older pitch points are trimmed from the accumulator.
4. **`addAudio` requires the internal detector.** It is always created by `create()`. If you only ever call `addPitch`, the detector is created but unused (small overhead, not an error).

## See also

- [Tessera (batch counterpart)](./overview)
- [TesseraRangeSession](./range#tesserarangesession) — guided range detection (different domain)
