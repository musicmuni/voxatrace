//[voxatrace](../../index.md)/[com.musicmuni.voxatrace.sonix.mixer](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [AndroidMultiTrackPlayer](-android-multi-track-player/index.md) | [android]<br/>class [AndroidMultiTrackPlayer](-android-multi-track-player/index.md) : [MultiTrackPlayer](-multi-track-player/index.md)<br/>Android implementation of MultiTrackPlayer. |
| [AudioMixer](-audio-mixer/index.md) | [common]<br/>interface [AudioMixer](-audio-mixer/index.md)<br/>Software audio mixer for combining multiple audio tracks. Used in practice mode to mix backing tracks with recorded audio. |
| [IosMultiTrackPlayer](-ios-multi-track-player/index.md) | [ios]<br/>class [IosMultiTrackPlayer](-ios-multi-track-player/index.md)<br/>iOS implementation of MultiTrackPlayer using AVAudioEngine. |
| [MultiTrackPlayer](-multi-track-player/index.md) | [common]<br/>interface [MultiTrackPlayer](-multi-track-player/index.md) : [PlaybackInfoProvider](../com.musicmuni.voxatrace.sonix.player/-playback-info-provider/index.md)<br/>Multi-track audio player for synchronized playback of multiple audio tracks. |
| [SoftwareMixer](-software-mixer/index.md) | [common]<br/>class [SoftwareMixer](-software-mixer/index.md) : SynchronizedObject, [AudioMixer](-audio-mixer/index.md)<br/>Pure software audio mixer implementation. Works on all platforms using common Kotlin code. |

## Functions

| Name | Summary |
|---|---|
| [createMultiTrackPlayer](create-multi-track-player.md) | [common]<br/>expect fun [createMultiTrackPlayer](create-multi-track-player.md)(): [MultiTrackPlayer](-multi-track-player/index.md)<br/>Factory function to create platform-specific MultiTrackPlayer.<br/>[android, ios]<br/>[android]<br/>actual fun [createMultiTrackPlayer](create-multi-track-player.md)(): [MultiTrackPlayer](-multi-track-player/index.md)<br/>[ios]<br/>actual fun [createMultiTrackPlayer](create-multi-track-player.md)(): MultiTrackPlayer |
