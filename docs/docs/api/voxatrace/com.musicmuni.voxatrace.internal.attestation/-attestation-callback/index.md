---
sidebar_label: "AttestationCallback"
---


# AttestationCallback

[common]\
interface [AttestationCallback](index.md)

Callback interface for attestation generation.

## Functions

| Name | Summary |
|---|---|
| [onError](on-error.md) | [common]<br/>abstract fun [onError](on-error.md)(error: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html))<br/>Called when attestation fails. |
| [onSuccess](on-success.md) | [common]<br/>abstract fun [onSuccess](on-success.md)(attestation: [AttestationResult](../-attestation-result/index.md))<br/>Called when attestation is successfully generated. |
