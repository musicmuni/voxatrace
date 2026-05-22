---
sidebar_position: 2
---

# PitchDetection

Realtime pitch detection and batch contour extraction. `PitchDetection` is the public facade for both workflows; it returns a `PitchDetector` (realtime) or `PitchContourExtractor` (batch).

## Quick Start

### Kotlin

```kotlin
// Realtime detection
val detector = PitchDetection.createDetector()
val point = detector.detect(audioBuffer, sampleRate = 16000)
detector.close()

// Batch extraction
val extractor = PitchDetection.createContourExtractor(
    ContourExtractorConfig.SCORING,
    modelProvider = { ModelLoader.loadSwiftF0() }
)
val contour = extractor.extract(audioSamples, sampleRate = 16000)
extractor.release()
```

### Swift

```swift
let detector = PitchDetection.createDetector()
let point = detector.detect(samples: audioBuffer, sampleRate: 48000)
detector.close()

let extractor = PitchDetection.createContourExtractor(config: .scoring) {
    ModelLoader.shared.loadSwiftF0()
}
let contour = extractor.extract(audio: audioSamples, sampleRate: 48000)
extractor.release()
```

## Factory Methods

| Method | Returns | Use for |
|--------|---------|---------|
| `createDetector(config, modelProvider)` | `PitchDetector` | Realtime, frame-by-frame |
| `createContourExtractor(config, modelProvider)` | `PitchContourExtractor` | Batch, whole recording |

`modelProvider` is required when the algorithm is `PitchAlgorithm.SWIFT_F0` and no global provider was registered via `AIModelRegistry.registerSwiftF0 { ... }`. Both factories `require` a resolvable provider in that case (`IllegalArgumentException` otherwise).

## PitchDetectorConfig

### Presets

| Preset | Kotlin | Swift | bufferSize | tolerance | confidenceThreshold |
|--------|--------|-------|-----------|-----------|---------------------|
| Balanced (default) | `PitchDetectorConfig.BALANCED` | `.balanced` | 1024 | 0.15 | 0.75 |
| Relaxed | `PitchDetectorConfig.RELAXED` | `.relaxed` | 1024 | 0.20 | 0.65 |
| Precise (offline only) | `PitchDetectorConfig.PRECISE` | `.precise` | 4096 | 0.10 | 0.85 |

`PRECISE` uses a 4096-sample buffer — too expensive per frame for realtime use (per ADR-020). Use it only for offline batch analysis.

### Properties

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `algorithm` | `PitchAlgorithm` | `YIN` | `YIN` or `SWIFT_F0` |
| `bufferSize` | `Int` | `1024` | Audio buffer size (YIN-specific) |
| `hopSize` | `Int` | `160` | Hop size between frames in samples |
| `tolerance` | `Float` | `0.15` | YIN tolerance (lower = more accurate) |
| `minFreq` | `Float` | `80` | Minimum detectable frequency (Hz) |
| `maxFreq` | `Float` | `1000` | Maximum detectable frequency (Hz) |
| `amplitudeGateDb` | `Float` | `-40` | RMS gate threshold (dB); below = unvoiced |
| `confidenceThreshold` | `Float` | `0.75` | Min confidence to accept pitch (0.0–1.0) |
| `enableSmoothing` | `Boolean` | `false` | Inline smoothing filter |
| `enableOctaveCorrection` | `Boolean` | `false` | Inline octave correction |
| `smoothingWindowSize` | `Int` | `5` | Smoothing window (must be odd) |
| `octaveThresholdCents` | `Float` | `150` | Snap-back threshold for octave correction |
| `swiftF0BatchSize` | `Int` | `2560` | SwiftF0 streaming buffer size |

### Builder

```kotlin
val config = PitchDetectorConfig.Builder()
    .preset(PitchDetectorConfig.BALANCED)
    .algorithm(PitchAlgorithm.SWIFT_F0)
    .voiceType(VoiceType.carnaticMale)
    .quietHandling(QuietHandling.SENSITIVE)
    .strictness(DetectionStrictness.LENIENT)
    .enableProcessing()              // smoothing + octave correction
    .bufferSize(1024)
    .hopSize(160)
    .tolerance(0.15f)
    .swiftF0BatchSize(2560)
    .build()
```

## VoiceType

Sealed class. Both the object form (`VoiceType.WesternSoprano`) and a lowercase companion getter (`VoiceType.westernSoprano`) work in Kotlin; Swift uses the lowercase form (`.westernSoprano`).

