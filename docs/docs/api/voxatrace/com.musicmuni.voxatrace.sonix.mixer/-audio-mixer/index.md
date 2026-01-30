//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.mixer](../index.md)/[AudioMixer](index.md)

# AudioMixer

interface [AudioMixer](index.md)

Software audio mixer for combining multiple audio tracks. Used in practice mode to mix backing tracks with recorded audio.

Usage:

```kotlin
val mixer = SoftwareMixer()
mixer.addTrack("backing", "/path/to/backing.mp3")
mixer.addTrack("vocal", "/path/to/recording.m4a")
mixer.setTrackVolume("backing", 0.7f)
mixer.setTrackVolume("vocal", 1.0f)

val mixed = mixer.renderToBuffer(44100)
// or
mixer.renderToFile("/path/to/output.m4a", encoder)
```

#### Inheritors

| |
|---|
| [SoftwareMixer](../-software-mixer/index.md) |

## Types

| Name | Summary |
|---|---|
| [TrackInfo](-track-info/index.md) | [common]<br/>data class [TrackInfo](-track-info/index.md)(val id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val durationMs: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), val sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), val channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [tracks](tracks.md) | [common]<br/>abstract val [tracks](tracks.md): StateFlow&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[AudioMixer.TrackInfo](-track-info/index.md)&gt;&gt;<br/>Current list of tracks in the mixer. |

## Functions

| Name | Summary |
|---|---|
| [addTrack](add-track.md) | [common]<br/>abstract suspend fun [addTrack](add-track.md)(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), filePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Add a track from an audio file. The file will be decoded and cached in memory. |
| [addTrackBytes](add-track-bytes.md) | [common]<br/>abstract fun [addTrackBytes](add-track-bytes.md)(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), pcmData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1)<br/>Add a track from raw PCM bytes. |
| [addTrackRaw](add-track-raw.md) | [common]<br/>abstract fun [addTrackRaw](add-track-raw.md)(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1)<br/>Add a track from raw PCM data. |
| [clearTracks](clear-tracks.md) | [common]<br/>abstract fun [clearTracks](clear-tracks.md)()<br/>Remove all tracks from the mixer. |
| [getTotalDurationMs](get-total-duration-ms.md) | [common]<br/>abstract fun [getTotalDurationMs](get-total-duration-ms.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br/>Get the total duration of the mix (longest track). |
| [release](release.md) | [common]<br/>abstract fun [release](release.md)()<br/>Release all resources. |
| [removeTrack](remove-track.md) | [common]<br/>abstract fun [removeTrack](remove-track.md)(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html))<br/>Remove a track from the mixer. |
| [renderToBuffer](render-to-buffer.md) | [common]<br/>abstract suspend fun [renderToBuffer](render-to-buffer.md)(outputSampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44100, outputChannels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)<br/>Render all tracks to a float sample buffer. All tracks are mixed and resampled to the output sample rate. |
| [renderToBytes](render-to-bytes.md) | [common]<br/>abstract suspend fun [renderToBytes](render-to-bytes.md)(outputSampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44100, outputChannels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br/>Render all tracks to a byte buffer (16-bit PCM). |
| [renderToFile](render-to-file.md) | [common]<br/>abstract suspend fun [renderToFile](render-to-file.md)(outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), encoder: [AudioEncoder](../../com.musicmuni.voxatrace.sonix.recorder/-audio-encoder/index.md), outputSampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44100): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Render all tracks directly to an encoded file. |
| [setTrackMuted](set-track-muted.md) | [common]<br/>abstract fun [setTrackMuted](set-track-muted.md)(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), muted: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html))<br/>Mute or unmute a track. |
| [setTrackPan](set-track-pan.md) | [common]<br/>abstract fun [setTrackPan](set-track-pan.md)(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), pan: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Set pan position for a track. |
| [setTrackVolume](set-track-volume.md) | [common]<br/>abstract fun [setTrackVolume](set-track-volume.md)(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), volume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html))<br/>Set volume for a track. |
