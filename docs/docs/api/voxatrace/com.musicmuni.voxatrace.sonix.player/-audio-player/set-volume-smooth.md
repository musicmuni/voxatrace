//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.player](../index.md)/[AudioPlayer](index.md)/[setVolumeSmooth](set-volume-smooth.md)

# setVolumeSmooth

[common]\
abstract suspend fun [setVolumeSmooth](set-volume-smooth.md)(targetVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), easing: [VolumeEasing](../-volume-easing/index.md) = VolumeEasing.Linear)

Smoothly transitions volume to target value over specified duration.

Uses platform-native smooth volume transitions when available (VolumeShaper on Android O+).

#### Parameters

common

| | |
|---|---|
| targetVolume | Target volume (0.0 to 1.0) |
| durationMs | Transition duration in milliseconds |
| easing | Easing curve for the transition |
