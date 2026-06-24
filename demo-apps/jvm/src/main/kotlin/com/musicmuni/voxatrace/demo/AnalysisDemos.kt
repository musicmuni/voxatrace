package com.musicmuni.voxatrace.demo

import com.musicmuni.voxatrace.accura.Accura
import com.musicmuni.voxatrace.accura.model.IntonationSystem
import com.musicmuni.voxatrace.accura.model.NoteLabelTradition
import com.musicmuni.voxatrace.common.MusicTheory
import com.musicmuni.voxatrace.tessera.Tessera
import com.musicmuni.voxatrace.tessera.TesseraAgility
import com.musicmuni.voxatrace.tessera.TesseraBreath
import com.musicmuni.voxatrace.tessera.TesseraRange
import com.musicmuni.voxatrace.tessera.TesseraSpeakingPitch
import com.musicmuni.voxatrace.tessera.model.BreathConfig
import com.musicmuni.voxatrace.tessera.model.Gender
import com.musicmuni.voxatrace.tona.PitchAnalysis
import com.musicmuni.voxatrace.tona.model.PitchAlgorithm

// ---- Tona -------------------------------------------------------------------

fun pitchDetectionDemo() {
    val audio = loadSample()
    for (algo in listOf(PitchAlgorithm.YIN, PitchAlgorithm.MELODIA, PitchAlgorithm.SWIFT_F0)) {
        try {
            val contour = contourOf(audio, algo)
            val voiced = contour.samples.filter { it.pitch > 0f }
            if (voiced.isEmpty()) {
                println("  $algo: no voiced frames")
            } else {
                val median = voiced.map { it.pitch }.sorted()[voiced.size / 2]
                println("  $algo: ${voiced.size}/${contour.size} voiced, median ${"%.1f".format(median)} Hz")
            }
        } catch (e: Throwable) {
            println("  $algo: unavailable (${e.message})")
        }
    }
}

fun pitchAnalysisDemo() {
    val contour = contourOf(loadSample())
    val histogram = PitchAnalysis.computeHistogram(contour, tonicHz = TONIC_HZ)
    // Top 3 histogram peaks by amplitude.
    val peaks = histogram.values.indices.sortedByDescending { histogram.values[it] }.take(3)
    println("  Histogram peaks (cents @ amplitude):")
    peaks.forEach { i -> println("    ${"%.0f".format(histogram.binCenters[i])} cents @ ${"%.3f".format(histogram.values[i])}") }

    val intervals = MusicTheory.EQ_TEMPERED_INTERVALS_CENTS_BASE.map { it.toFloat() }.toFloatArray()
    val segments = PitchAnalysis.labelByMeanPitch(contour, TONIC_HZ, intervals)
    println("  Tonal segments: ${segments.size}; first few:")
    segments.take(5).forEach { s ->
        println("    [${"%.2f".format(s.startSeconds)}-${"%.2f".format(s.endSeconds)}] ${s.label ?: "?"}")
    }
}

// ---- Accura -----------------------------------------------------------------

fun intonationDemo() {
    val contour = contourOf(loadSample())
    for (system in listOf(IntonationSystem.EQ, IntonationSystem.JI)) {
        val result = Accura.analyzePitching(
            contour = contour,
            tonicHz = TONIC_HZ,
            intonationSystem = system,
            noteLabelTradition = NoteLabelTradition.HINDUSTANI
        )
        val score = Accura.calculateScore(result)
        println("  $system: score ${"%.1f".format(score.score)} over ${score.noteCount} notes")
        result.notes.take(4).forEach { n ->
            println("    ${n.label}: ${"%+.1f".format(n.deviationCents)} cents (score ${"%.2f".format(n.score)})")
        }
    }
}

// ---- Tessera ----------------------------------------------------------------

fun breathDemo() {
    val metrics = TesseraBreath.analyze(contourOf(loadSample()), config = BreathConfig.PRACTICE)
    println("  controlScore=${metrics.controlScore?.let { "%.3f".format(it) } ?: "n/a"}, " +
        "alignmentScore=${metrics.alignmentScore?.let { "%.3f".format(it) } ?: "n/a"}, " +
        "phrases=${metrics.phrases?.let { "yes" } ?: "n/a"}")
}

fun agilityDemo() {
    val ac = TesseraAgility.computeContour(contourOf(loadSample()))
    val score = TesseraAgility.computeScore(ac)
    val scores = score.scores
    if (scores.isEmpty()) { println("  No agility scores"); return }
    println("  ${scores.size} scores, mean ${"%.3f".format(scores.average())}, max ${"%.3f".format(scores.max())}")
}

fun speakingPitchDemo() {
    val audio = loadSample()
    val hz = TesseraSpeakingPitch.detectFromAudio(audio.samples, audio.sampleRate)
    if (hz <= 0f) { println("  Speaking pitch not detected"); return }
    val gender = if (hz < 174.61f) "lower / male-range" else "higher / female-range"
    println("  Speaking pitch ${"%.1f".format(hz)} Hz ($gender)")
}

fun vocalRangeDemo() {
    val result = TesseraRange.computeVocalRange(contourOf(loadSample()))
        ?: run { println("  No vocal range (too little voiced pitch)"); return }
    val r = result.range
    println("  Range: ${r.lower.noteLabel} (${"%.1f".format(r.lower.frequencyHz)} Hz) " +
        "-> ${r.upper.noteLabel} (${"%.1f".format(r.upper.frequencyHz)} Hz), " +
        "${"%.2f".format(r.octaves)} octaves, ${r.semitones} semitones")
}

fun songMatchingDemo() {
    val singer = contourOf(loadSample("/samples/vocal-16k-mono.wav"))
    val song = contourOf(loadSample("/samples/song-16k-mono.wav"))
    val singerVec = TesseraRange.computeSearchVector(singer, normalize = false)
    val songVec = TesseraRange.computeSearchVector(song, normalize = true)
    val match = TesseraRange.computeMatch(singerVec, songVec, singerGender = Gender.FEMALE)
    println("  Match: similarity ${"%.3f".format(match.similarity)}, difficulty ${match.difficulty}/5")
}

fun voiceProfileDemo() {
    val result = Tessera.analyze(contourOf(loadSample()), breathConfig = BreathConfig.PRACTICE)
    println("  breath.controlScore=${result.breath?.controlScore?.let { "%.3f".format(it) } ?: "n/a"}")
    println("  agility.scores=${result.agility?.scores?.size ?: 0}")
    result.vocalRange?.range?.let { r ->
        println("  range=${r.lower.noteLabel}..${r.upper.noteLabel} (${"%.2f".format(r.octaves)} oct)")
    } ?: println("  range=n/a")
}
