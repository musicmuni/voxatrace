---
sidebar_label: "PlaybackListener"
---


# PlaybackListener

[common]\
interface [PlaybackListener](index.md)

Listener interface for playback events.

## Functions

| Name | Summary |
|---|---|
| [onError](on-error.md) | [common]<br/>open fun [onError](on-error.md)(error: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html))<br/>Called when a playback error occurs. |
| [onLoopCompleted](on-loop-completed.md) | [common]<br/>abstract fun [onLoopCompleted](on-loop-completed.md)(loopIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), totalLoops: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))<br/>Called when a loop iteration completes. |
| [onPlaybackCompleted](on-playback-completed.md) | [common]<br/>abstract fun [onPlaybackCompleted](on-playback-completed.md)()<br/>Called when playback completes (reaches end without looping). |
| [onPlaybackStateChanged](on-playback-state-changed.md) | [common]<br/>abstract fun [onPlaybackStateChanged](on-playback-state-changed.md)(isPlaying: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html))<br/>Called when playback state changes. |
