//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraLiveEval](index.md)

# CalibraLiveEval

class [CalibraLiveEval](index.md)

Real-time singing evaluation session with segment support.

## What is Live Evaluation?

Live evaluation scores a singer's performance **in real-time** by comparing their pitch to a reference melody. Use it for:

- 
   **Karaoke apps**: Score singing as users perform
- 
   **Music education**: Provide instant feedback on pitch accuracy
- 
   **Practice apps**: Track improvement across attempts

The session breaks a song into **segments** (phrases or sections), evaluates each one, and tracks the user's progress.

## When to Use

| Scenario | Use This? | Why |
|---|---|---|
| Score singing against reference | Yes | Core use case |
| Just detect pitch (no scoring) | No | Use `CalibraPitch.createDetector()` |
| Analyze recorded audio (not live) | No | Use `CalibraMelodyEval` |
| Voice activity detection only | No | Use `CalibraVAD` |

## Quick Start

### Kotlin

```kotlin
// 1. Create detector and session
val detector = CalibraPitch.createDetector()
val session = CalibraLiveEval.create(lessonMaterial, detector = detector)

// 2. Prepare (loads reference, extracts features)
session.prepareSession()

// 3. Start segment and feed audio
session.startPracticingSegment(0)
recorder.audioBuffers.collect { buffer ->
    session.feedAudioSamples(buffer.toFloatArray(), sampleRate = 48000)
}

// 4. Get result
val result = session.finishPracticingSegment()
println("Score: ${result?.score}")

// 5. Cleanup
session.closeSession()
```

### Swift

```swift
// 1. Create detector and session
let detector = CalibraPitch.companion.createDetector()
let session = CalibraLiveEval.companion.create(
    reference: lessonMaterial,
    detector: detector
)

// 2. Prepare (loads reference, extracts features)
try await session.prepareSession()

// 3. Start segment and feed audio
session.startPracticingSegment(index: 0)
for await buffer in recorder.audioBuffers {
    session.feedAudioSamples(samples: buffer.toFloatArray(), sampleRate: 48000)
}

// 4. Get result
if let result = session.finishPracticingSegment() {
    print("Score: \(result.score)")
}

// 5. Cleanup
session.closeSession()
```

## Usage Tiers (ADR-001)

### Tier 1: Convenience API (80% of users)

Pass player and recorder handles; the session coordinates everything.

```kotlin
val session = CalibraLiveEval.create(
    reference = lessonMaterial,
    detector = CalibraPitch.createDetector(),
    player = player,
    recorder = recorder
)
session.prepareSession()
session.onSegmentComplete { result -> showScore(result) }
session.startPracticingSegment(0)  // Seeks, plays, records, scores automatically
```

### Tier 2: Low-Level API (15% of users)

Manually manage audio; full control over timing.

```kotlin
val session = CalibraLiveEval.create(reference, detector = detector)
session.prepareSession()
session.startPracticingSegment(0)
recorder.audioBuffers.collect { buffer ->
    session.feedAudioSamples(buffer.toFloatArray(), buffer.sampleRate)
}
val result = session.finishPracticingSegment()
```

## Phase Progressions

- 
   **Singalong:**`IDLE → SINGING → EVALUATED`
- 
   **Singafter:**`IDLE → LISTENING → SINGING → EVALUATED`

Observe `phase` StateFlow for UI updates.

## State Machine

```kotlin
IDLE ──prepareSession()──► READY
READY ──startPracticingSegment()──► PRACTICING
PRACTICING ──finishPracticingSegment()──► BETWEEN (or COMPLETED if last)
PRACTICING ──discardCurrentSegment()──► BETWEEN
PRACTICING ──seekToSegment()──► PRACTICING (new segment)
BETWEEN ──startPracticingSegment()──► PRACTICING
BETWEEN ──advanceToNextSegment()──► PRACTICING (or COMPLETED if last)
BETWEEN ──finishSession()──► COMPLETED
* ──closeSession()──► (released)
```

## Ownership Model (per ADR-001)

| Dependency | Ownership | Rationale |
|---|---|---|
| `detector` | **Owned** - session closes it | Created specifically for this session |
| `player` | **Borrowed** - caller manages | Shared resource, UI may need direct access |
| `recorder` | **Borrowed** - caller manages | Shared resource, may be reused |

