package com.musicmuni.voxatrace.demo.sections.multitrack.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.musicmuni.voxatrace.demo.sections.multitrack.viewmodel.MultiTrackViewModel

/**
 * MultiTrack View - UI for multi-track mixing demo.
 *
 * Demonstrates:
 * - SonixMixer Builder pattern
 * - Multiple audio track mixing
 * - Per-track volume control
 * - Track fade in/out effects
 */
@Composable
fun MultiTrackView(viewModel: MultiTrackViewModel = viewModel()) {
    val context = LocalContext.current

    // Initialize on first composition
    LaunchedEffect(Unit) {
        viewModel.initialize(context)
    }

    // Collect state from ViewModel
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val currentTimeMs by viewModel.currentTimeMs.collectAsStateWithLifecycle()
    val durationMs by viewModel.durationMs.collectAsStateWithLifecycle()
    val backingVolume by viewModel.backingVolume.collectAsStateWithLifecycle()
    val vocalVolume by viewModel.vocalVolume.collectAsStateWithLifecycle()
    val status by viewModel.status.collectAsStateWithLifecycle()
    val isLoaded by viewModel.isLoaded.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Multi-Track",
            style = MaterialTheme.typography.titleMedium
        )

        Text(text = "Status: $status", style = MaterialTheme.typography.bodySmall)

        // Time display
        Text(
            text = "${MultiTrackViewModel.formatTime(currentTimeMs)} / ${MultiTrackViewModel.formatTime(durationMs)}",
            style = MaterialTheme.typography.bodyMedium
        )

        // Seek slider
        Slider(
            value = if (durationMs > 0) currentTimeMs.toFloat() / durationMs else 0f,
            onValueChange = { fraction -> viewModel.seek(fraction) },
            enabled = isLoaded,
            modifier = Modifier.fillMaxWidth()
        )

        // Backing volume
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Backing:", modifier = Modifier.width(70.dp))
            Slider(
                value = backingVolume,
                onValueChange = { viewModel.setBackingVolume(it) },
                modifier = Modifier.weight(1f)
            )
            Text("${(backingVolume * 100).toInt()}%", modifier = Modifier.width(40.dp))
        }

        // Vocal volume
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Vocal:", modifier = Modifier.width(70.dp))
            Slider(
                value = vocalVolume,
                onValueChange = { viewModel.setVocalVolume(it) },
                modifier = Modifier.weight(1f)
            )
            Text("${(vocalVolume * 100).toInt()}%", modifier = Modifier.width(40.dp))
        }

        // Playback controls
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.play() },
                enabled = isLoaded && !isPlaying
            ) {
                Text("Play")
            }
            Button(
                onClick = { viewModel.pause() },
                enabled = isPlaying
            ) {
                Text("Pause")
            }
            Button(
                onClick = { viewModel.stop() },
                enabled = isLoaded
            ) {
                Text("Stop")
            }
        }

        // Fade buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.fadeVocalDown() },
                enabled = isLoaded
            ) {
                Text("Fade Vocal Down")
            }
            Button(
                onClick = { viewModel.fadeVocalUp() },
                enabled = isLoaded
            ) {
                Text("Fade Vocal Up")
            }
        }
    }
}
