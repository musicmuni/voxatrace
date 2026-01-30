//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraBreath](index.md)/[getCumulativeVoicedTime](get-cumulative-voiced-time.md)

# getCumulativeVoicedTime

[common]\
fun [getCumulativeVoicedTime](get-cumulative-voiced-time.md)(times: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), pitchesHz: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)

Get cumulative voiced time from pitch contour.

Calculates the total amount of time where pitch was detected (i.e., the singer was producing voiced sound).

#### Return

Total voiced time in seconds

#### Parameters

common

| | |
|---|---|
| times | Array of timestamps in seconds |
| pitchesHz | Array of pitch values in Hz (-1 for unvoiced frames) |
