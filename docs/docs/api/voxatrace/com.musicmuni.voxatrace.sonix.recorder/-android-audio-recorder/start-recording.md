//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.recorder](../index.md)/[AndroidAudioRecorder](index.md)/[startRecording](start-recording.md)

# startRecording

[android]\
open override fun [startRecording](start-recording.md)()

Starts audio capture from the microphone.

Audio data will be emitted via [audioStream](audio-stream.md) after this call. Requires microphone permission to be granted.

#### Throws

| | |
|---|---|
| [IllegalStateException](https://developer.android.com/reference/kotlin/java/lang/IllegalStateException.html) | if already recording or not properly initialized |
