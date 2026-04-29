---
sidebar_position: 1
---

# Common Overview

Shared utilities used across all VoxaTrace modules.

| Facade | Purpose |
|--------|---------|
| [`MusicTheory`](./music-theory) | Pitch ↔ MIDI ↔ note-label ↔ cents conversions, interval generators, shruti alignment |

`MusicTheory` is consumed by [`PitchAnalysis`](../tona/pitch-analysis), [`Accura`](../accura/intonation), and demo apps. Use it directly when you need to convert between pitch representations or compute a student's practice tonic.
