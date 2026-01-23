---
sidebar_position: 1
---

# Sonix Overview

Sonix is the audio engine module of VozOS, providing:

- **Audio Playback** - Play audio files with pitch shifting and tempo control
- **Recording** - Record audio to M4A or MP3 formats
- **Multi-track Mixing** - Mix multiple audio tracks with individual controls
- **Metronome** - Programmable metronome with visual feedback
- **MIDI Synthesis** - Play MIDI notes using SoundFont instruments

## Key Features

### Playback

- Supports M4A, MP3, WAV formats
- Real-time pitch shifting (-12 to +12 semitones)
- Tempo control (0.5x to 2x speed)
- Seek to any position
- Loop regions

### Recording

- Record from device microphone
- Output to M4A (AAC) or MP3
- Configurable sample rate and quality
- Background recording support

### Multi-track

- Up to 8 simultaneous tracks
- Individual volume and pan controls
- Solo/mute per track
- Synchronized playback

### Metronome

- BPM control (40-240)
- Time signature support
- Custom click sounds
- Beat callback for visual sync

### MIDI

- SoundFont (.sf2) support
- Note on/off events
- Program changes
- Pitch bend
