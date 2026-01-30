//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.evaluation.live](../index.md)/[LiveEvaluatorConfig](index.md)

# LiveEvaluatorConfig

[common]\
data class [LiveEvaluatorConfig](index.md)(val sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000, val frameSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1024, val hopSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 160, val pitchTolerance: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.15f, val pitchCorrectToleranceMidi: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.35f)

Configuration for the LiveEvaluator.

This is an immutable config class following ADR-001. Use companion presets or .copy() for customization.

## Constructors

| | |
|---|---|
| [LiveEvaluatorConfig](-live-evaluator-config.md) | [common]<br/>constructor(sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000, frameSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1024, hopSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 160, pitchTolerance: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.15f, pitchCorrectToleranceMidi: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.35f) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [frameSize](frame-size.md) | [common]<br/>val [frameSize](frame-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1024<br/>Frame size for pitch detection |
| [hopSize](hop-size.md) | [common]<br/>val [hopSize](hop-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 160<br/>Hop size between frames |
| [pitchCorrectToleranceMidi](pitch-correct-tolerance-midi.md) | [common]<br/>val [pitchCorrectToleranceMidi](pitch-correct-tolerance-midi.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.35f<br/>Tolerance for pitch correctness (in MIDI semitones) |
| [pitchTolerance](pitch-tolerance.md) | [common]<br/>val [pitchTolerance](pitch-tolerance.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.15f<br/>YIN algorithm tolerance |
| [sampleRate](sample-rate.md) | [common]<br/>val [sampleRate](sample-rate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000<br/>Audio sample rate (must be 16000) |
