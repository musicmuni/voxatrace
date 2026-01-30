//[voxatrace](../../index.md)/[com.musicmuni.voxatrace.sonix.metronome](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [AndroidMetronomePlayer](-android-metronome-player/index.md) | [android]<br/>class [AndroidMetronomePlayer](-android-metronome-player/index.md) : [MetronomePlayer](-metronome-player/index.md)<br/>Android MetronomePlayer using MODE_STATIC AudioTracks for low-latency click playback. |
| [IosMetronomePlayer](-ios-metronome-player/index.md) | [ios]<br/>class [IosMetronomePlayer](-ios-metronome-player/index.md) |
| [MetronomePlayer](-metronome-player/index.md) | [common]<br/>interface [MetronomePlayer](-metronome-player/index.md)<br/>Metronome audio player for practice mode. |

## Functions

| Name | Summary |
|---|---|
| [createMetronomePlayer](create-metronome-player.md) | [common]<br/>expect fun [createMetronomePlayer](create-metronome-player.md)(): [MetronomePlayer](-metronome-player/index.md)<br/>Create platform-specific metronome player.<br/>[android, ios]<br/>[android]<br/>actual fun [createMetronomePlayer](create-metronome-player.md)(): [MetronomePlayer](-metronome-player/index.md)<br/>[ios]<br/>actual fun [createMetronomePlayer](create-metronome-player.md)(): MetronomePlayer |
