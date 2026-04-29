---
sidebar_position: 8
---

# CalibraMusic (deprecated)

:::warning Moved
`CalibraMusic` has moved to the **`common`** module as `MusicTheory`. The class on the calibra side is a `@Deprecated` shell that delegates to [`MusicTheory`](../common/music-theory) and will be removed in a future release.
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
