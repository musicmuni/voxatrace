---
sidebar_position: 9
---

# CalibraBreath (removed in 2.0.0)

:::danger Removed — migration required
`CalibraBreath` was **removed** in 2.0.0. There is no source-compat shell. The new contract operates on a `tona.model.PitchContour` instead of parallel `(times, pitchesHz)` arrays, and the canonical return type is now [`BreathMetrics { controlScore, phrases, alignmentScore }`](../tessera/breath#breathmetrics) on the [`TesseraBreath`](../tessera/breath) facade. 1.x callers must migrate before they will compile against 2.0.
:::

The new API operates on `tona.model.PitchContour` instead of parallel `times` / `pitchesHz` arrays, and the canonical return type is the unified `BreathMetrics`, not the legacy `calibra.model.BreathMetrics` (which used `-1f` as a failure sentinel) or the short-lived 2.0.0 `BreathScore`.

## Where to find each piece

| Old API | New API |
|---------|---------|
| `CalibraBreath.computeCapacity(times, pitchesHz)` | [`TesseraBreath.analyze(contour).phrases?.longestDuration`](../tessera/breath) — LOF-filtered peak phrase, in seconds |
| `CalibraBreath.computeMetrics(refTimes, refPitches, studentTimes, studentPitches, …)` | [`TesseraBreath.analyze(studentContour, referenceContour, config)`](../tessera/breath) — single call returns control + phrases + alignmentScore |
| `CalibraBreath.hasEnoughData(times, pitchesHz)` | Construct `PitchContour.fromArrays(times, pitchesHz)` and check `contour.size >= 2` (full check is inside `TesseraBreath`) |
| `BreathMetrics { capacity, control, isValid }` (calibra) | [`BreathMetrics { controlScore, phrases, alignmentScore }`](../tessera/breath#breathmetrics) (tessera) |

## Migration

```kotlin
// Before
val capacity = CalibraBreath.computeCapacity(times, pitchesHz)   // -1f on failure

// After
val contour = PitchContour.fromArrays(times, pitchesHz)
val metrics = TesseraBreath.analyze(contour, config = BreathConfig.DEFAULT)
val capacity = metrics.phrases?.longestDuration   // null when phrases can't be segmented
```

For full reference, see [TesseraBreath](../tessera/breath).
