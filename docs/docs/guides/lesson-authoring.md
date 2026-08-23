---
sidebar_position: 6
---

# Lesson Authoring

A **reference bundle** is a directory of pre-computed reference-lesson features
(audio, pitch, chroma, phrases) that a client app loads at runtime and hands to
singing evaluation, with no pitch/chroma analysis at load time. It is the
hand-off between **authoring** (offline, on the JVM) and **evaluation**
(on-device, via `CalibraLiveEval` / `CalibraMelodyEval`).

The bundle format is a **stable, versioned public contract**: a bundle that
conforms here loads on any VoxaTrace whose bundle format version is greater than
or equal to the bundle's `version`.

## Bundle layout

A bundle is a directory containing exactly these files:

| File | Contents |
|------|----------|
| `reference-meta.json` | Manifest: tonic, geometry, lesson metadata |
| the audio, named by the manifest's `audioFile` | Reference audio for playback: a 16 kHz mono WAV, or a compressed master carried as-is |
| `reference-pitch.tsv` | Pre-computed pitch contour |
| `reference-hpcp.bin` | Pre-computed HPCP chroma frames |
| `reference-phrases.json` | Phrase boundaries + note transcription |
| `accompaniment-<n>.<ext>` | One per declared accompaniment track, if the lesson has any (see below) |

The manifest declares the analysis geometry (`sampleRate`, `hopSize`,
`frameSize`, `hpcpSize`) and the tonic (`keyHz`). The geometry **must match the
consuming session**; `LessonBundle.load` reads the manifest first and validates
the bundle's `version` before reading the payloads.

```json
{
  "version": 1,
  "keyHz": 185.0,
  "sampleRate": 16000,
  "hopSize": 320,
  "frameSize": 1024,
  "hpcpSize": 12,
  "lessonType": "singafter",
  "bpm": null,
  "beatsPerMeasure": 4
}
```

## Authoring with the CLI

The `lesson-extractor` CLI turns reference inputs into bundles. `inputDir`
contains **one sub-folder per lesson**; each lesson folder holds three files:

| File in `<inputDir>/<lesson>/` | Contents |
|--------------------------------|----------|
| an audio file (`.wav` or `.mp3`) | Reference recording — any sample rate / channels / bit depth (decoded, down-mixed to mono, and resampled to 16 kHz internally) |
| a `.csv` | Phrase markers |
| a `.meta.json` | Lesson metadata (tonic, lesson type, and anything else the lesson plays) |
| any further audio files | Whatever else the lesson plays, **each declared in the meta** (see below) |

```bash
export VOXATRACE_API_KEY=sk_live_your_key_here
lesson-extractor <inputDir> <outputDir>
```

Each lesson folder named `<lesson>` produces `<outputDir>/<lesson>/` with the
bundle files above. Pitch is extracted with the octave-robust `MELODIA` backend.

### Declaring what else a lesson plays

A lesson folder may hold more audio than the recording, as long as the meta's
`accompaniment` declares every extra file:

```json
{ "keyHz": 233.08,
  "lessonType": "singafter",
  "accompaniment": [
    { "file": "backing.m4a", "loop": false, "transposes": true,
      "gainDb": 0.0, "role": "backing" }
  ] }
```

`file` names a file in the same lesson folder. The tool copies each into the
bundle, transcoding as it already does for the recording, and carries `loop`,
`transposes`, `gainDb` and `role` through untouched. What they mean is described
in [Lesson Bundles](../calibra/lesson-bundles#accompaniment).

Declaring them is also what identifies the recording: **it is the audio file
nothing declared**. So a folder holding two undeclared audio files fails that
lesson by name rather than one of them quietly becoming the lesson, and so does a
declared file that is not in the folder or is in a format the tool cannot read.
Two things warn rather than fail, because neither makes a bundle unreadable: a
track that plays once and is more than a second from the recording's length
(what a stale upload looks like), and a loop longer than the recording.

To print the CLI version (no API key required):

```bash
lesson-extractor --version
```

Per-platform CLI distributions are published on the releases page.

## Authoring programmatically

If you author from your own JVM code, use the SDK directly (see the
[JVM Quickstart](../getting-started/jvm-quickstart) for setup):

```kotlin
import com.musicmuni.voxatrace.calibra.ReferenceExtractor

val material = ReferenceExtractor.extract(
    samples = mono16kSamples,
    sampleRate = 16000,
    segments = phraseSegments,
    keyHz = tonicHz,
    contourExtractor = contourExtractor, // MELODIA-backed
)
// Serialize material's pieces and write the five bundle files
// (see the bundle layout above).
```

## Consuming a bundle

On the client, load the bundle and hand the result to evaluation:

```kotlin
import com.musicmuni.voxatrace.calibra.LessonBundle

val material = LessonBundle.load("/path/to/bundle-dir")
// material -> CalibraLiveEval / CalibraMelodyEval
```

`LessonBundle.load` rejects a bundle whose `version` is newer than the SDK
supports, with a clear error: upgrade the SDK to read it. Older bundles remain
readable.

## Phrases and transcription

`reference-phrases.json` is the segment source of truth. Each phrase carries a
time window, lyrics/sargam, and optional note events (`t_start`, `t_end`,
`freqHz`, `label`). Note labels follow the VoxaTrace octave convention: a
combining dot above the base letter per octave above the tonic octave (U+0307),
a dot below per octave below (U+0323); e.g. `S`, `Ṡ` (one up), `Ṣ` (one down).
The dot rides the base letter even for Carnatic numbered svaras (`Ṙ1`).

- **singalong** — one phrase object per phrase.
- **singafter** — each phrase is a `teacher_vocal` / `student_vocal` pair,
  cross-linked so the evaluator knows the expected-response window.
