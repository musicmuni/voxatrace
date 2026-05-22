---
sidebar_position: 1
---

# Sonix Overview

Sonix is the audio engine module of VoxaTrace, providing:

- **Audio Playback** — Play audio files with pitch shifting and tempo control
- **Recording** — Record audio to M4A, MP3, or WAV formats
- **Multi-track Mixing** — Mix multiple audio tracks with individual controls
- **Audio Utilities** — Concatenation, normalization, offline mixing, and tone generation
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
player.pitch = 2f     // +2 semitones (higher key)
player.tempo = 0.8f   // 80% speed (slower)
player.seek(30_000)   // Jump to 30 seconds
```

| Feature | Range |
| ------- | ----- |
| Formats | M4A, MP3, WAV |
| Pitch shifting | -12 to +12 semitones |
| Tempo control | 0.25× to 4× |

### Recording

```kotlin
val config = SonixRecorderConfig.Builder()
    .format(AudioFormat.MP3)
    .build()
val recorder = SonixRecorder.create("recording.mp3", config)
recorder.start()
// ... user sings ...
recorder.stop()
```

| Feature | Options |
| ------- | ------- |
| Output formats | M4A (AAC), MP3, WAV |
| Sample rates | 16 kHz (VOICE), 44.1 kHz mono (STANDARD), 44.1 kHz stereo (HIGH); custom via Builder |
| Background recording | Supported |
| Hardware-clock timing | `AudioBuffer.timestamp` is monotonic-nanos with input latency already subtracted (1.0.1+) |

Echo cancellation is exposed via `Builder.echoCancellation(true)`. As of 1.0.0, AEC is not requested on Android (unsupported in practice on most devices); on iOS it depends on the configured audio mode.

### Multi-track Mixing

For karaoke apps with backing track + vocal guide + click:

```kotlin
val mixer = SonixMixer.create(SonixMixerConfig.DEFAULT)
mixer.addTrack("backing", "backing.mp3")
mixer.addTrack("guide", "vocal_guide.mp3")
mixer.setTrackVolume("backing", 0.8f)
mixer.setTrackVolume("guide", 0.3f)
mixer.play()
```

Tracks are keyed by **string name** (not int index). Per-track controls: volume (`setTrackVolume`, `fadeTrackVolume`); tracks are added and removed by name (`addTrack`, `removeTrack`). There is no mute/solo API; set a track's volume to `0f` to silence it.

### Metronome

```kotlin
val metronome = SonixMetronome.create(
    samaSamplePath = "sama.wav",   // downbeat (first beat of the cycle)
    beatSamplePath = "beat.wav",   // regular beat
    bpm = 120f
)
metronome.currentBeat.collect { beat -> updateUI(beat) }
metronome.start()
```

| Feature | Range |
| ------- | ----- |
| BPM | 30 – 300 |
| Beats per cycle | Any positive integer (set at build time) |
| Custom sounds | Sama / beat sample paths via `SonixMetronome.Builder` |

### MIDI Synthesis

Synthesize vocal guides or accompaniment from MIDI files / pitch files:

```kotlin
val synth = SonixMidiSynthesizer.create("piano.sf2")   // sample rate set via Builder (default 44100)
val ok = synth.synthesize(midiPath = "song.mid", outputPath = "out.wav")
```

| Feature | Specification |
| ------- | ------------- |
| SoundFont support | .sf2 / .sf3 files |
| Inputs | MIDI files (`synthesize`), raw MIDI bytes (`synthesizeMidi`), MidiNote lists (`synthesizeFromNotes`), pitch files (`synthesizeFromPitchFile`) |
| Output | WAV file written to `outputPath`; every synthesis method returns `Boolean`. Synthesizer is stateless; no `noteOn`/`noteOff` event API |

## Next Steps

### API Reference

- [SonixPlayer](./player) — Audio playback with pitch shifting and tempo control
- [SonixRecorder](./recorder) — Record audio to M4A, MP3, or WAV
- [SonixMixer](./mixer) — Multi-track mixing with per-track volume
- [SonixMetronome](./metronome) — Programmable click track
- [SonixDecoder](./decoder) — Decode audio files to raw PCM
- [SonixEncoder](./encoder) — Encode PCM to M4A, MP3, or WAV
- [SonixAudioUtils](./audio-utils) — Concatenation, normalization, offline mixing
- [SonixToneGenerator](./tone-generator) — Generate sine, square, sawtooth, and triangle waveforms
- [SonixResampler](./resampler) — Sample rate conversion
- [SonixMidiSynthesizer](./midi-synthesizer) — MIDI to audio synthesis
- [SonixLessonSynthesizer](./lesson-synthesizer) — Lesson audio from note sequences
- [Utilities](./utilities) — Parser, frames, and error types

### Guides

- [Playing Audio Guide](../guides/playing-audio) — In-depth SonixPlayer guide
