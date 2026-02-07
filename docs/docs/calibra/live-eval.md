---
sidebar_position: 5
---

# CalibraLiveEval

Real-time singing evaluation session that scores a singer's performance by comparing their pitch to a reference melody. Supports segment-based progression with automatic advancement, retry logic, and both singalong and singafter practice modes.

## Quick Start

### Kotlin

```kotlin
// 1. Create detector and session
val detector = CalibraPitch.createDetector()
val session = CalibraLiveEval.create(lessonMaterial, detector = detector)

// 2. Prepare (loads reference, creates evaluator)
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
let detector = CalibraPitch.createDetector()
let session = CalibraLiveEval.create(
    reference: lessonMaterial,
    detector: detector
)

// 2. Prepare (loads reference, creates evaluator)
try await session.prepareSession()

// 3. Start segment and feed audio
session.startPracticingSegment(index: 0)
for await buffer in recorder.audioBuffers {
    session.feedAudioSamples(buffer.toFloatArray(), sampleRate: 48000)
}

// 4. Get result
if let result = session.finishPracticingSegment() {
    print("Score: \(result.score)")
}

// 5. Cleanup
session.closeSession()
```

## When to Use

| Scenario | Use This? | Why |
|----------|-----------|-----|
| Score singing against reference in real time | Yes | Core use case |
| Karaoke apps with segment scoring | Yes | Segment-based with auto-advance |
| Music education with retry logic | Yes | Practice mode with score thresholds |
| Just detect pitch (no scoring) | No | Use `CalibraPitch.createDetector()` |
| Analyze pre-recorded audio (not live) | No | Use `CalibraMelodyEval` |
| Voice activity detection only | No | Use `CalibraVAD` |

## Configuration

### Presets

| Preset | Kotlin | Swift | Description |
|--------|--------|-------|-------------|
| Default | `SessionConfig.DEFAULT` | `.default` | Auto-advancing, no score threshold |
| Practice | `SessionConfig.PRACTICE` | `.practice` | Score threshold 70%, max 3 attempts, best-of aggregation |
| Karaoke | `SessionConfig.KARAOKE` | `.karaoke` | Always advances, one attempt per segment |
| Performance | `SessionConfig.PERFORMANCE` | `.performance` | One attempt, no repetition |

### Builder

#### Kotlin

```kotlin
val config = SessionConfig.Builder()
    .preset(SessionConfig.PRACTICE)
    .scoreThreshold(0.6f)
    .maxAttempts(5)
    .resultAggregation(ResultAggregation.BEST)
    .build()

val session = CalibraLiveEval.create(
    reference = lessonMaterial,
    session = config,
    detector = CalibraPitch.createDetector()
)
```

#### Swift

```swift
let config = SessionConfig.Builder()
    .preset(.practice)
    .scoreThreshold(0.6)
    .maxAttempts(5)
    .resultAggregation(.best)
    .build()

let session = CalibraLiveEval.create(
    reference: lessonMaterial,
    session: config,
    detector: CalibraPitch.createDetector()
)
```

### Config Properties

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `autoAdvance` | `Boolean` | `true` | Automatically advance to next segment when current ends |
| `scoreThreshold` | `Float` | `0` | Minimum score to auto-advance (0 = advances regardless of score) |
| `maxAttempts` | `Int` | `0` | Maximum attempts before forced advance (0 = unlimited) |
| `resultAggregation` | `ResultAggregation` | `LATEST` | How to aggregate multiple attempts (`LATEST`, `BEST`, `AVERAGE`) |
| `hopSize` | `Int` | `160` | Hop size between frames in samples (160 = 10ms at 16kHz) |
| `autoPhaseTransition` | `Boolean` | `true` | Automatically transition LISTENING to SINGING in singafter mode |
| `autoSegmentDetection` | `Boolean` | `true` | Automatically detect segment end from player time |

### Builder Methods

| Method | Description |
|--------|-------------|
| `preset(config)` | Start from a preset configuration |
| `autoAdvance(enabled)` | Enable or disable auto-advance to next segment |
| `scoreThreshold(threshold)` | Set minimum score to auto-advance (0 = disabled) |
| `maxAttempts(max)` | Set maximum attempts before forced advance (0 = unlimited) |
| `resultAggregation(agg)` | Set how to aggregate multiple attempts |
| `hopSize(samples)` | Set hop size between frames in samples |
| `autoPhaseTransition(enabled)` | Enable or disable automatic LISTENING to SINGING transition |
| `autoSegmentDetection(enabled)` | Enable or disable automatic segment end detection |

