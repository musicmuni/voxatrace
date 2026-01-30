//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.model.buffer](../index.md)/[AudioBufferPitchQueue](index.md)

# AudioBufferPitchQueue

[common]\
class [AudioBufferPitchQueue](index.md)(bufferSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), queueSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), hopSizeMs: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), hopSizeBytes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), pitchFrameBytes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) : SynchronizedObject

## Constructors

| | |
|---|---|
| [AudioBufferPitchQueue](-audio-buffer-pitch-queue.md) | [common]<br/>constructor(bufferSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), queueSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), hopSizeMs: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), hopSizeBytes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), pitchFrameBytes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [finishedDataStream](finished-data-stream.md) | [common]<br/>var [finishedDataStream](finished-data-stream.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |

## Functions

| Name | Summary |
|---|---|
| [enqueue](enqueue.md) | [common]<br/>fun [enqueue](enqueue.md)(buffer: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), timeStamp: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [pollForBufferWithTime](poll-for-buffer-with-time.md) | [common]<br/>fun [pollForBufferWithTime](poll-for-buffer-with-time.md)(): [AudioBufferWithTime](../-audio-buffer-with-time/index.md)? |
| [reset](reset.md) | [common]<br/>fun [reset](reset.md)() |
