//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.util](../index.md)/[AudioUtils](index.md)/[addFadeOut](add-fade-out.md)

# addFadeOut

[common]\
fun [addFadeOut](add-fade-out.md)(audioData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), startIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), endIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), type: [AudioUtils.FadeOutFunction](-fade-out-function/index.md) = FadeOutFunction.LINEAR, sampleSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 2): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

This function is used to add a fadeout to the specified range of indices of the audio data. The user can specify the type of the function to be used. This function will work for 16 bits sampled mono audio.

#### Return

if the addition of fadeout is successful or not.

#### Parameters

common

| | |
|---|---|
| audioData | the byte array containing the audio data. |
| startIndex | the start index to start the fade out. |
| endIndex | the end index to stop the fadeout. |
| type | the type of the fade out function to be used. |
| sampleSize | the size of the sample in bytes (default 2 for 16-bit). |
