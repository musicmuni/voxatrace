//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixResampler](index.md)

# SonixResampler

expect object [SonixResampler](index.md)actual object [SonixResampler](index.md)actual object [SonixResampler](index.md)

High-quality audio resampling using libsamplerate.

## What is Resampling?

Audio is recorded at a specific **sample rate** (samples per second). Different systems use different rates:

- 
   CD quality: 44100 Hz
- 
   Video: 48000 Hz
- 
   Calibra APIs: 16000 Hz

Resampling converts audio between sample rates while preserving audio quality.

## When to Use

| Scenario | Use This? | Why |
|---|---|---|
| Prepare audio for Calibra | Usually No | `SonixDecoder.decode()` resamples automatically |
| Custom audio processing | Yes | When you have raw samples at wrong rate |
| Mix audio at different rates | Yes | All tracks need same rate |

## Quick Start

### Kotlin

```kotlin
// Resample from 44100 Hz to 16000 Hz (for Calibra)
val resampled = SonixResampler.resample(
    samples = audioSamples,
    fromRate = 44100,
    toRate = 16000
)

// Now use with Calibra
val contour = pitchExtractor.extract(resampled, 16000)
```

### Swift

```swift
// Resample from 44100 Hz to 16000 Hz (for Calibra)
let resampled = SonixResampler.companion.resample(
    samples: audioSamples,
    fromRate: 44100,
    toRate: 16000
)

// Now use with Calibra
let contour = pitchExtractor.extract(audio: resampled, sampleRate: 16000)
```

## Quality

Uses **libsamplerate** (Secret Rabbit Code) which provides:

- 
   High-quality sinc interpolation
- 
   Minimal aliasing artifacts
- 
   Fast performance on mobile devices

## Platform Notes

### iOS/Android

- 
   Uses native libsamplerate library (compiled for each platform)
- 
   Processing time scales with audio length and rate ratio

## Common Pitfalls

1. 
   **Mono only**: Input must be mono; convert stereo to mono first
2. 
   **Memory usage**: Creates new array for output (doesn't modify input)
3. 
   **Usually unnecessary**: `SonixDecoder.decode()` already resamples to 16kHz

#### See also

| | |
|---|---|
| [SonixDecoder](../-sonix-decoder/index.md) | For decoding with automatic resampling |
| CalibraPitch | For pitch detection (requires 16kHz) |

## Functions

| Name | Summary |
|---|---|
| [resample](resample.md) | [common]<br/>expect fun [resample](resample.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), fromRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), toRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Resample audio from one sample rate to another.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [resample](resample.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), fromRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), toRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html) |
