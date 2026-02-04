//[voxatrace](../../index.md)/[com.musicmuni.voxatrace.internal.platform](index.md)/[getHardwareSampleRate](get-hardware-sample-rate.md)

# getHardwareSampleRate

[common]\
expect fun [getHardwareSampleRate](get-hardware-sample-rate.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)

Get the hardware's preferred sample rate for audio recording.

On iOS: queries AVAudioEngine's input node format (typically 44100 or 48000 Hz). On Android: returns the native sample rate from AudioManager.

#### Return

Sample rate in Hz (defaults to 44100 if unavailable)

[android, ios]\
[android, ios]\
actual fun [getHardwareSampleRate](get-hardware-sample-rate.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)
