//[voxatrace](../../index.md)/[com.musicmuni.voxatrace.exceptions](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [VoxaTraceException](-voxa-trace-exception/index.md) | [common]<br/>open class [VoxaTraceException](-voxa-trace-exception/index.md)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-throwable/index.html)? = null)<br/>Base exception for all VoxaTrace SDK errors. |
| [VoxaTraceKilledException](-voxa-trace-killed-exception/index.md) | [common]<br/>class [VoxaTraceKilledException](-voxa-trace-killed-exception/index.md)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) : [VoxaTraceException](-voxa-trace-exception/index.md)<br/>Exception thrown when API key is invalid or revoked. |
| [VoxaTraceNotInitializedException](-voxa-trace-not-initialized-exception/index.md) | [common]<br/>open class [VoxaTraceNotInitializedException](-voxa-trace-not-initialized-exception/index.md)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) = &quot;VoxaTrace SDK not initialized. Call VT.initialize() first.&quot;) : [VoxaTraceException](-voxa-trace-exception/index.md)<br/>Exception thrown when SDK operations are attempted before initialization. |
