---
sidebar_position: 10
---

# CalibraSpeakingPitch

Natural speaking pitch detection for voice profiling. Detects the median fundamental frequency of a person's voice when speaking naturally.

## Quick Start

### Kotlin

```kotlin
// From audio samples (16kHz mono)
val speakingPitch = CalibraSpeakingPitch.detectFromAudio(audioSamples)

if (speakingPitch > 0) {
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
let speakingPitch = CalibraSpeakingPitch.detectFromAudio(audioMono: audioSamples)

if speakingPitch > 0 {
    print("Speaking pitch: \(speakingPitch) Hz")
    let note = CalibraMusic.hzToNoteLabel(speakingPitch)
    print("Closest note: \(note)")
}

// Or from existing pitch contour
let contour = pitchExtractor.extract(audio: audio, sampleRate: 16000)
let speakingPitch = CalibraSpeakingPitch.detectFromPitch(pitchesHz: contour.toPitchesArray())
```

## When to Use

| Scenario | Use This? | Why |
|----------|-----------|-----|
| Detect natural voice pitch | Yes | Core use case |
| Classify voice type | Yes | Based on frequency range |
| Suggest shruti/tonic | Yes | Recommend musical reference note |
| Voice health tracking | Yes | Monitor changes over time |
| Detect singing range | No | Use `CalibraVocalRange` |
| Real-time pitch display | No | Use `CalibraPitch` |

## Methods

### detectFromAudio

Detect natural speaking pitch from audio samples. Analyzes spoken audio to find the median fundamental frequency. Works best with natural speech samples. Automatically resamples to 16kHz internally if needed.

#### Kotlin

```kotlin
fun detectFromAudio(audioMono: FloatArray, sampleRate: Int = 16000): Float
```

#### Swift

```swift
static func detectFromAudio(audioMono: [Float], sampleRate: Int = 16000) -> Float
```

#### Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `audioMono` | `FloatArray` / `[Float]` | -- | Mono audio samples (normalized -1 to 1) |
| `sampleRate` | `Int` | `16000` | Sample rate of the input audio in Hz |

#### Return Value

Detected frequency in Hz, or `-1` if detection failed (not enough voiced audio).

#### Example

```kotlin
// Kotlin
val audioData = SonixDecoder.decode("/path/to/speech.mp3")
val samples16k = SonixResampler.resample(audioData.samples, audioData.sampleRate, 16000)
val speakingPitch = CalibraSpeakingPitch.detectFromAudio(samples16k)
```

```swift
// Swift
let audioData = SonixDecoder.decode(path: "/path/to/speech.mp3")!
let samples16k = SonixResampler.resample(samples: audioData.samples,
                                         fromRate: Int(audioData.sampleRate),
                                         toRate: 16000)
let speakingPitch = CalibraSpeakingPitch.detectFromAudio(audioMono: samples16k)
```

### detectFromPitch

Detect natural speaking pitch from an existing pitch contour. Analyzes pitch values to find the median pitch. Useful when you have already extracted pitches from audio.

#### Kotlin

```kotlin
fun detectFromPitch(pitchesHz: FloatArray): Float
```

#### Swift

```swift
static func detectFromPitch(pitchesHz: [Float]) -> Float
```

#### Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `pitchesHz` | `FloatArray` / `[Float]` | Pitch values in Hz (-1 for unvoiced frames) |

#### Return Value

Detected frequency in Hz, or `-1` if detection failed.

#### Example

```kotlin
// Kotlin
val extractor = CalibraPitch.createContourExtractor()
val contour = extractor.extract(audioSamples, sampleRate = 16000)
val speakingPitch = CalibraSpeakingPitch.detectFromPitch(contour.toPitchesArray())
extractor.release()
```

```swift
// Swift
let extractor = CalibraPitch.createContourExtractor()
let contour = extractor.extract(audio: audioSamples, sampleRate: 16000)
let speakingPitch = CalibraSpeakingPitch.detectFromPitch(pitchesHz: contour.toPitchesArray())
extractor.release()
```

## Typical Speaking Pitches

| Voice Type | Typical Range |
|------------|---------------|
| Bass | 85--155 Hz |
| Baritone | 110--165 Hz |
| Tenor | 130--200 Hz |
| Alto | 175--255 Hz |
| Soprano | 220--330 Hz |

## Common Patterns

### Voice Profile Setup

```kotlin
class VoiceProfileViewModel : ViewModel() {
    fun detectSpeakingPitch(audioPath: String) {
        viewModelScope.launch {
            val audioData = SonixDecoder.decode(audioPath) ?: return@launch
            val samples16k = SonixResampler.resample(
                audioData.samples, audioData.sampleRate, 16000
            )

            val pitchHz = CalibraSpeakingPitch.detectFromAudio(samples16k)
            if (pitchHz > 0) {
                val noteLabel = CalibraMusic.hzToNoteLabel(pitchHz)
                println("Speaking pitch: $noteLabel ($pitchHz Hz)")
            } else {
                println("Could not detect speaking pitch")
            }
        }
    }
}
```

### Reusing an Existing Pitch Contour

If you have already extracted a pitch contour (e.g., for visualization or scoring), you can pass it directly to avoid re-processing the audio:

```kotlin
val extractor = CalibraPitch.createContourExtractor()
val contour = extractor.extract(audioSamples, sampleRate = 16000)

// Use contour for visualization...
updatePitchDisplay(contour)

// Also derive speaking pitch from the same contour
val speakingPitch = CalibraSpeakingPitch.detectFromPitch(contour.toPitchesArray())
extractor.release()
```

## Platform Notes

- **iOS/Android**: Accepts any sample rate; internally resamples to 16kHz if needed.
- Uses median-based detection in MIDI space for robustness against outliers.
- Returns `-1` if detection fails (not enough voiced audio).
- Requires several seconds of natural speech for reliable results.

## Common Pitfalls

1. **Singing instead of speaking** -- This detects speaking pitch, not singing range. For singing, use `CalibraVocalRange`.
2. **Not enough audio** -- Short clips may not contain enough voiced frames. Aim for several seconds of natural speech.
3. **Background noise** -- High noise levels can affect detection accuracy. Record in a reasonably quiet environment.

## Next Steps

- [CalibraVocalRange](./vocal-range) -- Detect full singing range
- [CalibraPitch](./pitch) -- Real-time pitch detection
- [CalibraVAD](./vad) -- Voice activity detection
