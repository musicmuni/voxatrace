//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.model.buffer](../index.md)/[AudioBufferPool](index.md)/[acquire](acquire.md)

# acquire

[common]\
fun [acquire](acquire.md)(): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)

Acquire a buffer from the pool.

If no buffers are available, a new one is allocated (with a warning). For real-time use, ensure poolSize is large enough to avoid this.

#### Return

A FloatArray of bufferSize elements