#### See also

| | |
|---|---|
| [CalibraPitch](../-calibra-pitch/index.md) | For pitch detection without scoring |
| [LessonMaterial](../../com.musicmuni.voxatrace.calibra.model/-lesson-material/index.md) | Input format with reference audio and segments |
| [SessionConfig](../../com.musicmuni.voxatrace.calibra.model/-session-config/index.md) | Configuration for auto-advance, thresholds, etc. |
| [SegmentResult](../../com.musicmuni.voxatrace.calibra.model/-segment-result/index.md) | Per-segment scoring results |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [activeSegment](active-segment.md) | [common]<br/>val [activeSegment](active-segment.md): StateFlow&lt;[ActiveSegmentState](../../com.musicmuni.voxatrace.calibra.model/-active-segment-state/index.md)?&gt;<br/>Current active segment state, or null if not practicing. |
| [completedSegments](completed-segments.md) | [common]<br/>val [completedSegments](completed-segments.md): StateFlow&lt;[Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[SegmentResult](../../com.musicmuni.voxatrace.calibra.model/-segment-result/index.md)&gt;&gt;&gt;<br/>Map of segment index to list of attempts (from SegmentResultStore). |
| [currentTime](current-time.md) | [common]<br/>val [currentTime](current-time.md): StateFlow&lt;[Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)&gt;<br/>Current playback position in seconds (from player or clock in manual mode). |
| [isPlaying](is-playing.md) | [common]<br/>val [isPlaying](is-playing.md): StateFlow&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt;<br/>Whether the player is currently playing. |
| [isRecording](is-recording.md) | [common]<br/>val [isRecording](is-recording.md): StateFlow&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt;<br/>Whether recording is active. |
| [livePitch](live-pitch.md) | [common]<br/>val [livePitch](live-pitch.md): StateFlow&lt;[PitchPoint](../../com.musicmuni.voxatrace.calibra.model/-pitch-point/index.md)&gt;<br/>Real-time pitch point for visualization (includes time and confidence). |
| [livePitchContour](live-pitch-contour.md) | [common]<br/>val [livePitchContour](live-pitch-contour.md): StateFlow&lt;[PitchContour](../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md)&gt;<br/>Live pitch contour accumulated during the current segment. |
| [phase](phase.md) | [common]<br/>val [phase](phase.md): StateFlow&lt;[PracticePhase](../../com.musicmuni.voxatrace.calibra.model/-practice-phase/index.md)&gt;<br/>Current practice phase. Observe this for unified singalong/singafter UI. |
| [pitchProcessingEnabled](pitch-processing-enabled.md) | [common]<br/>val [pitchProcessingEnabled](pitch-processing-enabled.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Whether pitch processing is currently enabled |
| [referenceKeyHz](reference-key-hz.md) | [common]<br/>val [referenceKeyHz](reference-key-hz.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Reference key in Hz from LessonMaterial. |
| [segments](segments.md) | [common]<br/>val [segments](segments.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Segment](../../com.musicmuni.voxatrace.calibra.model/-segment/index.md)&gt;<br/>All segments from the reference. |
| [state](state.md) | [common]<br/>val [state](state.md): StateFlow&lt;[SessionState](../../com.musicmuni.voxatrace.calibra.model/-session-state/index.md)&gt;<br/>Current session state. Observe this in your UI. |
| [studentKeyHz](student-key-hz.md) | [common]<br/>val [studentKeyHz](student-key-hz.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Current student key in Hz. 0 = same as reference. |

## Functions

| Name | Summary |
|---|---|
| [advanceToNextSegment](advance-to-next-segment.md) | [common]<br/>fun [advanceToNextSegment](advance-to-next-segment.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Advance to the next segment. |
| [beginSingingPhase](begin-singing-phase.md) | [common]<br/>fun [beginSingingPhase](begin-singing-phase.md)()<br/>Manually trigger LISTENING → SINGING transition. |
| [close](close.md) | [common]<br/>open fun [close](close.md)()<br/>Release all resources. |
| [closeSession](close-session.md) | [common]<br/>fun [closeSession](close-session.md)()<br/>Close the session and release all resources. |
| [discardCurrentSegment](discard-current-segment.md) | [common]<br/>fun [discardCurrentSegment](discard-current-segment.md)()<br/>Discard the current segment without scoring. |
| [feedAudioSamples](feed-audio-samples.md) | [common]<br/>fun [feedAudioSamples](feed-audio-samples.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000)<br/>Feed audio samples to the session. |
| [finishPracticingSegment](finish-practicing-segment.md) | [common]<br/>fun [finishPracticingSegment](finish-practicing-segment.md)(): [SegmentResult](../../com.musicmuni.voxatrace.calibra.model/-segment-result/index.md)?<br/>Finish the current segment and get its result. |
| [finishSession](finish-session.md) | [common]<br/>fun [finishSession](finish-session.md)(): [SingingResult](../../com.musicmuni.voxatrace.calibra.model/-singing-result/index.md)<br/>Finish the session and get aggregated results. |
| [getResultsForSegment](get-results-for-segment.md) | [common]<br/>fun [getResultsForSegment](get-results-for-segment.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[SegmentResult](../../com.musicmuni.voxatrace.calibra.model/-segment-result/index.md)&gt;?<br/>Get all results for a specific segment. |
| [hasCompletedSegment](has-completed-segment.md) | [common]<br/>fun [hasCompletedSegment](has-completed-segment.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if a segment has been completed at least once. |
| [onPhaseChanged](on-phase-changed.md) | [common]<br/>fun [onPhaseChanged](on-phase-changed.md)(callback: ([PracticePhase](../../com.musicmuni.voxatrace.calibra.model/-practice-phase/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html))<br/>Register callback for phase changes. Called when practice phase transitions (e.g., LISTENING → SINGING). |
| [onReferenceEnd](on-reference-end.md) | [common]<br/>fun [onReferenceEnd](on-reference-end.md)(callback: ([Segment](../../com.musicmuni.voxatrace.calibra.model/-segment/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html))<br/>Register callback for reference end (singafter mode). Called when reference audio finishes playing, before student starts singing. |
| [onSegmentComplete](on-segment-complete.md) | [common]<br/>fun [onSegmentComplete](on-segment-complete.md)(callback: ([SegmentResult](../../com.musicmuni.voxatrace.calibra.model/-segment-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html))<br/>Register callback for segment completion. Called when a segment finishes with its result. |
| [onSessionComplete](on-session-complete.md) | [common]<br/>fun [onSessionComplete](on-session-complete.md)(callback: ([SingingResult](../../com.musicmuni.voxatrace.calibra.model/-singing-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html))<br/>Register callback for session completion. Called when all segments are finished. |
| [pausePlayback](pause-playback.md) | [common]<br/>fun [pausePlayback](pause-playback.md)()<br/>Pause playback and recording. |
| [prepareSession](prepare-session.md) | [common]<br/>suspend fun [prepareSession](prepare-session.md)()<br/>Prepare the session for practice. |
| [restartSession](restart-session.md) | [common]<br/>fun [restartSession](restart-session.md)(fromSegment: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0)<br/>Restart the session from a clean state. |
| [resumePlayback](resume-playback.md) | [common]<br/>fun [resumePlayback](resume-playback.md)()<br/>Resume from paused state. |
| [retryCurrentSegment](retry-current-segment.md) | [common]<br/>fun [retryCurrentSegment](retry-current-segment.md)()<br/>Retry the current segment. |
| [seekToSegment](seek-to-segment.md) | [common]<br/>fun [seekToSegment](seek-to-segment.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))<br/>Seek to a specific segment (discards current attempt if practicing). |
| [seekToTime](seek-to-time.md) | [common]<br/>fun [seekToTime](seek-to-time.md)(seconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Seek to a specific time position. |
| [setPitchProcessingEnabled](set-pitch-processing-enabled.md) | [common]<br/>fun [setPitchProcessingEnabled](set-pitch-processing-enabled.md)(enabled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html))<br/>Enable or disable pitch processing (smoothing + octave correction) at runtime. |
| [setStudentKeyHz](set-student-key-hz.md) | [common]<br/>fun [setStudentKeyHz](set-student-key-hz.md)(keyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Set student key for transposition. |
| [startPracticingSegment](start-practicing-segment.md) | [common]<br/>fun [startPracticingSegment](start-practicing-segment.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))<br/>Start practicing a specific segment. |
