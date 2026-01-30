//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixFrames](index.md)/[timeToFrames](time-to-frames.md)

# timeToFrames

[common]\
fun [timeToFrames](time-to-frames.md)(time: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-double/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), hopLength: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)

Convert time (in seconds) to frame index.

#### Return

Frame index (floored, non-negative)

#### Parameters

common

| | |
|---|---|
| time | Time in seconds |
| sampleRate | Audio sample rate (Hz) |
| hopLength | Hop length (samples) |

[common]\
fun [timeToFrames](time-to-frames.md)(times: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-double/index.html)&gt;, sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), hopLength: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)&gt;

Convert list of times to frame indices.

#### Return

List of frame indices

#### Parameters

common

| | |
|---|---|
| times | List of times in seconds |
| sampleRate | Audio sample rate (Hz) |
| hopLength | Hop length (samples) |
