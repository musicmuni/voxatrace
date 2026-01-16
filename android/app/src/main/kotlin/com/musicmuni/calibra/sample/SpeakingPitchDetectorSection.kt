package com.musicmuni.calibra.sample

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.musicmuni.calibra.PitchDetector
import com.musicmuni.calibra.metrics.SpeakingPitchDetector
import com.musicmuni.sonix.SonixRecorder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.log2
import kotlin.math.roundToInt

private const val RMS_THRESHOLD = 0.02f
private const val COUNTDOWN_SECONDS = 4
private const val FEMALE_THRESHOLD_HZ = 174.61f // F3

/**
 * Speaking Pitch/Gender Detector Section - Speech-based pitch detection with gender classification.
 *
 * Flow:
 * 1. User starts detection
 * 2. Listen for sustained speech (RMS > threshold)
 * 3. 4-second countdown when speech detected
 * 4. Collect audio during countdown
 * 5. Process with SpeakingPitchDetector
 * 6. Display speaking pitch and inferred gender
 */
@Composable
fun SpeakingPitchDetectorSection() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // State
    var detectionState by remember { mutableStateOf(SpeakingPitchDetectionState.IDLE) }
    var countdownSeconds by remember { mutableIntStateOf(COUNTDOWN_SECONDS) }
    var currentLevel by remember { mutableFloatStateOf(0f) }
    var detectedPitchHz by remember { mutableFloatStateOf(0f) }
    var detectedPitchNote by remember { mutableStateOf("") }
    var detectedGender by remember { mutableStateOf<SpeakingGender?>(null) }
    var status by remember { mutableStateOf("Speak naturally to detect your speaking pitch") }

    // Audio resources
    var recorder by remember { mutableStateOf<SonixRecorder?>(null) }
    var pitchHandle by remember { mutableLongStateOf(0L) }

    // Collected audio for processing
    var collectedSamples by remember { mutableStateOf<MutableList<Float>>(mutableListOf()) }

    // Start detection
    fun startDetection() {
        scope.launch {
            // Reset state
            detectionState = SpeakingPitchDetectionState.LISTENING
            countdownSeconds = COUNTDOWN_SECONDS
            detectedPitchHz = 0f
            detectedPitchNote = ""
            detectedGender = null
            collectedSamples = mutableListOf()
            status = "Say something..."

            // Create audio resources
            val recordPath = "${context.cacheDir}/speaking_pitch_detect.m4a"
            recorder?.release()
            recorder = SonixRecorder.create(recordPath, "m4a", "voice")

            if (pitchHandle != 0L) PitchDetector.destroy(pitchHandle)
            pitchHandle = PitchDetector.create(1024)

            // Start recording
            recorder?.start()

            // Collect level for detection
            launch {
                recorder?.level?.collect { level ->
                    currentLevel = level

                    when (detectionState) {
                        SpeakingPitchDetectionState.LISTENING -> {
                            if (level > RMS_THRESHOLD) {
                                // Speech detected - start countdown
                                detectionState = SpeakingPitchDetectionState.COUNTDOWN
                                status = "Keep speaking..."

                                // Start countdown coroutine
                                launch {
                                    for (i in COUNTDOWN_SECONDS downTo 1) {
                                        countdownSeconds = i
                                        delay(1000)
                                        if (detectionState != SpeakingPitchDetectionState.COUNTDOWN) break
                                    }

                                    if (detectionState == SpeakingPitchDetectionState.COUNTDOWN) {
                                        // Countdown complete - process
                                        detectionState = SpeakingPitchDetectionState.PROCESSING
                                        status = "Processing..."
                                        recorder?.stop()
                                    }
                                }
                            }
                        }
                        SpeakingPitchDetectionState.COUNTDOWN -> {
                            // Continue collecting during countdown
                            // If silence for too long, could reset (optional)
                        }
                        else -> {}
                    }
                }
            }

            // Collect audio samples for pitch detection
            launch {
                recorder?.audioBuffers?.collect { buffer ->
                    if (detectionState == SpeakingPitchDetectionState.COUNTDOWN) {
                        // Collect samples for processing - convert bytes to floats
                        for (i in 0 until buffer.sampleCount) {
                            collectedSamples.add(buffer.data[i].toFloat() / 128f)
                        }
                    }

                    if (detectionState == SpeakingPitchDetectionState.PROCESSING) {
                        // Process collected audio
                        if (collectedSamples.isNotEmpty()) {
                            val audioArray = collectedSamples.toFloatArray()

                            // Use SpeakingPitchDetector to analyze
                            val pitchHz = SpeakingPitchDetector.detectFromAudio(audioArray, 16000)

                            if (pitchHz > 0) {
                                detectedPitchHz = pitchHz

                                // Get pitch note label
                                val midiNote = 69 + (12 * log2(pitchHz / 440.0)).roundToInt()
                                detectedPitchNote = getMidiNoteNameLocal(midiNote)

                                // Determine gender based on frequency
                                detectedGender = if (pitchHz >= FEMALE_THRESHOLD_HZ) {
                                    SpeakingGender.FEMALE
                                } else {
                                    SpeakingGender.MALE
                                }

                                status = "Detection complete!"
                            } else {
                                status = "Could not detect speaking pitch. Try again."
                            }
                        } else {
                            status = "No audio collected. Try again."
                        }

                        detectionState = SpeakingPitchDetectionState.COMPLETE
                        return@collect
                    }
                }
            }
        }
    }

    // Stop detection
    fun stopDetection() {
        scope.launch {
            recorder?.stop()
            recorder?.release()
            recorder = null
            detectionState = SpeakingPitchDetectionState.IDLE
            status = "Speak naturally to detect your speaking pitch"
        }
    }

    // Cleanup on dispose
    DisposableEffect(Unit) {
        onDispose {
            recorder?.release()
            if (pitchHandle != 0L) PitchDetector.destroy(pitchHandle)
        }
    }

    // UI
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Speaking Pitch & Gender Detector",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = status,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Main display
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when (detectionState) {
                    SpeakingPitchDetectionState.COUNTDOWN -> Color(0xFF2196F3)
                    SpeakingPitchDetectionState.COMPLETE -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (detectionState) {
                    SpeakingPitchDetectionState.LISTENING -> {
                        Text(
                            text = "Listening...",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                    SpeakingPitchDetectionState.COUNTDOWN -> {
                        Text(
                            text = countdownSeconds.toString(),
                            style = MaterialTheme.typography.displayLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Keep speaking",
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                    SpeakingPitchDetectionState.PROCESSING -> {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Analyzing...")
                    }
                    SpeakingPitchDetectionState.COMPLETE -> {
                        if (detectedPitchHz > 0) {
                            // Pitch result
                            Text(
                                text = detectedPitchNote,
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "%.1f Hz".format(detectedPitchHz),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Gender result
                            detectedGender?.let { gender ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Inferred Voice Type:",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Surface(
                                        color = when (gender) {
                                            SpeakingGender.FEMALE -> Color(0xFFE91E63)
                                            SpeakingGender.MALE -> Color(0xFF2196F3)
                                        },
                                        shape = MaterialTheme.shapes.small
                                    ) {
                                        Text(
                                            text = gender.name,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                            color = Color.White,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "No pitch detected",
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }
                    SpeakingPitchDetectionState.IDLE -> {
                        Text(
                            text = "Ready",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Level meter during detection
        if (detectionState == SpeakingPitchDetectionState.LISTENING ||
            detectionState == SpeakingPitchDetectionState.COUNTDOWN) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Level:", style = MaterialTheme.typography.bodySmall)
                LinearProgressIndicator(
                    progress = { currentLevel.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp),
                    color = if (currentLevel > RMS_THRESHOLD) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
                )
            }
        }

        // Control buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (detectionState) {
                SpeakingPitchDetectionState.IDLE -> {
                    Button(
                        onClick = { startDetection() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Start Detection")
                    }
                }
                SpeakingPitchDetectionState.COMPLETE -> {
                    Button(
                        onClick = { startDetection() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Try Again")
                    }
                }
                else -> {
                    Button(
                        onClick = { stopDetection() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }

        // Info text
        Text(
            text = "Speak naturally for a few seconds. Your natural speaking pitch will be detected.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

enum class SpeakingPitchDetectionState {
    IDLE,
    LISTENING,
    COUNTDOWN,
    PROCESSING,
    COMPLETE
}

enum class SpeakingGender {
    MALE,
    FEMALE
}

private fun getMidiNoteNameLocal(midiNote: Int): String {
    val noteNames = arrayOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
    val octave = (midiNote / 12) - 1
    val noteName = noteNames[midiNote % 12]
    return "$noteName$octave"
}
