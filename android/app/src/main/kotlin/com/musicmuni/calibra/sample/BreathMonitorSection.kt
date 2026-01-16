package com.musicmuni.calibra.sample

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.musicmuni.calibra.PitchDetector
import com.musicmuni.sonix.SonixRecorder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val PREFS_NAME = "breath_monitor_prefs"
private const val KEY_BEST_SCORE = "best_score"
private const val RMS_THRESHOLD = 0.01f
private const val SILENCE_GRACE_MS = 500L

/**
 * Breath Monitor Section - Duration tracking with VAD and silence inertia.
 *
 * Flow:
 * 1. User starts monitoring
 * 2. Wait for voice to be detected
 * 3. Start timer when singing begins
 * 4. Allow 500ms silence grace period
 * 5. Stop timer when silence exceeds grace period
 * 6. Compare with best score and persist
 */
@Composable
fun BreathMonitorSection() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // State
    var monitoringState by remember { mutableStateOf(BreathMonitorState.IDLE) }
    var elapsedSeconds by remember { mutableFloatStateOf(0f) }
    var bestScore by remember { mutableFloatStateOf(loadBestScore(context)) }
    var isVoiceDetected by remember { mutableStateOf(false) }
    var recordingLevel by remember { mutableFloatStateOf(0f) }
    var currentPitch by remember { mutableFloatStateOf(0f) }
    var status by remember { mutableStateOf("Hold a note as long as you can!") }

    // Audio resources
    var recorder by remember { mutableStateOf<SonixRecorder?>(null) }
    var pitchHandle by remember { mutableLongStateOf(0L) }

    // Timing
    var startTimeMs by remember { mutableLongStateOf(0L) }
    var lastVoiceTimeMs by remember { mutableLongStateOf(0L) }

    // Start monitoring
    fun startMonitoring() {
        scope.launch {
            // Reset state
            elapsedSeconds = 0f
            isVoiceDetected = false
            monitoringState = BreathMonitorState.WAITING_FOR_VOICE
            status = "Start singing when ready..."

            // Create audio resources
            val recordPath = "${context.cacheDir}/breath_monitor.m4a"
            recorder?.release()
            recorder = SonixRecorder.create(recordPath, "m4a", "voice")

            if (pitchHandle != 0L) PitchDetector.destroy(pitchHandle)
            pitchHandle = PitchDetector.create(1024)

            // Start recording
            recorder?.start()

            // Collect level for meter
            launch {
                recorder?.level?.collect { level ->
                    recordingLevel = level
                }
            }

            // Main detection loop
            launch {
                recorder?.audioBuffers?.collect { buffer ->
                    if (monitoringState == BreathMonitorState.IDLE ||
                        monitoringState == BreathMonitorState.COMPLETE
                    ) {
                        return@collect
                    }

                    // Detect pitch
                    val pitch = PitchDetector.detect(
                        pitchHandle,
                        buffer.data,
                        buffer.sampleCount,
                        16000,
                        0.15f,
                        80f,
                        1200f,
                        0f,
                        0
                    )
                    currentPitch = pitch

                    // Voice is detected if pitch is valid (> 0) and level is above threshold
                    val hasVoice = pitch > 0 && recordingLevel > RMS_THRESHOLD
                    val currentTimeMs = System.currentTimeMillis()

                    when (monitoringState) {
                        BreathMonitorState.WAITING_FOR_VOICE -> {
                            if (hasVoice) {
                                // Voice detected - start timing
                                monitoringState = BreathMonitorState.SINGING
                                startTimeMs = currentTimeMs
                                lastVoiceTimeMs = currentTimeMs
                                isVoiceDetected = true
                                status = "Keep going!"
                            }
                        }
                        BreathMonitorState.SINGING -> {
                            if (hasVoice) {
                                // Voice still present - update last voice time
                                lastVoiceTimeMs = currentTimeMs
                                isVoiceDetected = true
                            } else {
                                // No voice - check grace period
                                isVoiceDetected = false
                                val silenceDuration = currentTimeMs - lastVoiceTimeMs

                                if (silenceDuration > SILENCE_GRACE_MS) {
                                    // Grace period exceeded - stop
                                    monitoringState = BreathMonitorState.COMPLETE
                                    elapsedSeconds = (lastVoiceTimeMs - startTimeMs) / 1000f

                                    // Update best score if needed
                                    if (elapsedSeconds > bestScore) {
                                        bestScore = elapsedSeconds
                                        saveBestScore(context, bestScore)
                                        status = "New record! ${formatTime(elapsedSeconds)}"
                                    } else {
                                        status = "Good try! ${formatTime(elapsedSeconds)}"
                                    }

                                    recorder?.stop()
                                }
                            }
                        }
                        else -> {}
                    }

                    // Update elapsed time during singing
                    if (monitoringState == BreathMonitorState.SINGING) {
                        elapsedSeconds = (currentTimeMs - startTimeMs) / 1000f
                    }
                }
            }
        }
    }

    // Stop monitoring
    fun stopMonitoring() {
        scope.launch {
            recorder?.stop()
            recorder?.release()
            recorder = null
            monitoringState = BreathMonitorState.IDLE
            status = "Hold a note as long as you can!"
        }
    }

    // Reset best score
    fun resetBestScore() {
        bestScore = 0f
        saveBestScore(context, 0f)
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
            text = "Breath Monitor",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = status,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Main timer display
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when (monitoringState) {
                    BreathMonitorState.SINGING -> if (isVoiceDetected) Color(0xFF4CAF50) else Color(0xFFFF9800)
                    BreathMonitorState.COMPLETE -> MaterialTheme.colorScheme.primaryContainer
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
                Text(
                    text = formatTime(elapsedSeconds),
                    style = MaterialTheme.typography.displayLarge,
                    color = when (monitoringState) {
                        BreathMonitorState.SINGING -> Color.White
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )

                if (monitoringState == BreathMonitorState.SINGING) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(
                                    if (isVoiceDetected) Color.White else Color.White.copy(alpha = 0.5f),
                                    CircleShape
                                )
                        )
                        Text(
                            text = if (isVoiceDetected) "Voice detected" else "Silence...",
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Level meter
        if (monitoringState != BreathMonitorState.IDLE) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Level:", style = MaterialTheme.typography.bodySmall)
                LinearProgressIndicator(
                    progress = { recordingLevel.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp),
                    color = if (recordingLevel > RMS_THRESHOLD) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
                )
            }
        }

        // Best score display
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Best: ", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = formatTime(bestScore),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            if (bestScore > 0) {
                TextButton(onClick = { resetBestScore() }) {
                    Text("Reset")
                }
            }
        }

        // Control buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (monitoringState) {
                BreathMonitorState.IDLE -> {
                    Button(
                        onClick = { startMonitoring() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Start")
                    }
                }
                BreathMonitorState.COMPLETE -> {
                    Button(
                        onClick = { startMonitoring() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Try Again")
                    }
                }
                else -> {
                    Button(
                        onClick = { stopMonitoring() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Stop")
                    }
                }
            }
        }
    }
}

enum class BreathMonitorState {
    IDLE,
    WAITING_FOR_VOICE,
    SINGING,
    COMPLETE
}

private fun formatTime(seconds: Float): String {
    val mins = (seconds / 60).toInt()
    val secs = seconds % 60
    return if (mins > 0) {
        "%d:%05.2f".format(mins, secs)
    } else {
        "%.2f".format(secs)
    }
}

private fun loadBestScore(context: Context): Float {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getFloat(KEY_BEST_SCORE, 0f)
}

private fun saveBestScore(context: Context, score: Float) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit().putFloat(KEY_BEST_SCORE, score).apply()
}
