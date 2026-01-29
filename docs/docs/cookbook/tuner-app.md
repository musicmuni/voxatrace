---
sidebar_position: 2
---

# Tuner App

Build a chromatic tuner that shows the current note and how many cents sharp/flat.

## What You'll Build

A classic tuner display:
- Shows current note (C4, A#3, etc.)
- Displays frequency in Hz
- Shows cents offset (-50 to +50)
- Visual needle/gauge showing pitch accuracy

## Implementation

### ViewModel

```kotlin
class TunerViewModel : ViewModel() {
    private var detector: CalibraPitch.Detector? = null
    private var recorder: SonixRecorder? = null

    private val _state = MutableStateFlow(TunerState())
    val state: StateFlow<TunerState> = _state.asStateFlow()

    data class TunerState(
        val isActive: Boolean = false,
        val note: String = "--",
        val octave: Int = 0,
        val frequency: Float = 0f,
        val centsOff: Int = 0,
        val confidence: Float = 0f
    )

    fun start() {
        detector = CalibraPitch.createDetector(
            PitchDetectorConfig.Builder()
                .algorithm(PitchAlgorithm.YIN)
                .enableProcessing()  // Smoothing for stable display
                .build()
        )

        recorder = SonixRecorder.createTemporary(SonixRecorderConfig.VOICE)
        recorder?.start()

        _state.update { it.copy(isActive = true) }

        viewModelScope.launch {
            recorder?.audioBuffers?.collect { buffer ->
                val samples = FloatArray(buffer.sampleCount)
                buffer.fillFloatSamples(samples)

                val point = detector?.detect(samples, buffer.sampleRate) ?: return@collect

                if (point.pitch > 0 && point.confidence > 0.5f) {
                    val noteInfo = pitchToNoteInfo(point.pitch)
                    _state.update {
                        it.copy(
                            note = noteInfo.name,
                            octave = noteInfo.octave,
                            frequency = point.pitch,
                            centsOff = noteInfo.cents,
                            confidence = point.confidence
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            note = "--",
                            octave = 0,
                            frequency = 0f,
                            centsOff = 0,
                            confidence = 0f
                        )
                    }
                }
            }
        }
    }

    fun stop() {
        _state.update { it.copy(isActive = false) }
        recorder?.stop()
        recorder?.release()
        detector?.close()
        recorder = null
        detector = null
    }

    private fun pitchToNoteInfo(frequency: Float): NoteInfo {
        val noteNames = arrayOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
        val a4 = 440.0

        // Calculate semitones from A4
        val semitones = 12 * kotlin.math.log2(frequency / a4) + 69

        // Get note and octave
        val noteIndex = ((semitones.toInt() % 12) + 12) % 12
        val octave = (semitones.toInt() / 12) - 1

        // Calculate cents offset from perfect pitch
        val perfectSemitone = semitones.toInt()
        val cents = ((semitones - perfectSemitone) * 100).toInt()

        // Normalize cents to -50 to +50 range
        val normalizedCents = if (cents > 50) cents - 100 else cents

        return NoteInfo(
            name = noteNames[noteIndex],
            octave = octave,
            cents = normalizedCents
        )
    }

    data class NoteInfo(
        val name: String,
        val octave: Int,
        val cents: Int
    )

    override fun onCleared() {
        stop()
    }
}
```

### UI (Compose)

```kotlin
@Composable
fun TunerScreen(viewModel: TunerViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Note display
        Text(
            text = if (state.note != "--") "${state.note}${state.octave}" else "--",
            fontSize = 96.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Frequency display
        Text(
            text = if (state.frequency > 0) "${state.frequency.toInt()} Hz" else "-- Hz",
            fontSize = 24.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Cents gauge
        TunerGauge(
            cents = state.centsOff,
            modifier = Modifier.size(250.dp)
        )

        // Cents text
        Text(
            text = when {
                state.centsOff > 0 -> "+${state.centsOff} cents"
                state.centsOff < 0 -> "${state.centsOff} cents"
                else -> "In tune"
            },
            fontSize = 18.sp,
            color = when {
                kotlin.math.abs(state.centsOff) <= 5 -> Color.Green
                kotlin.math.abs(state.centsOff) <= 15 -> Color.Yellow
                else -> Color.Red
            }
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Start/Stop button
        Button(
            onClick = {
                if (state.isActive) viewModel.stop() else viewModel.start()
            }
        ) {
            Text(if (state.isActive) "Stop" else "Start")
        }
    }
}

@Composable
fun TunerGauge(cents: Int, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2 - 20

        // Draw tick marks
        for (i in -50..50 step 10) {
            val angle = (i / 50f) * 90f - 90f  // -90 to +90 degrees
            val radians = Math.toRadians(angle.toDouble())
            val start = Offset(
                center.x + (radius - 20) * cos(radians).toFloat(),
                center.y + (radius - 20) * sin(radians).toFloat()
            )
            val end = Offset(
                center.x + radius * cos(radians).toFloat(),
                center.y + radius * sin(radians).toFloat()
            )
            drawLine(Color.Gray, start, end, strokeWidth = if (i == 0) 4f else 2f)
        }

        // Draw needle
        val needleAngle = (cents.coerceIn(-50, 50) / 50f) * 90f - 90f
        val needleRadians = Math.toRadians(needleAngle.toDouble())
        val needleEnd = Offset(
            center.x + (radius - 30) * cos(needleRadians).toFloat(),
            center.y + (radius - 30) * sin(needleRadians).toFloat()
        )

        drawLine(
            color = when {
                kotlin.math.abs(cents) <= 5 -> Color.Green
                kotlin.math.abs(cents) <= 15 -> Color.Yellow
                else -> Color.Red
            },
            start = center,
            end = needleEnd,
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )

        // Draw center circle
        drawCircle(Color.White, radius = 8f, center = center)
    }
}
```

### Swift (SwiftUI)

```swift
struct TunerView: View {
    @StateObject private var viewModel = TunerViewModel()

    var body: some View {
        ZStack {
            Color.black.ignoresSafeArea()

            VStack(spacing: 20) {
                // Note display
                Text(viewModel.state.note != "--"
                    ? "\(viewModel.state.note)\(viewModel.state.octave)"
                    : "--")
                    .font(.system(size: 96, weight: .bold))
                    .foregroundColor(.white)

                // Frequency
                Text(viewModel.state.frequency > 0
                    ? "\(Int(viewModel.state.frequency)) Hz"
                    : "-- Hz")
                    .font(.title2)
                    .foregroundColor(.gray)

                // Gauge
                TunerGaugeView(cents: viewModel.state.centsOff)
                    .frame(width: 250, height: 150)

                // Cents label
                Text(centsLabel)
                    .font(.title3)
                    .foregroundColor(centsColor)

                Spacer()

                // Start/Stop button
                Button(viewModel.state.isActive ? "Stop" : "Start") {
                    if viewModel.state.isActive {
                        viewModel.stop()
                    } else {
                        Task { await viewModel.start() }
                    }
                }
                .buttonStyle(.borderedProminent)
            }
            .padding()
        }
    }

    private var centsLabel: String {
        switch viewModel.state.centsOff {
        case let c where c > 0: return "+\(c) cents"
        case let c where c < 0: return "\(c) cents"
        default: return "In tune"
        }
    }

    private var centsColor: Color {
        let cents = abs(viewModel.state.centsOff)
        if cents <= 5 { return .green }
        if cents <= 15 { return .yellow }
        return .red
    }
}
```

## Enhancements

### Reference Pitch Selection

```kotlin
// A4 = 440 Hz is standard, but orchestras often use 442 Hz
var a4Reference = 440f

private fun pitchToNoteInfo(frequency: Float): NoteInfo {
    val semitones = 12 * kotlin.math.log2(frequency / a4Reference) + 69
    // ... rest of calculation
}
```

### Instrument Modes

```kotlin
enum class TunerMode {
    CHROMATIC,  // All notes
    GUITAR,     // E A D G B E
    BASS,       // E A D G
    UKULELE     // G C E A
}

val guitarNotes = listOf(
    82.41f to "E2",
    110.0f to "A2",
    146.83f to "D3",
    196.0f to "G3",
    246.94f to "B3",
    329.63f to "E4"
)

fun findNearestNote(frequency: Float, mode: TunerMode): NoteInfo {
    return when (mode) {
        TunerMode.GUITAR -> {
            guitarNotes.minByOrNull { abs(it.first - frequency) }
                ?.let { /* return info */ }
                ?: pitchToNoteInfo(frequency)
        }
        else -> pitchToNoteInfo(frequency)
    }
}
```

## Next Steps

- [Detecting Pitch Guide](/docs/guides/detecting-pitch) - Pitch detection details
- [Pitch Detection Concepts](/docs/concepts/pitch-detection) - Theory
- [Demo App](https://github.com/musicmuni/voxatrace-demos) - Full source
