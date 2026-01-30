//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[VocalRangeSessionConfig](../index.md)/[Companion](index.md)

# Companion

[common]\
object [Companion](index.md)

## Properties

| Name | Summary |
|---|---|
| [DEFAULT](-d-e-f-a-u-l-t.md) | [common]<br/>val [DEFAULT](-d-e-f-a-u-l-t.md): [VocalRangeSessionConfig](../index.md)<br/>Default session configuration |
| [MANUAL_FLOW](-m-a-n-u-a-l_-f-l-o-w.md) | [common]<br/>val [MANUAL_FLOW](-m-a-n-u-a-l_-f-l-o-w.md): [VocalRangeSessionConfig](../index.md)<br/>Configuration for manual phase control (autoFlow = false) |

## Functions

| Name | Summary |
|---|---|
| [custom](custom.md) | [common]<br/>fun [custom](custom.md)(countdownSeconds: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 3, maxDetectionTimeSeconds: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 10, minNoteDurationSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f, minConfidence: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.5f, transitionDelayMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) = 500, autoFlow: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true): [VocalRangeSessionConfig](../index.md)<br/>Create a custom configuration with specific parameters. |
