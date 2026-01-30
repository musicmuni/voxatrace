//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.sonix](../../index.md)/[SonixRecorder](../index.md)/[Companion](index.md)/[create](create.md)

# create

[common]\
fun [create](create.md)(outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), config: [SonixRecorderConfig](../../-sonix-recorder-config/index.md) = SonixRecorderConfig.VOICE, audioSession: [AudioMode](../../-audio-mode/index.md) = AudioMode.RECORDING): [SonixRecorder](../index.md)

Create recorder with configuration.

ADR-001 compliant: Factory takes (data, config).

#### Return

Ready-to-use SonixRecorder

#### Parameters

common

| | |
|---|---|
| outputPath | Path where the recording will be saved (data) |
| config | Recorder configuration (default: VOICE) |
| audioSession | Audio session mode - configures system audio automatically (default: RECORDING) |
