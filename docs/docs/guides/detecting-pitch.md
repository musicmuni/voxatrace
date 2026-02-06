---
sidebar_position: 3
---

# Detecting Pitch

A complete guide to pitch detection with CalibraPitch.

## What You'll Learn

- Detect pitch in real-time from microphone input
- Extract pitch contours from recorded audio
- Choose between YIN and SwiftF0 algorithms
- Handle octave errors and noise
- Convert frequency to musical notes

## Prerequisites

- VoxaTrace installed

## Quick Start

### Kotlin

```kotlin
val detector = CalibraPitch.createDetector()

recorder.audioBuffers.collect { buffer ->
    val samples = buffer.toFloatArray()
    val point = detector.detect(samples, buffer.sampleRate)

    if (point.pitch > 0) {
        println("Pitch: ${point.pitch} Hz, Confidence: ${point.confidence}")
    }
}

detector.close()
```

### Swift

```swift
let detector = CalibraPitch.companion.createDetector()

for await buffer in recorder.audioBuffersStream() {
    let samples = buffer.toFloatArray()
    let point = detector.detect(samples: samples, sampleRate: buffer.sampleRate)

    if point.pitch > 0 {
        print("Pitch: \(point.pitch) Hz, Confidence: \(point.confidence)")
    }
}

detector.close()
```

## Real-time Detection

### Basic Setup

```kotlin
// Create detector with defaults
val detector = CalibraPitch.createDetector()

// Or with preset
val detector = CalibraPitch.createDetector(PitchDetectorConfig.BALANCED)
```

### Processing Audio

```kotlin
// From recorder
recorder.audioBuffers.collect { buffer ->
    val samples = FloatArray(buffer.sampleCount)
    buffer.fillFloatSamples(samples)

    val point = detector.detect(samples, buffer.sampleRate)
    updateUI(point)
}

// From any audio source
val point = detector.detect(audioSamples, sampleRate = 48000)
```

### Understanding Results

```kotlin
data class PitchPoint(
    val pitch: Float,      // Frequency in Hz (-1 if unvoiced)
    val confidence: Float, // 0.0 to 1.0
    val timeSeconds: Float // Timestamp (for contours)
)

// Check if voiced
if (point.pitch > 0) {
    // Valid pitch detected
} else {
    // Unvoiced (silence, noise, or consonant)
}

// Check confidence
if (point.confidence > 0.7f) {
    // High confidence - trust this pitch
}
```

## Choosing an Algorithm

### YIN (Default)

Classic DSP algorithm. No external dependencies.

```kotlin
val detector = CalibraPitch.createDetector(
    PitchDetectorConfig.Builder()
        .algorithm(PitchAlgorithm.YIN)
        .build()
)
```

Best for:
- Simple tuner apps
- Low-resource devices
- When you can't add ONNX Runtime

### SwiftF0 (Neural Network)

Deep learning model with better accuracy in noisy conditions.

```kotlin
val detector = CalibraPitch.createDetector(
    PitchDetectorConfig.Builder()
        .algorithm(PitchAlgorithm.SWIFT_F0)
        .build(),
    modelProvider = { ModelLoader.loadSwiftF0() }
)
```

Best for:
- Singing apps (more robust to vibrato, noise)
- Production apps where accuracy matters
- When ONNX Runtime is acceptable

## Configuration Options

### Presets

```kotlin
PitchDetectorConfig.BALANCED  // Good tradeoff (default)
PitchDetectorConfig.PRECISE   // Higher accuracy, more CPU
PitchDetectorConfig.RELAXED      // Lower latency, less accuracy
```

### Voice Type

Optimize for specific vocal ranges:

```kotlin
val config = PitchDetectorConfig.Builder()
    .voiceType(VoiceType.WesternSoprano)  // High female voice
    .voiceType(VoiceType.WesternTenor)    // High male voice
    .voiceType(VoiceType.WesternBass)     // Low male voice
    .voiceType(VoiceType.Auto)         // Detect automatically
    .voiceType(VoiceType.carnaticMale) // Indian classical male
    .build()
```

### Processing (Smoothing + Octave Correction)

```kotlin
val config = PitchDetectorConfig.Builder()
    .enableProcessing()  // Enable smoothing and octave correction
    .build()

// Or control at runtime
detector.processingEnabled = true
```

### Quiet Handling

How to handle low-amplitude audio:

```kotlin
val config = PitchDetectorConfig.Builder()
    .quietHandling(QuietHandling.NORMAL)      // Standard gating
    .quietHandling(QuietHandling.SENSITIVE)   // More sensitive for soft singers
    .quietHandling(QuietHandling.NOISY)       // For noisy environments
    .build()
```

## Batch Extraction

Extract complete pitch contours from recorded audio:

```kotlin
val extractor = CalibraPitch.createContourExtractor(
    ContourExtractorConfig.SCORING,
    modelProvider = { ModelLoader.loadSwiftF0() }
)

// Load audio (must be 16kHz or will be resampled)
val audio = decoder.decode("recording.m4a")

// Extract contour
val contour = extractor.extract(audio.samples, audio.sampleRate)

// Access points
contour.samples.forEach { point ->
    println("Time: ${point.timeSeconds}s, Pitch: ${point.pitch} Hz")
}

extractor.release()
```

### Cleanup Options

