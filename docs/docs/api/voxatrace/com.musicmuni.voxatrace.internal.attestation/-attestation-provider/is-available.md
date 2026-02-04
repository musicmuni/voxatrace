//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.internal.attestation](../index.md)/[AttestationProvider](index.md)/[isAvailable](is-available.md)

# isAvailable

[common]\
expect fun [isAvailable](is-available.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Check if platform attestation is available on this device.

- 
   Android: Requires Play Store and Play Integrity API
- 
   iOS: Requires iOS 14+ and App Attest service

#### Return

true if attestation is available

#### Parameters

common

| | |
|---|---|
| context | Platform context (Android Context, null for iOS) |

[android, ios]\
[android, ios]\
actual fun [isAvailable](is-available.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)
