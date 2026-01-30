//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.recorder](../index.md)/[AudioRecorder](index.md)/[startRecording](start-recording.md)

# startRecording

[common]\
abstract fun [startRecording](start-recording.md)()

Starts audio capture from the microphone.

Audio data will be emitted via [audioStream](audio-stream.md) after this call. Requires microphone permission to be granted.

#### Throws

| | |
|---|---|
| IllegalStateException | if already recording or not properly initialized |
