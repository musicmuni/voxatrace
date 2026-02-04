---
sidebar_label: "RecordingListener"
---


# RecordingListener

[common]\
interface [RecordingListener](index.md)

Listener interface for recording events. Alternative to StateFlow observation and Builder callbacks.

## Functions

| Name | Summary |
|---|---|
| [onDurationUpdate](on-duration-update.md) | [common]<br/>open fun [onDurationUpdate](on-duration-update.md)(durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Called when duration updates |
| [onError](on-error.md) | [common]<br/>open fun [onError](on-error.md)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html))<br/>Called on error |
| [onLevelUpdate](on-level-update.md) | [common]<br/>open fun [onLevelUpdate](on-level-update.md)(level: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Called when audio level updates |
| [onRecordingStarted](on-recording-started.md) | [common]<br/>open fun [onRecordingStarted](on-recording-started.md)()<br/>Called when recording starts |
| [onRecordingStopped](on-recording-stopped.md) | [common]<br/>open fun [onRecordingStopped](on-recording-stopped.md)(outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html))<br/>Called when recording stops |
| [onSegmentSaved](on-segment-saved.md) | [common]<br/>open fun [onSegmentSaved](on-segment-saved.md)(segmentIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), filePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html))<br/>Called when a segment is saved |
| [onStateChanged](on-state-changed.md) | [common]<br/>open fun [onStateChanged](on-state-changed.md)(state: [RecordingState](../../-recording-state/index.md))<br/>Called when recording state changes |
