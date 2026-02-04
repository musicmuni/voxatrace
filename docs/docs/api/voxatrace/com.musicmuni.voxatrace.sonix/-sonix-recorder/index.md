---
sidebar_label: "SonixRecorder"
---


# SonixRecorder

class [SonixRecorder](index.md)

Unified audio recorder for capturing microphone input.

## What is SonixRecorder?

SonixRecorder captures audio from the device microphone and saves it to a file. It provides:

- 
   **Multiple formats**: M4A (AAC), MP3, WAV
- 
   **Quality presets**: Voice (16kHz), Standard (44.1kHz), High (48kHz)
- 
   **Real-time level**: Monitor audio levels during recording
- 
   **Echo cancellation**: Remove echo when used with playback
- 
   **Segment recording**: Record specific portions for practice apps

Use it for:

- 
   **Voice recording apps**: Capture voice memos, podcasts
- 
   **Karaoke apps**: Record vocals along with backing track
- 
   **Practice apps**: Record student performances for analysis
- 
   **Audio analysis**: Feed audio to pitch detection in real-time

## Quick Start

### Kotlin

```kotlin
val recorder = SonixRecorder.create("/path/to/output.m4a")
recorder.start()
// ... record for a while ...
recorder.stop()
recorder.release()
```

### Swift

```swift
let recorder = SonixRecorder.create(outputPath: "/path/to/output.m4a")
recorder.start()
// ... record for a while ...
recorder.stop()
recorder.release()
```

## Usage Tiers (ADR-001)

### Tier 1: Zero-Config (80% of users)

#### Kotlin

```kotlin
val recorder = SonixRecorder.create("/path/to/output.m4a")
recorder.start()
// ...
recorder.stop()
```

#### Swift

```swift
let recorder = SonixRecorder.create(outputPath: "/path/to/output.m4a")
recorder.start()
// ...
recorder.stop()
```

### Tier 2: With Config (15% of users)

#### Kotlin

```kotlin
val config = SonixRecorderConfig.Builder()
    .preset(SonixRecorderConfig.STANDARD)
    .format(AudioFormat.MP3)
    .echoCancellation(true)
    .onRecordingStarted { println("Started!") }
    .onRecordingStopped { path -> println("Saved to: $path") }
    .build()
val recorder = SonixRecorder.create("/path/to/output.mp3", config)
```

#### Swift

```swift
let config = SonixRecorderConfig.Builder()
    .preset(.standard)
    .format(.mp3)
    .echoCancellation(true)
    .onRecordingStarted { print("Started!") }
    .onRecordingStopped { path in print("Saved to: \(path)") }
    .build()
let recorder = SonixRecorder.create(outputPath: "/path/to/output.mp3", config: config)
```

### Tier 3: Real-time Audio Access (5% of users)

#### Kotlin

```kotlin
// Access raw audio buffers for real-time processing
recorder.audioBuffers.collect { buffer ->
    val samples = buffer.toFloatArray()
    pitchDetector.detect(samples, buffer.sampleRate)
}
```

#### Swift

```swift
// Access raw audio buffers for real-time processing
for await buffer in recorder.audioBuffers {
    let samples = buffer.toFloatArray()
    pitchDetector.detect(samples: samples, sampleRate: buffer.sampleRate)
}
```

## Platform Notes

### iOS

- 
   Requires microphone permission in Info.plist
- 
   Audio session automatically configured for recording
- 
   Hardware sample rate may differ from requested (check `actualSampleRate`)
- 
   Echo cancellation uses iOS AEC

### Android

- 
   Requires RECORD_AUDIO permission
- 
   Audio focus handled automatically
- 
   Sample rate depends on device capabilities
- 
   Echo cancellation uses Android AEC

## Common Pitfalls

1. 
   **Forgetting to release**: Call `recorder.release()` to free resources
2. 
   **Missing permissions**: Ensure microphone permission is granted before recording
3. 
   **Wrong file extension**: Extension determines format; `.m4a` = M4A, `.mp3` = MP3
4. 
   **Not calling stop**: Recording continues until `stop()` is called
5. 
   **Ignoring actualSampleRate**: Hardware may use different rate than requested

#### See also

| | |
|---|---|
| [SonixRecorderConfig](../-sonix-recorder-config/index.md) | Configuration options for recording |
| [SonixPlayer](../-sonix-player/index.md) | For playing recorded audio |
| CalibraLiveEval | For coordinated recording with live evaluation |
| CalibraPitch | For pitch detection on recorded audio |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |
| [RecordingListener](-recording-listener/index.md) | [common]<br/>interface [RecordingListener](-recording-listener/index.md)<br/>Listener interface for recording events. Alternative to StateFlow observation and Builder callbacks. |

## Properties

