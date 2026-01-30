//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.recorder](../index.md)/[AndroidAudioRecorder](index.md)

# AndroidAudioRecorder

[android]\
class [AndroidAudioRecorder](index.md)(config: [AudioConfig](../../com.musicmuni.voxatrace.sonix.model/-audio-config/index.md)) : [AudioRecorder](../-audio-recorder/index.md)

## Constructors

| | |
|---|---|
| [AndroidAudioRecorder](-android-audio-recorder.md) | [android]<br/>constructor(config: [AudioConfig](../../com.musicmuni.voxatrace.sonix.model/-audio-config/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [actualSampleRate](actual-sample-rate.md) | [android]<br/>open override val [actualSampleRate](actual-sample-rate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>The actual sample rate being used for recording. |
| [audioStream](audio-stream.md) | [android]<br/>open override val [audioStream](audio-stream.md): Flow&lt;[AudioBuffer](../../com.musicmuni.voxatrace.sonix.model/-audio-buffer/index.md)&gt;<br/>A Flow of audio buffers captured from the microphone. |
| [latestBuffer](latest-buffer.md) | [android]<br/>open override val [latestBuffer](latest-buffer.md): [AudioBuffer](../../com.musicmuni.voxatrace.sonix.model/-audio-buffer/index.md)?<br/>The most recently captured audio buffer, if available. |

## Functions

| Name | Summary |
|---|---|
| [isRecording](is-recording.md) | [android]<br/>open override fun [isRecording](is-recording.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Returns whether audio capture is currently active. |
| [release](release.md) | [android]<br/>open override fun [release](release.md)()<br/>Releases all resources associated with this recorder. |
| [startRecording](start-recording.md) | [android]<br/>open override fun [startRecording](start-recording.md)()<br/>Starts audio capture from the microphone. |
| [stopRecording](stop-recording.md) | [android]<br/>open override fun [stopRecording](stop-recording.md)()<br/>Stops audio capture. |
