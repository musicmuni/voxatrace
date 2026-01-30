//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.sonix.mixer](../../index.md)/[MultiTrackPlayer](../index.md)/[PlaybackListener](index.md)

# PlaybackListener

[common]\
interface [PlaybackListener](index.md)

Listener for multi-track playback events.

## Functions

| Name | Summary |
|---|---|
| [onError](on-error.md) | [common]<br/>open fun [onError](on-error.md)(error: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html))<br/>Called on playback error. |
| [onLoopCompleted](on-loop-completed.md) | [common]<br/>abstract fun [onLoopCompleted](on-loop-completed.md)(loopIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html))<br/>Called when a single loop iteration completes. |
| [onPlaybackCompleted](on-playback-completed.md) | [common]<br/>abstract fun [onPlaybackCompleted](on-playback-completed.md)()<br/>Called when all loops complete. |
| [onPlaybackPaused](on-playback-paused.md) | [common]<br/>abstract fun [onPlaybackPaused](on-playback-paused.md)(playbackTimeMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Called when playback pauses. |
| [onPlaybackStarted](on-playback-started.md) | [common]<br/>abstract fun [onPlaybackStarted](on-playback-started.md)(startTimeMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Called when playback starts. |
