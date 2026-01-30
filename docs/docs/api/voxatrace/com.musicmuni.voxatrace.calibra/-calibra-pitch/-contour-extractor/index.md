//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraPitch](../index.md)/[ContourExtractor](index.md)

# ContourExtractor

[common]\
class [ContourExtractor](index.md)

Pitch contour extractor for batch processing of complete audio files.

Creates a complete pitch contour from raw audio, suitable for:

- 
   Offline melody evaluation
- 
   Pitch analysis of recorded audio
- 
   Comparing reference and student performances

The extractor is reusable - call extract() on multiple audio files with the same configuration, then release() when done.

Includes artifact rejection and correction:

- 
   **Amplitude gating**: Rejects frames below RMS threshold (configurable in dB)
- 
   **Confidence thresholding**: Rejects frames with low confidence
- 
   **Octave correction**: Fixes octave detection errors (frequency doubling/halving)
- 
   **Outlier rejection**: Rejects isolated pitch points based on continuity

## Functions

| Name | Summary |
|---|---|
| [extract](extract.md) | [common]<br/>fun [extract](extract.md)(audio: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [PitchContour](../../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md)<br/>Extract a complete pitch contour from audio samples. |
| [release](release.md) | [common]<br/>fun [release](release.md)()<br/>Release resources. Must be called when done. |
