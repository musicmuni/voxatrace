---
sidebar_position: 2
---

# Voice Activity Detection

Voice Activity Detection (VAD) determines when someone is speaking or singing versus when there's silence or background noise.

## What is VAD?

VAD answers a simple question: **"Is there voice in this audio?"**

Unlike pitch detection (which tells you *what note* is being sung), VAD tells you *whether someone is singing at all*.

## Use Cases

| Scenario | How VAD Helps |
|----------|---------------|
| **Recording app** | Auto-start/stop recording when voice is detected |
| **Transcription** | Skip silent sections to save processing |
| **Singing evaluation** | Only score segments where the user is singing |
| **Noise gate** | Mute output when no voice is present |
| **Voice commands** | Detect when user starts speaking |

## How It Works

VAD analyzes audio and returns:

1. **Voice Detected** (boolean): Is voice present right now?
2. **VAD Ratio** (0.0 to 1.0): What percentage of the audio contains voice?

## Backends

VoxaTrace offers four VAD backends optimized for different scenarios:

### General (Energy-based)

Simple and fast. Detects when audio exceeds an energy threshold.

| Aspect | Details |
|--------|---------|
| **Speed** | Very fast |
| **Accuracy** | Basic |
| **Dependencies** | None |
| **Best For** | Simple voice detection, low-resource devices |

```kotlin
val vad = CalibraVAD.create(VADModelProvider.General)
```

### Speech (Silero VAD)

Neural network trained on speech. Very accurate for spoken content.

| Aspect | Details |
|--------|---------|
| **Speed** | Fast |
| **Accuracy** | Excellent for speech |
| **Dependencies** | ONNX Runtime, ai-models |
| **Best For** | Voice assistants, transcription |

```kotlin
val vad = CalibraVAD.create(
    VADModelProvider.Speech { ModelLoader.loadSpeechVAD() }
)
```

### Singing (YAMNet-based)

Distinguishes singing from speech and other sounds.

| Aspect | Details |
|--------|---------|
| **Speed** | Moderate |
| **Accuracy** | Excellent for singing |
| **Dependencies** | ONNX Runtime, ai-models |
| **Best For** | Karaoke apps, singing detection |

```kotlin
val vad = CalibraVAD.create(
    VADModelProvider.Singing { ModelLoader.loadSingingVAD() }
)
```

### Singing Realtime (SwiftF0-based)

Uses pitch detection confidence for low-latency singing detection.

| Aspect | Details |
|--------|---------|
| **Speed** | Very fast |
| **Accuracy** | Good for singing |
| **Dependencies** | ONNX Runtime, ai-models |
| **Best For** | Real-time singing apps, low latency |

```kotlin
val vad = CalibraVAD.create(
    VADModelProvider.SingingRealtime { ModelLoader.loadSingingRealtimeVAD() }
)
```

## API Modes

### Batch Mode

Analyze a complete audio segment:

```kotlin
val vad = CalibraVAD.create(VADModelProvider.General)

// Get ratio of voiced frames (0.0 = all silence, 1.0 = all voice)
val ratio = vad.getVADRatio(samples, sampleRate = 48000)

// Get rich result with level classification
val result = vad.analyze(samples, sampleRate = 48000)
println("Ratio: ${result?.ratio}, Level: ${result?.level}")

vad.release()
```

### Streaming Mode

For real-time processing, feed audio continuously:

```kotlin
val vad = CalibraVAD.create(VADModelProvider.Speech { ... })

recorder.audioBuffers.collect { buffer ->
    vad.acceptWaveform(buffer.toFloatArray(), buffer.sampleRate)

    if (vad.isVoiceDetected()) {
        showVoiceIndicator()
    } else {
        hideVoiceIndicator()
    }
}

vad.release()
```

## VAD Result

The `analyze()` method returns a `VADResult`:

```kotlin
data class VADResult(
    val ratio: Float,           // 0.0 to 1.0
    val level: VADLevel,        // SILENCE, SPARSE, MODERATE, STRONG
    val isSilence: Boolean,     // ratio < 0.1
    val hasVoice: Boolean,      // ratio > 0.3
    val isStrongVoice: Boolean  // ratio > 0.7
)
```

Use the level for UI feedback:

```kotlin
when (result.level) {
    VADLevel.SILENCE -> showSilenceState()
    VADLevel.SPARSE -> showWeakVoice()
    VADLevel.MODERATE -> showActiveVoice()
    VADLevel.STRONG -> showStrongVoice()
}
```

## Threshold Tuning

Adjust sensitivity with `VADConfig`:

```kotlin
val config = VADConfig.Builder()
    .preset(VADConfig.SPEECH)
    .threshold(0.3f)  // Lower = more sensitive (more false positives)
    .build()

val vad = CalibraVAD.create(config, VADModelProvider.Speech { ... })
```

| Threshold | Behavior |
|-----------|----------|
| 0.2 | Very sensitive, catches faint voice |
| 0.4 | Balanced (default for Speech) |
| 0.6 | Strict, requires clear voice |
| 0.8 | Very strict, only loud/clear voice |

## Common Patterns

### Auto-Record When Voice Detected

```kotlin
var isRecording = false

recorder.audioBuffers.collect { buffer ->
    vad.acceptWaveform(buffer.toFloatArray(), buffer.sampleRate)

    if (vad.isVoiceDetected() && !isRecording) {
        isRecording = true
        startRecording()
    } else if (!vad.isVoiceDetected() && isRecording) {
        // Add debounce to avoid cutting off between words
        isRecording = false
        stopRecording()
    }
}
```

### Skip Silent Sections for Analysis

```kotlin
val contour = pitchExtractor.extract(audio, sampleRate)
val vadRatio = vad.getVADRatio(audio, sampleRate)

if (vadRatio < 0.1f) {
    // Mostly silence, skip scoring
    return SkipResult("No voice detected")
}

// Proceed with evaluation
val score = evaluator.evaluate(contour)
```

### Combine with Pitch Detection

```kotlin
recorder.audioBuffers.collect { buffer ->
    val samples = buffer.toFloatArray()

    // Quick VAD check first (cheap)
    vad.acceptWaveform(samples, buffer.sampleRate)
    if (!vad.isVoiceDetected()) {
        updateUI(pitch = null)  // Show silence
        return@collect
    }

    // Only run pitch detection if voice detected (expensive)
    val point = detector.detect(samples, buffer.sampleRate)
    updateUI(pitch = point)
}
```

## Performance Considerations

| Backend | Latency | Memory |
|---------|---------|--------|
| General | < 1ms | Minimal |
| Speech | ~5ms | ~10 MB (model) |
| Singing | ~20ms | ~50 MB (model) |
| SingingRealtime | ~5ms | ~10 MB (model) |

For battery-sensitive apps, use **General** backend and only load neural models when needed.

## Next Steps

- [CalibraVAD API Reference](/api/calibra/CalibraVAD) - Full API documentation
- [Pitch Detection](/docs/concepts/pitch-detection) - Detect what note is being sung
- [Live Evaluation](/docs/concepts/live-evaluation) - Score singing in real-time
