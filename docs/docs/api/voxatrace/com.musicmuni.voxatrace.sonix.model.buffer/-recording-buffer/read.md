//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.model.buffer](../index.md)/[RecordingBuffer](index.md)/[read](read.md)

# read

[common]\
fun [read](read.md)(maxBytes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)

Read data from buffer. Thread-safe.

#### Return

Bytes read (may be less than maxBytes if not enough data), or empty array if no data

#### Parameters

common

| | |
|---|---|
| maxBytes | Maximum bytes to read |
