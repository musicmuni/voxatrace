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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.musicmuni.voxatrace.demo.sections.breathmonitor.viewmodel.BreathMonitorState
import com.musicmuni.voxatrace.demo.sections.breathmonitor.viewmodel.BreathMonitorViewModel

/**
 * Breath Monitor View - Duration tracking with VAD and silence inertia.
 */
@Composable
fun BreathMonitorView(viewModel: BreathMonitorViewModel = viewModel()) {
    val context = LocalContext.current

    // Load best score on first composition
    LaunchedEffect(Unit) {
        viewModel.loadBestScore(context)
    }

    val monitoringState by viewModel.monitoringState.collectAsStateWithLifecycle()
    val elapsedSeconds by viewModel.elapsedSeconds.collectAsStateWithLifecycle()
    val bestScore by viewModel.bestScore.collectAsStateWithLifecycle()
    val isVoiceDetected by viewModel.isVoiceDetected.collectAsStateWithLifecycle()
    val recordingLevel by viewModel.recordingLevel.collectAsStateWithLifecycle()
    val status by viewModel.status.collectAsStateWithLifecycle()

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
                    text = BreathMonitorViewModel.formatTime(elapsedSeconds),
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
                    color = if (recordingLevel > 0.01f) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
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
                    text = BreathMonitorViewModel.formatTime(bestScore),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            if (bestScore > 0) {
                TextButton(onClick = { viewModel.resetBestScore(context) }) {
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
                        onClick = { viewModel.startMonitoring(context) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Start")
                    }
                }
                BreathMonitorState.COMPLETE -> {
                    Button(
                        onClick = { viewModel.startMonitoring(context) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Try Again")
                    }
                }
                else -> {
                    Button(
                        onClick = { viewModel.stopMonitoring() },
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
