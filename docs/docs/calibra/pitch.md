---
sidebar_position: 2
---

# CalibraPitch

Real-time pitch detection and batch pitch extraction for singing and speech analysis. Detects the fundamental frequency (F0) of audio with two algorithm backends.

## Quick Start

### Kotlin

```kotlin
val detector = CalibraPitch.createDetector()
val point = detector.detect(audioBuffer, sampleRate = 16000)
println("Pitch: ${point.pitch} Hz, Confidence: ${point.confidence}")
detector.close()
```

### Swift

```swift
let detector = CalibraPitch.createDetector()
let point = detector.detect(samples: audioBuffer, sampleRate: 16000)
print("Pitch: \(point.pitch) Hz, Confidence: \(point.confidence)")
detector.close()
```

## When to Use

| Use Case | API | Example |
|----------|-----|---------|
| Live tuner display | `createDetector()` | Show pitch meter in real-time |
| Karaoke scoring | `createDetector()` + `CalibraLiveEval` | Score singing while playing |
| Analyze recorded audio | `createContourExtractor()` | Extract pitch from audio files |
| Post-process contours | `PostProcess` | Clean up octave errors, smooth noise |

## Detector Configuration

### Presets

| Preset | Kotlin | Swift | Description |
|--------|--------|-------|-------------|
| Balanced | `PitchDetectorConfig.BALANCED` | `.balanced` | Default for most use cases |
| Precise | `PitchDetectorConfig.PRECISE` | `.precise` | Larger buffer, stricter thresholds |
| Relaxed | `PitchDetectorConfig.RELAXED` | `.relaxed` | More forgiving, higher recall |

### Builder

#### Kotlin

```kotlin
val config = PitchDetectorConfig.Builder()
    .algorithm(PitchAlgorithm.SWIFT_F0)
    .voiceType(VoiceType.carnaticMale)
    .quietHandling(QuietHandling.SENSITIVE)
    .enableProcessing()
    .build()

val detector = CalibraPitch.createDetector(config, modelProvider = { ModelLoader.loadSwiftF0() })
```

#### Swift

```swift
let config = PitchDetectorConfig.Builder()
    .algorithm(.swiftF0)
    .voiceType(.carnaticMale)
    .quietHandling(.sensitive)
    .enableProcessing()
    .build()

let detector = CalibraPitch.createDetector(config: config, modelProvider: { ModelLoader.loadSwiftF0() })
```

### Config Properties

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `algorithm` | `PitchAlgorithm` | `YIN` | Detection algorithm (`YIN` or `SWIFT_F0`) |
| `bufferSize` | `Int` | `2048` | Audio buffer size for analysis |
| `hopSize` | `Int` | `160` | Hop size between frames in samples |
| `tolerance` | `Float` | `0.15` | YIN algorithm tolerance (lower = more accurate) |
| `minFreq` | `Float` | `80` | Minimum detectable frequency in Hz |
| `maxFreq` | `Float` | `1000` | Maximum detectable frequency in Hz |
| `amplitudeGateDb` | `Float` | `-40` | RMS threshold in dB for gating quiet frames |
| `confidenceThreshold` | `Float` | `0.75` | Minimum confidence to accept pitch (0.0-1.0) |
| `enableSmoothing` | `Boolean` | `false` | Enable pitch smoothing filter |
| `enableOctaveCorrection` | `Boolean` | `false` | Enable octave error correction |

### Builder Methods

| Method | Description |
|--------|-------------|
| `preset(config)` | Start from a preset configuration |
| `algorithm(algo)` | Set pitch detection algorithm |
| `voiceType(type)` | Set frequency range for a voice type |
| `quietHandling(handling)` | Set amplitude gate threshold |
| `strictness(strictness)` | Set confidence threshold |
| `enableProcessing()` | Enable smoothing + octave correction |
| `bufferSize(size)` | Set buffer size for analysis |
| `hopSize(samples)` | Set hop size between frames |
| `tolerance(value)` | Set YIN algorithm tolerance |
| `swiftF0BatchSize(samples)` | Set SwiftF0 batch size |

## Algorithms

