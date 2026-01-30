//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.recorder](../index.md)/[AudioSession](index.md)

# AudioSession

[common]\
class [AudioSession](index.md)(config: [AudioConfig](../../com.musicmuni.voxatrace.sonix.model/-audio-config/index.md), recorder: [AudioRecorder](../-audio-recorder/index.md), enableEncoding: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, encoderBitRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 128000)

Audio recording session with optional encoding support.

This class manages audio recording and provides a stream of raw audio frames. Feature extraction (pitch detection, VAD, etc.) should be done by the consuming application using Calibra library.

Usage (basic recording):

```kotlin
val session = AudioSession(config, recorder)
session.start()

// Subscribe to audio frames for external processing (e.g., with Calibra)
session.audioFlow.collect { frame ->
    val features = calibraFeatureExtractor.extract(frame)
}

session.stop()
```

Usage (with encoding):

```kotlin
val session = AudioSession(config, recorder, enableEncoding = true)
session.start()

// Record for some time...

// Stop and save to file
val outputPath = session.stopAndSave("/path/to/recording.m4a")
```

## Constructors

| | |
|---|---|
| [AudioSession](-audio-session.md) | [common]<br/>constructor(config: [AudioConfig](../../com.musicmuni.voxatrace.sonix.model/-audio-config/index.md), recorder: [AudioRecorder](../-audio-recorder/index.md), enableEncoding: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, encoderBitRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 128000) |

## Types

| Name | Summary |
|---|---|
| [State](-state/index.md) | [common]<br/>sealed class [State](-state/index.md) |

## Properties

| Name | Summary |
|---|---|
| [audioFlow](audio-flow.md) | [common]<br/>val [audioFlow](audio-flow.md): SharedFlow&lt;[AudioBuffer](../../com.musicmuni.voxatrace.sonix.model/-audio-buffer/index.md)&gt;<br/>Raw audio frames - subscribe to process with Calibra or other DSP library. Each AudioBuffer contains PCM bytes with timestamp. |
| [isRecording](is-recording.md) | [common]<br/>val [isRecording](is-recording.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if currently recording. |
| [recordedDurationMs](recorded-duration-ms.md) | [common]<br/>val [recordedDurationMs](recorded-duration-ms.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br/>Get duration of current recording in milliseconds. |
| [state](state.md) | [common]<br/>val [state](state.md): StateFlow&lt;[AudioSession.State](-state/index.md)&gt; |
| [synchronizedTimeMs](synchronized-time-ms.md) | [common]<br/>val [synchronizedTimeMs](synchronized-time-ms.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br/>Get the playback-synchronized current time. If a PlaybackInfoProvider is set, returns the playback time. Otherwise, returns the recording duration. |

## Functions

| Name | Summary |
|---|---|
| [clearSegments](clear-segments.md) | [common]<br/>fun [clearSegments](clear-segments.md)()<br/>Clear segment data (for retry). |
| [getRecordedSegments](get-recorded-segments.md) | [common]<br/>fun [getRecordedSegments](get-recorded-segments.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[SegmentInfo](../../com.musicmuni.voxatrace.sonix.model/-segment-info/index.md)&gt;<br/>Get all recorded segments. |
| [release](release.md) | [common]<br/>fun [release](release.md)()<br/>Release all resources. After calling this, the session cannot be reused. |
| [setPlaybackInfoProvider](set-playback-info-provider.md) | [common]<br/>fun [setPlaybackInfoProvider](set-playback-info-provider.md)(provider: [PlaybackInfoProvider](../../com.musicmuni.voxatrace.sonix.player/-playback-info-provider/index.md)?)<br/>Set playback info provider for synchronization. Use this to align recording timestamps with backing track. |
| [start](start.md) | [common]<br/>suspend fun [start](start.md)()<br/>Start recording. If encoding is enabled, audio data will be buffered for later encoding. |
| [startRecordingSegment](start-recording-segment.md) | [common]<br/>fun [startRecordingSegment](start-recording-segment.md)(segmentIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))<br/>Start recording a segment within the current session. Call this when the user begins a practice attempt. |
| [stop](stop.md) | [common]<br/>fun [stop](stop.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br/>Stop recording without saving. Use this when you don't need to save the recording to a file. |
| [stopAndSave](stop-and-save.md) | [common]<br/>suspend fun [stopAndSave](stop-and-save.md)(outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)?<br/>Stop recording and save to file. Only works if enableEncoding was set to true. |
| [stopRecordingSegmentAndSaveAudio](stop-recording-segment-and-save-audio.md) | [common]<br/>suspend fun [stopRecordingSegmentAndSaveAudio](stop-recording-segment-and-save-audio.md)(segmentIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), outputDir: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)?<br/>Stop recording the current segment and save to file. |
| [syncWithPlaybackTime](sync-with-playback-time.md) | [common]<br/>fun [syncWithPlaybackTime](sync-with-playback-time.md)(playbackTimeMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Sync recorder timeline with playback position. Call this when seeking in backing track. |
