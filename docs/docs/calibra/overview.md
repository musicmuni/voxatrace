---
sidebar_position: 1
---

# Calibra Overview

Calibra is the **singing-evaluation** module of VoxaTrace. It scores a singer's performance against a reference, runs voice-activity detection, and applies real-time vocal effects.

| Facade | Purpose |
|--------|---------|
| [`CalibraLiveEval`](./live-eval) | Real-time singing evaluation against a reference (segment-aware, observable state) |
| [`CalibraMelodyEval`](./melody-eval) | Offline melody scoring of a complete recording |
| [`CalibraNoteEval`](./note-eval) | Per-note scoring for scales, arpeggios, svara patterns |
| [`CalibraVAD`](./vad) | Voice activity detection (multiple backends) |
| [`CalibraEffects`](./effects) | Real-time vocal effects chain (noise gate, compressor, reverb) |
| [Utilities](./utilities) | Shared model types (Segment, SessionConfig, SegmentResult, …) |

Calibra **consumes** but does not own (1.x → 2.0):
- Pitch detection — [`tona.PitchDetection`](../tona/pitch-detection) (1.x: `CalibraPitch`).
- Voice metrics — [`tessera`](../tessera/overview) (1.x: `CalibraBreath`, `CalibraVocalRange`, `CalibraSpeakingPitch`; new in 2.0: `TesseraAgility`, multi-metric `Tessera.analyze` / `TesseraSession`, batch range + matching).
- Music theory — [`common.MusicTheory`](../common/music-theory) (1.x: `CalibraMusic`; new in 2.0: `MusicTheory.deriveUserShruti`).
- Intonation analysis & scoring — [`accura.Accura`](../accura/intonation). **New public facade in 2.0** — no 1.x equivalent.

The 1.x deprecation shells (`CalibraPitch`, `CalibraBreath`, `CalibraVocalRange`, `VocalRangeSession`, `CalibraSpeakingPitch`, `CalibraMusic`) were **removed** in 2.0.0 — there is no source-compat path. See [CHANGELOG](https://github.com/musicmuni/voxatrace/blob/main/CHANGELOG.md) for the migration table.

## Quick Start

### Real-time scoring (CalibraLiveEval)

```kotlin
val detector = PitchDetection.createDetector()
val session = CalibraLiveEval.create(lessonMaterial, detector = detector)

session.prepareSession()
session.onSegmentComplete { result ->
    println("Segment ${result.segment.index}: ${(result.score * 100).toInt()}%")
}
session.startPracticingSegment(0)

recorder.audioBuffers.collect { buffer ->
    session.feedAudioSamples(
        samples = buffer.toFloatArray(),
        sampleRate = buffer.sampleRate,
        captureTimestampNanos = buffer.timestamp,
    )
}

session.closeSession()
```

### Offline melody evaluation (CalibraMelodyEval)

```kotlin
val extractor = PitchDetection.createContourExtractor(ContourExtractorConfig.SCORING)
val result = CalibraMelodyEval.evaluate(reference, student, extractor)
extractor.release()
println("Overall: ${result.overallScorePercent}%")
```

### Voice activity detection (CalibraVAD)

```kotlin
val vad = CalibraVAD.create(VADModelProvider.General)
val ratio = vad.getVADRatio(samples, sampleRate)
if (ratio > 0.5f) println("Voice present")
vad.release()
```

### Real-time effects (CalibraEffects)

```kotlin
val effects = CalibraEffects.create(EffectsPreset.VOCAL_CHAIN)
recorder.audioBuffers.collect { buffer ->
    val samples = buffer.toFloatArray()
    effects.process(samples)   // in-place; expects 16kHz mono
}
effects.release()
```

## Two evaluation modes (Live Eval)

- **Singalong:** `IDLE → SINGING → EVALUATED`. Reference plays while the student sings; pitch is compared in real time.
- **Singafter:** `IDLE → LISTENING → SINGING → EVALUATED`. Reference plays first, then the student sings the same phrase from memory.

Mode is determined per-segment by `Segment.studentStartSeconds` (see [Utilities](./utilities)).

## Failure semantics (ADR-022)

| Kind | How it surfaces |
|------|-----------------|
| Caller bug (uninitialized SDK, invalid config) | Throws (`VoxaTraceNotInitializedException`, `IllegalArgumentException`) |
| Domain inconclusive (e.g. evaluator failed to extract any student data for a segment) | `finishPracticingSegment` returns `null` |

## See also

- [Live Evaluation Concept](../concepts/live-evaluation)
- [Voice Activity Concept](../concepts/voice-activity)
- [Tona / PitchDetection](../tona/pitch-detection) — for pitch detection
- [Tessera](../tessera/overview) — for voice metrics
- [Accura](../accura/intonation) — for intonation scoring
- [MusicTheory](../common/music-theory) — for music conversions
