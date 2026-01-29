---
sidebar_position: 4
---

# Live Evaluation

A complete guide to real-time singing evaluation with CalibraLiveEval.

## What You'll Learn

- Score singing against a reference melody in real-time
- Configure singalong and singafter modes
- Handle segment-based practice with auto-advance
- Display pitch visualization with reference comparison

## Prerequisites

- VoxaTrace installed
- Reference audio with segment annotations (LessonMaterial)

## Quick Start

### Kotlin

```kotlin
// 1. Create detector and session
val detector = CalibraPitch.createDetector()
val session = CalibraLiveEval.create(
    reference = lessonMaterial,
    detector = detector
)

// 2. Prepare (loads reference features)
session.prepare()

// 3. Start segment
session.beginSegment(0)

// 4. Feed audio
recorder.audioBuffers.collect { buffer ->
    session.addAudio(buffer.toFloatArray(), buffer.sampleRate)
}

// 5. Get result
val result = session.endSegmentEarly()
println("Score: ${result?.score}")

// 6. Cleanup
session.close()
```

### Swift

```swift
// 1. Create detector and session
let detector = CalibraPitch.companion.createDetector()
let session = CalibraLiveEval.companion.create(
    reference: lessonMaterial,
    detector: detector
)

// 2. Prepare
try await session.prepare()

// 3. Start segment
session.beginSegment(index: 0)

// 4. Feed audio
for await buffer in recorder.audioBuffersStream() {
    session.addAudio(samples: buffer.toFloatArray(), sampleRate: buffer.sampleRate)
}

// 5. Get result
if let result = session.endSegmentEarly() {
    print("Score: \(result.score)")
}

// 6. Cleanup
session.close()
```

## Low-Level vs Convenience API

### Low-Level API

Full control over audio flow:

```kotlin
val session = CalibraLiveEval.create(
    reference = lessonMaterial,
    detector = detector
)

session.prepare()
session.beginSegment(0)

// You manage recorder
recorder.start()
recorder.audioBuffers.collect { buffer ->
    session.addAudio(buffer.toFloatArray(), buffer.sampleRate)
}
recorder.stop()

val result = session.endSegmentEarly()
```

### Convenience API

Library coordinates playback and recording:

```kotlin
val session = CalibraLiveEval.create(
    reference = lessonMaterial,
    detector = detector,
    player = player,      // Library controls
    recorder = recorder   // Library controls
)

session.prepare()

// Register callbacks
session.onSegmentComplete { result ->
    showScore(result)
}

// Single call handles everything
session.playSegment(0)  // Seeks, plays, records, scores
```

## Configuration

### Auto-Advance

Automatically move to next segment:

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

Require minimum score before advancing:

```kotlin
val session = CalibraLiveEval.create(
    reference = lessonMaterial,
    session = SessionConfig.Builder()
        .scoreThreshold(0.7f)   // Must score 70%
        .maxAttempts(3)         // Up to 3 retries
        .autoAdvance(true)
        .build(),
    detector = detector
)
```

### Key Transposition

When student sings in a different key:

```kotlin
session.setStudentKeyHz(referenceKeyHz * 0.89f)  // 2 semitones down
```

## Observing State

```kotlin
// Session state
session.state.collect { state ->
    when (state.phase) {
        SessionPhase.IDLE -> showIdleUI()
        SessionPhase.READY -> showReadyUI()
        SessionPhase.PRACTICING -> {
            updateProgress(state.segmentProgress)
            showPitch(state.currentPitch)
        }
        SessionPhase.BETWEEN_SEGMENTS -> showScoreUI()
        SessionPhase.COMPLETED -> showCompletionUI()
    }
}

// Practice phase (singalong/singafter)
session.phase.collect { phase ->
    when (phase) {
        PracticePhase.LISTENING -> showListeningIndicator()
        PracticePhase.SINGING -> showSingingIndicator()
        PracticePhase.EVALUATED -> hideIndicators()
    }
}

// Live pitch for visualization
session.livePitchContour.collect { contour ->
    drawPitchCurve(contour)
}
```

## Segment Control

```kotlin
// Jump to specific segment
session.beginSegment(2)

// Retry current segment
session.retrySegment()

// Cancel without scoring
session.cancelSegment()

// End early and get result
val result = session.endSegmentEarly()

// Navigate between segments
segmentButtons.forEachIndexed { index, button ->
    button.onClick { session.beginSegment(index) }
}
```

## Working with Results

```kotlin
session.onSegmentComplete { result ->
    // Show score
    scoreLabel.text = "${(result.score * 100).toInt()}%"

    // Show performance level
    levelLabel.text = result.level.name  // POOR, FAIR, GOOD, GREAT, PERFECT

    // Draw pitch comparison
    drawPitchComparison(
        reference = result.referencePitch,
        student = result.studentPitch
    )

    // Handle based on score
    when {
        result.score >= 0.9f -> showCelebration()
        result.score >= 0.7f -> showEncouragement()
        else -> offerRetry()
    }
}
```

## Complete Example

```kotlin
class PracticeViewModel : ViewModel() {
    private var session: CalibraLiveEval? = null

    val state = MutableStateFlow<SessionState?>(null)
    val currentResult = MutableStateFlow<SegmentResult?>(null)

    suspend fun startSession(lesson: LessonMaterial, player: SonixPlayer, recorder: SonixRecorder) {
        val detector = CalibraPitch.createDetector()

        session = CalibraLiveEval.create(
            reference = lesson,
            session = SessionConfig.Builder()
                .preset(SessionConfig.PRACTICE)
                .autoAdvance(true)
                .scoreThreshold(0.6f)
                .build(),
            detector = detector,
            player = player,
            recorder = recorder
        )

        session?.prepare()

        // Observe state
        viewModelScope.launch {
            session?.state?.collect { state.value = it }
        }

        // Handle segment completion
        session?.onSegmentComplete { result ->
            currentResult.value = result
        }

        // Handle session completion
        session?.onSessionComplete { result ->
            showFinalScore(result.overallScore)
        }
    }

    fun playSegment(index: Int) {
        session?.playSegment(index)
    }

    fun retrySegment() {
        session?.retrySegment()
    }

    fun cleanup() {
        session?.close()
        session = null
    }
}
```

## Next Steps

- [CalibraLiveEval API Reference](/api/calibra/CalibraLiveEval) - Full API documentation
- [Live Evaluation Concepts](/docs/concepts/live-evaluation) - Theory and background
- [Karaoke App Recipe](/docs/cookbook/karaoke-app) - Complete example
