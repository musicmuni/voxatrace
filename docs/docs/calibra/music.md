---
sidebar_position: 8
---

# CalibraMusic (removed in 2.0.0)

:::danger Removed — migration required
`CalibraMusic` was **removed** in 2.0.0. There is no source-compat shell. The replacement is [`MusicTheory`](../common/music-theory) in the `common` module. 1.x callers must migrate imports before they will compile against 2.0.
:::

The new facade adds shruti-related helpers that did not exist on `CalibraMusic`:

- [`MusicTheory.alignShruti(...)`](../common/music-theory#alignshruti) — pitch-class shruti alignment with a 5-option picker (`ShrutiAlignmentResult`).
- [`MusicTheory.deriveUserShruti(...)`](../common/music-theory#deriveusershruti) — research-backed shruti derivation from NSP and vocal range (`UserShrutiDerivation`).

## Migration

```kotlin
// Before
import com.musicmuni.voxatrace.calibra.CalibraMusic
val midi = CalibraMusic.hzToMidi(440f)

// After
import com.musicmuni.voxatrace.common.MusicTheory
val midi = MusicTheory.hzToMidi(440f)
```

All conversion methods, constants (note-name lists, intervals), and interval generators carry over with identical signatures. See [MusicTheory](../common/music-theory) for the full reference.
