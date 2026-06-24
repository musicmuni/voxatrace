package com.musicmuni.voxatrace.demo

import com.musicmuni.voxatrace.sonix.SonixDecoder
import com.musicmuni.voxatrace.sonix.SonixMetronome
import com.musicmuni.voxatrace.sonix.SonixMidiSynthesizer
import com.musicmuni.voxatrace.sonix.SonixMixer
import com.musicmuni.voxatrace.sonix.SonixParser
import com.musicmuni.voxatrace.sonix.SonixPlayer
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixResampler
import com.musicmuni.voxatrace.sonix.midi.MidiNote
import kotlinx.coroutines.runBlocking
import java.io.File

fun audioInfoDemo() {
    val audio = loadSample()
    println("  sampleRate=${audio.sampleRate} Hz, channels=${audio.numChannels}, " +
        "duration=${audio.durationMilliSecs} ms, samples=${audio.samples.size}")
}

fun playbackDemo() {
    val path = resourceToFile("/samples/vocal-16k-mono.wav")
    runBlocking {
        val player = SonixPlayer.create(path)
        try {
            println("  [device] Playing to speakers...")
            player.play()
            val start = System.nanoTime()
            while (player.isPlaying.value && (System.nanoTime() - start) < 20_000_000_000L) {
                Thread.sleep(100)
            }
            player.stop()
        } finally {
            player.release()
        }
        println("  Playback done.")
    }
}

fun recordDemo() {
    val out = File.createTempFile("vt-demo-rec", ".wav").apply { deleteOnExit() }
    val recorder = SonixRecorder.create(out.path)
    println("  [device] Recording 3s from the default mic...")
    recorder.startRecordingSegment(0)
    Thread.sleep(3000)
    val file = runBlocking { recorder.stopRecordingSegment(0) }
    recorder.release()
    if (file == null) { println("  Recording produced no file"); return }
    println("  Wrote $file; analyzing pitch...")
    val audio = SonixDecoder.decode(file, targetSampleRate = SAMPLE_RATE) ?: return
    val voiced = contourOf(audio).samples.filter { it.pitch > 0f }
    if (voiced.isEmpty()) println("  No voiced frames detected")
    else println("  ${voiced.size} voiced frames, median ${"%.1f".format(voiced.map { it.pitch }.sorted()[voiced.size / 2])} Hz")
}

fun resampleDemo() {
    val audio = loadSample()
    val out = SonixResampler.resample(audio.samples, fromRate = SAMPLE_RATE, toRate = 8000)
    println("  16k samples=${audio.samples.size} -> 8k samples=${out.size} " +
        "(ratio ${"%.3f".format(out.size.toFloat() / audio.samples.size)})")
}

fun metronomeDemo() {
    val sama = resourceToFile("/samples/sama_click.wav")
    val beat = resourceToFile("/samples/beat_click.wav")
    val metronome = SonixMetronome.create(
        samaSamplePath = sama,
        beatSamplePath = beat,
        bpm = 100f,
        beatsPerCycle = 4
    )
    try {
        println("  [device] Metronome at 100 BPM, 4/4 for ~5s...")
        metronome.start()
        Thread.sleep(5000)
        metronome.stop()
        println("  Stopped at beat ${metronome.currentBeat.value}")
    } finally {
        metronome.release()
    }
}

fun midiDemo() {
    val sf2 = resourceToFile("/samples/harmonium.sf2")
    val synth = SonixMidiSynthesizer.create(sf2)
    try {
        // C major scale, 400ms notes with 100ms gaps (times in milliseconds).
        val midi = listOf(60, 62, 64, 65, 67, 69, 71, 72)
        val notes = midi.mapIndexed { i, n ->
            MidiNote(note = n, startTime = i * 500f, endTime = i * 500f + 400f)
        }
        val out = File.createTempFile("vt-demo-midi", ".wav").apply { deleteOnExit() }
        val ok = synth.synthesizeFromNotes(notes, out.path)
        if (ok) println("  Synthesized C-major scale -> ${out.path} (${out.length()} bytes)")
        else println("  MIDI synthesis failed")
    } finally {
        synth.release()
    }
}

fun multitrackDemo() {
    val song = resourceToFile("/samples/song-16k-mono.wav")
    val vocal = resourceToFile("/samples/vocal-16k-mono.wav")
    val mixer = SonixMixer.create()
    try {
        runBlocking {
            mixer.addTrack("song", song)
            mixer.addTrack("vocal", vocal)
        }
        println("  [device] Mixing 'song' + 'vocal' (duration ${mixer.duration} ms) for ~5s...")
        mixer.setTrackVolume("song", 0.7f)
        mixer.play()
        Thread.sleep(5000)
        mixer.stop()
    } finally {
        mixer.release()
    }
    println("  Multi-track done.")
}

fun parserDemo() {
    SonixParser.parseTransString(resourceText("/samples/alankaar.trans"))?.let { trans ->
        println("  .trans: ${trans.segments.size} segments")
        trans.segments.take(2).forEach { seg ->
            println("    seg ${seg.id}: [${"%.2f".format(seg.startTime)}, ${"%.2f".format(seg.endTime)}] " +
                "\"${seg.lyrics.trim()}\", ${seg.trans.size} notes")
        }
    } ?: println("  .trans: parse failed")

    SonixParser.parsePitchString(resourceText("/samples/alankaar.pitchPP"))?.let { pitch ->
        val voiced = pitch.pitchesHz.count { it > 0f }
        println("  .pitchPP: ${pitch.count} frames, $voiced voiced")
    } ?: println("  .pitchPP: parse failed")

    SonixParser.parseNotesString(resourceText("/samples/alankaar.notes"))?.let { notes ->
        println("  .notes: ${notes.labels.size} notes; first labels: ${notes.labels.take(6).joinToString(" ")}")
    } ?: println("  .notes: parse failed")
}
