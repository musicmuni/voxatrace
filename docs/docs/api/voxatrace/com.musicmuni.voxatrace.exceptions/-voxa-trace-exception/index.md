//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.exceptions](../index.md)/[VoxaTraceException](index.md)

# VoxaTraceException

open class [VoxaTraceException](index.md)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-throwable/index.html)? = null)

Base exception for all VoxaTrace SDK errors.

Module-specific exceptions extend this class:

- 
   `VoxaTraceKilledException` - API key invalid/revoked
- 
   `VoxaTraceNotInitializedException` - SDK not initialized
- 
   `SonixException` - Audio/playback errors
- 
   `CalibraException` - Pitch detection/processing errors

#### Inheritors

| |
|---|
| [CalibraException](../../com.musicmuni.voxatrace.calibra/-calibra-exception/index.md) |
| [SonixException](../../com.musicmuni.voxatrace.sonix/-sonix-exception/index.md) |
| [VoxaTraceKilledException](../-voxa-trace-killed-exception/index.md) |
| [VoxaTraceNotInitializedException](../-voxa-trace-not-initialized-exception/index.md) |

## Constructors

| | |
|---|---|
| [VoxaTraceException](-voxa-trace-exception.md) | [common]<br/>constructor(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-throwable/index.html)? = null) |
