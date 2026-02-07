---
sidebar_position: 4
---

# CalibraVocalRange

Streaming vocal range analyzer and guided session for determining a singer's pitch range. Includes both a low-level analyzer (`CalibraVocalRange`) and a high-level session (`VocalRangeSession`) with observable state.

## Quick Start

### Kotlin

```kotlin
val analyzer = CalibraVocalRange.create()

// Feed audio from recorder
recorder.audioBuffers.collect { buffer ->
    analyzer.addAudio(buffer.toFloatArray())
}

// Get detected range
val range = analyzer.getRange()
if (range != null) {
    println("Range: ${range.lower.noteLabel} to ${range.upper.noteLabel}")
    println("Octaves: ${range.octaves}")
}

analyzer.release()
```

### Swift

```swift
let analyzer = CalibraVocalRange.create()

// Feed audio from recorder
for await buffer in recorder.audioBuffersStream() {
    analyzer.addAudio(samples: buffer.toFloatArray())
}

// Get detected range
if let range = analyzer.getRange() {
    print("Range: \(range.lower.noteLabel) to \(range.upper.noteLabel)")
    print("Octaves: \(range.octaves)")
}

analyzer.release()
```

## CalibraVocalRange (Low-Level Analyzer)

### Creating an Analyzer

```kotlin
// Default config (ASHA 2018 guidelines)
val analyzer = CalibraVocalRange.create()

// Custom config
val analyzer = CalibraVocalRange.create(VocalRangeConfig(
    minNoteDurationSeconds = 0.5f,
    minConfidence = 0.7f
))
```

```swift
let analyzer = CalibraVocalRange.create()

let analyzer = CalibraVocalRange.create(config: VocalRangeConfig(
    minNoteDurationSeconds: 0.5,
    minConfidence: 0.7,
    stabilityWindowMs: 50,
    maxDeviationSemitones: 1.0,
    sampleRate: 16000
))
```

### Swift Config Convenience Methods

```swift
let config = VocalRangeConfig.fastDetection()     // 0.5s hold time
let config = VocalRangeConfig.strictDetection()    // 1.5s hold, 0.7 confidence
let config = VocalRangeConfig.withMinDuration(2.0) // Custom hold time
```

### Configuration

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `minNoteDurationSeconds` | `Float` | `1.0` | Minimum duration for a note to be included |
| `minConfidence` | `Float` | `0.5` | Minimum pitch detection confidence |
| `stabilityWindowMs` | `Float` | `50` | Window for stability checking (ms) |
| `maxDeviationSemitones` | `Float` | `1.0` | Maximum deviation for "stable" pitch |
| `sampleRate` | `Int` | `16000` | Expected audio sample rate |

### Methods

| Method | Description |
|--------|-------------|
| `addAudio(samples)` | Feed audio samples for analysis |
| `getRange()` | Get complete range (lower/upper/octaves), or null if not enough data |
| `getStableNote()` | Get the longest stable note detected so far |
| `getLowerLimit()` | Get 5th percentile pitch (lower bound) |
| `getUpperLimit()` | Get 95th percentile pitch (upper bound) |
| `getStats()` | Get detection statistics |
| `reset()` | Reset for new detection session |
| `release()` | Release resources |

### Result Types

#### VocalRange

| Property | Type | Description |
|----------|------|-------------|
| `lower` | `VocalPitch` | 5th percentile (physiological low) |
| `upper` | `VocalPitch` | 95th percentile (physiological high) |
| `octaves` | `Float` | Range in octaves |
| `semitones` | `Int` | Range in semitones |

#### VocalPitch

| Property | Type | Description |
|----------|------|-------------|
| `frequencyHz` | `Float` | Frequency in Hz |
| `midiNote` | `Int` | MIDI note number (0–127) |
| `noteLabel` | `String` | Note name with octave (e.g., "C4", "F#3") |
| `confidence` | `Float` | Detection confidence (0.0–1.0) |

#### DetectedNote

| Property | Type | Description |
|----------|------|-------------|
| `pitch` | `VocalPitch` | The detected pitch |
| `durationSeconds` | `Float` | How long this note was held stably |
| `isStable` | `Boolean` | Whether the note met stability criteria |

#### RangeStats

| Property | Type | Description |
|----------|------|-------------|
| `totalPitchesReceived` | `Int` | Total pitch frames processed |
| `validPitchesAfterFiltering` | `Int` | Frames that passed confidence filter |
| `stableSegmentsDetected` | `Int` | Number of stable segments found |
| `longestStableSegmentSeconds` | `Float` | Duration of longest stable segment |
| `hasEnoughDataForRange` | `Boolean` | Whether enough data for range calculation |

### Guided Range Detection

For step-by-step range finding (e.g., "sing your lowest note"):

```kotlin
val analyzer = CalibraVocalRange.create()

// Phase 1: Detect lowest note
showPrompt("Sing your lowest comfortable note")
// ... feed audio via addAudio() ...
val lowestNote = analyzer.getStableNote()
analyzer.reset()

// Phase 2: Detect highest note
showPrompt("Sing your highest comfortable note")
// ... feed audio via addAudio() ...
val highestNote = analyzer.getStableNote()

analyzer.release()
```

### Static Utility Methods

```kotlin
val label = CalibraVocalRange.labelForMidi(60)  // "C4"
val midi = CalibraVocalRange.hzToMidi(440f)     // 69
val hz = CalibraVocalRange.midiToHz(69)         // 440.0
```

```swift
let label = CalibraVocalRange.labelForMidi(60)  // "C4"
let midi = CalibraVocalRange.hzToMidi(440)      // 69
let hz = CalibraVocalRange.midiToHz(69)         // 440.0
```

