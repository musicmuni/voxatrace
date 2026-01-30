//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[VocalRangeSessionConfig](index.md)

# VocalRangeSessionConfig

[common]\
data class [VocalRangeSessionConfig](index.md)(val countdownSeconds: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 3, val maxDetectionTimeSeconds: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 10, val minNoteDurationSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f, val minConfidence: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.5f, val transitionDelayMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) = 500, val autoFlow: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true)

Configuration for VocalRangeSession.

## Constructors

| | |
|---|---|
| [VocalRangeSessionConfig](-vocal-range-session-config.md) | [common]<br/>constructor(countdownSeconds: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 3, maxDetectionTimeSeconds: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 10, minNoteDurationSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f, minConfidence: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.5f, transitionDelayMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) = 500, autoFlow: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [autoFlow](auto-flow.md) | [common]<br/>val [autoFlow](auto-flow.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true<br/>Whether to run automatic flow (countdown -> low -> high -> complete) |
| [countdownSeconds](countdown-seconds.md) | [common]<br/>val [countdownSeconds](countdown-seconds.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 3<br/>Countdown duration before detection starts |
| [maxDetectionTimeSeconds](max-detection-time-seconds.md) | [common]<br/>val [maxDetectionTimeSeconds](max-detection-time-seconds.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 10<br/>Maximum time to wait for stable note detection |
| [minConfidence](min-confidence.md) | [common]<br/>val [minConfidence](min-confidence.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.5f<br/>Minimum confidence threshold for pitch detection |
| [minNoteDurationSeconds](min-note-duration-seconds.md) | [common]<br/>val [minNoteDurationSeconds](min-note-duration-seconds.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f<br/>Minimum duration for a note to be considered stable |
| [transitionDelayMs](transition-delay-ms.md) | [common]<br/>val [transitionDelayMs](transition-delay-ms.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) = 500<br/>Delay between low and high note detection |
