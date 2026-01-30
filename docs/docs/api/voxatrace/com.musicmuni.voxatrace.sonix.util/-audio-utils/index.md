//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.util](../index.md)/[AudioUtils](index.md)

# AudioUtils

[common]\
object [AudioUtils](index.md)

## Types

| Name | Summary |
|---|---|
| [FadeOutFunction](-fade-out-function/index.md) | [common]<br/>enum [FadeOutFunction](-fade-out-function/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[AudioUtils.FadeOutFunction](-fade-out-function/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [addFadeOut](add-fade-out.md) | [common]<br/>fun [addFadeOut](add-fade-out.md)(audioData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), startIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), endIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), type: [AudioUtils.FadeOutFunction](-fade-out-function/index.md) = FadeOutFunction.LINEAR, sampleSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 2): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>This function is used to add a fadeout to the specified range of indices of the audio data. The user can specify the type of the function to be used. This function will work for 16 bits sampled mono audio. |
