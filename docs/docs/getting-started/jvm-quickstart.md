---
sidebar_position: 4
---

# JVM Quickstart

Build a desktop or server program that analyzes audio with VoxaTrace on the JVM
(macOS or Linux).

:::info Requires JDK 17+
The VoxaTrace JVM target needs **JDK 17 or newer** at build and runtime.
:::

## 1. Add the dependency

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.musicmuni:voxatrace-jvm:{{version}}")

    // Native libraries for your platform — pick one:
    runtimeOnly("com.musicmuni:voxatrace-jvm:{{version}}:natives-macos-arm64")
    // runtimeOnly("com.musicmuni:voxatrace-jvm:{{version}}:natives-linux-x64")
}
```

The native code is loaded from the classpath automatically; there is nothing to
put on `java.library.path`.

## 2. Initialize

VoxaTrace must be initialized once before any API call. On the JVM, use the
server initializer with your API key (registration happens over the network, so
connectivity is required):

```kotlin
import com.musicmuni.voxatrace.VT

VT.initializeForServer(apiKey = System.getenv("VOXATRACE_API_KEY"))
```

## 3. Detect pitch from a file

```kotlin
import com.musicmuni.voxatrace.tona.PitchDetection
import com.musicmuni.voxatrace.tona.model.ContourExtractorConfig
import com.musicmuni.voxatrace.tona.model.PitchAlgorithm

fun main() {
    VT.initializeForServer(apiKey = System.getenv("VOXATRACE_API_KEY"))

    // 16 kHz mono float samples (decode/resample with your audio library).
    val samples: FloatArray = loadMono16k("recording.wav")

    // MELODIA is octave-robust — recommended for offline/batch analysis.
    val extractor = PitchDetection.createContourExtractor(
        ContourExtractorConfig(algorithm = PitchAlgorithm.MELODIA)
    )
    try {
        val contour = extractor.extract(samples, sampleRate = 16000)
        val voiced = contour.samples.filter { it.pitch > 0f }
        println("Voiced frames: ${voiced.size}")
        println("Median pitch: ${voiced.map { it.pitch }.sorted()[voiced.size / 2]} Hz")
    } finally {
        extractor.release()
    }
}
```

## Audio I/O on the JVM

`Sonix` players, recorders and the mixer are available on the JVM via
`javax.sound` (best-effort latency; the hardware-clock timestamping used on
mobile is not available). For desktop apps with mobile-grade latency
requirements, follow issue #29.

## AI-backed features (optional)

Neural pitch (`SwiftF0`) and voice-activity detection (`SileroVAD`) require the
AI native artifact:

```kotlin
runtimeOnly("com.musicmuni:voxatrace-jvm:{{version}}:natives-ai-macos-arm64")
```

It is a large download (the ONNX runtime is bundled), so add it only if you use
those features.

## Next Steps

- [Lesson Authoring](../guides/lesson-authoring) - Pre-compute reference bundles
- [Detecting Pitch](../guides/detecting-pitch) - Pitch detection in depth
- [Authentication](../guides/authentication) - Proxy and attestation options
