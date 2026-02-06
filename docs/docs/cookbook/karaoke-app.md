---
sidebar_position: 1
---

# Karaoke App

Build a karaoke app that plays backing tracks and scores singing in real-time.

## What You'll Build

A complete karaoke experience:
- Play backing track with pitch/tempo control
- Record vocals with echo cancellation
- Show live pitch visualization
- Score singing per segment
- Track best scores

## Architecture Overview

```
┌─────────────────────────────────────────────────────────┐
│                     KaraokeViewModel                     │
├─────────────────────────────────────────────────────────┤
│  SonixPlayer          SonixRecorder     CalibraLiveEval │
│  (backing track)      (vocals)          (scoring)       │
│       ↓                    ↓                  ↑         │
│  Pitch/Tempo          Echo Cancel       Pitch Compare   │
│  Control              Audio Buffers →   with Reference  │
└─────────────────────────────────────────────────────────┘
```

## Implementation

### Data Models

```kotlin
data class KaraokeSong(
    val id: String,
    val title: String,
    val artist: String,
    val backingTrackPath: String,
    val vocalGuidePath: String?,  // Optional guide vocals
    val lessonMaterial: LessonMaterial  // Segments + reference audio
)

data class KaraokeState(
    val phase: KaraokePhase,
    val currentSegment: Int,
    val segmentProgress: Float,
    val currentPitch: Float,
    val currentScore: Float?,
    val overallScore: Float
)

enum class KaraokePhase {
    LOADING, READY, SINGING, BETWEEN_SEGMENTS, FINISHED
}
```

### ViewModel

```kotlin
class KaraokeViewModel : ViewModel() {
    private var player: SonixPlayer? = null
    private var recorder: SonixRecorder? = null
    private var session: CalibraLiveEval? = null

    private val _state = MutableStateFlow(KaraokeState(...))
    val state: StateFlow<KaraokeState> = _state.asStateFlow()

    val livePitch: StateFlow<PitchContour>
        get() = session?.livePitchContour ?: MutableStateFlow(PitchContour.EMPTY)

    // Settings
    var pitchShift: Float = 0f
        set(value) {
            field = value
            player?.pitch = value
        }

    var tempoFactor: Float = 1f
        set(value) {
            field = value
            player?.tempo = value
        }

    suspend fun loadSong(song: KaraokeSong) {
        _state.update { it.copy(phase = KaraokePhase.LOADING) }

        // Create player for backing track
        player = SonixPlayer.create(
            song.backingTrackPath,
            SonixPlayerConfig.Builder()
                .onComplete { handleSongEnd() }
                .build()
        )

        // Create recorder with echo cancellation
        recorder = SonixRecorder.createTemporary(
            SonixRecorderConfig.Builder()
                .echoCancellation(true)
                .playbackSyncProvider(player!!.asPlaybackInfoProvider)
                .build()
        )

        // Create pitch detector
        val detector = CalibraPitch.createDetector(
            PitchDetectorConfig.Builder()
                .algorithm(PitchAlgorithm.SWIFT_F0)
                .enableProcessing()
                .build(),
            modelProvider = { ModelLoader.loadSwiftF0() }
        )

        // Create evaluation session
        session = CalibraLiveEval.create(
            reference = song.lessonMaterial,
            session = SessionConfig.Builder()
                .autoAdvance(true)
                .build(),
            detector = detector,
            player = player,
            recorder = recorder
        )

        session?.prepareSession()

        // Wire up callbacks
        session?.onSegmentComplete { result ->
            _state.update { it.copy(currentScore = result.score) }
        }

        session?.onSessionComplete { result ->
            _state.update {
                it.copy(
                    phase = KaraokePhase.FINISHED,
                    overallScore = result.overallScore
                )
            }
        }

        // Observe state
        viewModelScope.launch {
            session?.state?.collect { sessionState ->
                _state.update {
                    it.copy(
                        phase = when (sessionState.phase) {
                            SessionPhase.PRACTICING -> KaraokePhase.SINGING
                            SessionPhase.BETWEEN_SEGMENTS -> KaraokePhase.BETWEEN_SEGMENTS
                            SessionPhase.COMPLETED -> KaraokePhase.FINISHED
                            else -> it.phase
                        },
                        currentSegment = sessionState.activeSegmentIndex ?: it.currentSegment,
                        segmentProgress = sessionState.segmentProgress,
                        currentPitch = sessionState.currentPitch
                    )
                }
            }
        }

        _state.update { it.copy(phase = KaraokePhase.READY) }
    }

    fun startSinging() {
        session?.startPracticingSegment(0)
    }

    fun skipToSegment(index: Int) {
        session?.startPracticingSegment(index)
    }

    fun pause() {
        session?.pause()
    }

    fun resume() {
        session?.resume()
    }

    private fun handleSongEnd() {
        val result = session?.finishSession()
        _state.update {
            it.copy(
                phase = KaraokePhase.FINISHED,
                overallScore = result?.overallScore ?: 0f
            )
        }
    }

    fun cleanup() {
        session?.closeSession()
        player?.release()
        recorder?.release()
    }
}
```

