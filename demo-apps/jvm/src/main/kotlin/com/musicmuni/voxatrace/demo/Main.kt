package com.musicmuni.voxatrace.demo

import com.musicmuni.voxatrace.VT
import com.musicmuni.voxatrace.sonix.SonixDecoder
import com.musicmuni.voxatrace.sonix.SonixPlayer
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixResampler
import com.musicmuni.voxatrace.tessera.TesseraRange
import com.musicmuni.voxatrace.tona.PitchDetection
import com.musicmuni.voxatrace.tona.model.ContourExtractorConfig
import com.musicmuni.voxatrace.tona.model.PitchAlgorithm
import com.musicmuni.voxatrace.tona.model.PitchContour
import kotlinx.coroutines.runBlocking
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * VoxaTrace desktop/server JVM demo.
 *
 * A menu-driven console app that exercises the SDK on real audio. It doubles as
 * a copy-able reference for client integrations: see how the project depends on
 * `voxatrace-jvm` + a per-platform `natives-*` classifier (build.gradle.kts),
 * initializes with [VT.initializeForServer], and calls each facade.
 *
 * Run:
 *   export VOXATRACE_API_KEY=sk_live_your_key_here
 *   ./gradlew run
 */
fun main() {
    val apiKey = System.getenv("VOXATRACE_API_KEY")
    if (apiKey.isNullOrBlank()) {
        System.err.println("ERROR: set VOXATRACE_API_KEY to run the demo.")
        return
    }
    print("Initializing VoxaTrace... ")
    VT.initializeForServer(apiKey = apiKey)
    println("done.")

    // Bundled 8s vocal clip (16 kHz mono). SonixDecoder on the JVM handles WAV
    // and MP3 (not AAC/M4A); point demos at your own WAV/MP3 via the sample path.
    val sample = extractResource("/samples/vocal-16k-mono.wav", "vocal", ".wav")

    while (true) {
        println(
            """

            ==== VoxaTrace JVM Demo ====
            1) Audio info (decode)
            2) Pitch detection (YIN / MELODIA / SWIFT_F0)
            3) Vocal range (Tessera)
            4) Playback (to speakers)
            5) Record from mic (3s) -> pitch
            6) Resample (16k -> 8k)
            0) Exit
            """.trimIndent()
        )
        print("> ")
        when (readlnOrNull()?.trim()) {
            "1" -> runCatching { audioInfo(sample) }.onFailure { reportError(it) }
            "2" -> runCatching { pitchDemo(sample) }.onFailure { reportError(it) }
            "3" -> runCatching { vocalRangeDemo(sample) }.onFailure { reportError(it) }
            "4" -> runCatching { playbackDemo(sample) }.onFailure { reportError(it) }
            "5" -> runCatching { recordDemo() }.onFailure { reportError(it) }
            "6" -> runCatching { resampleDemo(sample) }.onFailure { reportError(it) }
            "0", null -> { println("Bye."); return }
            else -> println("Unknown choice.")
        }
    }
}

private fun audioInfo(path: String) {
    val audio = SonixDecoder.decode(path, targetSampleRate = null)
        ?: error("Could not decode $path (JVM supports WAV/MP3)")
    println("  sampleRate=${audio.sampleRate} Hz, channels=${audio.numChannels}, " +
        "duration=${audio.durationMilliSecs} ms, samples=${audio.samples.size}")
}

private fun pitchDemo(path: String) {
    val audio = SonixDecoder.decode(path, targetSampleRate = 16000) ?: error("decode failed")
    for (algo in listOf(PitchAlgorithm.YIN, PitchAlgorithm.MELODIA, PitchAlgorithm.SWIFT_F0)) {
        try {
            val contour = detect(audio.samples, audio.sampleRate, algo)
            val voiced = contour.samples.filter { it.pitch > 0f }
            if (voiced.isEmpty()) {
                println("  $algo: no voiced frames")
            } else {
                val median = voiced.map { it.pitch }.sorted()[voiced.size / 2]
                val pct = 100 * voiced.size / contour.size
                println("  $algo: ${voiced.size}/${contour.size} voiced ($pct%), median ${"%.1f".format(median)} Hz")
            }
        } catch (e: Throwable) {
            // SWIFT_F0 needs the AI natives + a model download (network).
            println("  $algo: unavailable (${e.message})")
        }
    }
}

private fun vocalRangeDemo(path: String) {
    val audio = SonixDecoder.decode(path, targetSampleRate = 16000) ?: error("decode failed")
    val contour = detect(audio.samples, audio.sampleRate, PitchAlgorithm.MELODIA)
    val result = TesseraRange.computeVocalRange(contour)
        ?: run { println("  No vocal range (too little voiced pitch)"); return }
    val r = result.range
    println("  Range: ${r.lower.noteLabel} (${"%.1f".format(r.lower.frequencyHz)} Hz) " +
        "-> ${r.upper.noteLabel} (${"%.1f".format(r.upper.frequencyHz)} Hz), " +
        "${"%.2f".format(r.octaves)} octaves, ${r.semitones} semitones")
}

private fun playbackDemo(path: String) {
    runBlocking {
        val player = SonixPlayer.create(path)
        try {
            println("  Playing (press nothing; auto-stops at end)...")
            player.play()
            val start = System.nanoTime()
            while (player.isPlaying.value && (System.nanoTime() - start) < 12_000_000_000L) {
                Thread.sleep(100)
            }
            player.stop()
        } finally {
            player.release()
        }
        println("  Playback done.")
    }
}

private fun recordDemo() {
    val out = File.createTempFile("vt-demo-rec", ".wav").apply { deleteOnExit() }
    val recorder = SonixRecorder.create(out.path)
    println("  Recording 3s from the default mic...")
    recorder.startRecordingSegment(0)
    Thread.sleep(3000)
    val file = runBlocking { recorder.stopRecordingSegment(0) }
    recorder.release()
    if (file == null) { println("  Recording produced no file"); return }
    println("  Wrote $file; analyzing pitch...")
    pitchDemo(file)
}

private fun resampleDemo(path: String) {
    val audio = SonixDecoder.decode(path, targetSampleRate = 16000) ?: error("decode failed")
    val out = SonixResampler.resample(audio.samples, fromRate = 16000, toRate = 8000)
    println("  16k samples=${audio.samples.size} -> 8k samples=${out.size} (ratio ${"%.3f".format(out.size.toFloat() / audio.samples.size)})")
}

private fun detect(samples: FloatArray, sampleRate: Int, algo: PitchAlgorithm): PitchContour {
    val extractor = PitchDetection.createContourExtractor(ContourExtractorConfig(algorithm = algo))
    return try {
        extractor.extract(samples, sampleRate)
    } finally {
        extractor.release()
    }
}

private fun extractResource(resource: String, prefix: String, suffix: String): String {
    val stream = object {}.javaClass.getResourceAsStream(resource)
        ?: error("Bundled resource not found: $resource")
    val tmp = File.createTempFile(prefix, suffix).apply { deleteOnExit() }
    stream.use { Files.copy(it, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING) }
    return tmp.path
}

private fun reportError(t: Throwable) {
    System.err.println("  ! ${t::class.simpleName}: ${t.message}")
}
