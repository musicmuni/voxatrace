---
sidebar_label: "VADResult"
---


# VADResult

[common]\
data class [VADResult](index.md)(val ratio: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val level: [VoiceActivityLevel](../-voice-activity-level/index.md))

Result from VAD analysis containing ratio, level, and convenience properties.

## Constructors

| | |
|---|---|
| [VADResult](-v-a-d-result.md) | [common]<br/>constructor(ratio: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), level: [VoiceActivityLevel](../-voice-activity-level/index.md)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [isFullActivity](is-full-activity.md) | [common]<br/>val [isFullActivity](is-full-activity.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Convenience: true if level is FULL |
| [isVoiceDetected](is-voice-detected.md) | [common]<br/>val [isVoiceDetected](is-voice-detected.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Convenience: true if ratio 0.5 |
| [level](level.md) | [common]<br/>val [level](level.md): [VoiceActivityLevel](../-voice-activity-level/index.md)<br/>Classified activity level |
| [ratio](ratio.md) | [common]<br/>val [ratio](ratio.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Voice activity ratio (0.0 to 1.0) |
