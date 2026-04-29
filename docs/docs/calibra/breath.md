---
sidebar_position: 9
---

# CalibraBreath (removed in 2.0.0)

:::danger Removed — migration required
`CalibraBreath` was **removed** in 2.0.0. There is no source-compat shell. The new contract operates on a `tona.model.PitchContour` instead of parallel `(times, pitchesHz)` arrays, and the canonical return type is **`BreathScore { capacity: Float?, controlScore: Float }`** — `capacity` is now nullable rather than the legacy `-1f` failure sentinel. 1.x callers must migrate before they will compile against 2.0.
:::

The new API operates on `tona.model.PitchContour` instead of parallel `times` / `pitchesHz` arrays, and the canonical return type is the **nullable-capacity** `BreathScore`, not the legacy `BreathMetrics` (which used `-1f` as a failure sentinel).

## Where to find each piece

| Old API | New API |
|---------|---------|
| `CalibraBreath.computeCapacity(times, pitchesHz)` | [`TesseraBreath.computeScore(contour).capacity`](../tessera/breath) |
| `CalibraBreath.computeMetrics(refTimes, refPitches, studentTimes, studentPitches, …)` | [`TesseraBreath.compare(refContour, studentContour, config)`](../tessera/breath) |
| `CalibraBreath.hasEnoughData(times, pitchesHz)` | Construct `PitchContour.fromArrays(times, pitchesHz)` and check `contour.size >= 2` (full check is inside `TesseraBreath`) |
| `BreathMetrics` (capacity, control, isValid) | [`BreathScore { capacity: Float?, controlScore: Float }`](../tessera/breath) — capacity is now nullable, not `-1f` |

## Migration

```kotlin
// Before
val capacity = CalibraBreath.computeCapacity(times, pitchesHz)   // -1f on failure

// After
val contour = PitchContour.fromArrays(times, pitchesHz)
val score = TesseraBreath.computeScore(contour, BreathConfig.DEFAULT)
val capacity = score.capacity   // null on failure (must be checked)
```

For full reference, see [TesseraBreath](../tessera/breath).
