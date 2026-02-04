//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.internal.licensing](../index.md)/[DeviceToken](index.md)/[isExpiringSoon](is-expiring-soon.md)

# isExpiringSoon

[common]\
expect fun [isExpiringSoon](is-expiring-soon.md)(thresholdDays: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 7): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Check if token is expiring soon (within refresh threshold). SDK should proactively refresh when this returns true.

#### Return

true if token expires within threshold

#### Parameters

common

| | |
|---|---|
| thresholdDays | Days before expiry to consider &quot;expiring soon&quot; |

[android, ios]\
[android, ios]\
actual fun [isExpiringSoon](is-expiring-soon.md)(thresholdDays: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)
