---
sidebar_label: "HpcpExtractor"
---


# HpcpExtractor

[common]\
expect object [HpcpExtractor](index.md)

Platform-specific HPCP-only feature extraction (expect/actual).

Provides HPCP (Harmonic Pitch Class Profile) extraction without pitch detection. Used when pitch is already available from a detector (single source of truth pattern).

HPCP is a 12-bin chroma feature representing spectral energy distribution across pitch classes (C, C#, D, ..., B). Used for DTW-based scoring.

ADR-003 compliant: expect/actual for platform implementations. ADR-015 compliant: Full capability exposure through all layers.

[android]\
actual object [HpcpExtractor](index.md)

Android actual implementation for standalone HPCP extraction.

Uses dedicated HpcpJni (NOT PitchHpcpExtractorJni) for efficient HPCP extraction without computing pitch. This avoids wastefully computing YIN pitch when pitch is already available from a detector.

[ios]\
actual object [HpcpExtractor](index.md)

iOS actual implementation for standalone HPCP extraction.

Uses dedicated voxatrace_hpcp_* C API (NOT voxatrace_pitch_hpcp_*) for efficient HPCP extraction without computing pitch. This avoids wastefully computing YIN pitch when pitch is already available from a detector.

## Functions

| Name | Summary |
|---|---|
| [createHandle](create-handle.md) | [common]<br/>expect fun [createHandle](create-handle.md)(frameSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), hpcpSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 12): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)?<br/>Create a native handle for standalone HPCP extraction.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [createHandle](create-handle.md)(frameSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), sampleRate: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), hpcpSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)? |
| [destroyHandle](destroy-handle.md) | [common]<br/>expect fun [destroyHandle](destroy-handle.md)(handle: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html))<br/>Destroy a native feature handle.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [destroyHandle](destroy-handle.md)(handle: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)) |
| [extractHpcp](extract-hpcp.md) | [common]<br/>expect fun [extractHpcp](extract-hpcp.md)(handle: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)?<br/>Extract HPCP features from audio samples.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [extractHpcp](extract-hpcp.md)(handle: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)? |
