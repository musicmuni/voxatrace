//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.model.buffer](../index.md)/[LongCircularBuffer](index.md)

# LongCircularBuffer

[common]\
class [LongCircularBuffer](index.md)(bufferLength: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))

Thread-safe circular buffer for Long values.

## Constructors

| | |
|---|---|
| [LongCircularBuffer](-long-circular-buffer.md) | [common]<br/>constructor(bufferLength: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [available](available.md) | [common]<br/>val [available](available.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [capacity](capacity.md) | [common]<br/>val [capacity](capacity.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [enqueue](enqueue.md) | [common]<br/>fun [enqueue](enqueue.md)(elem: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [getNextElem](get-next-elem.md) | [common]<br/>fun [getNextElem](get-next-elem.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)? |
| [reset](reset.md) | [common]<br/>fun [reset](reset.md)() |
