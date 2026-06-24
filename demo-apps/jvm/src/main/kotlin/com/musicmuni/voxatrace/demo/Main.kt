package com.musicmuni.voxatrace.demo

import com.musicmuni.voxatrace.VT
import com.musicmuni.voxatrace.sonix.SonixDecoder
import com.musicmuni.voxatrace.sonix.model.AudioRawData
import com.musicmuni.voxatrace.tona.PitchDetection
import com.musicmuni.voxatrace.tona.model.ContourExtractorConfig
import com.musicmuni.voxatrace.tona.model.PitchAlgorithm
import com.musicmuni.voxatrace.tona.model.PitchContour
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * VoxaTrace desktop/server JVM demo.
 *
 * Menu-driven console app exercising the JVM SDK on real audio across every
 * domain the Android/iOS demos cover (Sonix, Tona, Accura, Tessera, Calibra).
 * Doubles as copy-able client-integration reference: see how the project
 * depends on `voxatrace-jvm` + a per-platform `natives-*` classifier
 * (build.gradle.kts), initializes with [VT.initializeForServer], and calls each
 * facade.
 *
 * Offline demos run on a bundled 16s Alankaar vocal clip (16 kHz mono). Demos
 * that need a sound device (playback, record, metronome, multitrack, live eval)
 * are marked [device] and need speakers/mic.
 *
 * Run:
 *   export VOXATRACE_API_KEY=sk_live_your_key_here
 *   ./gradlew run --console=plain
 */

const val SAMPLE_RATE = 16000

// Tonic of the bundled Alankaar clip (Sa ~= D3); used by histogram/intonation.
const val TONIC_HZ = 146.83f

private val menu: List<Pair<String, () -> Unit>> = listOf(
    "Sonix: audio info (decode)" to ::audioInfoDemo,
    "Sonix: playback [device]" to ::playbackDemo,
    "Sonix: record 3s from mic [device]" to ::recordDemo,
    "Sonix: resample 16k -> 8k" to ::resampleDemo,
    "Sonix: metronome [device]" to ::metronomeDemo,
    "Sonix: MIDI synthesis (to file)" to ::midiDemo,
    "Sonix: multi-track mixer [device]" to ::multitrackDemo,
    "Sonix: notation parsers (.trans/.pitchPP/.notes)" to ::parserDemo,
    "Tona: pitch detection (YIN/MELODIA/SWIFT_F0)" to ::pitchDetectionDemo,
    "Tona: pitch analysis (histogram + tonal segments)" to ::pitchAnalysisDemo,
    "Accura: intonation (EQ + JI)" to ::intonationDemo,
    "Tessera: breath" to ::breathDemo,
    "Tessera: agility" to ::agilityDemo,
    "Tessera: speaking pitch" to ::speakingPitchDemo,
    "Tessera: vocal range" to ::vocalRangeDemo,
    "Tessera: song matching" to ::songMatchingDemo,
    "Tessera: voice profile (combined)" to ::voiceProfileDemo,
    "Calibra: VAD (voice activity)" to ::vadDemo,
    "Calibra: melody eval (offline self-score)" to ::melodyEvalDemo,
    "Calibra: note eval (offline)" to ::noteEvalDemo,
    "Calibra: singalong session setup [device]" to ::singalongDemo,
    "Calibra: singafter session setup [device]" to ::singafterDemo,
)

fun main() {
    val apiKey = System.getenv("VOXATRACE_API_KEY")
    if (apiKey.isNullOrBlank()) {
        System.err.println("ERROR: set VOXATRACE_API_KEY to run the demo.")
        return
    }
    print("Initializing VoxaTrace... ")
    VT.initializeForServer(apiKey = apiKey)
    println("done.")

    while (true) {
        println("\n==== VoxaTrace JVM Demo ====")
        menu.forEachIndexed { i, (label, _) -> println("%2d) %s".format(i + 1, label)) }
        println(" 0) Exit")
        print("> ")
        when (val choice = readlnOrNull()?.trim()) {
            "0", null -> { println("Bye."); return }
            else -> {
                val idx = choice.toIntOrNull()?.minus(1)
                if (idx != null && idx in menu.indices) {
                    runCatching { menu[idx].second() }.onFailure { reportError(it) }
                } else {
                    println("Unknown choice.")
                }
            }
        }
    }
}

// ---- shared helpers (also illustrate the client-side flow) -------------------

/** Decode a bundled WAV to 16 kHz mono samples. */
fun loadSample(resource: String = "/samples/vocal-16k-mono.wav"): AudioRawData {
    val path = resourceToFile(resource)
    return SonixDecoder.decode(path, targetSampleRate = SAMPLE_RATE)
        ?: error("Could not decode $resource (JVM supports WAV/MP3)")
}

/** Run pitch detection and return the contour (caller need not manage the extractor). */
fun contourOf(audio: AudioRawData, algorithm: PitchAlgorithm = PitchAlgorithm.MELODIA): PitchContour {
    val extractor = PitchDetection.createContourExtractor(ContourExtractorConfig(algorithm = algorithm))
    return try {
        extractor.extract(audio.samples, audio.sampleRate)
    } finally {
        extractor.release()
    }
}

/** Extract a bundled resource to a temp file and return its path. */
fun resourceToFile(resource: String): String {
    val dot = resource.lastIndexOf('.')
    val suffix = if (dot >= 0) resource.substring(dot) else ""
    val stream = object {}.javaClass.getResourceAsStream(resource)
        ?: error("Bundled resource not found: $resource")
    val tmp = File.createTempFile("vt-demo", suffix).apply { deleteOnExit() }
    stream.use { Files.copy(it, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING) }
    return tmp.path
}

/** Read a bundled text resource (parser inputs). */
fun resourceText(resource: String): String =
    (object {}.javaClass.getResourceAsStream(resource)
        ?: error("Bundled resource not found: $resource")).bufferedReader().use { it.readText() }

fun reportError(t: Throwable) {
    System.err.println("  ! ${t::class.simpleName}: ${t.message}")
}
