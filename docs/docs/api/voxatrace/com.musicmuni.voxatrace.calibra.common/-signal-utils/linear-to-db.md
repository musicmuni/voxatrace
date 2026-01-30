//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.common](../index.md)/[SignalUtils](index.md)/[linearToDb](linear-to-db.md)

# linearToDb

[common]\
fun [linearToDb](linear-to-db.md)(linear: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)

Convert linear amplitude to dB.

#### Return

dB value (-120 for silence/zero, 0 for full scale)

#### Parameters

common

| | |
|---|---|
| linear | Linear amplitude (0.0 to 1.0) |
