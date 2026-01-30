//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.sonix](../../index.md)/[SonixMetronome](../index.md)/[Companion](index.md)/[create](create.md)

# create

[common]\
fun [create](create.md)(samaSamplePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), beatSamplePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), bpm: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 120.0f, beatsPerCycle: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 4): [SonixMetronome](../index.md)

Zero-config factory - creates metronome with default settings.

Usage:

```kotlin
val metronome = SonixMetronome.create(samaPath, beatPath)
metronome.start()
```

#### Return

SonixMetronome instance (loads samples asynchronously)

#### Parameters

common

| | |
|---|---|
| samaSamplePath | Path to downbeat (first beat) audio sample |
| beatSamplePath | Path to regular beat audio sample |
| bpm | Initial tempo (default: 120) |
| beatsPerCycle | Beats per cycle (default: 4) |
