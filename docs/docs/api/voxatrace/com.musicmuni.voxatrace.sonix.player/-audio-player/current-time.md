//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.player](../index.md)/[AudioPlayer](index.md)/[currentTime](current-time.md)

# currentTime

[common]\
abstract val [currentTime](current-time.md): StateFlow&lt;[Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)&gt;

Current playback position as a reactive StateFlow.

Emits the current position in milliseconds. Updates approximately every 100ms during playback.
