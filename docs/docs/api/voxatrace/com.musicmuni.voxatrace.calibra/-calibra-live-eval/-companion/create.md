//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraLiveEval](../index.md)/[Companion](index.md)/[create](create.md)

# create

[common]\
fun [create](create.md)(reference: [LessonMaterial](../../../com.musicmuni.voxatrace.calibra.model/-lesson-material/index.md), session: [SessionConfig](../../../com.musicmuni.voxatrace.calibra.model/-session-config/index.md) = SessionConfig.DEFAULT, detector: [CalibraPitch.Detector](../../-calibra-pitch/-detector/index.md), player: [SonixPlayer](../../../com.musicmuni.voxatrace.sonix/-sonix-player/index.md)? = null, recorder: [SonixRecorder](../../../com.musicmuni.voxatrace.sonix/-sonix-recorder/index.md)? = null): [CalibraLiveEval](../index.md)

Create a new live evaluation session.

Session takes ownership of the detector and will close it when the session closes. Player and recorder are NOT owned - caller manages their lifecycle.

## Usage

### Low-Level API (manual audio management)

```kotlin
val session = CalibraLiveEval.create(
    reference,
    detector = CalibraPitch.createDetector()
)
session.prepareSession()
session.startPracticingSegment(0)
// Manually feed audio...
```

### Convenience API (library manages audio)

```kotlin
val session = CalibraLiveEval.create(
    reference,
    detector = CalibraPitch.createDetector(),
    player = player,
    recorder = recorder
)
session.prepareSession()
session.onSegmentComplete { result -> showScore(result) }
session.startPracticingSegment(0)  // Single call handles everything
```

#### Return

New session instance

#### Parameters

common

| | |
|---|---|
| reference | The lesson material (reference audio, segments, key) |
| session | Session configuration (default: DEFAULT) |
| detector | Pitch detector (required). Session takes ownership. |
| player | Optional SonixPlayer for audio coordination. Caller manages lifecycle. |
| recorder | Optional SonixRecorder for audio coordination. Caller manages lifecycle. |
