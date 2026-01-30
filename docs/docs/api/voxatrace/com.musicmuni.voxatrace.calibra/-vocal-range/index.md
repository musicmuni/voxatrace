//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[VocalRange](index.md)

# VocalRange

[common]\
data class [VocalRange](index.md)(val lower: [VocalPitch](../-vocal-pitch/index.md), val upper: [VocalPitch](../-vocal-pitch/index.md), val octaves: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))

Complete vocal range result with total and comfortable ranges.

## Constructors

| | |
|---|---|
| [VocalRange](-vocal-range.md) | [common]<br/>constructor(lower: [VocalPitch](../-vocal-pitch/index.md), upper: [VocalPitch](../-vocal-pitch/index.md), octaves: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [lower](lower.md) | [common]<br/>val [lower](lower.md): [VocalPitch](../-vocal-pitch/index.md)<br/>5th percentile (physiological low) |
| [octaves](octaves.md) | [common]<br/>val [octaves](octaves.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Range in octaves (e.g., 1.5) |
| [semitones](semitones.md) | [common]<br/>val [semitones](semitones.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of semitones in the range |
| [upper](upper.md) | [common]<br/>val [upper](upper.md): [VocalPitch](../-vocal-pitch/index.md)<br/>95th percentile (physiological high) |