| Algorithm | Best For | Latency | Dependencies |
|-----------|----------|---------|-------------|
| `YIN` | Real-time, low power, edge cases | ~50ms | None (pure DSP) |
| `SWIFT_F0` | Vocals, high accuracy | ~16ms per frame | ONNX Runtime |

SwiftF0 achieves 91.80% harmonic mean accuracy at 10dB SNR with only 95k parameters. It requires a model provider:

```kotlin
// Register globally at app startup
AIModelRegistry.registerSwiftF0 { ModelLoader.loadSwiftF0() }

// Or pass per-instance
val detector = CalibraPitch.createDetector(config, modelProvider = { ModelLoader.loadSwiftF0() })
```

## Voice Types

Optimize frequency range for different singing styles:

| Voice Type | Kotlin | Swift | Range (Hz) |
|------------|--------|-------|------------|
| Auto | `VoiceType.auto` | `.auto` | 80–1000 |
| Western Soprano | `VoiceType.westernSoprano` | `.westernSoprano` | 250–1000 |
| Western Alto | `VoiceType.westernAlto` | `.westernAlto` | 180–700 |
| Western Tenor | `VoiceType.westernTenor` | `.westernTenor` | 130–500 |
| Western Bass | `VoiceType.westernBass` | `.westernBass` | 80–350 |
| Western Child | `VoiceType.westernChild` | `.westernChild` | 200–1200 |
| Carnatic Male | `VoiceType.carnaticMale` | `.carnaticMale` | 90–450 |
| Carnatic Female | `VoiceType.carnaticFemale` | `.carnaticFemale` | 140–900 |
| Carnatic Child | `VoiceType.carnaticChild` | `.carnaticChild` | 200–1000 |
| Hindustani Male | `VoiceType.hindustaniMale` | `.hindustaniMale` | 90–450 |
| Hindustani Female | `VoiceType.hindustaniFemale` | `.hindustaniFemale` | 180–900 |
| Hindustani Child | `VoiceType.hindustaniChild` | `.hindustaniChild` | 200–1000 |
| Pop Male | `VoiceType.popMale` | `.popMale` | 100–500 |
| Pop Female | `VoiceType.popFemale` | `.popFemale` | 180–800 |
| Pop Child | `VoiceType.popChild` | `.popChild` | 200–1000 |
| Indian Film Male | `VoiceType.indianFilmMale` | `.indianFilmMale` | 100–500 |
| Indian Film Female | `VoiceType.indianFilmFemale` | `.indianFilmFemale` | 180–900 |
| Indian Film Child | `VoiceType.indianFilmChild` | `.indianFilmChild` | 200–1000 |

## Quiet Handling

| Level | Kotlin | Swift | Gate (dB) | Description |
|-------|--------|-------|-----------|-------------|
| Sensitive | `QuietHandling.SENSITIVE` | `.sensitive` | -50 | Soft singing, quiet room |
| Normal | `QuietHandling.NORMAL` | `.normal` | -40 | Typical environment (default) |
| Noisy | `QuietHandling.NOISY` | `.noisy` | -30 | Loud environment |

## Detection Strictness

| Level | Kotlin | Swift | Threshold | Description |
|-------|--------|-------|-----------|-------------|
| Strict | `DetectionStrictness.STRICT` | `.strict` | 0.85 | Fewer false positives |
| Balanced | `DetectionStrictness.BALANCED` | `.balanced` | 0.75 | Default setting |
| Lenient | `DetectionStrictness.LENIENT` | `.lenient` | 0.65 | Catches more notes |

## PitchPoint

Each detection returns a `PitchPoint` with:

| Property | Type | Description |
|----------|------|-------------|
| `pitch` | `Float` | Frequency in Hz (-1 if unvoiced) |
| `confidence` | `Float` | Detection confidence (0.0–1.0) |
| `timeSeconds` | `Float` | Timestamp in seconds |
| `isSinging` | `Boolean` | Whether pitch was detected (pitch > 0) |
| `midiNote` | `Int` | MIDI note number (69 = A4) |
| `note` | `String?` | Note name with octave (e.g., "A4", "C#5") |
| `centsOff` | `Int` | Cents deviation from nearest note (-50 to +50) |
| `tuning` | `Tuning` | `SILENT`, `FLAT`, `IN_TUNE`, or `SHARP` |

