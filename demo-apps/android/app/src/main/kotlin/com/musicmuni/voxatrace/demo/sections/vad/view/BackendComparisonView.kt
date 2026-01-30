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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.musicmuni.voxatrace.calibra.model.VoiceActivityLevel
import com.musicmuni.voxatrace.demo.sections.vad.model.VADBackendInfo
import com.musicmuni.voxatrace.demo.sections.vad.model.WaveformSample
import com.musicmuni.voxatrace.demo.sections.vad.viewmodel.BackendComparisonViewModel
import kotlin.math.abs

/**
 * Side-by-side comparison view of two VAD backends.
 *
 * Showcases:
 * - Concurrent processing with two different backends
 * - Latency and accuracy differences
 * - Use case guidance for each backend
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BackendComparisonView(viewModel: BackendComparisonViewModel = viewModel()) {
    val context = LocalContext.current
    val audioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    val leftBackendIndex by viewModel.leftBackendIndex.collectAsStateWithLifecycle()
    val rightBackendIndex by viewModel.rightBackendIndex.collectAsStateWithLifecycle()
    val isRecording by viewModel.isRecording.collectAsStateWithLifecycle()

    val vadRatioLeft by viewModel.vadRatioLeft.collectAsStateWithLifecycle()
    val activityLevelLeft by viewModel.activityLevelLeft.collectAsStateWithLifecycle()
    val waveformSamplesLeft by viewModel.waveformSamplesLeft.collectAsStateWithLifecycle()
    val latencyMsLeft by viewModel.latencyMsLeft.collectAsStateWithLifecycle()

    val vadRatioRight by viewModel.vadRatioRight.collectAsStateWithLifecycle()
    val activityLevelRight by viewModel.activityLevelRight.collectAsStateWithLifecycle()
    val waveformSamplesRight by viewModel.waveformSamplesRight.collectAsStateWithLifecycle()
    val latencyMsRight by viewModel.latencyMsRight.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onDisappear()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Compare two backends processing the same audio",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Side-by-side comparison
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Left backend
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BackendDropdown(
                    label = "Left:",
                    selectedIndex = leftBackendIndex,
                    backends = viewModel.backends,
                    onSelect = { viewModel.setLeftBackendIndex(it) },
                    enabled = !isRecording
                )

                ComparisonColumn(
                    info = viewModel.backends[leftBackendIndex],
                    vadRatio = vadRatioLeft,
                    activityLevel = activityLevelLeft,
                    waveformSamples = waveformSamplesLeft,
                    latencyMs = latencyMsLeft,
                    viewModel = viewModel
                )
            }

            // Divider
            VerticalDivider(
                modifier = Modifier.height(300.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )

            // Right backend
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BackendDropdown(
                    label = "Right:",
                    selectedIndex = rightBackendIndex,
                    backends = viewModel.backends,
                    onSelect = { viewModel.setRightBackendIndex(it) },
                    enabled = !isRecording
                )

                ComparisonColumn(
                    info = viewModel.backends[rightBackendIndex],
                    vadRatio = vadRatioRight,
                    activityLevel = activityLevelRight,
                    waveformSamples = waveformSamplesRight,
                    latencyMs = latencyMsRight,
                    viewModel = viewModel
                )
            }
        }

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
            Text(if (isRecording) "Stop Comparison" else "Start Comparison")
        }

        // Comparison insights
        if (isRecording) {
            ComparisonInsights(
                viewModel = viewModel,
                leftInfo = viewModel.backends[leftBackendIndex],
                rightInfo = viewModel.backends[rightBackendIndex],
                latencyMsLeft = latencyMsLeft,
                latencyMsRight = latencyMsRight,
                vadRatioLeft = vadRatioLeft,
                vadRatioRight = vadRatioRight
            )
        }
    }
}

@Composable
private fun BackendDropdown(
    label: String,
    selectedIndex: Int,
    backends: List<VADBackendInfo>,
    onSelect: (Int) -> Unit,
    enabled: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { if (enabled) expanded = it }
        ) {
            OutlinedTextField(
                value = backends[selectedIndex].name,
                onValueChange = {},
                readOnly = true,
                enabled = enabled,
                singleLine = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                textStyle = MaterialTheme.typography.bodySmall
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                backends.forEachIndexed { index, info ->
                    DropdownMenuItem(
                        text = { Text(info.name) },
                        onClick = {
                            onSelect(index)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ComparisonColumn(
    info: VADBackendInfo,
    vadRatio: Float,
    activityLevel: VoiceActivityLevel,
    waveformSamples: List<WaveformSample>,
    latencyMs: Int,
    viewModel: BackendComparisonViewModel
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Backend name and latency
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = info.name,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${latencyMs}ms",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Waveform
        VADWaveformView(
            samples = waveformSamples,
            height = 60.dp.value.toInt()
        )

        // VAD percentage
        Text(
            text = "%.0f%%".format(vadRatio * 100),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = viewModel.colorForLevel(activityLevel)
        )

        // Activity level
        Text(
            text = viewModel.textForLevel(activityLevel),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Use case
        Text(
            text = info.guidance,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Blue,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
private fun ComparisonInsights(
    viewModel: BackendComparisonViewModel,
    leftInfo: VADBackendInfo,
    rightInfo: VADBackendInfo,
    latencyMsLeft: Int,
    latencyMsRight: Int,
    vadRatioLeft: Float,
    vadRatioRight: Float
) {
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
                text = "Insights",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )

            // Latency comparison
            val latencyInsight = when {
                latencyMsLeft < latencyMsRight -> "${leftInfo.name} is faster (${latencyMsRight - latencyMsLeft}ms)"
                latencyMsRight < latencyMsLeft -> "${rightInfo.name} is faster (${latencyMsLeft - latencyMsRight}ms)"
                else -> "Similar latency"
            }
            val latencyColor = if (latencyMsLeft != latencyMsRight) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurfaceVariant

            Text(
                text = latencyInsight,
                style = MaterialTheme.typography.bodySmall,
                color = latencyColor
            )

            // Detection difference
            val diff = abs(vadRatioLeft - vadRatioRight)
            if (diff > 0.2f) {
                Text(
                    text = "Significant detection difference (${(diff * 100).toInt()}%)",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFFF9800)
                )
            }
        }
    }
}

/**
 * Real-time waveform visualization with VAD overlay.
 */
@Composable
fun VADWaveformView(
    samples: List<WaveformSample>,
    height: Int = 80
) {
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    val voiceColor = Color(0xFF4CAF50)
    val silenceColor = Color.Gray.copy(alpha = 0.4f)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
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