## Creating a Session

### Factory Method

#### Kotlin

```kotlin
val session = CalibraLiveEval.create(
    reference = lessonMaterial,           // LessonMaterial (required)
    session = SessionConfig.PRACTICE,     // SessionConfig (default: DEFAULT)
    detector = CalibraPitch.createDetector(), // Detector (required)
    player = player,                      // SonixPlayer? (optional)
    recorder = recorder                   // SonixRecorder? (optional)
)
```

#### Swift

```swift
let session = CalibraLiveEval.create(
    reference: lessonMaterial,
    session: .practice,
    detector: CalibraPitch.createDetector(),
    player: player,
    recorder: recorder
)
```

### Factory Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `reference` | `LessonMaterial` | Yes | Reference audio, segments, and key |
| `session` | `SessionConfig` | No | Session configuration (default: `DEFAULT`) |
| `detector` | `CalibraPitch.Detector` | Yes | Pitch detector. Session takes ownership and closes it. |
| `player` | `SonixPlayer?` | No | Audio player for convenience API. Caller manages lifecycle. |
| `recorder` | `SonixRecorder?` | No | Audio recorder for convenience API. Caller manages lifecycle. |

### Usage Tiers

#### Tier 1: Convenience API

Pass `player` and `recorder` handles; the session coordinates seeking, playback, recording, and scoring automatically.

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

#### Tier 2: Low-Level API

Omit `player` and `recorder`; manually manage audio and feed samples directly.

```kotlin
val session = CalibraLiveEval.create(
    reference = lessonMaterial,
    detector = CalibraPitch.createDetector()
)
session.prepareSession()
session.startPracticingSegment(0)
recorder.audioBuffers.collect { buffer ->
    session.feedAudioSamples(buffer.toFloatArray(), sampleRate = 48000)
}
val result = session.finishPracticingSegment()
```

### Ownership Model

| Dependency | Ownership | Rationale |
|------------|-----------|-----------|
| `detector` | **Owned** -- session closes it | Created specifically for this session |
| `player` | **Borrowed** -- caller manages | Shared resource, UI may need direct access |
| `recorder` | **Borrowed** -- caller manages | Shared resource, may be reused |

## Core Features

### Session Lifecycle

```kotlin
// Prepare (suspend, runs on background dispatcher)
session.prepareSession()

// Finish and get aggregated results
val result: SingingResult = session.finishSession()

// Close and release all resources
session.closeSession()
// Alternatively, use close() (implements AutoCloseable)
session.close()

// Restart for "Practice Again"
session.restartSession(fromSegment = 0)
```

```swift
// Prepare
try await session.prepareSession()

// Finish and get aggregated results
let result = session.finishSession()

// Close and release all resources
session.closeSession()

// Restart for "Practice Again"
session.restartSession(fromSegment: 0)
```

### Segment Control

```kotlin
// Start practicing a specific segment
session.startPracticingSegment(0)

// Finish current segment and get result
val result: SegmentResult? = session.finishPracticingSegment()

// Discard current segment without scoring
session.discardCurrentSegment()

// Retry the same segment (increments attempt number)
session.retryCurrentSegment()
```

```swift
session.startPracticingSegment(index: 0)
let result = session.finishPracticingSegment()
session.discardCurrentSegment()
session.retryCurrentSegment()
```

### Navigation

```kotlin
// Jump to a specific segment (discards current attempt if practicing)
session.seekToSegment(3)

// Advance to next segment (returns false if at end)
val advanced: Boolean = session.advanceToNextSegment()

// Seek to a time position
session.seekToTime(seconds = 15.0f)

// Pause and resume playback
session.pausePlayback()
session.resumePlayback()

// Manual LISTENING → SINGING transition (when autoPhaseTransition = false)
session.beginSingingPhase()
```

```swift
session.seekToSegment(index: 3)
let advanced = session.advanceToNextSegment()
session.seekToTime(seconds: 15.0)
session.pausePlayback()
session.resumePlayback()
session.beginSingingPhase()
```

### Feeding Audio

