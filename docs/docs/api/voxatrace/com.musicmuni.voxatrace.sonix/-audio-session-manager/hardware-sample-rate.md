//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[AudioSessionManager](index.md)/[hardwareSampleRate](hardware-sample-rate.md)

# hardwareSampleRate

[common]\
expect val [hardwareSampleRate](hardware-sample-rate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)

The hardware's preferred sample rate for audio recording.

On iOS: typically 44100 or 48000 Hz. iOS ignores requested sample rates and uses the hardware rate, so use this value when configuring DSP processors (pitch detection, etc.) to ensure they match actual audio.

On Android: native sample rate from AudioManager (usually 44100 or 48000 Hz).

This value is lazily initialized on first access.

[android, ios]\
[android, ios]\
actual val [hardwareSampleRate](hardware-sample-rate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)