### UI (Compose)

```kotlin
@Composable
fun KaraokeScreen(viewModel: KaraokeViewModel) {
    val state by viewModel.state.collectAsState()
    val pitchContour by viewModel.livePitch.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Pitch visualization
        PitchVisualization(
            contour = pitchContour,
            modifier = Modifier.weight(1f)
        )

        // Current segment info
        SegmentProgress(
            segment = state.currentSegment,
            progress = state.segmentProgress
        )

        // Score display
        state.currentScore?.let { score ->
            ScoreDisplay(score = score)
        }

        // Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Pitch control
            Slider(
                value = viewModel.pitchShift,
                onValueChange = { viewModel.pitchShift = it },
                valueRange = -6f..6f
            )

            // Tempo control
            Slider(
                value = viewModel.tempoFactor,
                onValueChange = { viewModel.tempoFactor = it },
                valueRange = 0.5f..1.5f
            )
        }

        // Action button
        when (state.phase) {
            KaraokePhase.READY -> {
                Button(onClick = { viewModel.startSinging() }) {
                    Text("Start")
                }
            }
            KaraokePhase.SINGING -> {
                Button(onClick = { viewModel.pause() }) {
                    Text("Pause")
                }
            }
            KaraokePhase.FINISHED -> {
                FinalScoreDisplay(score = state.overallScore)
            }
        }
    }
}

@Composable
fun PitchVisualization(contour: PitchContour, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        contour.samples.forEachIndexed { index, point ->
            if (point.pitch > 0) {
                val x = (index.toFloat() / contour.samples.size) * width
                val y = height - (pitchToY(point.pitch, height))
                drawCircle(
                    color = Color.Blue,
                    radius = 4f,
                    center = Offset(x, y)
                )
            }
        }
    }
}
```

### Swift (SwiftUI)

```swift
struct KaraokeView: View {
    @StateObject private var viewModel = KaraokeViewModel()

    var body: some View {
        VStack {
            // Pitch visualization
            PitchVisualizationView(contour: viewModel.livePitch)
                .frame(height: 200)

            // Progress
            ProgressView(value: viewModel.state.segmentProgress)

            // Score
            if let score = viewModel.state.currentScore {
                Text("Score: \(Int(score * 100))%")
                    .font(.title)
            }

            // Controls
            HStack {
                VStack {
                    Text("Key: \(Int(viewModel.pitchShift))")
                    Slider(value: $viewModel.pitchShift, in: -6...6, step: 1)
                }

                VStack {
                    Text("Tempo: \(Int(viewModel.tempoFactor * 100))%")
                    Slider(value: $viewModel.tempoFactor, in: 0.5...1.5)
                }
            }
            .padding()

            // Start button
            Button(viewModel.isPlaying ? "Pause" : "Start") {
                if viewModel.isPlaying {
                    viewModel.pause()
                } else {
                    viewModel.startSinging()
                }
            }
            .buttonStyle(.borderedProminent)
        }
        .padding()
    }
}
```

## Key Features

### Echo Cancellation

Removes the backing track from the vocal recording:

```kotlin
val recorder = SonixRecorder.createTemporary(
    SonixRecorderConfig.Builder()
        .echoCancellation(true)
        .playbackSyncProvider(player.asPlaybackInfoProvider)
        .build()
)
```

### Key Transposition

Let users sing in their comfortable key:

```kotlin
player.pitch = userKeyOffset  // -6 to +6 semitones
session.setStudentKeyHz(referenceKeyHz * semitoneMultiplier(userKeyOffset))
```

### Practice Mode

Slow down for difficult sections:

```kotlin
player.tempo = 0.75f  // 75% speed
```

## Next Steps

- [Live Evaluation Guide](../guides/live-evaluation) - Deeper into scoring
- [Audio Effects Concepts](../concepts/audio-effects) - Add reverb to vocals
- [Demo App](https://github.com/musicmuni/voxatrace/tree/main/public/demo-apps) - Full source code
