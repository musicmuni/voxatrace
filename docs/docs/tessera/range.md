---
sidebar_position: 4
---

# TesseraRange

Vocal range estimation, search-vector creation, and singer↔song matching. The batch facade `TesseraRange` computes results from a complete `PitchContour`. The session facade `TesseraRangeSession` (below) drives an interactive "find your range" flow with observable state.

## Quick Start

### Kotlin

```kotlin
val rangeResult = TesseraRange.computeVocalRange(contour)
if (rangeResult != null) {
    println("${rangeResult.range.lower.noteLabel} – ${rangeResult.range.upper.noteLabel}")
    val match = TesseraRange.computeMatch(
        singerVector = rangeResult.searchVector,
        songVector = songVec,
        singerGender = Gender.MALE
    )
    println("Difficulty: ${match.difficulty}/5")
}
```

### Swift

```swift
if let result = TesseraRange.computeVocalRange(contour: contour) {
    let match = TesseraRange.computeMatch(
        singerVector: result.searchVector,
        songVector: songVec,
        singerGender: .male
    )
}
```

## Methods (batch)

| Method | Description |
|--------|-------------|
| `computeVocalRange(contour, ratioSumInRange: Float = 0.9f, config: SearchVectorConfig = DEFAULT): VocalRangeResult?` | Range bounds + 13-dim search vector. **Returns `null`** when there's insufficient voiced data (per ADR-022). |
| `computeSearchVector(contour, normalize: Boolean = true, config: SearchVectorConfig = DEFAULT): FloatArray` | 13-dim sigmoid-normalized vector. `normalize = true` for song vectors (L1-normalized), `false` for singer vectors (raw magnitude). |
| `computeMatch(singerVector, songVector, singerGender: Gender, config: SearchVectorConfig = DEFAULT): VocalRangeMatch` | Inner-product similarity with gender-based octave shift. |

## Result types

### VocalRangeResult (batch result)

```kotlin
data class VocalRangeResult(
    val range: VocalRange,
    val searchVector: FloatArray,    // 13-dim
    val match: VocalRangeMatch? = null,
)
```

This is distinct from `VocalRangeSessionResult` (returned by `TesseraRangeSession`, see below).

### VocalRange

```kotlin
data class VocalRange(
    val lower: VocalPitch,    // batch (this facade): centroid-anchored; streaming: 5th percentile
    val upper: VocalPitch,    // batch (this facade): centroid-anchored; streaming: 95th percentile
    val octaves: Float,
)
```

`TesseraRange.computeVocalRange` (batch) derives `lower`/`upper` from a density histogram: it anchors on the density-weighted centroid and expands a symmetric window until it captures `ratioSumInRange` (default 0.9) of the pitch mass. The streaming detector behind `TesseraRangeSession` / `TesseraSession` instead reads the 5th / 95th percentiles of its running histogram.

`semitones` (computed) returns `upper.midiNote - lower.midiNote`.

### VocalPitch

```kotlin
data class VocalPitch(
    val frequencyHz: Float,
    val midiNote: Int,
    val noteLabel: String,    // e.g., "C4"
    val confidence: Float = 1f,
)
```

### VocalRangeMatch

```kotlin
data class VocalRangeMatch(
    val similarity: Float,    // [0, 1]
    val difficulty: Int,      // 1 (easy) – 5 (hard)
)
```

### Gender

```kotlin
enum class Gender { MALE, FEMALE }
```

