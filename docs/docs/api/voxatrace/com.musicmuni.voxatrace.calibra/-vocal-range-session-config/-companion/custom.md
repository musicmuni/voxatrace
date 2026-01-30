//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[VocalRangeSessionConfig](../index.md)/[Companion](index.md)/[custom](custom.md)

# custom

[common]\
fun [custom](custom.md)(countdownSeconds: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 3, maxDetectionTimeSeconds: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 10, minNoteDurationSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f, minConfidence: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.5f, transitionDelayMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) = 500, autoFlow: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true): [VocalRangeSessionConfig](../index.md)

Create a custom configuration with specific parameters.

#### Parameters

common

| | |
|---|---|
| countdownSeconds | Countdown duration before detection starts (default: 3) |
| maxDetectionTimeSeconds | Maximum time to wait for stable note (default: 10) |
| minNoteDurationSeconds | Minimum duration for stable note (default: 1.0) |
| minConfidence | Minimum confidence threshold (default: 0.5) |
| transitionDelayMs | Delay between phases (default: 500) |
| autoFlow | Whether to run automatic flow (default: true) |
