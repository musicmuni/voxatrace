//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixPlayer](index.md)/[setVolumeSmooth](set-volume-smooth.md)

# setVolumeSmooth

[common]\
suspend fun [setVolumeSmooth](set-volume-smooth.md)(targetVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), easing: [VolumeEasing](../../com.musicmuni.voxatrace.sonix.player/-volume-easing/index.md) = VolumeEasing.Linear)

Smoothly transition volume with easing curve.

#### Parameters

common

| | |
|---|---|
| targetVolume | Target volume (0.0 to 1.0) |
| durationMs | Transition duration in milliseconds |
| easing | Easing curve (default: Linear) |
