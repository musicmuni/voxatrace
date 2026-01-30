//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.recorder](../index.md)/[AudioRecorder](index.md)/[stopRecording](stop-recording.md)

# stopRecording

[common]\
abstract fun [stopRecording](stop-recording.md)()

Stops audio capture.

After calling this, [audioStream](audio-stream.md) will stop emitting new buffers. The recorder can be restarted with [startRecording](start-recording.md).
