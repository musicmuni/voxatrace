//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra.evaluation](../../index.md)/[MelodyEvaluator](../index.md)/[Companion](index.md)/[create](create.md)

# create

[common]\
fun [create](create.md)(): [MelodyEvaluator](../index.md)

Create a MelodyEvaluator with default settings.

[common]\
fun [create](create.md)(refKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), stdKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f): [MelodyEvaluator](../index.md)

Create a MelodyEvaluator from key parameters.

#### Parameters

common

| | |
|---|---|
| refKeyHz | Reference key frequency in Hz (default: C4 = 261.63) |
| stdKeyHz | Student key frequency in Hz (0 = use reference key) |
