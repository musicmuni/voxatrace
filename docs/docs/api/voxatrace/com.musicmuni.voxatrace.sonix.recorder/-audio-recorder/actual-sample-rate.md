//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.recorder](../index.md)/[AudioRecorder](index.md)/[actualSampleRate](actual-sample-rate.md)

# actualSampleRate

[common]\
open val [actualSampleRate](actual-sample-rate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)

The actual sample rate being used for recording.

On some platforms (especially iOS), the hardware may not support the configured sample rate, so the actual rate may differ from what was requested in AudioConfig. Use this value for encoding to ensure playback at the correct speed.

Returns the actual sample rate after [startRecording](start-recording.md) has been called, or the configured rate before that.
