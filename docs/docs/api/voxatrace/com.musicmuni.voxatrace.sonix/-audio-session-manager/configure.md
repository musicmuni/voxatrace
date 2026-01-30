//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[AudioSessionManager](index.md)/[configure](configure.md)

# configure

[common]\
expect fun [configure](configure.md)(mode: [AudioMode](../-audio-mode/index.md), echoCancellation: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Configure audio session for the specified mode.

#### Return

true if configuration succeeded

#### Parameters

common

| | |
|---|---|
| mode | The audio mode (playback, recording, or both) |
| echoCancellation | Enable acoustic echo cancellation (for recording modes) |

[android]\
actual fun [configure](configure.md)(mode: [AudioMode](../-audio-mode/index.md), echoCancellation: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)