The `feedAudioSamples` method accepts audio at **any sample rate** and resamples internally to 16kHz. This means you do not need to pre-process audio before passing it to the session.

#### Kotlin

```kotlin
// Feed from recorder (any sample rate)
recorder.audioBuffers.collect { buffer ->
    session.feedAudioSamples(buffer.toFloatArray(), sampleRate = 48000)
}

// Default sample rate is 16000 if omitted
session.feedAudioSamples(samples)
```

#### Swift

```swift
// Feed from recorder (any sample rate)
session.feedAudioSamples(buffer, sampleRate: 48000)

// Default sample rate is 16000 if omitted
session.feedAudioSamples(buffer)
```

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `samples` | `FloatArray` / `[Float]` | -- | Mono audio samples, normalized -1.0 to 1.0 |
| `sampleRate` | `Int` | `16000` | Sample rate of the input audio in Hz |

If the input sample rate differs from 16kHz, the session uses `SonixResampler` to convert the audio before processing. This is handled transparently on every call.

### Runtime Configuration

```kotlin
// Set student key for transposition (0 = same as reference)
session.setStudentKeyHz(220.0f)

// Enable or disable pitch processing (smoothing + octave correction)
session.setPitchProcessingEnabled(true)
val isEnabled: Boolean = session.pitchProcessingEnabled
```

```swift
session.setStudentKeyHz(keyHz: 220.0)
session.setPitchProcessingEnabled(enabled: true)
let isEnabled = session.pitchProcessingEnabled
```

### Query Methods

```kotlin
// Get all results for a specific segment
val attempts: List<SegmentResult>? = session.getResultsForSegment(0)

// Check if a segment has been completed at least once
val completed: Boolean = session.hasCompletedSegment(0)
```

```swift
let attempts = session.getResultsForSegment(index: 0)
let completed = session.hasCompletedSegment(index: 0)
```

## State Machine

```
IDLE ──prepareSession()──► READY
READY ──startPracticingSegment()──► PRACTICING
PRACTICING ──finishPracticingSegment()──► BETWEEN_SEGMENTS (or COMPLETED if last)
PRACTICING ──discardCurrentSegment()──► BETWEEN_SEGMENTS
PRACTICING ──seekToSegment()──► PRACTICING (new segment)
BETWEEN_SEGMENTS ──startPracticingSegment()──► PRACTICING
BETWEEN_SEGMENTS ──advanceToNextSegment()──► PRACTICING (or COMPLETED if last)
BETWEEN_SEGMENTS ──finishSession()──► COMPLETED
* ──closeSession()──► (released)
```

### SessionPhase

| Phase | Description |
|-------|-------------|
| `IDLE` | Session created but not started |
| `READY` | Reference loaded, ready to begin practicing |
| `PRACTICING` | Actively capturing and evaluating audio for a segment |
| `BETWEEN_SEGMENTS` | Finished one segment, waiting before next |
| `COMPLETED` | All segments completed or session manually finished |
| `CANCELLED` | Session was cancelled via `closeSession()` |
| `ERROR` | An error occurred during preparation |

### PracticePhase

Tracks the student's activity within a single segment. The progression depends on mode:

- **Singalong:** `IDLE` -> `SINGING` -> `EVALUATED`
- **Singafter:** `IDLE` -> `LISTENING` -> `SINGING` -> `EVALUATED`

| Phase | Description |
|-------|-------------|
| `IDLE` | Not practicing -- waiting to start |
| `LISTENING` | Reference playing, student not recording yet (singafter only) |
| `SINGING` | Student is being recorded and evaluated |
| `EVALUATED` | Segment complete, score available |

## Observing State

### Kotlin (StateFlow)

```kotlin
// Session state (phase, active segment, pitch, progress)
session.state.collect { state ->
    updateUI(state.phase, state.currentPitch, state.segmentProgress)
}

// Active segment details (null when not practicing)
session.activeSegment.collect { active ->
    active?.let { showProgress(it.elapsedSeconds, it.remainingSeconds) }
}

// Completed segments map: segment index → list of attempts
session.completedSegments.collect { results ->
    updateScoreboard(results)
}

// Practice phase (IDLE, LISTENING, SINGING, EVALUATED)
session.phase.collect { phase ->
    updatePhaseIndicator(phase)
}

// Live pitch contour for scrolling visualization
session.livePitchContour.collect { contour ->
    drawPitchCanvas(contour)
}

// Live pitch point (real-time, includes time and confidence)
session.livePitch.collect { pitchPoint ->
    updatePitchIndicator(pitchPoint)
}

// Playback time, playing/recording status
session.currentTime.collect { seconds -> updateSeekBar(seconds) }
session.isPlaying.collect { playing -> updatePlayButton(playing) }
session.isRecording.collect { recording -> updateRecordingIndicator(recording) }
```

