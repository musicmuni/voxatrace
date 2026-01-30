//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.player](../index.md)/[Mp3Decoder](index.md)

# Mp3Decoder

[common]\
expect object [Mp3Decoder](index.md)

Native MP3 decoder using LAME hip.

Decodes MP3 files to raw PCM audio data. This decoder uses the LAME library's built-in hip (LAME's MP3 decoding counterpart) for decoding.

## Usage

```kotlin
if (Mp3Decoder.isAvailable()) {
    val audioData = Mp3Decoder.decode("/path/to/audio.mp3")
    if (audioData != null) {
        println("Sample rate: ${audioData.sampleRate}")
        println("Channels: ${audioData.numChannels}")
        println("Duration: ${audioData.durationMilliSecs}ms")
    }
}
```

## Platform Implementation

- 
   **iOS**: Uses cinterop to call native LAME hip decoder
- 
   **Android**: Uses JNI to call native LAME hip decoder

[android]\
actual object [Mp3Decoder](index.md)

Android implementation of Mp3Decoder using JNI + LAME hip.

[ios]\
actual object [Mp3Decoder](index.md)

iOS implementation of Mp3Decoder using cinterop + LAME hip.

## Functions

| Name | Summary |
|---|---|
| [decode](decode.md) | [common]<br/>expect fun [decode](decode.md)(inputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [AudioRawData](../../com.musicmuni.voxatrace.sonix.model/-audio-raw-data/index.md)?<br/>Decode an MP3 file to raw PCM audio data.<br/>[android, ios]<br/>[android]<br/>actual fun [decode](decode.md)(inputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [AudioRawData](../../com.musicmuni.voxatrace.sonix.model/-audio-raw-data/index.md)?<br/>[ios]<br/>actual fun [decode](decode.md)(inputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): AudioRawData? |
| [isAvailable](is-available.md) | [common]<br/>expect fun [isAvailable](is-available.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if the MP3 decoder is available on this platform.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [isAvailable](is-available.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
