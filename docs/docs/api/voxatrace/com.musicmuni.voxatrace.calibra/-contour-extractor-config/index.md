//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[ContourExtractorConfig](index.md)

# ContourExtractorConfig

data class [ContourExtractorConfig](index.md)(val preset: [PitchPreset](../../com.musicmuni.voxatrace.calibra.model/-pitch-preset/index.md) = PitchPreset.BALANCED, val algorithm: [PitchAlgorithm](../../com.musicmuni.voxatrace.calibra.model/-pitch-algorithm/index.md) = PitchAlgorithm.SWIFT_F0, val sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000, val hopMs: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 10, val cleanup: [ContourCleanup](../../com.musicmuni.voxatrace.calibra.model/-contour-cleanup/index.md) = ContourCleanup.SCORING, val voiceType: [VoiceType](../../com.musicmuni.voxatrace.calibra.model/-voice-type/index.md) = VoiceType.Auto, val quietHandling: [QuietHandling](../../com.musicmuni.voxatrace.calibra.model/-quiet-handling/index.md) = QuietHandling.NORMAL, val strictness: [DetectionStrictness](../../com.musicmuni.voxatrace.calibra.model/-detection-strictness/index.md) = DetectionStrictness.BALANCED)

Configuration for CalibraPitch ContourExtractor.

ADR-001 compliant: Builder builds Config, Factory takes Config.

## Usage Tiers

### Tier 1: Presets (80% of users)

#### Kotlin

```kotlin
val extractor = CalibraPitch.createContourExtractor(
    ContourExtractorConfig.SCORING,
    modelProvider = { ModelLoader.loadSwiftF0() }
)
```

#### Swift

```swift
let extractor = CalibraPitch.companion.createContourExtractor(
    config: ContourExtractorConfig.companion.SCORING,
    modelProvider: { ModelLoader.shared.loadSwiftF0() }
)
```

### Tier 2: Builder (15% of users)

#### Kotlin

```kotlin
val config = ContourExtractorConfig.Builder()
    .preset(ContourExtractorConfig.SCORING)
    .sampleRate(16000)
    .hopMs(10)
    .build()
val extractor = CalibraPitch.createContourExtractor(config, modelProvider = { ... })
```

#### Swift

```swift
let config = ContourExtractorConfig.Builder()
    .preset(config: ContourExtractorConfig.companion.SCORING)
    .sampleRate(rate: 16000)
    .hopMs(hop: 10)
    .build()
let extractor = CalibraPitch.companion.createContourExtractor(config: config, modelProvider: { ... })
```

### Tier 3: .copy() (5% of users)

#### Kotlin

```kotlin
val config = ContourExtractorConfig.SCORING.copy(hopMs = 5)
```

#### See also

| | |
|---|---|
| [CalibraPitch.Companion.createContourExtractor](../-calibra-pitch/-companion/create-contour-extractor.md) | Factory method to create extractors |
| [ContourCleanup](../../com.musicmuni.voxatrace.calibra.model/-contour-cleanup/index.md) | Post-processing options for pitch contours |

## Constructors

| | |
|---|---|
| [ContourExtractorConfig](-contour-extractor-config.md) | [common]<br/>constructor(preset: [PitchPreset](../../com.musicmuni.voxatrace.calibra.model/-pitch-preset/index.md) = PitchPreset.BALANCED, algorithm: [PitchAlgorithm](../../com.musicmuni.voxatrace.calibra.model/-pitch-algorithm/index.md) = PitchAlgorithm.SWIFT_F0, sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000, hopMs: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 10, cleanup: [ContourCleanup](../../com.musicmuni.voxatrace.calibra.model/-contour-cleanup/index.md) = ContourCleanup.SCORING, voiceType: [VoiceType](../../com.musicmuni.voxatrace.calibra.model/-voice-type/index.md) = VoiceType.Auto, quietHandling: [QuietHandling](../../com.musicmuni.voxatrace.calibra.model/-quiet-handling/index.md) = QuietHandling.NORMAL, strictness: [DetectionStrictness](../../com.musicmuni.voxatrace.calibra.model/-detection-strictness/index.md) = DetectionStrictness.BALANCED) |

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [common]<br/>class [Builder](-builder/index.md)<br/>Builder for ContourExtractorConfig. |
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [algorithm](algorithm.md) | [common]<br/>val [algorithm](algorithm.md): [PitchAlgorithm](../../com.musicmuni.voxatrace.calibra.model/-pitch-algorithm/index.md)<br/>Pitch detection algorithm |
| [cleanup](cleanup.md) | [common]<br/>val [cleanup](cleanup.md): [ContourCleanup](../../com.musicmuni.voxatrace.calibra.model/-contour-cleanup/index.md)<br/>Post-processing cleanup options |
| [hopMs](hop-ms.md) | [common]<br/>val [hopMs](hop-ms.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 10<br/>Hop size between pitch samples in milliseconds |
| [preset](preset.md) | [common]<br/>val [preset](preset.md): [PitchPreset](../../com.musicmuni.voxatrace.calibra.model/-pitch-preset/index.md)<br/>Detection preset for resolution/accuracy trade-off |
| [quietHandling](quiet-handling.md) | [common]<br/>val [quietHandling](quiet-handling.md): [QuietHandling](../../com.musicmuni.voxatrace.calibra.model/-quiet-handling/index.md)<br/>How to handle quiet audio |
| [sampleRate](sample-rate.md) | [common]<br/>val [sampleRate](sample-rate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000<br/>Input audio sample rate in Hz |
| [strictness](strictness.md) | [common]<br/>val [strictness](strictness.md): [DetectionStrictness](../../com.musicmuni.voxatrace.calibra.model/-detection-strictness/index.md)<br/>Detection strictness level |
| [voiceType](voice-type.md) | [common]<br/>val [voiceType](voice-type.md): [VoiceType](../../com.musicmuni.voxatrace.calibra.model/-voice-type/index.md)<br/>Voice type for frequency range optimization |
