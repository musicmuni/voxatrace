//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.mixer](../index.md)/[AudioMixer](index.md)/[renderToBytes](render-to-bytes.md)

# renderToBytes

[common]\
abstract suspend fun [renderToBytes](render-to-bytes.md)(outputSampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 44100, outputChannels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)

Render all tracks to a byte buffer (16-bit PCM).

#### Return

Mixed audio as 16-bit signed little-endian PCM bytes

#### Parameters

common

| | |
|---|---|
| outputSampleRate | Target sample rate for output |
| outputChannels | Target channel count (1=mono, 2=stereo) |