| VoiceType | Range (Hz) |
|-----------|-----------|
| `Auto` | 65 – 1500 |
| `WesternSoprano` | 200 – 1500 |
| `WesternAlto` | 130 – 1000 |
| `WesternTenor` | 100 – 700 |
| `WesternBass` | 65 – 450 |
| `WesternChild` | 180 – 1500 |
| `CarnaticMale` | 75 – 600 |
| `CarnaticFemale` | 120 – 1100 |
| `CarnaticChild` | 180 – 1300 |
| `HindustaniMale` | 75 – 600 |
| `HindustaniFemale` | 120 – 1100 |
| `HindustaniChild` | 180 – 1300 |
| `PopMale` | 75 – 600 |
| `PopFemale` | 120 – 1100 |
| `PopChild` | 180 – 1300 |
| `IndianFilmMale` | 75 – 600 |
| `IndianFilmFemale` | 120 – 1100 |
| `IndianFilmChild` | 180 – 1300 |

## QuietHandling

Maps to `amplitudeGateDb`. Frames below the gate are returned as unvoiced.

| Level | Gate (dB) | Use for |
|-------|-----------|---------|
| `SENSITIVE` | -50 | Quiet rooms, soft singing |
| `NORMAL` (default) | -40 | Typical environments |
| `NOISY` | -30 | Loud environments |

## DetectionStrictness

Maps to `confidenceThreshold`.

| Level | Threshold | Use for |
|-------|-----------|---------|
| `STRICT` | 0.85 | Fewer false positives |
| `BALANCED` (default) | 0.75 | Balanced |
| `LENIENT` | 0.65 | Catches more notes |

## ContourExtractorConfig

Used by `createContourExtractor`.

### Presets

| Preset | Kotlin | Swift | preset | cleanup |
|--------|--------|-------|--------|---------|
| Default | `ContourExtractorConfig.DEFAULT` | `.default` | `BALANCED` | `SCORING` |
| Scoring | `ContourExtractorConfig.SCORING` | `.scoring` | `PRECISE` | `SCORING` |
| Display | `ContourExtractorConfig.DISPLAY` | `.display` | `BALANCED` | `DISPLAY` |
| Raw | `ContourExtractorConfig.RAW` | `.raw` | `BALANCED` | `RAW` |

### Properties

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `preset` | `PitchPreset` | `BALANCED` | Resolution / accuracy trade-off |
| `algorithm` | `PitchAlgorithm` | `SWIFT_F0` | YIN or SwiftF0 |
| `sampleRate` | `Int` | `16000` | Input audio sample rate (Hz) |
| `hopMs` | `Int` | `10` | Hop between pitch samples (ms) |
| `cleanup` | `PitchProcessingConfig` | `SCORING` | Post-processing applied after extraction |
| `voiceType` | `VoiceType` | `Auto` | Frequency range optimization |
| `quietHandling` | `QuietHandling` | `NORMAL` | Amplitude gate level |
| `strictness` | `DetectionStrictness` | `BALANCED` | Confidence threshold |

### Builder

```kotlin
val config = ContourExtractorConfig.Builder()
    .preset(ContourExtractorConfig.SCORING)
    .pitchPreset(PitchPreset.PRECISE)
    .algorithm(PitchAlgorithm.SWIFT_F0)
    .sampleRate(16000)
    .hopMs(10)
    .cleanup(PitchProcessingConfig.SCORING)
    .voiceType(VoiceType.carnaticMale)
    .quietHandling(QuietHandling.SENSITIVE)
    .strictness(DetectionStrictness.BALANCED)
    .build()
```

## PitchDetector

`abstract class PitchDetector : AutoCloseable`. Construct via `PitchDetection.createDetector(...)`.

### Methods

| Method | Description |
|--------|-------------|
| `detect(samples, sampleRate)` | Single-shot detection. Returns latest `PitchPoint`. Does NOT write to `pitchContour`. |
| `feedContour(samples, sampleRate, anchorTime)` | Stream audio into `pitchContour` / `livePitch`. Each emission's timestamp is back-spread from `anchorTime` by the detector's hop. |
| `pitchAt(timeSeconds)` | Closest contour point to `timeSeconds`. Returns `null` if contour is empty. |
| `getAmplitude(samples, sampleRate)` | RMS of the input. Resamples to 16 kHz internally. |
| `clearPitchContour()` | Wipe the entire contour. |
| `clearPitchContourFrom(timeSeconds)` | Drop points at-or-after `timeSeconds`; keep earlier ones. Used for segment-aware retry / seek-back. |
| `reset()` | Reset internal state and audio buffer. |
| `release()` / `close()` | Release native resources. |
| `duplicate()` | New detector with the same config and an independent contour. |

### Properties

