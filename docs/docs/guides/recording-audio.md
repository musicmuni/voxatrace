---
sidebar_position: 2
---

# Recording Audio

A complete guide to capturing audio with SonixRecorder.

## What You'll Learn

- Record from the microphone
- Choose formats and quality presets
- Monitor recording levels
- Access raw audio for real-time processing
- Handle segment recording for practice apps

## Prerequisites

- VoxaTrace installed
- Microphone permission configured
- Output path accessible for writing

## Quick Start

### Kotlin

```kotlin
val recorder = SonixRecorder.create("/path/to/output.m4a")
recorder.start()

// Record for a while...

recorder.stop()
recorder.release()
```

### Swift

```swift
let recorder = SonixRecorder.companion.create(outputPath: "/path/to/output.m4a")
recorder.start()

// Record for a while...

recorder.stop()
recorder.release()
```

## Permissions

### Android

Add to `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.RECORD_AUDIO" />
```

Request at runtime:

```kotlin
if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
    != PackageManager.PERMISSION_GRANTED) {
    ActivityCompat.requestPermissions(
        activity,
        arrayOf(Manifest.permission.RECORD_AUDIO),
        REQUEST_CODE
    )
}
```

### iOS

Add to `Info.plist`:

```xml
<key>NSMicrophoneUsageDescription</key>
<string>We need microphone access to record your voice.</string>
```

## Output Formats

### M4A (Default)

AAC-encoded, good compression, widely compatible.

```kotlin
val recorder = SonixRecorder.create("/path/to/output.m4a")
```

### MP3

Universal compatibility, moderate compression.

```kotlin
val config = SonixRecorderConfig.Builder()
    .format(AudioFormat.MP3)
    .build()
val recorder = SonixRecorder.create("/path/to/output.mp3", config)
```

### WAV

Uncompressed, large files, lossless quality.

```kotlin
val config = SonixRecorderConfig.Builder()
    .format(AudioFormat.WAV)
    .build()
val recorder = SonixRecorder.create("/path/to/output.wav", config)
```

### Format Detection

File extension is auto-detected:

```kotlin
// Extension determines format
SonixRecorder.create("/path/to/output.m4a")  // M4A
SonixRecorder.create("/path/to/output.mp3")  // MP3
SonixRecorder.create("/path/to/output.wav")  // WAV
```

## Quality Presets

### Voice (Default)

16kHz mono, 64kbps - optimized for speech/singing.

```kotlin
val recorder = SonixRecorder.create(path, SonixRecorderConfig.VOICE)
```

### Standard

44.1kHz stereo, 128kbps - good balance.

```kotlin
val recorder = SonixRecorder.create(path, SonixRecorderConfig.STANDARD)
```

### High

48kHz stereo, 256kbps - maximum fidelity.

```kotlin
val recorder = SonixRecorder.create(path, SonixRecorderConfig.HIGH)
```

### Custom Quality

```kotlin
val config = SonixRecorderConfig.Builder()
    .sampleRate(48000)
    .channels(2)  // Stereo
    .bitrate(192_000)
    .build()
```

## Monitoring Levels

Display a VU meter during recording:

```kotlin
recorder.level.collect { level ->
    // level is 0.0 (silence) to 1.0 (loud)
    vuMeter.value = level
}
```

Or use callbacks:

```kotlin
val config = SonixRecorderConfig.Builder()
    .onLevelUpdate { level ->
        runOnUiThread { vuMeter.value = level }
    }
    .build()
```

## Observing State

### StateFlow

```kotlin
// Recording state machine
recorder.state.collect { state ->
    when (state) {
        is RecordingState.Idle -> showIdleUI()
        is RecordingState.Starting -> showStartingUI()
        is RecordingState.Recording -> updateDuration(state.durationMs)
        is RecordingState.Stopping -> showStoppingUI()
        is RecordingState.Encoding -> showEncodingUI()
        is RecordingState.Finished -> showFinished(state.outputPath)
        is RecordingState.Error -> showError(state.message)
    }
}

// Recording flag
recorder.isRecording.collect { recording ->
    recordButton.icon = if (recording) stopIcon else recordIcon
}

// Duration
recorder.duration.collect { durationMs ->
    timerLabel.text = formatDuration(durationMs)
}
```

### Callbacks

```kotlin
val config = SonixRecorderConfig.Builder()
    .onRecordingStarted { println("Started!") }
    .onRecordingStopped { path -> println("Saved to: $path") }
    .onError { message -> showError(message) }
    .onStateChange { state -> updateUI(state) }
    .build()
```

