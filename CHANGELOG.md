# Changelog

All notable changes to VoxaTrace will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/),
and this project adheres to [Semantic Versioning](https://semver.org/).

## [Unreleased]

### Changed

- **Realtime pitch-contour API on `PitchDetector` replaced.** The
  bounded, observable `livePitchContour: StateFlow<PitchContour>` (a
  fixed rolling window, 10 s by default) and the `setContourMaxDuration`
  knob are removed in favour of `pitchContour: PitchContourRecorder` —
  a lossless, append-only, unbounded contour store. This is a breaking
  change; 2.0.0 and later consumers must adopt `pitchContour`. There is
  no compatibility shim.
- **`Accura.analyzePitching` no longer reports glided-through notes.**
  A per-svara peak whose integrated histogram area is below 12 % of the
  busiest peak's is treated as a transient glide rather than an intended
  svara and is excluded from `IntonationAnalysisResult.swaras`.
  `PeakDetectionConfig` gains a `minPeakAreaFraction` knob (default `0f`
  = off) exposing this relative-area gate for direct peak-detection
  callers.

### Added

- **`PitchDetector.pitchContour: PitchContourRecorder`.** Records every
  detected point for the lifetime of the detector with no rolling-window
  cap. Read it two ways:
  - `pitchContour.snapshot(): PitchContour` — the whole session so far,
    for end-of-session scoring and analysis.
  - `pitchContour.recent(seconds): PitchContour` — the trailing window,
    for live visualization (replaces the old fixed `livePitchContour`
    window; the caller now chooses the span at read time).

  Also exposes `pitchAt(timeSeconds)`, `size`, and `durationSeconds`.
  `PitchContourRecorder` is a new public type in
  `com.musicmuni.voxatrace.common.streaming` — public reads,
  detector-internal writes.

### Removed

- **`PitchDetector.livePitchContour`** (`StateFlow<PitchContour>`) and
  **`PitchDetector.setContourMaxDuration(seconds)`** are gone. The
  contour is no longer exposed as an observable flow and is no longer
  windowed. Migration for 2.0.0+ consumers:
  - reactive collection of `livePitchContour` for display → poll
    `pitchContour.recent(seconds)` on your render tick.
  - `livePitchContour.value` for whole-session scoring →
    `pitchContour.snapshot()`. This also fixes silent truncation: the
    old window dropped points beyond its cap, so long sessions were
    scored on a partial contour.
  - `setContourMaxDuration(n)` → no replacement; the window span is now
    chosen at read time via `recent(n)`.

## [2.0.0]

Major release. **1.x had two public namespaces — `sonix` and `calibra`. 2.0.0
introduces four new ones: `tona`, `tessera`, `accura`, and `common.MusicTheory`.**
Existing calibra functionality that is fundamentally not about singing
evaluation has been split out into its permanent home (pitch → tona,
voice metrics → tessera, music theory → common); calibra retains only
singing evaluation. The legacy 1.x surface (`CalibraPitch`,
`CalibraBreath`, `CalibraVocalRange`, `VocalRangeSession`,
`CalibraSpeakingPitch`, `CalibraMusic`) is **removed in this release**
— there is no source-compat shell. 1.x consumers must update imports
and call shapes per the migration table below.

The four new namespaces also bring genuinely new public functionality
that did not exist in any form in 1.x (see the **Added** section):
the entire `Accura` facade, the entire `PitchAnalysis` facade,
`TesseraAgility`, the multi-metric `Tessera.analyze` /
`TesseraSession`, the batch-shape `TesseraRange.computeVocalRange` /
`computeSearchVector` / `computeMatch`, and `MusicTheory.deriveUserShruti`.

### Breaking changes

- **Legacy 1.x facades removed.** `CalibraPitch`, `CalibraBreath`,
  `CalibraVocalRange`, `VocalRangeSession`, `CalibraSpeakingPitch`,
  `CalibraMusic`, plus the public types under `calibra.model.*`
  (`PitchPoint`, `PitchContour`, `PitchDetectorConfig`, `PitchAlgorithm`,
  `PitchPreset`, `VoiceType`, `QuietHandling`, `DetectionStrictness`,
  `PitchProcessorConfig`, `ContourCleanup`, `ContourExtractorConfig`,
  `VocalPitch`, `DetectedNote`, `VocalRange`, `VocalRangeConfig`,
  `RangeStats`, `VocalRangePhase`, `VocalRangeState`, `VocalRangeResult`,
  `VocalRangeSessionConfig`) are gone. Migration table and code
  examples below.
- **`PitchPoint.isVoiced` removed.** The 1.x alias for `isSinging` is
  gone. Use `point.isSinging` or `point.pitch > 0`.
- **Singer-side call shapes that changed alongside the renames** —
  even after fixing imports, these functions take or return different
  values than their 1.x predecessors:
  - `CalibraBreath.computeCapacity(times, pitchesHz): Float` (returned
    `-1f` on failure) → `TesseraBreath.analyze(contour: PitchContour).phrases?.longestDuration: Float?`
    (null when phrases can't be segmented). Build the contour via
    `PitchContour.fromArrays(times, pitches)`.
  - `CalibraSpeakingPitch.detectFromAudio(...)` returned `-1` on failure
    → `TesseraSpeakingPitch.detectFromAudio(...)` returns `0` on failure.
  - `CalibraPitch.PostProcess.cleanup(contour, ContourCleanup.SCORING)`
    → `PitchProcessing.process(contour, PitchProcessingConfig.SCORING)`.
    There is no `ContourCleanup` enum — the cleanup field on
    `ContourExtractorConfig` is typed as `PitchProcessingConfig` with
    presets `RAW`, `SCORING`, `DISPLAY`.
  - `CalibraPitch.PostProcess.rejectOutliers(...)` renamed to
    `PitchProcessing.removeBlips(...)`.
  - `CalibraVocalRange` was streaming (`addAudio` + `getRange`) →
    `TesseraRange.computeVocalRange(contour)` (batch) or
    `TesseraRangeSession` (guided streaming with observable state).
  - `CalibraBreath.computeMetrics(...)` 9-array signature has no
    direct equivalent. Use `TesseraBreath.analyze(studentContour, referenceContour)`
    for a single call returning control score, phrase summary, and
    `alignmentScore` together; or `TesseraBreath.compare(refContour,
    studentContour)` for the alignment score alone.

### Added

#### New public facades

- **Pitch (`tona`).** `PitchDetection`, `PitchProcessing`, and
  `PitchAnalysis`. `PitchDetection` produces realtime `PitchDetector`
  instances and batch `PitchContourExtractor` instances; `PitchProcessing`
  exposes the full batch cleanup pipeline (octave correction → blip
  removal → smoothing) plus 14 individual array-level operations
  (`smooth`, `medianFilter`, `iqrFilter`, `dbscanFilter`, `removeBlips`,
  `interpolateSilence`, masking, segmentation, resampling); `PitchAnalysis`
  covers histograms, tuning estimation, quantization, mean-pitch
  labelling, and piecewise linear segment fitting.
- **Voice metrics (`tessera`).** `Tessera.analyze` (multi-metric batch),
  `TesseraSession` (multi-metric streaming, 10-minute cap),
  `TesseraBreath` (capacity / control / reference comparison),
  `TesseraAgility` (10-stage pipeline + 0–1 score),
  `TesseraRange` (batch range + 13-dim search vector + matching) and
  `TesseraRangeSession` (guided phase-driven flow with observable state),
  `TesseraSpeakingPitch` (median-F0 detection from speech).
- **Intonation (`accura`).** New top-level facade — no 1.x equivalent.
  `Accura.analyzePitching` returns per-swara deviation against EQ
  (12-TET) or JI (just-intonation) target intervals with optional
  global tuning-offset alignment; `Accura.calculateScore` produces a
  0–100 score with a piecewise-linear grading scale and a small-sample
  outlier-robust adjustment. Failure contract follows ADR-022 from
  inception: precondition violations (empty contour, `tonicHz <= 0`,
  empty `scaleNoteNames`) throw `IllegalArgumentException`;
  domain-level inconclusive outcomes (e.g. fewer than three histogram
  peaks) surface via the non-null `IntonationAnalysisResult.error`
  field. `calculateScore` requires `result.error == null` and a
  non-empty `result.swaras`.
- **Music theory (`common`).** `MusicTheory` is the canonical home for
  pitch ↔ MIDI ↔ note-label ↔ cents conversions, 12-TET / Just Intonation
  interval generators, the chromatic / Carnatic / Hindustani note-name
  constants, and shruti helpers.

#### New / extended APIs on existing facades

- **`MusicTheory.alignShruti(userShrutiHz, refKeyHz, maxFineTuneSemitones = 2f)`.**
  Computes a student's practice shruti relative to the lesson key in
  pitch-class space and returns `ShrutiAlignmentResult` with
  `practiceShrutiHz`, `shiftSemitones`, and a list of
  `ShrutiOption` values (5 by default) for picker UIs. Each option is
  anchored at the octave nearest to the student's shruti.
- **`MusicTheory.deriveUserShruti(nspHz, rangeLowHz, rangeHighHz, rangeThresholdSemitones)`.**
  Computes the user's practice shruti from their natural speaking pitch
  (NSP) and most-recent vocal range, applying the policy from Musicmuni
  research synthesis (§B7). Returns `UserShrutiDerivation { targetHz, source }`
  where `source ∈ {NSP_NO_RANGE, NSP_NARROW_RANGE, VOCAL_RANGE}`. Below
  18 st of measured range, falls back to NSP; at or above, applies
  `Sa = max(rangeLow + 7, nspMidi − 2)` clipped to
  `[rangeHigh − 17, rangeHigh − 12]`.
- **`PitchDetector.feedContour(samples, sampleRate, anchorTime)` and
  `PitchDetector.pitchAt(timeSeconds)`** are the canonical streaming
  contour APIs. `feedContour` writes into `livePitchContour` /
  `livePitch`, back-spread from the supplied anchor by the detector's
  hop. `pitchAt` returns the closest contour point via binary search.
- **`PitchDetector.clearPitchContourFrom(timeSeconds)`** for
  segment-aware retry/seek-back: drops points at or after `timeSeconds`,
  keeps earlier ones.

### Changed

- **Default frequency-detection range.** `PitchDetectorConfig.BALANCED`
  defaults to 80 Hz – 1000 Hz (was previously documented as 50–2000).
  SwiftF0 model range remains 46.875 Hz – 2093.75 Hz.

### Removed (migration table)

| Removed (1.x) | Replacement (2.0) |
|---------------|-------------------|
| `com.musicmuni.voxatrace.calibra.CalibraPitch` (`createDetector`, `createContourExtractor`) | `com.musicmuni.voxatrace.tona.PitchDetection.createDetector` / `createContourExtractor` |
| `CalibraPitch.PostProcess.*` | `com.musicmuni.voxatrace.tona.PitchProcessing.*` |
| `CalibraPitch.Detector` (nested type) | `com.musicmuni.voxatrace.tona.detection.PitchDetector` (interface) |
| `com.musicmuni.voxatrace.calibra.ContourExtractorConfig` | `com.musicmuni.voxatrace.tona.model.ContourExtractorConfig` |
| `com.musicmuni.voxatrace.calibra.CalibraBreath.*` | `com.musicmuni.voxatrace.tessera.TesseraBreath.*` |
| `com.musicmuni.voxatrace.calibra.CalibraVocalRange` | `com.musicmuni.voxatrace.tessera.TesseraRange.computeVocalRange` (batch) |
| `com.musicmuni.voxatrace.calibra.VocalRangeSession` | `com.musicmuni.voxatrace.tessera.TesseraRangeSession` |
| `com.musicmuni.voxatrace.calibra.CalibraSpeakingPitch.*` | `com.musicmuni.voxatrace.tessera.TesseraSpeakingPitch.*` |
| `com.musicmuni.voxatrace.calibra.CalibraMusic.*` | `com.musicmuni.voxatrace.common.MusicTheory.*` |
| `calibra.model.{PitchPoint, PitchContour, PitchDetectorConfig, …}` | `tona.model.*` (full list under "Breaking changes" above) |
| `calibra.model.{VocalPitch, DetectedNote, VocalRange, VocalRangeConfig, RangeStats, VocalRangePhase, VocalRangeState, VocalRangeResult, VocalRangeSessionConfig}` | `tessera.model.*` |
| `calibra.model.PitchPoint.isVoiced` (alias for `isSinging`) | `point.isSinging` or `point.pitch > 0` (the canonical `PitchPoint` itself moved to `tona.model`) |

Permanent calibra facades are unchanged: `CalibraLiveEval`,
`CalibraMelodyEval`, `CalibraNoteEval`, `CalibraVAD`. The audio-effects
facade (`CalibraEffects` and its config/preset types) is held back in
2.0.0 — kept internal until the public API is finalized. Apps that used
the 1.x effects chain should pin to 1.0.x until the public surface
returns in a later release.

### Migration

```kotlin
// Before
val detector = CalibraPitch.createDetector(PitchDetectorConfig.BALANCED)
val cleaned = CalibraPitch.PostProcess.cleanup(contour, ContourCleanup.SCORING)
val capacity = CalibraBreath.computeCapacity(times, pitchesHz)
val midi = CalibraMusic.hzToMidi(440f)

// After
val detector = PitchDetection.createDetector(PitchDetectorConfig.BALANCED)
val cleaned = PitchProcessing.process(contour, PitchProcessingConfig.SCORING)
val metrics = TesseraBreath.analyze(PitchContour.fromArrays(times, pitchesHz))
val capacity = metrics.phrases?.longestDuration   // null if phrases can't be segmented
val midi = MusicTheory.hzToMidi(440f)
```

There is no `ContourCleanup` enum — the cleanup field on
`ContourExtractorConfig` is typed as `PitchProcessingConfig` with presets
`RAW`, `SCORING`, `DISPLAY`.

`Accura` failure semantics (ADR-022): always inspect `result.error`
before reading `result.swaras` or passing the result to
`Accura.calculateScore`. Empty inputs throw rather than returning a
wrapped failure.

## [1.0.1] - 2026-04-25

Patch release. Audio timing fixes (mostly Android) and one MediaMuxer
crash fix. All changes are backward-compatible with 1.0.0.

### Fixed

- **Mic↔player audible-time alignment.** Pitch contour timestamps now
  reflect the player's audible time corresponding to the wall moment
  each audio buffer was captured at the mic, derived from the OS audio
  engine's hardware clock at both ends (`AudioTrack.getTimestamp` on
  Android, `AVAudioPlayerNode.lastRenderTime` + `playerTimeForNodeTime`
  on iOS). Replaces fragile player-currentTime polling and sample-counter
  extrapolation that could drift on pause / seek / tempo-shift transitions.
- **Android: `nowMonotonicNanos` clock domain.** Now uses
  `System.nanoTime()` (CLOCK_MONOTONIC) instead of
  `SystemClock.elapsedRealtimeNanos` (CLOCK_BOOTTIME). Subtracting the
  former from `AudioTimestamp.nanoTime` was producing days of apparent
  lag on devices with accumulated deep-sleep time.
- **Android: `AudioRecord` buffer vs. read-chunk size.** Buffer
  allocation and per-`read()` chunk size are now decoupled. Previously,
  a device whose `AudioRecord.getMinBufferSize()` exceeded the requested
  `bufferSizeMs` (e.g. Samsung A/M/S returns 40 ms vs Pixel 6's 20 ms)
  silently clamped the read cadence to the device floor. Read cadence
  now matches `bufferSizeMs` regardless of hardware floor.
- **Android: `MediaMuxer` double-free SIGABRT.** `AndroidAudioEncoder`'s
  `finalize()` and `release()` paths could race on the muxer reference
  during stop, releasing the native object twice
  (`decStrong() called on 0x... too many times`). Both paths now use a
  swap-and-null pattern; only one frees.
- **Android: stale `_currentTime` after seek.** A cancelled `AudioTrack`
  playback coroutine could publish a stale post-seek timestamp because
  `track.write()` is not a suspension point. Downstream live evaluation
  observed a forward jump as a spurious segment completion, wedging the
  evaluator silently. The loop now checks `isActive` before publishing.
- **Recorder: input latency compensated at the source.**
  `AudioBuffer.timestamp` is now `deliveryMs - inputLatencyMs` on
  Android and subtracts `AVAudioSession.inputLatency` on iOS; consumers
  no longer need to subtract `SonixRecorder.inputLatencyMs` themselves.
  `SonixRecorder.inputLatencyMs` remains public as a diagnostic surface,
  parallel to `SonixPlayer.outputLatencyMs`.

### Added

- **`SonixPlayer.audibleTimeMsAtWallNanos(wallNanos)`** — given a
  monotonic-nanos wall moment, returns the player's audible time at
  that moment. Returns `-1L` when the player isn't running yet.
- **`CalibraLiveEval.feedAudioSamples(samples, sampleRate,
  captureTimestampNanos = 0L)`** — optional capture-timestamp parameter
  for hardware-clock-aligned anchoring. Existing callers passing only
  `(samples, sampleRate)` continue to work; the anchor falls back to
  the session clock.

### Changed

- **`AudioBuffer.timestamp` semantics.** Now: absolute monotonic
  nanoseconds at the moment the LAST sample in the buffer was captured
  at the mic, with input latency already accounted for. Previously: raw
  wall-clock elapsed since recording started. Consumers that subtracted
  `SonixRecorder.inputLatencyMs` themselves should remove that
  correction.

## [1.0.0] - 2026-04-13

First stable release. The public API has been hardened and several defaults
retuned based on field use. Callers upgrading from 0.9.x should read the
**Breaking changes** section carefully.

### Breaking changes

- **`VT.initialize(...)` signature gains `proxyAuthProvider`.** A callback
  for supplying auth headers when registering with a proxy. Existing callers
  can pass `null` to preserve current behavior.
- **`CalibraLiveEval.LiveSession.livePitch` changed from `StateFlow` to
  `SharedFlow`.** The flow now emits every pitch point rather than dropping
  to the latest. Callers relying on `.value` must migrate to collection.
- **`CalibraLiveEval` config defaults retuned** to fit the 40 ms real-time
  audio budget:
  - `PitchDetectorConfig.BALANCED` / `RELAXED` `bufferSize`: `2048` → `1024`
  - `SessionConfig` default `hopSize`: `160` → `320` (2 frames per buffer)
- **`SonixPlayer.currentTime` semantics changed.** Now reports actual DAC
  presentation time (via `AudioTrack.getTimestamp` on Android, subtracting
  `AVAudioSession.outputLatency` on iOS) instead of the write/decode offset.
  Pitch timestamps from live evaluation subtract recorder input latency.
  Apps that previously offset `currentTime` manually should remove those
  corrections.
- **`CalibraLiveEval.feedAudioSamples` emits multiple pitch points per
  call** instead of only the last. Consumers that assumed a single emission
  per call must handle multi-point delivery.

### Added

- **Runtime auto-loop toggle.** `CalibraLiveEval.setAutoLoopEnabled(Boolean)`
  switches between retry-on-low-score and always-advance policies at
  runtime. `LiveSession.clearPendingAction()` for clearing pre-computed
  actions.
- **Shruti picker options.** `ShrutiAlignmentResult.options: List<ShrutiOption>`
  exposes all viable shrutis for picker UIs, each with pitch class,
  octave-aware note label, Hz at the octave nearest to the student's
  anchor, and the `shiftSemitones` to apply via `SonixPlayer.pitch`.
- **Audio latency APIs.** `SonixPlayer.outputLatencyMs` and
  `SonixRecorder.inputLatencyMs` expose platform latency for apps that
  need to align external timelines.
- **Shruti alignment.** `ShrutiAligner` engine and `CalibraMusic.alignShruti(...)`
  for picking the best fine-tune shift between a reference and a student
  anchor.
- **WAV encoding, peak normalization, offline mixing.** `SonixEncoder`
  supports WAV output; `SonixAudioUtils.normalize(...)` does peak
  normalization; `SonixAudioUtils.mix(...)` performs offline multi-track
  mixing with per-track gains, resampling, and soft clipping (mono only).
- **Music theory and tone generation APIs.** `CalibraMusic` adds sargam
  note names (Hindustani and Carnatic), just-intonation ratios, and
  interval generators. `SonixToneGenerator` produces sine, square, sawtooth,
  and triangle waveforms. `SonixAudioUtils` adds concatenation,
  frame/time conversion, and frame-mask-to-segment helpers.
- **`TransSegment.type`.** `.trans` JSON segment types (`teacher_vocal`,
  `student_vocal`, `commentary`) are now parsed instead of silently
  dropped.

### Changed

- **Real-time pitch quality.** The processor now applies a 5-point median
  filter before weighted averaging, dynamic Gaussian weighting, and
  short-gap interpolation (≤ 5 frames). Amplitude-gated frames flow
  through the processor instead of bypassing it.
- **Android audio session** now uses `USAGE_MEDIA` for all modes, which
  avoids the Bluetooth SCO latency penalty. AEC is no longer requested
  (unsupported on Android in practice).

### Fixed

- **R8 keep rules.** `SonixAudioUtils` and `SonixToneGenerator` are now
  retained in the AAR — they were previously stripped by R8 despite being
  public API, making them unusable from consumer apps.

## [0.9.2] - 2026-02-11

Release-pipeline fixes. No SDK API or behavior changes.

## [0.9.1] - 2026-02-06

Release-pipeline fixes. No SDK API or behavior changes.

## [0.9.0] - 2025-02-04

Initial public release.
