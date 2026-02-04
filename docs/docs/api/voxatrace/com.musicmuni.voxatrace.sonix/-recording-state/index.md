---
sidebar_label: "RecordingState"
---


# RecordingState

sealed class [RecordingState](index.md)

Recording session state - mirrors AudioSession.State with public visibility.

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
| [Encoding](-encoding/index.md) | [common]<br/>object [Encoding](-encoding/index.md) : [RecordingState](index.md)<br/>Encoding recorded audio to output format |
| [Error](-error/index.md) | [common]<br/>data class [Error](-error/index.md)(val message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) : [RecordingState](index.md)<br/>An error occurred |
| [Finished](-finished/index.md) | [common]<br/>data class [Finished](-finished/index.md)(val outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)?) : [RecordingState](index.md)<br/>Recording finished successfully |
| [Idle](-idle/index.md) | [common]<br/>object [Idle](-idle/index.md) : [RecordingState](index.md)<br/>Recorder is idle, not recording |
| [Recording](-recording/index.md) | [common]<br/>data class [Recording](-recording/index.md)(val durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)) : [RecordingState](index.md)<br/>Actively recording |
| [Starting](-starting/index.md) | [common]<br/>object [Starting](-starting/index.md) : [RecordingState](index.md)<br/>Recording is starting, initializing audio hardware |
| [Stopping](-stopping/index.md) | [common]<br/>object [Stopping](-stopping/index.md) : [RecordingState](index.md)<br/>Recording is stopping, finalizing buffers |