## Real-time Audio Access

Access raw audio buffers for processing:

```kotlin
recorder.start()

recorder.audioBuffers.collect { buffer ->
    val samples = FloatArray(buffer.sampleCount)
    buffer.fillFloatSamples(samples)

    // Process samples (pitch detection, visualization, etc.)
    val pitch = detector.detect(samples, buffer.sampleRate)
    updatePitchDisplay(pitch)
}
```

## Echo Cancellation

When recording while playing audio (e.g., karaoke):

```kotlin
val config = SonixRecorderConfig.Builder()
    .echoCancellation(true)
    .build()

val recorder = SonixRecorder.create(path, config)
```

This uses platform AEC to remove the backing track from the recording.

## Playback Synchronization

Sync recording timestamps with playback position:

```kotlin
val player = SonixPlayer.create("backing-track.mp3")

val recorder = SonixRecorder.create(
    "recording.m4a",
    SonixRecorderConfig.Builder()
        .playbackSyncProvider(player.asPlaybackInfoProvider)
        .build()
)

// Timestamps in audioBuffers now align with player position
```

## Segment Recording

For practice apps that record individual segments:

```kotlin
val config = SonixRecorderConfig.Builder()
    .enableSegmentRecording("/path/to/segments/")
    .build()

val recorder = SonixRecorder.create(path, config)
recorder.start()

// Start segment 0
recorder.startRecordingSegment(0)

// ... user sings segment ...

// Stop segment and get path
val segmentPath = recorder.stopRecordingSegment(0)
println("Segment saved to: $segmentPath")

// Start segment 1
recorder.startRecordingSegment(1)
// ...

// Get all recorded segments
val segments = recorder.getRecordedSegments()
```

## Temporary Recording

When you don't need a specific output path:

```kotlin
// Auto-generates temp path
val recorder = SonixRecorder.createTemporary(SonixRecorderConfig.VOICE)
recorder.start()

// ... record ...

recorder.stop()
// File saved to temp directory
```

## Sample Rate Handling

Hardware may use a different sample rate than requested:

```kotlin
recorder.start()

// Check actual rate being used
val actualRate = recorder.actualSampleRate  // e.g., 48000 instead of 16000

// Audio buffers contain this rate
recorder.audioBuffers.collect { buffer ->
    val rate = buffer.sampleRate  // Same as actualSampleRate
}
```

VoxaTrace handles encoding at the correct rate automatically.

## Platform Considerations

### iOS

- Audio session automatically configured for recording
- Hardware may use 48kHz regardless of requested rate
- Check `actualSampleRate` for true rate

### Android

- Sample rate depends on device capabilities
- Some devices only support specific rates (44.1kHz, 48kHz)
- Echo cancellation quality varies by device

## Common Patterns

### Voice Memo Recorder

```kotlin
class VoiceMemoRecorder(private val outputDir: String) {
    private var recorder: SonixRecorder? = null

    fun startRecording(filename: String) {
        val path = "$outputDir/$filename.m4a"
        recorder = SonixRecorder.create(path, SonixRecorderConfig.VOICE)
        recorder?.start()
    }

    fun stopRecording(): String? {
        val state = recorder?.state?.value
        recorder?.stop()
        recorder?.release()
        recorder = null
        return (state as? RecordingState.Finished)?.outputPath
    }

    fun isRecording(): Boolean = recorder?.isRecording?.value == true
}
```

### Recording with Pitch Detection

```kotlin
class PitchRecorder {
    private var recorder: SonixRecorder? = null
    private var detector: CalibraPitch.Detector? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    fun start(outputPath: String, onPitch: (Float) -> Unit) {
        recorder = SonixRecorder.create(outputPath, SonixRecorderConfig.VOICE)
        detector = CalibraPitch.createDetector()

        recorder?.start()

        scope.launch {
            recorder?.audioBuffers?.collect { buffer ->
                val samples = FloatArray(buffer.sampleCount)
                buffer.fillFloatSamples(samples)

                val point = detector?.detect(samples, buffer.sampleRate)
                point?.let {
                    withContext(Dispatchers.Main) {
                        onPitch(it.pitch)
                    }
                }
            }
        }
    }

    fun stop() {
        scope.cancel()
        recorder?.stop()
        recorder?.release()
        detector?.close()
    }
}
```

## Next Steps

- [SonixRecorder API Reference](/api/sonix/SonixRecorder) - Full API documentation
- [Playing Audio Guide](/docs/guides/playing-audio) - Playback features
- [Voice Recorder Recipe](/docs/cookbook/voice-recorder) - Complete example
