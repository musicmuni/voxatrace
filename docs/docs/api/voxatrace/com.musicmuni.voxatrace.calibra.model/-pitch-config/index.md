---
sidebar_label: "PitchConfig"
---


# PitchConfig

[common]\
data class [PitchConfig](index.md)(val bufferSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 2048, val sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000, val tolerance: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.15f, val minFreq: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 80.0f, val maxFreq: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1000.0f, val refPitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 440.0f, val octaveWrap: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0, val amplitudeGateThresholdDb: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = -40f, val minConfidenceThreshold: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.75f)

Configuration for pitch detection (YIN algorithm).

## Constructors

| | |
|---|---|
| [PitchConfig](-pitch-config.md) | [common]<br/>constructor(bufferSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 2048, sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000, tolerance: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.15f, minFreq: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 80.0f, maxFreq: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1000.0f, refPitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 440.0f, octaveWrap: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0, amplitudeGateThresholdDb: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = -40f, minConfidenceThreshold: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.75f) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [amplitudeGateThresholdDb](amplitude-gate-threshold-db.md) | [common]<br/>val [amplitudeGateThresholdDb](amplitude-gate-threshold-db.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>RMS amplitude threshold in dB for gating quiet frames. Frames below this threshold return unvoiced (-1). Range: -60 to -20, default -40. |
| [bufferSize](buffer-size.md) | [common]<br/>val [bufferSize](buffer-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 2048<br/>Size of the audio buffer for analysis |
| [maxFreq](max-freq.md) | [common]<br/>val [maxFreq](max-freq.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 1000.0f<br/>Maximum detectable frequency in Hz |
| [minConfidenceThreshold](min-confidence-threshold.md) | [common]<br/>val [minConfidenceThreshold](min-confidence-threshold.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.75f<br/>Minimum YIN confidence (0.0-1.0) to accept pitch. Frames below this threshold return unvoiced (-1). Range: 0.5 to 0.95, default 0.75. |
| [minFreq](min-freq.md) | [common]<br/>val [minFreq](min-freq.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 80.0f<br/>Minimum detectable frequency in Hz |
| [octaveWrap](octave-wrap.md) | [common]<br/>val [octaveWrap](octave-wrap.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0<br/>Whether to wrap octaves (0 = no, 1 = yes) |
| [refPitch](ref-pitch.md) | [common]<br/>val [refPitch](ref-pitch.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 440.0f<br/>Reference pitch for A4 in Hz (default 440.0) |
| [sampleRate](sample-rate.md) | [common]<br/>val [sampleRate](sample-rate.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 16000<br/>Audio sample rate in Hz |
| [tolerance](tolerance.md) | [common]<br/>val [tolerance](tolerance.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.15f<br/>YIN algorithm tolerance (lower = more accurate, slower) |
