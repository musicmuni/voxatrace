//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.player](../index.md)/[AudioPlayer](index.md)/[fadeIn](fade-in.md)

# fadeIn

[common]\
abstract suspend fun [fadeIn](fade-in.md)(targetVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f, durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) = 500)

Fades in from current volume (or 0 if stopped) to target volume.

#### Parameters

common

| | |
|---|---|
| targetVolume | Target volume to fade to (default 1.0) |
| durationMs | Fade duration in milliseconds |
