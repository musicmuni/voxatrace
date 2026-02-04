---
sidebar_label: "SessionConfig"
---


# SessionConfig

[common]\
data class [SessionConfig](index.md)(val autoAdvance: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true, val scoreThreshold: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, val maxAttempts: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0, val resultAggregation: [ResultAggregation](../-result-aggregation/index.md) = ResultAggregation.LATEST, val hopSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 160, val autoPhaseTransition: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true, val autoSegmentDetection: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true)

Configuration for a CalibraLiveEval session.

Note: Pitch detection is configured separately via [PitchDetectorConfig](../-pitch-detector-config/index.md) and the detector is injected into CalibraLiveEval.create.

For student key transposition, use [com.musicmuni.voxatrace.calibra.CalibraLiveEval.setStudentKeyHz](../../com.musicmuni.voxatrace.calibra/-calibra-live-eval/set-student-key-hz.md) at runtime - this allows key adjustment mid-lesson.

## Usage

```kotlin
// Tier 1: Use presets
val session = CalibraLiveEval.create(reference, SessionConfig.PRACTICE, detector)

// Tier 2: Use Builder
val config = SessionConfig.Builder()
    .preset(SessionConfig.PRACTICE)
    .scoreThreshold(0.6f)
    .build()

// Tier 3: Use .copy()
val config = SessionConfig.PRACTICE.copy(maxAttempts = 5)
```

## Constructors

| | |
|---|---|
| [SessionConfig](-session-config.md) | [common]<br/>constructor(autoAdvance: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true, scoreThreshold: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, maxAttempts: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0, resultAggregation: [ResultAggregation](../-result-aggregation/index.md) = ResultAggregation.LATEST, hopSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 160, autoPhaseTransition: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true, autoSegmentDetection: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true) |

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [common]<br/>class [Builder](-builder/index.md)<br/>Builder for SessionConfig. |
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [autoAdvance](auto-advance.md) | [common]<br/>val [autoAdvance](auto-advance.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true<br/>Automatically advance to next segment when current ends (default true) |
| [autoPhaseTransition](auto-phase-transition.md) | [common]<br/>val [autoPhaseTransition](auto-phase-transition.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true<br/>Automatically transition LISTENING → SINGING in singafter mode (default true) |
| [autoSegmentDetection](auto-segment-detection.md) | [common]<br/>val [autoSegmentDetection](auto-segment-detection.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true<br/>Automatically detect segment end from player time (default true) |
| [hopSize](hop-size.md) | [common]<br/>val [hopSize](hop-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 160<br/>Hop size between frames in samples (default 160 = 10ms at 16kHz) |
| [maxAttempts](max-attempts.md) | [common]<br/>val [maxAttempts](max-attempts.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0<br/>Max attempts before forced advance (0 = unlimited) |
| [resultAggregation](result-aggregation.md) | [common]<br/>val [resultAggregation](result-aggregation.md): [ResultAggregation](../-result-aggregation/index.md)<br/>How to aggregate multiple attempts into final score (default LATEST) |
| [scoreThreshold](score-threshold.md) | [common]<br/>val [scoreThreshold](score-threshold.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f<br/>Min score to auto-advance (0 = disabled, advances regardless of score) |
