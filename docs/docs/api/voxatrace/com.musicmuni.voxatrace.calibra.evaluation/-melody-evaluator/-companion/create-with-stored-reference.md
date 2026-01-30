//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra.evaluation](../../index.md)/[MelodyEvaluator](../index.md)/[Companion](index.md)/[createWithStoredReference](create-with-stored-reference.md)

# createWithStoredReference

[common]\
fun [createWithStoredReference](create-with-stored-reference.md)(refKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), stdKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), refPitchTimes: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)?, refPitchMidi: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)?, refSegStarts: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)?, refSegEnds: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)?): [MelodyEvaluator](../index.md)

Create a MelodyEvaluator with stored reference data.

Use this for the evaluateStudent() pattern where reference data is pre-computed and stored.

#### Parameters

common

| | |
|---|---|
| refKeyHz | Reference key frequency in Hz |
| stdKeyHz | Student key frequency in Hz (0 = use reference key) |
| refPitchTimes | Reference pitch timestamps |
| refPitchMidi | Reference pitch values in MIDI |
| refSegStarts | Segment start times |
| refSegEnds | Segment end times |
