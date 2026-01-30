//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.common](../index.md)/[SignalUtils](index.md)/[cosineDistance](cosine-distance.md)

# cosineDistance

[common]\
fun [cosineDistance](cosine-distance.md)(a: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), b: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)

Compute cosine distance between two vectors.

#### Return

Cosine distance (0 = identical, 1 = orthogonal, 2 = opposite)

Special cases:

- 
   If a is all zeros, returns 0.0 (assume close)
- 
   If b is all zeros, returns 1.0 (assume far)

#### Parameters

common

| | |
|---|---|
| a | First vector |
| b | Second vector |
