//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.sonix](../../index.md)/[SonixRecorderConfig](../index.md)/[Builder](index.md)

# Builder

[common]\
class [Builder](index.md)

Builder for SonixRecorderConfig.

Builds **Config objects**, not recorder instances (ADR-001 compliant).

## Constructors

| | |
|---|---|
| [Builder](-builder.md) | [common]<br/>constructor() |

## Functions

| Name | Summary |
|---|---|
| [audioBufferSizeMs](audio-buffer-size-ms.md) | [common]<br/>fun [audioBufferSizeMs](audio-buffer-size-ms.md)(ms: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set audio capture buffer size in milliseconds |
| [bitrate](bitrate.md) | [common]<br/>fun [bitrate](bitrate.md)(bps: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set encoder bitrate in bits per second |
| [bufferPoolSize](buffer-pool-size.md) | [common]<br/>fun [bufferPoolSize](buffer-pool-size.md)(count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set number of pre-allocated buffers for zero-allocation DSP |
| [bufferSampleSize](buffer-sample-size.md) | [common]<br/>fun [bufferSampleSize](buffer-sample-size.md)(samples: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set size of each buffer in samples |
| [build](build.md) | [common]<br/>fun [build](build.md)(): [SonixRecorderConfig](../index.md)<br/>Build the immutable config |
| [channels](channels.md) | [common]<br/>fun [channels](channels.md)(count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set number of channels (1 = mono, 2 = stereo) |
| [echoCancellation](echo-cancellation.md) | [common]<br/>fun [echoCancellation](echo-cancellation.md)(enabled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)): &lt;Error class: unknown class&gt;<br/>Enable acoustic echo cancellation |
| [enableSegmentRecording](enable-segment-recording.md) | [common]<br/>fun [enableSegmentRecording](enable-segment-recording.md)(outputDirectory: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): &lt;Error class: unknown class&gt;<br/>Enable segment recording for practice apps |
| [format](format.md) | [common]<br/>fun [format](format.md)(format: [AudioFormat](../../../com.musicmuni.voxatrace.sonix.model/-audio-format/index.md)): &lt;Error class: unknown class&gt;<br/>Set audio format |
| [onError](on-error.md) | [common]<br/>fun [onError](on-error.md)(callback: (message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)): &lt;Error class: unknown class&gt;<br/>Called when an error occurs |
| [onLevelUpdate](on-level-update.md) | [common]<br/>fun [onLevelUpdate](on-level-update.md)(callback: (level: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)): &lt;Error class: unknown class&gt;<br/>Called when audio level updates |
| [onRecordingStarted](on-recording-started.md) | [common]<br/>fun [onRecordingStarted](on-recording-started.md)(callback: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)): &lt;Error class: unknown class&gt;<br/>Called when recording starts |
| [onRecordingStopped](on-recording-stopped.md) | [common]<br/>fun [onRecordingStopped](on-recording-stopped.md)(callback: (outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)): &lt;Error class: unknown class&gt;<br/>Called when recording stops with output path |
| [onStateChange](on-state-change.md) | [common]<br/>fun [onStateChange](on-state-change.md)(callback: (state: [RecordingState](../../-recording-state/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)): &lt;Error class: unknown class&gt;<br/>Called when recording state changes |
| [playbackSyncProvider](playback-sync-provider.md) | [common]<br/>fun [playbackSyncProvider](playback-sync-provider.md)(provider: [PlaybackInfoProvider](../../../com.musicmuni.voxatrace.sonix.player/-playback-info-provider/index.md)): &lt;Error class: unknown class&gt;<br/>Sync recording timeline with a playback source (backing track) |
| [preset](preset.md) | [common]<br/>fun [preset](preset.md)(config: [SonixRecorderConfig](../index.md)): &lt;Error class: unknown class&gt;<br/>Start from a preset configuration |
| [recordingBufferSizeBytes](recording-buffer-size-bytes.md) | [common]<br/>fun [recordingBufferSizeBytes](recording-buffer-size-bytes.md)(bytes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set maximum bytes for encoding buffer ring |
| [sampleRate](sample-rate.md) | [common]<br/>fun [sampleRate](sample-rate.md)(rate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): &lt;Error class: unknown class&gt;<br/>Set sample rate in Hz |
