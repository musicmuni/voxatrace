//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraLiveEval](index.md)/[setStudentKeyHz](set-student-key-hz.md)

# setStudentKeyHz

[common]\
fun [setStudentKeyHz](set-student-key-hz.md)(keyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))

Set student key for transposition.

Use when student sings in different key than reference. Takes effect on current segment (evaluated with new key at segment end).

#### Parameters

common

| | |
|---|---|
| keyHz | Student key in Hz (0 = same as reference) |