## Real-time Detection

### Detector Methods

| Method | Description |
|--------|-------------|
| `detect(samples, sampleRate)` | Detect pitch from audio buffer. Resamples to 16kHz internally. |
| `getAmplitude(samples, sampleRate)` | Get RMS amplitude of audio buffer |
| `reset()` | Reset state and internal buffer |
| `release()` / `close()` | Release all resources |
| `duplicate()` | Create independent copy with same config |
| `setContourMaxDuration(seconds)` | Set max duration for live pitch contour |
| `clearPitchContour()` | Clear accumulated pitch contour |

### Detector Properties

| Property | Type | Description |
|----------|------|-------------|
| `config` | `PitchDetectorConfig` | Configuration used to create this detector |
| `latencyMs` | `Float` | Detection latency in milliseconds |
| `hasProcessing` | `Boolean` | Whether post-processing is available |
| `processingEnabled` | `Boolean` | Enable/disable post-processing at runtime |
| `livePitchContour` | `StateFlow<PitchContour>` | Accumulated pitch contour for visualization |

### Observing Live Pitch Contour

#### Kotlin (StateFlow)

```kotlin
detector.livePitchContour.collect { contour ->
    // Update scrolling pitch display
    updatePitchVisualization(contour)
}
```

#### Swift (Observer)

```swift
let task = detector.observeLivePitchContour { contour in
    self.pitchContour = contour
}

// Cancel when done
task.cancel()
```

## Batch Extraction

Extract a complete pitch contour from recorded audio.

### Contour Extractor Presets

| Preset | Kotlin | Swift | Description |
|--------|--------|-------|-------------|
| Default | `ContourExtractorConfig.DEFAULT` | `.default` | Balanced with scoring cleanup |
| Scoring | `ContourExtractorConfig.SCORING` | `.scoring` | Optimized for melody evaluation |
| Display | `ContourExtractorConfig.DISPLAY` | `.display` | Optimized for visualization |
| Raw | `ContourExtractorConfig.RAW` | `.raw` | No post-processing |

### Kotlin

```kotlin
val extractor = CalibraPitch.createContourExtractor(
    ContourExtractorConfig.SCORING,
    modelProvider = { ModelLoader.loadSwiftF0() }
)
val contour = extractor.extract(audioSamples, sampleRate = 44100)
println("Duration: ${contour.duration}s, Voiced: ${(contour.voicedRatio * 100).toInt()}%")
extractor.release()
```

### Swift

```swift
let extractor = CalibraPitch.createContourExtractor(
    config: .scoring,
    modelProvider: { ModelLoader.loadSwiftF0() }
)
let contour = extractor.extract(audio: audioSamples, sampleRate: 44100)
print("Duration: \(contour.duration)s, Voiced: \(Int(contour.voicedRatio * 100))%")
extractor.release()
```

### Contour Extractor Builder

#### Kotlin

```kotlin
val config = ContourExtractorConfig.Builder()
    .pitchPreset(PitchPreset.PRECISE)
    .algorithm(PitchAlgorithm.SWIFT_F0)
    .sampleRate(16000)
    .hopMs(10)
    .cleanup(ContourCleanup.SCORING)
    .voiceType(VoiceType.carnaticMale)
    .build()

val extractor = CalibraPitch.createContourExtractor(config, modelProvider = { ModelLoader.loadSwiftF0() })
```

#### Swift

```swift
let config = ContourExtractorConfig.Builder()
    .pitchPreset(.precise)
    .algorithm(.swiftF0)
    .sampleRate(16000)
    .hopMs(10)
    .cleanup(.scoring)
    .voiceType(.carnaticMale)
    .build()

let extractor = CalibraPitch.createContourExtractor(config: config, modelProvider: { ModelLoader.loadSwiftF0() })
```

### Contour Cleanup Presets

