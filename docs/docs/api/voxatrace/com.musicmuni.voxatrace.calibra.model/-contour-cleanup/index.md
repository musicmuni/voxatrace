//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../index.md)/[ContourCleanup](index.md)

# ContourCleanup

[common]\
data class [ContourCleanup](index.md)(val fixOctaveErrors: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true, val fixBoundaryOctaves: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true, val boundaryWindowMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 50.0f, val minimumNoteDurationMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 80.0f, val smoothPitch: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false)

Post-processing cleanup options for pitch contours.

Applied after pitch detection to fix common artifacts.

## Constructors

| | |
|---|---|
| [ContourCleanup](-contour-cleanup.md) | [common]<br/>constructor(fixOctaveErrors: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true, fixBoundaryOctaves: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true, boundaryWindowMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 50.0f, minimumNoteDurationMs: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 80.0f, smoothPitch: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [boundaryWindowMs](boundary-window-ms.md) | [common]<br/>val [boundaryWindowMs](boundary-window-ms.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 50.0f<br/>How much of phrase edges to check for boundary octaves (ms) |
| [fixBoundaryOctaves](fix-boundary-octaves.md) | [common]<br/>val [fixBoundaryOctaves](fix-boundary-octaves.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true<br/>Fix octave errors at phrase onset/offset |
| [fixOctaveErrors](fix-octave-errors.md) | [common]<br/>val [fixOctaveErrors](fix-octave-errors.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true<br/>Fix mid-phrase frame-to-frame octave jumps |
| [minimumNoteDurationMs](minimum-note-duration-ms.md) | [common]<br/>val [minimumNoteDurationMs](minimum-note-duration-ms.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 80.0f<br/>Minimum duration for valid pitch (removes blips) |
| [smoothPitch](smooth-pitch.md) | [common]<br/>val [smoothPitch](smooth-pitch.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false<br/>Apply pitch smoothing filter |
