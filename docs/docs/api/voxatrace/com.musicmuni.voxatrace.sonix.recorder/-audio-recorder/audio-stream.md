//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.recorder](../index.md)/[AudioRecorder](index.md)/[audioStream](audio-stream.md)

# audioStream

[common]\
abstract val [audioStream](audio-stream.md): Flow&lt;[AudioBuffer](../../com.musicmuni.voxatrace.sonix.model/-audio-buffer/index.md)&gt;

A Flow of audio buffers captured from the microphone.

Each [AudioBuffer](../../com.musicmuni.voxatrace.sonix.model/-audio-buffer/index.md) contains:

- 
   PCM audio data (16-bit signed integers)
- 
   Timestamp in milliseconds
- 
   Sample rate and channel information

The flow is hot and shared - multiple collectors receive the same data. Buffers are emitted at approximately 10-20ms intervals depending on the platform's audio buffer size.
