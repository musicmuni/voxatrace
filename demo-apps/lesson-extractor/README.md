# VoxaTrace Lesson Extractor

The lesson extractor turns a reference recording plus phrase markers into a
VoxaTrace **lesson bundle**: a small folder of pre-computed features (pitch,
chroma, phrases with per-note svara transcription) that your app loads at runtime
via VoxaTrace/Calibra, with no audio analysis on the device.

```
recording + markers  ──(lesson-extractor)──►  lesson bundle  ──(S3)──►  your app (VoxaTrace)
```

You run it offline to author bundles, upload each bundle to S3, and your app
downloads and consumes them on demand.

## Two guides, two roles

- **[Preparing lesson content](content-guide.md)** — for content creators: how to
  organize a lesson's inputs, the file formats, naming, sargam notation, and the
  `svaraMask`. No coding.
- **[Running the extractor](operations-guide.md)** — for operators: how to get the
  CLI, run it, and upload the output bundles.

## Get the CLI

Download the latest `lesson-extractor-<version>.zip` from the project's **GitHub
Releases** page and unzip it. One download runs on **macOS (Apple Silicon)** and
**Linux x64** — the binary picks the right native libraries at runtime.

```
lesson-extractor/
  bin/lesson-extractor      # run this
  lib/                      # bundled SDK + native libraries
```

## Worked example

`examples/inputs/` holds two ready-to-run lessons, one of each lesson type:

- `raag-bhairavi-aroh-avroh/` — a **singalong** exercise (Hindustani).
- `devamanohari-jatiswara/` — a **singafter** call-and-response lesson (Carnatic).

`examples/outputs/` holds the bundles the extractor produced from them. Following
[Running the extractor](operations-guide.md) against `examples/inputs/`
reproduces `examples/outputs/`.
