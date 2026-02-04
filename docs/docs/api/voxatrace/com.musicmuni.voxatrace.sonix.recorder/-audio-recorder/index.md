---
sidebar_label: "AudioRecorder"
---


# AudioRecorder

interface [AudioRecorder](index.md)

Interface for audio recording functionality.

AudioRecorder provides low-level audio capture from the device microphone. It emits audio data as a reactive Flow of [AudioBuffer](../../com.musicmuni.voxatrace.sonix.model/-audio-buffer/index.md) objects, enabling real-time processing without blocking.

## Usage

```kotlin
val config = AudioConfig(sampleRate = 16000, channels = 1)
val recorder = createAudioRecorder(config)

recorder.startRecording()

// Collect audio data reactively
recorder.audioStream.collect { buffer ->
    // Process audio buffer (e.g., send to Calibra for pitch detection)
}

recorder.stopRecording()
recorder.release()
```

## Zero-Allocation Real-Time Processing

For latency-critical DSP (e.g., Calibra pitch detection), use pre-allocated buffers:

```kotlin
val pool = AudioBufferPool(poolSize = 4, bufferSize = 2048)

recorder.audioStream.collect { buffer ->
    val floatBuffer = pool.acquire()
    val sampleCount = buffer.fillFloatSamples(floatBuffer)

    // Pass to Calibra for pitch detection (zero allocation in hot path)
    calibra.detectPitch(floatBuffer, sampleCount)

    pool.release(floatBuffer)
}
```

## Threading

- 
   [startRecording](start-recording.md) and [stopRecording](stop-recording.md) should be called from the main thread
- 
   [audioStream](audio-stream.md) emits on a background thread; use `withContext(Dispatchers.Main)` for UI updates

## Platform Implementation

- 
   **Android**: Uses android.media.AudioRecord with PCM 16-bit encoding
- 
   **iOS**: Uses AVAudioEngine with an input node tap

#### See also

| | |
|---|---|
| AudioSession | For higher-level recording with encoding support |
| AudioConfig | For configuration options |
| [AudioBufferPool](../../com.musicmuni.voxatrace.sonix.model.buffer/-audio-buffer-pool/index.md) | For zero-allocation buffer management |

## Properties

| Name | Summary |
|---|---|
| [actualSampleRate](actual-sample-rate.md) | [common]<br/>open val [actualSampleRate](actual-sample-rate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>The actual sample rate being used for recording. |
| [audioStream](audio-stream.md) | [common]<br/>abstract val [audioStream](audio-stream.md): Flow&lt;[AudioBuffer](../../com.musicmuni.voxatrace.sonix.model/-audio-buffer/index.md)&gt;<br/>A Flow of audio buffers captured from the microphone. |
| [latestBuffer](latest-buffer.md) | [common]<br/>open val [latestBuffer](latest-buffer.md): [AudioBuffer](../../com.musicmuni.voxatrace.sonix.model/-audio-buffer/index.md)?<br/>The most recently captured audio buffer, if available. |

## Functions

| Name | Summary |
|---|---|
| [isRecording](is-recording.md) | [common]<br/>abstract fun [isRecording](is-recording.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Returns whether audio capture is currently active. |
| [release](release.md) | [common]<br/>abstract fun [release](release.md)()<br/>Releases all resources associated with this recorder. |
| [startRecording](start-recording.md) | [common]<br/>abstract fun [startRecording](start-recording.md)()<br/>Starts audio capture from the microphone. |
| [stopRecording](stop-recording.md) | [common]<br/>abstract fun [stopRecording](stop-recording.md)()<br/>Stops audio capture. |
