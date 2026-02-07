---
sidebar_position: 3
---

# SonixRecorder

Capture microphone input and save recordings as M4A, MP3, or WAV files with real-time audio level monitoring.

## Quick Start

### Kotlin

```kotlin
val recorder = SonixRecorder.create("/path/to/output.m4a")
recorder.start()
// ... user sings ...
recorder.stop()
recorder.release()
```

### Swift

```swift
let recorder = SonixRecorder.create(outputPath: "/path/to/output.m4a")
recorder.start()
// ... user sings ...
recorder.stop()
recorder.release()
```

## Configuration

### Presets

| Preset | Kotlin | Swift | Sample Rate | Channels | Bitrate |
|--------|--------|-------|-------------|----------|---------|
| Voice | `SonixRecorderConfig.VOICE` | `.voice` | 16 kHz | Mono | 64 kbps |
| Standard | `SonixRecorderConfig.STANDARD` | `.standard` | 44.1 kHz | Stereo | 128 kbps |
| High | `SonixRecorderConfig.HIGH` | `.high` | 48 kHz | Stereo | 256 kbps |

### Builder

#### Kotlin

```kotlin
val config = SonixRecorderConfig.Builder()
    .preset(SonixRecorderConfig.STANDARD)
    .format(AudioFormat.MP3)
    .echoCancellation(true)
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
    .onRecordingStopped { path in print("Saved to: \(path)") }
    .build()

let recorder = SonixRecorder.create(outputPath: "/path/to/output.mp3", config: config)
```

### Config Properties

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `format` | `AudioFormat` | `M4A` | Output format (M4A, MP3, WAV) |
| `sampleRate` | `Int` | `16000` | Sample rate in Hz |
| `channels` | `Int` | `1` | 1 = mono, 2 = stereo |
| `bitrate` | `Int` | `64000` | Encoder bitrate (bps) |
| `echoCancellation` | `Boolean` | `false` | Enable acoustic echo cancellation |
| `audioBufferSizeMs` | `Int` | `40` | Audio capture buffer size (ms) |

### Callbacks

| Builder Method | Signature | Description |
|---------------|-----------|-------------|
| `onRecordingStarted` | `() -> Unit` | Recording started |
| `onRecordingStopped` | `(outputPath: String) -> Unit` | Recording saved |
| `onError` | `(message: String) -> Unit` | Error occurred |
| `onLevelUpdate` | `(level: Float) -> Unit` | Audio level changed (0.0–1.0) |
| `onStateChange` | `(RecordingState) -> Unit` | State machine transition |

## Creating a Recorder

### With Output Path

```kotlin
val recorder = SonixRecorder.create(
    outputPath = "/path/to/output.m4a",
    config = SonixRecorderConfig.VOICE,
    audioSession = AudioMode.RECORDING
)
```

```swift
let recorder = SonixRecorder.create(
    outputPath: "/path/to/output.m4a",
    config: .voice,
    audioSession: .recording
)
```

### With Temporary Path

For cases where you don't need a specific output location (e.g., real-time analysis):

```kotlin
val recorder = SonixRecorder.createTemporary()
```

```swift
let recorder = SonixRecorder.createTemporary()
```

## Recording Controls

```kotlin
recorder.start()    // Begin recording
recorder.stop()     // Stop and save to file
recorder.release()  // Release all resources
```

## Recording State

The recorder follows a state machine:

```text
Idle → Starting → Recording → Stopping → Encoding → Finished
                                                   ↘ Error
```

```kotlin
sealed class RecordingState {
    object Idle
    object Starting
    data class Recording(val durationMs: Long)
    object Stopping
    object Encoding
    data class Finished(val outputPath: String?)
    data class Error(val message: String)
}
```

## Observing State

### Kotlin (StateFlow)

```kotlin
// Recording status
recorder.isRecording.collect { recording ->
    recordButton.isEnabled = !recording
    stopButton.isEnabled = recording
}

// Duration
recorder.duration.collect { durationMs ->
    timerLabel.text = formatTime(durationMs)
}

// Audio level (for VU meter)
recorder.level.collect { level ->
    vuMeter.setLevel(level)  // 0.0 to 1.0
}

// Full state machine
recorder.state.collect { state ->
    when (state) {
        is RecordingState.Recording -> showRecording(state.durationMs)
        is RecordingState.Encoding -> showEncoding()
        is RecordingState.Finished -> showDone(state.outputPath)
        is RecordingState.Error -> showError(state.message)
        else -> {}
    }
}
```

### Swift (Observers)

```swift
let recordingTask = recorder.observeIsRecording { isRecording in
    self.isRecording = isRecording
}

let durationTask = recorder.observeDuration { durationMs in
    self.durationMs = durationMs
}

let levelTask = recorder.observeLevel { level in
    self.audioLevel = level
}

// Cancel when done
recordingTask.cancel()
durationTask.cancel()
levelTask.cancel()
```

### Swift (Combine)

