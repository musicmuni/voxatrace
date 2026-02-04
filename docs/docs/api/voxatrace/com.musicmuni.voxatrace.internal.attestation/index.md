---
sidebar_label: "attestation"
---


# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [AttestationCallback](-attestation-callback/index.md) | [common]<br/>interface [AttestationCallback](-attestation-callback/index.md)<br/>Callback interface for attestation generation. |
| [AttestationProvider](-attestation-provider/index.md) | [common]<br/>expect class [AttestationProvider](-attestation-provider/index.md)<br/>Platform-specific app attestation for secure device authentication.<br/>[android]<br/>actual class [AttestationProvider](-attestation-provider/index.md)<br/>Android implementation using Google Play Integrity API.<br/>[ios]<br/>actual class [AttestationProvider](-attestation-provider/index.md)<br/>iOS implementation using App Attest (DCAppAttestService). |
| [AttestationResult](-attestation-result/index.md) | [common]<br/>data class [AttestationResult](-attestation-result/index.md)(val platform: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val data: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt;)<br/>Attestation result containing platform-specific data. |
