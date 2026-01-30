//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraPitch](../index.md)/[Detector](index.md)

# Detector

[common]\
abstract class [Detector](index.md)

Realtime pitch detector for processing audio buffers.

Handles variable buffer sizes by internally accumulating samples until it has enough for pitch detection. This is important because:

- 
   iOS delivers audio at hardware sample rate (48kHz) in ~100ms chunks
- 
   After resampling to 16kHz, buffers may be smaller than required frame size

Implementations:

- 
   YinRealtimeDetector: YIN algorithm - frame-by-frame DSP, no ML dependency
- 
   SwiftF0RealtimeDetector: SwiftF0 neural network - batch-oriented, higher accuracy

## Constructors

| | |
|---|---|
| [Detector](-detector.md) | [common]<br/>constructor() |

## Properties

| Name | Summary |
|---|---|
| [config](config.md) | [common]<br/>abstract val [config](config.md): [PitchDetectorConfig](../../../com.musicmuni.voxatrace.calibra.model/-pitch-detector-config/index.md)<br/>The configuration used to create this detector |
| [hasProcessing](has-processing.md) | [common]<br/>abstract val [hasProcessing](has-processing.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if post-processing is active |
| [latencyMs](latency-ms.md) | [common]<br/>abstract val [latencyMs](latency-ms.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Detection latency in milliseconds. |
| [livePitchContour](live-pitch-contour.md) | [common]<br/>abstract val [livePitchContour](live-pitch-contour.md): StateFlow&lt;[PitchContour](../../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md)&gt;<br/>Live pitch contour accumulated during recording. |
| [processingEnabled](processing-enabled.md) | [common]<br/>abstract var [processingEnabled](processing-enabled.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Whether post-processing is currently enabled. Set to enable/disable at runtime. |

## Functions

| Name | Summary |
|---|---|
| [clearPitchContour](clear-pitch-contour.md) | [common]<br/>abstract fun [clearPitchContour](clear-pitch-contour.md)()<br/>Clear the accumulated pitch contour. |
| [close](close.md) | [common]<br/>open fun [close](close.md)()<br/>AutoCloseable implementation - delegates to release() |
| [detect](detect.md) | [common]<br/>abstract fun [detect](detect.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [PitchPoint](../../../com.musicmuni.voxatrace.calibra.model/-pitch-point/index.md)<br/>Detect pitch from audio samples. |
| [duplicate](duplicate.md) | [common]<br/>abstract fun [duplicate](duplicate.md)(): [CalibraPitch.Detector](index.md)<br/>Create a duplicate detector with the same configuration. |
| [getAmplitude](get-amplitude.md) | [common]<br/>abstract fun [getAmplitude](get-amplitude.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Get the amplitude (RMS) of the audio samples. |
| [release](release.md) | [common]<br/>abstract fun [release](release.md)()<br/>Release all resources. Must be called when done. |
| [reset](reset.md) | [common]<br/>abstract fun [reset](reset.md)()<br/>Reset state and internal buffer. Call when starting a new audio stream. |
| [setContourMaxDuration](set-contour-max-duration.md) | [common]<br/>abstract fun [setContourMaxDuration](set-contour-max-duration.md)(seconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Set the maximum duration for the live pitch contour. |
