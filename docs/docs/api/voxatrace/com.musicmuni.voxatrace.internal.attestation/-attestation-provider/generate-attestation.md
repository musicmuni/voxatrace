//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.internal.attestation](../index.md)/[AttestationProvider](index.md)/[generateAttestation](generate-attestation.md)

# generateAttestation

[common]\
expect fun [generateAttestation](generate-attestation.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?, challenge: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), callback: [AttestationCallback](../-attestation-callback/index.md))

Generate platform attestation.

#### Parameters

common

| | |
|---|---|
| context | Platform context |
| challenge | Server-provided challenge/nonce (base64 encoded) |
| callback | Callback with attestation result or error |

[android]\
actual fun [generateAttestation](generate-attestation.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?, challenge: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), callback: [AttestationCallback](../-attestation-callback/index.md))
