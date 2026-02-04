---
sidebar_label: "SonixPlayerConfig"
---


# SonixPlayerConfig

data class [SonixPlayerConfig](index.md)(val volume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f, val pitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, val tempo: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f, val loopCount: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1, val onComplete: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null, val onLoopComplete: (loopIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), totalLoops: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null, val onPlaybackStateChanged: (isPlaying: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null, val onError: (message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null)

Configuration for SonixPlayer.

ADR-001 compliant: Builder builds Config, Factory takes Config.

## Usage Tiers

### Tier 1: Presets (80% of users)

#### Kotlin

```kotlin
val player = SonixPlayer.create("song.mp3", SonixPlayerConfig.DEFAULT)
```

#### Swift

```swift
let player = try await SonixPlayer.create(source: "song.mp3", config: .default)
```

### Tier 2: Builder (15% of users)

#### Kotlin

```kotlin
val config = SonixPlayerConfig.Builder()
    .preset(SonixPlayerConfig.LOOPING)
    .volume(0.8f)
    .pitch(-2f)
    .build()
val player = SonixPlayer.create("song.mp3", config)
```

#### Swift

```swift
let config = SonixPlayerConfig.Builder()
    .preset(.looping)
    .volume(0.8)
    .pitch(-2)
    .build()
let player = try await SonixPlayer.create(source: "song.mp3", config: config)
```

### Tier 3: .copy() (5% of users)

#### Kotlin

```kotlin
val config = SonixPlayerConfig.LOOPING.copy(volume = 0.5f)
```

#### See also

| | |
|---|---|
| [SonixPlayer.Companion.create](../-sonix-player/-companion/create.md) | Factory method to create players |
| [SonixRecorder](../-sonix-recorder/index.md) | For recording audio |

## Constructors

| | |
|---|---|
| [SonixPlayerConfig](-sonix-player-config.md) | [common]<br/>constructor(volume: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f, pitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, tempo: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f, loopCount: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1, onComplete: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null, onLoopComplete: (loopIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), totalLoops: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null, onPlaybackStateChanged: (isPlaying: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null, onError: (message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null) |

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [common]<br/>class [Builder](-builder/index.md)<br/>Builder for SonixPlayerConfig. |
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [loopCount](loop-count.md) | [common]<br/>val [loopCount](loop-count.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1<br/>Loop count (1 = play once, -1 = infinite) |
| [onComplete](on-complete.md) | [common]<br/>val [onComplete](on-complete.md): () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null<br/>Called when playback completes (all loops finished) |
| [onError](on-error.md) | [common]<br/>val [onError](on-error.md): (message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null<br/>Called on playback errors |
| [onLoopComplete](on-loop-complete.md) | [common]<br/>val [onLoopComplete](on-loop-complete.md): (loopIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), totalLoops: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null<br/>Called when a loop iteration completes |
| [onPlaybackStateChanged](on-playback-state-changed.md) | [common]<br/>val [onPlaybackStateChanged](on-playback-state-changed.md): (isPlaying: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null<br/>Called when playback state changes |
| [pitch](pitch.md) | [common]<br/>val [pitch](pitch.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f<br/>Pitch shift in semitones (-12 to +12) |
| [tempo](tempo.md) | [common]<br/>val [tempo](tempo.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f<br/>Tempo/speed multiplier (0.25 to 4.0, 1.0 = normal) |
| [volume](volume.md) | [common]<br/>val [volume](volume.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1.0f<br/>Initial volume (0.0 to 1.0) |
