---
sidebar_position: 3
---

# CalibraVAD

Voice Activity Detection (VAD) for identifying speech and singing in audio. Determines when someone is speaking or singing vs. when there's silence or background noise.

## Quick Start

### Kotlin

```kotlin
val vad = CalibraVAD.create(VADModelProvider.General)
val ratio = vad.getVADRatio(samples, sampleRate = 48000)
if (ratio > 0.5f) {
    println("Voice detected!")
}
vad.release()
```

### Swift

```swift
let vad = CalibraVAD.create(.general)
let ratio = vad.getVADRatio(samples: samples, sampleRate: 48000)
if ratio > 0.5 {
    print("Voice detected!")
}
vad.release()
```

## Backends

| Backend | Kotlin Provider | Swift Provider | Best For | Dependencies |
|---------|----------------|----------------|----------|-------------|
| General | `VADModelProvider.General` | `.general` | Simple detection, low power | None |
| Speech | `VADModelProvider.speech()` | `.speech()` | Speech detection | Silero ONNX |
| SingingRealtime | `VADModelProvider.singingRealtime()` | `.singingRealtime()` | Low-latency singing | SwiftF0 ONNX |

## Creating a VAD

### With Model Provider Only (recommended)

Backend and defaults are inferred from the provider type:

```kotlin
// General (no model required)
val vad = CalibraVAD.create(VADModelProvider.General)

// Speech (Silero model)
val vad = CalibraVAD.create(VADModelProvider.speech())

// Singing realtime (SwiftF0 model)
val vad = CalibraVAD.create(VADModelProvider.singingRealtime())
```

```swift
let vad = CalibraVAD.create(.general)
let vad = CalibraVAD.create(.speech())
let vad = CalibraVAD.create(.singingRealtime())
```

### With Custom Model Provider

```kotlin
// Custom model loader
val vad = CalibraVAD.create(VADModelProvider.Speech { ModelLoader.loadSpeechVAD() })
val vad = CalibraVAD.create(VADModelProvider.SingingRealtime { ModelLoader.loadSingingRealtimeVAD() })
```

```swift
let vad = CalibraVAD.create(.speech { ModelLoader.shared.loadSpeechVAD() })
let vad = CalibraVAD.create(.singingRealtime { ModelLoader.shared.loadSingingRealtimeVAD() })
```

### With Config + Model Provider

```kotlin
val config = VADConfig.Builder()
    .preset(VADConfig.SPEECH)
    .threshold(0.4f)
    .build()

val vad = CalibraVAD.create(config, VADModelProvider.speech())
```

```swift
let config = VADConfig.Builder()
    .preset(.speech)
    .threshold(0.4)
    .build()

let vad = CalibraVAD.create(config: config, modelProvider: .speech())
```

## Configuration

### Presets

| Preset | Kotlin | Swift | Description |
|--------|--------|-------|-------------|
| Speech | `VADConfig.SPEECH` | `.speech` | Silero neural network |
| General | `VADConfig.GENERAL` | `.general` | RMS-based heuristics |
| SingingRealtime | `VADConfig.SINGING_REALTIME` | `.singingRealtime` | SwiftF0 pitch-based |

### Config Properties

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `backend` | `VADBackend` | `SPEECH` | Detection engine |
| `sampleRate` | `Int` | `16000` | Audio sample rate in Hz |
| `threshold` | `Float` | `0.5` | Detection threshold (0.0–1.0) |
| `minSpeechDuration` | `Float` | `0.25` | Minimum speech duration in seconds |
| `minSilenceDuration` | `Float` | `0.25` | Minimum silence duration in seconds |
| `windowSize` | `Int` | `512` | Processing window size in samples |
| `numThreads` | `Int` | `1` | Number of inference threads |
| `modelPath` | `String?` | `null` | Custom model path (null = bundled model) |
| `rmsThreshold` | `Float` | `0.05` | RMS threshold for GENERAL backend |
| `pitchProbThreshold` | `Float` | `0.5` | Pitch probability threshold for GENERAL backend |
| `minPitch` | `Float` | `50` | Minimum pitch in Hz for GENERAL backend |

### Builder Methods

