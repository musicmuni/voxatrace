---
sidebar_label: "SonixMixerConfig"
---


# SonixMixerConfig

[common]\
data class [SonixMixerConfig](index.md)(val loopCount: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1, val onPlaybackComplete: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null, val onLoopComplete: (loopIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null, val onError: (error: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null)

Configuration for SonixMixer.

## What is SonixMixer?

A multi-track audio mixer that plays multiple audio files **synchronized**. Use it for:

- 
   Karaoke apps (backing track + vocal guide)
- 
   Practice apps (instrument tracks + drone)
- 
   Any scenario requiring independent volume control per track

## When to Use

| Scenario | Use This? | Why |
|---|---|---|
| Play multiple tracks together | Yes | Core use case |
| Karaoke with vocal muting | Yes | Fade vocal track |
| Single audio file playback | No | Use `SonixPlayer` (simpler) |
| Metronome click track | No | Use `SonixMetronome` |

## Usage Tiers (ADR-001)

### Tier 1: Presets (80% of users)

```kotlin
val mixer = SonixMixer.create()  // Uses DEFAULT
val mixer = SonixMixer.create(SonixMixerConfig.LOOPING)
```

### Tier 2: Builder (15% of users)

```kotlin
val config = SonixMixerConfig.Builder()
    .preset(SonixMixerConfig.LOOPING)
    .loopCount(3)
    .onPlaybackComplete { println("Done!") }
    .build()
val mixer = SonixMixer.create(config)
```

### Tier 3: .copy() (5% of users)

```kotlin
val config = SonixMixerConfig.LOOPING.copy(loopCount = 3)
```

## Constructors

| | |
|---|---|
| [SonixMixerConfig](-sonix-mixer-config.md) | [common]<br/>constructor(loopCount: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1, onPlaybackComplete: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null, onLoopComplete: (loopIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null, onError: (error: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null) |

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [common]<br/>class [Builder](-builder/index.md)<br/>Builder for SonixMixerConfig. |
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [loopCount](loop-count.md) | [common]<br/>val [loopCount](loop-count.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1<br/>Loop count (1 = play once, -1 = infinite) |
| [onError](on-error.md) | [common]<br/>val [onError](on-error.md): (error: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null<br/>Called on playback error |
| [onLoopComplete](on-loop-complete.md) | [common]<br/>val [onLoopComplete](on-loop-complete.md): (loopIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null<br/>Called when a single loop iteration completes |
| [onPlaybackComplete](on-playback-complete.md) | [common]<br/>val [onPlaybackComplete](on-playback-complete.md): () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)? = null<br/>Called when all loops complete |
