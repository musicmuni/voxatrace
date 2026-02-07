---
sidebar_position: 11
---

# CalibraEffects

Unified audio effects chain for real-time vocal processing. Applies noise gate, compressor, and reverb in sequence to audio samples.

## Quick Start

### Kotlin

```kotlin
// Create with preset
val effects = CalibraEffects.create(EffectsPreset.VOCAL_CHAIN)

// Process audio in-place
effects.process(samples)

// Cleanup when done
effects.release()
```

### Swift

```swift
// Create with preset
let effects = CalibraEffects.create(preset: .vocalChain)

// Process audio in-place
effects.process(samples: &samples)

// Cleanup when done
effects.release()
```

## Configuration

### Effects Presets

Combo presets that configure the entire chain in one step.

| Preset | Kotlin | Swift | Gate | Compressor | Reverb | Use Case |
|--------|--------|-------|------|------------|--------|----------|
| Vocal Chain | `EffectsPreset.VOCAL_CHAIN` | `.vocalChain` | -45 dB | 3:1 auto-makeup | 0.25 mix | Karaoke / singing apps |
| Practice | `EffectsPreset.PRACTICE` | `.practice` | -50 dB | 2:1 | None | Learning / practice |
| Recording | `EffectsPreset.RECORDING` | `.recording` | -40 dB | 6:1 auto-makeup | 0.15 mix | Voice recording |
| Dry | `EffectsPreset.DRY` | `.dry` | None | 4:1 | None | Compression only |
| Wet | `EffectsPreset.WET` | `.wet` | None | None | 0.5 mix | Reverb only |
| Clean | `EffectsPreset.CLEAN` | `.clean` | -40 dB | None | None | Noise reduction only |

### Config Presets

Static configurations on `CalibraEffectsConfig` for common scenarios.

| Preset | Kotlin | Swift | Description |
|--------|--------|-------|-------------|
| Default | `CalibraEffectsConfig.DEFAULT` | `.default` | No effects enabled |
| Vocal Chain | `CalibraEffectsConfig.VOCAL_CHAIN` | `.vocalChain` | Noise gate + compression + reverb |
| Practice | `CalibraEffectsConfig.PRACTICE` | `.practice` | Noise gate only |
| Voice | `CalibraEffectsConfig.VOICE` | `.voice` | Noise gate + light compression |

### Builder

#### Kotlin

```kotlin
val config = CalibraEffectsConfig.Builder()
    .addNoiseGate()                              // defaults
    .addCompressor(CompressorPreset.VOCALS)      // preset
    .addReverb(mix = 0.3f, roomSize = 0.5f)      // custom params
    .build()
val effects = CalibraEffects.create(config)
```

#### Swift

```swift
let config = CalibraEffectsConfig.Builder()
    .addNoiseGate()
    .addCompressor(preset: .vocals)
    .addReverb(mix: 0.3, roomSize: 0.5)
    .build()
let effects = CalibraEffects.create(config: config)
```

### Builder Methods

| Method | Description |
|--------|-------------|
| `preset(config)` | Start from an existing `CalibraEffectsConfig` |
| `addNoiseGate()` | Add noise gate with default settings |
| `addNoiseGate(preset)` | Add noise gate with a `NoiseGatePreset` |
| `addNoiseGate(thresholdDb, holdTimeMs, timeConstMs)` | Add noise gate with custom parameters |
| `addCompressor()` | Add compressor with default settings |
| `addCompressor(preset)` | Add compressor with a `CompressorPreset` |
| `addCompressor(thresholdDb, ratio, attackMs, releaseMs, autoMakeup, makeupDb)` | Add compressor with custom parameters |
| `addReverb()` | Add reverb with default settings |
| `addReverb(preset)` | Add reverb with a `ReverbPreset` |
| `addReverb(mix, roomSize)` | Add reverb with custom parameters |
| `sampleRate(rate)` | Set audio sample rate in Hz |
| `build()` | Build the `CalibraEffectsConfig` |

### Config Properties

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `noiseGate` | `NoiseGateConfig?` | `null` | Noise gate configuration (null = disabled) |
| `compressor` | `CompressorConfig?` | `null` | Compressor configuration (null = disabled) |
| `reverb` | `ReverbConfig?` | `null` | Reverb configuration (null = disabled) |
| `sampleRate` | `Float` | `16000` | Audio sample rate in Hz |

## Effects Chain

Audio is processed in a fixed order:

```
Input Audio --> [Noise Gate] --> [Compressor] --> [Reverb] --> Output Audio
                    |                |               |
              Removes noise    Evens levels     Adds space
```

Each effect is optional. Disabled effects (null config) are skipped.

### Noise Gate

Mutes audio below a threshold to remove background noise.

#### NoiseGateConfig Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `thresholdDb` | `Float` | `-40` | Signal level below which gating occurs (dB) |
| `holdTimeMs` | `Float` | `100` | Time to hold gate open after signal drops (ms) |
| `timeConstMs` | `Float` | `10` | Envelope follower time constant (ms) |

#### NoiseGatePreset

| Preset | Kotlin | Swift | Threshold | Hold | Time Const | Description |
|--------|--------|-------|-----------|------|------------|-------------|
| Light | `NoiseGatePreset.LIGHT` | `.light` | -50 dB | 150 ms | 15 ms | Quiet environments |
| Standard | `NoiseGatePreset.STANDARD` | `.standard` | -40 dB | 100 ms | 10 ms | Typical background noise |
| Aggressive | `NoiseGatePreset.AGGRESSIVE` | `.aggressive` | -35 dB | 50 ms | 5 ms | Noisy environments |
| Tight | `NoiseGatePreset.TIGHT` | `.tight` | -30 dB | 30 ms | 3 ms | Maximum noise reduction |

### Compressor

Reduces dynamic range by attenuating loud sounds and optionally boosting quiet sounds.

#### CompressorConfig Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `thresholdDb` | `Float` | `-20` | Level above which compression begins (dB) |
| `ratio` | `Float` | `4` | Compression ratio (e.g., 4.0 = 4:1) |
| `attackMs` | `Float` | `10` | Time to reach full compression (ms) |
| `releaseMs` | `Float` | `100` | Time to release compression (ms) |
| `autoMakeup` | `Boolean` | `false` | Automatically compensate for gain reduction |
| `makeupDb` | `Float` | `0` | Manual makeup gain when autoMakeup is false (dB) |

#### CompressorPreset

| Preset | Kotlin | Swift | Threshold | Ratio | Attack | Release | Description |
|--------|--------|-------|-----------|-------|--------|---------|-------------|
| Vocals | `CompressorPreset.VOCALS` | `.vocals` | -20 dB | 3:1 | 15 ms | 150 ms | Natural vocal dynamics |
| Voice Leveler | `CompressorPreset.VOICE_LEVELER` | `.voiceLeveler` | -15 dB | 6:1 | 5 ms | 80 ms | Consistent voice volume |
| Speech | `CompressorPreset.SPEECH` | `.speech` | -25 dB | 2:1 | 20 ms | 200 ms | Gentle spoken word |
| Broadcast | `CompressorPreset.BROADCAST` | `.broadcast` | -10 dB | 10:1 | 1 ms | 50 ms | Heavy limiting |
| Transparent | `CompressorPreset.TRANSPARENT` | `.transparent` | -30 dB | 1.5:1 | 30 ms | 300 ms | Preserves dynamics |

### Reverb

Simulates natural reflections of sound in a space, adding depth and ambiance.

#### ReverbConfig Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `mix` | `Float` | `0.3` | Wet/dry mix (0.0 = dry, 1.0 = fully wet) |
| `roomSize` | `Float` | `0.5` | Simulated room size (0.0 = small, 1.0 = large) |

#### ReverbPreset

| Preset | Kotlin | Swift | Mix | Room Size | Description |
|--------|--------|-------|-----|-----------|-------------|
| Small Room | `ReverbPreset.SMALL_ROOM` | `.smallRoom` | 0.2 | 0.3 | Small practice room |
| Medium Room | `ReverbPreset.MEDIUM_ROOM` | `.mediumRoom` | 0.35 | 0.5 | Medium rehearsal space |
| Large Hall | `ReverbPreset.LARGE_HALL` | `.largeHall` | 0.5 | 0.8 | Concert hall ambiance |
| Dry | `ReverbPreset.DRY` | `.dry` | 0.1 | 0.2 | Minimal reverb |
| Cathedral | `ReverbPreset.CATHEDRAL` | `.cathedral` | 0.6 | 0.95 | Cathedral-like spaciousness |

## Real-Time Parameter Adjustment

Update effect parameters at runtime without recreating the chain.

### Kotlin