### Swift (Observers)

Each observer method returns a cancellable `Task` that dispatches updates on `MainActor`.

```swift
let stateTask = session.observeState { state in
    self.sessionPhase = state.phase
    self.currentPitch = state.currentPitch
    self.segmentProgress = state.segmentProgress
}

let activeTask = session.observeActiveSegment { active in
    self.activeSegment = active
}

let completedTask = session.observeCompletedSegments { results in
    self.completedResults = results  // [Int: [SegmentResult]]
}

let phaseTask = session.observePhase { phase in
    self.practicePhase = phase
}

let contourTask = session.observeLivePitchContour { contour in
    self.pitchContour = contour
}

let pitchTask = session.observeLivePitch { pitchPoint in
    self.livePitch = pitchPoint
}

let timeTask = session.observeCurrentTime { seconds in
    self.currentTime = seconds
}

let playingTask = session.observeIsPlaying { isPlaying in
    self.isPlaying = isPlaying
}

let recordingTask = session.observeIsRecording { isRecording in
    self.isRecording = isRecording
}

// Cancel when done
stateTask.cancel()
```

### StateFlows

| StateFlow | Type | Description |
|-----------|------|-------------|
| `state` | `StateFlow<SessionState>` | Session state (phase, pitch, amplitude, progress, completed segments) |
| `activeSegment` | `StateFlow<ActiveSegmentState?>` | Active segment details, or null if not practicing |
| `completedSegments` | `StateFlow<Map<Int, List<SegmentResult>>>` | Map of segment index to list of attempts |
| `phase` | `StateFlow<PracticePhase>` | Current practice phase (IDLE, LISTENING, SINGING, EVALUATED) |
| `livePitchContour` | `StateFlow<PitchContour>` | Accumulated pitch contour for scrolling visualization |
| `livePitch` | `StateFlow<PitchPoint>` | Real-time pitch point (includes time and confidence) |
| `currentTime` | `StateFlow<Float>` | Playback position in seconds |
| `isPlaying` | `StateFlow<Boolean>` | Whether player is currently playing |
| `isRecording` | `StateFlow<Boolean>` | Whether recording is active |

### Swift Observer Methods

| Method | Callback Type | Description |
|--------|---------------|-------------|
| `observeState(_:)` | `(SessionState) -> Void` | Session state changes |
| `observeActiveSegment(_:)` | `(ActiveSegmentState?) -> Void` | Active segment changes |
| `observeCompletedSegments(_:)` | `([Int: [SegmentResult]]) -> Void` | Completed segments with native Swift `Int` keys |
| `observePhase(_:)` | `(PracticePhase) -> Void` | Practice phase changes |
| `observeLivePitchContour(_:)` | `(PitchContour) -> Void` | Live pitch contour updates |
| `observeLivePitch(_:)` | `(PitchPoint) -> Void` | Real-time pitch point updates |
| `observeCurrentTime(_:)` | `(Float) -> Void` | Playback time updates |
| `observeIsPlaying(_:)` | `(Bool) -> Void` | Playing state changes |
| `observeIsRecording(_:)` | `(Bool) -> Void` | Recording state changes |

### Properties

| Property | Type | Description |
|----------|------|-------------|
| `segments` | `List<Segment>` | All segments from the reference |
| `referenceKeyHz` | `Float` | Reference key in Hz from LessonMaterial |
| `studentKeyHz` | `Float` | Current student key in Hz (0 = same as reference) |
| `pitchProcessingEnabled` | `Boolean` | Whether pitch processing is currently enabled |

## Callbacks

Alternative to StateFlow observation. Callbacks are dispatched on `MainActor` in Swift.

### Kotlin

