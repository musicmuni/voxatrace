//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../index.md)/[PitchDetectorConfig](index.md)

# PitchDetectorConfig

[common]\
data class [PitchDetectorConfig](index.md)(val algorithm: [PitchAlgorithm](../-pitch-algorithm/index.md) = PitchAlgorithm.YIN, val bufferSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 2048, val hopSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 160, val tolerance: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.15f, val minFreq: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 80.0f, val maxFreq: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1000.0f, val amplitudeGateDb: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = -40f, val confidenceThreshold: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.75f, val enableSmoothing: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, val enableOctaveCorrection: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, val smoothingWindowSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 5, val octaveThresholdCents: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 150.0f, val swiftF0BatchSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 2560)

Configuration for CalibraPitch detector.

Unified configuration for pitch detection including algorithm selection, detection parameters, and post-processing options.

## Usage

```kotlin
// Tier 1: Use presets
val detector = CalibraPitch.createDetector(PitchDetectorConfig.BALANCED)

// Tier 2: Use Builder for discoverability
val config = PitchDetectorConfig.Builder()
    .algorithm(PitchAlgorithm.SWIFT_F0)
    .voiceType(VoiceType.carnaticMale)
    .quietHandling(QuietHandling.SENSITIVE)
    .enableProcessing()
    .build()

// Tier 3: Use .copy() for raw parameter access
val config = PitchDetectorConfig.PRECISE.copy(tolerance = 0.08f)
```

## Constructors

| | |
|---|---|
| [PitchDetectorConfig](-pitch-detector-config.md) | [common]<br/>constructor(algorithm: [PitchAlgorithm](../-pitch-algorithm/index.md) = PitchAlgorithm.YIN, bufferSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 2048, hopSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 160, tolerance: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.15f, minFreq: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 80.0f, maxFreq: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1000.0f, amplitudeGateDb: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = -40f, confidenceThreshold: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.75f, enableSmoothing: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, enableOctaveCorrection: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, smoothingWindowSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 5, octaveThresholdCents: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 150.0f, swiftF0BatchSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 2560) |

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [common]<br/>class [Builder](-builder/index.md)<br/>Builder for PitchDetectorConfig. |
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [algorithm](algorithm.md) | [common]<br/>val [algorithm](algorithm.md): [PitchAlgorithm](../-pitch-algorithm/index.md)<br/>Pitch detection algorithm (YIN or SWIFT_F0) |
| [amplitudeGateDb](amplitude-gate-db.md) | [common]<br/>val [amplitudeGateDb](amplitude-gate-db.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>RMS amplitude threshold in dB for gating quiet frames |
| [bufferSize](buffer-size.md) | [common]<br/>val [bufferSize](buffer-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 2048<br/>Size of the audio buffer for analysis (YIN-specific) |
| [confidenceThreshold](confidence-threshold.md) | [common]<br/>val [confidenceThreshold](confidence-threshold.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.75f<br/>Minimum confidence (0.0-1.0) to accept pitch |
| [enableOctaveCorrection](enable-octave-correction.md) | [common]<br/>val [enableOctaveCorrection](enable-octave-correction.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false<br/>Enable octave error correction |
| [enableSmoothing](enable-smoothing.md) | [common]<br/>val [enableSmoothing](enable-smoothing.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false<br/>Enable pitch smoothing filter |
| [hopSize](hop-size.md) | [common]<br/>val [hopSize](hop-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 160<br/>Hop size between frames in samples |
| [maxFreq](max-freq.md) | [common]<br/>val [maxFreq](max-freq.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1000.0f<br/>Maximum detectable frequency in Hz |
| [minFreq](min-freq.md) | [common]<br/>val [minFreq](min-freq.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 80.0f<br/>Minimum detectable frequency in Hz |
| [octaveThresholdCents](octave-threshold-cents.md) | [common]<br/>val [octaveThresholdCents](octave-threshold-cents.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 150.0f<br/>How close to 1200 cents a jump must be to be corrected |
| [smoothingWindowSize](smoothing-window-size.md) | [common]<br/>val [smoothingWindowSize](smoothing-window-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 5<br/>Smoothing filter window size (must be odd) |
| [swiftF0BatchSize](swift-f0-batch-size.md) | [common]<br/>val [swiftF0BatchSize](swift-f0-batch-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 2560<br/>Buffer size for SwiftF0 streaming detection in samples |
| [tolerance](tolerance.md) | [common]<br/>val [tolerance](tolerance.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.15f<br/>YIN algorithm tolerance (lower = more accurate, slower) |
