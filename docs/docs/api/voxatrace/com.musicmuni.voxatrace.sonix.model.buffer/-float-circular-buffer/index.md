//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.model.buffer](../index.md)/[FloatCircularBuffer](index.md)

# FloatCircularBuffer

[common]\
class [FloatCircularBuffer](index.md)(bufferLength: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))

Thread-safe circular buffer for float values. Used in audio processing pipelines.

## Constructors

| | |
|---|---|
| [FloatCircularBuffer](-float-circular-buffer.md) | [common]<br/>constructor(bufferLength: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [available](available.md) | [common]<br/>val [available](available.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [capacity](capacity.md) | [common]<br/>val [capacity](capacity.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [enqueue](enqueue.md) | [common]<br/>fun [enqueue](enqueue.md)(elem: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Enqueue a float value |
| [getNextElem](get-next-elem.md) | [common]<br/>fun [getNextElem](get-next-elem.md)(): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)?<br/>Get the next element from the queue |
| [peek](peek.md) | [common]<br/>fun [peek](peek.md)(): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)?<br/>Peek at the next element without removing it |
| [reset](reset.md) | [common]<br/>fun [reset](reset.md)() |
