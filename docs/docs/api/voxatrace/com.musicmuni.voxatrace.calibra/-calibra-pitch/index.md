---
sidebar_label: "CalibraPitch"
---


# CalibraPitch

class [CalibraPitch](index.md)

Unified pitch detection API for singing and speech analysis.

## What is Pitch Detection?

Pitch detection finds the **fundamental frequency (F0)** of audio - the note being sung or spoken. For example, when you sing an A4, the pitch detector outputs 440 Hz.

This is essential for:

- 
   **Singing apps**: Show singers if they're on pitch
- 
   **Tuner apps**: Help musicians tune their instruments
- 
   **Music education**: Score student performances against reference
- 
   **Voice analysis**: Track pitch patterns in speech

## When to Use

| Use Case | API | Example |
|---|---|---|
| Live tuner display | `createDetector()` | Show pitch meter in real-time |
| Karaoke scoring | `createDetector()` + `CalibraLiveEval` | Score singing while playing |
| Analyze recorded audio | `createContourExtractor()` | Extract pitch from audio files |
| Post-process contours | `PostProcess` | Clean up octave errors, smooth noise |

## Quick Start

### Kotlin

```kotlin
// Real-time pitch detection
val detector = CalibraPitch.createDetector()
val point = detector.detect(audioBuffer, sampleRate = 16000)
println("Pitch: ${point.pitch} Hz, Confidence: ${point.confidence}")
detector.close()
```

### Swift

```swift
// Real-time pitch detection
let detector = CalibraPitch.createDetector()
let point = detector.detect(samples: audioBuffer, sampleRate: 16000)
print("Pitch: \(point.pitch) Hz, Confidence: \(point.confidence)")
detector.close()
```

## Usage Tiers (ADR-001)

### Tier 1: Defaults (80% of users)

#### Kotlin

```kotlin
val detector = CalibraPitch.createDetector(PitchDetectorConfig.BALANCED)
val point = detector.detect(buffer, sampleRate)
detector.close()
```

#### Swift

```swift
let detector = CalibraPitch.createDetector(config: .balanced)
let point = detector.detect(samples: buffer, sampleRate: sampleRate)
detector.close()
```

### Tier 2: Builder (15% of users)

#### Kotlin

```kotlin
val config = PitchDetectorConfig.Builder()
    .algorithm(PitchAlgorithm.SWIFT_F0)
    .voiceType(VoiceType.carnaticMale)
    .enableProcessing()
    .build()
val detector = CalibraPitch.createDetector(config, modelProvider = { ModelLoader.loadSwiftF0() })
```

#### Swift

```swift
let config = PitchDetectorConfig.Builder()
    .algorithm(.swiftF0)
    .voiceType(.carnaticMale)
    .enableProcessing()
    .build()
let detector = CalibraPitch.createDetector(
    config: config,
    modelProvider: { ModelLoader.shared.loadSwiftF0() }
)
```

### Tier 3: .copy() (5% of users)

#### Kotlin

```kotlin
val config = PitchDetectorConfig.PRECISE.copy(tolerance = 0.08f)
val detector = CalibraPitch.createDetector(config)
```

## Batch Extraction (Recorded Audio Files)

### Kotlin

```kotlin
val extractor = CalibraPitch.createContourExtractor(
    ContourExtractorConfig.SCORING,
    modelProvider = { ModelLoader.loadSwiftF0() }
)
val contour = extractor.extract(audioSamples, sampleRate = 16000)
extractor.release()
```

### Swift

```swift
let extractor = CalibraPitch.createContourExtractor(
    config: .scoring,
    modelProvider: { ModelLoader.shared.loadSwiftF0() }
)
let contour = extractor.extract(audio: audioSamples, sampleRate: 16000)
extractor.release()
```

## Post-Processing

### Kotlin

```kotlin
// Apply cleanup presets to contours
val cleaned = CalibraPitch.PostProcess.cleanup(contour, ContourCleanup.SCORING)

// Or individual operations
val smoothed = CalibraPitch.PostProcess.smooth(contour)
```

### Swift

```swift
// Apply cleanup presets to contours
let cleaned = CalibraPitch.PostProcess.cleanup(contour: contour, options: .scoring)

// Or individual operations
let smoothed = CalibraPitch.PostProcess.smooth(contour: contour)
```

## Platform Notes

### iOS

- 
   Audio input is typically 48kHz; resampled internally to 16kHz
- 
   SwiftF0 requires `ai-models` module with ONNX runtime
- 
   Use `@ShouldRefineInSwift` methods with provided Swift extensions

### Android

- 
   Audio input is typically 44.1kHz or 16kHz depending on device
- 
   SwiftF0 uses ONNX Runtime for Android
- 
   YIN algorithm has no external dependencies

## Common Pitfalls

1. 
   **Forgetting to close/release**: Always call `detector.close()` or `extractor.release()`
2. 
   **Wrong sample rate**: Pass the actual hardware sample rate; internal resampling handles the rest
3. 
   **SwiftF0 without model**: Register model at startup: `AIModelRegistry.registerSwiftF0 { ... }`
4. 
   **Octave errors in contours**: Use `ContourCleanup.SCORING` or `PostProcess.correctOctaveErrors()`
5. 
   **Low confidence in quiet audio**: Use `QuietHandling.LENIENT` for soft singers

#### See also

| | |
|---|---|
| [CalibraLiveEval](../-calibra-live-eval/index.md) | For live singing evaluation with scoring |
| [CalibraVAD](../-calibra-v-a-d/index.md) | For detecting when someone is singing vs. silence |
| [PitchDetectorConfig](../../com.musicmuni.voxatrace.calibra.model/-pitch-detector-config/index.md) | Configuration options for real-time detection |
| [ContourExtractorConfig](../-contour-extractor-config/index.md) | Configuration options for batch extraction |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |
| [ContourExtractor](-contour-extractor/index.md) | [common]<br/>class [ContourExtractor](-contour-extractor/index.md)<br/>Pitch contour extractor for batch processing of complete audio files. |
| [Detector](-detector/index.md) | [common]<br/>abstract class [Detector](-detector/index.md)<br/>Realtime pitch detector for processing audio buffers. |
| [PostProcess](-post-process/index.md) | [common]<br/>object [PostProcess](-post-process/index.md)<br/>Post-processing utilities for pitch arrays. |
