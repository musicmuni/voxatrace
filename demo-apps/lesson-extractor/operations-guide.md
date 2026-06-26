# Running the extractor

This guide is for **operators**: you take the lesson folders prepared by content
creators (see [Preparing lesson content](content-guide.md)), run the extractor,
and upload the output bundles.

## 1. Prerequisites

- **Java 17 or newer.** Check with `java -version`. To install:
  - **macOS** (Apple Silicon), with [Homebrew](https://brew.sh):
    ```bash
    brew install --cask temurin@17
    ```
  - **Ubuntu / Debian**:
    ```bash
    sudo apt update && sudo apt install -y openjdk-17-jdk
    ```
- Your **VoxaTrace API key**. The extractor registers over the network on each
  run, so it needs internet access.
- macOS (Apple Silicon) or Linux x64.

## 2. Get the CLI

Download the latest `lesson-extractor-<version>.zip` from the VoxaTrace releases
page — **https://github.com/musicmuni/voxatrace/releases** — and unzip it. The
same download runs on macOS and Linux.

Confirm what you have any time with `./lesson-extractor/bin/lesson-extractor
--version` (no API key needed).

## 3. Run it

```bash
export VOXATRACE_API_KEY=<your key>
./lesson-extractor/bin/lesson-extractor  inputs/  outputs/
```

It processes every lesson folder under `inputs/` and writes one bundle folder per
lesson under `outputs/`. Lessons missing a file are skipped with a message; the
rest still run.

## 4. The output bundle

Each lesson produces a folder with exactly these files:

```
outputs/raag-bhairavi-aroh-avroh/
    reference-meta.json      # tonic + geometry
    reference-16k-mono.wav   # the reference audio (16 kHz mono)
    reference-pitch.tsv      # pitch contour
    reference-hpcp.bin       # chroma features
    reference-phrases.json   # phrases + note transcription
```

Keep each bundle folder intact (all five files together).

## 5. Upload to S3

**Upload the whole bundle folder to S3.** Your app downloads it on demand and
loads it via VoxaTrace (`LessonBundle` → Calibra); there is no on-device audio
analysis. The bundle format is stable and versioned (see `examples/outputs/` for
finished bundles).

## 6. Troubleshooting

- `ERROR: set VOXATRACE_API_KEY` — export your key first.
- `Skipping '<lesson>': missing …` — that lesson folder is missing the audio,
  `.csv`, or `.meta.json`.
- `could not decode …` — the audio isn't a readable WAV or MP3.
- `meta must provide either 'keyHz' or 'shruti'` — add the tonic to the
  `.meta.json` (ask the content creator).