| Method | Description |
|--------|-------------|
| `preset(config)` | Start from a preset configuration |
| `backend(backend)` | Set backend with its default config |
| `sampleRate(rate)` | Set audio sample rate in Hz |
| `threshold(value)` | Set detection threshold (0.0–1.0) |
| `minSpeechDuration(seconds)` | Minimum speech duration |
| `minSilenceDuration(seconds)` | Minimum silence duration |
| `windowSize(samples)` | Processing window size |
| `numThreads(threads)` | Number of inference threads |
| `modelPath(path)` | Custom model path (null = bundled) |
| `rmsThreshold(value)` | RMS threshold for GENERAL backend |
| `pitchProbThreshold(value)` | Pitch probability threshold for GENERAL backend |
| `minPitch(hz)` | Minimum pitch in Hz for GENERAL backend |

## One-Shot Analysis

### getVADRatio

Returns the ratio of voiced frames (0.0 to 1.0). Accepts any sample rate and resamples internally to 16kHz.

```kotlin
val ratio = vad.getVADRatio(samples, sampleRate = 48000)
// 0.0 = silence, 1.0 = continuous voice
```

```swift
let ratio = vad.getVADRatio(samples: samples, sampleRate: 48000)
```

### analyze

Returns a rich `VADResult` with ratio, level, and convenience properties:

```kotlin
val result = vad.analyze(samples, sampleRate = 48000)
if (result != null) {
    println("Ratio: ${result.ratio}")
    println("Level: ${result.level}")  // NONE, PARTIAL, or FULL
    println("Voice detected: ${result.isVoiceDetected}")
}
```

### VADResult

| Property | Type | Description |
|----------|------|-------------|
| `ratio` | `Float` | Voice activity ratio (0.0–1.0) |
| `level` | `VoiceActivityLevel` | `NONE`, `PARTIAL`, or `FULL` |
| `isVoiceDetected` | `Boolean` | True if ratio > 0.5 |
| `isFullActivity` | `Boolean` | True if level is `FULL` |

## Streaming Mode

For real-time processing, use `acceptWaveform` + `isVoiceDetected`:

### Kotlin

```kotlin
recorder.audioBuffers.collect { buffer ->
    vad.acceptWaveform(buffer.toFloatArray(), sampleRate = 48000)
    if (vad.isVoiceDetected()) {
        showVoiceIndicator()
    }
}
```

### Swift

```swift
for await buffer in recorder.audioBuffers {
    vad.acceptWaveform(samples: buffer.toFloatArray(), sampleRate: 48000)
    if vad.isVoiceDetected() {
        showVoiceIndicator()
    }
}
```

### Swift Convenience Methods

```swift
// Quick check with custom threshold
let hasVoice = vad.hasVoiceActivity(samples: samples, sampleRate: 48000, threshold: 0.3)

// Classify activity level
let level = vad.classifyVoiceActivity(samples: samples, sampleRate: 48000)
// .none, .partial, or .full
```

## Methods

| Method | Description |
|--------|-------------|
| `getVADRatio(samples, sampleRate)` | Get voiced frame ratio (0.0–1.0) |
| `analyze(samples, sampleRate)` | Get rich VADResult with level classification |
| `acceptWaveform(samples, sampleRate)` | Feed samples for streaming detection |
| `isVoiceDetected()` | Check if voice is currently detected (streaming mode) |
| `reset()` | Reset state for new audio stream |
| `release()` | Release all resources |

## Common Patterns

### VAD with Recording

```kotlin
class VoiceDetectionViewModel : ViewModel() {
    private var vad: CalibraVAD? = null

    val isVoiceActive = MutableStateFlow(false)
    val voiceLevel = MutableStateFlow(0f)

    fun startListening() {
        vad = CalibraVAD.create(VADModelProvider.speech())

        viewModelScope.launch {
            recorder.audioBuffers.collect { buffer ->
                val ratio = vad!!.getVADRatio(buffer.samples, buffer.sampleRate)
                voiceLevel.value = ratio
                isVoiceActive.value = ratio > 0.5f
            }
        }
    }

    override fun onCleared() {
        vad?.release()
    }
}
```

## Next Steps

- [CalibraPitch](./pitch) — Detect what note is being sung (not just voice presence)
- [CalibraLiveEval](./live-eval) — Live singing evaluation (uses VAD internally)
- [CalibraVocalRange](./vocal-range) — Detect singer's comfortable range
