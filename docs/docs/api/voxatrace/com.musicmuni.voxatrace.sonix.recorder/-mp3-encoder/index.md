---
sidebar_label: "Mp3Encoder"
---


# Mp3Encoder

[common]\
expect object [Mp3Encoder](index.md)

MP3 encoder using LAME.

Use this when you need to encode audio to MP3 format. For AAC encoding, use the platform AudioEncoder API instead.

## Usage

```kotlin
// Encode float samples to MP3
val success = Mp3Encoder.encode(
    samples = floatArrayOf(...),
    sampleRate = 44100,
    channels = 1,
    bitrateKbps = 192,
    outputPath = "/path/to/output.mp3"
)

// With custom quality (0 = best, 9 = fastest)
val success = Mp3Encoder.encode(
    samples = floatArrayOf(...),
    sampleRate = 44100,
    channels = 1,
    bitrateKbps = 192,
    quality = 0,  // Best quality
    outputPath = "/path/to/output.mp3"
)
```

[android]\
actual object [Mp3Encoder](index.md)

Android implementation of Mp3Encoder using JNI + LAME.

[ios]\
actual object [Mp3Encoder](index.md)

iOS implementation of Mp3Encoder using cinterop + LAME.

## Functions

| Name | Summary |
|---|---|
| [encode](encode.md) | [common]<br/>expect fun [encode](encode.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), bitrateKbps: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), quality: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 2, outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Encode float samples to MP3 file.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [encode](encode.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), bitrateKbps: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), quality: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [encodePcm](encode-pcm.md) | [common]<br/>expect fun [encodePcm](encode-pcm.md)(pcmData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), bitrateKbps: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), quality: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 2, outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Encode PCM bytes to MP3 file.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [encodePcm](encode-pcm.md)(pcmData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), bitrateKbps: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), quality: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [isAvailable](is-available.md) | [common]<br/>expect fun [isAvailable](is-available.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if MP3 encoding is available on this platform.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [isAvailable](is-available.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
