//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraSpeakingPitch](index.md)

# CalibraSpeakingPitch

object [CalibraSpeakingPitch](index.md)

Natural speaking pitch detection for voice profiling.

## What is Speaking Pitch?

Speaking pitch is the **median fundamental frequency** of a person's voice when speaking naturally. It represents their &quot;home base&quot; vocal frequency.

Use it for:

- 
   **Voice profiling**: Establish user's natural pitch range
- 
   **Shruti suggestion**: Recommend musical tonic based on voice
- 
   **Voice type classification**: Soprano, tenor, bass, etc.
- 
   **Voice health tracking**: Monitor changes over time

**Note**: Speaking pitch is different from:

- 
   **Singing range**: Full high-to-low capability (use `CalibraVocalRange`)
- 
   **Shruti/tonic**: Musical reference note (calculated from range)

## When to Use

| Scenario | Use This? | Why |
|---|---|---|
| Detect natural voice pitch | Yes | Core use case |
| Classify voice type | Yes | Based on frequency range |
| Detect singing range | No | Use `CalibraVocalRange` |
| Real-time pitch display | No | Use `CalibraPitch` |

## Quick Start

### Kotlin

```kotlin
// From audio samples (16kHz mono)
val speakingPitch = CalibraSpeakingPitch.detectFromAudio(audioSamples)

if (speakingPitch 0) {
    println("Speaking pitch: $speakingPitch Hz")
    val note = CalibraMusic.hzToNoteLabel(speakingPitch)
    println("Closest note: $note")
}

// Or from existing pitch contour
val contour = pitchExtractor.extract(audio, 16000)
val speakingPitch = CalibraSpeakingPitch.detectFromPitch(contour.toPitchesArray())
```

### Swift

```swift
// From audio samples (16kHz mono)
let speakingPitch = CalibraSpeakingPitch.companion.detectFromAudio(audioMono: audioSamples)

if speakingPitch 0 {
    print("Speaking pitch: \(speakingPitch) Hz")
    let note = CalibraMusic.companion.hzToNoteLabel(frequency: speakingPitch)
    print("Closest note: \(note)")
}

// Or from existing pitch contour
let contour = pitchExtractor.extract(audio: audio, sampleRate: 16000)
let speakingPitch = CalibraSpeakingPitch.companion.detectFromPitch(pitchesHz: contour.toPitchesArray())
```

## Typical Speaking Pitches

| Voice Type | Typical Range |
|---|---|
| Bass | 85-155 Hz |
| Baritone | 110-165 Hz |
| Tenor | 130-200 Hz |
| Alto | 175-255 Hz |
| Soprano | 220-330 Hz |

## Platform Notes

### iOS/Android

- 
   Audio **must be 16kHz mono**; use SonixDecoder or SonixResampler to convert
- 
   Uses median-based detection for robustness against outliers
- 
   Returns -1 if detection fails (not enough voiced audio)

## Common Pitfalls

1. 
   **Wrong sample rate**: Audio must be 16kHz; use SonixResampler if needed
2. 
   **Singing instead of speaking**: This detects speaking pitch, not singing
3. 
   **Not enough audio**: Need several seconds of natural speech
4. 
   **Background noise**: High noise levels affect detection

#### See also

| | |
|---|---|
| [CalibraVocalRange](../-calibra-vocal-range/index.md) | For detecting singing range |
| [CalibraMusic](../-calibra-music/index.md) | For frequency-to-note conversions |

## Functions

| Name | Summary |
|---|---|
| [detectFromAudio](detect-from-audio.md) | [common]<br/>fun [detectFromAudio](detect-from-audio.md)(audioMono: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Detect natural speaking pitch from audio samples. |
| [detectFromPitch](detect-from-pitch.md) | [common]<br/>fun [detectFromPitch](detect-from-pitch.md)(pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Detect natural speaking pitch from pitch contour. |
