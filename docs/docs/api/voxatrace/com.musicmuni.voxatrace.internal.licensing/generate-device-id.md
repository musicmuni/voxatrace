//[voxatrace](../../index.md)/[com.musicmuni.voxatrace.internal.licensing](index.md)/[generateDeviceId](generate-device-id.md)

# generateDeviceId

[common]\
expect fun [generateDeviceId](generate-device-id.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)

Platform-specific device ID generation. Returns a hashed identifier for the device (SHA-256).

#### Parameters

common

| | |
|---|---|
| context | Platform context (Android Context, ignored on iOS) |

[android, ios]\
[android, ios]\
actual fun [generateDeviceId](generate-device-id.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)
