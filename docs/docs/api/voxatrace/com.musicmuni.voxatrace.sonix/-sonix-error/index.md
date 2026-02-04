---
sidebar_label: "SonixError"
---


# SonixError

[common]\
data class [SonixError](index.md)(val message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-throwable/index.html)? = null)

Error information for Sonix operations.

Used by StateFlows to report errors without throwing exceptions.

## Constructors

| | |
|---|---|
| [SonixError](-sonix-error.md) | [common]<br/>constructor(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-throwable/index.html)? = null) |

## Properties

| Name | Summary |
|---|---|
| [cause](cause.md) | [common]<br/>val [cause](cause.md): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-throwable/index.html)? = null |
| [message](message.md) | [common]<br/>val [message](message.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
