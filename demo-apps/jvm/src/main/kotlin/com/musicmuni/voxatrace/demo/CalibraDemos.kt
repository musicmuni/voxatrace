package com.musicmuni.voxatrace.demo

import com.musicmuni.voxatrace.calibra.CalibraLiveEval
import com.musicmuni.voxatrace.calibra.CalibraMelodyEval
import com.musicmuni.voxatrace.calibra.CalibraNoteEval
import com.musicmuni.voxatrace.calibra.CalibraVAD
import com.musicmuni.voxatrace.calibra.ExercisePattern
import com.musicmuni.voxatrace.calibra.model.LessonMaterial
import com.musicmuni.voxatrace.calibra.model.LessonType
import com.musicmuni.voxatrace.calibra.model.NoteEvalPreset
import com.musicmuni.voxatrace.calibra.model.Segment
import com.musicmuni.voxatrace.calibra.model.VADModelProvider
import com.musicmuni.voxatrace.common.MusicTheory
import com.musicmuni.voxatrace.tona.PitchDetection
import com.musicmuni.voxatrace.tona.model.ContourExtractorConfig
import com.musicmuni.voxatrace.tona.model.PitchAlgorithm

fun vadDemo() {
    val audio = loadSample()
    // General (DSP) backend works offline on a buffer.
    val general = CalibraVAD.create(VADModelProvider.General)
    println("  General VAD ratio: ${"%.3f".format(general.getVADRatio(audio.samples, audio.sampleRate))}")
    // Neural backends need the AI natives + a model download (network).
    runCatching {
        val speech = CalibraVAD.create(VADModelProvider.speech())
        println("  Speech (Silero) VAD ratio: ${"%.3f".format(speech.getVADRatio(audio.samples, audio.sampleRate))}")
    }.onFailure { println("  Speech VAD unavailable (${it.message})") }
}

fun melodyEvalDemo() {
    val audio = loadSample()
    val durSec = audio.samples.size.toFloat() / audio.sampleRate
    val segments = listOf(Segment(index = 0, startSeconds = 0f, endSeconds = durSec, lyrics = "full clip"))

    val reference = LessonMaterial.fromAudio(audio.samples, audio.sampleRate, segments, keyHz = TONIC_HZ)
    // Offline self-evaluation: score the reference against itself (should be high).
    val student = LessonMaterial.fromAudio(audio.samples, audio.sampleRate, segments, keyHz = TONIC_HZ)

    val extractor = PitchDetection.createContourExtractor(ContourExtractorConfig(algorithm = PitchAlgorithm.MELODIA))
    try {
        val result = CalibraMelodyEval.evaluate(reference, student, extractor)
        println("  Self-eval overall score: ${result.overallScorePercent}% (${"%.3f".format(result.overallScore)})")
    } finally {
        extractor.release()
    }
}

fun noteEvalDemo() {
    val audio = loadSample()
    val student = contourOf(audio)
    // A simple ascending exercise (C major pentad), 500ms per note.
    val pattern = ExercisePattern.fromMidiNotes(listOf(60, 62, 64, 65, 67), noteDurationMs = 500)
    val result = CalibraNoteEval.evaluate(
        pattern = pattern,
        student = student,
        referenceKeyHz = MusicTheory.midiToHz(60f),
        preset = NoteEvalPreset.BALANCED
    )
    println("  Note-eval score: ${result.scorePercent}% over ${result.noteResults.size} notes")
    result.noteResults.take(5).forEach { n ->
        println("    note ${n.noteIndex} (${"%.1f".format(n.expectedFrequencyHz)} Hz): ${n.scorePercent}%")
    }
}

fun singalongDemo() = liveSessionSetup(callResponse = false)
fun singafterDemo() = liveSessionSetup(callResponse = true)

/**
 * Live evaluation is interactive (it drives playback + mic over a session
 * clock), so the console demo builds the reference + detector and constructs the
 * session to show the integration, then notes that running it needs a device.
 */
private fun liveSessionSetup(callResponse: Boolean) {
    val audio = loadSample()
    val durSec = audio.samples.size.toFloat() / audio.sampleRate
    val mid = durSec / 2f
    val segments = if (callResponse) {
        // singafter: teacher window + paired student-response window.
        listOf(
            Segment(0, 0f, mid, "phrase 1", studentStartSeconds = mid, studentEndSeconds = durSec)
        )
    } else {
        // singalong: sing over the reference.
        listOf(Segment(0, 0f, durSec, "phrase 1"))
    }

    // The lesson-level declaration is the authority for call/response behavior;
    // student windows on segments carry the timing, lessonType says what it means.
    val lessonType = if (callResponse) LessonType.SINGAFTER else LessonType.SINGALONG
    val reference = LessonMaterial.fromAudio(
        audio.samples, audio.sampleRate, segments, keyHz = TONIC_HZ, lessonType = lessonType
    )
    val detector = PitchDetection.createDetector()
    val session = CalibraLiveEval.create(reference = reference, detector = detector)
    try {
        val mode = if (callResponse) "singafter (call/response)" else "singalong"
        println("  $mode session constructed with ${segments.size} segment(s).")
        println("  [device] Interactive run needs a mic + speaker: prepareSession() then")
        println("  startPracticingSegment(i); observe session.state for live scoring.")
    } finally {
        runCatching { session.finishSession() }
    }
}
