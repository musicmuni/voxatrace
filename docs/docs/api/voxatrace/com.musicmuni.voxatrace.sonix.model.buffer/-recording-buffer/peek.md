//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.model.buffer](../index.md)/[RecordingBuffer](index.md)/[peek](peek.md)

# peek

[common]\
fun [peek](peek.md)(maxBytes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)

Peek at data without consuming it. Thread-safe.

#### Return

Copy of data (may be less than maxBytes if not enough data)

#### Parameters

common

| | |
|---|---|
| maxBytes | Maximum bytes to peek |
