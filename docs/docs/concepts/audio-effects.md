---
sidebar_position: 5
---

# Audio Effects

Audio effects transform audio in real-time to improve sound quality or add character.

## What Are Audio Effects?

Effects are signal processors that modify audio as it passes through. VoxaTrace provides three essential vocal effects:

| Effect | What It Does | When to Use |
|--------|--------------|-------------|
| **Noise Gate** | Mutes audio below a threshold | Remove background noise |
| **Compressor** | Reduces dynamic range | Even out volume differences |
| **Reverb** | Adds room ambiance | Make vocals sound richer |

## Signal Flow

Effects are applied in a specific order (called a "chain"):

```
Microphone Input
       ↓
  [Noise Gate]  ← Removes noise when not singing
       ↓
  [Compressor]  ← Evens out loud and soft parts
       ↓
    [Reverb]    ← Adds space and ambiance
       ↓
  Speaker Output
```

The order matters! A different order produces different results.

## Understanding Each Effect

### Noise Gate

A noise gate **mutes audio when it falls below a threshold**. Think of it as an automatic mute button.

```
Audio Level:    ████░░████████░░░░░░████████░░░░░░░░░░░░░░
Threshold:      ────────────────────────────────────────── (-40 dB)
Gate Output:    ████░░████████░░░░░░████████░░░░░░░░░░░░░░
                 ↑     ↑           ↑          ↑
              Opens  Open      Opens      Closed (muted)
```

Use it to:
- Remove background noise during pauses
- Clean up breath sounds between phrases
- Eliminate hum when not singing

Key parameters:
- **Threshold (dB)**: Below this level, audio is muted. -40 dB is typical.
- **Hold Time (ms)**: How long to stay open after audio drops below threshold.
- **Attack/Release (ms)**: How quickly the gate opens/closes.

### Compressor

A compressor **reduces the difference between loud and soft sounds**. It makes quiet parts louder and loud parts quieter.

```
Input:         ░░██░░░░░░░░████████████░░░░░░░░░░███░░░░░
                 ↑          ↑                    ↑
               Quiet      Loud                 Quiet

Output:        ███████░░░░░████████░░░░░░░░░░░░██████░░░░
                 ↑          ↑                    ↑
               Boosted   Reduced              Boosted
```

Use it to:
- Keep vocals at consistent volume
- Prevent clipping on loud notes
- Make softer passages audible

Key parameters:
- **Threshold (dB)**: Compression starts above this level.
- **Ratio**: How much to reduce (4:1 means 4 dB input = 1 dB output above threshold).
- **Attack (ms)**: How quickly compression kicks in.
- **Release (ms)**: How quickly compression releases.

### Reverb

Reverb **simulates the acoustic reflections of a room**. It makes audio sound like it's in a physical space.

```
Dry Signal:    ████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
                ↓
Reverb Added:  ████████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
                   ↑
              Decay/tail
```

Use it to:
- Add warmth and depth to vocals
- Make recordings sound less "dry" or clinical
- Create ambiance matching your app's style

Key parameters:
- **Mix**: Blend of dry (original) and wet (reverb) signal. 0.0 = dry, 1.0 = all reverb.
- **Room Size**: How big the virtual room is. Larger = longer decay.

## Using CalibraEffects

### Basic Usage

```kotlin
// Create with preset
val effects = CalibraEffects.create(EffectsPreset.VOCAL_CHAIN)

// Process audio (in-place)
effects.process(samples)

// Cleanup
effects.release()
```

### Presets

```kotlin
// Full vocal chain: gate + compressor + reverb
val effects = CalibraEffects.create(EffectsPreset.VOCAL_CHAIN)

// Minimal processing for practice/learning
val effects = CalibraEffects.create(EffectsPreset.PRACTICE)

// Voice recording: gate + light compression
val effects = CalibraEffects.create(EffectsPreset.VOICE)
```

### Custom Chain

Build exactly what you need using the Config Builder pattern (ADR-001 compliant):

```kotlin
val config = CalibraEffectsConfig.Builder()
    .addNoiseGate(NoiseGatePreset.LIGHT)         // Light gating
    .addCompressor(CompressorPreset.VOCALS)       // Vocal compression
    .addReverb(mix = 0.2f, roomSize = 0.4f)      // Subtle reverb
    .build()  // Returns CalibraEffectsConfig

val effects = CalibraEffects.create(config)
```

