package com.musicmuni.voxatrace.demo.sections.speakingpitch.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musicmuni.voxatrace.calibra.CalibraMusic
import com.musicmuni.voxatrace.calibra.CalibraSpeakingPitch
import com.musicmuni.voxatrace.sonix.AudioSessionManager
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixRecorderConfig
import com.musicmuni.voxatrace.sonix.SonixResampler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Speaking pitch result data.
 */
data class SpeakingPitchResult(
    val frequencyHz: Float,
    val noteLabel: String
)

/**
 * Speaking Pitch (Shruti) Detector View.
 *
 * Uses CalibraSpeakingPitch.detectFromAudio() - a stateless one-shot detection.
 * Records several seconds of speech, then analyzes offline.
 */
@Composable
fun SpeakingPitchView() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var recorder by remember { mutableStateOf<SonixRecorder?>(null) }
    var recordingJob by remember { mutableStateOf<Job?>(null) }
    var collectedAudio by remember { mutableStateOf(mutableListOf<Float>()) }

    var isRecording by remember { mutableStateOf(false) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var recordingDuration by remember { mutableFloatStateOf(0f) }
    var result by remember { mutableStateOf<SpeakingPitchResult?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    val targetDuration = 5f // seconds of speech needed

    fun startRecording() {
        collectedAudio = mutableListOf()
        result = null
        error = null
        recordingDuration = 0f
        isRecording = true

        recorder = SonixRecorder.create("${context.cacheDir}/shruti_temp.m4a", SonixRecorderConfig.VOICE)
        recorder?.start()

        recordingJob = scope.launch {
            val hwRate = AudioSessionManager.hardwareSampleRate.toInt()
            var sampleCount = 0

            recorder?.audioBuffers?.collect { buffer ->
                val samples16k = SonixResampler.resample(buffer.samples, hwRate, 16000)
                collectedAudio.addAll(samples16k.toList())
                sampleCount += samples16k.size
                recordingDuration = sampleCount / 16000f
            }
        }
    }

    fun stopRecording() {
        recordingJob?.cancel()
        recorder?.stop()
        isRecording = false
    }

    fun analyze() {
        if (collectedAudio.isEmpty()) {
            error = "No audio recorded"
            return
        }

        isAnalyzing = true
        error = null

        scope.launch {
            val audioArray = collectedAudio.toFloatArray()

            val detectedHz = withContext(Dispatchers.Default) {
                CalibraSpeakingPitch.detectFromAudio(audioArray)
            }

            if (detectedHz > 0) {
                val noteLabel = CalibraMusic.hzToNoteLabel(detectedHz)
                result = SpeakingPitchResult(
                    frequencyHz = detectedHz,
                    noteLabel = noteLabel
                )
            } else {
                error = "Could not detect speaking pitch. Try speaking more clearly."
            }

            isAnalyzing = false
        }
    }

    fun reset() {
        stopRecording()
        collectedAudio = mutableListOf()
        result = null
        error = null
        recordingDuration = 0f
    }

    DisposableEffect(Unit) {
        onDispose {
            recordingJob?.cancel()
            recorder?.release()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Speaking Pitch Detector", style = MaterialTheme.typography.titleMedium)

        Text(
            text = "Detects your natural speaking pitch (shruti)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        HorizontalDivider()

        // Instructions
        Text(
            text = when {
                isAnalyzing -> "Analyzing..."
                isRecording -> "Speak naturally for ${targetDuration.toInt()} seconds"
                result != null -> "Detection complete!"
                else -> "Press Start and speak naturally"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Recording progress
        if (isRecording) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Recording: ${"%.1f".format(recordingDuration)}s / ${targetDuration.toInt()}s",
                    style = MaterialTheme.typography.bodySmall
                )
                LinearProgressIndicator(
                    progress = { (recordingDuration / targetDuration).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().height(8.dp)
                )
            }
        }

        // Analysis progress
        if (isAnalyzing) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        // Error display
        error?.let { e ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = e,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // Result display
        result?.let { r ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your Speaking Pitch",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Text(
                        text = r.noteLabel,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${"%.1f".format(r.frequencyHz)} Hz",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when {
                result != null -> {
                    Button(onClick = { reset(); startRecording() }, modifier = Modifier.weight(1f)) {
                        Text("Detect Again")
                    }
                }
                isAnalyzing -> {
                    // Disabled during analysis
                    Button(
                        onClick = { },
                        modifier = Modifier.weight(1f),
                        enabled = false
                    ) {
                        Text("Analyzing...")
                    }
                }
                isRecording -> {
                    Button(
                        onClick = { stopRecording(); analyze() },
                        modifier = Modifier.weight(1f),
                        enabled = recordingDuration >= 2f
                    ) {
                        Text("Stop & Analyze")
                    }
                    Button(
                        onClick = { reset() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Cancel")
                    }
                }
                else -> {
                    Button(onClick = { startRecording() }, modifier = Modifier.weight(1f)) {
                        Text("Start Detection")
                    }
                }
            }
        }
    }
}
