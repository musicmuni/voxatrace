//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.sonix.recorder](../../index.md)/[AudioSession](../index.md)/[State](index.md)

# State

sealed class [State](index.md)

#### Inheritors

| |
|---|
| [Idle](-idle/index.md) |
| [Starting](-starting/index.md) |
| [Recording](-recording/index.md) |
| [Stopping](-stopping/index.md) |
| [Encoding](-encoding/index.md) |
| [Finished](-finished/index.md) |
| [Error](-error/index.md) |

## Types

| Name | Summary |
|---|---|
| [Encoding](-encoding/index.md) | [common]<br/>object [Encoding](-encoding/index.md) : [AudioSession.State](index.md) |
| [Error](-error/index.md) | [common]<br/>data class [Error](-error/index.md)(val message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) : [AudioSession.State](index.md) |
| [Finished](-finished/index.md) | [common]<br/>data class [Finished](-finished/index.md)(val outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)?) : [AudioSession.State](index.md) |
| [Idle](-idle/index.md) | [common]<br/>object [Idle](-idle/index.md) : [AudioSession.State](index.md) |
| [Recording](-recording/index.md) | [common]<br/>data class [Recording](-recording/index.md)(val durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)) : [AudioSession.State](index.md) |
| [Starting](-starting/index.md) | [common]<br/>object [Starting](-starting/index.md) : [AudioSession.State](index.md) |
| [Stopping](-stopping/index.md) | [common]<br/>object [Stopping](-stopping/index.md) : [AudioSession.State](index.md) |
