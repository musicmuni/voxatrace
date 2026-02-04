//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.internal.licensing](../index.md)/[DeviceToken](index.md)/[save](save.md)

# save

[common]\
expect fun [save](save.md)(token: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), expiresAt: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?)

Save device token to secure storage.

#### Parameters

common

| | |
|---|---|
| token | The device token (dt_xxx format) |
| expiresAt | Unix timestamp when token expires (seconds) |
| context | Platform context |

[android, ios]\
[android, ios]\
actual fun [save](save.md)(token: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), expiresAt: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?)