```kotlin
effects.setCompressorThreshold(-15f)
effects.setCompressorRatio(6f)
effects.setReverbMix(0.4f)
effects.setReverbRoomSize(0.6f)
effects.setNoiseGateThreshold(-45f)
effects.resetReverb()
```

### Swift

```swift
effects.setCompressorThreshold(thresholdDb: -15)
effects.setCompressorRatio(ratio: 6)
effects.setReverbMix(mix: 0.4)
effects.setReverbRoomSize(roomSize: 0.6)
effects.setNoiseGateThreshold(thresholdDb: -45)
effects.resetReverb()
```

### Runtime Methods

| Method | Parameter | Description |
|--------|-----------|-------------|
| `setCompressorThreshold(thresholdDb)` | `Float` | Update compressor threshold (dB) |
| `setCompressorRatio(ratio)` | `Float` | Update compressor ratio |
| `setReverbMix(mix)` | `Float` | Update reverb wet/dry mix |
| `setReverbRoomSize(roomSize)` | `Float` | Update reverb room size |
| `setNoiseGateThreshold(thresholdDb)` | `Float` | Update noise gate threshold (dB) |
| `resetReverb()` | None | Reset reverb state (useful when starting new audio) |

## Properties

| Property | Type | Description |
|----------|------|-------------|
| `hasNoiseGate` | `Boolean` | Whether noise gate is active in this chain |
| `hasCompressor` | `Boolean` | Whether compressor is active in this chain |
| `hasReverb` | `Boolean` | Whether reverb is active in this chain |

## Factory Methods

| Method | Description |
|--------|-------------|
| `CalibraEffects.create()` | Create with default config (`VOCAL_CHAIN`) |
| `CalibraEffects.create(config)` | Create with a `CalibraEffectsConfig` |
| `CalibraEffects.create(preset)` | Create with an `EffectsPreset` |

### Constants

| Constant | Value | Description |
|----------|-------|-------------|
| `CalibraEffects.DEFAULT_SAMPLE_RATE` | `16000f` | Default sample rate for effects processing |

## Common Patterns

### Karaoke Effects Processor

```kotlin
class KaraokeEffectsProcessor {
    private val effects = CalibraEffects.create(EffectsPreset.VOCAL_CHAIN)

    fun processAudioBuffer(samples: FloatArray) {
        effects.process(samples)
    }

    fun setReverbLevel(level: Float) {
        effects.setReverbMix(level)
    }

    fun destroy() {
        effects.release()
    }
}
```

### Dynamic Effects with User Controls

```kotlin
class EffectsController {
    private val effects: CalibraEffects

    init {
        val config = CalibraEffectsConfig.Builder()
            .addNoiseGate(NoiseGatePreset.STANDARD)
            .addCompressor(CompressorPreset.VOCALS)
            .addReverb(ReverbPreset.SMALL_ROOM)
            .build()
        effects = CalibraEffects.create(config)
    }

    // Wire to UI sliders
    fun onReverbMixChanged(value: Float) = effects.setReverbMix(value)
    fun onReverbSizeChanged(value: Float) = effects.setReverbRoomSize(value)
    fun onGateThresholdChanged(value: Float) = effects.setNoiseGateThreshold(value)
    fun onCompThresholdChanged(value: Float) = effects.setCompressorThreshold(value)

    fun release() = effects.release()
}
```

### Swift Practice Session

```swift
class PracticeSession {
    private let effects = CalibraEffects.create(preset: .practice)

    func processBuffer(_ samples: inout [Float]) {
        effects.process(samples: &samples)
    }

    func enableReverb() {
        // Note: to add reverb at runtime, create a new chain
        // Runtime methods only adjust existing effects
    }

    func cleanup() {
        effects.release()
    }
}
```

### Conditional Chain with Introspection

```kotlin
val effects = CalibraEffects.create(EffectsPreset.VOCAL_CHAIN)

if (effects.hasReverb) {
    println("Reverb is active")
    effects.setReverbMix(0.2f)
}

if (effects.hasCompressor) {
    effects.setCompressorThreshold(-18f)
}
```

## Platform Notes

- Audio must be **16kHz mono**. Use `SonixResampler` to resample if your source audio is 44.1kHz or 48kHz.
- The `process()` method modifies samples in-place for zero-allocation real-time use.

## Next Steps

- [CalibraPitch](./pitch) -- Real-time pitch detection
- [CalibraVAD](./vad) -- Voice activity detection
- [CalibraVocalRange](./vocal-range) -- Vocal range detection
