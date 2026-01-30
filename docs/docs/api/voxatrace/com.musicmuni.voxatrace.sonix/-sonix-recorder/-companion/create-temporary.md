//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.sonix](../../index.md)/[SonixRecorder](../index.md)/[Companion](index.md)/[createTemporary](create-temporary.md)

# createTemporary

[common]\
fun [createTemporary](create-temporary.md)(config: [SonixRecorderConfig](../../-sonix-recorder-config/index.md) = SonixRecorderConfig.VOICE, audioSession: [AudioMode](../../-audio-mode/index.md) = AudioMode.RECORDING): [SonixRecorder](../index.md)

Create a recorder with automatic temp file path.

Useful for recording sessions where you don't need a specific output path (e.g., real-time pitch detection, live analysis).

#### Return

Ready-to-use SonixRecorder with temp output path

#### Parameters

common

| | |
|---|---|
| config | Recorder configuration (default: VOICE) |
| audioSession | Audio session mode - configures system audio automatically (default: RECORDING) |
