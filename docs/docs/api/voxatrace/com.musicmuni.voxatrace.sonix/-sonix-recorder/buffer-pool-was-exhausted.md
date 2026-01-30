//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixRecorder](index.md)/[bufferPoolWasExhausted](buffer-pool-was-exhausted.md)

# bufferPoolWasExhausted

[common]\
val [bufferPoolWasExhausted](buffer-pool-was-exhausted.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Whether the buffer pool was ever exhausted during recording. If true, the pool size may need to be increased for optimal real-time performance. This is critical for Calibra DSP integration where allocations cause latency spikes.
