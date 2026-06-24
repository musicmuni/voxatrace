# VoxaTrace JVM Demo (desktop / server)

A menu-driven console app that exercises the VoxaTrace JVM SDK on real audio. It
is both a **manual test harness** (for the real-time audio I/O that CI can't
cover) and a **copy-able reference** for client integrations.

## What it shows

- How to depend on `voxatrace-jvm` + a per-platform `natives-*` classifier (and
  the optional `natives-ai-*`) — see `build.gradle.kts`.
- Initializing with `VT.initializeForServer(apiKey = ...)`.
- Calling the facades: decode, pitch detection (YIN / MELODIA / SWIFT_F0),
  vocal range (Tessera), playback, mic recording, resampling.

Native libraries load automatically from the classpath; there is no
`java.library.path` to configure.

## Run

```bash
export VOXATRACE_API_KEY=sk_live_your_key_here
./gradlew run
```

Then pick a menu option. The pitch/range/playback demos run on a bundled 8s
vocal clip (`src/main/resources/samples/vocal-16k-mono.wav`); the record demo
captures 3s from your default microphone and analyzes it.

> SonixDecoder on the JVM handles **WAV and MP3** (not AAC/M4A). Point the demos
> at your own WAV/MP3 to try real material.

## Platform

`build.gradle.kts` defaults to the macOS arm64 native classifiers. On Linux,
switch `nativesClassifier` to `natives-linux-x64` (and `aiNativesClassifier` to
`natives-ai-linux-x64`).

`SWIFT_F0` pitch downloads its neural model on first use (network) and requires
the `natives-ai-*` artifact.