| Name | Summary |
|---|---|
| [actualSampleRate](actual-sample-rate.md) | [common]<br/>val [actualSampleRate](actual-sample-rate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>The actual sample rate being used by the hardware. On iOS, this may differ from the configured rate. Use this value for encoding to ensure correct playback speed. |
| [audioBuffers](audio-buffers.md) | [common]<br/>val [audioBuffers](audio-buffers.md): SharedFlow&lt;[AudioBuffer](../../com.musicmuni.voxatrace.sonix.model/-audio-buffer/index.md)&gt;<br/>Raw audio buffer flow for advanced use cases (visualization, analysis). Each buffer contains PCM data that can be converted to floats. |
| [bufferPoolAvailable](buffer-pool-available.md) | [common]<br/>val [bufferPoolAvailable](buffer-pool-available.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of buffers currently available in the pool. Use this to monitor zero-allocation audio processing health. |
| [bufferPoolWasExhausted](buffer-pool-was-exhausted.md) | [common]<br/>val [bufferPoolWasExhausted](buffer-pool-was-exhausted.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Whether the buffer pool was ever exhausted during recording. If true, the pool size may need to be increased for optimal real-time performance. This is critical for Calibra DSP integration where allocations cause latency spikes. |
| [duration](duration.md) | [common]<br/>val [duration](duration.md): StateFlow&lt;[Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)&gt;<br/>Recording duration in milliseconds |
| [error](error.md) | [common]<br/>val [error](error.md): StateFlow&lt;[SonixError](../-sonix-error/index.md)?&gt;<br/>Error state, null if no error |
| [isRecording](is-recording.md) | [common]<br/>val [isRecording](is-recording.md): StateFlow&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt;<br/>Whether currently recording |
| [latestBuffer](latest-buffer.md) | [common]<br/>val [latestBuffer](latest-buffer.md): [AudioBuffer](../../com.musicmuni.voxatrace.sonix.model/-audio-buffer/index.md)?<br/>Most recent audio buffer for polling-style access. Prefer audioBuffers flow for reactive processing. |
| [level](level.md) | [common]<br/>val [level](level.md): StateFlow&lt;[Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)&gt;<br/>Audio level from 0.0 (silence) to 1.0 (loud) - updated in real-time |
| [state](state.md) | [common]<br/>val [state](state.md): StateFlow&lt;[RecordingState](../-recording-state/index.md)&gt;<br/>Full recording state machine |
| [synchronizedTimeMs](synchronized-time-ms.md) | [common]<br/>val [synchronizedTimeMs](synchronized-time-ms.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br/>Playback-synchronized time when using playback sync. Returns recording duration if no sync provider is set. |

## Functions

| Name | Summary |
|---|---|
| [clearSegments](clear-segments.md) | [common]<br/>fun [clearSegments](clear-segments.md)()<br/>Clear all segment data (for retry). |
| [getRecordedSegments](get-recorded-segments.md) | [common]<br/>fun [getRecordedSegments](get-recorded-segments.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[SegmentInfo](../../com.musicmuni.voxatrace.sonix.model/-segment-info/index.md)&gt;<br/>Get all recorded segments in this session. |
| [release](release.md) | [common]<br/>fun [release](release.md)()<br/>Release all resources |
| [setPlaybackSyncProvider](set-playback-sync-provider.md) | [common]<br/>fun [setPlaybackSyncProvider](set-playback-sync-provider.md)(provider: [PlaybackInfoProvider](../../com.musicmuni.voxatrace.sonix.player/-playback-info-provider/index.md)?)<br/>Set playback provider for timeline synchronization. Can be changed during recording. |
| [setRecordingListener](set-recording-listener.md) | [common]<br/>fun [setRecordingListener](set-recording-listener.md)(listener: [SonixRecorder.RecordingListener](-recording-listener/index.md)?)<br/>Set a listener for recording events. |
| [start](start.md) | [common]<br/>fun [start](start.md)()<br/>Start recording |
| [startRecordingSegment](start-recording-segment.md) | [common]<br/>fun [startRecordingSegment](start-recording-segment.md)(segmentIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))<br/>Start recording a segment within the current session. Requires enableSegmentRecording() in Builder. |
| [stop](stop.md) | [common]<br/>fun [stop](stop.md)()<br/>Stop recording and save to file |
| [stopRecordingSegment](stop-recording-segment.md) | [common]<br/>suspend fun [stopRecordingSegment](stop-recording-segment.md)(segmentIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)?<br/>Stop recording segment and save to file. |
| [syncWithPlaybackTime](sync-with-playback-time.md) | [common]<br/>fun [syncWithPlaybackTime](sync-with-playback-time.md)(playbackTimeMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Sync recording timeline with current playback position. Call after seeking in backing track. |
