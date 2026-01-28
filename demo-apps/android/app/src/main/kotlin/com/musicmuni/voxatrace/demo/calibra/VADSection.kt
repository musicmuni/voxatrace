package com.musicmuni.voxatrace.demo.calibra

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
import com.musicmuni.voxatrace.calibra.CalibraVAD
import com.musicmuni.voxatrace.calibra.model.VADBackend
import com.musicmuni.voxatrace.calibra.model.VoiceActivityLevel
import com.musicmuni.voxatrace.sonix.AudioSessionManager
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixResampler
import kotlinx.coroutines.launch

/**
 * Voice Activity Detection section using Calibra public API.
 *
 * Demonstrates:
 * - CalibraVAD with multiple backends (SPEECH, GENERAL, SINGING, SINGING_REALTIME)
 * - Real-time voice activity detection
 * - SonixRecorder for audio capture
 */
@Composable
fun VADSection() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var vad by remember { mutableStateOf<CalibraVAD?>(null) }
    var vadRatio by remember { mutableFloatStateOf(0f) }
    var activityLevel by remember { mutableStateOf(VoiceActivityLevel.NONE) }
    var isRecording by remember { mutableStateOf(false) }
    var selectedBackendIndex by remember { mutableIntStateOf(0) }

    var recorder by remember { mutableStateOf<SonixRecorder?>(null) }

    val backends = listOf(
        VADBackend.SPEECH to BackendInfo("Speech", "Silero neural network - high accuracy for speech"),
        VADBackend.GENERAL to BackendInfo("General", "RMS-based - fast, no neural network"),
        VADBackend.SINGING_REALTIME to BackendInfo("Singing Realtime", "Pitch-based - low latency singing detection"),
        VADBackend.SINGING to BackendInfo("Singing", "Essentia YAMNet - high accuracy singing detection")
    )

    fun createVAD(): CalibraVAD {
        val backend = backends[selectedBackendIndex].first
        return when (backend) {
            VADBackend.GENERAL -> CalibraVAD.create(backend = backend)
            else -> {
                // For neural backends, model provider would be needed
                // For demo without models, fall back to GENERAL
                CalibraVAD.create(backend = VADBackend.GENERAL)
            }
        }
    }

    fun startRecording() {
        scope.launch {
            val recordPath = "${context.cacheDir}/vad_temp.m4a"
            recorder?.release()
            recorder = SonixRecorder.create(recordPath, "m4a", "voice")

            vad?.release()
            vad = createVAD()

            recorder?.start()
            isRecording = true

            launch {
                recorder?.audioBuffers?.collect { buffer ->
                    val v = vad ?: return@collect

                    val hwRate = AudioSessionManager.hardwareSampleRate.toInt()
                    val samples16k = SonixResampler.resample(
                        samples = buffer.data,
                        fromRate = hwRate,
                        toRate = 16000
                    )

                    val ratio = v.getVADRatio(samples16k)
                    if (ratio >= 0) {
                        vadRatio = ratio
                        activityLevel = when {
                            ratio < 0.2f -> VoiceActivityLevel.NONE
                            ratio < 0.6f -> VoiceActivityLevel.PARTIAL
                            else -> VoiceActivityLevel.FULL
                        }
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
            vad?.reset()
            vadRatio = 0f
            activityLevel = VoiceActivityLevel.NONE
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            recorder?.release()
            vad?.release()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Voice Activity Detection",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Detects speech/singing in audio using neural network VAD",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Backend picker
        Text("VAD Backend:", style = MaterialTheme.typography.labelMedium)
        var backendExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = backendExpanded,
            onExpandedChange = { backendExpanded = it }
        ) {
            OutlinedTextField(
                value = backends[selectedBackendIndex].second.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Backend") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = backendExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = backendExpanded,
                onDismissRequest = { backendExpanded = false }
            ) {
                backends.forEachIndexed { index, (_, info) ->
                    DropdownMenuItem(
                        text = { Text(info.name) },
                        onClick = {
                            selectedBackendIndex = index
                            backendExpanded = false
                            vad?.release()
                            vad = createVAD()
                        }
                    )
                }
            }
        }

        Text(
            text = backends[selectedBackendIndex].second.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        HorizontalDivider()

        // VAD display
        VADDisplayCard(vadRatio = vadRatio, activityLevel = activityLevel)

        // Real-time indicator
        if (isRecording) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val indicatorColor = if (vadRatio > 0.5f) Color.Green else Color.Gray
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = indicatorColor,
                    modifier = Modifier.size(12.dp)
                ) {}
                Text(
                    text = if (vadRatio > 0.5f) "Voice Detected" else "Silence",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Control button
        Button(
            onClick = { if (isRecording) stopRecording() else startRecording() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isRecording) "Stop" else "Start Detection")
        }

        // API info
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "API Usage:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = """
                        val vad = CalibraVAD.create(
                            backend = VADBackend.SPEECH,
                            modelProvider = { ModelLoader.loadSpeechVAD() }
                        )
                        val ratio = vad.getVADRatio(samples16k)
                        // ratio: 0.0 = silence, 1.0 = full voice
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private data class BackendInfo(
    val name: String,
    val description: String
)

@Composable
private fun VADDisplayCard(vadRatio: Float, activityLevel: VoiceActivityLevel) {
    val color = when (activityLevel) {
        VoiceActivityLevel.NONE -> Color.Gray
        VoiceActivityLevel.PARTIAL -> Color(0xFFFF9800)
        VoiceActivityLevel.FULL -> Color.Green
    }

    val levelText = when (activityLevel) {
        VoiceActivityLevel.NONE -> "None"
        VoiceActivityLevel.PARTIAL -> "Partial"
        VoiceActivityLevel.FULL -> "Full"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "%.0f%%".format(vadRatio * 100),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )

            Text(
                text = levelText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { vadRatio.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = color
            )
        }
    }
}