```kotlin
session.onPhaseChanged { phase ->
    println("Phase: $phase")
}

session.onReferenceEnd { segment ->
    println("Reference ended for: ${segment.lyrics}")
}

session.onSegmentComplete { result ->
    println("Score: ${result.score}")
}

session.onSessionComplete { result ->
    println("Overall: ${result.overallScore}")
}
```

### Swift

```swift
session.onPhaseChanged { phase in
    print("Phase: \(phase)")
}

session.onReferenceEnd { segment in
    print("Reference ended for: \(segment.lyrics)")
}

session.onSegmentComplete { result in
    print("Score: \(result.score)")
}

session.onSessionComplete { result in
    print("Overall: \(result.overallScore)")
}
```

### Callback Reference

| Method | Callback Signature | Description |
|--------|-------------------|-------------|
| `onPhaseChanged` | `(PracticePhase) -> Unit` | Practice phase transitions (e.g., LISTENING to SINGING) |
| `onReferenceEnd` | `(Segment) -> Unit` | Reference audio finished playing (singafter mode) |
| `onSegmentComplete` | `(SegmentResult) -> Unit` | Segment finished with its result |
| `onSessionComplete` | `(SingingResult) -> Unit` | All segments finished |

## Model Types

### SessionState

| Property | Type | Description |
|----------|------|-------------|
| `phase` | `SessionPhase` | Current session phase |
| `activeSegmentIndex` | `Int?` | Index of segment being practiced, or null |
| `activeSegment` | `Segment?` | The segment being practiced, or null |
| `currentPitch` | `Float` | Detected pitch in Hz (-1 for unvoiced) |
| `currentAmplitude` | `Float` | Audio amplitude (0.0 - 1.0) |
| `segmentProgress` | `Float` | Progress through current segment (0.0 - 1.0) |
| `completedSegments` | `Set<Int>` | Indices of completed segments |
| `error` | `String?` | Error message if phase is ERROR |
| `isPracticing` | `Boolean` | True if session is actively practicing |
| `canBeginSegment` | `Boolean` | True if a new segment can be started |
| `isFinished` | `Boolean` | True if session is finished (COMPLETED, CANCELLED, or ERROR) |
| `completedCount` | `Int` | Number of completed segments |

### ActiveSegmentState

| Property | Type | Description |
|----------|------|-------------|
| `segmentIndex` | `Int` | Index of the segment |
| `segment` | `Segment` | The segment being practiced |
| `currentPitch` | `Float` | Detected pitch in Hz (-1 for unvoiced) |
| `currentAmplitude` | `Float` | Audio amplitude (0.0 - 1.0) |
| `elapsedSeconds` | `Float` | Time elapsed since segment started |
| `isCapturing` | `Boolean` | Whether audio is currently being captured |
| `progress` | `Float` | Progress through the segment (0.0 - 1.0) |
| `remainingSeconds` | `Float` | Time remaining in seconds |
| `hasVoice` | `Boolean` | True if detected pitch is valid |

### SegmentResult

| Property | Type | Description |
|----------|------|-------------|
| `segment` | `Segment` | The segment that was evaluated |
| `score` | `Float` | Overall score (0.0 - 1.0) |
| `pitchAccuracy` | `Float` | Pitch accuracy component (0.0 - 1.0) |
| `level` | `PerformanceLevel` | Performance level classification |
| `attemptNumber` | `Int` | Which attempt this is (1-based) |
| `referencePitch` | `PitchContour` | Reference pitch contour for visualization |
| `studentPitch` | `PitchContour` | Student pitch contour for visualization |
| `isPassing` | `Boolean` | True if score >= 0.5 |
| `isGood` | `Boolean` | True if score >= 0.7 |
| `isExcellent` | `Boolean` | True if score >= 0.9 |
| `scorePercent` | `Int` | Score as percentage (0-100) |
| `feedbackMessage` | `String` | Human-readable feedback based on performance level |

### SingingResult

Returned by `finishSession()` with aggregated results across all segments.

| Property | Type | Description |
|----------|------|-------------|
| `overallScore` | `Float` | Aggregate score across all segments (0.0 - 1.0) |
| `segmentResults` | `Map<Int, List<SegmentResult>>` | Map of segment index to list of attempts |
| `aggregation` | `ResultAggregation` | How the overall score was calculated |
| `overallScorePercent` | `Int` | Overall score as percentage (0-100) |
| `segmentCount` | `Int` | Number of segments evaluated |
| `totalAttempts` | `Int` | Total attempts across all segments |
| `allPassing` | `Boolean` | True if all segments have a passing score |