## SearchVectorConfig

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `sigmoidK` | `Float` | `0.025` | Sigmoid normalization gain |
| `binWidth` | `Int` | `4` | Histogram bin width in MIDI semitones |
| `featureRate` | `Float` | `10` | Resampling rate (Hz) |
| `pitchRangeMin` | `Int` | `36` | Min MIDI note (= C2) |
| `pitchRangeMax` | `Int` | `92` | Max MIDI note (= G#6) |

## Common Pitfalls

1. **`computeVocalRange` returns null for insufficient data** (valid input, domain outcome — not an exception).
2. **Singer vector: `normalize = false`. Song vector: `normalize = true`.** The asymmetry is by design.
3. **Contour must have ≥ 2 samples** (throws per ADR-022).

---

## TesseraRangeSession

Interactive guided range detection with observable state. Drives the user through countdown → low note → transition → high note → complete, exposing a `StateFlow<VocalRangeState>` that UI can collect.

### Quick Start

```kotlin
val session = TesseraRangeSession.create(
    detectorConfig = PitchDetectorConfig.BALANCED
)

session.state.collect { updateUI(it) }
session.start()
recorder.audioBuffers.collect { buffer ->
    session.addAudio(buffer.toFloatArray(), sampleRate = buffer.sampleRate)
}
session.confirmNote()    // user presses "Lock" to confirm
session.release()
```

### Factory

```kotlin
fun create(
    config: VocalRangeSessionConfig = VocalRangeSessionConfig.DEFAULT,
    rangeConfig: VocalRangeConfig = VocalRangeConfig.DEFAULT,
    detectorConfig: PitchDetectorConfig = PitchDetectorConfig.BALANCED,
): TesseraRangeSession
```

### Methods

| Method | Description |
|--------|-------------|
| `addAudio(samples, sampleRate = 16000)` | Feed raw audio. Resamples to 16 kHz. Only processed during `DETECTING_LOW`/`DETECTING_HIGH` phases. |
| `addPitch(time, pitchHz, confidence)` | Feed pre-extracted pitch. Same phase gating. |
| `start()` | Begin auto-flow (countdown → low → high → complete). Throws if `autoFlow = false`. |
| `confirmNote(): Boolean` | Lock the current best detected note. Returns `false` if no stable note yet. |
| `cancel()` | Cancel session, transition to `CANCELLED`. |
| `reset()` | Reset state and detector. |
| `startPhase(phase)` | Manual flow only — start a specific phase. Throws when `autoFlow = true`. |
| `advancePhase()` | Manual flow only — advance to next phase. Throws when `autoFlow = true`. |
| `complete()` | Force completion using current `lowNote`/`highNote`. |
| `getStats(): RangeStats` | Statistics about accumulated range data. |
| `release()` | Free detector resources. |

### State

```kotlin
val state: StateFlow<VocalRangeState>

data class VocalRangeState(
    val phase: VocalRangePhase = IDLE,
    val countdownSeconds: Int = 0,
    val phaseMessage: String = "Ready to detect your vocal range",
    val currentPitch: VocalPitch? = null,
    val currentAmplitude: Float = 0f,         // input RMS level [0,1]; updated on addAudio only
    val stabilityProgress: Float = 0f,
    val bestLowNote: DetectedNote? = null,
    val bestHighNote: DetectedNote? = null,
    val lowNote: DetectedNote? = null,        // locked low
    val highNote: DetectedNote? = null,       // locked high
    val result: VocalRangeSessionResult? = null,
    val error: String? = null,
)

enum class VocalRangePhase {
    IDLE, COUNTDOWN, DETECTING_LOW, TRANSITION,
    DETECTING_HIGH, COMPLETE, CANCELLED,
}
```

### VocalRangeSessionResult

```kotlin
data class VocalRangeSessionResult(
    val low: VocalPitch,
    val high: VocalPitch,
    val octaves: Float,
    val semitones: Int,
    val naturalShruti: VocalPitch,    // calculated tonic (Sa)
)
```

`naturalShruti` is computed using Indian Classical pedagogy:
- Range &lt; 12 st: center the Sa (`min + width/2`)
- 12–18 st: anchor from top (`max - 12`)
- ≥ 19 st: Mandra-Pa rule (`min + 5`), capped at `max - 12`

For a research-backed shruti derivation that combines NSP + range, use [`MusicTheory.deriveUserShruti`](../common/music-theory#deriveusershruti) instead.

### VocalRangeSessionConfig

Tunes the guided flow only; detection-engine settings live in `VocalRangeConfig` (below).

| Property | Type | Default |
|----------|------|---------|
| `countdownSeconds` | `Int` | `3` |
| `transitionDelayMs` | `Long` | `500` |
| `autoFlow` | `Boolean` | `true` |

Presets: `DEFAULT` (auto-flow), `MANUAL_FLOW` (`autoFlow = false`).

### VocalRangeConfig (streaming detection internals)

| Property | Type | Default |
|----------|------|---------|
| `minNoteDurationSeconds` | `Float` | `1.0` |
| `minConfidence` | `Float` | `0.5` |
| `stabilityWindowMs` | `Float` | `50` |
| `maxDeviationSemitones` | `Float` | `1.0` |
| `sampleRate` | `Int` | `16000` |

### Common Pitfalls

1. **`start()` is for auto-flow only.** Use `startPhase`/`advancePhase` when `autoFlow = false`.
2. **Audio is only processed during DETECTING_LOW / DETECTING_HIGH.** Samples sent in IDLE/COUNTDOWN/TRANSITION/COMPLETE/CANCELLED are silently ignored.
3. **`confirmNote()` is user-driven.** Returns `false` if no stable note has been detected yet.
4. **Always `release()`.** Holds native detector resources.

## See also

- [Tessera Session (multi-metric streaming)](./session)
- [Tessera (multi-metric batch)](./overview)
- [MusicTheory.deriveUserShruti](../common/music-theory#deriveusershruti) — research-backed shruti policy from NSP + range
- [PitchDetection](../tona/pitch-detection)
