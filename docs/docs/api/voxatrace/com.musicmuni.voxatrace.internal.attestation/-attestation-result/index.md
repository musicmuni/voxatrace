---
sidebar_label: "AttestationResult"
---


# AttestationResult

[common]\
data class [AttestationResult](index.md)(val platform: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val data: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt;)

Attestation result containing platform-specific data.

For Android (Play Integrity):

- 
   integrityToken: The integrity token from PlayIntegrityClient
- 
   nonce: The nonce used (echoed back)
- 
   packageName: The app's package name

For iOS (App Attest):

- 
   attestation: Base64-encoded CBOR attestation object
- 
   keyId: Base64-encoded App Attest key identifier
- 
   challenge: The challenge used (echoed back)
- 
   appId: The app's identifier (TEAMID.bundleid)

## Constructors

| | |
|---|---|
| [AttestationResult](-attestation-result.md) | [common]<br/>constructor(platform: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), data: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [data](data.md) | [common]<br/>val [data](data.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt; |
| [platform](platform.md) | [common]<br/>val [platform](platform.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
