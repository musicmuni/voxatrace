//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra.evaluation](../../index.md)/[NoteEvaluator](../index.md)/[Companion](index.md)/[create](create.md)

# create

[common]\
fun [create](create.md)(): [NoteEvaluator](../index.md)

Create a NoteEvaluator with default settings.

[common]\
fun [create](create.md)(scoreType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0, leewaySamples: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0, refKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = DEFAULT_KEY_HZ, stdKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f): [NoteEvaluator](../index.md)

Create a NoteEvaluator with specified parameters.

#### Parameters

common

| | |
|---|---|
| scoreType | Scoring algorithm type (0 = RULE_BASED_1, 1 = RULE_BASED_2) |
| leewaySamples | Leeway samples at segment boundaries |
| refKeyHz | Reference key frequency in Hz (default: C4 = 261.63) |
| stdKeyHz | Student key frequency in Hz (0 = use reference key) |
