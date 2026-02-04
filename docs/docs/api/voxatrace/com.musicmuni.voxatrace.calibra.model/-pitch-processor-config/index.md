---
sidebar_label: "PitchProcessorConfig"
---


# PitchProcessorConfig

[common]\
data class [PitchProcessorConfig](index.md)(val enableSmoothing: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true, val enableOctaveCorrection: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true, val smoothingWindowSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 5, val octaveThresholdCents: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 150.0f, val referencePitchHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f)

Configuration for pitch post-processing. Shared by both realtime and batch post-processing.

## Constructors

| | |
|---|---|
| [PitchProcessorConfig](-pitch-processor-config.md) | [common]<br/>constructor(enableSmoothing: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true, enableOctaveCorrection: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true, smoothingWindowSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 5, octaveThresholdCents: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 150.0f, referencePitchHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f) |

## Properties

| Name | Summary |
|---|---|
| [enableOctaveCorrection](enable-octave-correction.md) | [common]<br/>val [enableOctaveCorrection](enable-octave-correction.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true<br/>Enable octave error correction (fixes YIN detector octave errors) |
| [enableSmoothing](enable-smoothing.md) | [common]<br/>val [enableSmoothing](enable-smoothing.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true<br/>Enable weighted smoothing filter |
| [octaveThresholdCents](octave-threshold-cents.md) | [common]<br/>val [octaveThresholdCents](octave-threshold-cents.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 150.0f<br/>How close to 1200 cents a jump must be to be corrected. 150 = correct jumps between 1050-1350 cents (within 1.5 semitones of an octave). Only true octave detection errors are corrected, not melodic jumps. |
| [referencePitchHz](reference-pitch-hz.md) | [common]<br/>val [referencePitchHz](reference-pitch-hz.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f<br/>Reference pitch in Hz for octave correction (0 = auto-detect) |
| [smoothingWindowSize](smoothing-window-size.md) | [common]<br/>val [smoothingWindowSize](smoothing-window-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 5<br/>Smoothing filter window (must be odd) |
