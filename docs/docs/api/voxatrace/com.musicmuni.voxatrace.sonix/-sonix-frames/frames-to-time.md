//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixFrames](index.md)/[framesToTime](frames-to-time.md)

# framesToTime

[common]\
fun [framesToTime](frames-to-time.md)(frame: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), hopLength: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-double/index.html)

Convert frame index to time (in seconds).

#### Return

Time in seconds

#### Parameters

common

| | |
|---|---|
| frame | Frame index |
| sampleRate | Audio sample rate (Hz) |
| hopLength | Hop length (samples) |

[common]\
fun [framesToTime](frames-to-time.md)(frames: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)&gt;, sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), hopLength: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-double/index.html)&gt;

Convert list of frame indices to times.

#### Return

List of times in seconds

#### Parameters

common

| | |
|---|---|
| frames | List of frame indices |
| sampleRate | Audio sample rate (Hz) |
| hopLength | Hop length (samples) |
