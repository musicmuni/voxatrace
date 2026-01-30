//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraBreath](index.md)/[hasEnoughData](has-enough-data.md)

# hasEnoughData

[common]\
fun [hasEnoughData](has-enough-data.md)(times: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Check if there's enough data for breath analysis.

Breath analysis requires at least 5 seconds of cumulative voiced audio to produce meaningful results.

#### Return

true if there's at least 5 seconds of voiced audio

#### Parameters

common

| | |
|---|---|
| times | Array of timestamps in seconds |
| pitchesHz | Array of pitch values in Hz (-1 for unvoiced frames) |
