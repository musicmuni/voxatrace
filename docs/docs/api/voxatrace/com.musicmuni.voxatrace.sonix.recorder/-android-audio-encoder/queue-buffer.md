//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.recorder](../index.md)/[AndroidAudioEncoder](index.md)/[queueBuffer](queue-buffer.md)

# queueBuffer

[android]\
open override fun [queueBuffer](queue-buffer.md)(pcmData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html))

Queue PCM samples for encoding. Call this repeatedly as audio is recorded. Thread-safe - can be called from any thread.

#### Parameters

android

| | |
|---|---|
| pcmData | Raw PCM bytes (16-bit signed little-endian, interleaved if stereo) |
