---
sidebar_position: 1
---

# Accura Overview

Accura is the intonation module. Given a `PitchContour` (from [`PitchDetection`](../tona/pitch-detection)) and a tonic, it grades how accurately the singer hit the target intervals of a tuning system, returning per-swara deviations and an overall 0–100 score.

| Facade | Purpose |
|--------|---------|
| [`Accura.analyzePitching`](./intonation#analyzepitching) | Per-swara deviation analysis against EQ or JI |
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
    scaleNoteNames = listOf("C", "D", "E", "G", "A"),  // optional pentatonic filter
)

if (result.error == null) {
    val score = Accura.calculateScore(result)
    println("Score: ${score.score}/100 over ${score.swaraCount} swaras")
}
```

## Failure semantics (ADR-022)

| Kind | How it surfaces | Caller action |
|------|-----------------|---------------|
| Caller bug (empty contour, `tonicHz <= 0`, empty `scaleNoteNames`) | `IllegalArgumentException` | Fix the call site |
| Domain-level inconclusive (valid input, but &lt; 3 histogram peaks, no target intervals match) | Non-null `IntonationAnalysisResult` with `error != null` and empty `swaras` | Check `result.error` and degrade gracefully |

`calculateScore` requires `result.error == null` and a non-empty `swaras` list — pass an inconclusive result and it throws.

## See also

- [Intonation Analysis](./intonation)
- [Tona / PitchAnalysis](../tona/pitch-analysis) — lower-level histogram + transcription
- [MusicTheory](../common/music-theory) — interval generators and conversions used internally
