---
sidebar_position: 1
---

# Tona Overview

Tona is the pitch module of VoxaTrace. It owns three responsibilities, each with its own facade:

| Facade | Purpose |
|--------|---------|
| [`PitchDetection`](./pitch-detection) | Realtime detection (per frame) and batch contour extraction (whole recording) |
| [`PitchProcessing`](./pitch-processing) | Cleanup of an existing contour — octave correction, smoothing, blip removal |
| [`PitchAnalysis`](./pitch-analysis) | Histograms, tuning estimation, quantization, melodic transcription |

Tona produces and consumes a single shared data type — `PitchContour` — which downstream modules ([Tessera](../tessera/overview), [Accura](../accura/overview), [Calibra](../calibra/overview)) read.

## Pipeline

```text
audio samples ─► PitchDetection ──► PitchContour ──► PitchProcessing ──► cleaned PitchContour
                                       │                                       │
                                       └────────────────────► PitchAnalysis ◄──┘
                                                                  │
                                                                  ▼
                                                  PitchHistogram, TonalSegment[], …
```

- **Live use case**: `PitchDetection.createDetector()` — feed audio buffer-by-buffer: call `detect(samples, sampleRate)` for a `PitchPoint` per frame, and/or `feedContour(samples, sampleRate, anchorTime)` to accumulate the session contour, then read `pitchContour.recent(seconds)` for visualization.
- **Offline use case**: `PitchDetection.createContourExtractor()` — pass a complete recording, get back a full `PitchContour` with cleanup baked in (configurable via `ContourExtractorConfig`).
- **Cleanup an existing contour**: `PitchProcessing.process(contour, PitchProcessingConfig.SCORING)`.
- **Histogram or transcription**: `PitchAnalysis.computeHistogram(contour, tonicHz)`, `PitchAnalysis.quantize(...)`, `PitchAnalysis.labelByMeanPitch(...)`.

## Algorithms

| Algorithm | Best for | Cost | Dependencies |
|-----------|----------|------|--------------|
| `PitchAlgorithm.YIN` | Realtime, pure DSP, no model bundle | Lower per frame | None |
| `PitchAlgorithm.SWIFT_F0` | Higher accuracy on vocals; batch | Needs ONNX model | `swift_f0.onnx` (95k params) |
| `PitchAlgorithm.MELODIA` | Offline / batch reference-contour extraction; octave-robust | Needs the whole signal | None |

`PitchDetectorConfig` defaults to `YIN` (realtime). `ContourExtractorConfig` defaults to `SWIFT_F0` (batch). `MELODIA` is **offline-only**: it tracks the predominant melody across the whole recording, so it avoids the octave-halving plain `YIN` shows on high / strong-harmonic voices, but it needs the full signal. Set it on a `ContourExtractorConfig` (`createContourExtractor`); `createDetector` rejects it (throws `IllegalArgumentException`).

To use SwiftF0 in either path, register a model provider once at startup or pass it explicitly:

```kotlin
AIModelRegistry.registerSwiftF0 { ModelLoader.loadSwiftF0() }
```

## Performance budget

Per ADR-020, the per-buffer processing cost on mid-tier hardware must stay under 40 ms when used inside `CalibraLiveEval`. Defaults are tuned to that budget:
- `PitchDetectorConfig.BALANCED.bufferSize = 1024` (was 2048 before 1.0.0)
- `SessionConfig.hopSize = 320` (was 160 before 1.0.0)
- `PitchDetectorConfig.PRECISE.bufferSize = 4096` — **not for realtime**; use only for offline analysis.

## See also

- [Calibra Live Evaluation](../calibra/live-eval) — consumes a `PitchDetector` for realtime scoring.
- [Calibra Melody Eval](../calibra/melody-eval) — consumes a `PitchContourExtractor` for batch scoring.
- [Accura](../accura/overview) — consumes a `PitchContour` for intonation analysis.
- [Tessera](../tessera/overview) — consumes a `PitchContour` for breath / agility / range metrics.