## VocalRangeSession (High-Level)

A high-level session with observable state that manages the full detection flow: countdown, detect low note, transition, detect high note, complete.

### Creating a Session

```kotlin
val detector = CalibraPitch.createDetector()
val session = VocalRangeSession.create(detector = detector)
```

```swift
let detector = CalibraPitch.createDetector()
let session = VocalRangeSession.create(detector: detector)
```

### Session Configuration

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `countdownSeconds` | `Int` | `3` | Countdown before detection starts |
| `maxDetectionTimeSeconds` | `Int` | `10` | Max time to wait for stable note |
| `minNoteDurationSeconds` | `Float` | `1.0` | Minimum stable note duration |
| `minConfidence` | `Float` | `0.5` | Minimum pitch confidence |
| `transitionDelayMs` | `Long` | `500` | Delay between low and high detection |
| `autoFlow` | `Boolean` | `true` | Run automatic flow |

### Presets

| Preset | Kotlin | Description |
|--------|--------|-------------|
| Default | `VocalRangeSessionConfig.DEFAULT` | Auto-flow with 3s countdown |
| Manual | `VocalRangeSessionConfig.MANUAL_FLOW` | Manual phase control |

### Auto-Flow Mode (Recommended)

```kotlin
val session = VocalRangeSession.create(detector = CalibraPitch.createDetector())

// Observe state in UI
session.state.collect { state ->
    when (state.phase) {
        VocalRangePhase.COUNTDOWN -> showCountdown(state.countdownSeconds)
        VocalRangePhase.DETECTING_LOW -> showLowDetection(state)
        VocalRangePhase.DETECTING_HIGH -> showHighDetection(state)
        VocalRangePhase.COMPLETE -> showResult(state.result!!)
        else -> {}
    }
}

// Start detection
session.start()

// Feed audio
recorder.audioBuffers.collect { buffer ->
    session.addAudio(buffer.samples, sampleRate = buffer.sampleRate)
}

// User taps "Lock" button
session.confirmNote()

session.release()
```

### Observing State

#### Kotlin (StateFlow)

```kotlin
session.state.collect { state ->
    updatePhaseUI(state.phase)
    updatePitchDisplay(state.currentPitch)
    updateStabilityBar(state.stabilityProgress)
    state.bestLowNote?.let { showBestLow(it) }
    state.bestHighNote?.let { showBestHigh(it) }
    state.result?.let { showFinalResult(it) }
}
```

### VocalRangeState

| Property | Type | Description |
|----------|------|-------------|
| `phase` | `VocalRangePhase` | Current detection phase |
| `countdownSeconds` | `Int` | Remaining countdown seconds |
| `phaseMessage` | `String` | User-facing status message |
| `currentPitch` | `VocalPitch?` | Real-time detected pitch |
| `currentAmplitude` | `Float` | Current audio amplitude |
| `stabilityProgress` | `Float` | Progress toward stable note (0.0–1.0) |
| `bestLowNote` | `DetectedNote?` | Lowest stable note found so far |
| `bestHighNote` | `DetectedNote?` | Highest stable note found so far |
| `lowNote` | `DetectedNote?` | Locked low note (confirmed by user) |
| `highNote` | `DetectedNote?` | Locked high note (confirmed by user) |
| `result` | `VocalRangeResult?` | Final result when complete |
| `error` | `String?` | Error message if detection failed |

### VocalRangePhase

| Phase | Description |
|-------|-------------|
| `IDLE` | Session not started |
| `COUNTDOWN` | Countdown before detection |
| `DETECTING_LOW` | Detecting lowest comfortable note |
| `TRANSITION` | Transition between low and high |
| `DETECTING_HIGH` | Detecting highest comfortable note |
| `COMPLETE` | Detection complete, results available |
| `CANCELLED` | Detection cancelled or failed |

### VocalRangeResult

| Property | Type | Description |
|----------|------|-------------|
| `low` | `VocalPitch` | Lowest detected note |
| `high` | `VocalPitch` | Highest detected note |
| `octaves` | `Float` | Range in octaves |
| `semitones` | `Int` | Range in semitones |
| `naturalShruti` | `VocalPitch` | Calculated natural key (Sa) for Indian Classical music |

### Session Methods

| Method | Description |
|--------|-------------|
| `addAudio(samples, sampleRate)` | Feed audio samples. Resamples to 16kHz internally. |
| `start()` | Start auto-flow detection |
| `confirmNote()` | Lock current best note and advance to next phase |
| `cancel()` | Cancel the current session |
| `reset()` | Reset to start a new session |
| `release()` | Release all resources |

### Manual Flow Control

For custom UIs that don't use the auto-flow:

```kotlin
val config = VocalRangeSessionConfig(autoFlow = false)
val session = VocalRangeSession.create(config, detector = CalibraPitch.createDetector())

session.startPhase(VocalRangePhase.DETECTING_LOW)
// ... feed audio ...
session.advancePhase()  // Move to DETECTING_HIGH
// ... feed audio ...
session.complete()
```

| Method | Description |
|--------|-------------|
| `startPhase(phase)` | Start a specific detection phase |
| `advancePhase()` | Advance to the next phase |
| `complete()` | Finalize and calculate result |

## Next Steps

- [CalibraPitch](./pitch) — Real-time pitch detection used by range analyzer
- [CalibraLiveEval](./live-eval) — Evaluate singing against a reference
- [CalibraSpeakingPitch](./speaking-pitch) — Detect speaking voice pitch
