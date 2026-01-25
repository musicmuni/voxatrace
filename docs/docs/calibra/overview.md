---
sidebar_position: 1
---

# Calibra Overview

Calibra is the audio analysis module of VoxaTrace, providing:

- **Pitch Detection** - Real-time fundamental frequency detection
- **Voice Activity Detection** - Detect when someone is speaking/singing
- **Vocal Range Detection** - Determine a singer's vocal range
- **Singing Evaluation** - Score singing accuracy against reference

## Key Features

### Pitch Detection

- Real-time F0 detection using neural network model
- Works with singing and speech
- Frequency range: 50Hz - 2000Hz
- Low latency (~50ms)

### Voice Activity Detection (VAD)

- Silero VAD model
- Speech vs. silence classification
- Configurable sensitivity
- Energy and neural network modes

### Vocal Range Detection

- Guided range finding
- Detects comfortable singing range
- Returns low and high notes in MIDI

### Singing Evaluation

Two evaluation modes:

**Singalong Mode**
- Evaluate while singing along with reference audio
- Real-time pitch comparison
- Note-by-note scoring

**Singafter Mode**
- Listen to phrase, then sing it back
- Pitch and rhythm evaluation
- Detailed per-phrase scoring
