//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra.scoring](../../index.md)/[DtwScorer](../index.md)/[DtwPath](index.md)

# DtwPath

[common]\
data class [DtwPath](index.md)(val length: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0, val px: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html) = IntArray(0), val py: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html) = IntArray(0))

DTW path structure.

## Constructors

| | |
|---|---|
| [DtwPath](-dtw-path.md) | [common]<br/>constructor(length: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0, px: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html) = IntArray(0), py: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html) = IntArray(0)) |

## Properties

| Name | Summary |
|---|---|
| [length](length.md) | [common]<br/>val [length](length.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0<br/>Length of the path |
| [px](px.md) | [common]<br/>val [px](px.md): [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html)<br/>Indices in first sequence |
| [py](py.md) | [common]<br/>val [py](py.md): [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html)<br/>Indices in second sequence |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [common]<br/>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [common]<br/>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