Or omit effects you don't need:

```kotlin
// Reverb only
val config = CalibraEffectsConfig.Builder()
    .addReverb(ReverbPreset.SMALL_ROOM)
    .build()
val effects = CalibraEffects.create(config)

// Gate and compressor only (no reverb)
val config = CalibraEffectsConfig.Builder()
    .addNoiseGate()
    .addCompressor()
    .build()
val effects = CalibraEffects.create(config)
```

### Runtime Adjustment

Tweak parameters during playback:

```kotlin
// User moves reverb slider
reverbSlider.onValueChanged { value ->
    effects.setReverbMix(value)  // 0.0 to 1.0
}

// User adjusts noise gate
gateSlider.onValueChanged { value ->
    effects.setNoiseGateThreshold(value)  // -60 to 0 dB
}

// User adjusts compression
compressionSlider.onValueChanged { value ->
    effects.setCompressorThreshold(value)  // -40 to 0 dB
}
```

## Integration with Recording

Process audio during recording:

```kotlin
val effects = CalibraEffects.create(EffectsPreset.VOCAL_CHAIN)

recorder.audioBuffers.collect { buffer ->
    val samples = buffer.toFloatArray()

    // Process in-place
    effects.process(samples)

    // Now samples has effects applied
    // Use for playback monitoring or save to file
}

effects.release()
```

## Integration with Playback

Use player's processing tap:

```kotlin
val effects = CalibraEffects.create(EffectsPreset.VOCAL_CHAIN)

// Install tap on player
player.setProcessingTap { samples ->
    effects.process(samples)
}

player.play()

// Later...
player.setProcessingTap(null)  // Remove tap
effects.release()
```

## Performance Considerations

| Effect | CPU Cost | Notes |
|--------|----------|-------|
| Noise Gate | Very Low | Simple threshold comparison |
| Compressor | Low | Envelope following |
| Reverb | Moderate | Multiple delay lines |

Tips:
- Use presets when possible (optimized configurations)
- Disable unused effects (don't add what you won't use)
- Process at 16kHz when possible (less samples to process)

## Common Configurations

### Karaoke App

```kotlin
// Rich reverb for karaoke feel
val config = CalibraEffectsConfig.Builder()
    .addNoiseGate(NoiseGatePreset.STANDARD)
    .addCompressor(CompressorPreset.VOCALS)
    .addReverb(mix = 0.35f, roomSize = 0.6f)  // More reverb
    .build()
val effects = CalibraEffects.create(config)
```

### Practice/Learning App

```kotlin
// Minimal processing - hear your real voice
val config = CalibraEffectsConfig.Builder()
    .addNoiseGate(NoiseGatePreset.LIGHT)  // Just reduce noise
    .build()
val effects = CalibraEffects.create(config)
```

### Voice Recording

```kotlin
// Clean, professional sound
val config = CalibraEffectsConfig.Builder()
    .addNoiseGate(NoiseGatePreset.STANDARD)
    .addCompressor(CompressorPreset.VOCALS)
    .addReverb(mix = 0.1f, roomSize = 0.3f)  // Subtle room
    .build()
val effects = CalibraEffects.create(config)
```

### Podcast Style

```kotlin
// Consistent volume, no reverb
val config = CalibraEffectsConfig.Builder()
    .addNoiseGate(NoiseGatePreset.AGGRESSIVE)
    .addCompressor(CompressorPreset.VOCALS)  // Heavy compression
    .build()
val effects = CalibraEffects.create(config)
```

## Troubleshooting

### "Audio sounds choppy"

The noise gate may be cutting off the start of words. Try:
- Lowering threshold (`setNoiseGateThreshold(-50f)`)
- Increasing hold time

### "Volume jumps around"

The compressor settings may be too aggressive. Try:
- Lower ratio (2:1 instead of 8:1)
- Longer attack time
- Longer release time

### "Too much echo"

Reduce reverb mix:
```kotlin
effects.setReverbMix(0.1f)  // Start low, increase gradually
```

### "Vocals sound harsh"

The compressor may be squashing too hard:
- Raise threshold
- Use `CompressorPreset.GENTLE`

## Next Steps

- [CalibraEffects API Reference](/api/calibra/CalibraEffects) - Full API documentation
- [Recording Audio Guide](/docs/guides/recording-audio) - Use effects with recording
- [Karaoke App Recipe](/docs/cookbook/karaoke-app) - Complete example with effects
