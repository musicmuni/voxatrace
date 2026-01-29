package com.musicmuni.voxatrace.demo.sections.metronome.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.musicmuni.voxatrace.demo.components.OptionChip
import com.musicmuni.voxatrace.demo.sections.metronome.viewmodel.MetronomeViewModel

/**
 * Metronome View - UI for metronome demo.
 *
 * Demonstrates:
 * - SonixMetronome Builder pattern
 * - Beat callback for UI synchronization
 * - BPM and volume control
 * - Beat visualization
 */
@Composable
fun MetronomeView(viewModel: MetronomeViewModel = viewModel()) {
    val context = LocalContext.current

    // Initialize on first composition
    LaunchedEffect(Unit) {
        viewModel.initialize(context)
    }

    // Collect state from ViewModel
    val isRunning by viewModel.isRunning.collectAsStateWithLifecycle()
    val bpm by viewModel.bpm.collectAsStateWithLifecycle()
    val beatsPerCycle by viewModel.beatsPerCycle.collectAsStateWithLifecycle()
    val currentBeat by viewModel.currentBeat.collectAsStateWithLifecycle()
    val volume by viewModel.volume.collectAsStateWithLifecycle()
    val status by viewModel.status.collectAsStateWithLifecycle()
    val isInitialized by viewModel.isInitialized.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Metronome",
            style = MaterialTheme.typography.titleMedium
        )

        Text(text = "Status: $status", style = MaterialTheme.typography.bodySmall)

        // BPM slider
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("BPM:", modifier = Modifier.width(40.dp))
            Slider(
                value = (bpm - 60f) / 140f,  // 60-200 range
                onValueChange = {
                    val newBpm = 60f + (it * 140f)
                    viewModel.setBpm(newBpm)
                },
                modifier = Modifier.weight(1f)
            )
            Text("${bpm.toInt()}", modifier = Modifier.width(40.dp))
        }

        // Beats per cycle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Beats:", modifier = Modifier.width(50.dp))
            listOf(3, 4, 6, 8).forEach { count ->
                OptionChip(
                    selected = beatsPerCycle == count,
                    onClick = { viewModel.setBeatsPerCycle(count) },
                    label = count.toString(),
                    enabled = !isRunning
                )
            }
        }

        // Volume
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Vol:", modifier = Modifier.width(40.dp))
            Slider(
                value = volume,
                onValueChange = { viewModel.setVolume(it) },
                modifier = Modifier.weight(1f)
            )
            Text("${(volume * 100).toInt()}%", modifier = Modifier.width(40.dp))
        }

        // Beat indicators
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Beat:", modifier = Modifier.width(50.dp))
            (0 until beatsPerCycle).forEach { beat ->
                val isCurrentBeat = beat == currentBeat && isRunning
                val isSama = beat == 0
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCurrentBeat && isSama -> Color.Red
                                isCurrentBeat -> Color.Green
                                isSama -> Color.Red.copy(alpha = 0.3f)
                                else -> Color.Gray.copy(alpha = 0.3f)
                            }
                        )
                )
            }
        }

        // Control buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.start() },
                enabled = isInitialized && !isRunning
            ) {
                Text("Start")
            }
            Button(
                onClick = { viewModel.stop() },
                enabled = isRunning
            ) {
                Text("Stop")
            }
        }
    }
}
