//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.recorder](../index.md)/[AudioSession](index.md)/[audioFlow](audio-flow.md)

# audioFlow

[common]\
val [audioFlow](audio-flow.md): SharedFlow&lt;[AudioBuffer](../../com.musicmuni.voxatrace.sonix.model/-audio-buffer/index.md)&gt;

Raw audio frames - subscribe to process with Calibra or other DSP library. Each AudioBuffer contains PCM bytes with timestamp.
