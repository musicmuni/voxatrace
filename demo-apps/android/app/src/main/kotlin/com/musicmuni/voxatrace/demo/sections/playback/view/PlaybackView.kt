package com.musicmuni.voxatrace.demo.sections.playback.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.musicmuni.voxatrace.demo.components.OptionChip
import com.musicmuni.voxatrace.demo.sections.playback.viewmodel.PlaybackViewModel

/**
 * Playback View - UI for audio playback demo.
 *
 * Demonstrates:
 * - SonixPlayer Builder pattern
 * - Real-time volume and pitch control
 * - Loop count configuration
 * - Seek slider
 * - Fade in/out effects
 */
@Composable
fun PlaybackView(viewModel: PlaybackViewModel = viewModel()) {
    val context = LocalContext.current

    // Initialize player on first composition
    LaunchedEffect(Unit) {
        viewModel.initialize(context)
    }

    // Stop playback when leaving the screen (matches iOS onDisappear)
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stop()
        }
    }

    // Collect state from ViewModel
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val currentTimeMs by viewModel.currentTimeMs.collectAsStateWithLifecycle()
    val durationMs by viewModel.durationMs.collectAsStateWithLifecycle()
    val volume by viewModel.volume.collectAsStateWithLifecycle()
    val pitch by viewModel.pitch.collectAsStateWithLifecycle()
    val loopCount by viewModel.loopCount.collectAsStateWithLifecycle()
    val status by viewModel.status.collectAsStateWithLifecycle()
    val isLoaded by viewModel.isLoaded.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Playback",
            style = MaterialTheme.typography.titleMedium
        )

        Text(text = "Status: $status", style = MaterialTheme.typography.bodySmall)

        // Time display
        Text(
            text = "${PlaybackViewModel.formatTime(currentTimeMs)} / ${PlaybackViewModel.formatTime(durationMs)}",
            style = MaterialTheme.typography.bodyMedium
        )

        // Seek slider
        Slider(
            value = if (durationMs > 0) currentTimeMs.toFloat() / durationMs else 0f,
            onValueChange = { fraction -> viewModel.seek(fraction) },
            enabled = isLoaded,
            modifier = Modifier.fillMaxWidth()
        )

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

        // Volume control
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Volume:", modifier = Modifier.width(60.dp))
            Slider(
                value = volume,
                onValueChange = { viewModel.setVolume(it) },
                modifier = Modifier.weight(1f)
            )
            Text("${(volume * 100).toInt()}%", modifier = Modifier.width(40.dp))
        }

        // Pitch control - realtime updates
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Pitch:", modifier = Modifier.width(60.dp))
            Slider(
                value = (pitch + 12f) / 24f,
                onValueChange = {
                    val newPitch = (it * 24f) - 12f
                    viewModel.setPitch(newPitch)
                },
                modifier = Modifier.weight(1f)
            )
            Text("${pitch.toInt()} st", modifier = Modifier.width(40.dp))
        }

        // Loop count
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Loop:", modifier = Modifier.width(60.dp))
            listOf(1, 2, 3, -1).forEach { count ->
                OptionChip(
                    selected = loopCount == count,
                    onClick = { viewModel.setLoopCount(count) },
                    label = if (count == -1) "Inf" else count.toString()
                )
            }
        }

        // Fade controls
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.fadeIn() },
                enabled = isLoaded
            ) {
                Text("Fade In")
            }
            Button(
                onClick = { viewModel.fadeOut() },
                enabled = isLoaded
            ) {
                Text("Fade Out")
            }
        }
    }
}
