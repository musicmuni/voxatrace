---
sidebar_position: 3
---

# Live Evaluation

Live evaluation scores singing in real-time by comparing the singer's pitch to a reference melody.

## What is Live Evaluation?

Live evaluation answers: **"How accurately is this person singing the reference melody?"**

It works by:
1. Extracting pitch from the reference audio (what they should sing)
2. Detecting pitch from the student in real-time (what they're singing)
3. Comparing the two and calculating a score

## Use Cases

| App Type | How Live Evaluation Helps |
|----------|---------------------------|
| **Karaoke** | Show score as users sing along |
| **Music education** | Provide instant feedback on pitch accuracy |
| **Practice apps** | Track improvement over multiple attempts |
| **Singing games** | Gamify pitch accuracy |

## Key Concepts

### Segments

Songs are divided into **segments** - typically phrases or sections. Each segment is evaluated independently.

```
Song Timeline:
[Segment 0: "Happy birthday"] [Segment 1: "to you"] [Segment 2: "Happy birthday"]
     ↓                              ↓                        ↓
   Score: 85%                    Score: 92%               Score: 78%
```

Benefits of segmentation:
- Users can retry individual segments
- Granular feedback per phrase
- Easier progress tracking

### Practice Modes

#### Singalong Mode

User sings **simultaneously** with the reference audio.

```
Reference: ──────────────────────────>
Student:   ──────────────────────────>
           ^                         ^
         Start                      End
```

Best for: Karaoke-style apps where users sing along with music.

#### Singafter Mode

User **listens first**, then sings back the phrase.

```
Reference: ────────>
Student:            ────────>
           ^        ^        ^
        Listen   Start     End
```

Best for: Ear training apps where users echo what they hear.

### Scores

Live evaluation returns several score types:

| Score | What It Measures |
|-------|------------------|
| **Overall Score** | Combined pitch + timing accuracy (0.0 to 1.0) |
| **Pitch Accuracy** | How many notes matched the reference pitch |
| **Performance Level** | Qualitative rating (POOR, FAIR, GOOD, GREAT, PERFECT) |

## Session Lifecycle

### 1. Create Session

```kotlin
// Create pitch detector
val detector = CalibraPitch.createDetector()

// Create session with reference material
val session = CalibraLiveEval.create(
    reference = lessonMaterial,  // Contains audio + segment info
    detector = detector
)
```

### 2. Prepare (Load Reference)

```kotlin
// Suspending - extracts features from reference audio
session.prepare()
```

### 3. Practice Segment

```kotlin
// Start practicing segment 0
session.beginSegment(0)

// Feed audio from recorder
recorder.audioBuffers.collect { buffer ->
    session.addAudio(buffer.toFloatArray(), buffer.sampleRate)
}

// End segment and get result
val result = session.endSegmentEarly()
println("Score: ${result?.score}")
```

### 4. Cleanup

```kotlin
session.close()  // Releases detector and resources
```

## API Modes

### Low-Level API

Full control over audio flow. You manage player and recorder.

```kotlin
val session = CalibraLiveEval.create(
    reference = lessonMaterial,
    detector = CalibraPitch.createDetector()
)

session.prepare()
session.beginSegment(0)

// You control when audio is fed
myRecorder.audioFlow.collect { buffer ->
    session.addAudio(buffer.samples, buffer.sampleRate)
}

val result = session.endSegmentEarly()
session.close()
```

### Convenience API

Library coordinates playback, recording, and scoring automatically.

```kotlin
val session = CalibraLiveEval.create(
    reference = lessonMaterial,
    detector = CalibraPitch.createDetector(),
    player = myPlayer,      // Library controls playback
    recorder = myRecorder   // Library controls recording
)

session.prepare()

// Register callbacks
session.onSegmentComplete { result ->
    showScore(result.score)
}

// Single call handles everything
session.playSegment(0)  // Seeks, plays, records, evaluates
```

## Observing State

Use StateFlows to observe session state in your UI:

```kotlin
// Observe session phase
session.state.collect { state ->
    when (state.phase) {
        SessionPhase.IDLE -> showIdleUI()
        SessionPhase.READY -> showReadyUI()
        SessionPhase.PRACTICING -> showPracticingUI(state.segmentProgress)
        SessionPhase.BETWEEN_SEGMENTS -> showScoreUI()
        SessionPhase.COMPLETED -> showCompletionUI()
    }
}

// Observe live pitch for visualization
session.livePitchContour.collect { contour ->
    drawPitchCurve(contour)
}

// Observe practice phase (singalong/singafter)
session.phase.collect { phase ->
    when (phase) {
        PracticePhase.IDLE -> // Waiting
        PracticePhase.LISTENING -> // Listening to reference
        PracticePhase.SINGING -> // User is singing
        PracticePhase.EVALUATED -> // Segment finished
    }
}
```

## Configuration

### Auto-Advance

Automatically move to next segment after scoring:

```kotlin
val session = CalibraLiveEval.create(
    reference = lessonMaterial,
    session = SessionConfig.Builder()
        .autoAdvance(true)
        .build(),
    detector = detector
)
```

### Score Threshold

Require minimum score before advancing (for practice apps):

```kotlin
val session = CalibraLiveEval.create(
    reference = lessonMaterial,
    session = SessionConfig.Builder()
        .scoreThreshold(0.7f)  // Must score 70% to advance
        .maxAttempts(3)        // Allow 3 retries
        .build(),
    detector = detector
)
```

### Key Transposition

When student sings in a different key than reference:

```kotlin
// Student sings 2 semitones lower
session.setStudentKeyHz(referenceKeyHz * 0.89f)  // 2 semitones down = 0.89x
```

## Segment Result

Each segment returns detailed results:

```kotlin
data class SegmentResult(
    val segment: Segment,           // Which segment
    val score: Float,               // Overall score (0.0 to 1.0)
    val pitchAccuracy: Float,       // Pitch accuracy (0.0 to 1.0)
    val level: PerformanceLevel,    // POOR, FAIR, GOOD, GREAT, PERFECT
    val attemptNumber: Int,         // Which attempt this was
    val referencePitch: PitchContour,  // What they should have sung
    val studentPitch: PitchContour     // What they actually sung
)
```

Use pitch contours for visualization:

```kotlin
session.onSegmentComplete { result ->
    // Draw reference and student pitch on same graph
    drawPitchComparison(
        reference = result.referencePitch,
        student = result.studentPitch
    )

    // Show score
    showScore(result.score, result.level)
}
```

## Ownership Model

Understanding resource ownership prevents memory leaks:

| Resource | Owned By | Cleanup |
|----------|----------|---------|
| Detector | Session | `session.close()` releases it |
| Player | Caller | You call `player.release()` |
| Recorder | Caller | You call `recorder.release()` |

```kotlin
// Correct cleanup order
session.close()       // Releases detector
player.release()      // You manage player
recorder.release()    // You manage recorder
```

## Common Patterns

### Retry Segment

```kotlin
session.onSegmentComplete { result ->
    if (result.score < 0.7f) {
        showRetryButton {
            session.retrySegment()
        }
    } else {
        session.beginSegment(result.segment.index + 1)
    }
}
```

### Seek to Segment

```kotlin
// Jump to any segment
segmentButtons.forEachIndexed { index, button ->
    button.onClick {
        session.beginSegment(index)
    }
}
```

### Cancel Mid-Segment

```kotlin
cancelButton.onClick {
    session.cancelSegment()  // Discards current attempt
}
```

## Next Steps

- [CalibraLiveEval API Reference](/api/calibra/CalibraLiveEval) - Full API documentation
- [Live Evaluation Guide](/docs/guides/live-evaluation) - Implementation walkthrough
- [Karaoke App Recipe](/docs/cookbook/karaoke-app) - Complete example
