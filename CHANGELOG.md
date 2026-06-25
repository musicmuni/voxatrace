# Changelog

All notable changes to VoxaTrace will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/),
and this project adheres to [Semantic Versioning](https://semver.org/).

## [Unreleased]

### Added
- **Desktop & server JVM SDK.** VoxaTrace is now published for the JVM (macOS and
  Linux) with the full pitch, voice-metrics, intonation, singing-evaluation and
  audio API, so you can build desktop apps and run analysis or lesson authoring
  on a server. Add the base dependency plus the native artifact for your platform
  (AI-backed features are an opt-in extra). Initialize with
  `VT.initializeForServer(apiKey = ...)`. See the JVM quick start.
- **Octave-robust pitch detection (`PitchAlgorithm.MELODIA`).** A new pitch
  backend tracks the fundamental on high or harmonic-rich voices where the
  default detector can jump down an octave. Recommended for offline reference
  extraction and batch analysis.
- **Lesson bundles: a stable, versioned format.** Reference material
  (16 kHz audio, pitch, HPCP, phrases) is packaged as a documented bundle you can
  produce ahead of time and load with `LessonBundle`. Author bundles with the new
  offline `lesson-extractor` CLI, or from your own JVM code via `ReferenceExtractor`.
  See the lesson-authoring guide and the bundle-format reference.

### Changed
- **Note-label genre is now `MusicGenre`.** The `analyzePitching`
  `noteLabelTradition: NoteLabelTradition` parameter is renamed to
  `genre: MusicGenre`, and the type moved package. Update call sites and imports.
- **Svara octave markers now use the traditional combining dots.** Upper/lower
  octaves are marked with a dot above/below the svara (e.g. `Ṡ`, `Ṣ`) instead of
  apostrophe/comma (`S'`, `S,`), matching standard Indian-classical notation. This
  affects intonation note labels and lesson-bundle svara transcription.

## [2.1.0] - 2026-05-29

### Added
- **Synced backing-track accompaniment under live evaluation.** A new
  `PlaybackController` transport interface (`currentTime`, `isPlaying`,
  `play`/`pause`/`seek`, `audibleTimeMsAtWallNanos`) is implemented by both
  `SonixPlayer` and `SonixMixer`, and `CalibraLiveEval` now accepts a
  `PlaybackController`. A multi-track mix (teacher + accompaniment) can
  therefore drive a scored session, sample-locked to a single audible
  presentation clock. `SonixMixer.setReferenceTrack(...)` selects the track
  whose clock drives `currentTime`; `setPitch(...)` transposes every track
  tempo-preserving so reference and backing stay in tune.
- **iOS: `PlayerState.tempo` passthrough.** The SwiftUI observable wrapper
  now exposes `tempo`, restoring parity with the Kotlin `SonixPlayer` and the
  Swift builder so SwiftUI apps can control playback speed.

### Fixed
- **Singing scores are now correct when the student sings in a different key
  than the reference.** `CalibraLiveEval.setStudentKeyHz()` is now honored
  even when called before `prepareSession()`. Previously such a call was
  silently dropped (the evaluator did not exist yet), so a lesson sung in a
  key other than the reference recording was scored with **no key-shift
  compensation**: the student's pitch sat a fixed interval off the reference
  and scored far too low. The student key is now retained on the session and
  applied when the evaluator is created, independent of call order.
- **`PitchContourRecorder` is now usable from Kotlin Multiplatform `commonMain`.**
  It no longer extends `kotlinx.atomicfu.locks.SynchronizedObject`; thread-safety
  is now held by an internal lock (composition). atomicfu is a compile-only
  dependency, so leaking it as a public supertype broke KMP consumers'
  `commonMain` metadata compilation ("Cannot access 'SynchronizedObject' which is
  a supertype of 'PitchContourRecorder'"). Android-only consumers were unaffected.
  No behavioral or API change otherwise.

## [2.0.0] - 2026-05-22

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
- **Unused calibra error and evaluator types removed.** `CalibraException`
  and `CalibraErrorType` (never thrown by any facade), the iOS-only error
  enums `PitchDetectorError` / `EvaluatorError` / `EffectsError` /
  `AnalysisError` (never thrown/returned), and the orphaned
  `calibra.model.EvaluatorConfig` / `EvaluatorPreset` (consumed by no
  facade) are gone. Failure handling follows ADR-022:
  `VoxaTraceNotInitializedException` and `VoxaTraceKilledException` for
  SDK-state errors, `IllegalArgumentException` for invalid input, and
  return-value encoding (`null`, `SingingResult.EMPTY`) for inconclusive
  domain outcomes. For student key transposition use `studentKeyHz`
  (`CalibraNoteEval.evaluate` / `CalibraMelodyEval` via `student.keyHz`)
  or `CalibraLiveEval.setStudentKeyHz(...)`.
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
- **Realtime pitch-contour API replaced.** `PitchDetector.livePitchContour:
  StateFlow<PitchContour>` (a bounded rolling window, 10 s by default) and
  `PitchDetector.setContourMaxDuration(seconds)` are removed in favour of
  `PitchDetector.pitchContour: PitchContourRecorder`, a lossless,
  append-only, unbounded contour store. There is no compatibility shim.
  Migration:
  - reactive collection of `livePitchContour` for display: poll
    `pitchContour.recent(seconds)` on your render tick.
  - `livePitchContour.value` for whole-session scoring:
    `pitchContour.snapshot()`. This also fixes silent truncation, since the
    old window dropped points beyond its cap, so long sessions were scored
    on a partial contour.
  - `setContourMaxDuration(n)`: no replacement; the window span is now
    chosen at read time via `recent(n)`.

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
  `TesseraBreath` (control / phrase structure / reference comparison),
  `TesseraAgility` (10-stage pipeline + 0–1 score),
  `TesseraRange` (batch range + 13-dim search vector + matching) and
  `TesseraRangeSession` (guided phase-driven flow with observable state),
  `TesseraSpeakingPitch` (median-F0 detection from speech).
- **Intonation (`accura`).** New top-level facade — no 1.x equivalent.
  `Accura.analyzePitching` returns per-note deviation against EQ
  (12-TET) or JI (just-intonation) target intervals with optional
  global tuning-offset alignment; `Accura.calculateScore` produces a
  0–100 score with a piecewise-linear grading scale and a small-sample
  outlier-robust adjustment. Failure contract follows ADR-022 from
  inception: precondition violations (empty contour, `tonicHz <= 0`,
  empty `scaleIntervals`) throw `IllegalArgumentException`;
  domain-level inconclusive outcomes (e.g. fewer than three histogram
  peaks) surface via the non-null `IntonationAnalysisResult.error`
  field. `calculateScore` requires `result.error == null` and a
  non-empty `result.notes`.
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
- **`MusicTheory.deriveUserShruti(nspHz, rangeLowHz = null, rangeHighHz = null, rangeThresholdSemitones = 18f)`.**
  Computes the user's practice shruti from their natural speaking pitch
  (NSP) and most-recent vocal range, applying the policy from Musicmuni
  research synthesis (§B7). Returns `UserShrutiDerivation { targetHz, source }`
  where `source ∈ {NSP_NO_RANGE, NSP_NARROW_RANGE, VOCAL_RANGE}`. Below
  18 st of measured range, falls back to NSP; at or above, applies
  `Sa = max(rangeLow + 7, nspMidi − 2)` clipped to
  `[rangeHigh − 17, rangeHigh − 12]`.
- **`PitchDetector.feedContour(samples, sampleRate, anchorTime)` and
  `PitchDetector.pitchAt(timeSeconds)`** are the canonical streaming
  contour APIs. `feedContour` writes into `pitchContour` /
  `livePitch`, back-spread from the supplied anchor by the detector's
  hop. `pitchAt` returns the closest contour point via binary search.
- **`PitchDetector.clearPitchContourFrom(timeSeconds)`** for
  segment-aware retry/seek-back: drops points at or after `timeSeconds`,
  keeps earlier ones.
- **`PitchDetector.pitchContour: PitchContourRecorder`.** Records every
  detected point for the lifetime of the detector with no rolling-window
  cap. Read the whole session via `pitchContour.snapshot()` (end-of-session
  scoring) or the trailing window via `pitchContour.recent(seconds)` (live
  visualization; the caller chooses the span at read time). Also exposes
  `pitchAt(timeSeconds)`, `size`, and `durationSeconds`. `PitchContourRecorder`
  is a new public type in `com.musicmuni.voxatrace.common.streaming`, with
  public reads and detector-internal writes.
- **Off-scale note detection in `Accura.analyzePitching`.** When an explicit
  scale is supplied, prominent peaks the singer dwelt on that land on a
  chromatic degree outside the scale are surfaced as
  `IntonationAnalysisResult.offScaleNotes: List<OffScaleNote>`: notes sung
  by mistake, reported for awareness only. They carry no target, score, or
  tier and never affect `PitchingScore`. Each `OffScaleNote` gives the
  chromatic `label`, its `nearestInScaleLabel` (for "you reached for P"
  framing), and cents offsets. Best-effort: if detection fails the list is
  empty and `error` is not set. Capture radius is 30 cents.
- **Kotlin/Swift parity for convenience helpers.** Helpers that previously
  existed only as iOS Swift conveniences now have Kotlin equivalents (the
  Swift wrappers delegate to them, so behaviour is identical on both
  platforms): `CalibraVAD.hasVoiceActivity(samples, sampleRate, threshold = 0.3)`
  and `CalibraVAD.classifyVoiceActivity(samples, sampleRate)`;
  `SingingResult.latestScore(segmentIndex)` / `bestScore(segmentIndex)`
  (single-segment accessors alongside the existing `*PerSegment()` maps);
  and `SonixRecorder.audioBuffersResampled(targetRate): Flow<FloatArray>`
  (a fixed-rate counterpart to the raw `audioBuffers` flow).

### Changed

- **Default frequency-detection range.** `PitchDetectorConfig.BALANCED`
  defaults to 80 Hz – 1000 Hz (was previously documented as 50–2000).
  SwiftF0 model range remains 46.875 Hz – 2093.75 Hz.
- **`Accura.analyzePitching` does not report glided-through notes.** A
  per-note histogram peak whose integrated area is below 12 % of the
  busiest peak's is treated as a transient glide, not an intended note, and
  is excluded from `IntonationAnalysisResult.notes`. `PeakDetectionConfig`
  gains a `minPeakAreaFraction` knob (default `0f` = off) exposing this
  relative-area gate to direct peak-detection callers.
- **Accura grading on a single scale.** Each note reports
  `tier: PitchingTier` (`EXCELLENT`/`GOOD`/`FAIR`/`POOR`) and
  `score: Float` (per-note 0–100); `PitchingScore` also carries `tier` and
  reports `noteCount`. Per-note and overall verdicts derive from one 0–100
  grading curve and one tier band table, so a note's tier and its score can
  never disagree. `PitchingTier` exposes each band's `minScore` boundary
  (EXCELLENT ≥ 85, GOOD ≥ 65, FAIR ≥ 40, POOR ≥ 0) as the single source of
  truth for the bands.
- **`PitchDetector.detect` drops its reserved third parameter.** The
  signature is now `detect(samples, sampleRate)`. The former
  `startTimeSeconds` argument had no effect (it was reserved once `detect`
  stopped writing the contour; contour writes go through `feedContour`).
- **`LessonNote` is an immutable value type.** Its fields are now `val`,
  and the unused `audioLengthMilliSecs` field is gone. The type is
  `LessonNote(noteName, noteLabel, noteAudioFilePath, numBeats, numSamplesConsonant)`.

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
| `PitchDetector.livePitchContour` (`StateFlow<PitchContour>`) | `PitchDetector.pitchContour.recent(seconds)` (display) / `pitchContour.snapshot()` (whole-session scoring) |
| `PitchDetector.setContourMaxDuration(seconds)` | removed; choose the window span at read time via `pitchContour.recent(seconds)` |
| `sonix.model.LessonSvara` (`svaraName`, `svaraLabel`, `svaraAudioFilePath`) | `sonix.model.LessonNote` (`noteName`, `noteLabel`, `noteAudioFilePath`) |
| `SonixLessonSynthesizer.create(svaras = …)` / `Builder.svaras(…)` | `SonixLessonSynthesizer.create(notes = …)` / `Builder.notes(…)` |

Permanent calibra facades are unchanged: `CalibraLiveEval`,
`CalibraMelodyEval`, `CalibraNoteEval`, `CalibraVAD`. The 1.x audio-effects
facade (`CalibraEffects` and its config/preset types) is **not available**
in 2.0.0 — held back until its public API is finalized. Apps that used the
1.x effects chain should pin to 1.0.x until the public surface returns in a
later release.

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
before reading `result.notes` or passing the result to
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
- **`TransSegment.type`.** The `.trans` JSON `type` field is now
  deserialized onto `TransSegment.type` instead of being silently
  dropped. The value (e.g. `teacher_vocal`, `student_vocal`,
  `commentary`) is passed through verbatim; the SDK does not validate
  or interpret it.

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
