package com.musicmuni.voxatrace.demo.sections.vad.view

import android.Manifest
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.musicmuni.voxatrace.calibra.model.VoiceActivityLevel
import com.musicmuni.voxatrace.demo.sections.vad.model.WaveformSample
import com.musicmuni.voxatrace.demo.sections.vad.viewmodel.LiveVADViewModel

/**
 * Voice Activity Detection section using Calibra public API.
 *
 * Demonstrates:
 * - All 4 VAD backends (SPEECH, GENERAL, SINGING_REALTIME, SINGING)
 * - Real-time waveform visualization with VAD overlay
 * - Backend comparison with concurrent processing
 * - Sensitivity/threshold configuration
 */
@Composable
fun VADView() {
    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Voice Activity Detection",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Detects speech/singing using multiple detection backends",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Live") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Compare") }
            )
        }

        HorizontalDivider()

        when (selectedTab) {
            0 -> LiveDetectionView()
            1 -> BackendComparisonView()
        }
    }
}

/**
 * Real-time voice activity detection view with all 4 backends.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LiveDetectionView(viewModel: LiveVADViewModel = viewModel()) {
    val context = LocalContext.current
    val audioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    val vadRatio by viewModel.vadRatio.collectAsStateWithLifecycle()
    val activityLevel by viewModel.activityLevel.collectAsStateWithLifecycle()
    val isRecording by viewModel.isRecording.collectAsStateWithLifecycle()
    val selectedBackendIndex by viewModel.selectedBackendIndex.collectAsStateWithLifecycle()
    val waveformSamples by viewModel.waveformSamples.collectAsStateWithLifecycle()
    val voiceTimeSeconds by viewModel.voiceTimeSeconds.collectAsStateWithLifecycle()
    val silenceTimeSeconds by viewModel.silenceTimeSeconds.collectAsStateWithLifecycle()
    val lastProcessingLatencyMs by viewModel.lastProcessingLatencyMs.collectAsStateWithLifecycle()
    val threshold by viewModel.threshold.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onDisappear()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Backend picker
        BackendPicker(
            selectedIndex = selectedBackendIndex,
            backends = viewModel.backends,
            onSelect = { viewModel.setSelectedBackend(it) }
        )

        // Backend info card
        BackendInfoCard(info = viewModel.backends[selectedBackendIndex])

        HorizontalDivider()

        // Waveform visualization
        LiveWaveformView(samples = waveformSamples)

        // VAD display
        VADDisplayCard(vadRatio = vadRatio, activityLevel = activityLevel)

        // Voice indicator
        if (isRecording) {
            VoiceIndicator(isVoiceDetected = vadRatio > threshold)
        }

        // Statistics
        VADStatsRow(
            voiceTime = voiceTimeSeconds,
            silenceTime = silenceTimeSeconds,
            latencyMs = lastProcessingLatencyMs
        )

        // Sensitivity slider
        SensitivitySlider(
            threshold = threshold,
            onThresholdChange = { viewModel.setThreshold(it) }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Control button
        Button(
            onClick = {
                if (audioPermissionState.status.isGranted) {
                    viewModel.toggleRecording(context)
                } else {
                    audioPermissionState.launchPermissionRequest()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRecording) Color.Red else MaterialTheme.colorScheme.primary
            )
        ) {
            Text(if (isRecording) "Stop" else "Start Detection")
        }

        // API usage info
        ApiInfoCard(viewModel.apiCodeExample)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BackendPicker(
    selectedIndex: Int,
    backends: List<com.musicmuni.voxatrace.demo.sections.vad.model.VADBackendInfo>,
    onSelect: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Backend",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            backends.forEachIndexed { index, info ->
                FilterChip(
                    selected = selectedIndex == index,
                    onClick = { onSelect(index) },
                    label = { Text(info.name) }
                )
            }
        }
    }
}

@Composable
private fun BackendInfoCard(info: com.musicmuni.voxatrace.demo.sections.vad.model.VADBackendInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = info.name,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(
                        text = info.latency,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Text(
                text = info.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = info.guidance,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Blue
            )
        }
    }
}

@Composable
private fun LiveWaveformView(samples: List<WaveformSample>) {
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    val voiceColor = Color(0xFF4CAF50)
    val silenceColor = Color.Gray.copy(alpha = 0.4f)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(backgroundColor, MaterialTheme.shapes.small)
    ) {
        if (samples.isEmpty()) return@Canvas

        val barWidth = size.width / samples.size
        val midY = size.height / 2

        samples.forEachIndexed { index, sample ->
            val normalizedAmp = sample.amplitude.coerceIn(0f, 1f)
            val barHeight = normalizedAmp * size.height * 0.9f
            val color = if (sample.isVoice) voiceColor else silenceColor

            drawRoundRect(
                color = color,
                topLeft = Offset(
                    x = index * barWidth,
                    y = midY - barHeight / 2
                ),
                size = Size(
                    width = (barWidth - 1).coerceAtLeast(1f),
                    height = barHeight.coerceAtLeast(2f)
                ),
                cornerRadius = CornerRadius(1f, 1f)
            )
        }
    }
}

@Composable
private fun VADDisplayCard(vadRatio: Float, activityLevel: VoiceActivityLevel) {
    val color = when (activityLevel) {
        VoiceActivityLevel.NONE -> Color.Gray
        VoiceActivityLevel.PARTIAL -> Color(0xFFFF9800)
        VoiceActivityLevel.FULL -> Color(0xFF4CAF50)
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

@Composable
private fun VoiceIndicator(isVoiceDetected: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val indicatorColor = if (isVoiceDetected) Color(0xFF4CAF50) else Color.Gray
        Surface(
            shape = MaterialTheme.shapes.small,
            color = indicatorColor,
            modifier = Modifier.size(12.dp)
        ) {}
        Text(
            text = if (isVoiceDetected) "Voice Detected" else "Silence",
            style = MaterialTheme.typography.bodyMedium,
            color = if (isVoiceDetected) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun VADStatsRow(
    voiceTime: Float,
    silenceTime: Float,
    latencyMs: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard(
            title = "Voice",
            value = "%.1fs".format(voiceTime),
            highlight = true,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Silence",
            value = "%.1fs".format(silenceTime),
            highlight = false,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Latency",
            value = "${latencyMs}ms",
            highlight = false,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    highlight: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (highlight) Color(0xFF4CAF50) else LocalContentColor.current
            )
        }
    }
}

@Composable
private fun SensitivitySlider(
    threshold: Float,
    onThresholdChange: (Float) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Sensitivity:", style = MaterialTheme.typography.labelSmall)
            Text(
                text = "%.2f".format(threshold),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "High",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Slider(
                value = threshold,
                onValueChange = onThresholdChange,
                valueRange = 0.2f..0.9f,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Low",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = "Lower threshold = more sensitive to quiet sounds",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ApiInfoCard(codeExample: String) {
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
                text = codeExample,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