| Method | Description |
|--------|-------------|
| `latestScorePerSegment()` | Latest score for each segment |
| `bestScorePerSegment()` | Best score for each segment |
| `averageScorePerSegment()` | Average score for each segment |
| `latestResultPerSegment()` | Latest `SegmentResult` for each segment |
| `getAllFeedback()` | Feedback messages for all segments |

### PerformanceLevel

| Level | Score Range | Display Name |
|-------|-------------|--------------|
| `NEEDS_WORK` | < 0.3 | "Needs Work" |
| `FAIR` | 0.3 - 0.6 | "Fair" |
| `GOOD` | 0.6 - 0.8 | "Good" |
| `VERY_GOOD` | 0.8 - 0.95 | "Very Good" |
| `EXCELLENT` | >= 0.95 | "Excellent" |
| `NOT_DETECTED` | negative | "No Voice" |
| `NOT_EVALUATED` | -- | "Not Evaluated" |

### Segment

| Property | Type | Description |
|----------|------|-------------|
| `index` | `Int` | Zero-based segment index |
| `startSeconds` | `Float` | Reference audio start time |
| `endSeconds` | `Float` | Reference audio end time |
| `lyrics` | `String` | Text/lyrics for this segment (optional) |
| `studentStartSeconds` | `Float?` | When student recording starts (null = same as `startSeconds`) |
| `studentEndSeconds` | `Float?` | When student recording ends (null = same as `endSeconds`) |
| `duration` | `Float` | Duration in seconds |
| `isSingafter` | `Boolean` | True if student starts after reference |
| `effectiveStudentStart` | `Float` | Student start time (falls back to `startSeconds`) |
| `effectiveStudentEnd` | `Float` | Student end time (falls back to `endSeconds`) |
| `studentDuration` | `Float` | Duration of the student recording portion |

### LessonMaterial

| Property | Type | Description |
|----------|------|-------------|
| `audioSource` | `AudioSource` | Source of the reference audio |
| `segments` | `List<Segment>` | Segment boundaries and lyrics |
| `keyHz` | `Float` | Musical key frequency in Hz (e.g., 261.63 for middle C) |
| `pitchContour` | `PitchContour?` | Pre-computed pitch (enables fast initialization) |
| `hpcpFrames` | `List<FloatArray>?` | Pre-computed HPCP frames for DTW alignment |
| `duration` | `Float` | Total duration based on the last segment's end time |
| `segmentCount` | `Int` | Number of segments |

#### Creating LessonMaterial

```kotlin
// From audio file
val material = LessonMaterial.fromFile(
    audioPath = "/path/to/reference.m4a",
    segments = segments,
    keyHz = 196.0f
)

// From raw audio samples (with optional pre-computed pitch)
val material = LessonMaterial.fromAudio(
    samples = audioData.samples,
    sampleRate = audioData.sampleRate,
    segments = segments,
    keyHz = 196.0f,
    pitchContour = preComputedContour  // optional, speeds up prepareSession()
)
```

#### Creating Segments

```kotlin
// Individual segments
val segments = listOf(
    Segment(index = 0, startSeconds = 0.0f, endSeconds = 4.5f, lyrics = "Sa Re Ga Ma"),
    Segment(index = 1, startSeconds = 4.5f, endSeconds = 9.0f, lyrics = "Pa Da Ni Sa")
)

// From parallel arrays
val segments = Segment.fromArrays(
    starts = floatArrayOf(0.0f, 4.5f, 9.0f),
    ends = floatArrayOf(4.5f, 9.0f, 13.5f),
    lyrics = listOf("Sa Re Ga Ma", "Pa Da Ni Sa", "Sa Ni Da Pa")
)

// Singafter segments (student sings after reference)
val segments = Segment.fromArrays(
    starts = floatArrayOf(0.0f, 8.0f),
    ends = floatArrayOf(8.0f, 16.0f),
    studentStarts = floatArrayOf(4.0f, 12.0f),  // Student starts halfway
    studentEnds = floatArrayOf(8.0f, 16.0f)
)
```

## Common Patterns

### Singalong ViewModel (Kotlin)

