//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../index.md)/[AudioSource](index.md)

# AudioSource

sealed class [AudioSource](index.md)

Represents the source of audio data for evaluation.

Can be a file path, URL, or raw audio samples.

#### Inheritors

| |
|---|
| [File](-file/index.md) |
| [Url](-url/index.md) |
| [Samples](-samples/index.md) |

## Types

| Name | Summary |
|---|---|
| [File](-file/index.md) | [common]<br/>data class [File](-file/index.md)(val path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) : [AudioSource](index.md)<br/>Audio from a local file path. |
| [Samples](-samples/index.md) | [common]<br/>data class [Samples](-samples/index.md)(val samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), val sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000) : [AudioSource](index.md)<br/>Raw audio samples already in memory. |
| [Url](-url/index.md) | [common]<br/>data class [Url](-url/index.md)(val url: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) : [AudioSource](index.md)<br/>Audio from a URL (for future streaming support). |
