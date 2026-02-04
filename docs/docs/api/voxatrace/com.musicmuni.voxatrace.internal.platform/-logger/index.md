---
sidebar_label: "Logger"
---


# Logger

[common]\
object [Logger](index.md)

Centralized logging for VoxaTrace SDK.

Wraps Napier logger with consistent API across all modules. Call [initialize](initialize.md) once at app startup to enable debug logging.

## Functions

| Name | Summary |
|---|---|
| [d](d.md) | [common]<br/>fun [d](d.md)(tag: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), message: () -&gt; [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html))<br/>fun [d](d.md)(tag: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) |
| [e](e.md) | [common]<br/>fun [e](e.md)(tag: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), throwable: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-throwable/index.html)? = null) |
| [i](i.md) | [common]<br/>fun [i](i.md)(tag: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) |
| [initialize](initialize.md) | [common]<br/>fun [initialize](initialize.md)()<br/>Initialize logging. Call once at app startup. |
| [w](w.md) | [common]<br/>fun [w](w.md)(tag: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) |
