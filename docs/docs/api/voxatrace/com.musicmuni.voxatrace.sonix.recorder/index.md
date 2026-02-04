---
sidebar_label: "recorder"
---


# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [AudioRecorder](-audio-recorder/index.md) | [common]<br/>interface [AudioRecorder](-audio-recorder/index.md)<br/>Interface for audio recording functionality. |
| [Mp3Encoder](-mp3-encoder/index.md) | [common]<br/>expect object [Mp3Encoder](-mp3-encoder/index.md)<br/>MP3 encoder using LAME.<br/>[android]<br/>actual object [Mp3Encoder](-mp3-encoder/index.md)<br/>Android implementation of Mp3Encoder using JNI + LAME.<br/>[ios]<br/>actual object [Mp3Encoder](-mp3-encoder/index.md)<br/>iOS implementation of Mp3Encoder using cinterop + LAME. |

## Functions

| Name | Summary |
|---|---|
| [createAudioRecorder](create-audio-recorder.md) | [common]<br/>expect fun [createAudioRecorder](create-audio-recorder.md)(config: [AudioConfig](../com.musicmuni.voxatrace.sonix.model/-audio-config/index.md)): [AudioRecorder](-audio-recorder/index.md)<br/>Creates a platform-specific [AudioRecorder](-audio-recorder/index.md) instance.<br/>[android]<br/>actual fun [createAudioRecorder](create-audio-recorder.md)(config: [AudioConfig](../com.musicmuni.voxatrace.sonix.model/-audio-config/index.md)): [AudioRecorder](-audio-recorder/index.md) |
