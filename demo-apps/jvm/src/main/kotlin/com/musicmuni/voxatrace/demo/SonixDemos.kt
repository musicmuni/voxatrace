package com.musicmuni.voxatrace.demo

import com.musicmuni.voxatrace.sonix.SonixDecoder
import com.musicmuni.voxatrace.sonix.SonixMetronome
import com.musicmuni.voxatrace.sonix.SonixMidiSynthesizer
import com.musicmuni.voxatrace.sonix.SonixMixer
import com.musicmuni.voxatrace.sonix.SonixParser
import com.musicmuni.voxatrace.sonix.SonixPlayer
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixResampler
import com.musicmuni.voxatrace.sonix.SonixToneGenerator
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
    // .wav extension -> WAV format auto-detected. start()/stop() is the simple
    // single-file record path (segment recording needs Builder config).
    val recorder = SonixRecorder.create(out.path)
    val seconds = 6
    println("  [device] Sing after the beep! Recording for ${seconds}s, then a beep stops it.")
    playCue()                 // audible "start" beep
    recorder.start()
    Thread.sleep(seconds * 1000L)
    recorder.stop()
    playCue()                 // audible "stop" beep
    // stop() finalizes the file asynchronously; wait for it to appear.
    val deadline = System.currentTimeMillis() + 5000
    while ((!out.exists() || out.length() < 1024) && System.currentTimeMillis() < deadline) Thread.sleep(100)
    recorder.release()
    if (!out.exists() || out.length() < 1024) { println("  No audio captured (mic permission?)"); return }
    println("  Wrote ${out.path} (${out.length()} bytes).")

    val audio = SonixDecoder.decode(out.path, targetSampleRate = SAMPLE_RATE)
    if (audio != null) {
        val voiced = contourOf(audio).samples.filter { it.pitch > 0f }
        if (voiced.isEmpty()) println("  No voiced frames (silence?)")
        else println("  ${voiced.size} voiced frames, median ${"%.1f".format(voiced.map { it.pitch }.sorted()[voiced.size / 2])} Hz")
    }
    println("  Playing your recording back...")
    playFile(out.path)
}

/**
 * Play a clear beep through the speakers as an audible cue. Uses a SourceDataLine
 * (Clip playback is unreliable on some JVM/OS combinations), and a 250 ms tone so
 * it is unmistakable.
 */
private fun playCue() {
    runCatching {
        val tone = SonixToneGenerator.generate(frequencyHz = 880f, durationMs = 250, sampleRate = 16000)
        val fmt = javax.sound.sampled.AudioFormat(16000f, 16, 1, true, false)
        val line = javax.sound.sampled.AudioSystem.getLine(
            javax.sound.sampled.DataLine.Info(javax.sound.sampled.SourceDataLine::class.java, fmt)
        ) as javax.sound.sampled.SourceDataLine
        line.open(fmt); line.start()
        line.write(tone.audioData, 0, tone.audioData.size)
        line.drain(); line.stop(); line.close()
    }
}

/** Play a file to the speakers and block until it finishes. */
private fun playFile(path: String) {
    runBlocking {
        val player = SonixPlayer.create(path)
        try {
            player.play()
            val start = System.nanoTime()
            while (player.isPlaying.value && (System.nanoTime() - start) < 30_000_000_000L) Thread.sleep(100)
            player.stop()
        } finally {
            player.release()
        }
    }
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
        // Samples load asynchronously; wait for isInitialized before start().
        val ready = System.currentTimeMillis() + 3000
        while (!metronome.isInitialized.value && System.currentTimeMillis() < ready) Thread.sleep(50)
        println("  [device] Metronome at 100 BPM, 4/4 for ~5s (initialized=${metronome.isInitialized.value})...")
        metronome.start()
        repeat(5) {
            Thread.sleep(1000)
            print(" beat=${metronome.currentBeat.value}")
        }
        println()
        metronome.stop()
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
        val (songOk, vocalOk) = runBlocking {
            mixer.addTrack("song", song) to mixer.addTrack("vocal", vocal)
        }
        println("  addTrack song=$songOk vocal=$vocalOk; tracks=${mixer.getTrackNames()}")
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
