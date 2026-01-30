package com.musicmuni.voxatrace.demo.sections.recording.view

import android.content.Context
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
import com.musicmuni.voxatrace.demo.sections.recording.viewmodel.RecordingViewModel
import java.io.File

/**
 * Recording View - UI for audio recording demo.
 *
 * Demonstrates:
 * - SonixRecorder Builder pattern for advanced configuration
 * - Custom sample rate, channels, and bitrate
 * - Buffer Pool status monitoring (for Calibra integration)
 * - SonixPlayer for playback after recording
 */
@Composable
fun RecordingView(viewModel: RecordingViewModel = viewModel()) {
    val context = LocalContext.current

    // Stop recording/playback when leaving the screen (matches iOS onDisappear)
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopRecording()
            viewModel.stopPlayback()
        }
    }

    // Collect state from ViewModel
    val isRecording by viewModel.isRecording.collectAsStateWithLifecycle()
    val durationMs by viewModel.durationMs.collectAsStateWithLifecycle()
    val audioLevel by viewModel.audioLevel.collectAsStateWithLifecycle()
    val savedFilePath by viewModel.savedFilePath.collectAsStateWithLifecycle()
    val status by viewModel.status.collectAsStateWithLifecycle()
    val selectedFormat by viewModel.selectedFormat.collectAsStateWithLifecycle()
    val bufferPoolAvailable by viewModel.bufferPoolAvailable.collectAsStateWithLifecycle()
    val bufferPoolWasExhausted by viewModel.bufferPoolWasExhausted.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val playbackTimeMs by viewModel.playbackTimeMs.collectAsStateWithLifecycle()
    val playbackDurationMs by viewModel.playbackDurationMs.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Recording",
            style = MaterialTheme.typography.titleMedium
        )

        Text(text = "Status: $status", style = MaterialTheme.typography.bodySmall)

        // Format selection
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Format:", style = MaterialTheme.typography.bodySmall)
            viewModel.formats.forEach { format ->
                OptionChip(
                    selected = selectedFormat == format,
                    onClick = { viewModel.setSelectedFormat(format) },
                    label = format.uppercase(),
                    enabled = !isRecording
                )
            }
        }

        // Duration display
        Text(
            text = "Duration: ${RecordingViewModel.formatDuration(durationMs)}",
            style = MaterialTheme.typography.bodyMedium
        )

        // Audio level indicator
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Level:", style = MaterialTheme.typography.bodySmall)
            LinearProgressIndicator(
                progress = { audioLevel.coerceIn(0f, 1f) },
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp),
            )
        }

        // Buffer pool status (demonstrates zero-allocation processing for Calibra integration)
        if (isRecording) {
            Text(
                text = "Buffer Pool: $bufferPoolAvailable/4 available" +
                        if (bufferPoolWasExhausted) " (overflow occurred)" else " (zero-alloc)",
                style = MaterialTheme.typography.bodySmall,
                color = if (bufferPoolWasExhausted) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.primary
            )
        }

        // Control buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.startRecording(context) },
                enabled = !isRecording
            ) {
                Text("Record")
            }

            Button(
                onClick = { viewModel.stopRecording() },
                enabled = isRecording
            ) {
                Text("Stop & Save")
            }
        }

        // Show saved file info and playback controls (match iOS: show when savedFilePath exists and not recording)
        if (savedFilePath != null && !isRecording) {
            val file = File(savedFilePath!!)
            if (file.exists()) {
                Text(
                    text = "Saved: ${file.name} (${file.length() / 1024} KB)",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Playback time display
            if (playbackDurationMs > 0) {
                Text(
                    text = "Playback: ${RecordingViewModel.formatDuration(playbackTimeMs)} / ${RecordingViewModel.formatDuration(playbackDurationMs)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Playback controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.playRecording() },
                    enabled = !isRecording && !isPlaying && file.exists()
                ) {
                    Text("Play")
                }

                Button(
                    onClick = { viewModel.pausePlayback() },
                    enabled = isPlaying
                ) {
                    Text("Pause")
                }

                Button(
                    onClick = { viewModel.stopPlayback() },
                    enabled = playbackDurationMs > 0
                ) {
                    Text("Stop")
                }
            }
        }
    }
}
