---
sidebar_label: "Companion"
---


# Companion

[common]\
object [Companion](index.md)

## Types

| Name | Summary |
|---|---|
| [InitCallback](-init-callback/index.md) | [common]<br/>interface [InitCallback](-init-callback/index.md)<br/>Callback for async attestation-based initialization. |

## Properties

| Name | Summary |
|---|---|
| [isInitialized](is-initialized.md) | [common]<br/>val [isInitialized](is-initialized.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Whether the SDK has been initialized with an API key or device token. |

## Functions

| Name | Summary |
|---|---|
| [initialize](initialize.md) | [common]<br/>fun [initialize](initialize.md)(proxyEndpoint: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)? = null, debugLogging: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false)<br/>Initialize SDK with proxy endpoint for secure production deployment. |
| [initializeForServer](initialize-for-server.md) | [common]<br/>fun [initializeForServer](initialize-for-server.md)(apiKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)? = null, debugLogging: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false)<br/>Initialize SDK with API key for server-side, desktop, or non-mobile environments. |
| [initializeWithAttestation](initialize-with-attestation.md) | [common]<br/>fun [initializeWithAttestation](initialize-with-attestation.md)(apiKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)? = null, debugLogging: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, callback: [VT.Companion.InitCallback](-init-callback/index.md))<br/>Initialize SDK using platform attestation for apps without a backend server. |
