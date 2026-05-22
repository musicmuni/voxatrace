---
sidebar_position: 1
---

# Accura Overview

Accura is the intonation module. Given a `PitchContour` (from [`PitchDetection`](../tona/pitch-detection)) and a tonic, it grades how accurately the singer hit the target intervals of a tuning system, returning per-note deviations and an overall 0–100 score.

| Facade | Purpose |
|--------|---------|
| [`Accura.analyzePitching`](./intonation#analyzepitching) | Per-note deviation analysis against EQ or JI |
| [`Accura.calculateScore`](./intonation#calculatescore) | 0–100 score from an analysis result |

Accura wraps the lower-level [`PitchAnalysis`](../tona/pitch-analysis). Use Accura when you want a tuning-system-aware grade; use `PitchAnalysis` when you just want a raw histogram or transcription.

## Quick Start

```kotlin
val extractor = PitchDetection.createContourExtractor(ContourExtractorConfig.SCORING)
val contour = extractor.extract(audioSamples, sampleRate = 16000)
extractor.release()

val result = Accura.analyzePitching(
    contour,
    tonicHz = 196f,                          // G3
    intonationSystem = IntonationSystem.EQ,
    scaleIntervals = listOf(                  // optional: explicit scale targets
        TargetInterval(0f, "S"),
        TargetInterval(200f, "R2"),
        TargetInterval(400f, "G3"),
        TargetInterval(700f, "P"),
        TargetInterval(900f, "D2"),
    ),
)

if (result.error == null) {
    val score = Accura.calculateScore(result)
    println("Score: ${score.score}/100 (${score.tier}) over ${score.noteCount} notes")
}
```

## Failure semantics (ADR-022)

| Kind | How it surfaces | Caller action |
|------|-----------------|---------------|
| Caller bug (empty contour, `tonicHz <= 0`, empty `scaleIntervals`) | `IllegalArgumentException` | Fix the call site |
| Domain-level inconclusive (valid input, but &lt; 3 histogram peaks, no target intervals match) | Non-null `IntonationAnalysisResult` with `error != null` and empty `notes` | Check `result.error` and degrade gracefully |

`calculateScore` requires `result.error == null` and a non-empty `notes` list — pass an inconclusive result and it throws.

## See also

- [Intonation Analysis](./intonation)
- [Tona / PitchAnalysis](../tona/pitch-analysis) — lower-level histogram + transcription
- [MusicTheory](../common/music-theory) — interval generators and conversions used internally
