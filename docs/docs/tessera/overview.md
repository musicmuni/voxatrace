---
sidebar_position: 1
---

# Tessera Overview

Tessera is the voice-metrics module. It analyzes a `PitchContour` (from [`PitchDetection`](../tona/pitch-detection)) and produces structured measurements of the singer's voice.

| Facade | Purpose |
|--------|---------|
| [`Tessera`](#tesseraanalyze-batch) | Multi-metric batch — breath + agility + range from one contour |
| [`TesseraSession`](./session) | Streaming multi-metric — feed audio incrementally |
| [`TesseraBreath`](./breath) | Breath capacity, control, comparison |
| [`TesseraAgility`](./agility) | Vocal agility (ornament speed and regularity) |
| [`TesseraRange`](./range) | Vocal range, search vector, song matching |
| [`TesseraRangeSession`](./range#tesserarangesession) | Guided "find your range" flow with observable state |
| [`TesseraSpeakingPitch`](./speaking-pitch) | Natural speaking pitch detection |

All facades operate on a `tona.model.PitchContour`. Get one via `PitchDetection.createContourExtractor(...).extract(...)` or accumulate it via `PitchDetection.createDetector(...).feedContour(...)`.

## When to Use

| Scenario | Facade |
|----------|--------|
| Profile a singer from one recording (one call) | [`Tessera.analyze`](#tesseraanalyze-batch) |
| Stream metrics live | [`TesseraSession`](./session) |
| Score breath capacity / control | [`TesseraBreath`](./breath) |
| Score vocal agility | [`TesseraAgility`](./agility) |
| Find a singer's lower / upper note (batch) | [`TesseraRange`](./range) |
| Interactive "find your range" flow | [`TesseraRangeSession`](./range#tesserarangesession) |
| Natural speaking pitch from speech | [`TesseraSpeakingPitch`](./speaking-pitch) |
| Voice/song matching by pitch distribution | [`TesseraRange.computeMatch`](./range#methods-batch) |

## Tessera.analyze (batch)

```kotlin
val result = Tessera.analyze(contour)

println("Breath control: ${result.breath?.controlScore}")
println("Agility: ${result.agility?.scores?.firstOrNull()}")
println("Range: ${result.vocalRange?.range?.octaves} octaves")

// Specific metrics only
val breathOnly = Tessera.analyze(contour, setOf(TesseraMetric.BREATH))
```

### Signature

```kotlin
fun analyze(
    contour: PitchContour,
    metrics: Set<TesseraMetric> = TesseraMetric.ALL,
    breathConfig: BreathConfig = BreathConfig.DEFAULT,
    agilityConfig: AgilityConfig = AgilityConfig.DEFAULT,
    rangeConfig: SearchVectorConfig = SearchVectorConfig.DEFAULT,
): TesseraResult
```

`TesseraMetric.ALL` = `{ BREATH, AGILITY, VOCAL_RANGE }`.

`TesseraResult` carries one nullable field per metric — `null` means the metric was not requested. (For range, `null` may also mean insufficient voiced data.)

```kotlin
data class TesseraResult(
    val breath: BreathScore?,
    val agility: AgilityScore?,
    val vocalRange: VocalRangeResult?,
)
```

## Failure Semantics (ADR-022)

| Kind | How it surfaces |
|------|-----------------|
| Empty contour or `< 2` samples | Throws `IllegalArgumentException` |
| Insufficient data for range estimation (valid input) | `vocalRange` is null in the result |

Per-facade pages document additional throws/null-returns.

## See also

- [Tessera Session](./session) — streaming counterpart
- [Tona / PitchDetection](../tona/pitch-detection) — produces the contour
- [Calibra Live Eval](../calibra/live-eval) — for singing scoring against a reference (different domain)
