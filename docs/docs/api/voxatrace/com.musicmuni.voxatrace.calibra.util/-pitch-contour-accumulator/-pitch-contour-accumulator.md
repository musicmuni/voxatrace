//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.util](../index.md)/[PitchContourAccumulator](index.md)/[PitchContourAccumulator](-pitch-contour-accumulator.md)

# PitchContourAccumulator

[common]\
constructor(sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), hopSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), maxDurationSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 10.0f)

#### Parameters

common

| | |
|---|---|
| sampleRate | Audio sample rate (for PitchContour metadata) |
| hopSeconds | Time between pitch samples in seconds (default 10ms) |
| maxDurationSeconds | Initial max duration for the contour (default 10s) |
