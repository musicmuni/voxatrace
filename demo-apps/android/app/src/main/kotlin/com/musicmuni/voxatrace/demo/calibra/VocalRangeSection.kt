package com.musicmuni.voxatrace.demo

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
import com.musicmuni.voxatrace.calibra.VocalRangePhase
import com.musicmuni.voxatrace.calibra.VocalRangeSession
import com.musicmuni.voxatrace.calibra.VocalRangeState
import com.musicmuni.voxatrace.sonix.Sonix
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixResampler
import kotlinx.coroutines.launch

/**
 * Vocal Range Detector using VocalRangeSession API.
 *
 * Demonstrates the simplified high-level API with observable state.
 */
@Composable
fun VocalRangeSection() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var session by remember { mutableStateOf<VocalRangeSession?>(null) }
    var recorder by remember { mutableStateOf<SonixRecorder?>(null) }
    val state by session?.state?.collectAsState() ?: remember { mutableStateOf(VocalRangeState()) }

    fun start() {
        // Create session and recorder
        session = VocalRangeSession.create()
        recorder = SonixRecorder.create("${context.cacheDir}/range_temp.m4a", "m4a", "voice")

        // Start session (runs auto-flow)
        session?.start()

        // Start recording and feed audio to session
        recorder?.start()
        scope.launch {
            val hwRate = Sonix.hardwareSampleRate
            recorder?.audioBuffers?.collect { buffer ->
                // Resample to 16kHz and convert to ShortArray
                val resampled = SonixResampler.resample(buffer.floatSamples, hwRate, 16000)
                val samples = ShortArray(resampled.size) { i ->
                    (resampled[i] * 32767).toInt().coerceIn(-32768, 32767).toShort()
                }
                session?.addAudio(samples)
            }
        }
    }

    fun stop() {
        recorder?.stop()
        session?.cancel()
    }

    fun reset() {
        recorder?.stop()
        session?.reset()
    }

    DisposableEffect(Unit) {
        onDispose {
            recorder?.release()
            session?.release()
        }
    }

    // UI
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Vocal Range Detector", style = MaterialTheme.typography.titleMedium)

        Text(
            text = state.error ?: state.phaseMessage,
            style = MaterialTheme.typography.bodyMedium,
            color = if (state.error != null) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Phase indicators
        PhaseIndicators(phase = state.phase)

        // Countdown
        if (state.phase == VocalRangePhase.COUNTDOWN && state.countdownSeconds > 0) {
            Text(
                text = "${state.countdownSeconds}",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        // Real-time pitch display during detection
        if (state.phase == VocalRangePhase.DETECTING_LOW || state.phase == VocalRangePhase.DETECTING_HIGH) {
            PitchDisplayCard(
                noteLabel = state.currentPitch?.noteLabel ?: "-",
                frequencyHz = state.currentPitch?.frequencyHz ?: 0f,
                isStable = state.stabilityProgress >= 1.0f
            )

            // Stability progress
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Stability: Hold for 1 second",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LinearProgressIndicator(
                    progress = { state.stabilityProgress.coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = if (state.stabilityProgress >= 1.0f) Color(0xFF4CAF50)
                            else MaterialTheme.colorScheme.primary
                )
            }

            // Level meter
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Level:", style = MaterialTheme.typography.bodySmall)
                LinearProgressIndicator(
                    progress = { state.currentAmplitude.coerceIn(0f, 1f) },
                    modifier = Modifier.weight(1f).height(8.dp)
                )
            }
        }

        // Results
        state.result?.let { result ->
            ResultsCard(
                lowLabel = result.low.noteLabel,
                lowHz = result.low.frequencyHz,
                highLabel = result.high.noteLabel,
                highHz = result.high.frequencyHz,
                octaves = result.octaves,
                naturalShruti = result.naturalShruti.noteLabel
            )
        }

        // Controls
        Row(modifier = Modifier.fillMaxWidth()) {
            when (state.phase) {
                VocalRangePhase.IDLE -> {
                    Button(onClick = { start() }, modifier = Modifier.weight(1f)) {
                        Text("Start Detection")
                    }
                }
                VocalRangePhase.COMPLETE, VocalRangePhase.CANCELLED -> {
                    Button(onClick = { reset(); start() }, modifier = Modifier.weight(1f)) {
                        Text("Detect Again")
                    }
                }
                else -> {
                    Button(
                        onClick = { stop() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

@Composable
private fun PhaseIndicators(phase: VocalRangePhase) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        PhaseIndicator("Ready", phase == VocalRangePhase.COUNTDOWN, phase.ordinal > VocalRangePhase.COUNTDOWN.ordinal)
        Text("→", modifier = Modifier.padding(top = 8.dp))
        PhaseIndicator("Low", phase == VocalRangePhase.DETECTING_LOW, phase.ordinal > VocalRangePhase.DETECTING_LOW.ordinal)
        Text("→", modifier = Modifier.padding(top = 8.dp))
        PhaseIndicator("High", phase == VocalRangePhase.DETECTING_HIGH, phase == VocalRangePhase.COMPLETE)
        Text("→", modifier = Modifier.padding(top = 8.dp))
        PhaseIndicator("Done", phase == VocalRangePhase.COMPLETE, false)
    }
}

@Composable
private fun PhaseIndicator(label: String, isActive: Boolean, isComplete: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(
                    when {
                        isActive -> MaterialTheme.colorScheme.primary
                        isComplete -> Color(0xFF4CAF50)
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    },
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isComplete) {
                Text("✓", color = Color.White, style = MaterialTheme.typography.labelSmall)
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PitchDisplayCard(noteLabel: String, frequencyHz: Float, isStable: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isStable) Color(0xFF4CAF50).copy(alpha = 0.3f)
                             else MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(noteLabel, style = MaterialTheme.typography.displayMedium)
            Text("${"%.1f".format(frequencyHz)} Hz", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun ResultsCard(
    lowLabel: String, lowHz: Float,
    highLabel: String, highHz: Float,
    octaves: Float, naturalShruti: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Low", style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.7f))
                    Text(lowLabel, style = MaterialTheme.typography.headlineMedium, color = Color.White)
                    Text("${"%.1f".format(lowHz)} Hz", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.7f))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Range", style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.7f))
                    Text("${"%.1f".format(octaves)} oct", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("High", style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.7f))
                    Text(highLabel, style = MaterialTheme.typography.headlineMedium, color = Color.White)
                    Text("${"%.1f".format(highHz)} Hz", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.7f))
                }
            }
            HorizontalDivider(color = Color.White.copy(alpha = 0.3f))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Natural Shruti: ", color = Color.White.copy(alpha = 0.7f))
                Text(naturalShruti, style = MaterialTheme.typography.titleLarge, color = Color.White)
            }
        }
    }
}
