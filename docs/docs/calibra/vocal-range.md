---
sidebar_position: 4
---

# CalibraVocalRange (removed in 2.0.0)

:::danger Removed — migration required
Both `CalibraVocalRange` and the old `VocalRangeSession` (in `calibra`) were **removed** in 2.0.0. There is no source-compat shell. Vocal range detection now lives in the **`tessera`** module. 1.x callers must migrate imports before they will compile against 2.0.
:::

There are now two distinct facades for two distinct workflows:

| Workflow | Old API | New API |
|----------|---------|---------|
| Batch range from a complete recording | `CalibraVocalRange` | [`TesseraRange.computeVocalRange(contour)`](../tessera/range) |
| Interactive guided "find your range" flow | `VocalRangeSession` | [`TesseraRangeSession`](../tessera/range#tesserarangesession) |

The two return-types are also distinct:
- [`VocalRangeResult`](../tessera/range#vocalrangeresult-batch-result) (range + 13-dim search vector) — from the batch facade
- [`VocalRangeSessionResult`](../tessera/range#vocalrangesessionresult) (low / high / naturalShruti) — from the session facade

## Migration

```kotlin
// Before
val vr = CalibraVocalRange.create()
vr.addAudio(samples)
val range = vr.getRange()
vr.release()

// After (batch — when you already have the contour)
val contour = extractor.extract(samples, sampleRate)
val result = TesseraRange.computeVocalRange(contour)

// After (guided session)
val session = TesseraRangeSession.create(detectorConfig = PitchDetectorConfig.BALANCED)
session.start()
recorder.audioBuffers.collect { buffer ->
    session.addAudio(buffer.toFloatArray(), sampleRate = buffer.sampleRate)
}
// observe session.state for progress, then call session.confirmNote()
session.release()
```

Other moves:
- `CalibraVocalRange.labelForMidi(midi)` → [`MusicTheory.midiToNoteLabel(midi.toFloat())`](../common/music-theory)
- All model types (`VocalPitch`, `DetectedNote`, `VocalRange`, `VocalRangeConfig`, `RangeStats`, `VocalRangePhase`, `VocalRangeState`, `VocalRangeSessionConfig`, `VocalRangeSessionResult`) live under `com.musicmuni.voxatrace.tessera.model`. Re-import them from `tessera.model`; there are no calibra-side aliases (the 1.x calibra surface was removed outright, no source-compat shell).

For full reference, see [TesseraRange](../tessera/range).
