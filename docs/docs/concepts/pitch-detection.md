---
sidebar_position: 1
---

# Pitch Detection

Understanding how pitch detection works helps you choose the right settings for your app.

## What is Pitch?

**Pitch** is the perceptual quality that allows us to order sounds from "low" to "high." When you sing an A4 note, your vocal cords vibrate 440 times per second, producing a **fundamental frequency (F0)** of 440 Hz.

Pitch detection algorithms analyze audio to find this fundamental frequency.

## Frequency vs. Note

| Concept | Example | What It Is |
|---------|---------|------------|
| **Frequency** | 440 Hz | How many times per second the waveform repeats |
| **Note** | A4 | Musical name (letter + octave number) |
| **MIDI Number** | 69 | Integer representation (A4 = 69) |
| **Cents** | +5 cents | How many hundredths of a semitone off from perfect |

VoxaTrace returns frequency in Hz. Convert to notes using:

```kotlin
// Frequency to MIDI number
val midi = 12 * log2(frequency / 440.0) + 69

// MIDI to note name
val noteNames = arrayOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
val noteName = noteNames[midi.toInt() % 12]
val octave = (midi.toInt() / 12) - 1
```

## Detection Algorithms

VoxaTrace offers two pitch detection algorithms:

### YIN (Default)

A classic DSP algorithm that works well for most use cases.

| Aspect | Details |
|--------|---------|
| **Accuracy** | Good for clean audio |
| **Latency** | ~50ms |
| **Dependencies** | None (pure Kotlin) |
| **Best For** | Simple tuners, low-resource devices |

### SwiftF0 (Neural Network)

A deep learning model trained on singing and speech data.

| Aspect | Details |
|--------|---------|
| **Accuracy** | Excellent, handles noise well |
| **Latency** | ~50ms |
| **Dependencies** | ONNX Runtime, ai-models module |
| **Best For** | Singing apps, noisy environments |

### Choosing an Algorithm

```kotlin
// YIN - no dependencies, good for simple apps
val detector = CalibraPitch.createDetector(
    PitchDetectorConfig.BALANCED
)

// SwiftF0 - better accuracy, requires model
val detector = CalibraPitch.createDetector(
    PitchDetectorConfig.Builder()
        .algorithm(PitchAlgorithm.SWIFT_F0)
        .build(),
    modelProvider = { ModelLoader.loadSwiftF0() }
)
```

## Key Concepts

### Confidence

Each pitch detection returns a **confidence** value (0.0 to 1.0) indicating how certain the algorithm is about the result.

- **> 0.8**: High confidence, reliable pitch
- **0.5 - 0.8**: Moderate confidence, probably correct
- **< 0.5**: Low confidence, might be noise or wrong octave

Use confidence to filter unreliable detections:

```kotlin
val point = detector.detect(samples, sampleRate)
if (point.confidence > 0.7f) {
    // Trust this pitch
    updateDisplay(point.pitch)
}
```

### Octave Errors

Sometimes the algorithm detects the wrong **octave** - reporting 880 Hz instead of 440 Hz (one octave too high) or 220 Hz (one octave too low).

VoxaTrace provides octave correction:

```kotlin
// Enable in detector config
val config = PitchDetectorConfig.Builder()
    .enableProcessing()  // Enables smoothing + octave correction
    .build()

// Or fix in post-processing
val corrected = CalibraPitch.PostProcess.correctOctaveErrors(pitches)
```

### Voiced vs. Unvoiced

Not all audio contains pitch. Consonants, noise, and silence are **unvoiced**.

VoxaTrace returns `-1` for unvoiced frames:

```kotlin
val point = detector.detect(samples, sampleRate)
if (point.pitch > 0) {
    // Voiced - show pitch
} else {
    // Unvoiced - show silence indicator
}
```

## Real-time vs. Batch

### Real-time (Detector)

For live audio streams - process one buffer at a time:

```kotlin
val detector = CalibraPitch.createDetector()
recorder.audioBuffers.collect { buffer ->
    val point = detector.detect(buffer.toFloatArray(), buffer.sampleRate)
    updateUI(point)
}
detector.close()
```

### Batch (ContourExtractor)

For recorded audio files - process the entire file at once:

```kotlin
val extractor = CalibraPitch.createContourExtractor(ContourExtractorConfig.SCORING)
val contour = extractor.extract(audioSamples, sampleRate = 16000)
// contour.samples contains all pitch points with timestamps
extractor.release()
```

## Frequency Range

Human singing typically spans:

| Voice Type | Low | High |
|------------|-----|------|
| Bass | E2 (82 Hz) | E4 (330 Hz) |
| Baritone | A2 (110 Hz) | A4 (440 Hz) |
| Tenor | C3 (131 Hz) | C5 (523 Hz) |
| Alto | F3 (175 Hz) | F5 (698 Hz) |
| Soprano | C4 (262 Hz) | C6 (1047 Hz) |

VoxaTrace detects 50 Hz to 2000 Hz by default. Customize for your use case:

```kotlin
val config = PitchDetectorConfig.Builder()
    .voiceType(VoiceType.Soprano)  // Optimizes for soprano range
    .build()
```

## Sample Rate

Pitch detection works best at **16 kHz**. VoxaTrace resamples automatically:

```kotlin
// Pass any sample rate - VoxaTrace handles resampling
val point = detector.detect(samples, sampleRate = 48000)  // Resampled internally
```

## Next Steps

- [Detecting Pitch Guide](/docs/guides/detecting-pitch) - Implementation guide
- [CalibraPitch API Reference](/api/calibra/CalibraPitch) - Full API documentation
- [Voice Activity Detection](/docs/concepts/voice-activity) - Detect when someone is singing
