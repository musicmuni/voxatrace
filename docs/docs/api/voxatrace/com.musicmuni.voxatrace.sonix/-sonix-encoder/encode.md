//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixEncoder](index.md)/[encode](encode.md)

# encode

[common]\
fun [encode](encode.md)(data: [AudioRawData](../../com.musicmuni.voxatrace.sonix.model/-audio-raw-data/index.md), outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), format: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) = &quot;m4a&quot;, bitrateKbps: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 128): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Encode AudioRawData to a compressed audio file.

#### Return

true on success, false on failure

#### Parameters

common

| | |
|---|---|
| data | Raw PCM audio data (from SonixDecoder or synthesis) |
| outputPath | Absolute path for output file |
| format | Output format: &quot;m4a&quot; (default) or &quot;mp3&quot; |
| bitrateKbps | Target bitrate in kbps (default: 128) |

[common]\
fun [encode](encode.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), format: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) = &quot;m4a&quot;, bitrateKbps: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 128): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Encode float samples to a compressed audio file.

#### Return

true on success, false on failure

#### Parameters

common

| | |
|---|---|
| samples | Interleaved float samples in range -1.0, 1.0 |
| sampleRate | Sample rate in Hz (e.g., 44100) |
| channels | Number of channels (1 = mono, 2 = stereo) |
| outputPath | Absolute path for output file |
| format | Output format: &quot;m4a&quot; (default) or &quot;mp3&quot; |
| bitrateKbps | Target bitrate in kbps (default: 128) |

[common]\
fun [encode](encode.md)(pcmData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), format: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) = &quot;m4a&quot;, bitrateKbps: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 128): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Encode PCM bytes to a compressed audio file.

#### Return

true on success, false on failure

#### Parameters

common

| | |
|---|---|
| pcmData | 16-bit signed PCM bytes (little-endian, interleaved if stereo) |
| sampleRate | Sample rate in Hz (e.g., 44100) |
| channels | Number of channels (1 = mono, 2 = stereo) |
| outputPath | Absolute path for output file |
| format | Output format: &quot;m4a&quot; (default) or &quot;mp3&quot; |
| bitrateKbps | Target bitrate in kbps (default: 128) |
