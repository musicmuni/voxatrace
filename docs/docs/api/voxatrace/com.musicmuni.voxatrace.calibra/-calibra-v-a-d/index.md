---
sidebar_label: "CalibraVAD"
---


# CalibraVAD

class [CalibraVAD](index.md)

Voice Activity Detection (VAD) for identifying speech/singing in audio.

## What is VAD?

Voice Activity Detection determines **when someone is speaking or singing** vs. when there's silence or background noise. Use it for:

- 
   **Recording apps**: Auto-start/stop recording when voice is detected
- 
   **Transcription**: Skip silent sections to save processing
- 
   **Singing evaluation**: Only score segments where the user is actually singing
- 
   **Noise gate control**: Mute audio when no voice is present

## When to Use

| Scenario | Backend | Why |
|---|---|---|
| Simple voice detection | `General` | Fast, no model required |
| Accurate speech detection | `Speech` | Silero neural network, best for speech |
| Singing detection | `Singing` | YAMNet-based, distinguishes singing from speech |
| Low-latency singing | `SingingRealtime` | SwiftF0-based, minimal delay |

## Quick Start

### Kotlin

```kotlin
// Simple energy-based detection (no model required)
val vad = CalibraVAD.create(VADModelProvider.General)
val ratio = vad.getVADRatio(samples, sampleRate = 48000)  // 0.0 to 1.0
if (ratio 0.5f) {
    println("Voice detected!")
}
vad.release()
```

### Swift

```swift
// Simple energy-based detection (no model required)
let vad = CalibraVAD.create(modelProvider: .general())
let ratio = vad.getVADRatio(samples: samples, sampleRate: 48000)
if ratio 0.5 {
    print("Voice detected!")
}
vad.release()
```

## Usage Tiers (ADR-001)

### Tier 1: Simple Creation (80% of users)

#### Kotlin

```kotlin
// GENERAL backend (no model required)
val vad = CalibraVAD.create(VADModelProvider.General)

// SPEECH backend (Silero model)
val vad = CalibraVAD.create(VADModelProvider.Speech { ModelLoader.loadSpeechVAD() })
```

#### Swift

```swift
// GENERAL backend (no model required)
let vad = CalibraVAD.create(modelProvider: .general())

// SPEECH backend (Silero model)
let vad = CalibraVAD.create(
    modelProvider: .speech { ModelLoader.shared.loadSpeechVAD() }
)
```

### Tier 2: Custom Config (15% of users)

#### Kotlin

```kotlin
val config = VADConfig.Builder()
    .preset(VADConfig.SPEECH)
    .threshold(0.4f)
    .build()
val vad = CalibraVAD.create(config, VADModelProvider.Speech { ModelLoader.loadSpeechVAD() })
```

#### Swift

```swift
let config = VADConfig.Builder()
    .preset(.speech)
    .threshold(0.4)
    .build()
let vad = CalibraVAD.create(
    config: config,
    modelProvider: .speech { ModelLoader.shared.loadSpeechVAD() }
)
```

## Streaming Mode

For real-time processing, use streaming mode:

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

## Platform Notes

### iOS

- 
   Audio from microphone is typically 48kHz; resampled internally to 16kHz
- 
   Neural network backends (Speech, Singing) require ONNX Runtime
- 
   General backend works without any external dependencies

### Android

- 
   Audio from microphone varies by device (44.1kHz, 48kHz, 16kHz)
- 
   Neural network backends use ONNX Runtime for Android
- 
   General backend is pure Kotlin, no native dependencies

## Common Pitfalls

1. 
   **Forgetting to release**: Call `vad.release()` to free native resources
2. 
   **Using Speech backend for singing**: Speech VAD is trained on speech, not singing
3. 
   **Too sensitive threshold**: Default thresholds work well; lower values = more false positives
4. 
   **Not resetting between streams**: Call `vad.reset()` when starting a new audio source

#### See also

| | |
|---|---|
| [CalibraPitch](../-calibra-pitch/index.md) | For pitch detection (what note, not just voice presence) |
| [CalibraLiveEval](../-calibra-live-eval/index.md) | For live singing evaluation (uses VAD internally) |
| [VADConfig](../../com.musicmuni.voxatrace.calibra.model/-v-a-d-config/index.md) | Configuration options for sensitivity tuning |
| [VADModelProvider](../../com.musicmuni.voxatrace.calibra.model/-v-a-d-model-provider/index.md) | Type-safe model provider selection |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [acceptWaveform](accept-waveform.md) | [common]<br/>fun [acceptWaveform](accept-waveform.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000)<br/>Feed audio samples for streaming detection. Use with isSpeechDetected for real-time detection. |
| [analyze](analyze.md) | [common]<br/>fun [analyze](analyze.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000): [VADResult](../../com.musicmuni.voxatrace.calibra.model/-v-a-d-result/index.md)?<br/>Analyze audio and return rich VAD result. |
| [getVADRatio](get-v-a-d-ratio.md) | [common]<br/>fun [getVADRatio](get-v-a-d-ratio.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Get ratio of voiced frames in audio (0.0 to 1.0). Higher values indicate more speech/singing content. |
| [isVoiceDetected](is-voice-detected.md) | [common]<br/>fun [isVoiceDetected](is-voice-detected.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if voice is currently detected. Call after [acceptWaveform](accept-waveform.md) for streaming mode. |
| [release](release.md) | [common]<br/>fun [release](release.md)()<br/>Release all resources. Must be called when done to free native resources. |
| [reset](reset.md) | [common]<br/>fun [reset](reset.md)()<br/>Reset VAD state for new audio stream. Call when starting to process a new audio source. |
