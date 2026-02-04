//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.internal.platform](../index.md)/[ArrayInterop](index.md)/[byteArrayFromNSData](byte-array-from-n-s-data.md)

# byteArrayFromNSData

[ios]\
fun [byteArrayFromNSData](byte-array-from-n-s-data.md)(data: &lt;Error class: unknown class&gt;): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)

Create a ByteArray from NSData (Swift → Kotlin).

This enables fast conversion from Swift Data to KotlinByteArray for PCM audio data, model bytes, etc.

#### Return

New ByteArray with copied contents

#### Parameters

ios

| | |
|---|---|
| data | NSData containing raw bytes |
