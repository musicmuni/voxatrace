//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.model.buffer](../index.md)/[RecordingBuffer](index.md)

# RecordingBuffer

[common]\
class [RecordingBuffer](index.md)(maxSizeBytes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 10 * 1024 * 1024) : SynchronizedObject

Thread-safe ring buffer for recording data. Optimized for single producer (recorder thread), single consumer (encoder thread).

Usage:

```kotlin
val buffer = RecordingBuffer(maxSizeBytes = 10 * 1024 * 1024) // 10 MB

// Producer: write audio data
recorder.audioStream.collect { frame ->
    buffer.write(frame.data)
}

// Consumer: read for encoding
while (buffer.availableBytes 0) {
    val chunk = buffer.read(4096)
    encoder.queueBuffer(chunk)
}
```

## Constructors

| | |
|---|---|
| [RecordingBuffer](-recording-buffer.md) | [common]<br/>constructor(maxSizeBytes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 10 * 1024 * 1024) |

## Properties

| Name | Summary |
|---|---|
| [availableBytes](available-bytes.md) | [common]<br/>val [availableBytes](available-bytes.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of bytes available to read. |
| [freeBytes](free-bytes.md) | [common]<br/>val [freeBytes](free-bytes.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Number of bytes that can be written without overwriting unread data. |
| [isEmpty](is-empty.md) | [common]<br/>val [isEmpty](is-empty.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Whether the buffer is empty. |
| [isFull](is-full.md) | [common]<br/>val [isFull](is-full.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Whether the buffer is full. |

## Functions

| Name | Summary |
|---|---|
| [clear](clear.md) | [common]<br/>fun [clear](clear.md)()<br/>Clear all data from buffer. Thread-safe. |
| [peek](peek.md) | [common]<br/>fun [peek](peek.md)(maxBytes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br/>Peek at data without consuming it. Thread-safe. |
| [read](read.md) | [common]<br/>fun [read](read.md)(maxBytes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br/>Read data from buffer. Thread-safe. |
| [readAll](read-all.md) | [common]<br/>fun [readAll](read-all.md)(): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br/>Read all available data from buffer. Thread-safe. |
| [skip](skip.md) | [common]<br/>fun [skip](skip.md)(bytes: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Skip bytes without reading them. Thread-safe. |
| [write](write.md) | [common]<br/>fun [write](write.md)(data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Write data to buffer. Thread-safe. |
