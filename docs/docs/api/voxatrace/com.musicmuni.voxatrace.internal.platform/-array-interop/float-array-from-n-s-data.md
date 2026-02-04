//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.internal.platform](../index.md)/[ArrayInterop](index.md)/[floatArrayFromNSData](float-array-from-n-s-data.md)

# floatArrayFromNSData

[ios]\
fun [floatArrayFromNSData](float-array-from-n-s-data.md)(data: &lt;Error class: unknown class&gt;): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)

Create a FloatArray from NSData (Swift → Kotlin).

This is the primary method for fast Swift-to-Kotlin array conversion. Swift creates NSData from [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) via withUnsafeBufferPointer, then this method bulk-copies via memcpy.

#### Return

New FloatArray with copied contents

#### Parameters

ios

| | |
|---|---|
| data | NSData containing float bytes (4 bytes per float, little-endian) |
