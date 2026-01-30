//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.recorder](../index.md)/[AudioRecorder](index.md)/[latestBuffer](latest-buffer.md)

# latestBuffer

[common]\
open val [latestBuffer](latest-buffer.md): [AudioBuffer](../../com.musicmuni.voxatrace.sonix.model/-audio-buffer/index.md)?

The most recently captured audio buffer, if available.

This provides polling-style access for legacy compatibility with patterns like `audioFeaturesForNextFrame`. For new code, prefer [audioStream](audio-stream.md).

Returns `null` if no buffer has been captured yet or recording is not active.

Thread-safe for reading from any thread.
