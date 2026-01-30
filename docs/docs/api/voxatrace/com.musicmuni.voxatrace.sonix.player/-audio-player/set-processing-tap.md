//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.player](../index.md)/[AudioPlayer](index.md)/[setProcessingTap](set-processing-tap.md)

# setProcessingTap

[common]\
open fun [setProcessingTap](set-processing-tap.md)(callback: ([FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)?)

Installs a processing tap to receive and modify audio buffers during playback.

The callback receives audio as Float32 samples (-1.0 to 1.0 range). Modifications to the buffer are applied to playback output in real-time.

**Important**: The callback runs on the audio thread and must be fast. Avoid allocations, blocking operations, or heavy computation.

#### Parameters

common

| | |
|---|---|
| callback | Function that receives a mutable audio buffer (interleaved Float32).     Pass `null` to remove an existing tap. |
