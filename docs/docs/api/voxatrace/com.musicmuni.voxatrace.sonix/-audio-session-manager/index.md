//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[AudioSessionManager](index.md)

# AudioSessionManager

[common]\
expect object [AudioSessionManager](index.md)

Cross-platform audio session manager.

Coordinates audio session/focus for player and recorder to work together. Platform implementations handle iOS AVAudioSession and Android AudioFocus.

Usage:

```kotlin
// Configure for singalong (playback + recording)
AudioSessionManager.configure(AudioMode.PLAY_AND_RECORD, echoCancellation = true)

// Listen for focus changes
AudioSessionManager.addFocusListener(object : AudioFocusListener {
    override fun onFocusGained() { player.play() }
    override fun onFocusLost(transient: Boolean) { player.pause() }
    override fun onDuck() { player.setVolume(0.3f) }
})

// Deactivate when done
AudioSessionManager.deactivate()
```

[android]\
actual object [AudioSessionManager](index.md)

Android implementation of AudioSessionManager.

Manages AudioFocus for coordinated player/recorder usage.

[ios]\
actual object [AudioSessionManager](index.md)

iOS implementation of AudioSessionManager.

Manages AVAudioSession configuration for coordinated player/recorder usage.

## Properties

| Name | Summary |
|---|---|
| [currentMode](current-mode.md) | [common]<br/>expect val [currentMode](current-mode.md): [AudioMode](../-audio-mode/index.md)?<br/>Current active mode, or null if not configured.<br/>[android, ios]<br/>[android]<br/>actual val [currentMode](current-mode.md): [AudioMode](../-audio-mode/index.md)?<br/>[ios]<br/>actual val [currentMode](current-mode.md): AudioMode? |
| [hardwareSampleRate](hardware-sample-rate.md) | [common]<br/>expect val [hardwareSampleRate](hardware-sample-rate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>The hardware's preferred sample rate for audio recording.<br/>[android, ios]<br/>[android, ios]<br/>actual val [hardwareSampleRate](hardware-sample-rate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [isActive](is-active.md) | [common]<br/>expect val [isActive](is-active.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Whether audio session is currently active.<br/>[android, ios]<br/>[android, ios]<br/>actual val [isActive](is-active.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |

## Functions

| Name | Summary |
|---|---|
| [addFocusListener](add-focus-listener.md) | [common]<br/>expect fun [addFocusListener](add-focus-listener.md)(listener: [AudioFocusListener](../-audio-focus-listener/index.md))<br/>Add listener for audio focus changes.<br/>[android]<br/>actual fun [addFocusListener](add-focus-listener.md)(listener: [AudioFocusListener](../-audio-focus-listener/index.md)) |
| [configure](configure.md) | [common]<br/>expect fun [configure](configure.md)(mode: [AudioMode](../-audio-mode/index.md), echoCancellation: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Configure audio session for the specified mode.<br/>[android]<br/>actual fun [configure](configure.md)(mode: [AudioMode](../-audio-mode/index.md), echoCancellation: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [deactivate](deactivate.md) | [common]<br/>expect fun [deactivate](deactivate.md)()<br/>Deactivate audio session and release focus.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [deactivate](deactivate.md)() |
| [removeFocusListener](remove-focus-listener.md) | [common]<br/>expect fun [removeFocusListener](remove-focus-listener.md)(listener: [AudioFocusListener](../-audio-focus-listener/index.md))<br/>Remove focus listener.<br/>[android]<br/>actual fun [removeFocusListener](remove-focus-listener.md)(listener: [AudioFocusListener](../-audio-focus-listener/index.md)) |
