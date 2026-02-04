---
sidebar_label: "ArrayInterop"
---


# ArrayInterop

[ios]\
object [ArrayInterop](index.md)

High-performance array interop between Kotlin and Swift.

Uses `memcpy` for bulk copy operations instead of per-element `get/set` loops. This provides ~500-1600x faster conversion for large arrays (e.g., 2.88M audio samples).

## Performance

- 
   Per-element loop: ~50-100ms for 2.88M floats
- 
   memcpy bulk copy: ~1-2ms for 2.88M floats

## Usage from Swift (NSData bridge)

```swift
// Swift [Float] → KotlinFloatArray (via NSData)
let data = swiftArray.withUnsafeBufferPointer { buffer in
    NSData(bytes: buffer.baseAddress, length: buffer.count * 4)
}
let kotlinArray = ArrayInterop.shared.floatArrayFromNSData(data)

// KotlinFloatArray → Swift [Float] (via NSData)
let data = ArrayInterop.shared.floatArrayToNSData(kotlinArray)
let swiftArray = data.withUnsafeBytes { ... }
```

## Functions

| Name | Summary |
|---|---|
| [byteArrayFromNSData](byte-array-from-n-s-data.md) | [ios]<br/>fun [byteArrayFromNSData](byte-array-from-n-s-data.md)(data: &lt;Error class: unknown class&gt;): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br/>Create a ByteArray from NSData (Swift → Kotlin). |
| [byteArrayToNSData](byte-array-to-n-s-data.md) | [ios]<br/>fun [byteArrayToNSData](byte-array-to-n-s-data.md)(array: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)): &lt;Error class: unknown class&gt;<br/>Convert ByteArray to NSData (Kotlin → Swift). |
| [copyFloatsFromPointer](copy-floats-from-pointer.md) | [ios]<br/>fun [copyFloatsFromPointer](copy-floats-from-pointer.md)(source: &lt;Error class: unknown class&gt;&lt;&lt;Error class: unknown class&gt;&gt;, dest: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))<br/>Copy from a C pointer to FloatArray (C → Kotlin). |
| [copyFloatsToPointer](copy-floats-to-pointer.md) | [ios]<br/>fun [copyFloatsToPointer](copy-floats-to-pointer.md)(source: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), dest: &lt;Error class: unknown class&gt;&lt;&lt;Error class: unknown class&gt;&gt;, count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))<br/>Copy FloatArray contents to a C pointer (Kotlin → C). |
| [copyIntsFromPointer](copy-ints-from-pointer.md) | [ios]<br/>fun [copyIntsFromPointer](copy-ints-from-pointer.md)(source: &lt;Error class: unknown class&gt;&lt;&lt;Error class: unknown class&gt;&gt;, dest: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html), count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))<br/>Copy from a C pointer to IntArray (C → Kotlin). |
| [copyIntsToPointer](copy-ints-to-pointer.md) | [ios]<br/>fun [copyIntsToPointer](copy-ints-to-pointer.md)(source: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html), dest: &lt;Error class: unknown class&gt;&lt;&lt;Error class: unknown class&gt;&gt;, count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))<br/>Copy IntArray contents to a C pointer (Kotlin → C). |
| [floatArrayFromNSData](float-array-from-n-s-data.md) | [ios]<br/>fun [floatArrayFromNSData](float-array-from-n-s-data.md)(data: &lt;Error class: unknown class&gt;): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Create a FloatArray from NSData (Swift → Kotlin). |
| [floatArrayFromPointer](float-array-from-pointer.md) | [ios]<br/>fun [floatArrayFromPointer](float-array-from-pointer.md)(source: &lt;Error class: unknown class&gt;&lt;&lt;Error class: unknown class&gt;&gt;, count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Create a new FloatArray from a C pointer. |
| [floatArrayToNSData](float-array-to-n-s-data.md) | [ios]<br/>fun [floatArrayToNSData](float-array-to-n-s-data.md)(array: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): &lt;Error class: unknown class&gt;<br/>Convert FloatArray to NSData (Kotlin → Swift). |
| [intArrayFromNSData](int-array-from-n-s-data.md) | [ios]<br/>fun [intArrayFromNSData](int-array-from-n-s-data.md)(data: &lt;Error class: unknown class&gt;): [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html)<br/>Create an IntArray from NSData (Swift → Kotlin). |
| [intArrayFromPointer](int-array-from-pointer.md) | [ios]<br/>fun [intArrayFromPointer](int-array-from-pointer.md)(source: &lt;Error class: unknown class&gt;&lt;&lt;Error class: unknown class&gt;&gt;, count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html)<br/>Create a new IntArray from a C pointer. |
| [intArrayToNSData](int-array-to-n-s-data.md) | [ios]<br/>fun [intArrayToNSData](int-array-to-n-s-data.md)(array: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html)): &lt;Error class: unknown class&gt;<br/>Convert IntArray to NSData (Kotlin → Swift). |
