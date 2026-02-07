---
sidebar_position: 1
---

# Calibra Overview

Calibra is the audio analysis module of VoxaTrace, providing:

- **Pitch Detection** — Real-time fundamental frequency detection
- **Voice Activity Detection** — Detect when someone is speaking or singing
- **Vocal Range Detection** — Determine a singer's comfortable range
- **Singing Evaluation** — Score singing accuracy against a reference

## Calibra in Action

### Real-Time Pitch Feedback

```text
User sings: "Hap-py birth-day to you"

Timeline:     0.0s    0.5s    1.0s    1.5s    2.0s
              ├───────┼───────┼───────┼───────┤
Reference:    C4      C4      D4      C4      F4
Detected:     C4      C4      D4      C#4     F4
              ✓       ✓       ✓       ~       ✓

Score: 4/5 notes matched = 80%
```

### Voice Activity Detection

```text
Audio input:  [noise][singing~~~~~][breath][singing~~~~~][noise]
VAD output:   [  0  ][     1      ][  0   ][     1      ][  0  ]

Action:       Skip    Process      Skip    Process       Skip
```

### Live Evaluation Flow

```text
                    ┌──────────────┐
    Reference ───── │              │
    Audio           │   Compare    │ ───── Score (0.0–1.0)
                    │   Pitches    │
    Student  ─────  │              │
    Audio           └──────────────┘
                          │
                          ▼
              ┌───────────────────────┐
              │ Segment 1: 85%        │
              │ Segment 2: 92%        │
              │ Segment 3: 78%        │
              │ ───────────────────── │
              │ Overall: 85%          │
              └───────────────────────┘
```

## Key Features

### Pitch Detection

- Real-time F0 detection with two algorithms (YIN and SwiftF0)
- Works with singing and speech
- Frequency range: 50Hz – 2000Hz
- Low latency (~50ms)

```kotlin
val detector = CalibraPitch.createDetector()
val point = detector.detect(samples, sampleRate = 16000)
// point.pitch = 440.0, point.confidence = 0.92
```

### Voice Activity Detection (VAD)

Multiple detection backends for different use cases:

| Backend | Best For | Latency |
| ------- | -------- | ------- |
| General (Energy) | Simple apps, low power | &lt;1ms |
| Speech (Silero) | Voice assistants, transcription | ~5ms |
| Singing (YAMNet) | Karaoke, singing detection | ~20ms |
| SingingRealtime | Real-time singing apps | ~5ms |

```kotlin
val vad = CalibraVAD.create(VADConfig.SINGING)
val result = vad.analyze(samples, sampleRate)
// result.isSpeaking = true, result.level = Level.MODERATE
```

### Vocal Range Detection

Guided range finding that detects a singer's comfortable range:

```kotlin
val session = VocalRangeSession.create()
session.start()
// Guide user through high and low notes
val range = session.finish()
// range.lowNote = 48 (C3), range.highNote = 72 (C5)
```

### Singing Evaluation

Two evaluation modes:

**Singalong Mode** — Evaluate while singing along with reference audio

- Real-time pitch comparison
- Note-by-note scoring
- Immediate feedback

**Singafter Mode** — Listen to phrase, then sing it back

- Pitch and rhythm evaluation
- Detailed per-phrase scoring
- Great for ear training

```kotlin
val session = CalibraLiveEval.create(lessonMaterial, config)
session.onSegmentComplete { result ->
    println("Segment ${result.segmentIndex}: ${result.score}%")
}
session.start()
```

## Next Steps

### API Reference

- [CalibraPitch](./pitch) — Real-time and batch pitch detection
- [CalibraVAD](./vad) — Voice activity detection
- [CalibraVocalRange](./vocal-range) — Vocal range detection with guided sessions
- [CalibraLiveEval](./live-eval) — Live singing evaluation with scoring
- [CalibraMelodyEval](./melody-eval) — Evaluate recorded melodies against reference
- [CalibraNoteEval](./note-eval) — Evaluate individual note accuracy
- [CalibraMusic](./music) — Musical pitch conversions (Hz, MIDI, note labels, cents)
- [CalibraBreath](./breath) — Breath capacity and control analysis
- [CalibraSpeakingPitch](./speaking-pitch) — Speaking pitch detection
- [CalibraEffects](./effects) — Real-time vocal effects chain (noise gate, compressor, reverb)
- [Utilities](./utilities) — Shared model types, error types, and configuration classes

### Guides

- [Pitch Detection Concepts](../concepts/pitch-detection) — How pitch detection works
- [Voice Activity Concepts](../concepts/voice-activity) — VAD backends explained
- [Detecting Pitch Guide](../guides/detecting-pitch) — Implementation details
- [Live Evaluation Guide](../guides/live-evaluation) — Building scoring features