```kotlin
// For scoring - removes artifacts, corrects octaves
val config = ContourExtractorConfig.SCORING

// For display - includes smoothing
val config = ContourExtractorConfig.DISPLAY

// Raw - no processing
val config = ContourExtractorConfig.RAW

// Custom
val config = ContourExtractorConfig.Builder()
    .preset(ContourExtractorConfig.SCORING)
    .hopMs(10)  // 10ms between pitch samples
    .cleanup(ContourCleanup.SCORING)
    .build()
```

## Post-Processing

Apply cleanup to existing contours:

```kotlin
// Apply preset cleanup
val cleaned = CalibraPitch.PostProcess.cleanup(contour, ContourCleanup.SCORING)

// Individual operations
val smoothed = CalibraPitch.PostProcess.smooth(contour)
val octaveFixed = CalibraPitch.PostProcess.fixOctaveErrors(contour)
val noBlips = CalibraPitch.PostProcess.removeBlips(contour, minDurationMs = 80f)
```

### Working with Arrays

```kotlin
val pitches = floatArrayOf(440f, 442f, 880f, 438f)  // Has octave error

// Full processing
val processed = CalibraPitch.PostProcess.process(pitches)

// Individual operations
val smoothed = CalibraPitch.PostProcess.smooth(pitches, windowSize = 5)
val corrected = CalibraPitch.PostProcess.correctOctaveErrors(pitches)
val filtered = CalibraPitch.PostProcess.medianFilter(pitches, kernelSize = 3)
```

## Live Pitch Contour

For visualization, access the growing contour:

```kotlin
// Observe live contour for drawing
detector.livePitchContour.collect { contour ->
    drawPitchCurve(contour.samples)
}

// Set max duration (for scrolling displays)
detector.setContourMaxDuration(30f)  // Keep last 30 seconds

// Clear when starting new recording
detector.clearPitchContour()
```

## Converting to Notes

```kotlin
fun pitchToNote(frequency: Float): NoteInfo {
    if (frequency <= 0) return NoteInfo.UNVOICED

    val a4 = 440.0
    val semitones = 12 * kotlin.math.log2(frequency / a4) + 69

    val noteNames = arrayOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
    val noteIndex = (semitones.toInt() % 12 + 12) % 12
    val octave = (semitones.toInt() / 12) - 1
    val midiNumber = semitones.toInt()
    val centsOff = ((semitones - semitones.toInt()) * 100).toInt()

    return NoteInfo(
        name = "${noteNames[noteIndex]}$octave",
        midi = midiNumber,
        centsOff = centsOff,
        frequency = frequency
    )
}

data class NoteInfo(
    val name: String,
    val midi: Int,
    val centsOff: Int,  // -50 to +50
    val frequency: Float
) {
    companion object {
        val UNVOICED = NoteInfo("--", 0, 0, -1f)
    }
}
```

## Common Patterns

### Tuner App

```kotlin
class TunerViewModel : ViewModel() {
    private var detector: CalibraPitch.Detector? = null
    private var recorder: SonixRecorder? = null

    val note = MutableStateFlow("--")
    val frequency = MutableStateFlow(0f)
    val centsOff = MutableStateFlow(0)

    fun start() {
        detector = CalibraPitch.createDetector()
        recorder = SonixRecorder.createTemporary()

        recorder?.start()

        viewModelScope.launch {
            recorder?.audioBuffers?.collect { buffer ->
                val samples = FloatArray(buffer.sampleCount)
                buffer.fillFloatSamples(samples)

                val point = detector?.detect(samples, buffer.sampleRate) ?: return@collect

                if (point.pitch > 0 && point.confidence > 0.6f) {
                    val noteInfo = pitchToNote(point.pitch)
                    note.value = noteInfo.name
                    frequency.value = noteInfo.frequency
                    centsOff.value = noteInfo.centsOff
                } else {
                    note.value = "--"
                    frequency.value = 0f
                    centsOff.value = 0
                }
            }
        }
    }

    fun stop() {
        recorder?.stop()
        recorder?.release()
        detector?.close()
    }
}
```

### Pitch Visualization

```kotlin
class PitchGraphView : View {
    private val pitchHistory = mutableListOf<Float>()
    private val maxHistory = 100

    fun addPitch(pitch: Float) {
        pitchHistory.add(pitch)
        if (pitchHistory.size > maxHistory) {
            pitchHistory.removeAt(0)
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        // Draw pitch history as scrolling graph
        pitchHistory.forEachIndexed { index, pitch ->
            if (pitch > 0) {
                val x = index * (width / maxHistory.toFloat())
                val y = height - (pitch / 1000f * height)  // Normalize to view
                canvas.drawCircle(x, y, 4f, paint)
            }
        }
    }
}
```

## Troubleshooting

### No Pitch Detected

- Check microphone permission
- Verify audio is reaching detector (print buffer sizes)
- Try lowering confidence threshold
- Check if audio is too quiet (use `QuietHandling.SENSITIVE`)

### Wrong Octave

- Enable processing: `.enableProcessing()`
- Use post-processing: `PostProcess.correctOctaveErrors()`
- Try SwiftF0 algorithm (better octave handling)

### Noisy/Jumpy Readings

- Enable smoothing: `.enableProcessing()`
- Filter by confidence (> 0.7)
- Use median filter in post-processing

## Next Steps

- [Pitch Detection Concepts](../concepts/pitch-detection) - Theory and background
- [Tuner App Recipe](../cookbook/tuner-app) - Complete example