```swift
recorder.isRecordingPublisher
    .receive(on: DispatchQueue.main)
    .sink { self.isRecording = $0 }
    .store(in: &cancellables)

recorder.levelPublisher
    .receive(on: DispatchQueue.main)
    .sink { self.audioLevel = $0 }
    .store(in: &cancellables)
```

### StateFlows

| StateFlow | Type | Description |
|-----------|------|-------------|
| `isRecording` | `StateFlow<Boolean>` | Whether currently recording |
| `duration` | `StateFlow<Long>` | Recording duration in milliseconds |
| `level` | `StateFlow<Float>` | Audio level (0.0 = silence, 1.0 = loud) |
| `error` | `StateFlow<SonixError?>` | Error state |
| `state` | `StateFlow<RecordingState>` | Full state machine |
| `audioBuffers` | `SharedFlow<AudioBuffer>` | Raw audio buffers for real-time processing |

### Properties

| Property | Type | Description |
|----------|------|-------------|
| `actualSampleRate` | `Int` | Hardware sample rate (may differ from configured) |
| `bufferPoolAvailable` | `Int` | Buffers available in pool |
| `bufferPoolWasExhausted` | `Boolean` | Whether pool was ever exhausted |
| `latestBuffer` | `AudioBuffer?` | Most recent audio buffer |
| `synchronizedTimeMs` | `Long` | Playback-synchronized time |

## Real-time Audio Access

Access raw audio buffers for visualization or analysis:

### Kotlin

```kotlin
recorder.audioBuffers.collect { buffer ->
    val samples = buffer.toFloatArray()
    pitchDetector.detect(samples, buffer.sampleRate)
}
```

### Swift (Resampled Stream)

```swift
for await samples in recorder.audioBuffers(resampledTo: 16000) {
    pitchDetector.detect(samples: samples, sampleRate: 16000)
}
```

## Segment Recording

Record specific portions within a session (for practice apps):

```kotlin
// Enable in config
val config = SonixRecorderConfig.Builder()
    .enableSegmentRecording(outputDirectory = "/path/to/segments/")
    .build()

val recorder = SonixRecorder.create("/path/to/full.m4a", config)
recorder.start()

// Record a segment
recorder.startRecordingSegment(segmentIndex = 0)
// ... user performs section ...
val segmentPath = recorder.stopRecordingSegment(segmentIndex = 0)

// Query segments
val segments: List<SegmentInfo> = recorder.getRecordedSegments()
recorder.clearSegments()  // Clear for retry
```

## Playback Sync

Synchronize recording with a backing track for karaoke:

```kotlin
val player = SonixPlayer.create("backing-track.mp3")

val config = SonixRecorderConfig.Builder()
    .playbackSyncProvider(player.asPlaybackInfoProvider)
    .build()

val recorder = SonixRecorder.create("recording.m4a", config)

player.play()
recorder.start()

// Recording timestamps align with playback position
val syncedTime = recorder.synchronizedTimeMs
```

## Listener Interface

```kotlin
recorder.setRecordingListener(object : SonixRecorder.RecordingListener {
    override fun onRecordingStarted() { println("Started") }
    override fun onRecordingStopped(outputPath: String) { println("Saved: $outputPath") }
    override fun onStateChanged(state: RecordingState) { updateUI(state) }
    override fun onLevelUpdate(level: Float) { vuMeter.setLevel(level) }
    override fun onDurationUpdate(durationMs: Long) { updateTimer(durationMs) }
    override fun onError(message: String) { showError(message) }
    override fun onSegmentSaved(segmentIndex: Int, filePath: String) {
        println("Segment $segmentIndex saved to $filePath")
    }
})
```

## Common Patterns

### Recording ViewModel

```kotlin
class RecordingViewModel : ViewModel() {
    private var recorder: SonixRecorder? = null

    val isRecording = MutableStateFlow(false)
    val duration = MutableStateFlow(0L)
    val audioLevel = MutableStateFlow(0f)

    fun startRecording(outputPath: String) {
        val config = SonixRecorderConfig.Builder()
            .preset(SonixRecorderConfig.VOICE)
            .format(AudioFormat.MP3)
            .build()

        recorder = SonixRecorder.create(outputPath, config)

        viewModelScope.launch { recorder!!.isRecording.collect { isRecording.value = it } }
        viewModelScope.launch { recorder!!.duration.collect { duration.value = it } }
        viewModelScope.launch { recorder!!.level.collect { audioLevel.value = it } }

        recorder!!.start()
    }

    fun stopRecording() {
        recorder?.stop()
    }

    override fun onCleared() {
        recorder?.release()
    }
}
```

## Platform Notes

- **iOS**: Requires microphone permission in Info.plist. Hardware sample rate may differ from requested — check `actualSampleRate`.
- **Android**: Requires `RECORD_AUDIO` permission. Audio focus handled automatically.

## Next Steps

- [SonixPlayer](./player) — Play recorded audio
- [SonixMixer](./mixer) — Multi-track mixing with recorded audio
- [SonixEncoder](./encoder) — Encode audio to different formats
