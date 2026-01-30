//[voxatrace](../../index.md)/[com.musicmuni.voxatrace.sonix.recorder](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [AndroidAudioEncoder](-android-audio-encoder/index.md) | [android]<br/>class [AndroidAudioEncoder](-android-audio-encoder/index.md) : [AudioEncoder](-audio-encoder/index.md)<br/>Android implementation of AudioEncoder using MediaCodec and MediaMuxer. Encodes PCM audio to AAC format in an M4A container. |
| [AndroidAudioRecorder](-android-audio-recorder/index.md) | [android]<br/>class [AndroidAudioRecorder](-android-audio-recorder/index.md)(config: [AudioConfig](../com.musicmuni.voxatrace.sonix.model/-audio-config/index.md)) : [AudioRecorder](-audio-recorder/index.md) |
| [AudioEncoder](-audio-encoder/index.md) | [common]<br/>interface [AudioEncoder](-audio-encoder/index.md)<br/>Encodes PCM audio to compressed formats (AAC, MP3). Thread-safe: queueBuffer can be called from any thread. |
| [AudioRecorder](-audio-recorder/index.md) | [common]<br/>interface [AudioRecorder](-audio-recorder/index.md)<br/>Interface for audio recording functionality. |
| [AudioSession](-audio-session/index.md) | [common]<br/>class [AudioSession](-audio-session/index.md)(config: [AudioConfig](../com.musicmuni.voxatrace.sonix.model/-audio-config/index.md), recorder: [AudioRecorder](-audio-recorder/index.md), enableEncoding: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, encoderBitRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 128000)<br/>Audio recording session with optional encoding support. |
| [IosAudioEncoder](-ios-audio-encoder/index.md) | [ios]<br/>class [IosAudioEncoder](-ios-audio-encoder/index.md)<br/>iOS implementation of AudioEncoder using AVAudioFile and AVAssetExportSession. |
| [IosAudioRecorder](-ios-audio-recorder/index.md) | [ios]<br/>class [IosAudioRecorder](-ios-audio-recorder/index.md)(config: &lt;Error class: unknown class&gt;) |
| [Mp3Encoder](-mp3-encoder/index.md) | [common]<br/>expect object [Mp3Encoder](-mp3-encoder/index.md)<br/>MP3 encoder using LAME.<br/>[android]<br/>actual object [Mp3Encoder](-mp3-encoder/index.md)<br/>Android implementation of Mp3Encoder using JNI + LAME.<br/>[ios]<br/>actual object [Mp3Encoder](-mp3-encoder/index.md)<br/>iOS implementation of Mp3Encoder using cinterop + LAME. |

## Functions

| Name | Summary |
|---|---|
| [createAudioEncoder](create-audio-encoder.md) | [common]<br/>expect fun [createAudioEncoder](create-audio-encoder.md)(): [AudioEncoder](-audio-encoder/index.md)<br/>Platform-specific factory function to create an AudioEncoder.<br/>[android, ios]<br/>[android]<br/>actual fun [createAudioEncoder](create-audio-encoder.md)(): [AudioEncoder](-audio-encoder/index.md)<br/>[ios]<br/>actual fun [createAudioEncoder](create-audio-encoder.md)(): AudioEncoder |
| [createAudioRecorder](create-audio-recorder.md) | [common]<br/>expect fun [createAudioRecorder](create-audio-recorder.md)(config: [AudioConfig](../com.musicmuni.voxatrace.sonix.model/-audio-config/index.md)): [AudioRecorder](-audio-recorder/index.md)<br/>Creates a platform-specific [AudioRecorder](-audio-recorder/index.md) instance.<br/>[android]<br/>actual fun [createAudioRecorder](create-audio-recorder.md)(config: [AudioConfig](../com.musicmuni.voxatrace.sonix.model/-audio-config/index.md)): [AudioRecorder](-audio-recorder/index.md) |
