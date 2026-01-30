//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.model.buffer](../index.md)/[AudioCircularBuffer](index.md)

# AudioCircularBuffer

[common]\
class [AudioCircularBuffer](index.md)(bufferLength: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val mInputFrameSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val mHopSizeBytes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val mReqFrameBytes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val mSampleSizeBytes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val mSampRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) : SynchronizedObject

## Constructors

| | |
|---|---|
| [AudioCircularBuffer](-audio-circular-buffer.md) | [common]<br/>constructor(bufferLength: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), mInputFrameSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), mHopSizeBytes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), mReqFrameBytes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), mSampleSizeBytes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), mSampRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [mHopSizeBytes](m-hop-size-bytes.md) | [common]<br/>val [mHopSizeBytes](m-hop-size-bytes.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [mInputFrameSize](m-input-frame-size.md) | [common]<br/>val [mInputFrameSize](m-input-frame-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [mNumFramesRead](m-num-frames-read.md) | [common]<br/>val [mNumFramesRead](m-num-frames-read.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [mReqFrameBytes](m-req-frame-bytes.md) | [common]<br/>val [mReqFrameBytes](m-req-frame-bytes.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [mSampleSizeBytes](m-sample-size-bytes.md) | [common]<br/>val [mSampleSizeBytes](m-sample-size-bytes.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [mSampRate](m-samp-rate.md) | [common]<br/>val [mSampRate](m-samp-rate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [mSaveEndSample](m-save-end-sample.md) | [common]<br/>var [mSaveEndSample](m-save-end-sample.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [mSaveStartSample](m-save-start-sample.md) | [common]<br/>var [mSaveStartSample](m-save-start-sample.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [enqueue](enqueue.md) | [common]<br/>fun [enqueue](enqueue.md)(recordedFrame: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), timeStamp: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [getAudioByteData](get-audio-byte-data.md) | [common]<br/>fun [getAudioByteData](get-audio-byte-data.md)(): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)? |
| [getNextFrame](get-next-frame.md) | [common]<br/>fun [getNextFrame](get-next-frame.md)(): [AudioBuffer](../../com.musicmuni.voxatrace.sonix.model/-audio-buffer/index.md)? |
| [isEmpty](is-empty.md) | [common]<br/>fun [isEmpty](is-empty.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [reset](reset.md) | [common]<br/>fun [reset](reset.md)() |
