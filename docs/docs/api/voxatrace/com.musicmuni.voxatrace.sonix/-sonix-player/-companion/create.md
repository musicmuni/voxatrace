//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.sonix](../../index.md)/[SonixPlayer](../index.md)/[Companion](index.md)/[create](create.md)

# create

[common]\
suspend fun [create](create.md)(source: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), config: [SonixPlayerConfig](../../-sonix-player-config/index.md) = SonixPlayerConfig.DEFAULT, audioSession: [AudioMode](../../-audio-mode/index.md) = AudioMode.PLAYBACK): [SonixPlayer](../index.md)

Create player from source with configuration.

ADR-001 compliant: Factory takes (data, config).

#### Return

Ready-to-play SonixPlayer

#### Parameters

common

| | |
|---|---|
| source | Path to audio file (data) |
| config | Player configuration (default: DEFAULT) |
| audioSession | Audio session mode - configures system audio automatically (default: PLAYBACK) |
