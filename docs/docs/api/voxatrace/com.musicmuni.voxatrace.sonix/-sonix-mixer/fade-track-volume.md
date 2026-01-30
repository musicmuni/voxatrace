//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixMixer](index.md)/[fadeTrackVolume](fade-track-volume.md)

# fadeTrackVolume

[common]\
fun [fadeTrackVolume](fade-track-volume.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), targetVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))

Fade a track's volume smoothly from current to target.

#### Parameters

common

| | |
|---|---|
| name | Track name |
| targetVolume | Target volume (0.0 to 1.0) |
| durationMs | Transition duration in milliseconds |

[common]\
fun [fadeTrackVolume](fade-track-volume.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), startVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), endVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))

Fade a track's volume smoothly from start to end value.

#### Parameters

common

| | |
|---|---|
| name | Track name |
| startVolume | Starting volume (0.0 to 1.0) |
| endVolume | Ending volume (0.0 to 1.0) |
| durationMs | Transition duration in milliseconds |
