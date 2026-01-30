//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra.evaluation](../../index.md)/[MelodyEvaluator](../index.md)/[Companion](index.md)

# Companion

[common]\
object [Companion](index.md)

## Functions

| Name | Summary |
|---|---|
| [create](create.md) | [common]<br/>fun [create](create.md)(): [MelodyEvaluator](../index.md)<br/>Create a MelodyEvaluator with default settings.<br/>[common]<br/>fun [create](create.md)(refKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), stdKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f): [MelodyEvaluator](../index.md)<br/>Create a MelodyEvaluator from key parameters. |
| [createWithStoredReference](create-with-stored-reference.md) | [common]<br/>fun [createWithStoredReference](create-with-stored-reference.md)(refKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), stdKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), refPitchTimes: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)?, refPitchMidi: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)?, refSegStarts: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)?, refSegEnds: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)?): [MelodyEvaluator](../index.md)<br/>Create a MelodyEvaluator with stored reference data. |