```kotlin
class SingalongViewModel : ViewModel() {
    private var session: CalibraLiveEval? = null
    private var player: SonixPlayer? = null
    private var recorder: SonixRecorder? = null

    val practicePhase = MutableStateFlow(PracticePhase.IDLE)
    val currentPitch = MutableStateFlow(-1f)
    val lastResult = MutableStateFlow<SegmentResult?>(null)

    fun loadSession(reference: LessonMaterial, config: SessionConfig) {
        viewModelScope.launch {
            player = SonixPlayer.create(audioPath, SonixPlayerConfig.DEFAULT)
            recorder = SonixRecorder.create(tempPath, SonixRecorderConfig.VOICE)

            session = CalibraLiveEval.create(
                reference = reference,
                session = config,
                detector = CalibraPitch.createDetector(),
                player = player,
                recorder = recorder
            )

            session?.onPhaseChanged { phase -> practicePhase.value = phase }
            session?.onSegmentComplete { result -> lastResult.value = result }

            session?.prepareSession()

            // Observe session state
            session?.state?.collect { state ->
                currentPitch.value = state.currentPitch
            }
        }
    }

    fun play(segmentIndex: Int) {
        session?.startPracticingSegment(segmentIndex)
    }

    fun pause() {
        session?.pausePlayback()
    }

    fun seekTo(segmentIndex: Int) {
        session?.seekToSegment(segmentIndex)
    }

    fun retry() {
        session?.retryCurrentSegment()
    }

    fun finish(): SingingResult? {
        return session?.finishSession()
    }

    override fun onCleared() {
        session?.close()
        player?.release()
        recorder?.release()
    }
}
```

### SwiftUI View Model (Swift)

```swift
@MainActor
class SingalongViewModel: ObservableObject {
    @Published var phase: PracticePhase = .idle
    @Published var lastResult: SegmentResult?
    @Published var currentPitch: Float = -1

    private var session: CalibraLiveEval?
    private var observerTasks: [Task<Void, Never>] = []

    func loadSession(reference: LessonMaterial) async {
        let detector = CalibraPitch.createDetector()
        let session = CalibraLiveEval.create(
            reference: reference,
            session: .practice,
            detector: detector,
            player: player,
            recorder: recorder
        )
        self.session = session

        session.onPhaseChanged { [weak self] phase in
            self?.phase = phase
        }
        session.onSegmentComplete { [weak self] result in
            self?.lastResult = result
        }

        try? await session.prepareSession()

        observerTasks.append(session.observeState { [weak self] state in
            self?.currentPitch = state.currentPitch
        })
    }

    func play(segmentIndex: Int) {
        session?.startPracticingSegment(index: segmentIndex)
    }

    func pause() {
        session?.pausePlayback()
    }

    func seekTo(segmentIndex: Int) {
        session?.seekToSegment(index: segmentIndex)
    }

    func cleanup() {
        observerTasks.forEach { $0.cancel() }
        session?.closeSession()
    }
}
```

### Low-Level Manual Audio (Kotlin)

```kotlin
val session = CalibraLiveEval.create(
    reference = lessonMaterial,
    detector = CalibraPitch.createDetector()
)
session.prepareSession()

for (segmentIndex in session.segments.indices) {
    session.startPracticingSegment(segmentIndex)

    // Feed audio from your own source
    recorder.audioBuffers.collect { buffer ->
        session.feedAudioSamples(buffer.toFloatArray(), sampleRate = 48000)
    }

    val result = session.finishPracticingSegment()
    println("Segment $segmentIndex: ${result?.scorePercent}%")
}

val finalResult = session.finishSession()
println("Overall: ${finalResult.overallScorePercent}%")
session.closeSession()
```

### Pitch Visualization (Swift)

```swift
let contourTask = session.observeLivePitchContour { contour in
    let anchorX: CGFloat = 200  // "Now" position on screen
    let currentTime = contour.samples.last?.timeSeconds ?? 0

    for sample in contour.samples {
        let x = anchorX - CGFloat(currentTime - sample.timeSeconds) * pixelsPerSecond
        let y = midiToScreenY(sample.midiNote)
        drawPoint(x, y)
    }
}
```

## Next Steps

- [CalibraPitch](./pitch) -- Pitch detection without scoring
- [CalibraVAD](./vad) -- Voice activity detection
- [CalibraVocalRange](./vocal-range) -- Detect a singer's vocal range