| Preset | Kotlin | Swift | Description |
|--------|--------|-------|-------------|
| Raw | `ContourCleanup.RAW` | `.raw` | No post-processing |
| Scoring | `ContourCleanup.SCORING` | `.scoring` | Octave + boundary + blip removal |
| Display | `ContourCleanup.DISPLAY` | `.display` | Scoring + smoothing for visualization |

## Post-Processing

Clean up pitch contours with `CalibraPitch.PostProcess`.

### Contour-Level Methods

```kotlin
// Apply a cleanup preset
val cleaned = CalibraPitch.PostProcess.cleanup(contour, ContourCleanup.SCORING)

// Individual operations
val fixed = CalibraPitch.PostProcess.fixOctaveErrors(contour)
val noBoundary = CalibraPitch.PostProcess.fixBoundaryOctaves(contour)
val noBlips = CalibraPitch.PostProcess.removeBlips(contour, minDurationMs = 80f)
val smoothed = CalibraPitch.PostProcess.smooth(contour)
```

```swift
let cleaned = CalibraPitch.PostProcess.cleanup(contour, options: .scoring)

let fixed = CalibraPitch.PostProcess.fixOctaveErrors(contour)
let noBoundary = CalibraPitch.PostProcess.fixBoundaryOctaves(contour)
let noBlips = CalibraPitch.PostProcess.removeBlips(contour, minDurationMs: 80)
let smoothed = CalibraPitch.PostProcess.smooth(contour)
```

### Array-Level Methods

| Method | Description |
|--------|-------------|
| `process(pitchesHz)` | Full processing (smoothing + octave correction) |
| `smooth(pitchesHz, windowSize)` | Smoothing filter only |
| `correctOctaveErrors(pitchesHz, thresholdCents, referencePitchHz)` | Fix octave jumps |
| `medianFilter(pitchesHz, kernelSize)` | Median filter for spike removal |
| `rejectOutliers(pitchesHz, hopMs, minDurationMs)` | Remove short pitch runs (blips) |
| `correctBoundaryOctaves(pitchesHz, hopMs, boundaryWindowMs)` | Fix octave errors at phrase edges |

## PitchContour

A sequence of `PitchPoint` values over time.

| Property | Type | Description |
|----------|------|-------------|
| `samples` | `List<PitchPoint>` | Pitch points in chronological order |
| `sampleRate` | `Int` | Original audio sample rate |
| `duration` | `Float` | Duration in seconds |
| `voicedRatio` | `Float` | Ratio of voiced to total samples (0.0–1.0) |
| `size` | `Int` | Number of samples |
| `isEmpty` | `Boolean` | True if contour has no samples |

| Method | Description |
|--------|-------------|
| `slice(startTime, endTime, relativeTimes)` | Extract a time range |
| `fromArrays(times, pitches, sampleRate)` | Create from parallel arrays |
| `fromPoints(points, sampleRate)` | Create from a list of PitchPoints |

## Common Patterns

### Pitch Detector ViewModel

```kotlin
class PitchViewModel : ViewModel() {
    private var detector: CalibraPitch.Detector? = null

    val currentPitch = MutableStateFlow(PitchPoint.EMPTY)

    fun startDetection() {
        detector = CalibraPitch.createDetector(PitchDetectorConfig.BALANCED)

        viewModelScope.launch {
            recorder.audioBuffers.collect { buffer ->
                val point = detector!!.detect(buffer.samples, buffer.sampleRate)
                currentPitch.value = point
            }
        }
    }

    override fun onCleared() {
        detector?.close()
    }
}
```

## Platform Notes

- **iOS**: Audio input is typically 48kHz; resampled internally to 16kHz. SwiftF0 requires `ai-models` module with ONNX Runtime.
- **Android**: Audio input is typically 44.1kHz or 16kHz depending on device. SwiftF0 uses ONNX Runtime for Android. YIN has no external dependencies.

## Next Steps

- [CalibraVAD](./vad) — Detect when someone is singing vs. silence
- [CalibraLiveEval](./live-eval) — Live singing evaluation with scoring
- [CalibraMelodyEval](./melody-eval) — Evaluate singing accuracy against a reference
- [Utilities](./utilities) — Shared model types (PitchPoint, PitchContour, VoiceType)
