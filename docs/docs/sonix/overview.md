---
sidebar_position: 1
---

# Sonix Overview

Sonix is the audio engine module of VoxaTrace, providing:

- **Audio Playback** — Play audio files with pitch shifting and tempo control
- **Recording** — Record audio to M4A or MP3 formats
- **Multi-track Mixing** — Mix multiple audio tracks with individual controls
- **Metronome** — Programmable metronome with visual feedback
- **MIDI Synthesis** — Play MIDI notes using SoundFont instruments

## Sonix in Action

### Mixing Multiple Tracks

```text
Track 1 (Backing):  ████████████████████████████████  vol: 0.8
Track 2 (Vocals):   ░░░░████████████████░░░░░░░░░░░░  vol: 1.0
Track 3 (Guide):    ████████████████████████████████  vol: 0.3 (muted)
Track 4 (Click):    █░░░█░░░█░░░█░░░█░░░█░░░█░░░█░░░  vol: 0.5
                    ─────────────────────────────────
Output:             Mixed audio to speakers
```

### Pitch Shifting

```text
Original:     C4 ──── D4 ──── E4 ──── F4 ────
              262 Hz  294 Hz  330 Hz  349 Hz

+2 semitones: D4 ──── E4 ──── F#4 ─── G4 ────
              294 Hz  330 Hz  370 Hz  392 Hz

The melody stays the same, just in a different key.
```

### Recording Pipeline

```text
Microphone
    │
    ▼
┌─────────────────┐
│ SonixRecorder   │──── audioBuffers (real-time for visualization)
│                 │──── level (VU meter)
│ ┌─────────────┐ │
│ │ MP3 Encoder │ │──── output.mp3
│ └─────────────┘ │
└─────────────────┘
```

## Key Features

### Playback

```kotlin
val player = SonixPlayer.create("song.mp3")
player.play()
player.setPitch(2)    // +2 semitones (higher key)
player.setTempo(0.8f) // 80% speed (slower)
player.seek(30_000)   // Jump to 30 seconds
```

| Feature | Range |
| ------- | ----- |
| Formats | M4A, MP3, WAV |
| Pitch shifting | -12 to +12 semitones |
| Tempo control | 0.5x to 2x speed |
| Looping | Any region |

### Recording

```kotlin
val recorder = SonixRecorder.create(
    SonixRecorderConfig.Builder()
        .format(AudioFormat.MP3)
        .outputPath("recording.mp3")
        .build()
)
recorder.start()
// ... user sings ...
recorder.stop()
```

| Feature | Options |
| ------- | ------- |
| Output formats | M4A (AAC), MP3 |
| Sample rates | 16kHz, 44.1kHz, 48kHz |
| Echo cancellation | Built-in support |
| Background recording | Supported |

### Multi-track Mixing

Perfect for karaoke apps that need backing track + vocal guide + click track:

```kotlin
val mixer = SonixMixer.create()
mixer.loadTrack(0, "backing.mp3")
mixer.loadTrack(1, "vocal_guide.mp3")
mixer.setVolume(0, 0.8f)  // Backing at 80%
mixer.setVolume(1, 0.3f)  // Guide quiet
mixer.play()
```

| Feature | Specification |
| ------- | ------------- |
| Simultaneous tracks | Up to 8 |
| Per-track controls | Volume, pan, solo, mute |
| Synchronization | Sample-accurate |

### Metronome

```kotlin
val metronome = SonixMetronome.create(bpm = 120)
metronome.onBeat { beat, isAccent ->
    // Update UI on each beat
}
metronome.start()
```

| Feature | Range |
| ------- | ----- |
| BPM | 40 – 240 |
| Time signatures | Any |
| Custom sounds | Supported |

### MIDI Synthesis

Generate vocal guides or accompaniment from MIDI:

```kotlin
val synth = SonixMidiSynthesizer.create("piano.sf2")
synth.noteOn(channel = 0, note = 60, velocity = 100) // Middle C
synth.noteOff(channel = 0, note = 60)
```

| Feature | Specification |
| ------- | ------------- |
| SoundFont support | .sf2 files |
| Channels | 16 (MIDI standard) |
| Events | Note on/off, program change, pitch bend |

## Next Steps

- [Playing Audio Guide](../guides/playing-audio) — SonixPlayer details
- [Recording Audio Guide](../guides/recording-audio) — SonixRecorder details
- [Karaoke App Recipe](../cookbook/karaoke-app) — Full example with mixing
