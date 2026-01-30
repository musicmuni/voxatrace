//[voxatrace](../../index.md)/[com.musicmuni.voxatrace.sonix.player](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [AndroidAudioPlayer](-android-audio-player/index.md) | [android]<br/>class [AndroidAudioPlayer](-android-audio-player/index.md) : [AudioPlayer](-audio-player/index.md) |
| [AudioDecoder](-audio-decoder/index.md) | [common]<br/>expect object [AudioDecoder](-audio-decoder/index.md)<br/>Decodes audio files to raw PCM data.<br/>[android, ios]<br/>[android, ios]<br/>actual object [AudioDecoder](-audio-decoder/index.md) |
| [AudioPlayer](-audio-player/index.md) | [common]<br/>interface [AudioPlayer](-audio-player/index.md) : [PlaybackInfoProvider](-playback-info-provider/index.md)<br/>Audio playback interface with pitch shifting, looping, and volume control. |
| [IosAudioPlayer](-ios-audio-player/index.md) | [ios]<br/>class [IosAudioPlayer](-ios-audio-player/index.md) |
| [IosSoundTouch](-ios-sound-touch/index.md) | [ios]<br/>class [IosSoundTouch](-ios-sound-touch/index.md) |
| [Mp3Decoder](-mp3-decoder/index.md) | [common]<br/>expect object [Mp3Decoder](-mp3-decoder/index.md)<br/>Native MP3 decoder using LAME hip.<br/>[android]<br/>actual object [Mp3Decoder](-mp3-decoder/index.md)<br/>Android implementation of Mp3Decoder using JNI + LAME hip.<br/>[ios]<br/>actual object [Mp3Decoder](-mp3-decoder/index.md)<br/>iOS implementation of Mp3Decoder using cinterop + LAME hip. |
| [PlaybackInfoProvider](-playback-info-provider/index.md) | [common]<br/>interface [PlaybackInfoProvider](-playback-info-provider/index.md)<br/>Interface for providing playback timing information. |
| [SoundTouchJni](-sound-touch-jni/index.md) | [android]<br/>class [SoundTouchJni](-sound-touch-jni/index.md)<br/>SoundTouch audio processing wrapper. Delegates to internal JNI bindings. |
| [VolumeEasing](-volume-easing/index.md) | [common]<br/>enum [VolumeEasing](-volume-easing/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[VolumeEasing](-volume-easing/index.md)&gt; <br/>Easing curves for volume transitions. |

## Functions

| Name | Summary |
|---|---|
| [createAudioPlayer](create-audio-player.md) | [common]<br/>expect fun [createAudioPlayer](create-audio-player.md)(): [AudioPlayer](-audio-player/index.md)<br/>Creates a platform-specific [AudioPlayer](-audio-player/index.md) instance.<br/>[android, ios]<br/>[android]<br/>actual fun [createAudioPlayer](create-audio-player.md)(): [AudioPlayer](-audio-player/index.md)<br/>[ios]<br/>actual fun [createAudioPlayer](create-audio-player.md)(): AudioPlayer |
