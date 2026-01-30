//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixRecorderConfig](index.md)

# SonixRecorderConfig

data class [SonixRecorderConfig](index.md)(val format: [AudioFormat](../../com.musicmuni.voxatrace.sonix.model/-audio-format/index.md) = AudioFormat.M4A, val sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000, val channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1, val bitrate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 64000, val playbackSyncProvider: [PlaybackInfoProvider](../../com.musicmuni.voxatrace.sonix.player/-playback-info-provider/index.md)? = null, val enableSegmentRecording: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, val segmentOutputDirectory: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null, val bufferPoolSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 4, val bufferSampleSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 2048, val recordingBufferSizeBytes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 10 * 1024 * 1024, val audioBufferSizeMs: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 40, val echoCancellation: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, val onRecordingStarted: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null, val onRecordingStopped: (outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null, val onError: (message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null, val onLevelUpdate: (level: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null, val onStateChange: ([RecordingState](../-recording-state/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null)

Configuration for SonixRecorder.

ADR-001 compliant: Builder builds Config, Factory takes Config.

## Usage Tiers

### Tier 1: Presets (80% of users)

#### Kotlin

```kotlin
val recorder = SonixRecorder.create("/path/to/output.m4a", SonixRecorderConfig.VOICE)
```

#### Swift

```swift
let recorder = SonixRecorder.companion.create(outputPath: "/path/to/output.m4a", config: SonixRecorderConfig.companion.VOICE)
```

### Tier 2: Builder (15% of users)

#### Kotlin

```kotlin
val config = SonixRecorderConfig.Builder()
    .preset(SonixRecorderConfig.HIGH)
    .format(AudioFormat.MP3)
    .echoCancellation(true)
    .build()
val recorder = SonixRecorder.create("/path/to/output.mp3", config)
```

#### Swift

```swift
let config = SonixRecorderConfig.Builder()
    .preset(config: SonixRecorderConfig.companion.HIGH)
    .format(format: AudioFormat.mp3)
    .echoCancellation(enabled: true)
    .build()
let recorder = SonixRecorder.companion.create(outputPath: "/path/to/output.mp3", config: config)
```

### Tier 3: .copy() (5% of users)

#### Kotlin

```kotlin
val config = SonixRecorderConfig.VOICE.copy(bitrate = 128000)
```

#### See also

| | |
|---|---|
| [SonixRecorder.Companion.create](../-sonix-recorder/-companion/create.md) | Factory method to create recorders |
| [SonixPlayer](../-sonix-player/index.md) | For playing recorded audio |

## Constructors

| | |
|---|---|
| [SonixRecorderConfig](-sonix-recorder-config.md) | [common]<br/>constructor(format: [AudioFormat](../../com.musicmuni.voxatrace.sonix.model/-audio-format/index.md) = AudioFormat.M4A, sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000, channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1, bitrate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 64000, playbackSyncProvider: [PlaybackInfoProvider](../../com.musicmuni.voxatrace.sonix.player/-playback-info-provider/index.md)? = null, enableSegmentRecording: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, segmentOutputDirectory: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null, bufferPoolSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 4, bufferSampleSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 2048, recordingBufferSizeBytes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 10 * 1024 * 1024, audioBufferSizeMs: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 40, echoCancellation: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, onRecordingStarted: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null, onRecordingStopped: (outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null, onError: (message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null, onLevelUpdate: (level: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null, onStateChange: ([RecordingState](../-recording-state/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null) |

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [common]<br/>class [Builder](-builder/index.md)<br/>Builder for SonixRecorderConfig. |
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [audioBufferSizeMs](audio-buffer-size-ms.md) | [common]<br/>val [audioBufferSizeMs](audio-buffer-size-ms.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 40<br/>Audio capture buffer size in milliseconds |
| [bitrate](bitrate.md) | [common]<br/>val [bitrate](bitrate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 64000<br/>Encoder bitrate in bits per second |
| [bufferPoolSize](buffer-pool-size.md) | [common]<br/>val [bufferPoolSize](buffer-pool-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 4<br/>Number of pre-allocated buffers for zero-allocation DSP |
| [bufferSampleSize](buffer-sample-size.md) | [common]<br/>val [bufferSampleSize](buffer-sample-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 2048<br/>Size of each buffer in samples |
| [channels](channels.md) | [common]<br/>val [channels](channels.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1<br/>Number of channels (1 = mono, 2 = stereo) |
| [echoCancellation](echo-cancellation.md) | [common]<br/>val [echoCancellation](echo-cancellation.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false<br/>Enable acoustic echo cancellation |
| [enableSegmentRecording](enable-segment-recording.md) | [common]<br/>val [enableSegmentRecording](enable-segment-recording.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false<br/>Enable segment recording for practice apps |
| [format](format.md) | [common]<br/>val [format](format.md): [AudioFormat](../../com.musicmuni.voxatrace.sonix.model/-audio-format/index.md)<br/>Audio format (M4A, MP3, WAV) |
| [onError](on-error.md) | [common]<br/>val [onError](on-error.md): (message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null<br/>Called when an error occurs |
| [onLevelUpdate](on-level-update.md) | [common]<br/>val [onLevelUpdate](on-level-update.md): (level: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null<br/>Called when audio level updates |
| [onRecordingStarted](on-recording-started.md) | [common]<br/>val [onRecordingStarted](on-recording-started.md): () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null<br/>Called when recording starts |
| [onRecordingStopped](on-recording-stopped.md) | [common]<br/>val [onRecordingStopped](on-recording-stopped.md): (outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null<br/>Called when recording stops with output path |
| [onStateChange](on-state-change.md) | [common]<br/>val [onStateChange](on-state-change.md): ([RecordingState](../-recording-state/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null<br/>Called when recording state changes |
| [playbackSyncProvider](playback-sync-provider.md) | [common]<br/>val [playbackSyncProvider](playback-sync-provider.md): [PlaybackInfoProvider](../../com.musicmuni.voxatrace.sonix.player/-playback-info-provider/index.md)? = null<br/>Provider for playback timeline synchronization |
| [recordingBufferSizeBytes](recording-buffer-size-bytes.md) | [common]<br/>val [recordingBufferSizeBytes](recording-buffer-size-bytes.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Maximum bytes for encoding buffer ring |
| [sampleRate](sample-rate.md) | [common]<br/>val [sampleRate](sample-rate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000<br/>Sample rate in Hz |
| [segmentOutputDirectory](segment-output-directory.md) | [common]<br/>val [segmentOutputDirectory](segment-output-directory.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null<br/>Directory for segment output files |
