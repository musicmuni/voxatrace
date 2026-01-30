//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraPitch](../index.md)/[Companion](index.md)/[createDetector](create-detector.md)

# createDetector

[common]\
fun [createDetector](create-detector.md)(config: [PitchDetectorConfig](../../../com.musicmuni.voxatrace.calibra.model/-pitch-detector-config/index.md) = PitchDetectorConfig.BALANCED, modelProvider: () -&gt; [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)? = null): [CalibraPitch.Detector](../-detector/index.md)

Create a realtime pitch detector.

## Usage

```kotlin
// Tier 1: Defaults (YIN algorithm)
val detector = CalibraPitch.createDetector()

// Tier 2: With config from Builder
val config = PitchDetectorConfig.Builder()
    .algorithm(PitchAlgorithm.SWIFT_F0)
    .voiceType(VoiceType.carnaticMale)
    .build()
val detector = CalibraPitch.createDetector(config, modelProvider = { ... })

// Tier 3: With .copy()
val detector = CalibraPitch.createDetector(
    PitchDetectorConfig.PRECISE.copy(tolerance = 0.08f)
)
```

#### Parameters

common

| | |
|---|---|
| config | Detection configuration (default: BALANCED) |
| modelProvider | Function to load ONNX model bytes (required for SWIFT_F0).     Example: `{ ModelLoader.loadSwiftF0() }` after adding ai-models dependency. |
