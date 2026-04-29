---
sidebar_position: 4
---

# CalibraVocalRange (deprecated)

:::warning Moved
Vocal range detection has moved to the **`tessera`** module. Both `CalibraVocalRange` and the old `VocalRangeSession` (in `calibra`) are `@Deprecated` shells that delegate to the new facades and will be removed in a future release.
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
- All model types (`VocalPitch`, `DetectedNote`, `VocalRange`, `VocalRangeConfig`, `RangeStats`, `VocalRangePhase`, `VocalRangeState`, `VocalRangeSessionConfig`, `VocalRangeSessionResult`) live under `com.musicmuni.voxatrace.tessera.model`. Source-level typealiases on the calibra side are kept for compilation compatibility.

For full reference, see [TesseraRange](../tessera/range).
