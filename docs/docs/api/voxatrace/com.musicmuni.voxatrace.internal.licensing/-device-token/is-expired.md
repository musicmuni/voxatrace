//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.internal.licensing](../index.md)/[DeviceToken](index.md)/[isExpired](is-expired.md)

# isExpired

[common]\
expect fun [isExpired](is-expired.md)(gracePeriodDays: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 7): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Check if token is completely expired (past grace period).

#### Return

true if token is past grace period

#### Parameters

common

| | |
|---|---|
| gracePeriodDays | Days of grace period after expiry |

[android, ios]\
[android, ios]\
actual fun [isExpired](is-expired.md)(gracePeriodDays: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)
