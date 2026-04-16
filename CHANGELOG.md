# Changelog

All notable changes to VoxaTrace will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/),
and this project adheres to [Semantic Versioning](https://semver.org/).

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