| Property | Type | Description |
|----------|------|-------------|
| `config` | `PitchDetectorConfig` | Configuration used to create this detector |
| `latencyMs` | `Float` | Detection latency in milliseconds |
| `hasProcessing` | `Boolean` | Whether post-processing is available |
| `processingEnabled` | `Boolean` (var) | Toggle smoothing / octave correction at runtime |
| `pitchContour` | `PitchContourRecorder` | Lossless append-only session contour; read whole via `snapshot()` or windowed via `recent(seconds)` |
| `livePitch` | `SharedFlow<PitchPoint>` | Per-emission pitch stream — same source/rate as `pitchContour`, event-shaped |

Both `pitchContour` and `livePitch` are filled by `feedContour` in lock-step. Read `pitchContour.recent(seconds)` once per render frame for scrolling trails (the caller chooses the span at read time), or `pitchContour.snapshot()` for the whole session; use the SharedFlow for live tuners or telemetry. `PitchContourRecorder` lives in `com.musicmuni.voxatrace.common.streaming` and is read-only to the caller; also exposes `pitchAt(timeSeconds)`, `size`, and `durationSeconds`.

## PitchContourExtractor

Construct via `PitchDetection.createContourExtractor(...)`. Holds a native ONNX session (when SwiftF0). Call `release()` when done.

| Method | Description |
|--------|-------------|
| `extract(samples, sampleRate)` | Run the configured pipeline; returns `PitchContour` |
| `release()` | Free native resources |

## PitchPoint

```kotlin
data class PitchPoint(
    val pitch: Float,        // Hz, or -1f if unvoiced
    val confidence: Float,   // 0.0 – 1.0
    val timeSeconds: Float = 0f
)
```

| Computed property | Type | Description |
|-------------------|------|-------------|
| `isSinging` | `Boolean` | `pitch > 0` |
| `midiNote` | `Int` | MIDI number, or -1 if unvoiced |
| `note` | `String?` | e.g., `"A4"`, `"C#5"`; null if unvoiced |
| `centsOff` | `Int` | -50…+50 cents from nearest 12-TET note |
| `tuning` | `PitchPoint.Tuning` | `SILENT`, `FLAT`, `IN_TUNE`, or `SHARP` (±10 c thresholds) |

## PitchContour

```kotlin
data class PitchContour(
    val samples: List<PitchPoint>,
    val sampleRate: Int = 16000,
    val hopSize: Int = 0
)
```

| Property | Type | Description |
|----------|------|-------------|
| `duration` | `Float` | Seconds (timestamp of the last sample) |
| `voicedRatio` | `Float` | Ratio of voiced to total samples |
| `size` | `Int` | Number of samples |
| `isEmpty` | `Boolean` | True if no samples |
| `times` | `FloatArray` | Timestamps |
| `pitchesHz` | `FloatArray` | Pitch values in Hz (-1 = unvoiced) |
| `pitchesMidi` | `FloatArray` | Pitch values in MIDI (-1 = unvoiced) |

| Method / factory | Description |
|------------------|-------------|
| `slice(startTime, endTime, relativeTimes = true)` | Time-range slice |
| `toTimesArray()` / `toPitchesArray()` | Parallel arrays for native-side APIs |
| `PitchContour.fromArrays(times, pitches, …)` | Build from parallel arrays |
| `PitchContour.fromPoints(points, …)` | Build from a list |
| `PitchContour.fromPitchData(data, …)` | Build from parsed `PitchData` (see `SonixParser`) |
| `PitchContour.EMPTY` | Empty constant |

## Common Pitfalls

1. **`PRECISE` is not for realtime.** 4096-sample buffer breaks the 40 ms per-buffer budget. Use `BALANCED` or `RELAXED` for realtime; reserve `PRECISE` for offline (per ADR-020).
2. **SwiftF0 needs a model.** Either register globally (`AIModelRegistry.registerSwiftF0 { … }`) or pass `modelProvider` explicitly. `createDetector`/`createContourExtractor` throw `IllegalArgumentException` otherwise.
3. **Mono input only.** `SonixDecoder.decode()` averages stereo channels automatically (per ADR-017). Custom audio paths must convert to mono.
4. **`detect()` does not write the contour.** Use `feedContour(samples, sampleRate, anchorTime)` if you want `pitchContour` populated.
5. **Always `release()` / `close()`.** Detectors and extractors hold native resources.

## See also

- [PitchProcessing](./pitch-processing) — clean an existing contour
- [PitchAnalysis](./pitch-analysis) — histogram, quantization, transcription
- [Calibra Live Evaluation](../calibra/live-eval) — consumes `PitchDetector`
- [Calibra Melody Eval](../calibra/melody-eval) — consumes `PitchContourExtractor`
