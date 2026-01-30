//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraEffects](index.md)

# CalibraEffects

class [CalibraEffects](index.md)

Unified audio effects chain for real-time vocal processing.

## What Are Audio Effects?

Audio effects transform audio in real-time to improve sound quality or add character:

- 
   **Noise Gate**: Mutes audio below a threshold (removes background noise)
- 
   **Compressor**: Reduces dynamic range (makes quiet parts louder, loud parts quieter)
- 
   **Reverb**: Adds room ambiance (makes vocals sound less &quot;dry&quot;)

The effects chain processes audio in order: **NoiseGate → Compressor → Reverb**

## When to Use

| Scenario | Preset | Why |
|---|---|---|
| Karaoke/singing app | `VOCAL_CHAIN` | Full processing for professional sound |
| Practice/learning | `PRACTICE` | Minimal processing, hear your raw voice |
| Voice recording | `VOICE` | Noise gate + light compression |
| Add reverb only | Builder with `.addReverb()` | Custom chain |

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
let effects = CalibraEffects.companion.create(preset: EffectsPreset.vocalChain)

// Process audio in-place
effects.process(samples: &samples)

// Cleanup when done
effects.release()
```

## Usage Tiers (ADR-001)

### Tier 1: Presets (80% of users)

#### Kotlin

```kotlin
// Default vocal chain
val effects = CalibraEffects.create()

// Specific preset
val effects = CalibraEffects.create(EffectsPreset.PRACTICE)
```

#### Swift

```swift
// Default vocal chain
let effects = CalibraEffects.companion.create()

// Specific preset
let effects = CalibraEffects.companion.create(preset: EffectsPreset.practice)
```

### Tier 2: Config Builder (15% of users)

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
    .addCompressor(preset: CompressorPreset.vocals)
    .addReverb(mix: 0.3, roomSize: 0.5)
    .build()
let effects = CalibraEffects.companion.create(config: config)
```

### Tier 3: Runtime Adjustment (5% of users)

#### Kotlin

```kotlin
// Update parameters at runtime
effects.setCompressorThreshold(-15f)
effects.setReverbMix(0.4f)
effects.setNoiseGateThreshold(-45f)
```

## Signal Flow

```kotlin
Input Audio → [Noise Gate] → [Compressor] → [Reverb] → Output Audio
                   ↓              ↓            ↓
            Removes noise    Evens levels   Adds space
```

## Platform Notes

### iOS

- 
   Expects 16kHz mono audio (use SonixResampler if needed)
- 
   Effects use Accelerate framework for optimal performance
- 
   Process on audio thread for lowest latency

### Android

- 
   Expects 16kHz mono audio (use SonixResampler if needed)
- 
   Effects use native C++ for performance
- 
   Works with AudioTrack and Oboe

## Common Pitfalls

1. 
   **Forgetting to release**: Call `effects.release()` to free native resources
2. 
   **Wrong sample rate**: Effects expect 16kHz; resample first if needed
3. 
   **Processing stereo as mono**: Convert to mono first or process channels separately
4. 
   **Too much reverb**: Start with low mix values (0.1-0.2) and increase gradually
5. 
   **Compressor pumping**: Lower ratio (2:1) for natural sound, higher (8:1) for limiting

#### See also

| | |
|---|---|
| [EffectsPreset](../../com.musicmuni.voxatrace.calibra.model/-effects-preset/index.md) | Pre-configured effect combinations |
| [NoiseGatePreset](../../com.musicmuni.voxatrace.calibra.model/-noise-gate-preset/index.md) | Noise gate sensitivity options |
| [CompressorPreset](../../com.musicmuni.voxatrace.calibra.model/-compressor-preset/index.md) | Compressor behavior options |
| [ReverbPreset](../../com.musicmuni.voxatrace.calibra.model/-reverb-preset/index.md) | Room size and character options |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [hasCompressor](has-compressor.md) | [common]<br/>val [hasCompressor](has-compressor.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if compressor is active in this chain |
| [hasNoiseGate](has-noise-gate.md) | [common]<br/>val [hasNoiseGate](has-noise-gate.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if noise gate is active in this chain |
| [hasReverb](has-reverb.md) | [common]<br/>val [hasReverb](has-reverb.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if reverb is active in this chain |

## Functions

| Name | Summary |
|---|---|
| [process](process.md) | [common]<br/>fun [process](process.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html))<br/>Process audio samples in-place through the effects chain. Effects are applied in order: NoiseGate → Compressor → Reverb |
| [release](release.md) | [common]<br/>fun [release](release.md)()<br/>Release all native resources. Must be called when done. |
| [resetReverb](reset-reverb.md) | [common]<br/>fun [resetReverb](reset-reverb.md)()<br/>Reset reverb state (useful when starting new audio) |
| [setCompressorRatio](set-compressor-ratio.md) | [common]<br/>fun [setCompressorRatio](set-compressor-ratio.md)(ratio: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Update compressor ratio at runtime |
| [setCompressorThreshold](set-compressor-threshold.md) | [common]<br/>fun [setCompressorThreshold](set-compressor-threshold.md)(thresholdDb: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Update compressor threshold at runtime |
| [setNoiseGateThreshold](set-noise-gate-threshold.md) | [common]<br/>fun [setNoiseGateThreshold](set-noise-gate-threshold.md)(thresholdDb: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Update noise gate threshold at runtime |
| [setReverbMix](set-reverb-mix.md) | [common]<br/>fun [setReverbMix](set-reverb-mix.md)(mix: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Update reverb wet/dry mix at runtime |
| [setReverbRoomSize](set-reverb-room-size.md) | [common]<br/>fun [setReverbRoomSize](set-reverb-room-size.md)(roomSize: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Update reverb room size at runtime |
