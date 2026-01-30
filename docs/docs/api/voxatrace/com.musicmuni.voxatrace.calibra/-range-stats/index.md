//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[RangeStats](index.md)

# RangeStats

[common]\
data class [RangeStats](index.md)(val totalPitchesReceived: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val validPitchesAfterFiltering: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val stableSegmentsDetected: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val longestStableSegmentSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val hasEnoughDataForRange: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html))

Statistics about accumulated range data.

## Constructors

| | |
|---|---|
| [RangeStats](-range-stats.md) | [common]<br/>constructor(totalPitchesReceived: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), validPitchesAfterFiltering: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), stableSegmentsDetected: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), longestStableSegmentSeconds: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), hasEnoughDataForRange: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [hasEnoughDataForRange](has-enough-data-for-range.md) | [common]<br/>val [hasEnoughDataForRange](has-enough-data-for-range.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [longestStableSegmentSeconds](longest-stable-segment-seconds.md) | [common]<br/>val [longestStableSegmentSeconds](longest-stable-segment-seconds.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) |
| [stableSegmentsDetected](stable-segments-detected.md) | [common]<br/>val [stableSegmentsDetected](stable-segments-detected.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [totalPitchesReceived](total-pitches-received.md) | [common]<br/>val [totalPitchesReceived](total-pitches-received.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [validPitchesAfterFiltering](valid-pitches-after-filtering.md) | [common]<br/>val [validPitchesAfterFiltering](valid-pitches-after-filtering.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
