//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixMixer](index.md)/[setPlaybackListener](set-playback-listener.md)

# setPlaybackListener

[common]\
fun [setPlaybackListener](set-playback-listener.md)(listener: [SonixMixer.PlaybackListener](-playback-listener/index.md)?)

Set a listener for playback events.

Useful for synchronizing UI or triggering actions at playback milestones. Setting a listener replaces any callbacks configured via Builder.

#### Parameters

common

| | |
|---|---|
| listener | Listener instance, or null to remove |
