//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.mixer](../index.md)/[MultiTrackPlayer](index.md)/[fadeTrackVolume](fade-track-volume.md)

# fadeTrackVolume

[common]\
abstract fun [fadeTrackVolume](fade-track-volume.md)(trackName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), targetVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))

Smoothly transition a track's volume over time.

#### Parameters

common

| | |
|---|---|
| trackName | Track identifier |
| targetVolume | Target volume (0.0 to 1.0) |
| durationMs | Transition duration in milliseconds |

[common]\
abstract fun [fadeTrackVolume](fade-track-volume.md)(trackName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), startVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), endVolume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))

Smoothly transition a track's volume from start to end value.

#### Parameters

common

| | |
|---|---|
| trackName | Track identifier |
| startVolume | Starting volume (0.0 to 1.0) |
| endVolume | Ending volume (0.0 to 1.0) |
| durationMs | Transition duration in milliseconds |
