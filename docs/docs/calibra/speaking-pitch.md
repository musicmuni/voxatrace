---
sidebar_position: 10
---

# CalibraSpeakingPitch (removed in 2.0.0)

:::danger Removed — migration required
`CalibraSpeakingPitch` was **removed** in 2.0.0. There is no source-compat shell. The replacement is [`TesseraSpeakingPitch`](../tessera/speaking-pitch) — note the failure sentinel changed from `-1` to `0`. 1.x callers must migrate before they will compile against 2.0.
:::

## Where to find each piece

| Old API | New API |
|---------|---------|
| `CalibraSpeakingPitch.detectFromAudio(audio, sampleRate)` | [`TesseraSpeakingPitch.detectFromAudio(audioMono, sampleRate)`](../tessera/speaking-pitch) |
| `CalibraSpeakingPitch.detectFromPitch(pitchesHz)` | [`TesseraSpeakingPitch.detectFromPitch(pitchesHz)`](../tessera/speaking-pitch) |
| (no equivalent) | [`TesseraSpeakingPitch.detect(contour)`](../tessera/speaking-pitch) — detect from an already-extracted `PitchContour` |

The failure sentinel is `0` (not `-1`) on the canonical facade. Always check `> 0` before using the returned Hz.

## Migration

```kotlin
// Before
val hz = CalibraSpeakingPitch.detectFromAudio(samples, 16000)

// After
val hz = TesseraSpeakingPitch.detectFromAudio(samples, sampleRate = 16000)
if (hz > 0) {
    val derivation = MusicTheory.deriveUserShruti(nspHz = hz)
}
```

For full reference, see [TesseraSpeakingPitch](../tessera/speaking-pitch).
