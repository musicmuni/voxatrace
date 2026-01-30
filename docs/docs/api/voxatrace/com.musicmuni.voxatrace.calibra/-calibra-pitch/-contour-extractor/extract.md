//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraPitch](../index.md)/[ContourExtractor](index.md)/[extract](extract.md)

# extract

[common]\
fun [extract](extract.md)(audio: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [PitchContour](../../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md)

Extract a complete pitch contour from audio samples.

Automatically resamples to 16kHz internally if needed.

Applies artifact rejection:

- 
   Amplitude gating: frames below RMS threshold are marked unvoiced
- 
   Confidence thresholding: frames with low confidence are marked unvoiced
- 
   Outlier rejection (if enabled): isolated pitch points are marked unvoiced

#### Return

PitchContour with detected pitch points at each hop interval

#### Parameters

common

| | |
|---|---|
| audio | Audio samples as Float (-1.0 to 1.0 range) |
| sampleRate | Sample rate of the input audio |
