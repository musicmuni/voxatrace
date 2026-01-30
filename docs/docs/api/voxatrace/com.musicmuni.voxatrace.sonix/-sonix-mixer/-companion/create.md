//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.sonix](../../index.md)/[SonixMixer](../index.md)/[Companion](index.md)/[create](create.md)

# create

[common]\
fun [create](create.md)(config: [SonixMixerConfig](../../-sonix-mixer-config/index.md) = SonixMixerConfig.DEFAULT, audioSession: [AudioMode](../../-audio-mode/index.md) = AudioMode.PLAYBACK): [SonixMixer](../index.md)

Create mixer with configuration.

ADR-001 compliant: Factory takes config.

Usage:

```kotlin
val mixer = SonixMixer.create()
mixer.addTrack("backing", "/path/to/backing.mp3")
mixer.play()
```

#### Return

Ready-to-use SonixMixer instance

#### Parameters

common

| | |
|---|---|
| config | Mixer configuration (default: DEFAULT) |
| audioSession | Audio session mode - configures system audio automatically (default: PLAYBACK) |
