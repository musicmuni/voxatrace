package com.musicmuni.calibra.sample

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.musicmuni.calibra.PitchDetector
import com.musicmuni.calibra.PitchProcessor
import com.musicmuni.sonix.SonixRecorder
import kotlinx.coroutines.launch
import kotlin.math.log2
import kotlin.math.roundToInt

@Composable
fun RealtimePitchSection() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // State for the UI
    var pitch by remember { mutableFloatStateOf(0f) }
    var noteName by remember { mutableStateOf("-") }
    var rms by remember { mutableFloatStateOf(0f) }
    var isVoiced by remember { mutableStateOf(false) }
    var isRecording by remember { mutableStateOf(false) }

    // Calibra handles
    var pitchHandle by remember { mutableLongStateOf(0L) }
    var pitchProcessor by remember { mutableStateOf<PitchProcessor?>(null) }
    var recorder by remember { mutableStateOf<SonixRecorder?>(null) }

    fun startRecording() {
        scope.launch {
            val recordPath = "${context.cacheDir}/realtime_pitch.m4a"
            recorder?.release()
            recorder = SonixRecorder.create(recordPath, "m4a", "voice")

            if (pitchHandle != 0L) PitchDetector.destroy(pitchHandle)
            pitchHandle = PitchDetector.create(1024)
            pitchProcessor = PitchProcessor.create()

            recorder?.start()
            isRecording = true

            // Collect level for RMS
            launch {
                recorder?.level?.collect { level ->
                    rms = level
                }
            }

            // Process audio buffers
            launch {
                recorder?.audioBuffers?.collect { buffer ->
                    // Detect pitch
                    val rawPitch = PitchDetector.detect(
                        handle = pitchHandle,
                        buffer = buffer.data,
                        buffLen = buffer.sampleCount,
                        sampleRate = 16000,
                        tolerance = 0.15f,
                        minPitch = 80f,
                        maxPitch = 1200f,
                        refPitch = 0f,
                        octaveWrap = 0
                    )

                    // Process pitch (smoothing + octave correction)
                    val processedPitch = pitchProcessor?.process(rawPitch) ?: rawPitch
                    pitch = processedPitch

                    // Update voiced state
                    isVoiced = processedPitch > 0

                    // Get note name for the pitch
                    if (processedPitch > 0) {
                        val midiNote = 69 + (12 * log2(processedPitch / 440.0)).roundToInt()
                        noteName = getMidiNoteName(midiNote)
                    } else {
                        noteName = "-"
                    }
                }
            }
        }
    }

    fun stopRecording() {
        scope.launch {
            recorder?.stop()
            recorder?.release()
            recorder = null
            isRecording = false
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            recorder?.release()
            if (pitchHandle != 0L) PitchDetector.destroy(pitchHandle)
            pitchProcessor?.destroy()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Real-time Pitch & VAD",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Pitch: ${"%.2f".format(pitch)} Hz ($noteName)",
            style = MaterialTheme.typography.bodyMedium
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("RMS:", style = MaterialTheme.typography.bodySmall)
            LinearProgressIndicator(
                progress = { rms.coerceIn(0f, 1f) },
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp),
            )
        }

        Text(
            text = "VAD: ${if (isVoiced) "Voiced" else "Silence"}",
            style = MaterialTheme.typography.bodyMedium
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            androidx.compose.material3.Button(
                onClick = { if (isRecording) stopRecording() else startRecording() }
            ) {
                Text(if (isRecording) "Stop" else "Start")
            }
        }
    }
}

private fun getMidiNoteName(midiNote: Int): String {
    val noteNames = arrayOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
    val octave = (midiNote / 12) - 1
    val noteName = noteNames[midiNote % 12]
    return "$noteName$octave"
}
