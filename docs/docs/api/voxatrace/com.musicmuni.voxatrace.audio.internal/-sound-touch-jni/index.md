---
sidebar_label: "SoundTouchJni"
---


# SoundTouchJni

[android]\
class [SoundTouchJni](index.md)

Internal JNI bindings for SoundTouch audio processing library.

## Constructors

| | |
|---|---|
| [SoundTouchJni](-sound-touch-jni.md) | [android]<br/>constructor() |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [android]<br/>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [close](close.md) | [android]<br/>fun [close](close.md)() |
| [getVersionString](get-version-string.md) | [android]<br/>external fun [getVersionString](get-version-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [processBuffer](process-buffer.md) | [android]<br/>fun [processBuffer](process-buffer.md)(inputBuffer: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), bufferSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), bytesPerSample: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channels: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html) |
| [processFile](process-file.md) | [android]<br/>fun [processFile](process-file.md)(inputFile: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), outputFile: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [setPitchSemiTones](set-pitch-semi-tones.md) | [android]<br/>fun [setPitchSemiTones](set-pitch-semi-tones.md)(pitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)) |
| [setSpeed](set-speed.md) | [android]<br/>fun [setSpeed](set-speed.md)(speed: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)) |
| [setTempo](set-tempo.md) | [android]<br/>fun [setTempo](set-tempo.md)(tempo: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)) |
