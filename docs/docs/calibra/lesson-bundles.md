---
sidebar_position: 11
---

# Lesson Bundles

A **reference bundle** is a directory of pre-computed reference-lesson features
(audio, pitch contour, HPCP chroma, phrases) produced offline and loaded at
runtime. `LessonBundle.load` turns a bundle directory into a `LessonMaterial`
you hand straight to `CalibraLiveEval` / `CalibraMelodyEval`, with **no
pitch/chroma DSP at load time** (the features are already computed).

This page is the API reference for the runtime flow. For the on-disk file
contract see the [Bundle Format reference](https://github.com/musicmuni/voxatrace/blob/main/docs/bundle-format.md);
for producing bundles with the CLI see the
[Lesson Authoring guide](../guides/lesson-authoring).

## Loading a bundle

`LessonBundle.load(directoryPath)` reads the manifest, validates its format
version, decodes the audio at the manifest's sample rate, and reconstructs the
pre-computed pitch contour and HPCP frames into a `LessonMaterial`.

### Kotlin

```kotlin
import com.musicmuni.voxatrace.calibra.LessonBundle

// 1. Load a bundle directory -> LessonMaterial (features pre-computed)
val reference = LessonBundle.load("/path/to/bundle-dir")

// 2. Hand it to evaluation. The student recording is a separate LessonMaterial.
val extractor = PitchDetection.createContourExtractor(ContourExtractorConfig.SCORING)
val result = CalibraMelodyEval.evaluate(reference, student, extractor)
extractor.release()
```

### Swift

```swift
let reference = LessonBundle.shared.load(directoryPath: "/path/to/bundle-dir")

let extractor = PitchDetection.createContourExtractor(config: .scoring)
let result = CalibraMelodyEval.evaluate(
    reference: reference,
    student: student,
    contourExtractor: extractor
)
extractor.release()
```

Because the bundle ships pre-computed `pitchContour` and `hpcpFrames`, the
evaluator skips contour extraction and HPCP analysis for the reference side: the
loaded `LessonMaterial` is the fast-path input described in
[CalibraMelodyEval](./melody-eval#pre-computed-pitch-contour-skip-extraction).

### Load Method

```kotlin
object LessonBundle {
    fun load(directoryPath: String): LessonMaterial
}
```

| Parameter | Type | Description |
|-----------|------|-------------|
| `directoryPath` | `String` | Path to the bundle folder. |

| Returns | Description |
|---------|-------------|
| `LessonMaterial` | Reference material with `pitchContour` and `hpcpFrames` populated. |

| Throws | When |
|--------|------|
| `IllegalArgumentException` | `directoryPath` is blank, the bundle audio cannot be decoded, or the bundle's format `version` is newer than this SDK supports. |

File-not-found / malformed-manifest surface as the underlying I/O or
serialization exception.

## Bundle layout

A bundle directory contains five required files — four with literal names, plus
the audio the manifest names — and one further file per declared accompaniment
track, if the lesson has any.

| File | Contents |
|------|----------|
| `reference-meta.json` | Manifest: tonic (`keyHz`), geometry (`sampleRate` / `hopSize` / `frameSize` / `hpcpSize`), `lessonType`, optional tempo, and `audioFile` — the name of the audio below. |
| the audio (`audioFile`) | Reference audio for playback: a 16 kHz mono WAV, or a compressed master (m4a/mp3) the bundle carries as-is. **Not decoded at load** — the loader references it by path. Bundles written before format 2 have no `audioFile` and always use `reference-16k-mono.wav`. |
| `reference-pitch.tsv` | Pre-computed pitch contour (looked up by time). |
| `reference-hpcp.bin` | Pre-computed HPCP chroma frames (indexed by absolute frame number). |
| `reference-phrases.json` | Phrase boundaries + note-level transcription; the **segment source of truth**. |
| `accompaniment-<n>.<ext>` | One per entry of the manifest's `accompaniment`, if it declares any. See [Accompaniment](#accompaniment). Most bundles have none. |

The manifest's geometry **must match the consuming session** (ADR-017): the live
evaluator indexes HPCP frames by absolute frame number, so `hopSize` /
`frameSize` / `sampleRate` are load-bearing, not cosmetic.

### Accompaniment

A lesson is often more than the recording a learner answers: there may be a
backing recording under it, a repeating pattern over it, a guide part beside it.
The manifest's optional `accompaniment` is that material, in **mix priority**,
and `LessonBundle.load` hands it back as `LessonMaterial.accompaniment`.

```kotlin
val reference = LessonBundle.load("/path/to/bundle-dir")

reference.accompaniment.forEach { track ->
    // track.source  - where the audio is (an AudioSource.File in the bundle)
    // track.loop    - tile it to fill the reference, or play it once through
    // track.transposes - follow the key when the learner moves the tonic
    // track.gainDb  - level relative to the reference
    // track.role    - your own label; VoxaTrace never reads it
}
```

| Field | Type | Meaning |
|-------|------|---------|
| `source` | `AudioSource` | Where the track's audio is. Not decoded at load, like the reference. |
| `loop` | `Boolean` | Tile it to fill the reference, or play it once through. |
| `transposes` | `Boolean` | Follow the key when the learner moves the tonic, or stay fixed. Pitched material has to move; unpitched material has to stay put. |
| `gainDb` | `Float` | Level relative to the reference, in decibels. |
| `role` | `String` | What your app calls this track. Free-form, and **never read by VoxaTrace**: it exists so you can find your own track without this SDK owning the vocabulary that names it. |

Three mechanical switches and one opaque label, deliberately. A track you carry
that nobody here anticipated — a chord pad, a drum machine, a spoken cue — is
described with the same three switches and your own word for it, and needs no
change to the format.

**Order is mix priority**, and the format sets no cap: a device that can only
afford two tracks plays the first two, and should report what it dropped.
**Looping is declared, not performed** — the entry says a track tiles; tiling it
is your player's job.

A bundle that declares none has none, which is every bundle written before this
existed. The bundle format version is unchanged, so an older SDK reading a
bundle that does declare accompaniment plays the reference alone.

### `lessonType` (authoritative)

`lessonType` in the manifest is the authoritative lesson mode, and it drives how
`reference-phrases.json` is interpreted into segments:

- `"singalong"` — one phrase object per phrase; the student sings along with the
  reference.
- `"singafter"` — each phrase is a `teacher_vocal` / `student_vocal` pair,
  cross-linked so the evaluator knows the expected-response window.

## Producing bundles from your own code

The CLI is the usual authoring path (see the
[Lesson Authoring guide](../guides/lesson-authoring)), but you can produce the
same features from your own JVM/app code with `ReferenceExtractor`. It computes
the reference side of a lesson into a `LessonMaterial`; you then serialize its
pieces and write the five files above.

```kotlin
import com.musicmuni.voxatrace.calibra.ReferenceExtractor

val extractor = PitchDetection.createContourExtractor(
    ContourExtractorConfig(algorithm = PitchAlgorithm.MELODIA) // octave-robust
)
val material = ReferenceExtractor.extract(
    samples = referenceSamples, // mono; resampled to 16 kHz internally
    sampleRate = 44100,
    segments = phraseSegments,
    keyHz = 196f,
    contourExtractor = extractor
)
extractor.release()
// material.pitchContour and material.hpcpFrames are now populated.
// Serialize with SonixWriter.formatPitchString / formatHpcp / formatTransString,
// write reference-meta.json + the audio it names, and you have a bundle.
```

### Extract Method

```kotlin
fun extract(
    samples: FloatArray,
    sampleRate: Int,
    segments: List<Segment>,
    keyHz: Float,
    contourExtractor: PitchContourExtractor,
    config: ReferenceExtractorConfig = ReferenceExtractorConfig.DEFAULT
): LessonMaterial
```

| Parameter | Type | Description |
|-----------|------|-------------|
| `samples` | `FloatArray` | Reference audio, mono. Resampled to 16 kHz internally (ADR-017). |
| `sampleRate` | `Int` | Sample rate of `samples` in Hz. |
| `segments` | `List<Segment>` | Phrase boundaries + lyrics for the lesson. Must be non-empty. |
| `keyHz` | `Float` | Tonic frequency in Hz. |
| `contourExtractor` | `PitchContourExtractor` | Pitch contour extractor; caller owns the lifecycle and must `release()` when done. |
| `config` | `ReferenceExtractorConfig` | HPCP frame geometry. **Must match the consuming session.** |

It is an **authoring** helper only: it produces a `LessonMaterial`, never
consumes one. The HPCP frames are computed at `config` geometry, the form the
live evaluator indexes by absolute frame number.

## Versioning

The bundle format is **versioned independently of the SDK** (held by
`BundleManifest.FORMAT_VERSION` and stamped into every bundle the SDK writes).

- `LessonBundle.load` accepts a bundle iff `1 <= version <= FORMAT_VERSION`.
- A bundle authored by a **newer** VoxaTrace (`version > FORMAT_VERSION`) is
  rejected with a clear error: upgrade the SDK to read it.
- Older bundles remain readable.

## Next Steps

- [Lesson Authoring](../guides/lesson-authoring) — produce bundles with the CLI
- [Bundle Format reference](https://github.com/musicmuni/voxatrace/blob/main/docs/bundle-format.md) — the on-disk file contract
- [CalibraMelodyEval](./melody-eval) — offline scoring against a reference
- [CalibraLiveEval](./live-eval) — real-time scoring (consumes pitch + HPCP)
