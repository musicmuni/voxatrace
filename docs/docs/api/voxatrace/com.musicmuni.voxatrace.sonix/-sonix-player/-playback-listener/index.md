---
sidebar_label: "PlaybackListener"
---


# PlaybackListener

[common]\
interface [PlaybackListener](index.md)

Listener interface for playback events. Alternative to StateFlow observation and Builder callbacks.

## Functions

| Name | Summary |
|---|---|
| [onError](on-error.md) | [common]<br/>open fun [onError](on-error.md)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html))<br/>Called on playback errors |
| [onLoopCompleted](on-loop-completed.md) | [common]<br/>open fun [onLoopCompleted](on-loop-completed.md)(loopIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), totalLoops: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))<br/>Called when a loop iteration completes |
| [onPlaybackCompleted](on-playback-completed.md) | [common]<br/>open fun [onPlaybackCompleted](on-playback-completed.md)()<br/>Called when playback completes (all loops finished) |
| [onPlaybackStateChanged](on-playback-state-changed.md) | [common]<br/>open fun [onPlaybackStateChanged](on-playback-state-changed.md)(isPlaying: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html))<br/>Called when playback state changes |
