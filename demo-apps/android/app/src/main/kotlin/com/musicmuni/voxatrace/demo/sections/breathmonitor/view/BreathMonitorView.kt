package com.musicmuni.voxatrace.demo.sections.breathmonitor.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.musicmuni.voxatrace.demo.sections.breathmonitor.viewmodel.BreathMonitorState
import com.musicmuni.voxatrace.demo.sections.breathmonitor.viewmodel.BreathMonitorViewModel

/**
 * Breath Monitor View.
 *
 * Real-time: voice/silence indicator while recording.
 * On stop: analyzes recording for breath capacity and control.
 */
@Composable
fun BreathMonitorView(viewModel: BreathMonitorViewModel = viewModel()) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()
    val isVoiceDetected by viewModel.isVoiceDetected.collectAsStateWithLifecycle()
    val recordingLevel by viewModel.recordingLevel.collectAsStateWithLifecycle()

    // Live results
    val breathCapacity by viewModel.breathCapacity.collectAsStateWithLifecycle()
    val controlScore by viewModel.controlScore.collectAsStateWithLifecycle()

    // Offline analysis state
    val offlineControlScore by viewModel.offlineControlScore.collectAsStateWithLifecycle()
    val offlineBreathCapacity by viewModel.offlineBreathCapacity.collectAsStateWithLifecycle()
    val offlineVoicedTime by viewModel.offlineVoicedTime.collectAsStateWithLifecycle()
    val isAnalyzingOffline by viewModel.isAnalyzingOffline.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Breath Monitor",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Sing naturally — tap Stop when done to analyze breath capacity and control",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Voice/silence indicator card
        if (state == BreathMonitorState.RECORDING) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isVoiceDetected) Color(0xFF4CAF50) else Color(0xFFFF9800)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(Color.White, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isVoiceDetected) "Voice" else "Silence",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                }
            }

            // Level meter
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
                    color = if (recordingLevel > 0.01f) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
                )
            }
        }

        // Analyzing state
        if (state == BreathMonitorState.ANALYZING) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Analyzing breath...", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        // Results
        if (state == BreathMonitorState.COMPLETE && controlScore != null) {
            ResultCard(
                breathCapacity = breathCapacity,
                controlScore = controlScore!!
            )
        }

        // Control buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (state) {
                BreathMonitorState.IDLE -> {
                    Button(
                        onClick = { viewModel.startRecording(context) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Start")
                    }
                }
                BreathMonitorState.RECORDING -> {
                    Button(
                        onClick = { viewModel.stopRecording() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Stop")
                    }
                }
                BreathMonitorState.COMPLETE -> {
                    Button(
                        onClick = { viewModel.startRecording(context) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Record Again")
                    }
                    OutlinedButton(
                        onClick = { viewModel.reset() }
                    ) {
                        Text("Reset")
                    }
                }
                BreathMonitorState.ANALYZING -> {
                    // No buttons while analyzing
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // Offline Analysis Section
        OfflineAnalysisSection(
            viewModel = viewModel,
            context = context,
            offlineBreathCapacity = offlineBreathCapacity,
            offlineControlScore = offlineControlScore,
            offlineVoicedTime = offlineVoicedTime,
            isAnalyzingOffline = isAnalyzingOffline
        )
    }
}

@Composable
private fun ResultCard(
    breathCapacity: Float?,
    controlScore: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Capacity",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = if (breathCapacity != null) BreathMonitorViewModel.formatTime(breathCapacity) else "—",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Control",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "%.0f%%".format(controlScore * 100),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        controlScore >= 0.7f -> Color(0xFF4CAF50)
                        controlScore >= 0.4f -> Color(0xFFFF9800)
                        else -> MaterialTheme.colorScheme.error
                    }
                )
            }
        }
    }
}

@Composable
private fun OfflineAnalysisSection(
    viewModel: BreathMonitorViewModel,
    context: android.content.Context,
    offlineBreathCapacity: Float,
    offlineControlScore: Float,
    offlineVoicedTime: Float,
    isAnalyzingOffline: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Offline Breath Analysis",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Analyze breath capacity from audio file",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedButton(
            onClick = { viewModel.analyzeOffline(context) },
            enabled = !isAnalyzingOffline
        ) {
            Text("Analyze Alankaar Voice")
        }

        if (isAnalyzingOffline) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (offlineVoicedTime > 0) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Offline Result",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Capacity",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = BreathMonitorViewModel.formatTime(offlineBreathCapacity),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Control",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "%.0f%%".format(offlineControlScore * 100),
                                style = MaterialTheme.typography.titleLarge,
                                color = when {
                                    offlineControlScore >= 0.7f -> Color(0xFF4CAF50)
                                    offlineControlScore >= 0.4f -> Color(0xFFFF9800)
                                    else -> MaterialTheme.colorScheme.error
                                }
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Voiced Time",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = BreathMonitorViewModel.formatTime(offlineVoicedTime),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                }
            }
        }

        ApiInfoCard()
    }
}

@Composable
private fun ApiInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "APIs Demonstrated:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Real-time: CalibraVAD (singingRealtime) for voice detection",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Offline: TesseraBreath.computeScore(config: .PRACTICE) - capacity + control",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Offline: PitchDetection.createContourExtractor() - pitch extraction",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
