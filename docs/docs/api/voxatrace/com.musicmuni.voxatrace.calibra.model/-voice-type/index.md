//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../index.md)/[VoiceType](index.md)

# VoiceType

sealed class [VoiceType](index.md)

Voice type presets for different singing styles and voice ranges.

Each voice type defines a frequency range optimized for that style.

## Usage

```kotlin
// Kotlin
.voiceType(VoiceType.carnaticMale)
.voiceType(VoiceType.westernSoprano)
```
```swift
// Swift
.voiceType(.carnaticMale)
.voiceType(.westernSoprano)
```

#### Inheritors

| |
|---|
| [Auto](-auto/index.md) |
| [WesternSoprano](-western-soprano/index.md) |
| [WesternAlto](-western-alto/index.md) |
| [WesternTenor](-western-tenor/index.md) |
| [WesternBass](-western-bass/index.md) |
| [WesternChild](-western-child/index.md) |
| [CarnaticMale](-carnatic-male/index.md) |
| [CarnaticFemale](-carnatic-female/index.md) |
| [CarnaticChild](-carnatic-child/index.md) |
| [HindustaniMale](-hindustani-male/index.md) |
| [HindustaniFemale](-hindustani-female/index.md) |
| [HindustaniChild](-hindustani-child/index.md) |
| [PopMale](-pop-male/index.md) |
| [PopFemale](-pop-female/index.md) |
| [PopChild](-pop-child/index.md) |
| [IndianFilmMale](-indian-film-male/index.md) |
| [IndianFilmFemale](-indian-film-female/index.md) |
| [IndianFilmChild](-indian-film-child/index.md) |

## Types

| Name | Summary |
|---|---|
| [Auto](-auto/index.md) | [common]<br/>object [Auto](-auto/index.md) : [VoiceType](index.md)<br/>Auto-detect from audio (wide range: 80-1000 Hz) |
| [CarnaticChild](-carnatic-child/index.md) | [common]<br/>object [CarnaticChild](-carnatic-child/index.md) : [VoiceType](index.md)<br/>Carnatic child voice (200-1000 Hz) |
| [CarnaticFemale](-carnatic-female/index.md) | [common]<br/>object [CarnaticFemale](-carnatic-female/index.md) : [VoiceType](index.md)<br/>Carnatic female voice (180-900 Hz) |
| [CarnaticMale](-carnatic-male/index.md) | [common]<br/>object [CarnaticMale](-carnatic-male/index.md) : [VoiceType](index.md)<br/>Carnatic male voice (90-450 Hz) |
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |
| [HindustaniChild](-hindustani-child/index.md) | [common]<br/>object [HindustaniChild](-hindustani-child/index.md) : [VoiceType](index.md)<br/>Hindustani child voice (200-1000 Hz) |
| [HindustaniFemale](-hindustani-female/index.md) | [common]<br/>object [HindustaniFemale](-hindustani-female/index.md) : [VoiceType](index.md)<br/>Hindustani female voice (180-900 Hz) |
| [HindustaniMale](-hindustani-male/index.md) | [common]<br/>object [HindustaniMale](-hindustani-male/index.md) : [VoiceType](index.md)<br/>Hindustani male voice (90-450 Hz) |
| [IndianFilmChild](-indian-film-child/index.md) | [common]<br/>object [IndianFilmChild](-indian-film-child/index.md) : [VoiceType](index.md)<br/>Indian film child voice (200-1000 Hz) |
| [IndianFilmFemale](-indian-film-female/index.md) | [common]<br/>object [IndianFilmFemale](-indian-film-female/index.md) : [VoiceType](index.md)<br/>Indian film female voice (180-900 Hz) |
| [IndianFilmMale](-indian-film-male/index.md) | [common]<br/>object [IndianFilmMale](-indian-film-male/index.md) : [VoiceType](index.md)<br/>Indian film male voice (100-500 Hz) |
| [PopChild](-pop-child/index.md) | [common]<br/>object [PopChild](-pop-child/index.md) : [VoiceType](index.md)<br/>Pop child voice (200-1000 Hz) |
| [PopFemale](-pop-female/index.md) | [common]<br/>object [PopFemale](-pop-female/index.md) : [VoiceType](index.md)<br/>Pop female voice (180-800 Hz) |
| [PopMale](-pop-male/index.md) | [common]<br/>object [PopMale](-pop-male/index.md) : [VoiceType](index.md)<br/>Pop male voice (100-500 Hz) |
| [WesternAlto](-western-alto/index.md) | [common]<br/>object [WesternAlto](-western-alto/index.md) : [VoiceType](index.md)<br/>Western alto voice (180-700 Hz) |
| [WesternBass](-western-bass/index.md) | [common]<br/>object [WesternBass](-western-bass/index.md) : [VoiceType](index.md)<br/>Western bass voice (80-350 Hz) |
| [WesternChild](-western-child/index.md) | [common]<br/>object [WesternChild](-western-child/index.md) : [VoiceType](index.md)<br/>Western child voice (200-1200 Hz) |
| [WesternSoprano](-western-soprano/index.md) | [common]<br/>object [WesternSoprano](-western-soprano/index.md) : [VoiceType](index.md)<br/>Western soprano voice (250-1000 Hz) |
| [WesternTenor](-western-tenor/index.md) | [common]<br/>object [WesternTenor](-western-tenor/index.md) : [VoiceType](index.md)<br/>Western tenor voice (130-500 Hz) |

## Properties

| Name | Summary |
|---|---|
| [maxFreq](max-freq.md) | [common]<br/>val [maxFreq](max-freq.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Maximum detectable frequency in Hz |
| [minFreq](min-freq.md) | [common]<br/>val [minFreq](min-freq.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Minimum detectable frequency in Hz |
